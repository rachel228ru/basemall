<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:29:21
-->
<template>
  <div
    class="editor__preview_new spellPage--preview"
    style="border: 1px solid #dcdfe6 !important"
  >
    <div class="header__box">
      <span
        class="back"
        v-if="[2, 3].includes(formData.isSearch)"
        @click="gotoBack"
        >{{ backStr }}</span
      >
      分类
      <span class="tis">
        <i></i>
        <i></i>
        <i></i>
      </span>
    </div>

    <!-- 显示搜索状态 -->
    <div v-if="formData.isSearch === 2">
      <SearchPage :formData="formData"></SearchPage>
    </div>

    <!-- 显示内容页面 -->
    <div v-if="formData.isSearch === 1" class="show__content-info">
      <!-- 搜索 -->
      <div class="search__box" @click="changeSearch">
        <div class="search-icon">
          <i class="el-icon-search"></i>
        </div>
      </div>
      <!-- 内容显示区 -->
      <div class="show__con-box">
        <!-- 顶部模式 -->
        <div
          class="con__box"
          style="height: 100%"
          v-if="formData.selectMode === 1"
        >
          <TopModeHead :formData="formData"></TopModeHead>
          <div class="dividing__line"></div>
          <TopBigGoods :formData="formData"></TopBigGoods>
        </div>
        <!-- 侧边栏模式 -->
        <div class="con__box left-con__box" v-if="formData.selectMode === 2">
          <div class="left__tabbar-box">
            <LeftTabbar :formData="formData"></LeftTabbar>
          </div>
          <div class="left__biggoods-box" :style="getPageStyle">
            <div
              class="right__box"
              :style="{
                paddingBottom: !getCategoryVo.hasNextCatgory ? '0px' : '50px',
              }"
            >
              <SwiperList
                v-if="
                  formData.currentFirstCategory.swiperList &&
                    formData.currentFirstCategory.swiperList.length
                "
                :formData="formData"
              ></SwiperList>
              <LeftBigGoods
                v-if="formData.selectType === 1"
                :formData="formData"
              ></LeftBigGoods>
              <ClassificationList
                v-if="formData.selectType === 2"
                :formData="formData"
              ></ClassificationList>
            </div>
            <div
              class="left__biggoods-more"
              v-if="getCategoryVo.hasNextCatgory && formData.selectType === 1"
            >
              <img
                src="https://oss-tencent.bgniao.cn//gruul/20201117/26690c0bd517470d9dad39938555d93b.png"
                alt=""
              />
              <span>上拉浏览{{ getCategoryVo.name }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="show__content-info" v-if="formData.isSearch === 3">
      <SecondCatGoods :formData="formData"></SecondCatGoods>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import BusinessSuper from "./BusinessSuper";
import TopModeHead from "./components/TopModeHead.vue";
import TopBigGoods from "./components/TopBigGoods.vue";
import LeftTabbar from "./components/LeftTabbar.vue";
import LeftBigGoods from "./components/LeftBigGoods.vue";
import ClassificationList from "./components/ClassificationList.vue";
import SearchPage from "./components/SearchPage.vue";
import SecondCatGoods from "./components/SecondCatGoods.vue";
import SwiperList from "./components/SwiperList.vue";

@Component({
  components: {
    TopModeHead,
    TopBigGoods,
    LeftTabbar,
    LeftBigGoods,
    ClassificationList,
    SearchPage,
    SecondCatGoods,
    SwiperList,
  },
})
export default class BusinessSuperPreview extends Vue {
  @Prop()
  formData!: BusinessSuper;

  backStr = "<";

  /**
   * 动态设置页面样式
   */
  get getPageStyle() {
    return { padding: `0px ${this.formData.pagePadding}px` };
  }

  /**
   * 获取当前显示的展示分类，并判断是否还有下一个
   */
  get getCategoryVo() {
    const { category = {} as any } = this.formData.currentFirstCategory;
    const firstCatList = this.formData.firstCatList || [];
    const index = firstCatList.findIndex(i => i.category.id === category.id);
    const hasNextCatgory = index !== -1 && index !== firstCatList.length - 1;
    const hasUpperCatgory = index !== -1 && index !== 0;
    const nextCategory = hasNextCatgory ? firstCatList[index + 1].category : {};
    const upperCatgory = hasUpperCatgory
      ? firstCatList[index - 1].category
      : {};
    return {
      ...nextCategory,
      hasNextCatgory,
      hasUpperCatgory,
      upperCatgory,
    };
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 搜索页面返回
   */

  gotoBack() {
    this.formData.isSearch = 1;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 修改搜索状态
   */

  changeSearch() {
    // this.formData.isSearch = 2;
  }
}
</script>
<style lang="scss">
@import "@/assets/styles/decoration/businessSuper";
</style>
