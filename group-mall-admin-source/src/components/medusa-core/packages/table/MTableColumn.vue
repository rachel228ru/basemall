<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 16:35:20
-->
<template>
  <span class="item__content">
    <slot :row="row">
      {{ row[prop] }}
    </slot>
  </span>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";

@Component
export default class MTableColumn extends Vue {
  static componentName = "MTableColumn";

  @Prop({ type: String, default: "" })
  prop!: string;

  @Prop({ type: String, default: "" })
  label!: string;

  @Prop({ type: Number, default: 0 })
  index!: number;

  @Prop({ type: [String, Number], default: "" })
  width!: string | number;

  @Prop({ type: Boolean, default: false })
  showsSlection!: boolean;

  get row() {
    return (this.$parent as any).row;
  }

  onChange(v: any) {
    this.$parent.$parent.onItemCheckChange(!v, this.index);
  }
}
</script>
