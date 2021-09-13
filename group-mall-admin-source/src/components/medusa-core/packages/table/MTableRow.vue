<template>
  <tr class="body--content">
    <slot :row="row"></slot>
  </tr>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";

@Component
export default class MTableRow extends Vue {
  static componentName = "MTableRow";

  @Prop({ type: Object })
  row: any;
}
</script>

