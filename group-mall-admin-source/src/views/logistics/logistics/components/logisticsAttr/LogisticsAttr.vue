<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:48:05
-->
<template>
  <div>
    <el-button @click="back" v-if="pageType">返回发布商品</el-button>
    <el-button type="primary" @click="addNewModel">新增运费模板</el-button>
    <div v-for="(item, index) in logisModelList" :key="index" class="list">
      <div v-if="logisModelList.length > 0">
        <div class="list__top">
          <div class="list__top--name">
            {{ item.name }}
            {{ item.choiceConditionPostage === 1 ? "(已指定包邮区域)" : "" }}
          </div>
          <div class="list__top--deal">
            <span style="color: #409eff" @click="editModel(item)">编辑</span>
            <span style="color: rgb(250, 100, 101)" @click="delGetItem(item)"
              >删除</span
            >
          </div>
        </div>
        <div class="list__content">
          <div class="list__content--name">可配送区域</div>
          <div class="list__content--status">
            <span>
              {{ item.valuationModel === 1 ? "首件数(件)" : "首重量(kg)" }}
            </span>
            <span>首费(元)</span>
            <span>
              {{ item.valuationModel === 1 ? "续件数(件)" : "续重量(kg)" }}
            </span>
            <span>续费(元)</span>
          </div>
        </div>
        <div
          class="list__content"
          v-for="(setItem, setIndex) in item.logisticsShippingModelVos"
          :key="setIndex"
        >
          <div class="list__content--name">{{ setItem.area }}</div>
          <div class="list__content--status">
            <span>
              {{
                setItem.valuationModel === 1
                  ? setItem.firstPiece
                  : setItem.firstWeight
              }}
            </span>
            <span>{{ setItem.firstAmount }}</span>
            <span>
              {{
                setItem.valuationModel === 1
                  ? setItem.secondPiece
                  : setItem.secondWeight
              }}
            </span>
            <span>{{ setItem.secondAmount }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="emptyLine" v-if="hasList">暂无数据~</div>
    <PageManage
      :pageSize="query.size"
      :pageNum="query.current"
      :total="query.total"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    ></PageManage>
    <el-dialog
      :visible.sync="logDialog"
      width="850px"
      :before-close="handleClose"
    >
      <div slot="title" class="digTitle">
        {{ titleType ? "新增" : "编辑" }}物流配送模板
      </div>
      <AddModel
        ref="addModel"
        :editCurrent="editCurrent"
        :logDialog="logDialog"
      ></AddModel>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="addLogModel">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import AddModel from "./compont/AddModel.vue";
import PageManage from "@/components/PageManage.vue";
import { LogisticsState, PageQueryType } from "../../logisticType";
import {
  FreightTempType,
  LogisticsRegionType,
} from "@/views/goods/marketModel/goodType";
import {
  addLogistModel,
  getLogistModel,
  delLogistModel,
} from "@/api/logistics/logistics";
import address from "@/views/goods/common/city.js";
import { AddLogisticsSubForm } from "./compont/AddModelType";

@Component({
  components: {
    AddModel,
    PageManage,
  },
})
export default class LogisticsAttr extends Vue implements LogisticsState {
  @Ref()
  readonly addModel!: AddModel;

  logDialog = false;

  editCurrent = {} as Partial<FreightTempType>;

  titleType = true;

  logisModelList: Array<FreightTempType> = [];

  hasList = false;

  pageType = "";

  query = {
    current: 1,
    size: 10,
    total: 0,
  } as PageQueryType;

  mounted() {
    this.pageType = this.$route.params.options;
    this.getList();
  }

  /**
   * 返回上一页
   */
  back() {
    this.$router.go(-1);
  }

  /**
   * 获取运费模板
   */
  getList() {
    getLogistModel(this.query).then(res => {
      this.query.total = res.data.total;
      res.data.list.forEach((item: FreightTempType) => {
        item.logisticsShippingModelVos.forEach(seItem => {
          seItem.area = this.dealArea(JSON.parse(seItem.region as string));
        });
        item.logisticsIncludePostageVos.forEach(poItem => {
          poItem.area = this.dealArea(JSON.parse(poItem.region as string));
        });
      });
      this.logisModelList = res.data.list;
      this.hasList = res.data.list.length === 0 ? true : false;
    });
  }

  /**
   * 根据返回的地域信息显示对应城市
   */
  dealArea(region: LogisticsRegionType[]) {
    const addressProv = address.getProvince();
    let addressCity = [];
    const area: string[] = [];
    region.forEach(item => {
      addressProv.forEach(proItem => {
        if (item.fatherid === proItem.value) {
          addressCity = address.getCity(proItem.value);
          if (item.list.length > 0 && item.list.length !== addressCity.length) {
            area.push(
              `${proItem.label}(${item.list.length}/${addressCity.length})`,
            );
          } else {
            area.push(`${proItem.label}`);
          }
        }
      });
    });
    return area.join(",");
  }

  /**
   * 编辑模板
   */
  editModel(item: FreightTempType) {
    this.titleType = false;
    this.logDialog = true;
    this.editCurrent = item;
  }

  /**
   * 删除选中模板
   */
  delGetItem(item: FreightTempType) {
    this.$confirm(`确定删除选中模板吗?删除后模板信息不可恢复?`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      delLogistModel(item.id, {})
        .then(() => {
          this.$message.success("删除成功");
          this.getList();
        })
        .catch(err => {
          this.$message.error(err);
        });
    });
  }

  /**
   * 新增物流模板
   */
  addNewModel() {
    this.logDialog = true;
    this.titleType = true;
    this.editCurrent = {};
  }

  handleClose() {
    this.$confirm("确定退出物流模板编辑？退出后不会保留选择操作！", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      this.logDialog = false;
      this.addModel.clearValidate();
    });
  }

  /**
   * 运费模板输入验证
   */
  checkModel(checkItem: Partial<AddLogisticsSubForm>) {
    let passType = true;
    let errMsg = "";
    if (!checkItem.name) {
      errMsg = "请输入模板名称";
      passType = false;
      this.$message.error(errMsg);
      return passType;
    }
    checkItem.logisticsShippingModelDtos &&
      checkItem.logisticsShippingModelDtos.forEach(item => {
        if (!item.area && passType) {
          errMsg = "请选择配送区域";
          passType = false;
          this.$message.error(errMsg);
          return passType;
        }
      });

    return passType;
  }

  /**
   * 包邮模板输入验证
   */
  checkFreeModel(checkItem: Partial<AddLogisticsSubForm>) {
    let passType = true;
    checkItem.logisticsIncludePostageDtos &&
      checkItem.logisticsIncludePostageDtos.forEach(item => {
        if (!item.area && passType) {
          passType = false;
          this.$message.error("请选择包邮区域");
          return passType;
        }
      });

    return passType;
  }

  /**
   * 点击确定新增物流模板
   */
  async addLogModel() {
    const logisticsTemplateDto = {
      ...this.addModel.modelValidateForm,
    };

    logisticsTemplateDto.logisticsIncludePostageDtos &&
      logisticsTemplateDto.logisticsIncludePostageDtos.forEach(item => {
        if (item.value && logisticsTemplateDto.valuationModel) {
          item.type = this.getType([
            item.value,
            logisticsTemplateDto.valuationModel,
          ]);
        }
      });
    logisticsTemplateDto.logisticsShippingModelDtos &&
      logisticsTemplateDto.logisticsShippingModelDtos.forEach(item => {
        if (logisticsTemplateDto.valuationModel) {
          item.valuationModel = logisticsTemplateDto.valuationModel;
        }
      });
    logisticsTemplateDto.choiceConditionPostage = Number(
      logisticsTemplateDto.choiceConditionPostage,
    );
    const checkFlag = await this.checkModel(logisticsTemplateDto);
    let freeCheck = true;
    if (checkFlag) {
      await this.getLogisticsModel();
      if (logisticsTemplateDto.choiceConditionPostage === 1) {
        freeCheck = await this.checkFreeModel(logisticsTemplateDto);
        if (freeCheck) {
          await this.getFreeLogisticsModel();
        }
      }
      if (
        checkFlag &&
        freeCheck &&
        logisticsTemplateDto.logisticsIncludePostageDtos
      ) {
        logisticsTemplateDto.logisticsIncludePostageDtos.forEach(item => {
          if (
            item.value === "1" &&
            logisticsTemplateDto.valuationModel === "2"
          ) {
            item.weight = item.pieceNum;
          }
        });
        await addLogistModel(logisticsTemplateDto).then(() => {
          this.logDialog = false;
          this.$message.success(
            `${logisticsTemplateDto.id ? "编辑成功" : "新增成功"}`,
          );
          this.getList();
        });
      }
    }
  }

  async getLogisticsModel() {
    try {
      await this.addModel.getFormModel();
      return;
    } catch (error) {
      this.$message.warning("请填写正确运费信息");
      return Promise.reject(error);
    }
  }

  async getFreeLogisticsModel() {
    try {
      await this.addModel.getFreeFormModel();
      return;
    } catch (error) {
      this.$message.warning("请填写正确包邮地区信息");
      return Promise.reject(error);
    }
  }

  getType(arr: string[]) {
    const types = [
      [["1", "1"], 0],
      [["2", "1"], 2],
      [["3", "1"], 3],
      [["1", "2"], 1],
      [["2", "2"], 2],
      [["3", "2"], 4],
    ];
    const logType = new Map();
    types.forEach(([key, value]) => {
      logType.set(JSON.stringify(key), value);
    });
    return logType.get(JSON.stringify(arr));
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val: number) {
    this.query.size = val;
    this.getList();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val: number) {
    this.query.current = val;
    this.getList();
  }
}
</script>

<style lang="scss" scoped>
@include b(list) {
  margin-top: 15px;
  background-color: white;
  padding: 0px;
  color: #595961;
  @include e(top) {
    padding: 15px 20px;
    margin-top: 10px;
    display: flex;
    justify-content: space-between;
    background-color: #f6f8fa;
    @include m(name) {
      font-size: 13px;
      font-weight: bold;
    }
    @include m(deal) {
      font-size: 13px;
      width: 70px;
      display: flex;
      justify-content: space-between;
      cursor: pointer;
    }
  }

  @include e(content) {
    border: 1px solid #ededed;
    border-top: none;
    padding: 15px 20px;
    display: flex;
    justify-content: space-between;
    @include m(name) {
      width: 550px;
    }
    @include m(status) {
      width: 350px;
      display: flex;
      justify-content: space-around;
    }
  }
}

.digTitle {
  font-size: 17px;
  font-weight: bold;
}

.emptyLine {
  width: 100%;
  height: 80px;
  background-color: white;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
  margin-top: 15px;
  font-size: 14px;
  color: #b3b3b3;
  border-top: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
}
</style>
