<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-27 13:16:18
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:16:28
-->
<template>
  <div class="my--setting">
    <top-bar color="#000" bgcolor="#fff" />
    <div class="cover--group__cell" @click="goBack">
      <div class="group__cell--left">
        <i class="el-icon-arrow-left"></i>
        <span class="cell--left--text" style="font-size: 16px">设置</span>
      </div>
      <i class="el-icon-more"></i>
    </div>
    <div class="info">允许“小程序名称”使用我的地理位置</div>
    <div class="cover--group">
      <div class="cover--group__cell" v-if="showAddress">
        <div class="group__cell--left">
          <i class="iconfont cell--left--icon icongerenzhongxin-dizhiguanli" />
          <span class="cell--left--text">地址管理</span>
        </div>
        <i class="el-icon-arrow-right"></i>
      </div>
      <div class="cover--group__cell">
        <div class="group__cell--left">
          <i class="iconfont cell--left--icon icondaohang" />
          <span class="cell--left--text">使用我的地理位置</span>
        </div>
        <el-switch v-model="value" />
      </div>
      <!--      <div class="cover&#45;&#45;group__cell">-->
      <!--        <div class="group__cell&#45;&#45;left">-->
      <!--          <i class="iconfont cell&#45;&#45;left&#45;&#45;icon iconjiechubangding" />-->
      <!--          <span class="cell&#45;&#45;left&#45;&#45;text">解除接龙活动绑定</span>-->
      <!--        </div>-->
      <!--      </div>-->
      <div class="cover--group__cell">
        <div class="group__cell--left">
          <i class="iconfont cell--left--icon iconshanchu" />
          <span class="cell--left--text">清除缓存</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Emit, Prop } from "vue-property-decorator";
import TopBar from "@/views/decoration/components/UserCenter/PhoneView/TopBar.vue";

@Component({
  components: {
    TopBar,
  },
})
export default class PickCode extends Vue {
  @Prop({
    type: Boolean,
    default: true,
  })
  showAddress!: boolean;

  value = true;

  @Emit("goBack")
  goBack() {
    return false;
  }
}
</script>

<style lang="scss">
@mixin flex($justify-content: center, $align-items: center) {
  display: flex;
  justify-content: $justify-content;
  align-items: $align-items;
}

@mixin bottom-line($weight: 1px, $color: #f8f5f9) {
  border-bottom: $weight solid $color;
}

// 默认字体大小
$default-font-size: 14px;

// 字体颜色
$page-text-color: #45403c;

.my--setting {
  background: #f5f5f5;
  min-height: 100%;
}

.info {
  font-size: 12px;
  color: rgba(69, 64, 60, 0.6);
  padding: 8px 15px;
}

.cover--group {
  border-radius: 5px;
  margin-bottom: 10px;

  .cover--group__cell:not(:last-child) {
    // CSS样式
    @include bottom-line;
  }
}

.cover--group__cell {
  @include flex(space-between, center);
  background-color: #ffffff;
  height: 50px;
  padding: 15px 12px 15px 15px;

  .group__cell--left {
    @include flex(center, center);

    .cell--left--icon {
      width: 22px;
      height: 12px;
      padding-right: 11px;
    }

    .cell--left--text {
      font-size: $default-font-size;
      color: $page-text-color;
    }
  }

  .group__cell--right {
    color: rgba(69, 64, 60, 0.6);
    font-size: 12px;
    line-height: 0px;
  }
}
</style>
