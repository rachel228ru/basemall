<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:18:10
-->
<template>
  <div
    :class="`notify ${type}`"
    v-if="!(shopInfo.isDue === 0 && dueDay(shopInfo.dueTime) > 7)"
  >
    <div>
      <i :class="`el-icon-${type} ${type}-icon`"></i>
    </div>
    <div class="notify__text">
      <p class="notify__text--top">
        <span v-if="shopInfo.isDue === 0 && shopInfo.orderSource === 2"
          >店铺免费试用期不足
          {{ dueDay(shopInfo.dueTime) }}
          天，为不影响正常经营，请尽快订购。</span
        >
        <span v-else-if="shopInfo.isDue === 0 && shopInfo.orderSource !== 2"
          >你的套餐即将过期（剩余{{
            dueDay(shopInfo.dueTime)
          }}天），请完成续费以免影响店铺正常营业</span
        >
        <span v-else
          >你的套餐已过期店铺已打样，请完成店铺升级，店铺将恢复正常营业。</span
        >
        <el-button
          type="text"
          class="primary"
          @click="goUpdate"
          v-if="shopInfo.isDue === 0 && shopInfo.orderSource !== 2"
          >立即续费</el-button
        >
        <el-button type="text" class="primary" @click="goOrder" v-else
          >立即订购</el-button
        >
      </p>
      <p class="notify__text--bottom">官方咨询电话：18058505737/18106661091</p>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";

@Component
export default class Notification extends Vue {
  @Prop({
    type: String,
    default: "warning",
  })
  type!: "error" | "info" | "warning" | "success";

  @Prop({
    type: Object,
    default: {},
  })
  shopInfo!: any;

  @Watch("shopInfo", { deep: true })
  getInfo() {
    console.log(this.shopInfo);
  }

  get dueDay() {
    return (dueTime: string) => {
      let result;
      const dValue = new Date(dueTime).getTime() - new Date().getTime();
      if (dValue > 0) {
        result = Math.ceil(dValue / 1000 / 60 / 60 / 24);
      } else {
        result = 0;
      }
      return result;
    };
  }

  goOrder() {
    this.$router.push("/meal");
  }

  goUpdate() {
    this.$router.push("/meal/update");
  }
}
</script>

<style lang="scss" scoped>
.notify {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  left: 190px;
  right: 0;
  top: 0;
  width: 1010px;
  min-width: 1010px;
  margin: 0 auto;
  padding: 0 23px;
  border-radius: 8px;
  box-sizing: border-box;
  border: 1px solid #ebeef5;
  position: fixed;
  background-color: rgba(255, 0, 0, 0.1);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: opacity 0.3s, transform 0.3s, left 0.3s, right 0.3s, top 0.4s,
    bottom 0.3s;
  overflow: hidden;
  z-index: 9999;

  .notify__text {
    padding-left: 20px;
    font-size: 13px;

    .notify__text--top {
      color: #666666;
    }

    .notify__text--bottom {
      color: #999999;
    }
  }
}
.warning {
  background-color: rgba(255, 0, 0, 0.1);
}

.warning-icon {
  color: red;
  font-size: 20px;
}
</style>
