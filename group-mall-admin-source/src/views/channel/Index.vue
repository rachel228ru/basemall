<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:28:03
-->
<template>
  <div class="channel">
    <el-tabs
      v-model="activeName"
      @tab-click="tabClickHandle"
      class="channel-tabs"
    >
      <el-tab-pane label="微信小程序" name="App" class="App-tab"></el-tab-pane>
      <el-tab-pane
        label="微信公众号"
        name="Public"
        class="Public-tab"
      ></el-tab-pane>
    </el-tabs>
    <div class="message__info">
      <p>使用须知:</p>
      <p>
        1、使用商家需注意换绑不同的小程序后，之前客户数据都会受到影响，请谨慎操作；
      </p>
      <p>
        2、公众号和微信打通需同一主体才可正常使用；
      </p>
    </div>
    <component :is="activeName" ref="componentRef" />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import Public from "@/views/channel/pulic/Public.vue";
import App from "@/views/channel/app/App.vue";

@Component({
  components: {
    Public,
    App,
  },
})
export default class Basics extends Vue {
  @Ref() readonly componentRef!: HTMLElement;

  activeName = "App";

  created() {
    if (this.$route.query.tabName) {
      this.activeName = this.$route.query.tabName as string;
    }
  }

  // tab跳转路由添加参数tabName
  tabClickHandle(tab: { name: string }) {
    history.pushState(
      {},
      "",
      `${process.env.VUE_APP_BASEPATH}/setting/channel?tabName=${tab.name}`,
    );
  }
}
</script>

<style lang="scss">
@import "src/assets/styles/channel/channel.scss";
</style>
