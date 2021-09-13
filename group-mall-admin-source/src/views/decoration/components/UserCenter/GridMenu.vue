<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:35:13
-->
<template>
  <div class="user__menu--grid">
    <div class="grid">
      <div class="grid__name">我的工具</div>
      <div class="grid__wrap">
        <div
          class="grid__navigator"
          v-for="gridItem in gridMenu"
          :key="gridItem.id"
          v-show="gridItem.hideMenu"
        >
          <img class="grid__navigator--image" :src="gridItem.menuIconUrl" />
          <div class="grid__navigator--right">
            <span class="grid__navigator--name">{{ gridItem.menuName }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import { MenuList } from "./formModel/UserCenter";

@Component
export default class MenuItem extends Vue {
  @Prop({
    default: () => {
      return [];
    },
  })
  gridMenu!: MenuList[];
}
</script>

<style lang="scss" scoped>
.grid {
  background-color: #ffffff;
  margin-bottom: 15px;
  border-radius: 5px;

  .grid__name {
    font-size: 14px;
    color: #45403c;
    padding: 15px 0 0 10px;
    font-weight: bolder;
  }

  .grid__wrap {
    @include flex(flex-start);
    flex-wrap: wrap;

    .grid__navigator {
      @include flex();
      flex-direction: column;
      flex: 0 0 33.33%;
      padding: 15px 0;

      .grid__navigator--image {
        width: 28px;
        height: 28px;
      }

      .grid__navigator--right {
        text-align: center;
        padding-top: 15px;
        display: flex;
        flex-direction: column;
        justify-content: center;

        .grid__navigator--name {
          font-size: 13px;
          color: #45403c;
        }

        .grid__navigator--text {
          font-size: 14px;
          color: #b2b2b2;
        }
      }
    }
  }
}
</style>
