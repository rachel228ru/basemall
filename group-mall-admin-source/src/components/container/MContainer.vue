<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:07:24
-->
<template>
  <div class="m__container" ref="container">
    <div class="m__container--form">
      <slot name="form"></slot>
    </div>
    <div class="m__container--content" style="padding-bottom: 30px;">
      <slot name="content"></slot>
    </div>
    <div
      ref="pagination"
      class="m__container--pagination "
      v-if="paginationVisible"
    >
      <div>
        <slot name="pagination--left"></slot>
      </div>
      <el-pagination
        small
        layout="total, prev, pager, next, sizes"
        :page-size.sync="pageSize"
        :current-page.sync="pageCurrent"
        :page-sizes="[10, 20, 50, 100]"
        :total.sync="pageTotal"
      >
      </el-pagination>
    </div>
    <slot name="other"></slot>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Ref } from "vue-property-decorator";
import debounce from "lodash/debounce";

@Component
export default class MContainer extends Vue {
  @Ref("container")
  container!: HTMLElement;

  @Ref("pagination")
  pagination!: HTMLElement;

  @Prop({ type: Boolean, default: true })
  paginationVisible!: boolean;

  @Prop({ type: Boolean, default: true })
  paginationAbsolute!: boolean;

  @Prop()
  total!: number;

  @Prop()
  size!: number;

  @Prop()
  current = 1;

  get pageTotal() {
    return Number(this.total) || 0;
  }

  set pageTotal(v) {
    this.$emit("update:total", v);
  }

  get pageSize() {
    return Number(this.size) || 10;
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

  setPaginationPosition = debounce(() => {
    this.pagination.style.left = `${this.container.offsetLeft}px`;
    this.pagination.style.width = `${this.container.offsetWidth}px`;
  }, 50);

  // updated() {
  //   this.setPaginationPosition();
  // }
}
</script>

<style lang="scss">
@include b(m) {
  @include e(container) {
    width: 100%;
    height: 100%;
    position: relative;
    @include m(form) {
      margin-bottom: 15px;
    }
    @include m(content) {
    }

    @include m(pagination) {
      @include flex(space-between);

      margin-top: 20px;

      &.pagination__fixed {
        position: fixed;
        bottom: 0;
      }

      .el-pagination__sizes {
        margin-top: -3px;
        height: 28px;
        margin-right: 0;
      }
    }
  }
}
</style>
