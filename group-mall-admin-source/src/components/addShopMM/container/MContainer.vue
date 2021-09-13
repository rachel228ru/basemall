<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 17:49:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:08:46
-->
<template>
  <div class="m__container">
    <div class="m__container--form">
      <slot name="form"></slot>
    </div>
    <div class="m__container--content">
      <slot name="content"></slot>
    </div>
    <div class="m__container--pagination" v-if="paginationVisible">
      <el-pagination
        small
        layout="prev, pager, next, sizes"
        :page-size.sync="pageSize"
        :current-page.sync="pageCurrent"
        :total.sync="pageTotal"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";

@Component
export default class MContainer extends Vue {
  @Prop({ type: Boolean, default: true })
  paginationVisible!: boolean;

  @Prop({ default: 0 })
  total!: number;

  @Prop({ default: 0 })
  size!: number;

  @Prop({ default: 0 })
  current!: number;

  get pageTotal() {
    return Number(this.total) || 10;
  }

  set pageTotal(v) {
    this.$emit("update:total", v);
  }

  get pageSize() {
    return Number(this.$props.size) || 10;
  }

  set pageSize(v) {
    this.$emit("update:size", v);
  }

  get pageCurrent() {
    return Number(this.$props.current) || 1;
  }

  set pageCurrent(v) {
    this.$emit("update:current", v);
  }
}
</script>

<style lang="scss" scoped>
@include b(m) {
  @include e(container) {
    @include m(form) {
      margin-bottom: 15px;
    }
    @include m(content) {
    }
    @include m(pagination) {
      @include flex(flex-end);
      margin: 50px 15px;
    }
  }
}
</style>
