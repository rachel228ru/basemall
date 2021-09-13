<template>
  <div class="Exception">
    <div>
      <img
        src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/manage.png"
        alt=""
      />
      <div class="Exception__text">功能暂未开放,敬请期待~</div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";

@Component
export default class Exception extends Vue {}
</script>

<style lang="scss" scoped>
.Exception {
  width: 100%;
  height: 750px;
  display: flex;
  justify-content: center;
  align-items: center;
  &__text {
    display: flex;
    justify-content: center;
    margin-top: 20px;
    color: #949494;
  }
}
</style>
