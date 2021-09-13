<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 17:20:49
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:42:14
-->
<template>
  <div class="menu">
    <template v-for="(route, i) of localRoutes">
      <template v-if="route.meta.isShow !== 1">
        <template v-if="route.children">
          <div
            :key="i"
            class="menu__item"
            :class="{ active: parentIsActive(route.children, route.path) }"
          >
            <div class="menu__item--title">
              <span>
                <i :class="'iconfont ' + route.meta.icon"></i>
                {{ route.meta.title }}</span
              >
              <i
                class="el-icon-arrow-right"
                v-show="hideLength(route.children)"
              ></i>
            </div>
            <div class="item__sub--menu">
              <template v-for="sRoute of route.children">
                <div
                  class="sub--item"
                  :key="route.path + '/' + sRoute.path"
                  :index="route.path + '/' + sRoute.path"
                  v-if="!sRoute.meta.isShow && sRoute.show"
                >
                  <router-link
                    v-if="!isEditMode"
                    :to="route.path + '/' + sRoute.path"
                    :class="{
                      active: isCurrent(route.path + '/' + sRoute.path),
                    }"
                  >
                    {{ sRoute.meta.title }}
                  </router-link>
                  <a href="javascript:;" @click="onSelect(sRoute)" v-else>
                    {{ sRoute.meta.title }}
                    <i class="el-icon-d-arrow-right"></i>
                  </a>
                </div>
              </template>
            </div>
            <div class="item--modal" v-show="hideLength(route.children)">
              <template v-for="sRoute of route.children">
                <div
                  class="modal--item"
                  :key="route.path + '/' + sRoute.path"
                  :index="route.path + '/' + sRoute.path"
                  v-if="!sRoute.meta.isShow && !sRoute.show"
                >
                  <router-link
                    v-if="!isEditMode"
                    :to="route.path + '/' + sRoute.path"
                    :class="{
                      active: isCurrent(route.path + '/' + sRoute.path),
                    }"
                  >
                    {{ sRoute.meta.title }}
                  </router-link>
                  <a href="javascript:;" @click="onSelect(sRoute)" v-else>
                    <i class="el-icon-d-arrow-left"></i> {{ sRoute.meta.title }}
                  </a>
                </div>
              </template>
            </div>
            <div class="item--mask"></div>
          </div>
        </template>
        <template v-else>
          <div
            :key="i"
            class="menu--item"
            :class="{ active: route.path === $route.path }"
          >
            <div class="menu--item--title">
              <router-link v-if="!isEditMode" :to="route.path">
                <span
                  ><i :class="'iconfont ' + route.meta.icon"></i>
                  {{ route.meta.title }}</span
                >
              </router-link>
              <a href="javascript:;" v-else>
                <i :class="'iconfont ' + route.meta.icon"></i>
                {{ route.meta.title }}
              </a>
            </div>
          </div>
        </template>
      </template>
    </template>
    <div class="menu--setting" @click="toggleEditMode">
      <i class="el-icon-s-tools" v-if="!isEditMode"></i>
      <i class="el-icon-success" v-else></i>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import { RouteConfig } from "vue-router";
import pick from "lodash/pick";
import debounce from "lodash/debounce";

@Component
export default class MMenu extends Vue {
  @Prop()
  routes!: any[];

  get currentRoute() {
    return this.$route.path;
  }

  localRoutes: RouteConfig[] = [];

  /** 编辑模式 */
  isEditMode = false;

  /** 是否为当前路由 */
  isCurrent(path: string) {
    return this.currentRoute === path;
  }

  /** 切换菜单编辑模式 */
  toggleEditMode() {
    this.isEditMode = !this.isEditMode;
  }

  @Watch("isEditMode")
  handlelocalRoutesChange = debounce(() => {
    localStorage.setItem("localRoutes", JSON.stringify(this.localRoutes));
  }, 100);

  created() {
    this.getRouteData();
  }

  getRouteData() {
    try {
      const storagData = JSON.parse(localStorage.getItem("localRoutes") || "");
      if (storagData && storagData[0].version === this.routes[0].version) {
        this.localRoutes = storagData;
        return;
      }
      this.localRoutes = this.handleRouteData(this.routes);
    } catch {
      this.localRoutes = this.handleRouteData(this.routes);
    }
  }

  handleRouteData(routes: any[]) {
    return routes.map(item => {
      if (item.children) {
        item.children = this.handleRouteData(item.children);
      }
      item = pick(item, ["path", "meta", "name", "children", "version"]);
      item.show = false;
      return item;
    });
  }

  onSelect(item: { show: boolean }) {
    item.show = !item.show;
  }

  hideLength(items: {
    filter: (
      arg0: (item: any) => boolean,
    ) => { (): any; new (): any; length: any };
  }) {
    return items.filter((item: { show: any }) => !item.show).length;
  }

  parentIsActive(
    items: {
      filter: (
        arg0: (item: any) => boolean,
      ) => { (): any; new (): any; length: any };
    },
    path: string,
  ) {
    return items.filter((item: { path: string }) => {
      return this.isCurrent(path + "/" + item.path);
    }).length;
  }
}
</script>

<style lang="scss" scoped></style>
