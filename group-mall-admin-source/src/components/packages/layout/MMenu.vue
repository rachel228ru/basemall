<template>
  <div class="aside-wrap">
    <div class="admin__aside--shop">
      <slot name="dropmenu"></slot>
      <div class="iconfont iconweixiubeijing cursor"></div>
    </div>
    <div class="side-nav-wrap">
      <div class="side-nav-wrap-main">
        <div class="admin__menu" @scroll="onScroll">
          <template v-for="(route, i) of localRoutes">
            <template v-if="route.meta.isShow !== 1">
              <template v-if="!route.meta.islink && route.children">
                <div
                  v-bind:key="route.path + i"
                  class="admin__menu--item"
                  :class="[
                    {
                      active:
                        parentIsActive(route.children, route.path) &&
                        !isEditMode,
                      bgf8: itemActiveIndex === i,
                    },
                  ]"
                  :ref="`menu_item_${i}`"
                >
                  <div
                    class="item--title"
                    :ref="`item_sub_${i}`"
                    :class="
                      currentRouteShow && currentRouteShow.show
                        ? 'noActive'
                        : ''
                    "
                    @mouseover="showFloatBox(i, route.children)"
                  >
                    <span>
                      <i :class="'iconfont ' + route.meta.icon"></i>
                      {{ route.meta.title }}
                    </span>
                    <i
                      class="iconfont iconicon-arrow-right2 f14"
                      :class="
                        currentRouteShow && currentRouteShow.show ? 'black' : ''
                      "
                      v-show="hideLength(route.children)"
                    ></i>
                  </div>
                  <div class="item__sub--menu" @mouseover="onMouseLeave">
                    <template v-for="sRoute of route.children">
                      <div
                        class="sub--item pt20"
                        :key="route.path + '/' + sRoute.path"
                        :index="route.path + '/' + sRoute.path"
                        v-if="!sRoute.meta.isShow && sRoute.show"
                      >
                        <router-link
                          v-if="!isEditMode"
                          :path="sRoute.path"
                          :target="
                            sRoute.path == 'editorPage' ? '_blank' : '_self'
                          "
                          :to="route.path + '/' + sRoute.path"
                          :class="{
                            active: isCurrent2(route.path + '/' + sRoute.path),
                          }"
                          >{{ sRoute.meta.title }}</router-link
                        >
                        <a
                          href="javascript:;"
                          @click.stop="onSelect(sRoute, i)"
                          v-else
                        >
                          {{ sRoute.meta.title }}
                          <i class="iconfont iconyoujiantou"></i>
                        </a>
                      </div>
                    </template>
                  </div>
                  <!-- <div
                    class="item--modal floatmenuBox"
                    v-show="hideLength(route.children)"
                    :ref="`item_sub_${i}`"
                  >
                    <template v-for="(sRoute, sIndex) of route.children">
                      <div
                        class="modal--item"
                        :key="sIndex"
                        :index="route.path + '/' + sRoute.path"
                        v-if="!sRoute.meta.isShow && !sRoute.show"
                      >
                        <router-link
                          v-if="!isEditMode"
                          :to="route.path + '/' + sRoute.path"
                          :class="{
                            active: isCurrent2(route.path + '/' + sRoute.path),
                          }"
                          >{{ sRoute.meta.title }}</router-link
                        >
                        <a
                          href="javascript:;"
                          @click.stop="onSelect(sRoute, i)"
                          v-else
                        >
                          <i class="iconfont iconzuojiantou"></i>
                          {{ sRoute.meta.title }}
                        </a>
                      </div>
                    </template>
                  </div>-->
                  <div class="item--mask" v-show="!!itemActiveIndex"></div>
                </div>
              </template>
              <template v-else>
                <div
                  v-bind:key="route.path"
                  class="admin__menu--item"
                  :class="{ active: route.path === $route.path && !isEditMode }"
                >
                  <div class="item--title">
                    <router-link v-if="!isEditMode" :to="route.path">
                      <span>
                        <i :class="'iconfont ' + route.meta.icon"></i>
                        {{ route.meta.title }}
                      </span>
                    </router-link>
                    <a href="javascript:;" v-else>
                      <span>
                        <i :class="'iconfont ' + route.meta.icon"></i>
                        {{ route.meta.title }}
                      </span>
                    </a>
                  </div>
                </div>
              </template>
            </template>
          </template>
          <!-- <div
        class="admin__menu--setting isEditModen sub-active"
        @click="toggleEditMode"
          >{{!isEditMode?'编辑':'保存'}}</div>-->
        </div>
      </div>
      <div
        class="floatmenuBox item--modal"
        :class="[
          itemActiveIndex && hideLength(localRoutes[itemActiveIndex].children)
            ? 'active_v'
            : '',
        ]"
        :style="{ top: itemSubTop + 'px' }"
        ref="floatmenuBox"
        @mouseleave="onMouseLeave"
      >
        <div v-if="itemActiveIndex && localRoutes[itemActiveIndex].children">
          <template
            v-for="(sRoute, sIndex) of localRoutes[itemActiveIndex].children"
          >
            <div
              class="modal--item"
              :key="sIndex"
              :index="localRoutes[itemActiveIndex].path + '/' + sRoute.path"
              v-if="!sRoute.meta.isShow && !sRoute.show"
            >
              <router-link
                v-if="!isEditMode"
                :path="sRoute.path"
                :target="sRoute.path == 'editorPage' ? '_blank' : '_self'"
                :to="localRoutes[itemActiveIndex].path + '/' + sRoute.path"
                :class="{
                  active: isCurrent2(
                    localRoutes[itemActiveIndex].path + '/' + sRoute.path,
                  ),
                }"
                >{{ sRoute.meta.title }}</router-link
              >
              <a
                href="javascript:;"
                @click.stop="onSelect(sRoute, itemActiveIndex)"
                v-else
              >
                <i class="iconfont iconzuojiantou"></i>
                {{ sRoute.meta.title }}
              </a>
            </div>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import { RouteConfig } from "vue-router";
import pick from "lodash/pick";
import { upDateAsyncRouterMap } from "@/api/sign";
let scrollVal: any = null; //存储滑动的变量
let timer: any = null; //定时器
@Component
export default class MMenu extends Vue {
  @Prop()
  routes!: any[];

  @Prop()
  userInfo: any;

  get currentRoute() {
    return this.$route.path;
  }

  get routersId() {
    return this.$STORE.menuStore.routersId;
  }

  localRoutes: any = [];

  /* 二级菜单定位top*/
  itemActiveIndex = 0;

  /* 判断是不是所有导航都在左侧*/
  is_empty = false;

  itemSubTop = -1;

  isScrolling = false;

  currentRouteShow = { path: "" };

  /** 编辑模式 */
  isEditMode = false;

  mounted() {
    document.addEventListener("click", (e: any) => {
      if (
        document.querySelector(".aside-wrap") &&
        !document.querySelector(".aside-wrap").contains(e.target) &&
        this.isEditMode
      ) {
        this.toggleEditMode();
      }
    });
    document.addEventListener("mouseover", (e: any) => {
      if (
        !this.isEditMode &&
        document.querySelector(".aside-wrap") &&
        !document.querySelector(".aside-wrap").contains(e.target)
      ) {
        this.hideFloatBox();
      }
    });
  }

  /** 是否为当前路由 */
  isCurrent(
    path: string,
    item: {
      path: string;
    },
  ) {
    const { matched } = this.$route;
    if (
      this.currentRoute === path ||
      (matched[1] && matched[1].path === path)
    ) {
      this.currentRouteShow = item;
      return true;
    }
    return false;
  }

  /** 是否为当前路由 indeof 针对三级*/
  isCurrent2(path: string) {
    const { matched } = this.$route;

    if (
      (matched[1] && matched[1].path === path) ||
      this.currentRoute === path ||
      (path.indexOf("/copartner/order/") !== -1 &&
        path.indexOf("/copartner/setting/") !== -1 &&
        this.currentRoute.indexOf(path) !== -1)
    ) {
      return true;
    }
    return false;
  }

  /*编辑导航默认在右显示右侧蓝*/
  @Watch("isEditMode")
  setDefaultItem(newVal: any) {
    if (newVal) {
      if (!this.checkItem()) {
        this.showFloatBox(1, this.localRoutes[1].children);
      }
    } else {
      this.hideFloatBox();
    }
  }

  checkItem() {
    let a = false;
    const localRoutes = this.localRoutes;
    for (let i = 0; i < localRoutes.length; i++) {
      if (localRoutes[i].children && localRoutes[i].children.length) {
        for (let n = 0; n < localRoutes[i].children.length; n++) {
          if (localRoutes[i].children[n].show) {
            a = true;
            break;
          }
        }
      }
    }
    return a;
  }

  /** 切换菜单编辑模式 */
  toggleEditMode() {
    this.isEditMode = !this.isEditMode;
    if (!this.isEditMode) {
      const localRoutes = JSON.stringify(this.localRoutes);
      upDateAsyncRouterMap({
        id: this.routersId,
        properties: localRoutes,
      }).then(() => {
        this.$message({
          message: "保存成功",
          type: "success",
        });
      });
    }
  }

  created() {
    this.getRouteData();
  }

  getRouteData() {
    this.localRoutes = this.handleRouteData(this.$STORE.menuStore.routers);
  }

  handleRouteData(routes: RouteConfig[]) {
    return routes.map(item => {
      if (item.children) {
        item.children = this.handleRouteData(item.children);
      }
      item = pick(item, [
        "path",
        "meta",
        "name",
        "children",
        "version",
        "show",
      ]);
      return item;
    });
  }

  onSelect(item: { show: boolean }, index: any) {
    item.show = !item.show;
    this.showFloatBox(index, this.localRoutes[this.itemActiveIndex].children);
  }

  hideLength(items: {
    filter: (
      arg0: (item: any) => boolean,
    ) => { (): any; new (): any; length: any };
  }) {
    return items.filter(
      (item: { show: any; meta: { isShow: any } }) =>
        !item.show && !item.meta.isShow,
    ).length;
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
      return this.isCurrent(path + "/" + item.path, item);
    }).length;
  }

  showFloatBox(index: number, arr: any) {
    if (this.isScrolling) {
      return;
    }
    if (arr && this.hideLength(arr)) {
      this.itemActiveIndex = index;
      // const itemSub = this.$refs[`item_sub_${index}`][0];
      const menuItem = this.$refs[`menu_item_${index}`][0];
      const clientHeight = document.documentElement.clientHeight;
      const wrapHeight = clientHeight - 88;
      const itemHeight = 38 + 40 * this.hideLength(arr);
      const scrollTop = document.querySelector(".side-nav-wrap-main").scrollTop;
      const domToclientHeight = menuItem.offsetTop - scrollTop;
      const dValue = wrapHeight - (domToclientHeight + itemHeight);
      if (dValue < 0) {
        this.itemSubTop = wrapHeight - itemHeight;
      } else {
        this.itemSubTop = domToclientHeight;
      }
    } else {
      this.hideFloatBox();
    }
  }

  hideFloatBox() {
    this.itemActiveIndex = 0;
  }

  onScroll(val: any) {
    if (timer) clearTimeout(timer);
    if (!this.isScrolling) {
      this.isScrolling = true;
    }
    this.hideFloatBox();
    timer = setTimeout(() => {
      /* 判断是否已经滚动结束，不能写在scrollEnd，有bug */
      if (val === scrollVal) {
        //实现上移
        this.isScrolling = false;
      }
    }, 500);
    scrollVal = val;
  }

  onMouseLeave() {
    if (!this.isEditMode) {
      this.hideFloatBox();
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../styles/new_mmenu.scss";
.black {
  color: #676767 !important;
}
</style>
