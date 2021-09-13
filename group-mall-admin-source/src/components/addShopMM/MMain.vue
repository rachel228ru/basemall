<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 16:47:49
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:36:06
-->
<template>
  <div>
    <m-layout>
      <el-image
        slot="logo"
        @click="linkTo"
        class="admin__header--logo pointer"
        :src="logo"
      >
        <div slot="error" class="image-slot">
          <i class="el-icon-picture-outline"></i>
        </div>
      </el-image>
      <div slot="user" class="admin__header--user">
        <img class="user--avatar" :src="userInfo.avatar" />
        <el-dropdown trigger="click" @command="commandHandle">
          <i class="el-icon-more" />
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="userInfo">账号信息</el-dropdown-item>
            <el-dropdown-item command="logout">退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <el-breadcrumb slot="breadcrumb" separator="/">
        <el-breadcrumb-item
          v-for="item of breadcrumb"
          :to="{ path: item.path }"
          :key="item.path"
          >{{ item.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
      <router-view slot="content" class="admin__main--view"></router-view>
      <footer slot="footer" class="admin__footer">
        <p class="medusa__rights">
          Copyright © 2020 启山智软. All Rights Reserved
          宁波启山科技有限公司版权所有
        </p>
        <p>
          <img
            style="width: 14px;"
            src="https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200321/gongan.jpg"
          />
          <span @click="goPolice" class="pointer"
            >浙公网安备33020502000496号</span
          >
        </p>
      </footer>
    </m-layout>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import MLayout from "./packages/layout";
import MMenu from "./packages/layout/MMenu.vue";
import { logout, modifySignStatus } from "@/libs";
import { getSystemConfig } from "@/api/sign";

@Component({ components: { MLayout, MMenu } })
export default class MMain extends Vue {
  // mock
  logo =
    "https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200407/logo.png";

  get routes(): any[] {
    return this.$router.options.routes || [];
  }

  get breadcrumb() {
    return this.$route.matched
      .map(route => {
        return {
          title: route.meta.title,
          path: route.path,
        };
      })
      .filter(item => !!item.title);
  }

  get active() {
    return this.$route.fullPath;
  }

  /** 用户数据 */
  userInfo = {
    name: this.$STORE.userStore.userInfo
      ? this.$STORE.userStore.userInfo.nikeName
      : "",
    avatar: this.$STORE.userStore.userInfo
      ? this.$STORE.userStore.userInfo.avatarUrl
      : "",
    isAgent: this.$STORE.userStore.userInfo
      ? this.$STORE.userStore.userInfo.isAgent
      : false,
  };

  async linkTo() {
    try {
      const response = await getSystemConfig();
      const { code, data } = response;
      if (code === 200 && data) {
        open(`${data.systemConfig.consoleLog}`, "_top");
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  goPolice() {
    open(
      `http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=33020502000496`,
    );
  }

  async commandHandle(command: string) {
    // 推出登录
    if (command === "logout") {
      modifySignStatus("", null);
      logout();
      try {
        const response = await getSystemConfig();
        const { code, data } = response;
        if (code === 200 && data) {
          open(`${data.systemConfig.consoleLog}`, "_top");
        }
      } catch (e) {
        this.$message({
          type: "warning",
          message: e,
        });
      }
    }
    // 账号信息
    if (command === "userInfo") {
      await this.$router.replace(process.env.VUE_APP_PUBLICPATH + "/business");
    }
  }
}
</script>
