<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:19:30
-->
<template>
  <div>
    <RichEditor :text="detailText" ref="wEditor" />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch, Ref } from "vue-property-decorator";
import RichEditor from "@/components/RichEditor.vue";

@Component({
  components: {
    RichEditor,
  },
})
export default class AssitionLog extends Vue {
  @Prop({
    type: Boolean,
    default: {},
  })
  isGroup!: boolean;

  @Prop({
    type: Boolean,
    default: {},
  })
  logDialog!: boolean;

  @Prop({
    type: String,
    default: "",
  })
  detail!: string;

  @Watch("logDialog")
  watchLogDialog() {
    if (this.logDialog) {
      this.getDetail();
    }
  }

  @Ref()
  wEditor!: HTMLFormElement;

  detailText = "";

  mounted() {
    this.getDetail();
  }

  /**
   * 获取协议内容
   */
  getDetail(param = "") {
    if (param) {
      this.detailText = param;
    } else {
      this.detailText = this.detail;
    }
  }

  getDetailHtml() {
    return this.wEditor.getHtml();
  }
}
</script>

<style lang="scss" scoped></style>
