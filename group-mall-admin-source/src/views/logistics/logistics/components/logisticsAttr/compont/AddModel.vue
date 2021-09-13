<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:48:10
-->
<template>
  <div style="padding: 0px 20px">
    <div class="nameInput">
      <div style="margin-right: 20px; font-size: 13px">
        <span style="color: red">*</span>模板名称：
      </div>
      <el-input
        v-model="modelValidateForm.name"
        style="width: 280px"
        maxlength="25"
      ></el-input>
      <span class="nameInput__text">模板名称最多25个字</span>
    </div>
    <div class="radioChoise">
      <div style="margin-right: 20px; font-size: 13px">
        <span style="color: red">*</span>计费方式：
      </div>
      <el-radio
        v-model="modelValidateForm.valuationModel"
        label="1"
        @change="selectRadio"
      >
        <span style="font-size: 13px">按件数</span>
      </el-radio>
      <el-radio
        v-model="modelValidateForm.valuationModel"
        label="2"
        @change="selectRadio"
      >
        <span style="font-size: 13px">按重量</span>
      </el-radio>
      <span class="radioChoise__text">指定区域城市运费包邮可在运费填写为0</span>
    </div>
    <div class="lineStyle">
      <div class="lineStyle__left">选择区域</div>
      <div class="lineStyle__right">
        <span>
          {{
            modelValidateForm.valuationModel === "1"
              ? "首件数(件)"
              : "首重量(kg)"
          }}
        </span>
        <span style="margin-left: 80px">首费(元)</span>
        <span style="margin-left: 80px">
          {{
            modelValidateForm.valuationModel === "1"
              ? "续件数(件)"
              : "续重量(kg)"
          }}
        </span>
        <span style="margin-left: 80px">续费(元)</span>
        <span style="margin-left: 80px">操作</span>
      </div>
    </div>
    <div
      v-for="(item, index) in modelValidateForm.logisticsShippingModelDtos"
      :key="index"
    >
      <div class="lineStyle" style="padding: 10px 0px">
        <div class="lineStyle__left middleCenter">
          {{ item.area }}
          <el-button
            type="text"
            @click="updateArea(item, index)"
            style="margin-left: 10px"
            >{{ item.area ? "编辑" : "添加" }}</el-button
          >
        </div>
        <div class="lineStyle__right">
          <el-form
            :model="item"
            :rules="areaRules"
            ref="areaRef"
            class="demo-ruleForm"
          >
            <div style="display: flex">
              <div>
                <el-form-item
                  prop="firstPiece"
                  v-if="modelValidateForm.valuationModel === '1'"
                >
                  <el-input-number
                    :min="0"
                    :controls="false"
                    v-model="item.firstPiece"
                    style="width: 80px"
                  ></el-input-number>
                </el-form-item>
                <el-form-item
                  prop="firstWeight"
                  v-if="modelValidateForm.valuationModel === '2'"
                >
                  <el-input-number
                    :min="0"
                    :controls="false"
                    v-model="item.firstWeight"
                    style="width: 80px"
                  ></el-input-number>
                </el-form-item>
              </div>
              <div style="margin-left: 65px">
                <el-form-item prop="firstAmount">
                  <el-input-number
                    :min="0"
                    :controls="false"
                    v-model="item.firstAmount"
                    style="width: 80px"
                  ></el-input-number>
                </el-form-item>
              </div>
              <div style="margin-left: 50px">
                <el-form-item
                  prop="secondPiece"
                  v-if="modelValidateForm.valuationModel === '1'"
                >
                  <el-input-number
                    :min="0"
                    :controls="false"
                    v-model="item.secondPiece"
                    style="width: 80px"
                  ></el-input-number>
                </el-form-item>
                <el-form-item
                  prop="secondWeight"
                  v-if="modelValidateForm.valuationModel === '2'"
                >
                  <el-input-number
                    :min="0"
                    :controls="false"
                    v-model="item.secondWeight"
                    style="width: 80px"
                  ></el-input-number>
                </el-form-item>
              </div>
              <div style="margin-left: 55px">
                <el-form-item prop="secondAmount">
                  <el-input-number
                    :min="0"
                    :controls="false"
                    v-model="item.secondAmount"
                    style="width: 80px"
                  ></el-input-number>
                </el-form-item>
              </div>
            </div>
          </el-form>
          <div
            class="lineStyle__right--deal"
            :style="{
              'justify-content':
                modelValidateForm.logisticsShippingModelDtos.length ===
                index + 1
                  ? 'space-between'
                  : '',
            }"
          >
            <span
              style="color: #409eff; cursor: pointer"
              v-if="
                modelValidateForm.logisticsShippingModelDtos.length ===
                  index + 1
              "
              @click="addnewArea"
              >添加</span
            >
            <span
              style="color: rgb(250, 100, 101); cursor: pointer"
              @click="deleteNewArea(index, 0)"
              >删除</span
            >
          </div>
          <AreaSelect
            :visible.sync="areaDialog"
            @getConList="getConList"
            :modelIndex="modelIndex"
            :getRegion="modelRegion"
          ></AreaSelect>
        </div>
      </div>
    </div>
    <el-checkbox
      v-model="modelValidateForm.choiceConditionPostage"
      style="margin-top: 20px"
      @change="showCheck"
    >
      <div class="checkArea">指定包邮条件</div>
    </el-checkbox>
    <div v-if="modelValidateForm.choiceConditionPostage">
      <div class="lineStyle">
        <div class="lineStyle__left">
          <span>选择区域</span>
        </div>
        <div
          class="lineStyle__right"
          style="justify-content: space-between; padding: 0px 80px 0px 0px"
        >
          <span>设置包邮条件</span>
          <span>操作</span>
        </div>
      </div>
      <div
        v-for="(item, index) in modelValidateForm.logisticsIncludePostageDtos"
        :key="index"
      >
        <div class="areaLineStyle">
          <div class="areaLineStyle__left middleCenter">
            {{ item.area }}
            <el-button
              type="text"
              @click="updateFreeArea(item, index)"
              style="margin-left: 10px"
              >{{ item.area ? "编辑" : "添加" }}</el-button
            >
          </div>
          <div class="areaLineStyle__right">
            <el-form
              :model="item"
              :rules="freeAreaRules"
              ref="freeAreaRef"
              class="demo-ruleForm"
            >
              <div style="display: flex; padding-top: 15px">
                <el-select
                  v-model="item.value"
                  placeholder="请选择"
                  style="width: 120px; margin-right: 20px; margin-left: 15px"
                  @change="selectValue"
                >
                  <el-option
                    v-for="item in selectOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
                <el-form-item prop="pieceNum" v-if="item.value === '1'">
                  <span
                    >在
                    <el-input-number
                      :min="0"
                      :controls="false"
                      class="areaLineStyle__right--input"
                      v-model="item.pieceNum"
                      style="width: 80px"
                    ></el-input-number>
                    <span>
                      {{
                        modelValidateForm.valuationModel === "1" ? "件" : "kg"
                      }} </span
                    >以上包邮
                  </span>
                </el-form-item>
                <el-form-item prop="amountNum" v-if="item.value === '2'">
                  <span
                    >满
                    <el-input-number
                      :min="0"
                      :controls="false"
                      class="areaLineStyle__right--input"
                      v-model="item.amountNum"
                      style="width: 100px"
                    ></el-input-number
                    >元以上包邮
                  </span>
                </el-form-item>
                <el-form-item v-if="item.value === '3'" style="width: 450px">
                  <div style="display: flex">
                    在
                    <el-form-item
                      prop="pieceNum"
                      v-if="modelValidateForm.valuationModel === '1'"
                    >
                      <el-input-number
                        :min="0"
                        :controls="false"
                        class="areaLineStyle__right--input"
                        v-model="item.pieceNum"
                        style="width: 80px"
                      ></el-input-number>
                    </el-form-item>
                    <el-form-item
                      prop="weight"
                      v-if="modelValidateForm.valuationModel === '2'"
                    >
                      <el-input-number
                        :min="0"
                        :controls="false"
                        class="areaLineStyle__right--input"
                        v-model="item.weight"
                        style="width: 100px"
                      ></el-input-number>
                    </el-form-item>
                    <div style="display: flex">
                      {{
                        modelValidateForm.valuationModel === "1" ? "件" : "kg"
                      }}
                    </div>
                    以上,满
                    <el-form-item prop="amountNum">
                      <el-input-number
                        :min="0"
                        :controls="false"
                        class="areaLineStyle__right--input"
                        v-model="item.amountNum"
                        style="width: 100px"
                      ></el-input-number
                      >元以上包邮
                    </el-form-item>
                  </div>
                </el-form-item>
              </div>
            </el-form>
            <div
              class="areaLineStyle__right--deal"
              :style="{
                'justify-content':
                  modelValidateForm.logisticsIncludePostageDtos.length ===
                  index + 1
                    ? 'space-between'
                    : '',
              }"
            >
              <span
                style="color: #409eff; cursor: pointer"
                v-if="
                  modelValidateForm.logisticsIncludePostageDtos.length ===
                    index + 1
                "
                @click="addnewFreeArea"
                >添加</span
              >
              <span
                style="color: rgb(250, 100, 101); cursor: pointer"
                @click="deleteNewArea(index, 1)"
                >删除</span
              >
            </div>
            <AreaSelect
              :visible.sync="areaDialog"
              @getConList="getConList"
              :modelIndex="modelIndex"
              :getRegion="modelRegion"
            ></AreaSelect>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch, Prop, Ref } from "vue-property-decorator";
import {
  FreightTempType,
  LogisticsRegionType,
} from "@/views/goods/marketModel/goodType";
import {
  AddLogisticsSubForm,
  AddModelState,
  FreeShippingOptions,
} from "./AddModelType";
import AreaSelect, { NewRegionType } from "./AreaSelect.vue";
import { ElForm } from "element-ui/types/form";

@Component({
  components: {
    AreaSelect,
  },
})
export default class AddModel extends Vue implements AddModelState {
  @Prop({
    type: Object,
    required: true,
    default: {},
  })
  editCurrent!: FreightTempType;

  @Watch("editCurrent")
  watchEditCurrent() {
    if (this.editCurrent.id) {
      this.editModelOption();
    } else {
      this.initModel();
    }
  }

  @Prop({
    type: Boolean,
    required: true,
    default: false,
  })
  logDialog!: boolean;

  @Watch("logDialog")
  watchLogDialog() {
    if (this.editCurrent.id) {
      this.editModelOption();
    }
  }

  @Ref()
  readonly areaRef!: ElForm[];

  @Ref()
  readonly freeAreaRef!: ElForm[];

  pieceVlidator(_rule: any, value: string, callback: (arg?: Error) => void) {
    const regs = /^[+]{0,1}(\d+)$/;
    if (!regs.test(value)) {
      callback(new Error("件数请输入整数"));
    } else {
      callback();
    }
  }

  weightVlidator(_rule: any, value: string, callback: (arg?: Error) => void) {
    const regs = /^\d+.?\d{0,2}$/;
    if (!regs.test(value)) {
      callback(new Error("重量输入有误,数字最多保留两位小数"));
    } else {
      callback();
    }
  }

  amountVlidator(_rule: any, value: string, callback: (arg?: Error) => void) {
    const regs = /^\d+.?\d{0,2}$/;
    if (!regs.test(value)) {
      callback(new Error("费用输入有误,数字最多保留两位小数"));
    } else {
      callback();
    }
  }

  /** 包邮验证 */
  FreeFirstVlidator(
    _rule: any,
    value: string,
    callback: (arg?: Error) => void,
  ) {
    const regs = /^[+]{0,1}(\d+)$/;
    const regWeight = /^\d+.?\d{0,2}$/;
    const valuationModel = this.modelValidateForm.valuationModel;
    if (value === "") {
      callback(
        new Error(`${valuationModel === "1" ? "件数" : "重量"}不能为空`),
      );
    } else if (!regs.test(value) && valuationModel === "1") {
      callback(new Error(`件数请输入整数`));
    } else if (!regWeight.test(value) && valuationModel === "2") {
      callback(new Error(`重量输入有误,数字最多保留两位小数`));
    } else {
      callback();
    }
  }

  areaRules = {
    firstPiece: [
      { required: true, message: "首件不能为空", trigger: "blur" },
      { validator: this.pieceVlidator, trigger: "blur" },
    ],
    firstWeight: [
      { required: true, message: "首重不能为空", trigger: "blur" },
      { validator: this.weightVlidator, trigger: "blur" },
    ],
    firstAmount: [
      { required: true, message: "首费不能为空", trigger: "blur" },
      { validator: this.amountVlidator, trigger: "blur" },
    ],
    secondPiece: [
      { required: true, message: "续件不能为空", trigger: "blur" },
      { validator: this.pieceVlidator, trigger: "blur" },
    ],
    secondWeight: [
      { required: true, message: "续重不能为空", trigger: "blur" },
      { validator: this.weightVlidator, trigger: "blur" },
    ],
    secondAmount: [
      { required: true, message: "续费不能为空", trigger: "blur" },
      { validator: this.amountVlidator, trigger: "blur" },
    ],
  };

  freeAreaRules = {
    pieceNum: [{ validator: this.FreeFirstVlidator, trigger: "blur" }],
    weight: [{ validator: this.FreeFirstVlidator, trigger: "blur" }],
    amountNum: [
      { required: true, message: "费用不能为空", trigger: "blur" },
      { validator: this.amountVlidator, trigger: "blur" },
    ],
  };

  modelIndex = 0;

  modelRegion: Array<LogisticsRegionType> = [];

  freeArea = false;

  modelValidateForm: Partial<AddLogisticsSubForm> = {
    id: "",
    name: "",
    valuationModel: "1",
    choiceConditionPostage: false,
    logisticsShippingModelDtos: [
      {
        region: [] as LogisticsRegionType[],
        area: "",
        id: "",
        logisticsId: "",
        firstPiece: "",
        firstWeight: "",
        firstAmount: "",
        secondPiece: "",
        secondWeight: "",
        secondAmount: "",
        valuationModel: "",
      },
    ],
    logisticsIncludePostageDtos: [
      {
        id: "",
        area: "",
        region: [] as LogisticsRegionType[],
        value: "1",
        pieceNum: "",
        amountNum: "",
        weight: "",
        type: "",
      },
    ],
  };

  selectOptions: Array<FreeShippingOptions> = [
    {
      value: "1",
      label: "件数",
      show: false,
    },
    {
      value: "2",
      label: "金额",
    },
    {
      value: "3",
      label: "件数+金额",
    },
  ];

  areaDialog = false;

  /** 运费模板选择 */
  selectRadio(e: string, idEdit = false) {
    this.modelValidateForm.valuationModel = e;
    const tonOptions = [
      {
        value: "1",
        label: "重量",
      },
      {
        value: "2",
        label: "金额",
      },
      {
        value: "3",
        label: "重量+金额",
      },
    ];
    const numOptions = [
      {
        value: "1",
        label: "件数",
      },
      {
        value: "2",
        label: "金额",
      },
      {
        value: "3",
        label: "件数+金额",
      },
    ];
    this.selectOptions = e === "1" ? numOptions : tonOptions;
    if (!idEdit && this.modelValidateForm.logisticsIncludePostageDtos) {
      this.modelValidateForm.logisticsIncludePostageDtos.forEach(item => {
        item.amountNum = "";
        item.pieceNum = "";
        item.weight = "";
      });
    }
  }

  initModel() {
    this.modelValidateForm = {
      id: "",
      name: "",
      valuationModel: "1",
      choiceConditionPostage: false,
      logisticsShippingModelDtos: [
        {
          region: [],
          area: "",
          id: "",
          logisticsId: "",
          firstPiece: "",
          firstWeight: "",
          firstAmount: "",
          secondPiece: "",
          secondWeight: "",
          secondAmount: "",
          valuationModel: "",
        },
      ],

      logisticsIncludePostageDtos: [
        {
          id: "",
          area: "",
          region: [],
          value: "1",
          pieceNum: "",
          amountNum: "",
          weight: "",
          type: "",
        },
      ],
    };
  }

  mounted() {
    if (this.editCurrent.id) {
      this.editModelOption();
    }
  }

  selectValue() {
    this.clearValidate();
  }

  /**
   * 对选择区域表单进行校验
   */
  validate() {
    const arr = this.$refs.areaRef as ElForm[];
    return Promise.all(
      arr.map(async item => {
        return await item.validate();
      }),
    );
  }

  async getFormModel() {
    try {
      await this.validate();
    } catch (error) {
      console.log(error);
      return Promise.reject(false);
    }
  }

  /**
   * 清除验证
   */
  clearValidate() {
    this.areaRef.forEach(item => {
      item.clearValidate();
    });

    this.freeAreaRef.forEach(item => {
      item.clearValidate();
    });
  }

  /**
   * 对包邮区域表单进行校验
   */
  freeValidate() {
    const arr = this.$refs.freeAreaRef as ElForm[];
    return Promise.all(
      arr.map(async item => {
        return await item.validate();
      }),
    );
  }

  async getFreeFormModel() {
    try {
      await this.freeValidate();
    } catch (error) {
      console.log(error);
      return Promise.reject(false);
    }
  }

  /**
   * 编辑时初始化数据
   */
  editModelOption() {
    const modelValidateForm = this.modelValidateForm;
    const editCurrent = JSON.parse(
      JSON.stringify(this.editCurrent),
    ) as FreightTempType;
    modelValidateForm.choiceConditionPostage = !!editCurrent.choiceConditionPostage;
    modelValidateForm.valuationModel = String(editCurrent.valuationModel);
    modelValidateForm.name = editCurrent.name;
    modelValidateForm.id = editCurrent.id;
    editCurrent.logisticsShippingModelVos.forEach(item => {
      item.valuationModel = String(item.valuationModel);
      item.region =
        typeof item.region === "object" ? item.region : JSON.parse(item.region);
    });

    editCurrent.logisticsIncludePostageVos.forEach(item => {
      item.value = this.getType([item.type, editCurrent.valuationModel]);
      item.region =
        typeof item.region === "object" ? item.region : JSON.parse(item.region);
    });
    modelValidateForm.logisticsShippingModelDtos =
      editCurrent.logisticsShippingModelVos;
    modelValidateForm.logisticsIncludePostageDtos =
      editCurrent.logisticsIncludePostageVos;
    this.selectRadio(String(this.modelValidateForm.valuationModel), true);
  }

  /** 逆向返回选中包邮类型 */
  getType(arr: Array<string | number>) {
    const types = [
      [[0, 1], "1"],
      [[2, 1], "2"],
      [[3, 1], "3"],
      [[1, 2], "1"],
      [[2, 2], "2"],
      [[4, 2], "3"],
    ];
    const logType = new Map();
    types.forEach(([key, value]) => {
      logType.set(JSON.stringify(key), value);
    });
    return logType.get(JSON.stringify(arr));
  }

  /**
   * 编辑或新增地址
   */
  updateArea(_item: any, index: number) {
    this.modelIndex = index;
    this.modelRegion = this.modelValidateForm.logisticsShippingModelDtos
      ? (this.modelValidateForm.logisticsShippingModelDtos[this.modelIndex]
        .region as LogisticsRegionType[])
      : [];
    this.areaDialog = true;
    this.freeArea = false;
  }

  /**
   * 编辑或新增包邮地址
   */
  updateFreeArea(_item: any, index: number) {
    this.modelIndex = index;
    this.modelRegion = this.modelValidateForm.logisticsIncludePostageDtos
      ? (this.modelValidateForm.logisticsIncludePostageDtos[index]
        .region as LogisticsRegionType[])
      : [];
    this.areaDialog = true;
    this.freeArea = true;
  }

  /** 选择包邮区域 */
  showCheck() {
    if (
      this.modelValidateForm.logisticsIncludePostageDtos &&
      this.modelValidateForm.logisticsIncludePostageDtos.length === 0
    ) {
      this.addnewFreeArea();
    }
    this.selectRadio(String(this.modelValidateForm.valuationModel));
  }

  /**
   * 添加新的区域
   */
  addnewArea() {
    if (this.modelValidateForm.logisticsShippingModelDtos) {
      this.modelValidateForm.logisticsShippingModelDtos.push({
        region: [],
        area: "",
        id: "",
        firstPiece: "",
        firstWeight: "",
        logisticsId: "",
        firstAmount: "",
        secondPiece: "",
        secondWeight: "",
        secondAmount: "",
        valuationModel: "",
      });
      this.$set(
        this.modelValidateForm,
        "logisticsShippingModelDtos",
        this.modelValidateForm.logisticsShippingModelDtos,
      );
    }
  }

  /**
   * 删除新区域
   */
  deleteNewArea(index: number, type: number) {
    if (type === 0) {
      if (
        this.modelValidateForm.logisticsShippingModelDtos &&
        this.modelValidateForm.logisticsShippingModelDtos.length === 1
      ) {
        this.$message.error("至少保留一个配送区域");
        return;
      }
      this.modelValidateForm.logisticsShippingModelDtos &&
        this.modelValidateForm.logisticsShippingModelDtos.splice(index, 1);
    } else {
      if (
        this.modelValidateForm.logisticsIncludePostageDtos &&
        this.modelValidateForm.logisticsIncludePostageDtos.length === 1
      ) {
        this.$message.error("至少保留一个包邮区域");
        return;
      }
      this.modelValidateForm.logisticsIncludePostageDtos &&
        this.modelValidateForm.logisticsIncludePostageDtos.splice(index, 1);
    }
  }

  addnewFreeArea() {
    this.modelValidateForm.logisticsIncludePostageDtos &&
      this.modelValidateForm.logisticsIncludePostageDtos.push({
        id: "",
        area: "",
        region: [],
        value: "1",
        pieceNum: "",
        amountNum: "",
        weight: "",
        type: "",
      });
  }

  /**
   * 获取选择后的地址
   */
  getConList(
    region: LogisticsRegionType[],
    conList: NewRegionType[],
    modelIndex: number,
    type: boolean,
  ) {
    if (!type) {
      this.dealLogArea(region, conList, modelIndex);
    } else {
      this.dealFreeLogArea(region, conList, modelIndex);
    }
  }

  /** 配送区域 */
  dealLogArea(
    region: LogisticsRegionType[],
    conList: NewRegionType[],
    modelIndex: number,
  ) {
    const area: string[] = [];
    this.modelValidateForm.logisticsShippingModelDtos &&
      this.modelValidateForm.logisticsShippingModelDtos.forEach(
        (item, index) => {
          if (index === modelIndex) {
            item.region = region;
            conList.forEach(v => {
              if (
                v.cityList &&
                v.cityList.length > 0 &&
                v.cityList.length !== v.hasNum &&
                v.newlabel
              ) {
                v.newlabel = `${v.label} (${v.cityList.length} / ${v.hasNum})`;
              } else {
                v.newlabel = v.label;
              }
              area.push(v.newlabel);
              item.area = area.join(",");
            });
          }
        },
      );
  }

  /** 包邮区域 */
  dealFreeLogArea(
    region: LogisticsRegionType[],
    conList: NewRegionType[],
    modelIndex: number,
  ) {
    const area: string[] = [];
    this.modelValidateForm.logisticsIncludePostageDtos &&
      this.modelValidateForm.logisticsIncludePostageDtos.forEach(
        (item, index) => {
          if (index === modelIndex) {
            item.region = region;
            conList.forEach(v => {
              if (
                v.cityList &&
                v.cityList.length > 0 &&
                v.cityList.length !== v.hasNum
              ) {
                v.newlabel = `${v.label} (${v.cityList.length} / ${v.hasNum})`;
              } else {
                v.newlabel = v.label;
              }
              area.push(v.newlabel);
              item.area = area.join(",");
            });
          }
        },
      );
  }
}
</script>

<style lang="scss" scoped>
.nameInput {
  display: flex;
  align-items: center;
  .nameInput__text {
    font-size: 12px;
    color: #c7c7c7;
    margin-left: 30px;
  }
}

.radioChoise {
  display: flex;
  align-items: center;
  margin-top: 20px;
  .radioChoise__text {
    font-size: 12px;
    color: #c7c7c7;
    margin-left: 25px;
  }
}

.middleCenter {
  display: flex;
  align-items: center;
}

@include b(lineStyle) {
  display: flex;
  // justify-content: space-between;
  border-bottom: 0.5px solid #dcdfe6;
  padding: 20px 0px;
  font-size: 13px;
  margin-top: 10px;
  @include e(left) {
    width: 130px;
    flex-wrap: wrap;
  }
  @include e(right) {
    width: 750px;
    display: flex;
    margin-left: 20px;
    // justify-content: space-around;
    @include m(deal) {
      margin-left: 50px;
      width: 60px;
      display: flex;
      align-items: center;
    }
  }
}

.checkArea {
  font-size: 13px;
}
/deep/ .el-form-item--mini.el-form-item,
.el-form-item--small.el-form-item {
  margin-bottom: 10px;
}

@include b(areaLineStyle) {
  display: flex;
  justify-content: space-between;
  border-bottom: 0.5px solid #dcdfe6;
  padding: 10px 0px;
  font-size: 13px;
  margin-top: 10px;
  @include e(left) {
    width: 130px;
    flex-wrap: wrap;
  }
  @include e(right) {
    width: 750px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    @include m(input) {
      width: 60px;
      margin: 0 5px;
    }
    @include m(deal) {
      width: 60px;
      margin-right: 50px;
      display: flex;
      align-items: center;
    }
  }
}
</style>
