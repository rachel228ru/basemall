<template>
  <div>
    <table>
      <thead>
        <tr>
          <th style="width: 90px;">消息类别</th>
          <th>消息标题</th>
          <th>模板ID</th>
          <th>推送规则</th>
          <th>操作</th>
        </tr>
      </thead>
      <template v-for="supItem in dataList">
        <tr
          v-for="(subItem, subIndex) in supItem.miniSubscriberiMsgVos"
          class="message__tr"
        >
          <td
            v-if="subIndex === 0"
            :rowspan="supItem.miniSubscriberiMsgVos.length"
            class="message__first__tr"
          >
            <span v-if="supItem.messageType === 1">订单消息</span>
            <span v-if="supItem.messageType === 2">售后消息</span>
            <span v-if="supItem.messageType === 3">用户消息</span>
            <span v-if="supItem.messageType === 4">营销活动</span>
          </td>
          <td>{{ subItem.msgTitle }}</td>
          <td>{{ subItem.templateId }}</td>
          <td>{{ subItem.sendRule }}</td>
          <td class="message__useStatus">
            <el-switch
              v-model="subItem.useStatus"
              @change="useStatusChange(subItem.id, $event)"
              style="margin-right: 15px;"
              active-color="#409EFF"
              inactive-color="#909399"
              :active-value="1"
              :inactive-value="0"
            >
            </el-switch>
            <el-popover trigger="click">
              <el-card shadow="never" class="message__card">
                <div slot="header" class="message__clearfix">
                  <div class="message__card--title">
                    {{ exampleJson(subItem.exampleJson).title }}
                  </div>
                  <div v-for="item in exampleJson(subItem.exampleJson).items">
                    <span>{{ item.k }}：</span>
                    <span>{{ item.v }}</span>
                  </div>
                </div>
                <div class="message__card--detail">
                  <span>详情</span>
                  <i class="el-icon-arrow-right"></i>
                </div>
              </el-card>
              <el-button slot="reference" type="text">预览</el-button>
            </el-popover>
          </td>
        </tr>
      </template>
    </table>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import { getSubMessage, updateMerchantState } from "@/api/message/message";

@Component
export default class App extends Vue {
  dataList = [];

  created() {
    this.getDataList();
  }

  get exampleJson() {
    return exampleJson => {
      return JSON.parse(exampleJson);
    };
  }

  async getDataList() {
    try {
      const response = await getSubMessage(2);
      const { data } = response;
      this.dataList = data;
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  async useStatusChange(id, useStatus) {
    try {
      await updateMerchantState(id, {
        useStatus,
      });
      await this.getDataList();
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }
}
</script>

<style lang="scss" scoped></style>
