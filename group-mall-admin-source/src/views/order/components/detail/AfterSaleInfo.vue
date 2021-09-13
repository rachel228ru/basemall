<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:04:39
-->
<template>
  <div class="after" v-if="afterDetail">
    <div class="after__status">
      <div class="flex" style="margin-bottom: 10px">
        <div>
          <div>
            当前订单状态:{{ getAfterName(afterDetail.type) }}
            {{ getAfterStatusName(afterDetail.status) }}
          </div>
        </div>

        <div v-if="afterDetail.deadline">
          剩余 {{ computedResidualTime(afterDetail.deadline) }}
        </div>
      </div>
      <div class="tips">
        <template v-if="afterDetail.status === 'WAIT_FOR_BUSINESS_APPROVED'">
          如您逾期未响应申请，视作同意买家申请，系统会自动退款给买家
        </template>
        <!-- <template v-else>
          {{getAfterName(afterDetail.type)}} {{ getDetailAfterStatus(afterDetail.status) }}
        </template> -->
      </div>
      <!-- <div>无售后记录</div> -->
    </div>
    <div class="after__btns">
      <template
        v-if="
          isWaitSendRefund(afterDetail.type, afterDetail.status, detail.status)
        "
      >
        <el-button :disabled="loading" type="primary" @click="send"
          >发货</el-button
        >
        <el-button :disabled="loading" @click="agree">同意退款</el-button>
      </template>

      <template
        v-else-if="
          isWaitReview(afterDetail.type, afterDetail.status, detail.status)
        "
      >
        <el-button :disabled="loading" @click="disagree" type="primary"
          >拒绝退款</el-button
        >
        <el-button :disabled="loading" @click="agree">同意退款</el-button>
      </template>

      <template
        v-else-if="
          isExchange(afterDetail.type, afterDetail.status, detail.status)
        "
      >
        <el-button :disabled="loading" @click="agree" type="primary"
          >同意换货</el-button
        >
        <el-button :disabled="loading" @click="disagree">拒绝换货</el-button>
      </template>

      <template
        v-else-if="
          isReturnAndrefund(afterDetail.type, afterDetail.status, detail.status)
        "
      >
        <el-button :disabled="loading" @click="agree" type="primary"
          >同意退货退款</el-button
        >
        <el-button :disabled="loading" @click="disagree"
          >拒绝退货退款</el-button
        >
      </template>

      <template
        v-else-if="
          isWaitConfirm(afterDetail.type, afterDetail.status, detail.status)
        "
      >
        <el-button :disabled="loading" @click="agree" type="primary"
          >确认收到退货</el-button
        >
        <el-button :disabled="loading" @click="disagree">未收到退货</el-button>
      </template>
    </div>

    <div class="negotiate" v-if="negotiateList.length">
      <div class="negotiate--title">
        <h1>协商历史</h1>
      </div>
      <div class="negotiate--item" v-for="item of negotiateList" :key="item.id">
        <div
          class="negotiate--item__head"
          :style="{
            backgroundImage: `url(${item.userAvatarUrl})`,
          }"
        ></div>
        <div class="negotiate--item__content">
          <div class="content--name">
            <span>{{ item.userName }}</span> <span>{{ item.updateTime }}</span>
          </div>
          <div class="content--text">{{ item.info }}</div>
          <div class="content--images" v-if="item.image">
            <div
              v-for="sitem of item.image"
              :key="sitem"
              class="image--item"
              :style="{
                backgroundImage: `url(${sitem})`,
              }"
            ></div>
          </div>
          <!-- <div v-else>无</div> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Vue, Component, Watch, Prop } from "vue-property-decorator";
import {
  getAfterDetail,
  getNegotiateList,
  agree,
  disagree,
} from "@/api/order/afterSale";
import {
  getAfterName,
  getAfterStatusName,
  isWaitSendRefund,
  isExchange,
  isReturnAndrefund,
  getDetailAfterStatus,
  isWaitReview,
  isWaitConfirm,
} from "../../common/afterSale";

@Component
export default class AfterSaleInfo extends Vue {
  /** 订单详情 */
  @Prop({
    default() {
      return {};
    },
  })
  detail;

  @Prop({
    default() {
      return {};
    },
  })
  afterInfo;

  negotiateList = [];

  afterDetail = {};

  loading = false;

  @Watch("afterInfo", { immediate: true })
  handleChange({ id, orderId }) {
    if (!id) return;

    getAfterDetail(id)
      .then(res => {
        this.afterDetail = res.data;
      })
      .catch(() => {
        this.$message.warning("售后详情获取失败");
      });

    getNegotiateList(orderId)
      .then(res => {
        res.data.map(item => {
          if (item.image) {
            item.image = item.image.split(",");
          }
        });
        this.negotiateList = res.data;
      })
      .catch(() => {
        this.$message.warning("售后详情获取失败");
      });
  }

  setLoading(status) {
    this.loading = status;
  }

  agree() {
    this.setLoading(true);
    agree(this.afterInfo.id)
      .then(() => {
        this.$emit("reset", this.afterDetail.item);
      })
      .catch(() => {
        this.$message.warning("操作失败");
      })
      .finally(() => this.setLoading(false));
  }

  send() {
    disagree({
      afsId: this.afterDetail.id,
      refusalReason: "已发货",
    })
      .then(() => {
        this.$emit("reset", this.afterDetail.item);
      })
      .catch(() => {
        this.$message.warning("操作失败");
      });
  }

  disagree() {
    this.$prompt("拒绝原因", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      inputType: "textarea",
      inputPattern: /^[\s\S]*.*[^\s][\s\S]*$/,
      inputErrorMessage: "请输入拒绝原因",
    })
      .then(({ value }) => {
        this.setLoading(true);
        disagree({
          afsId: this.afterDetail.id,
          refusalReason: value,
        })
          .then(() => {
            this.$emit("reset", this.afterDetail.item);
          })
          .catch(() => {
            this.$message.warning("操作失败");
          })
          .finally(() => this.setLoading(false));
      })
      .catch(() => null);
    //agree
  }

  computedResidualTime(time) {
    const checkTime = i => {
      if (i < 10) {
        i = "0" + i;
      }
      return i;
    };

    let ts = new Date(time) - new Date(); //计算剩余的毫秒数
    let dd = parseInt(ts / 1000 / 60 / 60 / 24, 10); //计算剩余的天数
    let hh = parseInt((ts / 1000 / 60 / 60) % 24, 10); //计算剩余的小时数
    let mm = parseInt((ts / 1000 / 60) % 60, 10); //计算剩余的分钟数
    var ss = parseInt((ts / 1000) % 60, 10); //计算剩余的秒数
    dd = checkTime(dd);
    hh = checkTime(hh);
    mm = checkTime(mm);
    ss = checkTime(ss);
    return dd + "天" + hh + "时" + mm + "分" + ss + "秒";
  }

  isExchange = isExchange;

  isReturnAndrefund = isReturnAndrefund;

  getDetailAfterStatus = getDetailAfterStatus;

  /** 是否在待发货状态下申请退款 */
  isWaitSendRefund = isWaitSendRefund;

  /** 获取售后状态名称 */
  getAfterStatusName = getAfterStatusName;

  isWaitReview = isWaitReview;

  isWaitConfirm = isWaitConfirm;

  getAfterName = getAfterName;
}
</script>

<style lang="scss" scoped>
.after {
  &__status {
    width: 100%;
    padding: 20px 10px;
    background: #e8f4fe;
    border: 1px solid #88e2ff;

    .flex {
      @include flex(space-between);
    }

    .tips {
      font-size: 12px;
      margin-top: 5px;
    }
  }

  &__btns {
    margin-top: 15px;
  }

  .negotiate {
    .negotiate--title {
      h1 {
        font-size: 16px;
        padding: 10px 0;
        border-bottom: 1px solid rgb(233, 233, 233);
        margin-bottom: 20px;
      }
    }
    &--item {
      display: flex;
      margin: 20px 0 10px;
      padding-bottom: 20px;
      border-bottom: 1px solid rgb(233, 233, 233);

      &__head {
        width: 40px;
        height: 40px;
        background: #ccc;
        margin-right: 10px;
        background-size: cover;
      }
      &__content {
        width: 100%;
        .content--name {
          display: flex;
          justify-content: space-between;
          margin-bottom: 5px;
          font-size: 14px;
        }
        .content--images {
          margin-top: 10px;
          display: flex;
          justify-content: flex-start;
        }
        .image--item {
          width: 100px;
          height: 100px;
          background-size: cover;
          margin-right: 15px;
        }
      }
    }
  }
}
.modal-after_btns {
  margin: 15px 0;

  .ivu-btn-default {
    margin-left: 10px;
  }
}
</style>
