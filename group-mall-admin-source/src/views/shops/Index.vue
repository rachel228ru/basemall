<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 15:01:46
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 17:17:23
-->
<template>
  <div class="shop">
    <div class="shop__header">
      <div class="shop__header--user">
        <span>欢迎 {{ userInfo.name }}</span>
      </div>
      <div class="shop__header--banner">
        <el-image
          class="banner"
          src="https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20210517/e5d9b381b6764afba4cc2e5b06012673.jpg"
          fit="cover"
        />
      </div>
    </div>
    <div class="shop__list" v-if="shopList.length > 0">
      <MContainer
        :size.sync="pageSize"
        :current.sync="pageNum"
        :pagination-visible="false"
        :total.sync="total"
      >
        <div class="shop__list--form" slot="form">
          <span>我的店铺</span>
          <el-button
            type="primary"
            class="shop__list--create"
            @click="routerLinkHandle"
          >
            +创建店铺
          </el-button>
        </div>
        <el-table
          :data="shopList"
          class="shop__list--content"
          :header-cell-style="{ backgroundColor: '#f8f8f8' }"
          slot="content"
          style="width: 100%"
        >
          <el-table-column prop="info" align="center" label="基本信息">
            <template slot-scope="scope">
              <div class="info">
                <el-image
                  class="info__img"
                  :src="scope.row.logoUrl"
                  fit="cover"
                >
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
                <div class="info__msg">
                  <div class="info__msg--name">
                    <el-button type="text">{{ scope.row.shopName }}</el-button>
                  </div>
                  <div class="info__msg--phone">
                    {{ scope.row.templateName }}
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="status" align="center" label="店铺状态">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 0" type="warning"
                >审核中</el-tag
              >
              <el-tag v-if="scope.row.status === 1">部署中</el-tag>
              <el-tag v-if="scope.row.status === 2" type="success">正常</el-tag>
              <span v-if="scope.row.status === 3">已打烊</span>
              <el-tag v-if="scope.row.status === 4" type="danger">禁用</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" align="center" label="创建时间">
          </el-table-column>
          <el-table-column prop="dueTime" align="center" label="剩余时长">
            <template slot-scope="scope">
              <div v-if="scope.row.isDue === 1" class="pointer">
                <div v-if="scope.row.orderSource === 2">
                  <span class="error">赠送时长已到期</span>
                </div>
                <div v-else>
                  <span class="error">已到期</span>
                </div>
                <div>
                  <el-button type="text" @click="orderHandle(scope.row)">
                    立即续费>
                  </el-button>
                </div>
              </div>
              <div v-if="scope.row.isDue === 0">
                <div>剩余{{ dueDay(scope.row.dueTime) }}天</div>
                <div>{{ scope.row.dueTime }}到期</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="scope">
              <set-drop
                setName="进入后台"
                :dropdownList="dropdownList(scope.row)"
                @setClick="itemClick(scope.row)"
                @command="itemCommand($event, scope.row)"
              />
            </template>
          </el-table-column>
        </el-table>
      </MContainer>
      <PageManage
        :pageSize="pageSize"
        :pageNum="pageNum"
        :total="total"
        @handleSizeChange="handleSizeChange"
        @handleCurrentChange="handleCurrentChange"
      />
    </div>
    <div class="shop__list" v-if="shopList.length === 0">
      <div class="myShop">我的店铺</div>
      <div class="shop__list--empty">
        <el-button type="primary" @click="routerLinkHandle">
          +创建店铺
        </el-button>
        <div class="mt15">
          你还没有运行中的店铺，现在就去<span class="primary"
            >新建一个吧！</span
          >
        </div>
      </div>
    </div>
    <el-dialog :visible.sync="dialogVisible" width="30%">
      <ScanQrCode
        :redirectUrl="`${publicPath}?shopInfoId=${shopInfoId}`"
        :shopInfoId="shopInfoId"
        scenes="AccountShopInfoCheck"
      />
    </el-dialog>
    <ChangePayment :visible.sync="paymentVisible"></ChangePayment>
  </div>
</template>

<script lang="ts">
import { Component } from "vue-property-decorator";
import { mixins } from "vue-class-component";
import DialogMixin, { login } from "@/libs/index";
import MContainer from "@/components/addShopMM/container/MContainer.vue";
import PageManage from "@/components/PageManage.vue";
import ChangePayment from "@/views/shops/ChangePayment.vue";
import SetDrop from "@/components/SetDrop.vue";
import ScanQrCode from "@/components/ScanQrCode.vue";
import {
  getShopList,
  enterShop,
  closeOrOpenShop,
  deleteShop,
} from "@/api/shop";
import { verifyStateResult, getAccountInfo } from "@/api/sign";
import moment from "moment";
import storage from "@/libs/storage";
import { getShopInfo } from "@/api/businessCenter/setting";

@Component({
  components: {
    MContainer,
    SetDrop,
    ScanQrCode,
    PageManage,
    ChangePayment,
  },
})
export default class Shops extends mixins(DialogMixin) {
  static componentName = "Shop";

  shopList: any = [];

  dialogVisible = false;

  paymentVisible = false;

  shopInfoId = 0;

  publicPath = process.env.VUE_APP_PUBLICPATH;

  orderBy = 2;

  /** 分页条数 */
  pageSize = 10;

  /** 分页页码 */
  pageNum = 1;

  /** 数据长度 */
  total = 0;

  get dueDay() {
    return (dueTime: moment.MomentInput) => {
      let result;
      const dValue = moment(dueTime).valueOf() - new Date().getTime();
      if (dValue > 0) {
        result = Math.floor(dValue / 1000 / 60 / 60 / 24);
      } else {
        result = 0;
      }
      return result;
    };
  }

  /** 用户数据 */
  userInfo = {
    name: this.$STORE.userStore.userInfo
      ? this.$STORE.userStore.userInfo.nikeName
      : "",
    avatar: this.$STORE.userStore.userInfo
      ? this.$STORE.userStore.userInfo.avatarUrl
      : "",
  };

  async created() {
    if (this.$route.query.status === "success") {
      this.$message({
        message: "套餐操作成功",
        type: "success",
      });
      await this.$router.push({
        path: process.env.VUE_APP_PUBLICPATH,
      });
    }
    await this.getDataList();
    if (this.$route.query.code && this.$route.query.shopInfoId) {
      const cd = this.$route.query.code;
      history.pushState({}, "", "/");
      const response = await verifyStateResult({
        code: cd,
      });
      const { code } = response;
      if (code === 200) {
        await this.deleteShopById(Number(this.$route.query.shopInfoId));
      }
    }
    if (!storage.get("token")) {
      debugger;
      open(`${process.env.VUE_APP_DOMAIN}/login`, "_top");
    }
  }

  /**
   * 获取店铺列表
   */
  async getDataList() {
    try {
      const form = {
        page: this.pageNum,
        size: this.pageSize,
        orderBy: this.orderBy,
      };
      const response = await getShopList(form);
      const { code, data } = response;
      if (code === 200) {
        this.shopList = data.list || [];
        this.total = data.total;
      }
    } catch (e) {
      this.$message({
        message: e,
        type: "warning",
      });
    }
  }

  routerLinkHandle() {
    this.$router.push("/console/create");
  }

  /**
   * 删除店铺
   */
  async deleteShopById(shopId: number) {
    try {
      const response = await deleteShop(shopId);
      const { code } = response;
      if (code === 200) {
        this.$message({
          type: "success",
          message: "删除成功!",
        });
        await this.getDataList();
      }
    } catch (e) {
      console.log(e);
    }
  }

  // 指令下拉列表
  get dropdownList() {
    return (row: { status: number; isDue: number; orderSource: number }) => {
      /** 操作项下拉菜单 */
      return [
        {
          command: "营业",
          disabled: false,
          show: row.status === 3 || row.status === 4,
          text: "营业",
        },
        {
          command: "打烊",
          disabled: false,
          show: row.status === 2 || row.status === 1,
          text: "打烊",
        },
        {
          command: "订购",
          disabled: false,
          show: row.isDue === 1 || row.orderSource === 2,
          text: "订购",
        },
        {
          command: "切换支付服务商",
          disabled: false,
          show: true,
          text: "切换支付服务商",
        },
      ];
    };
  }

  async itemClick(row: { status: number; id: number }) {
    const statusList = ["审核中", "部署中", "正常", "已打烊", "禁用"];
    if (row.status !== 2 && row.status !== 3) {
      this.$message({
        type: "warning",
        message: `当前店铺${statusList[row.status]}无法进入后台`,
      });
      return;
    }
    try {
      const response = await enterShop(row.id);
      const { code, data } = response;
      const userInfo = await this.getRecentAccountInfo();
      userInfo.shopInfoVo = data;
      // 更新最新的店铺信息
      await getShopInfo();
      if (code === 200 && data.backUrl && userInfo.token) {
        login(userInfo.token, userInfo);
        this.$router.push("/overview");
      }
    } catch (e) {
      this.$message({
        type: "success",
        message: e,
      });
    }
  }

  async getRecentAccountInfo() {
    if (storage.get("token")) {
      const response = await getAccountInfo();
      const { code, data } = response;
      if (code === 200) {
        return data;
      } else {
        return {};
      }
    } else {
      return {};
    }
  }

  itemCommand(command: any, row: { status: number; id: number }) {
    switch (command) {
      case "连接":
        this.$confirm("确认连接该店铺？连接成功后店铺正常营业？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            this.getDataList();
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
        break;
      case "营业":
        // 店铺禁用不允许营业
        if (row.status === 4) {
          this.$message({
            type: "warning",
            message: "店铺已禁用，请联系客服!",
          });
          break;
        }
        this.$confirm(
          "开启后商家将正常进入后台，该店铺将正常运行使用，确定开启营业吗？",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          },
        )
          .then(async () => {
            const response = await closeOrOpenShop(row.id);
            const { code } = response;
            if (code === 200) {
              this.$message({
                type: "success",
                message: "开启成功!",
              });
              await this.getDataList();
            }
          })
          .catch(e => {
            console.log(e);
            this.$message({
              type: "warning",
              message: e,
            });
          });
        break;
      case "打烊":
        try {
          closeOrOpenShop(row.id).then(data => {
            const { code } = data;
            if (code === 200) {
              this.$message({
                type: "success",
                message: "打烊成功!",
              });
              this.getDataList();
            }
          });
        } catch (e) {
          console.log(e);
        }
        break;
      case "订购":
        this.orderHandle(row);
        break;
      case "切换支付服务商":
        this.paymentVisible = true;
        break;
      case "续费升级":
        this.renewHandle(row);
        break;
      case "删除":
        this.shopInfoId = row.id;
        this.dialogVisible = true;
        break;
    }
  }

  /**
   * 店铺订购
   */
  orderHandle(row: any) {
    this.$router.push({
      path: process.env.VUE_APP_PUBLICPATH + "/meal",
      query: { shopInfo: JSON.stringify(row) },
    });
  }

  /**
   * 店铺续费
   */
  renewHandle(row: any) {
    this.$router.push({
      path: process.env.VUE_APP_PUBLICPATH + "/meal/update",
      query: { shopInfo: JSON.stringify(row) },
    });
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val: number) {
    this.pageSize = val;
    this.getDataList();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val: number) {
    this.pageNum = val;
    this.getDataList();
  }
}
</script>

<style lang="scss" scoped>
@import "src/assets/styles/shops/shops.scss";
</style>
