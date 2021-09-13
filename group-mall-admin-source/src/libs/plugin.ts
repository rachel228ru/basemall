/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:43:20
 */
import { VueConstructor } from "vue";
import MedusaUI from "@/components/medusa-core";
import MContainer from "@/components/container/MContainer.vue";
import VueAMap from "vue-amap";
import ProgressBar from "vue-progressbar";
import VueClipboards from "vue-clipboard2";
import "@/assets/styles/element-variables.scss";
import "normalize.css";

// Vue插件安装
export const installPlugin = (Vue: VueConstructor) => {
  /** 自定义UI库 */
  Vue.use(MedusaUI);

  /** admin容器 */
  Vue.component("m-container", MContainer);

  /** 进度条 */
  Vue.use(ProgressBar, {
    color: "#409EFF",
    failedColor: "red",
    height: "2px",
  });

  /** 地图组件 */
  Vue.use(VueAMap);

  VueAMap.initAMapApiLoader({
    key: "5834a95f0e9d1a6794a4c21dd275f6cd",
    plugin: [
      "AMap.Autocomplete",
      "AMap.PlaceSearch",
      "AMap.Scale",
      "AMap.OverView",
      "AMap.ToolBar",
      "AMap.MapType",
      "AMap.PolyEditor",
      "AMap.CircleEditor",
    ],
    v: "1.4.11",
  });

  /** 剪贴板 */
  Vue.use(VueClipboards);

  /** 基础地址 */
  Vue.prototype.VUE_APP_BASEURL = process.env.VUE_APP_BASEURL;

  /** 内容容器滚动到顶部 */
  Vue.prototype.$scrollTop = function() {
    this.$nextTick(el => {
      document
        .querySelector(el || ".admin__main--content")
        .scrollTo({ top: 0 });
    });
  };
};
