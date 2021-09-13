<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 13:21:55
 * 123
-->
<template>
  <div>
    <div class="regionTop">
      <div class="regionTop__lineWidth">至少保留一个专区</div>
    </div>
    <div class="regionAdd">
      <el-button @click="addRegion" :disabled="showType">添加专区</el-button>
      <div class="regionAdd__tip">拖拽可调整专区顺序</div>
    </div>
    <div v-if="regionList">
      <ClassCom :regionList="regionList"></ClassCom>
    </div>
    <PageManage
      :pageSize="pageSize"
      :pageNum="pageNum"
      :total="total"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    ></PageManage>

    <el-dialog :visible.sync="showRegion" width="400px" :before-close="cancel">
      <span slot="title">
        <div class="dialogTitle">{{ addFlag ? "添加" : "编辑" }}专区</div>
      </span>
      <div class="dialogName">专区名称</div>
      <div v-for="(item, index) in addList" :key="index" class="dialogList">
        <div class="dialogList__item">
          <el-input v-model="item.modeName" style="width: 200px"></el-input>
          <el-button
            type="text"
            @click="deleteRegion(item, index)"
            v-if="index !== 0"
            >删除</el-button
          >
        </div>
      </div>
      <div>
        <el-button type="text" @click="textAdd" v-if="addFlag">添加</el-button>
      </div>
      <span slot="footer" class="dialog--footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="sureDeal">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog :visible.sync="deleteType" width="400px" :before-close="cancel">
      <div class="center">
        <ScanQrCode
          :redirectUrl="redirectUrl"
          scenes="AccountShopInfoCheck"
          :shopInfoId="shopInfoId"
        />
        <div style="width: 100%; margin-top: 20px" class="center">
          请管理员扫码
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import ClassCom from "./components/ClassCom.vue";
import PageManage from "@/components/PageManage.vue";
import ScanQrCode from "@/components/ScanQrCode.vue";
import { GoodRegionState } from "./goodRegion";
import {
  getRegionList,
  sortRegionList,
  editRegionList,
  addRegionList,
  delRegionItem,
} from "@/api/good/goods";
import { verifyStateResult } from "@/api/sign";
import { ApiSpecArea } from "../marketModel/marketType";

@Component({
  components: {
    ClassCom,
    PageManage,
    ScanQrCode,
  },
})
export default class GoodRegion extends Vue implements GoodRegionState {
  showRegion = false;

  addFlag = true;

  deleteType = false;

  pageSize = 10;

  pageNum = 1;

  total = 0;

  regionList: Array<ApiSpecArea> = [];

  redirectUrl = "";

  addList: Array<Partial<ApiSpecArea>> = [
    {
      modeName: "",
      id: null,
    },
  ];

  showType = false;

  shopInfoId = this.$STORE.userStore.userInfo.shopInfoVo.platformShopId;

  code = "";

  click = false;

  async mounted() {
    const id = this.getUrlParam("deleteId");
    try {
      if (this.$route.query.code) {
        this.code = this.$route.query.code as string;
        // 扫码时传过来的shopInfoId
        const shopInfoId = this.$route.query.shopInfoId;
        const userInfo = this.$STORE.userStore.userInfo;
        const isValid =
          userInfo && userInfo.shopInfoVo && userInfo.shopInfoVo.platformShopId;
        if (isValid && userInfo.shopInfoVo.platformShopId === shopInfoId) {
          history.pushState(
            {},
            "",
            `${location.href.replace(/&?code=.*&?$/, "")}`,
          );
          const response = await verifyStateResult({
            code: this.code,
          });
          const { code } = response;
          if (code === 200 && id) {
            this.regionDelete(id);
          }
        }
      } else {
        this.getInit();
      }
    } catch (e) {
      this.$message({
        message: e,
        type: "warning",
      });
      this.getInit();
    }
  }

  /**
   * 进入升级套餐页面
   */
  manageUpdate() {
    this.$router.push({
      name: "mealUpdate",
    });
  }

  getUrlParam(name: string) {
    const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
    const r = window.location.search.substr(1).match(reg); // 匹配目标参数
    if (r != null) {
      return unescape(r[2]);
    }
    return null; // 返回参数值
  }

  /**
   * 删除专区
   */
  regionDelete(id: string) {
    delRegionItem(id, {})
      .then(res => {
        this.$message.success("删除成功");
        this.getInit();
        console.log(res);
      })
      .catch(err => {
        this.$message.error(err || "网络错误");
        this.getInit();
      });
  }

  /**
   * 获得已有专区
   */
  getInit() {
    // const level = this.shopLevel;
    const param = {
      current: this.pageNum,
      size: this.pageSize,
    };
    getRegionList(param)
      .then(res => {
        this.regionList = res.data.list;
        this.total = res.data.total;
      })
      .catch(err => {
        this.$message.error(err || "网络错误");
      });
  }

  addRegion() {
    this.showRegion = true;
    this.addFlag = true;
  }

  /**
   * 排序专区
   */
  sort(list: ApiSpecArea[]) {
    list.forEach((item, index: number) => {
      item.sort = index;
    });
    sortRegionList(list)
      .then(() => {
        this.getInit();
      })
      .catch(err => {
        this.$message.error(err || "网络错误");
      });
  }

  /**
   * 删除专区
   */
  deleteRegion(_item: any, index: number) {
    this.addList.splice(index, 1);
  }

  /** 新增专区 */
  textAdd() {
    this.addList.push({
      modeName: "",
    });
  }

  cancel() {
    this.showRegion = false;
    this.deleteType = false;
    this.addList = [
      {
        modeName: "",
      },
    ];
  }

  /**
   * 子组件编辑操作
   */
  editFucntion(item: ApiSpecArea) {
    this.showRegion = true;
    this.addFlag = false;
    this.addList = [];
    this.addList.push(item);
  }

  /**
   * 确认操作
   */
  sureDeal() {
    const list = this.addList[0];
    if (this.click) {
      return;
    }
    if (!list.id) {
      this.addRegionList();
      return;
    }
    this.click = true;
    editRegionList(list)
      .then(res => {
        this.$message.success("设置成功");
        this.showRegion = false;
        this.getInit();
        console.log(res);
        this.click = false;
      })
      .catch(err => {
        this.$message.error(err || "网络错误");
        this.click = false;
      });
  }

  /**
   * 添加专区
   */
  addRegionList() {
    this.click = true;
    const list = this.addList;
    const nameType = list.some(item => {
      return item.modeName === "";
    });
    if (nameType) {
      this.$message.error("专区名称不能为空");
      return;
    }
    addRegionList(list)
      .then(() => {
        this.$message.success("设置成功");
        this.showRegion = false;
        this.getInit();
        this.click = false;
      })
      .catch(err => {
        this.$message.error(err || "网络错误");
        this.click = false;
      });
  }

  /**
   * 删除子组件专区
   */
  deleteRegionFunction(item: ApiSpecArea) {
    if (this.regionList.length <= 1) {
      this.$message.error("至少保留一个专区");
      return;
    }
    if (item.productNumber > 0) {
      this.$message.error("请将商品移至其它专区后，再进行删除操作");
      return;
    }
    this.redirectUrl = `/goodRegion?deleteId=${item.id}`;
    this.deleteType = true;
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val: number) {
    this.pageSize = val;
    this.getInit();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val: number) {
    this.pageNum = val;
    this.getInit();
  }
}
</script>

<style lang="scss" scoped>
@include b(regionTop) {
  width: 100%;
  height: 50px;
  background-color: #f2f2f2;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  padding: 10px 30px;
  color: #666666;
  font-size: 12px;

  @include e(lineWidth) {
    width: 100%;
    display: flex;
  }
}

@include b(regionAdd) {
  margin: 20px 0px;
  display: flex;
  justify-content: space-between;

  @include e(tip) {
    color: #666666;
    font-size: 14px;
  }
}

.dialogTitle {
  display: flex;
  justify-content: center;
  font-weight: bold;
  font-size: 16px;
}

.dialogName {
  width: 100%;
  height: 40px;
  display: flex;
  align-items: center;
  padding-left: 20px;
  background-color: #e9f3fb;
  font-size: 14px;
  font-weight: bold;
  margin-top: -20px;
}

@include b(dialogList) {
  margin-top: 20px;

  @include e(item) {
    display: flex;
    margin-bottom: 20px;
    justify-content: space-between;
    width: 300px;
  }
}

.center {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}
</style>
