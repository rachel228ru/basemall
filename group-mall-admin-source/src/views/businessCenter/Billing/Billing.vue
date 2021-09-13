<template>
  <div class="billing__setting--form">
    <div class="billing__tip">
      <div class="billing__container">
        <div class="billing__tip--lump" style="background-color:#FE0000"></div>
        <span class="billing__tip--title">抬头信息</span>
      </div>
      <div class="billing__tip--btn" @click="updateData">添加抬头</div>
    </div>
    <div class="billing__content">
      <div
        v-if="!!!invoiceList || (!!invoiceList && invoiceList.length === 0)"
        class="empty-info"
      >
        暂无数据
      </div>
      <div
        class="billing__item"
        v-for="invoice in invoiceList"
        :key="invoice.id"
      >
        <div class="billing__item--title">
          <span>{{ invoice.invoiceRiseName }}</span>
        </div>
        <div class="billing__item--detail">
          <div>
            <el-tag>{{ invoice.invoiceTaxpayerNum }}</el-tag>
          </div>
          <div>
            <i class="el-icon-edit" @click="updateData(invoice)"></i>
          </div>
        </div>
        <div class="billing__item--default" v-if="invoice.defaultStatus">
          默认
        </div>
      </div>
    </div>
    <div class="billing__tip">
      <div>
        <div class="billing__tip--lump"></div>
        <span class="billing__tip--title">电子发票邮箱信息</span>
      </div>
    </div>
    <div class="billing__content">
      <el-form class="billing__form" inline>
        <el-form-item label="电子发票接收邮箱">
          <el-input v-model="email" placeholder="请输入发票接收邮箱"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <el-button type="primary" @click="updateEmail">确认邮箱</el-button>
    <UpdateInvoice
      :visible.sync="dialogVisible"
      :invoice="currentInvoice"
      @refreshDataList="getDataList"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import { getInvoiceList } from "@/api/businessCenter/invoice";
import UpdateInvoice from "@/views/businessCenter/Billing/UpdateInvoice.vue";
import { changeEmail } from "@/api/businessCenter/setting";
import storage from "@/libs/storage";
import { getAccountInfo } from "@/api/sign";
import { emailReg } from "@/libs/validate";

@Component({
  components: {
    UpdateInvoice,
  },
})
export default class Billing extends Vue {
  /** 弹窗 */
  dialogVisible = false;

  currentInvoice = {};

  /** 发票列表 */
  invoiceList = [];

  email = "";

  mounted() {
    this.getDataList();
    this.getRecentAccountInfo();
  }

  // 抬头列表
  async updateEmail() {
    if (this.email) {
      if (!emailReg.test(this.email)) {
        this.$message({
          type: "warning",
          message: "请输入正确的邮箱格式",
        });
      } else {
        try {
          const response = await changeEmail({
            email: this.email,
          });
          const { code } = response;
          if (code === 200) {
            this.$message({
              type: "success",
              message: "更新邮箱成功！",
            });
            await this.getRecentAccountInfo();
          } else {
            this.$message({
              type: "warning",
              message: "更新邮箱失败！",
            });
          }
        } catch (e) {
          console.log(e);
        }
      }
    } else {
      this.$message({
        type: "warning",
        message: "请输入邮箱",
      });
    }
  }

  /**
   * 编辑操作
   */
  updateData(invoice) {
    if (!(invoice instanceof MouseEvent)) {
      this.currentInvoice = invoice;
    } else {
      this.currentInvoice = {};
    }
    this.$nextTick(() => {
      this.dialogVisible = true;
    });
  }

  // 抬头列表
  async getDataList() {
    try {
      const response = await getInvoiceList();
      const { code, data } = response;
      if (code === 200) {
        this.invoiceList = data;
      } else {
        this.$message({
          type: "warning",
          message: "获取发票抬头失败！",
        });
      }
    } catch (e) {
      console.log(e);
    }
  }

  // 根据请求token获取当前用户最新的信息
  async getRecentAccountInfo() {
    if (storage.get("token")) {
      const response = await getAccountInfo();
      const { code, data } = response;
      if (code === 200) {
        this.email = data.email;
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.billing__setting--form {
  font-size: 13px;
  color: #767676;

  .billing__tip {
    vertical-align: center;
    background-color: rgba(246, 248, 250, 1);
    padding: 15px 15px 15px 30px;
    display: flex;
    justify-content: space-between;

    .billing__container {
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .billing__tip--title {
      margin-left: 12px;
      color: #586884;
      font-weight: 700;
    }

    .billing__tip--right {
      margin-left: 40px;
      color: #cacaca;
    }

    .billing__tip--btn {
      border: 1px solid #007cff;
      padding: 3px;
      color: #007cff;
    }
  }
}

.billing__tip--lump {
  display: inline-block;
  width: 3px;
  height: 12px;
  background-color: #00db52;
}
.billing__content {
  display: flex;
  flex-wrap: wrap;
}

.billing__item {
  font-size: 12px;
  background-color: #f9f9f9;
  width: 375px;
  padding: 10px 15px;
  margin: 15px;
  position: relative;
}

.billing__item--detail {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 5px;
}

.billing__item--default {
  position: absolute;
  right: 0;
  top: 0;
  padding: 2px 5px;
  border-bottom-left-radius: 10px;
  background-color: #007cff;
  color: #ffffff;
}

.billing__form {
  margin: 50px;
}

.empty-info {
  width: 100%;
  padding: 50px 0;
  min-height: 100px;
  text-align: center;
  color: #c0c4cc;
  font-size: 14px;
}
</style>
