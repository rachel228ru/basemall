<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:58:11
-->
<template>
  <div>
    <el-button type="primary" @click="addNew">添加地址</el-button>
    <div class="addressBox">
      <div class="addressBox__top">
        <span style="width: 15%">联系人</span>
        <span style="width: 31%">地址</span>
        <span style="width: 14%">邮政编码</span>
        <span style="width: 18%">联系电话</span>
        <span style="width: 20%">默认设置</span>
        <span style="width: 10%">操作</span>
      </div>
      <div v-if="addList.length !== 0">
        <div
          v-for="(item, index) in addList"
          :key="index"
          class="addressBox__content"
          :style="{ 'background-color': index % 2 === 0 ? '#F2F2F6' : '' }"
        >
          <div style="width: 20%; margin-right: 20px">
            <div>{{ item.name }}</div>
          </div>
          <div style="width: 60%; margin-right: 20px">
            <div>
              {{ item.province }}{{ item.city }}{{ item.country
              }}{{ item.address }}
            </div>
          </div>
          <div style="width: 20%; margin-right: 20px">
            <div>{{ item.zipCode }}</div>
          </div>
          <div style="width: 30%; margin-right: 20px">
            <div>{{ item.phone }}</div>
          </div>
          <div :style="{ width: isAdd ? '20%' : '30%' }">
            <div
              style="display: flex; width: 80px; flex-wrap: wrap"
              v-if="!isAdd"
            >
              <div
                class="addressBox__content--botton"
                :style="{
                  'background-color':
                    item.defSend === 1 ? '#2D8CF0' : '#FFFFFF',
                  color: item.defSend === 1 ? 'white' : 'black',
                }"
                @click="setSendArea(item, 1)"
              >
                发货地址
              </div>
              <div
                class="addressBox__content--botton"
                style="margin-top: 5px"
                :style="{
                  'background-color':
                    item.defReceive === 1 ? '#2D8CF0' : '#FFFFFF',
                  color: item.defReceive === 1 ? 'white' : 'black',
                }"
                @click="setSendArea(item, 2)"
              >
                退货地址
              </div>
            </div>
          </div>
          <div class="doDeal">
            <div
              style="
                width: 20%;
                color: #409eff;
                margin-right: 10px;
                cursor: pointer;
              "
              @click="editAddress(item)"
            >
              编辑
            </div>
            <div
              style="width: 20%; color: rgb(250, 100, 101); cursor: pointer"
              @click="delAddress(item)"
            >
              删除
            </div>
          </div>
        </div>
      </div>
      <div class="emptyLine" v-if="hasList">暂无数据~</div>
    </div>
    <el-dialog
      :visible.sync="addDialog"
      width="580px"
      :before-close="handleClose"
    >
      <div slot="title" class="digTitle">
        {{ currentItem.id ? "编辑" : "新增" }}地址
      </div>
      <AddModel
        ref="addModel"
        :currentItem="currentItem"
        :addDialog="addDialog"
      ></AddModel>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="sureAdd">确认</el-button>
      </span>
    </el-dialog>
    <PageManage
      :pageSize="query.size"
      :pageNum="query.current"
      :total="query.total"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    ></PageManage>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import { ApiAddressType } from "../../logisticType";
import AddModel from "./edit/AddModel.vue";
import PageManage from "@/components/PageManage.vue";

import {
  getShopAddress,
  addShopAddress,
  delShopAddress,
  setShopAddress,
} from "@/api/logistics/logistics";

@Component({
  components: {
    AddModel,
    PageManage,
  },
})
export default class AddressProgram extends Vue {
  @Ref()
  readonly addModel!: AddModel;

  /** 地址管理 */
  addList: Array<ApiAddressType> = [];

  /** list是否为空标识 */
  hasList = false;

  /** 是否添加状态 */
  isAdd = false;

  /** 添加弹窗 */
  addDialog = false;

  /** 编辑内容 */
  currentItem = {} as Partial<ApiAddressType>;

  /** 分页信息 */
  query = {
    current: 1,
    size: 10,
    total: 0,
  };

  mounted() {
    this.getList();
  }

  /**
   * 获取地址管理数据
   */
  getList() {
    getShopAddress(this.query).then(res => {
      res.data.list.forEach((item: ApiAddressType) => {
        item.isShow = true;
      });
      this.addList = res.data.list;
      this.hasList = res.data.list.length === 0 ? true : false;
      this.query.total = res.data.total;
    });
  }

  addNew() {
    this.addDialog = true;
    this.currentItem = {};
  }

  /**
   * 添加地址
   */
  sureAdd() {
    const passFlag = this.saveAddress(this.addModel);
    if (!passFlag) {
      return;
    }
    this.addModel.addressOption.countryId = this.addModel.countryId;
    addShopAddress(this.addModel.addressOption)
      .then(() => {
        this.$message.success(
          `${this.addModel.addressOption.id ? "修改" : " 新增"}成功`,
        );
        this.getList();
        this.addDialog = false;
        this.currentItem = {};
      })
      .catch(err => {
        this.$message.error(err);
      });
  }

  handleClose() {
    this.currentItem = {};
    this.addDialog = false;
  }

  /**
   * 保存地址
   */
  saveAddress(addModel: AddModel) {
    const postReg = /^[0-9][0-9]{5}$/;
    const phoneReg = /^1(3|4|5|6|7|8|9)\d{9}$/;
    const passFlag = true;

    if (!addModel.addressOption.name) {
      this.$message.error("请输入联系人");
      return false;
    }

    if (
      addModel.addressOption.phone &&
      !phoneReg.test(addModel.addressOption.phone)
    ) {
      this.$message.error("请输入正确手机号码");
      return false;
    }

    if (
      addModel.addressOption.zipCode &&
      !postReg.test(addModel.addressOption.zipCode)
    ) {
      this.$message.error("请输入正确邮政编码");
      return false;
    }

    if (
      !addModel.addressOption.cityId ||
      !addModel.countryId ||
      !addModel.addressOption.provinceId
    ) {
      this.$message.error("请输入地区信息");
      return false;
    }

    if (!addModel.addressOption.address) {
      this.$message.error("请输入详细地址");
      return false;
    }
    return passFlag;
  }

  /**
   * 设置收货退货地址
   */
  setSendArea(item: ApiAddressType, type: number) {
    setShopAddress(item.id, type, {}).then(() => {
      this.$message.success("设置成功");
      this.getList();
    });
  }

  /**
   * 编辑地址
   */
  editAddress(item: ApiAddressType) {
    this.currentItem = item;
    this.addDialog = true;
  }

  /**
   * 删除地址
   */
  delAddress(item: ApiAddressType) {
    this.$confirm("确定删除此条地址？删除后不会保留已删除地址?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      delShopAddress(item.id, {})
        .then(() => {
          this.$message.success("删除成功");
          this.getList();
        })
        .catch(err => {
          this.$message.error(err || "网络异常，请稍后重试");
        });
    });
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val: number) {
    this.query.size = val;
    this.getList();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val: number) {
    this.query.current = val;
    this.getList();
  }
}
</script>

<style lang="scss" scoped>
@include b(addressBox) {
  margin-top: 20px;
  width: 100%;
  border: 1px solid #c9c9c9;
  color: #595961;
  border-radius: 5px;
  @include e(top) {
    display: flex;
    padding: 10px;
    padding-left: 40px;
    font-size: 12px;
  }
  @include e(content) {
    display: flex;
    padding: 10px;
    padding-left: 40px;
    border-top: 1px solid #c9c9c9;
    height: 80px;
    align-items: center;
    font-size: 12px;
    @include m(botton) {
      width: 80px;
      height: 24px;
      display: flex;
      border-radius: 5px;
      justify-content: center;
      align-items: center;
      border: 1px solid #c9c9c9;
      cursor: pointer;
    }
  }
  @include e(addButton) {
    display: flex;
    padding: 10px;
    padding-left: 40px;
    height: 80px;
    align-items: center;
  }
}

.doDeal {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30%;
}

.digTitle {
  font-size: 15px;
  font-weight: bold;
}

.emptyLine {
  width: 100%;
  height: 80px;
  background-color: white;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
  margin-top: 15px;
  margin-left: 0;
  font-size: 14px;
  color: #b3b3b3;
  border-top: 1px solid #ebeef5;
}
</style>
