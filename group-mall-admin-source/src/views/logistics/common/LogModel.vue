<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:57:47
-->
<template>
  <div v-if="templateId && modelOption.logisticsShippingModelVos">
    <el-table
      :data="modelOption.logisticsShippingModelVos"
      border
      style="width: 80%; margin: 20px 0px"
      :header-cell-style="rowClass"
    >
      <el-table-column label="可配送区域" style="color: red">
        <template slot-scope="scope">
          <div>{{ scope.row.area }}</div>
        </template>
      </el-table-column>
      <el-table-column label="首件(件)" v-if="modelOption.valuationModel === 1">
        <template slot-scope="scope">
          <div class="noClick">{{ scope.row.firstPiece }}</div>
        </template>
      </el-table-column>
      <el-table-column label="首重(kg)" v-else>
        <template slot-scope="scope">
          <div class="noClick">{{ scope.row.firstWeight }}</div>
        </template>
      </el-table-column>
      <el-table-column label="首费(元)">
        <template slot-scope="scope">
          <div class="noClick">{{ scope.row.firstAmount }}</div>
        </template>
      </el-table-column>
      <el-table-column label="续件(件)" v-if="modelOption.valuationModel === 1">
        <template slot-scope="scope">
          <div class="noClick">{{ scope.row.secondPiece }}</div>
        </template>
      </el-table-column>
      <el-table-column label="续重(kg)" v-else>
        <template slot-scope="scope">
          <div class="noClick">{{ scope.row.secondWeight }}</div>
        </template>
      </el-table-column>
      <el-table-column label="续费(元)">
        <template slot-scope="scope">
          <div class="noClick">{{ scope.row.secondAmount }}</div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import {
  FreightTempType,
  LogisticsRegionType,
} from "@/views/goods/marketModel/goodType";
import { getLogisticsContent } from "@/api/good/goods";
import address from "@/views/goods/common/city.js";

@Component
export default class LogModel extends Vue {
  @Prop({
    type: String,
    default() {
      return "";
    },
  })
  templateId!: string;

  /** 获取模板内容 */
  modelOption = {} as Partial<FreightTempType>;

  rowClass() {
    return "background: '#f6f8fa';font-weight: normal;";
  }

  @Watch("templateId")
  watchId() {
    this.getModelContent();
  }

  mounted() {
    this.getModelContent();
  }

  /**
   * 获取物流详情
   */
  async getModelContent() {
    if (Number(this.templateId) > 0) {
      const res = await getLogisticsContent(this.templateId, {});
      res.data.logisticsShippingModelVos.forEach(item => {
        item.area = this.dealArea(JSON.parse(item.region as string));
      });
      this.modelOption = res.data;
      this.$emit("getModelOption", this.modelOption);
    } else {
      this.modelOption = {};
    }
  }

  /**
   * 获取省市区所有数据
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
}
</script>

<style lang="scss" scoped>
.noClick {
  width: 80px;
  height: 30px;
  background-color: #f0f0f0;
  border: 1px solid #797979;
  display: flex;
  align-items: center;
  padding-left: 10px;
}
</style>
