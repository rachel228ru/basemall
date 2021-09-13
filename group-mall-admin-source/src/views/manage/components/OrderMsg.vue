<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 14:10:53
-->
<template>
  <div class="orderBox">
    <div class="text">交易概况</div>
    <div class="order">
      <div class="order__content">
        <div class="order__content--text">
          <div class="order__content--text--type">
            <i
              class="iconfont icondengdaifukuan"
              style="color:#FD8D42;font-size:20px;margin-right:3px"
            />
            待付款订单
          </div>
          <div class="order__content--text--account">
            {{ orderQuery.waitForPay }}
          </div>
        </div>
      </div>
      <div class="order__content">
        <div class="order__content--text">
          <div class="order__content--text--type">
            <i
              class="iconfont icondaifahuo"
              style="color:#FD8D42;font-size:20px;margin-right:3px"
            />
            待发货订单
          </div>
          <div class="order__content--text--account">
            {{ orderQuery.waitForSend }}
          </div>
        </div>
      </div>
      <div class="order__content">
        <div class="order__content--text">
          <div class="order__content--text--type">
            <i
              class="iconfont iconziyuan"
              style="color:#FD8D42;font-size:20px;margin-right:3px"
            />
            待签收订单
          </div>
          <div class="order__content--text--account">
            {{ orderQuery.shipped }}
          </div>
        </div>
      </div>
      <div class="order__content">
        <div class="order__content--text">
          <div class="order__content--text--type">
            <i
              class="iconfont icondaishouhuofuben"
              style="color:#FD8D42;font-size:20px;margin-right:3px"
            />
            待提货订单
          </div>
          <div class="order__content--text--account">
            {{ orderQuery.waitForPickup }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";

@Component
export default class OrderMsg extends Vue {
  @Prop()
  orderQuery!: string[];
}
</script>

<style lang="scss" scoped>
.orderBox {
  background-color: white;
  margin-top: 10px;
  padding: 20px;
}

.text {
  font-size: 14px;
  font-weight: bold;
  color: #5f6779;
}

.order {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  background-color: white;

  .order__content {
    width: 24%;
    height: 100px;
    background-color: white;
    display: flex;
    align-items: center;
    justify-content: center;

    .order__content--text {
      display: flex;
      flex-wrap: wrap;
      width: 90px;
      justify-content: center;
      color: #727073;

      .order__content--text--account {
        font-size: 30px;
        font-weight: bold;
        margin-top: 10px;
      }

      .order__content--text--type {
        font-size: 12px;
        margin-top: 5px;
        display: flex;
        align-items: center;
      }
    }
  }
}
</style>
