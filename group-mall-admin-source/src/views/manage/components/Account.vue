<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 09:14:55
-->
<template>
  <div class="account">
    <div class="account__left">
      <div class="store">
        <span class="store__title">今日访客统计</span>
      </div>
      <div class="account__left--addNum">
        <span>新增访客(人)</span>
        <div class="account__left--addNum--number">{{ accountQuery.uv }}</div>
      </div>
      <div class="account__left--accNum">
        <div class="account__left--accNum--inCenter">
          <p class="inCenter__text">当日浏览量</p>
          <p class="inCenter__number">{{ accountQuery.pv }}</p>
        </div>
        <div class="account__left--accNum--inCenter">
          <p class="inCenter__text">总访客(人)</p>
          <p class="inCenter__number">{{ accountQuery.totalUv }}</p>
        </div>
      </div>
    </div>
    <div class="account__center">
      <Ranking></Ranking>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import { ManageAccountQuery } from "../manageType";
import Ranking from "./Ranking.vue";

@Component({
  components: {
    Ranking,
  },
})
export default class Account extends Vue {
  @Prop()
  accountQuery!: ManageAccountQuery;

  active = 0;

  userList = [];

  selectType(type: number) {
    this.active = type;
  }
}
</script>

<style lang="scss" scoped>
.account {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;

  .account__left {
    width: 35%;
    height: 250px;
    background-color: white;
    padding: 10px;
    padding-top: 20px;

    .account__left--addNum {
      padding: 30px 0px 0px 10px;
      color: #757575;
      width: 90px;
      display: flex;
      flex-wrap: wrap;
      justify-content: center;

      .account__left--addNum--number {
        color: #f36673;
        font-size: 30px;
        margin-top: 22px;
        font-weight: bold;
      }
    }

    .account__left--accNum {
      display: flex;
      margin-top: 30px;
      justify-content: space-between;
      padding-right: 50px;

      .account__left--accNum--inCenter {
        display: flex;
        flex-wrap: wrap;
        width: 88px;
        justify-content: center;
      }
    }
  }

  .account__center {
    width: 64%;
    background-color: white;
    // padding: 20px;
    height: 250px;
    overflow: auto;
    .account__center--precent {
      .account__center--precent--inCenter {
        display: flex;
        flex-wrap: wrap;
        width: 90px;
        justify-content: center;
        margin-top: 40px;
      }
    }

    .account__center--header {
      display: flex;
      margin-top: 10px;
      margin-left: 10px;
      flex-wrap: wrap;
      width: 450px;
      align-content: space-around;
      height: 190px;

      .account__center--header--inCenter {
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
  }

  .account__center::-webkit-scrollbar {
    display: none;
  }
}

.lineCenter {
  display: flex;
  width: 260px;
}

.inCenter__number {
  color: #515a6e;
  font-size: 22px;
  margin-top: 10px;
  font-weight: bold;
  width: 40px;
  display: flex;
  justify-content: center;
}

.inCenter__precent {
  color: #5c86fd;
  font-size: 22px;
}

.inCenter__precent::after {
  content: "%";
  font-weight: normal;
  font-size: 15px;
  margin-left: 4px;
}

.inCenter__img {
  .inCenter__img--head {
    width: 50px;
    height: 50px;
    border-radius: 50px;
  }
}

.inCenter__text {
  color: #515a6e;
  font-size: 14px;
  margin-top: 5px;
}

.active {
  color: #fd7c25;
  position: relative;

  // .activeLine {
  //   width: 26px;
  //   height: 2px;
  //   background-color: #2d8cf0;
  //   position: absolute;
  //   bottom: -8px;
  // }
}

.store {
  vertical-align: center;

  .store__lump {
    display: inline-block;
    width: 3px;
    height: 12px;
    background-color: #5b85fd;
  }

  .store__title {
    margin-left: 10px;
    color: #586884;
    font-weight: 700;
  }
}

.el-range-editor--small.el-input__inner {
  height: 30px;
}
</style>
