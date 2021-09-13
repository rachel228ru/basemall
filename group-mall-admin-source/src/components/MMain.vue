<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-31 15:00:17
-->
<template>
  <m-layout :routes="routes" :breadcrumb="breadcrumb" :user-info="userInfo">
    <el-breadcrumb slot="breadcrumb" separator="/" class="m__breadcrumb">
      <el-breadcrumb-item
        v-for="(item, i) of breadcrumb"
        :class="[
          'm__breadcrumb--item',
          { 'm__breadcrumb--last': i !== breadcrumb.length - 1 },
        ]"
        :to="
          i !== breadcrumb.length - 1 && i !== 0 ? { path: item.path } : null
        "
        :key="item.path"
        >{{ item.title }}
      </el-breadcrumb-item>
    </el-breadcrumb>
    <div slot="aside" class="side-nav-main">
      <div class="side-nav">
        <MMenu :routes="routes" :userInfo="userInfo">
          <el-dropdown
            placement="bottom-start"
            slot="dropmenu"
            @command="commandHandle"
          >
            <div class="lineFlex">
              <img
                @click.stop="goChange"
                :src="shopInfo.logoUrl"
                class="shop--logo"
              />
              <div class="shop--name">
                <span class="el-dropdown-link">
                  <span>{{ shopInfo.shopName }}</span>
                  <!-- <i class="el-icon-caret-bottom"></i> -->
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="console">
                    <div class="dorp-cell">
                      <span>控制台</span>
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item command="index">
                    <div class="dorp-cell">
                      <span>官网首页</span>
                    </div>
                  </el-dropdown-item>
                  <!-- <el-dropdown-item
                    command="update"
                    v-if="shopInfo.level && shopInfo.orderSource !== 2"
                  >
                    <div class="dorp-cell">
                      <span>续费升级</span>
                    </div>
                  </el-dropdown-item> -->
                  <!-- <el-dropdown-item command="order" v-else>
                    <div class="dorp-cell">
                      <span>套餐订购</span>
                    </div>
                  </el-dropdown-item> -->
                  <el-dropdown-item command="setting">
                    <div class="dorp-cell">
                      <span>商家中心</span>
                      <span class="c6">{{ userInfo.name }}</span>
                      <i class="el-icon-arrow-right"></i>
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item command="logout">
                    <div class="dorp-cell noborder">
                      <span>退出登陆</span>
                      <i class="el-icon-switch-button"></i>
                    </div>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </div>
            </div>
          </el-dropdown>
        </MMenu>
      </div>
    </div>
    <template slot="view">
      <router-view slot="view"></router-view>
    </template>

    <!-- <Notification :shopInfo="shopInfo" slot="notify" /> -->
  </m-layout>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import MLayout from "@/components/packages/layout";
import MMenu from "@/components/packages/layout/MMenu.vue";
import { logout, modifySignStatus } from "@/libs";
import { getSystemConfig } from "@/api/sign";

@Component({ components: { MLayout, MMenu } })
export default class MMain extends Vue {
  // mock
  logo = "";

  get routes(): any[] {
    return (this.$router as any).options.routes;
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
  get userInfo() {
    const info = {
      name: "",
      avatar: "",
      logo: "",
      shopName: "",
    };
    if (this.$STORE.userStore.userInfo) {
      const userInfo = this.$STORE.userStore.userInfo;
      info.name = userInfo.nikeName;
      info.avatar = userInfo.avatarUrl;
      if (userInfo.shopInfoVo) {
        info.logo = userInfo.shopInfoVo.logoUrl;
        info.shopName = userInfo.shopInfoVo.shopName;
      }
    }
    return info;
  }

  /** 店铺数据 */
  get shopInfo() {
    let shopInfo = {
      businessHours: "",
      dueTime: "",
      isDue: 0,
      level: 0,
      logoUrl: "",
      packageName: "",
      shopName: "",
      shopPhone: "",
      status: 0,
      templateName: "",
    };
    if (this.$STORE.userStore.shopInfo) {
      shopInfo = this.$STORE.userStore.shopInfo;
    }
    return shopInfo;
  }

  async commandHandle(command: string) {
    // 推出登录
    if (command === "logout") {
      modifySignStatus("", null);
      logout();
      try {
        const response = await getSystemConfig();
        const { code, data } = response;
        if (code === 200 && data.systemConfig && data.systemConfig.consoleLog) {
          open(data.systemConfig.consoleLog, "_top");
        }
      } catch (e) {
        this.$message({
          type: "warning",
          message: e,
        });
      }
    }
    // 账号信息
    if (command === "setting") {
      await this.$router.push("/business");
    }

    // 控制台
    if (command === "console") {
      await this.$router.push("/console");
    }

    // 官网
    if (command === "index") {
      const url = process.env.VUE_APP_BASEURL.replace(/\/api/, "");
      open(`${url}`, "_top");
    }

    // 订购
    if (command === "order") {
      await this.$router.push("/meal");
    }

    // 升级/续费
    if (command === "update") {
      await this.$router.push("/meal/update");
    }
  }

  async goConsole() {
    try {
      const response = await getSystemConfig();
      const { code, data } = response;
      if (code === 200 && data.systemConfig && data.systemConfig.consoleUrl) {
        open(`${data.systemConfig.consoleUrl}`, "_top");
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  goChange() {
    this.$router.push({
      path: "/setting",
      query: {
        tabName: "Store",
      },
    });
  }
}
</script>

<style lang="scss">
.lineFlex {
  display: flex;
  align-items: center;
  height: 60px;
  cursor: pointer;
}

.m__breadcrumb {
  &--item {
    .is-link {
      font-weight: 400 !important;
      color: #606266 !important;
      cursor: pointer;
    }

    &:last-child .el-breadcrumb__inner {
      font-weight: 700 !important;
      text-decoration: none;
      transition: color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
      color: #303133;
    }
  }
}

.el-dropdown-link {
  display: flex;
  height: 50px;
  align-items: center;
}
</style>
