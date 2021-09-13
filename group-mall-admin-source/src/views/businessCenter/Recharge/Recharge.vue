<template>
  <div>
    <m-card>
      <div>
        <div class="bar">账户余额（元）</div>
        <div class="bar">
          <span v-if="inputType" class="money">{{ userInfo.balance }}</span>
          <span v-if="!inputType" class="money">****</span>
          <span @click="inputType = !inputType" class="money_btn">
            <i class="iconfont iconeye" v-if="inputType"></i>
            <i class="iconfont iconicon_password_eye_on" v-if="!inputType"></i>
          </span>
          <el-button @click="rechargeHandle" type="primary">
            充值
          </el-button>
        </div>
      </div>
    </m-card>
    <div class="activeTop">
      <div class="tip">
        <div class="tip__lump"></div>
        <span class="tip__title">充值记录</span>
      </div>
      <div class="form">
        <el-select
          v-model="dataForm.status"
          placeholder="充值状态"
          @change="getDataList"
        >
          <el-option
            v-for="item in rechargeStatusList"
            :key="item.id"
            :label="item.label"
            :value="item.id"
          ></el-option>
        </el-select>
        <el-select
          v-model="dataForm.platfromType"
          placeholder="平台类型"
          @change="getDataList"
        >
          <el-option label="全部平台" value=""></el-option>
          <el-option
            v-for="item in platformList"
            :key="item.id"
            :label="item.label"
            :value="item.id"
          ></el-option>
        </el-select>
        <span class="activeTop__sel">充值时间</span>
        <el-date-picker
          v-model="dataForm.time"
          type="daterange"
          :default-time="['00:00:00', '23:59:59']"
          value-format="yyyy-MM-dd HH:mm:ss"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="getDataList"
        ></el-date-picker>
      </div>
    </div>
    <el-table
      ref="multipleTable"
      :data="rechargeList"
      tooltip-effect="dark"
      style="width: 100%;margin-top:20px"
      :header-cell-style="{
        background: '#f6f8fa',
        'font-weight': 'normal',
        color: 'black',
      }"
      :row-style="{ height: '50px' }"
    >
      <el-table-column label="充值编号" prop="rechargeNum"></el-table-column>
      <el-table-column label="充值平台" prop="paySource">
        <template slot-scope="scope">
          <span v-if="scope.row.paySource === 1">商户</span>
          <span v-else>系统管理员</span>
          <!--          <span v-if="scope.row.paySource === 2">代理商</span>-->
          <!--          <span v-if="scope.row.paySource === 3">渠道商</span>-->
        </template>
      </el-table-column>
      <el-table-column label="充值时间" prop="createTime"></el-table-column>
      <el-table-column
        label="充值金额（元）"
        prop="payAmount"
      ></el-table-column>
      <el-table-column
        label="账户余额（元）"
        prop="accountAmount"
      ></el-table-column>
      <el-table-column label="支付方式" prop="payType">
        <template slot-scope="scope">
          <span v-if="scope.row.payType === 1">支付宝</span>
          <span v-if="scope.row.payType === 2">微信</span>
          <span v-if="scope.row.payType === 3">汇款支付</span>
          <span v-if="scope.row.payType > 3">系统支付</span>
        </template>
      </el-table-column>
      <el-table-column label="充值状态" prop="status">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0">生成订单</span>
          <span v-if="scope.row.status === 1">充值中</span>
          <span v-if="scope.row.status === 2">充值成功</span>
          <span v-if="scope.row.status === 3">已关闭</span>
        </template>
      </el-table-column>
    </el-table>
    <PageManage
      :pageSize="pageSize"
      :pageNum="pageNum"
      :total="total"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    ></PageManage>
    <!--充值弹窗-->
    <RechargeMoney
      :user-info="userInfo"
      :visible.sync="rechargeVisible"
      @refreshDataList="getDataList"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import PageManage from "@/components/PageManage.vue";
import RechargeMoney from "@/views/businessCenter/Recharge/RechargeMoney.vue";
import { getRechargeList } from "@/api/businessCenter/recharge";
import storage from "@/libs/storage";
import { getAccountInfo } from "@/api/sign";

@Component({
  components: {
    PageManage,
    RechargeMoney,
  },
})
export default class CouponIndex extends Vue {
  // 账户余额显示状态
  inputType = false;

  // 充值列表数据
  rechargeList = [];

  // 充值弹窗
  rechargeVisible = false;

  userInfo = {
    balance: null,
    phone: null,
  };

  /** 搜索类型 */
  rechargeStatusList = [
    {
      id: "0",
      label: "全部状态",
    },
    {
      id: "1",
      label: "充值中",
    },
    {
      id: "2",
      label: "充值成功",
    },
  ];

  /** 平台类型 */
  platformList = [
    {
      id: 1,
      label: "系统管理员",
    },
    {
      id: 2,
      label: "商家",
    },
  ];

  dataForm = {
    time: "",
    status: "",
    platfromType: "",
  };

  pageSize = 10;

  pageNum = 1;

  total = 0;

  mounted() {
    this.getRecentAccountInfo();
    this.getDataList();
  }

  /**
   * 获取充值列表
   */
  async getDataList() {
    try {
      const form = this.dataForm;
      const param = {
        page: this.pageNum,
        size: this.pageSize,
        status: form.status,
        platfromType: form.platfromType,
        payStartTime: form.time !== null ? form.time[0] : "",
        payEndTime: form.time !== null ? form.time[1] : "",
      };
      const response = await getRechargeList(param);
      const { code, data } = response;
      if (code === 200) {
        this.rechargeList = data.list;
        this.pageSize = data.size;
        this.total = data.total;
      } else {
        this.$message({
          type: "warning",
          message: "获取充值列表失败！",
        });
      }
    } catch (err) {
      console.log(err);
    }
  }

  // 根据请求token获取当前用户最新的信息
  async getRecentAccountInfo() {
    if (storage.get("token")) {
      const response = await getAccountInfo();
      const { code, data } = response;
      if (code === 200) {
        this.userInfo = data;
      }
    }
  }

  /**
   * 充值点击事件
   */
  rechargeHandle() {
    this.rechargeVisible = true;
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val) {
    this.pageSize = val;
    this.getDataList();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val) {
    this.pageNum = val;
    this.getDataList();
  }
}
</script>

<style lang="scss" scoped>
.bar {
  padding: 10px;
}

.money {
  display: inline-block;
  min-width: 100px;
  font-size: 24px;
}

.money_btn {
  padding-right: 45px;
}

.activeTop {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 25px 0;

  &__sel {
    padding: 0 25px;
  }
}
</style>
