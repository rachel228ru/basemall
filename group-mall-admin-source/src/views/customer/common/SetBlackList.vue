<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:28:49
-->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :before-close="closeHandle"
    class="dialog"
    title="黑名单限制行为"
  >
    <div class="container">
      <el-checkbox-group v-model="dataForm.rejectInteger">
        <el-checkbox :label="1">下单</el-checkbox>
        <el-checkbox :label="2">评论</el-checkbox>
      </el-checkbox-group>
    </div>
    <span slot="footer" class="dialog--footer">
      <el-button @click="closeHandle">取 消</el-button>
      <el-button type="primary" @click="submitDataForm">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import { ApiCustomerList, CustomerTagList } from "../list/customerListType";
import { batchSetBlackList } from "@/api/customer/customer";
export interface NewCustomerTagList extends CustomerTagList {
  blacklistType: number[];
}
@Component
export default class BlackList extends Vue {
  dataForm = {
    option: 0,
    rejectInteger: [] as Array<number>,
    userIds: [] as Array<string | number>,
  };

  dialogVisible = false;

  init(multipleSelection: ApiCustomerList[], option: number) {
    this.initData();
    const form = this.dataForm;
    form.option = option;
    form.userIds = multipleSelection.map(item => item.userId);
    if (multipleSelection.length === 1 && multipleSelection[0].blacklistType) {
      form.rejectInteger = multipleSelection[0].blacklistType;
    }
    this.dialogVisible = true;
  }

  initData() {
    this.dataForm = {
      option: 0,
      rejectInteger: [],
      userIds: [],
    };
  }

  submitDataForm() {
    const form = this.dataForm;
    if (form.rejectInteger.length === 0) {
      this.$message({
        message: "至少选择一个限制行为",
        type: "warning",
      });
      return;
    }
    batchSetBlackList(form).then(() => {
      this.$message({
        message: `已成功${form.option === 1 ? "加入" : "修改"}黑名单`,
        type: "success",
      });
      this.$emit("refreshDataList", 1);
      this.dialogVisible = false;
    });
  }

  // 关闭弹窗
  closeHandle(done: () => any) {
    this.$confirm(
      "确定退出加入黑名单页面，退出后，未保存的信息将不再保留！",
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      },
    )
      .then(() => {
        done instanceof Function ? done() : (this.dialogVisible = false);
      })
      .catch(err => {
        console.log(err, "err");
      });
  }
}
</script>

<style scoped lang="scss">
.container {
  padding: 25px;

  .el-button {
    margin: 30px 0px;
  }
}

.dialog--footer {
  text-align: center;
}
</style>
