<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:52:07
-->
<template>
  <m-card class="form" :needToggle="true">
    <el-form ref="form" :model="searchType" label-width="90px">
      <el-row>
        <el-col :span="8">
          <el-form-item label="商品名称">
            <el-input
              v-model="searchType.name"
              placeholder="请输入商品名称"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="展示分类">
            <el-select
              v-model="searchType.showCategoryId"
              placeholder="请选择分类"
              style="width: 224px"
              size="small"
            >
              <el-option-group v-for="group in showCateList" :key="group.index">
                <el-option
                  :label="group.name"
                  :value="group.showCategoryId"
                ></el-option>
                <el-option
                  v-for="item in group.showCategoryVos"
                  :key="item.showCategoryId"
                  :label="item.name"
                  :value="item.showCategoryId"
                  style="margin-left: 20px"
                ></el-option>
              </el-option-group>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="商品状态">
            <el-select
              v-model="searchType.status"
              placeholder="请选择"
              style="width: 224px"
              size="small"
            >
              <el-option
                v-for="item in goodsType"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="createTime"
              style="width: 224px"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions"
              @change="chooseTime"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-button type="primary" @click="search">搜索</el-button>
      </el-form-item>
    </el-form>
  </m-card>
  <!-- </div> -->
</template>

<script lang="ts">
import { Vue, Component, Watch, Prop } from "vue-property-decorator";
import { getAllCategory } from "@/api/good/goods";
import { SearchState, SearchKeyType } from "./searchType";
import DateUtil from "@/store/modules/date";
import { DatePickerOptions } from "element-ui/types/date-picker";

@Component
export default class Search extends Vue implements SearchState {
  name = "Search";

  @Prop({})
  changeId!: string;

  @Watch("changeId")
  getSaleMode() {
    this.saleMode = this.changeId;
    this.init();
  }

  pickerOptions: DatePickerOptions = {
    shortcuts: [
      {
        text: "最近一周",
        onClick(picker) {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
          picker.$emit("pick", [start, end]);
        },
      },
      {
        text: "最近一个月",
        onClick(picker) {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
          picker.$emit("pick", [start, end]);
        },
      },
      {
        text: "最近三个月",
        onClick(picker) {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
          picker.$emit("pick", [start, end]);
        },
      },
    ],
  };

  showCateList = [];

  goodsType = [
    {
      value: "",
      label: "全部",
    },
    {
      value: "1",
      label: "上架",
    },
    {
      value: "0",
      label: "下架",
    },
  ];

  searchType = {
    name: "",
    showCategoryId: "",
    status: "",
    createBeginTime: "",
    createEndTime: "",
  } as SearchKeyType;

  saleMode = "";

  createTime = "";

  mounted() {
    this.init();
  }

  init() {
    const param = {
      saleMode: this.saleMode,
    };
    Promise.all([getAllCategory(param)]).then(res => {
      res[0].data.unshift({
        id: "0",
        name: "全部",
        parentId: "0",
        showCategoryId: "",
        showCategoryVos: [],
        sort: 0,
      });
      this.showCateList = res[0].data;
      this.searchType.showCategoryId = "";
    });
  }

  chooseTime(data: Array<Date>) {
    this.searchType.createBeginTime = data ? this.dateConversion(data[0]) : "";
    this.searchType.createEndTime = data ? this.dateConversion(data[1]) : "";
  }

  dateConversion(value: Date) {
    const date = new DateUtil("").getYMDs(value);
    return date;
  }

  search() {
    this.$emit("searchBy", this.searchType);
  }
}
</script>

<style lang="scss" scoped>
@include b(form) {
  transform-origin: left top;
  overflow: hidden;
  position: relative;
  transition: all 0.3s ease 0s;

  &.show {
    height: 200px;
    margin-bottom: 20px;
  }

  &.hide {
    margin-bottom: 20px;
    height: 50px;

    .form__btn {
      width: 940px;
      height: 50px;
      background: #f9f9f9;
      line-height: 50px;
      // margin-top: 20px
    }
  }

  @include e(btn) {
    width: 100%;
    position: absolute;
    bottom: 0;
    text-align: center;
    padding-bottom: 20px;

    span {
      cursor: pointer;
    }
  }
}
.page {
  // height: 270px;
  background-color: #f9f9f9;
  margin-bottom: 20px;
}

@include b(search) {
  display: flex;
  flex-wrap: wrap;
  @include e(item) {
    padding: 20px 40px 10px 40px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    @include m(text) {
      width: 60px;
    }
  }
  @include e(icon) {
    width: 40px;
    text-align: center;
    border-left: 1px solid #dcdfe6;
    cursor: pointer;
    vertical-align: middle;
  }
}

@include b(searchButton) {
  margin: 20px 30px;
}
</style>
