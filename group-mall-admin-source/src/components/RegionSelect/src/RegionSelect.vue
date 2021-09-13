<template>
  <!-- 行政区划选择器 -->
  <el-cascader
    ref="cascader"
    v-model="syncedValues"
    :options="districts"
    :props="props"
    @change="handleChange"
  ></el-cascader>
</template>

<script lang="ts">
import { Vue, Component, PropSync, Ref, Emit } from "vue-property-decorator";
import { districtParameters } from "@/api/regionSelect";
import { ElCarousel } from "element-ui/types/carousel";

type ExpandTrigger = "click" | "hover";

interface CascaderProps {
  expandTrigger?: ExpandTrigger;
  value?: string;
  label?: string;
  children?: string;
  leaf?: string;
}

/**
 * 高德地图行政区划
 */
interface IDistriscts {
  adcode: string;
  center: string;
  citycode: string | string[];
  districts: IDistriscts[];
  level: string;
  name: string;
}

/**
 * 行政区划选择器返回的数据
 */
interface IRegionResult {
  /** 省 Code */
  provinceCode: string;
  /** 省 Name */
  provinceName: string;
  /** 市 Code */
  cityCode: string;
  /** 市 Name */
  cityName: string;
  /** 区 Code */
  districtCode: string;
  /** 区 Name */
  districtName: string;
}

@Component
export default class RegionSelect extends Vue {
  districts: IDistriscts[] = [];

  @PropSync("value", {
    required: true,
    type: Array,
    default() {
      return [];
    },
  })
  syncedValues: string[];

  props: CascaderProps = {
    expandTrigger: "hover",
    label: "name",
    value: "adcode",
    children: "districts",
  };

  @Ref() cascader!: ElCarousel;

  @Emit("change")
  cascaderChange(): IRegionResult {
    const syncedValues: string[] = this.syncedValues;
    const separator: string = this.cascader.separator;
    const presentText: string = this.cascader._data.presentText;
    const presentTexts: string[] = presentText.split(separator);
    const regionResult: IRegionResult = {
      provinceCode: syncedValues[0],
      provinceName: presentTexts[0],
      cityCode: syncedValues[1],
      cityName: presentTexts[1],
      districtCode: syncedValues[2],
      districtName: presentTexts[2],
    };
    return regionResult;
  }

  mounted() {
    this.getDistrict();
  }

  /**
   * 获取行政区划数据
   */
  async getDistrict() {
    try {
      const res = await districtParameters();
      if (res.data.districts && res.data.districts[0]) {
        const districts: IDistriscts[] = res.data.districts[0].districts;
        this.checkDistrictslevel(districts);
        this.districts = districts;
      }
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * 检测数据等级是否为 district/区
   */
  checkDistrictslevel(list: IDistriscts[]) {
    list.forEach(item => {
      if (Array.isArray(item.districts) && item.districts.length > 0) {
        this.checkDistrictslevel(item.districts);
      } else {
        item.districts = null;
      }
    });
  }

  handleChange() {
    this.$nextTick(() => {
      this.cascaderChange();
    });
  }
}
</script>
