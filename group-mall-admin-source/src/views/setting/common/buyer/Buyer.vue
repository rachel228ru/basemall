<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:22:19
-->
<template>
  <div>
    <table>
      <thead>
        <tr>
          <th style="width: 90px">消息类别</th>
          <th>消息标题</th>
          <th>小程序模板消息</th>
          <th>短信通知</th>
          <th>公众号模板消息</th>
        </tr>
      </thead>
      <template v-for="supItem in dataList">
        <tr
          v-for="(subItem, subIndex) in supItem.shopMessageDetailVos"
          :key="subItem.id"
          class="message__tr"
        >
          <td
            v-if="subIndex === 0"
            :rowspan="supItem.shopMessageDetailVos.length"
            class="message__first__tr"
          >
            <span>{{ supItem.msgTitle }}</span>
          </td>
          <td>{{ subItem.title }}</td>
          <td>
            <div class="message__useStatus">
              <el-switch
                v-model="subItem.miniOpen"
                @change="useStatusChange(subItem, 'miniOpen', $event)"
                style="margin-right: 15px"
                active-color="#409EFF"
                inactive-color="#909399"
                :active-value="1"
                :inactive-value="0"
              >
              </el-switch>
              <el-popover trigger="click">
                <el-card
                  shadow="never"
                  class="message__card"
                  v-if="subItem.miniMsg"
                >
                  <div slot="header" class="message__clearfix">
                    <div class="message__card--title">
                      {{ subItem.miniMsg.exampleJson.title }}
                    </div>
                    <div
                      v-for="item in subItem.miniMsg.exampleJson.items"
                      :key="item.id"
                    >
                      <span>{{ item.k }}：</span>
                      <span>{{ item.v }}</span>
                    </div>
                  </div>
                </el-card>
                <el-button slot="reference" type="text">预览</el-button>
              </el-popover>
            </div>
          </td>
          <td class="flex__center">
            <div class="message__useStatus">
              <el-switch
                v-model="subItem.codeOpen"
                disabled
                @change="useStatusChange(subItem, 'codeOpen', $event)"
                style="margin-right: 15px"
                active-color="#409EFF"
                inactive-color="#909399"
                :active-value="1"
                :inactive-value="0"
              >
              </el-switch>
              <el-popover trigger="click">
                <el-card
                  shadow="never"
                  class="message__card"
                  v-if="subItem.codeMsg"
                >
                  <div slot="header" class="message__clearfix">
                    <div class="message__card--title">
                      {{ subItem.codeMsg.exampleJson.title }}
                    </div>
                    <div
                      v-for="item in subItem.codeMsg.exampleJson.items"
                      :key="item.id"
                    >
                      <span>{{ item.k }}：</span>
                      <span>{{ item.v }}</span>
                    </div>
                  </div>
                </el-card>
                <el-card v-else>
                  <div class="message__card--title">暂无消息模板</div>
                </el-card>
                <el-button slot="reference" type="text">预览</el-button>
              </el-popover>
            </div>
          </td>
          <td class="flex__center">
            <div class="message__useStatus">
              <el-switch
                v-model="subItem.mpOpen"
                disabled
                @change="useStatusChange(subItem, 'mpOpen', $event)"
                style="margin-right: 15px"
                active-color="#409EFF"
                inactive-color="#909399"
                :active-value="1"
                :inactive-value="0"
              >
              </el-switch>
              <el-popover trigger="click">
                <el-card
                  shadow="never"
                  class="message__card"
                  v-if="subItem.mpMsg && subItem.mpMsg !== '{}'"
                >
                  <div slot="header" class="message__clearfix">
                    <div class="message__card--title">
                      {{ subItem.mpMsg.exampleJson.title }}
                    </div>
                    <div
                      v-for="item in subItem.mpMsg.exampleJson.items"
                      :key="item.id"
                    >
                      <span>{{ item.k }}：</span>
                      <span>{{ item.v }}</span>
                    </div>
                  </div>
                </el-card>
                <el-button slot="reference" type="text">预览</el-button>
              </el-popover>
            </div>
          </td>
        </tr>
      </template>
    </table>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import { getBuyerMessage, updateMessageState } from "@/api/message/message";

interface InitDataList {
  msgTitle: string;
  shopMessageDetailVos: Array<shopMessageDetailType>;
}

interface shopMessageDetailType {
  codeMsg: string;
  codeOpen: number;
  id: number;
  miniMsg: string;
  miniOpen: number;
  mpMsg: string;
  mpOpen: number;
  title: string;
}

@Component
export default class App extends Vue {
  initData: Array<InitDataList> = [];

  dataList: Array<InitDataList> = [];

  created() {
    this.getDataList();
  }

  async getDataList() {
    try {
      const response = await getBuyerMessage();
      const { data } = response;
      this.initData = data;
      const dataList = JSON.parse(JSON.stringify(data)) as Array<InitDataList>;
      this.dataList = dataList.map(supItem => {
        if (
          supItem.shopMessageDetailVos &&
          supItem.shopMessageDetailVos.length > 0
        ) {
          supItem.shopMessageDetailVos = supItem.shopMessageDetailVos.map(
            subItem => {
              if (subItem.codeMsg && typeof subItem.codeMsg === "string") {
                subItem.codeMsg = JSON.parse(subItem.codeMsg);
              }
              if (subItem.miniMsg && typeof subItem.miniMsg === "string") {
                subItem.miniMsg = JSON.parse(subItem.miniMsg);
              }
              if (
                subItem.mpMsg &&
                typeof subItem.mpMsg === "string" &&
                subItem.mpMsg !== "{}"
              ) {
                subItem.mpMsg = JSON.parse(subItem.mpMsg);
              }
              return subItem;
            },
          );
        }
        return supItem;
      });
    } catch (e) {
      console.log(e);
    }
  }

  async useStatusChange(
    subItem: shopMessageDetailType,
    name: "mpOpen" | "miniOpen" | "codeOpen",
    value: number,
  ) {
    try {
      const form = {
        codeOpen: subItem.codeOpen,
        id: subItem.id,
        miniOpen: subItem.miniOpen,
        mpOpen: subItem.mpOpen,
      };
      form[name] = value;
      await updateMessageState(form);
      await this.getDataList();
    } catch (e) {
      subItem[name] = value ? 0 : 1;
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }
}
</script>

<style lang="scss" scoped></style>
