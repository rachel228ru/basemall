<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:37:30
-->
<template>
  <div>
    <div class="printTop">
      <div class="printTop__icon"></div>
      <div class="printTop__value">电子面单</div>
      <div class="printTop__desc">用于打印物流发货单</div>
    </div>
    <div class="sheet">
      <el-button type="primary" @click="addSheetNew()">新增设备</el-button>
      <div class="sheet__top">
        <div style="width: 200px" class="center">设备类型</div>
        <div style="width: 300px" class="center">设备备注名称</div>
        <div style="width: 300px" class="center">连接状态</div>
        <div style="width: 130px" class="center">操作</div>
      </div>
      <div v-for="(item, index) in sheetList" :key="index" class="sheet__list">
        <div class="sheet__list--type">{{ item.deviceType }}</div>
        <div class="sheet__list--model">{{ item.deviceName }}</div>
        <div class="sheet__list--link">
          {{ item.status === "1" ? "已连接" : "未连接" }}
        </div>
        <div class="sheet__list--deal">
          <div
            class="sheet__list--deal--noDel"
            @click="linkDel(item.id, item.status)"
          >
            {{ item.status === "1" ? "断开" : "连接" }}
          </div>
          <div class="sheet__list--deal--noDel" @click="editSheet(item)">
            编辑
          </div>
          <div class="sheet__list--deal--del" @click="delSheet(item.id)">
            删除
          </div>
        </div>
      </div>
      <div class="emptyLine" v-if="sheetList.length === 0">
        暂无数据~
      </div>
    </div>
    <el-dialog
      :visible.sync="addDialog"
      width="500px"
      :before-close="handleClose"
    >
      <div slot="title" class="digTitle">新增设备</div>
      <AddSheetModel
        ref="addSheetModel"
        :editCurrent="editCurrent"
        :addDialog="addDialog"
      ></AddSheetModel>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="saveSheet">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import AddSheetModel from "../common/AddSheetModel.vue";
import AddPrintModel from "../common/AddPrintModel.vue";
import {
  addSheetMach,
  getSheetList,
  sheetLink,
  delSheetMach,
} from "@/api/logistics/logistics";
import { LogisticsPrinterVos } from "@/views/marketing/integralMall/integralOrder/components/product/productTableType";

@Component({
  components: {
    AddSheetModel,
    AddPrintModel,
  },
})
export default class PrintSetting extends Vue {
  @Ref()
  readonly addSheetModel!: AddSheetModel;

  @Ref()
  readonly addPrintModel!: AddPrintModel;

  /** 电子面单数组 */
  sheetList: Array<LogisticsPrinterVos> = [];

  /** 小票打印数组 */
  receiptList = [
    {
      id: "0",
      type: "打印机",
      xinghao: "备注名称/热敏条码云打印机 365M2",
      yici: "1",
      lianjie: "0",
      linkType: "0",
      setType: "0",
    },
    {
      id: "1",
      type: "打印机",
      xinghao: "备注名称/映美标签打印机",
      yici: "2",
      lianjie: "1",
      linkType: "1",
      setType: "1",
    },
  ];

  /** 新增打印机 */
  addDialog = false;

  /** 新增小票打印 */
  addPrintDialog = false;

  editCurrent = {};

  mounted() {
    this.getSheet();
  }

  /**
   * 获取电子面单列表
   */
  getSheet() {
    getSheetList({ currnt: 1, size: 10 }).then(res => {
      this.sheetList = res.data.list;
    });
  }

  /**
   * 新增电子面单
   */
  addSheetNew() {
    this.addDialog = true;
    this.editCurrent = {};
  }

  /**
   * 电子面单打印机保存
   */
  async saveSheet() {
    await this.getSheetFormModel();
    const param = this.addSheetModel.sheetForm;
    addSheetMach(param).then(() => {
      this.addDialog = false;
      this.$message.success("保存成功");
      this.getSheet();
    });
  }

  async getSheetFormModel() {
    try {
      await this.addSheetModel.validate();
      return;
    } catch (error) {
      return Promise.reject(error);
    }
  }

  /**
   * 启用/停用电子面单打印机
   */
  linkDel(id: number, status: string) {
    sheetLink(id, status === "1" ? "0" : "1", {}).then(() => {
      this.$message.success("设置成功");
      this.getSheet();
    });
  }

  /**
   * 编辑电子面单
   */
  editSheet(item: LogisticsPrinterVos) {
    this.addDialog = true;
    this.editCurrent = item;
  }

  /**
   * 删除电子面单打印机
   */
  delSheet(id: number) {
    this.$confirm("确定删除与打印机的连接吗?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      delSheetMach(id, {}).then(() => {
        this.$message.success("删除成功");
        this.getSheet();
      });
    });
  }

  /**
   * 关闭新增打印机弹窗
   */
  handleClose() {
    this.addDialog = false;
    this.addSheetModel.clearValidate();
  }

  /**
   * 小票打印保存
   */
  savePrint() {
    this.addPrintModel.validate();
  }

  /**
   * 关闭新增小票打印
   */
  handlePrintClose() {
    this.addPrintDialog = false;
  }
}
</script>

<style lang="scss" scoped>
@include b(printTop) {
  width: 100%;
  display: flex;
  align-items: center;
  background-color: #f6f8fa;
  color: #586884;
  font-size: 14px;
  padding: 15px 15px 15px 30px;
  @include e(icon) {
    width: 3px;
    height: 12px;
    background-color: #199ed8;
    margin-right: 10px;
  }
  @include e(value) {
    color: #586884;
    font-weight: 700;
    font-size: 13px;
  }
  @include e(desc) {
    margin-left: 20px;
    color: #515a6e;
    font-size: 12px;
  }
}

.center {
  display: flex;
  // justify-content: center;
  justify-content: flex-start;
}

@include b(sheet) {
  padding: 20px;
  @include e(top) {
    margin-top: 10px;
    padding-left: 10px;
    width: 100%;
    height: 40px;
    display: flex;
    align-items: center;
    background-color: #f6f8fa;
    color: #586884;
    font-size: 14px;
  }
  @include e(list) {
    display: flex;
    height: 60px;
    @include m(type) {
      width: 200px;
      display: flex;
      padding-left: 10px;
      justify-content: flex-start;
      align-items: center;
    }
    @include m(model) {
      width: 300px;
      display: flex;
      padding-left: 10px;
      justify-content: flex-start;
      align-items: center;
    }
    @include m(link) {
      width: 300px;
      display: flex;
      padding-left: 10px;
      justify-content: flex-start;
      align-items: center;
    }
    @include m(deal) {
      width: 130px;
      display: flex;
      justify-content: space-around;
      align-items: center;

      @include m(noDel) {
        color: #6eb5ff;
        cursor: pointer;
      }

      @include m(del) {
        color: #ff5500;
        cursor: pointer;
      }
    }
  }
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
  font-size: 14px;
  color: #b3b3b3;
  border-bottom: 1px solid #ebeef5;
}
</style>
