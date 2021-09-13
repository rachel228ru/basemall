<template>
  <div class="line">
    <div class="circle"></div>
    <div class="lineOne"></div>
    <div class="LineTwo"></div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";

@Component
export default class PreImg extends Vue {}
</script>

<style lang="scss" scoped>
.line {
  display: flex;
  align-items: center;
}

.circle {
  width: 8px;
  height: 8px;
  background-color: #e75e75;
  border-radius: 50px;
}

.lineOne {
  width: 8px;
  height: 20px;
  border-radius: 10px;
  background-color: #eeb84e;
  transform: skewX(-35deg);
  margin-left: 8px;
}

.LineTwo {
  width: 8px;
  height: 16px;
  border-radius: 10px;
  background-color: #e78159;
  transform: skewX(-35deg);
  margin-left: 8px;
}
</style>
