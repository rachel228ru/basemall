<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 14:39:04
-->
<template>
  <div class="ranking">
    <div style="width: 645px">
      <div class="ranking__rank">
        <div
          style="width:120px;display:flex;flex-wrap:wrap;justify-content: center;"
        >
          <div
            :class="[
              rankActive === 0
                ? 'ranking__rank--noType'
                : 'ranking__rank--type',
            ]"
            @click="clickRankTop(0)"
          >
            商品交易量排行
          </div>
          <div :class="[rankActive === 0 ? 'ranking__rank--Line' : '']"></div>
        </div>
        <div
          style="width:120px;display:flex;flex-wrap:wrap;justify-content: center;"
        >
          <div
            :class="[
              rankActive === 1
                ? 'ranking__rank--noType'
                : 'ranking__rank--type',
            ]"
            @click="clickRankTop(1)"
          >
            商品交易额排行
          </div>
          <div :class="[rankActive === 1 ? 'ranking__rank--Line' : '']"></div>
        </div>
      </div>
    </div>
    <!-- 商品销量排行榜 商品交易量 -->
    <div
      v-if="topName === '2' && rankActive === 0"
      style="width:100%; height:450px;"
    >
      <div
        class="ranking__list"
        v-for="(item, index) in productVBRanking"
        :key="index"
      >
        <div class="ranking__list--line" v-if="productVBRanking">
          <div class="ranking__list--line--left">
            <span class="indexStyle">{{ index + 1 }}</span>
            <img
              :src="item.url"
              style="width:50px;height:50px;border-radius:50px"
            />
            <div class="userBox">
              <div class="userBox__name">{{ item.nick }}</div>
              <div
                class="userBox__community"
                style="color:#FF4D4D;font-weight:bold"
              >
                ￥{{ item.price.toFixed(2) }}
              </div>
            </div>
          </div>
          <div class="ranking__list--line--right">{{ item.tradingVolume }}</div>
        </div>
        <div v-else class="noRank">暂无排行数据~</div>
      </div>
    </div>
    <!-- 商品销量排行榜 商品交易额 -->
    <div
      v-if="
        topName === '2' && rankActive === 1 && gvmSproductVBRanking.length > 0
      "
      style="width:100%; height:450px;"
    >
      <div
        class="ranking__list"
        v-for="(item, index) in gvmSproductVBRanking"
        :key="index"
      >
        <div class="ranking__list--line">
          <div class="ranking__list--line--left">
            <span class="indexStyle">{{ index + 1 }}</span>
            <img
              :src="item.url"
              style="width:50px;height:50px;border-radius:50px"
            />
            <div class="userBox">
              <div class="userBox__name">{{ item.nick }}</div>
              <div
                class="userBox__community"
                style="color:#FF4D4D;font-weight:bold"
              >
                ￥{{ item.price.toFixed(2) }}
              </div>
            </div>
          </div>
          <div class="ranking__list--line--right">{{ item.tradeVolume }}</div>
        </div>
      </div>
    </div>
    <div v-else class="noRank">暂无排行数据~</div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";

@Component
export default class Ranking extends Vue {
  active = 0;

  topName = "0";

  rankActive = 0;

  /** 商品销量排行榜 */
  productVBRanking = [
    {
      nick: "宝宝汉服女童连衣裙夏装婴儿洋气儿童裙子女孩夏装潮时髦",
      price: 45,
      tradingVolume: 20,
      url:
        "https://oss-tencent.bgniao.cn//gruul/20210726/7f994c1d6d2042b583fec3e35331600c.jpg",
    },
    {
      nick: "笛莎童装女童连衣裙2021秋季新款小宝宝儿童可爱玩偶衬衫裙子",
      price: 109,
      tradingVolume: 15,
      url:
        "https://oss-tencent.bgniao.cn//gruul/20210726/7f994c1d6d2042b583fec3e35331600c.jpg",
    },
  ];

  gvmSproductVBRanking = [
    {
      nick: "笛莎童装女童连衣裙2021秋季新款小宝宝儿童可爱玩偶衬衫裙子",
      price: 109,
      tradingVolume: 1635,
      url:
        "https://oss-tencent.bgniao.cn//gruul/20210726/7f994c1d6d2042b583fec3e35331600c.jpg",
    },
    {
      nick: "宝宝汉服女童连衣裙夏装婴儿洋气儿童裙子女孩夏装潮时髦",
      price: 45,
      tradingVolume: 800,
      url:
        "https://oss-tencent.bgniao.cn//gruul/20210726/7f994c1d6d2042b583fec3e35331600c.jpg",
    },
  ];

  mounted() {
    this.topName = "2";
  }

  /**
   * 点击顶部一级导航
   */
  handleClick(tab: { name: string }) {
    this.topName = tab.name;
  }

  clickTop(type: number) {
    this.active = type;
  }

  clickRankTop(type: number) {
    this.rankActive = type;
  }
}
</script>

<style lang="scss" scoped>
.ranking {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  justify-content: space-between;
  background-color: white;
  margin-top: 10px;
  // padding: 20px;

  .ranking__rank {
    display: flex;
    width: 100%;
    margin-top: 10px;
    align-items: center;

    .ranking__rank--type {
      width: 120px;
      height: 2px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #6c7485;
      font-size: 12px;
      font-weight: bold;
      cursor: pointer;
    }

    .ranking__rank--Line {
      width: 90px;
      height: 2px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 13px;
      background-color: #fd7c25;
    }

    .ranking__rank--noType {
      width: 120px;
      height: 30px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fd7c25;
      font-size: 12px;
      font-weight: bold;
      cursor: pointer;
    }
  }

  .ranking__list {
    margin-top: 10px;
    width: 100%;
    padding: 10px;
    padding-right: 40px;

    .ranking__list--line {
      display: flex;
      width: 100%;
      align-items: center;
      justify-content: space-between;

      .ranking__list--line--left {
        display: flex;
        width: 560px;
        align-items: center;
      }

      .ranking__list--line--right {
        color: #515a6e;
        margin-left: 20px;
        font-weight: bold;
      }
    }
  }
}

.noRank {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 200px;
  color: #adaeb0;
  font-size: 15px;
}

.indexStyle {
  width: 25px;
  height: 25px;
  color: #fd9047;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  margin-right: 20px;
}

.userBox {
  display: flex;
  margin-left: 20px;
  flex-wrap: wrap;
  width: 370px;

  .userBox__name {
    width: 300px;
    color: #515a6e;
  }

  .userBox__community {
    width: 340px;
    margin-top: 10px;
    color: #868c9a;
  }
}

/deep/ .el-tabs--border-card {
  border: none;
  width: 100%;
  box-shadow: none;
  -webkit-box-shadow: none;
}

/deep/ .el-tabs--border-card > .el-tabs__content {
  padding: 0px;
}
</style>
