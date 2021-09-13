<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:33:12
-->
<template>
  <div class="order order__setting--form">
    <div class="order__tip">
      <div class="order__tip--lump" style="background: #08cc00"></div>
      <span class="order__tip--title">下单设置</span>
    </div>
    <el-form ref="form" :rules="rules" :model="form" label-width="120px">
      <el-form-item label="订单播报" required>
        <el-radio-group v-model="form.orderNotify">
          <el-radio :label="true">开启</el-radio>
          <el-radio :label="false">关闭</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="下单表单" required>
        <el-table :data="forms" style="width: 100%">
          <el-table-column prop="key" label="表单名称"> </el-table-column>
          <el-table-column prop="type" label="表单格式">
            <template slot-scope="scope">
              {{ getFormName(scope.row.type) }}
            </template>
          </el-table-column>
          <el-table-column label="必填" prop="required">
            <template slot-scope="scope">
              {{ scope.row.required ? "是" : "否" }}
            </template>
          </el-table-column>
          <el-table-column label="提示语" prop="placeholder"> </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                @click="handleEdit(scope.row, scope.$index)"
                type="text"
                size="small"
                >编辑</el-button
              >

              <el-button
                @click="handleDel(scope.row, scope.$index)"
                type="text"
                size="small"
                :disabled="forms.length === 1"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <el-button
          class="setupcon__item3--addnew"
          type="text"
          @click="toggleDialogVisible"
          >添加
        </el-button>
      </el-form-item>
    </el-form>

    <div class="order__tip">
      <div class="order__tip--lump" style="background: #08cc00"></div>
      <span class="order__tip--title">订单设置</span>
    </div>
    <el-form ref="form" :rules="rules" :model="form" label-width="120px">
      <el-form-item label="未支付订单" prop="normalOrderOvertime">
        <el-row>
          <el-col :span="8">
            <el-input-number
              v-model.number="time.day"
              @input="timeUpdate"
              class="input--item"
              :min="0"
              :max="60"
              :controls="false"
            ></el-input-number
            >天
            <el-input-number
              v-model.number="time.hour"
              @input="timeUpdate"
              class="input--item"
              :min="0"
              :max="60"
              :controls="false"
            ></el-input-number
            >小时
            <el-input-number
              v-model.number="time.min"
              @input="timeUpdate"
              class="input--item"
              :min="0"
              :max="60"
              :controls="false"
            ></el-input-number
            >分钟
          </el-col>
          <el-col :span="3" style="padding-left: 10px; font-size: 12px">
            后自动关闭
          </el-col>
          <el-col :span="13" style="font-size: 12px; padding-left: 15px">
            最长可设置30天，最短可设置15分钟。
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="已发货订单" prop="confirmOvertime">
        <el-row>
          <el-col :span="8">
            <el-input-number
              :min="0"
              step-strictly
              :step="1"
              :controls="false"
              v-model.number="form.confirmOvertime"
            ></el-input-number>
          </el-col>
          <el-col :span="3" style="padding-left: 10px; font-size: 12px">
            天后自动确认收货
          </el-col>
          <el-col :span="13" style="font-size: 12px; padding-left: 15px">
            <span
              >请考虑物流运输时间，最长可设置30天，0代表发货后系统自动确认收货</span
            >
          </el-col>
        </el-row>
      </el-form-item>

      <el-form-item label="已完成订单" prop="finishOvertime">
        <el-row>
          <el-col :span="8">
            <el-input-number
              :min="0"
              step-strictly
              :step="1"
              :controls="false"
              v-model.number="form.finishOvertime"
            ></el-input-number>
          </el-col>
          <el-col :span="3" style="padding-left: 10px; font-size: 12px">
            天后自动关闭售后
          </el-col>

          <el-col :span="13" style="font-size: 12px; padding-left: 15px">
            <span
              >填写则买家可在确认收货后进行售后申请，最长可设置30天，0代表确认收货后无法申请售后。</span
            >
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>

    <div class="order__tip">
      <div class="order__tip--lump" style="background: #ff0200"></div>
      <span class="order__tip--title">售后设置</span>
    </div>
    <el-form ref="form" :rules="rules" :model="form" label-width="120px">
      <el-form-item label="申请售后时间" prop="userReturnOvertime">
        <el-row>
          <el-col :span="11">
            <el-input-number
              :min="0"
              :step="1"
              step-strictly
              :controls="false"
              v-model="form.userReturnOvertime"
            ></el-input-number>
            天
          </el-col>
          <el-col :span="13" style="font-size: 12px; padding-left: 15px">
            申请售后的时间。
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="售后审核时间" prop="merchantConfirmOvertime">
        <el-row>
          <el-col :span="11">
            <el-input-number
              :step="1"
              :min="1"
              step-strictly
              v-model="form.merchantConfirmOvertime"
            ></el-input-number>
            天
          </el-col>
          <el-col :span="13" style="font-size: 12px; padding-left: 15px">
            <span
              >买家申请售后卖家审核时间，温馨提示：默认是3天，设置后以设置时间为准！</span
            >
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <div class="order__tip">
      <div class="order__tip--lump" style="background: #2e8cf0"></div>
      <span class="order__tip--title">快递设置</span>
    </div>
    <el-form ref="form" :rules="rules" :model="form" label-width="120px">
      <el-form-item label="快宝id">
        <el-row>
          <el-col :span="11">
            <el-input v-model="form.kdAppId"></el-input>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="快宝API KEY">
        <el-row>
          <el-col :span="11">
            <el-input v-model="form.kdAppKey"></el-input>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label=" ">
        <el-button @click="setOrderSetting" type="primary">保存</el-button>
      </el-form-item>
    </el-form>

    <el-dialog title="自定义表单" :visible.sync="dialogVisible" width="30%">
      <el-form
        ref="coustomForm"
        :rules="coustomRules"
        :model="coustomForm"
        label-width="80px"
      >
        <el-form-item label="表单名称" prop="key">
          <el-input v-model="coustomForm.key"></el-input>
        </el-form-item>
        <el-form-item label="表单格式" prop="type">
          <el-select
            v-model="coustomForm.type"
            style="width: 100%"
            placeholder="请选择"
          >
            <el-option
              v-for="item of formOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否必填" prop="required">
          <el-radio-group v-model="coustomForm.required">
            <el-radio :label="true">开启</el-radio>
            <el-radio :label="false">关闭</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="提示语" prop="placeholder">
          <el-input v-model="coustomForm.placeholder"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="toggleDialogVisible">取 消</el-button>
        <el-button type="primary" @click="onConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from "vue-property-decorator";
import {
  getOrderManageSetting,
  setOrderManageSetting,
} from "@/api/order/index";
import cloneDeep from "lodash/cloneDeep";
import { ElForm } from "element-ui/types/form";
interface FormsType {
  key: string;
  type: string;
  required: boolean;
  placeholder: string;
}
interface FormType {
  customFrom: string;
  id: number;
  kdAppId: string;
  kdAppKey: string;
  orderNotify: boolean;
  paymentModel: number[];
  shopId: string;
  userReturnOvertime: number;
  merchantConfirmOvertime: number;
  type: string;
  normalOrderOvertime: number;
  finishOvertime: number;
  confirmOvertime: number;
  [x: string]: any;
}
@Component
export default class Order extends Vue {
  coustomForm: FormsType = {
    key: "",
    type: "",
    required: false,
    placeholder: "请输入",
  };

  coustomRules = {
    key: [
      {
        required: true,
        message: "表单名称不可为空",
        trigger: "change",
      },
    ],
    type: [
      {
        required: true,
        message: "表单类型不可为空",
        trigger: "change",
      },
    ],
    placeholder: [
      {
        required: true,
        message: "表单提示语不可为空",
        trigger: "change",
      },
    ],
  };

  currentForm: FormsType | null = null;

  currentIndex = 0;

  dialogVisible = false;

  formOptions = [
    {
      label: "电话",
      value: "phone",
    },
    {
      label: "身份证",
      value: "id",
    },
    {
      label: "文本",
      value: "text",
    },
    {
      label: "数字",
      value: "number",
    },
    {
      label: "图片",
      value: "image",
    },
    {
      label: "日期",
      value: "date",
    },
    {
      label: "时间",
      value: "time",
    },
    {
      label: "日期时间",
      value: "datetime",
    },
  ];

  time = {
    day: 0,
    hour: 0,
    min: 15,
  };

  form: FormType = {
    customFrom: "",
    id: 0,
    kdAppId: "",
    kdAppKey: "",
    orderNotify: true,
    paymentModel: [1],
    shopId: "",
    userReturnOvertime: 0,
    merchantConfirmOvertime: 0,
    type: "",
    // 关闭时间
    normalOrderOvertime: 0,
    finishOvertime: 0,
    confirmOvertime: 0,
  };

  rules = {
    userReturnOvertime: [
      {
        required: true,
        message: "申请售后时间不可为空",
        trigger: "change",
      },
    ],
    merchantConfirmOvertime: [
      { required: true, message: "待审核时间不可为空", trigger: "change" },
    ],
    paymentModel: [
      {
        type: "array",
        required: true,
        message: "至少选择一种支付方式",
        trigger: "change",
      },
    ],
    finishOvertime: [
      {
        type: "number",
        required: true,
        message: "不可为空",
        trigger: "change",
      },
      {
        type: "number",
        required: true,
        max: 30,
        message: "不可大于30",
        trigger: "change",
      },
    ],
    confirmOvertime: [
      {
        type: "number",
        required: true,
        message: "不可为空",
        trigger: "change",
      },
      {
        type: "number",
        required: true,
        max: 30,
        message: "不可大于30",
        trigger: "change",
      },
    ],
  };

  forms: Array<FormsType> = [];

  @Watch("dialogVisible")
  handleDialogVisible(v: boolean) {
    if (!v) {
      this.currentForm = null;
      this.resetCoustomForm();
    }
  }

  created() {
    getOrderManageSetting()
      .then(res => {
        res.data.paymentModel = res.data.paymentModel.split(",");
        this.forms = JSON.parse(res.data.customFrom);
        this.handleNormalOrderOvertime(res.data.normalOrderOvertime);
        Object.assign(this.form, res.data);
      })
      .catch(err => {
        this.$message.warning(err);
      });
  }

  verification() {
    const rules = {
      userReturnOvertime: "申请售后时间不可为空",
      merchantConfirmOvertime: "待审核时间不可为空",
    } as { [x: string]: any };
    const item = Object.keys(this.form).find(key => {
      if (rules[`${key}`] && !this.form[key] && this.form[key] !== 0) {
        this.$message.warning(rules[key]);
        return key;
      }
    });
    return item;
  }

  timeUpdate() {
    const { day, hour, min } = this.time;
    const gm = (() => (min || 0) * 60 * 1000)();
    const gh = (() => (hour || 0) * 60 * 60 * 1000)();
    const gd = (() => ((day || 0) > 30 ? 30 : day) * 24 * 60 * 60 * 1000)();

    this.$set(this.form, "normalOrderOvertime", (gm + gh + gd) / (60 * 1000));
  }

  /** 处理过期时间 */
  handleNormalOrderOvertime(time: number) {
    this.time = this.diffTime(0, time);
  }

  diffTime(startDate: number | Date, endDate: number | Date) {
    startDate = new Date((startDate as number) * (60 * 1000));
    endDate = new Date((endDate as number) * (60 * 1000));
    let diff = endDate.getTime() - startDate.getTime();

    //计算出相差天数
    let day = Math.floor(diff / (24 * 3600 * 1000));

    //计算出小时数
    let leave1 = diff % (24 * 3600 * 1000);
    let hour = Math.floor(leave1 / (3600 * 1000));
    //计算相差分钟数
    let leave2 = leave1 % (3600 * 1000);
    let min = Math.floor(leave2 / (60 * 1000));

    return {
      day: day || 0,
      hour: hour || 0,
      min: min || 0,
    };
  }

  getFormName(key: string) {
    const data = this.formOptions.find(item => item.value === key);
    return data && data.label;
  }

  resetCoustomForm() {
    this.coustomForm = {
      key: "",
      type: "",
      required: false,
      placeholder: "请输入",
    };
  }

  toggleDialogVisible() {
    this.dialogVisible = !this.dialogVisible;
  }

  onConfirm() {
    (this.$refs.coustomForm as ElForm).validate(valid => {
      if (!valid) {
        return this.$message.info("请填写正确的表单");
      }
      if (!this.currentForm) {
        this.forms.push(this.coustomForm);
      } else {
        this.$set(this.forms, this.currentIndex, cloneDeep(this.coustomForm));
      }

      this.toggleDialogVisible();
    });
  }

  handleDel(i: number) {
    this.forms.splice(i, 1);
  }

  handleEdit(item: FormsType, i: number) {
    this.currentForm = cloneDeep(item);
    this.coustomForm = this.currentForm;
    this.currentIndex = i;
    this.toggleDialogVisible();
  }

  setOrderSetting() {
    if (this.verification()) return;
    const gd = (day => (day > 30 ? 30 : day) * 24 * 60)(30);
    if (gd <= this.form.normalOrderOvertime) {
      return this.$message.info("超时时间不可大于30天");
    }
    if (this.form.normalOrderOvertime < 15) {
      return this.$message.info("超时时间不可小于15分钟");
    }

    (this.$refs.form as any).validate((valid: any) => {
      if (!valid) {
        return this.$message.info("请填写正确的表单");
      }

      setOrderManageSetting({
        ...this.form,
        paymentModel: this.form.paymentModel.join(","),
        customFrom: JSON.stringify(this.forms),
      })
        .then(() => {
          this.$message.success("保存成功");
          this.$store.dispatch("global/getShopSetting");
        })
        .catch(err => {
          this.$message.warning(err);
        });
    });
  }
}
</script>

<style lang="scss">
.order__setting--form {
  font-size: 13px;

  .order__tip {
    vertical-align: center;
    background-color: rgba(246, 248, 250, 1);
    padding: 15px 15px 15px 30px;
    margin-bottom: 30px;

    .order__tip--title {
      margin-left: 12px;
      color: #586884;
      font-weight: 700;
    }
  }

  .el-table--fit {
    border: 1px solid #e4e4e4;
    border-bottom: 0px;
  }

  .el-table td,
  .el-table th {
    padding: 0 !important;
  }

  .el-table th {
    padding: 0 !important;
    background-color: #f2f4f3 !important;
  }

  .el-table__header-wrapper tr {
    background-color: #f2f4f3 !important;
  }

  .el-form {
    margin-bottom: 40px;
  }
}

.order__tip--lump {
  display: inline-block;
  width: 3px;
  height: 12px;
  background-color: rgba(255, 153, 0, 1);
}

.input--item {
  width: 60px;
  margin: 0 3px;
}
</style>
