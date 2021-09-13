<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 10:52:43
-->
<template>
  <m-container
    class="evaluation"
    :size.sync="query.size"
    :current.sync="query.current"
    :total.sync="total"
  >
    <template slot="form">
      <m-card
        class="evaluation__switch"
        hide-text="展开搜索条件"
        show-text="收起搜索条件"
      >
        <span>关闭后小程序端将不显示评价模块</span>
        <el-switch v-model="value" @change="setManageSetting"></el-switch>
      </m-card>
      <EvaluationForm v-model="query" @input="search"></EvaluationForm>
    </template>
    <m-table
      :data="data"
      :custom="true"
      :columns="columns"
      slot="content"
      :need-border="true"
    >
      <template v-slot:header="{ row }">
        <div style="width:100%">
          <span style="margin-right:50px">订单号：{{ row.orderId }}</span>
          <span style="margin-right:50px">评价时间：{{ row.createTime }}</span>
        </div>
      </template>
      <template v-slot:custom-body="{ row }">
        <template v-for="(goods, i) of row.itemList">
          <tr :key="i">
            <td>
              <div class="tw">
                <img class="user--avatar" :src="row.userAvatarUrl" />
                {{ row.userName }}
              </div>
            </td>
            <td>
              <div class="name">{{ goods.productName }}</div>
            </td>
            <td class="text--center">
              <template v-if="goods.rate">
                <i
                  v-for="star of goods.rate"
                  :key="star"
                  style="color:#FFD21F"
                  class="iconfont iconpingjiaxingxing"
                ></i>
              </template>
              <template v-else>0</template>
            </td>
            <td>
              <div style="line-height:19px">{{ goods.comment }}</div>
              <div v-if="goods.picture" class="evaluation__table--imgs">
                <img
                  v-for="(img, index) of goods.picture.split(',')"
                  :src="img"
                  alt=""
                  :key="index"
                />
              </div>
              <div v-if="goods.reply" class="evaluation__table--reply">
                <div style="line-height: 18px;">
                  商家回复：{{ goods.reply }}
                </div>
              </div>
            </td>
            <td>
              <div class="btn-container">
                <el-button
                  v-if="!goods.choice"
                  type="text"
                  @click="setChoice(goods.id)"
                  >精选</el-button
                >
                <el-button v-else type="text" @click="cancelChoice(goods.id)"
                  >取消精选</el-button
                >
              </div>
              <div class="btn-container">
                <el-button
                  v-if="!goods.reply"
                  type="text"
                  @click="reply(goods.id)"
                  >回复</el-button
                >
              </div>
            </td>
          </tr>
        </template>
      </template>
    </m-table>
  </m-container>
</template>

<script lang="ts">
/* eslint-disable indent */
import { Vue, Component, Watch } from "vue-property-decorator";
import EvaluationForm from "./components/EvaluationForm.vue";
import {
  getEvaluateList,
  setChoice,
  cancelChoice,
  reply,
  getManageSetting,
  setManageSetting,
} from "@/api/order";
import { evaluationQuery, filterEmpty } from "./common/order";
import { cloneDeep } from "lodash";
import { ApiEvaluteItemList, EvaluateQueryType } from "./orderType";

Component.registerHooks(["beforeRouteEnter"]);

@Component({
  components: {
    EvaluationForm,
  },
})
export default class Evaluation extends Vue {
  value = true;

  query: EvaluateQueryType = cloneDeep(evaluationQuery);

  get columns() {
    return [
      {
        label: "评价人",
        width: 200,
      },
      {
        label: "产品信息",
        width: 130,
      },
      {
        label: "商品评分",
      },
      {
        label: "评分内容",
        width: 250,
      },
      {
        label: "操作",
        width: 120,
      },
    ];
  }

  data: Array<ApiEvaluteItemList> = [];

  total = 0;

  /** 监听query变化 */
  @Watch("query", { deep: true })
  handleQueryChange(v: EvaluateQueryType) {
    this.getEvaluateList(filterEmpty({ ...this.$route.query, ...v }));
  }

  /** 获取评价列表 */
  beforeRouteEnter(
    to: { query: EvaluateQueryType },
    _form: any,
    next: (arg0: (vm: any) => void) => void,
  ) {
    getEvaluateList(Object.assign(evaluationQuery, to.query))
      .then(res => {
        next(vm => {
          const { list, total } = res.data;
          vm.setTotal(total);
          vm.data = list;
        });
      })
      .catch(err => {
        alert(err || "评价列表获取失败");
      });
  }

  search(query: EvaluateQueryType) {
    this.query.pageNum = 1;
    getEvaluateList(query);
  }

  /** 获取评价列表 */
  getEvaluateList(query = {}, next?: (() => void) | undefined) {
    query = Object.assign({}, this.query, query);

    getEvaluateList(query)
      .then(res => {
        const { list, total } = res.data;
        this.setTotal(total);
        this.data = list;
        if (next) {
          next();
        }
      })
      .catch(err => {
        alert(err || "评价列表获取失败");
      });
  }

  created() {
    this.getManageSetting();
  }

  setManageSetting(openEvaluate: boolean) {
    setManageSetting({ openEvaluate })
      .then(() => null)
      .catch(err => {
        this.$message.warning(err);
      });
  }

  getManageSetting() {
    getManageSetting()
      .then(res => {
        this.value = res.data;
      })
      .catch(err => {
        this.$message.warning(err);
      });
  }

  setTotal(v: number) {
    this.total = v;
  }

  /** 设置精选 */
  setChoice(id: string) {
    setChoice([id])
      .then(() => {
        this.getEvaluateList();
        this.$message.success("设置成功");
      })
      .catch(err => {
        this.$message.warning(err || "设置失败");
      });
  }

  /** 取消精选 */
  cancelChoice(id: string) {
    cancelChoice([id])
      .then(() => {
        this.getEvaluateList();
        this.$message.success("设置成功");
      })
      .catch(err => {
        this.$message.warning(err || "设置失败");
      });
  }

  /** 回复订单 */
  reply(id: string) {
    this.$prompt("", `商家回复`, {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      inputType: "textarea",
      inputPattern: /^[\s\S]*.*[^\s][\s\S]*$/,
      inputErrorMessage: "回复不可为空",
    }).then((res: any) => {
      reply({ reply: res.value, id })
        .then(() => {
          this.getEvaluateList();
          this.$message.success("回复成功");
        })
        .catch(err => {
          this.$message.warning(err || "回复失败");
        });
    });
  }
}
</script>

<style lang="scss" scoped>
@import "../../assets/styles/order/evaluation.scss";
</style>
