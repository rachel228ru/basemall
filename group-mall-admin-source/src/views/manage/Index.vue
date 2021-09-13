<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 17:19:51
-->
<template>
  <div>
    <div style="background-color:#EEF1F6">
      <div style="padding:10px 0px;background-color:white">
        <div class="chart">
          <div class="chart__top">
            <div class="chart__top--left">
              实时概况
              <span class="chart__top--left--update"
                >更新时间：{{ dayQuery.updateTime }}</span
              >
            </div>
            <div>
              日期
              <el-date-picker
                v-model="dateValue"
                type="daterange"
                range-separator="-"
                @change="selectTime"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 230px;margin-left:20px"
              ></el-date-picker>
            </div>
          </div>
          <div class="chart__top">
            <div class="chart__top--account">
              <div class="chart__top--account--content">
                <div class="titles">交易量</div>
                <div class="todayAccount">
                  {{ dayQuery.trade.transactionVolume }}
                  <i
                    class="iconfont iconxiayi iconClass"
                    v-if="
                      dayQuery.trade.transactionVolume <
                        dayQuery.yesterdayTrade.transactionVolume &&
                        dayQuery.yesterdayTrade.transactionVolume
                    "
                  />
                  <i
                    class="iconfont iconshangyi iconClass"
                    v-if="
                      dayQuery.trade.transactionVolume >
                        dayQuery.yesterdayTrade.transactionVolume &&
                        dayQuery.yesterdayTrade.transactionVolume
                    "
                  />
                </div>
                <div class="lastDay" v-if="showYes">
                  昨日 {{ dayQuery.yesterdayTrade.transactionVolume }}
                </div>
              </div>
              <div class="chart__top--account--content">
                <div class="titles">交易额(元)</div>
                <div class="todayAccount">
                  {{ dayQuery.trade.turnover }}
                  <i
                    class="iconfont iconxiayi iconClass"
                    v-if="
                      dayQuery.trade.turnover <
                        dayQuery.yesterdayTrade.turnover &&
                        dayQuery.yesterdayTrade.turnover
                    "
                  />
                  <i
                    class="iconfont iconshangyi iconClass"
                    v-if="
                      dayQuery.trade.turnover >
                        dayQuery.yesterdayTrade.turnover &&
                        dayQuery.yesterdayTrade.turnover
                    "
                  />
                </div>
                <div class="lastDay" v-if="showYes">
                  昨日 {{ dayQuery.yesterdayTrade.turnover }}
                </div>
              </div>
              <div class="chart__top--account--content">
                <div class="titles">浏览量</div>
                <div class="todayAccount">
                  {{ dayQuery.trade.views ? dayQuery.trade.views : 0 }}
                  <i
                    class="iconfont iconxiayi iconClass"
                    v-if="
                      dayQuery.trade.views < dayQuery.yesterdayTrade.views &&
                        dayQuery.yesterdayTrade.views
                    "
                  />
                  <i
                    class="iconfont iconshangyi iconClass"
                    v-if="
                      dayQuery.trade.views > dayQuery.yesterdayTrade.views &&
                        dayQuery.yesterdayTrade.views
                    "
                  />
                </div>
                <div class="lastDay" v-if="showYes">
                  昨日
                  {{
                    dayQuery.yesterdayTrade.views
                      ? dayQuery.yesterdayTrade.views
                      : 0
                  }}
                </div>
              </div>
            </div>
            <div id="main" style="height:320px;width:660px;right: -20px;"></div>
          </div>
        </div>
      </div>
      <OrderMsg :orderQuery="orderQuery" />
      <Account :accountQuery="accountQuery" />
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import Exception from "@/components/Exception.vue";
import echarts from "echarts";
import OrderMsg from "./components/OrderMsg.vue";
import Account from "./components/Account.vue";
import Ranking from "./components/Ranking.vue";
import {
  ManageAccountQuery,
  ManageOrderQuery,
  ManageQueryType,
  ManageState,
} from "./manageType";
import { getManageOrder } from "@/api/manage/manage";
import DateUtil from "@/store/modules/date";

@Component({
  components: {
    Exception,
    OrderMsg,
    Account,
    Ranking,
  },
})
export default class ManageIndex extends Vue implements ManageState {
  name = "manageIndex";

  dateValue: Array<string> = [];

  /** 实时概况 */
  dayQuery = {
    updateTime: "2021-09-02 14:03:09",
    yesterdayTrade: {
      transactionVolume: 40,
      turnover: 250,
      brokerage: 1044.24,
      views: 159,
    },
    trade: {
      transactionVolume: 120,
      turnover: 3000,
      brokerage: 0,
      views: 356,
    },
    tradeLineVos: [
      {
        transactionVolume: 20,
        turnover: 300,
        date: "2021-08-27",
      },
      {
        transactionVolume: 50,
        turnover: 400,
        date: "2021-08-28",
      },
      {
        transactionVolume: 10,
        turnover: 200,
        date: "2021-08-29",
      },
      {
        transactionVolume: 80,
        turnover: 1000,
        date: "2021-08-30",
      },
      {
        transactionVolume: 100,
        turnover: 1500,
        date: "2021-08-31",
      },
      {
        transactionVolume: 40,
        turnover: 2500,
        date: "2021-09-01",
      },
      {
        transactionVolume: 120,
        turnover: 3000,
        date: "2021-09-02",
      },
    ],
  } as ManageQueryType;

  /** 交易概况 */
  orderQuery = {
    shipped: 0,
    waitForPay: 0,
    waitForPickup: 0,
    waitForSend: 0,
  } as ManageOrderQuery;

  /** 访客统计 */
  accountQuery = {
    pv: 42,
    totalUv: 1753,
    uv: 8,
  } as ManageAccountQuery;

  showYes = true;

  mounted() {
    this.init();
  }

  /**
   * 初始化信息
   */
  init() {
    this.drawLine();
    this.getOrder();
  }

  drawLine() {
    const dateList: string[] = [];
    const transactionVolumeList: number[] = [];
    const turnoverList: number[] = [];
    const tradeLineVos = this.dayQuery.tradeLineVos;
    tradeLineVos.forEach(item => {
      item.date = new DateUtil("").getMDs(item.date);
      dateList.push(item.date);
      transactionVolumeList.push(item.transactionVolume);
      turnoverList.push(item.turnover);
    });
    // 基于准备好的dom，初始化echarts实例
    const myChart = echarts.init(document.getElementById("main"));
    // 绘制图表
    myChart.setOption({
      backgroundColor: "white",
      tooltip: {
        trigger: "axis",
      },
      grid: {
        left: "3%",
        right: "4%",
        bottom: "3%",
        containLabel: true,
      },
      xAxis: {
        boundaryGap: false,
        data: dateList,
      },
      yAxis: {
        axisTick: {
          show: false,
        },
        splitLine: {
          // 网格线
          lineStyle: {
            type: "dashed", // 设置网格线类型 dotted：虚线   solid:实线
          },
          show: true, // 隐藏或显示
        },
      },
      series: [
        {
          name: "交易额",
          type: "line",
          smooth: true,
          areaStyle: {
            color: "#FFF9F5",
          },
          data: turnoverList,
          itemStyle: {
            normal: { color: "#FD7C25" },
          },
        },
        {
          name: "交易量",
          type: "line",
          smooth: true,
          areaStyle: {
            color: "#F4F2F5",
          },
          data: transactionVolumeList,
          itemStyle: {
            normal: { color: "#0C67F5" },
          },
        },
      ],
    });
  }

  /**
   * 选择时间
   */
  selectTime(e: Array<Date>) {
    if (e) {
      this.showYes = false;
    } else {
      this.dateValue = [];
      this.showYes = true;
    }
  }

  /**
   * 获取订单数据
   */
  getOrder() {
    getManageOrder()
      .then(res => {
        this.orderQuery = res.data;
      })
      .catch(err => {
        this.$message.error(err || "网络错误");
      });
  }
}
</script>

<style lang="scss" scoped>
.chart {
  margin: 0px 20px;
  padding-top: 10px;
  height: 390px;
  background-color: white;

  .chart__top {
    display: flex;
    justify-content: space-between;

    .chart__top--left {
      font-size: 14px;
      font-weight: bold;
      color: #5f6779;

      .chart__top--left--update {
        margin-left: 20px;
        color: #a8adb7;
        font-weight: normal;
      }
    }

    .chart__top--account {
      margin-top: 40px;
      width: 350px;
      height: 280px;
      display: flex;
      flex-wrap: wrap;
      .chart__top--account--content {
        width: 150px;
        height: 120px;
        background-color: #fbfbfb;
        margin: 10px 10px 10px 0px;
        padding-top: 10px;
        // display: flex;
        // justify-content: center;
        // flex-wrap: wrap;
      }
    }
  }
}

.iconClass {
  margin-left: 5px;
  color: #a1a1a1;
  font-size: 20px;
}

.titles {
  padding-left: 40px;
  width: 150px;
  font-size: 14px;
  color: #969ba7;
  margin-top: 10px;
}

.todayAccount {
  width: 150px;
  padding-left: 40px;
  display: flex;
  font-size: 26px;
  font-weight: bold;
  align-items: center;
  color: #586074;
  margin-top: 12px;
}

.lastDay {
  padding-left: 40px;
  width: 150px;
  display: flex;
  font-size: 12px;
  align-items: center;
  color: #a6abb5;
  margin-bottom: 10px;
  margin-top: 12px;
}
</style>
