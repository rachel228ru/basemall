<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-18 10:25:18
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-31 17:11:43
-->
<template>
  <div class="page">
    <m-card class="form" :needToggle="true">
      <el-form ref="form" :model="searchMsg" label-width="90px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="供应商名称">
              <el-input
                v-model="searchMsg.name"
                placeholder="请输入供应商名称"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="手机号">
              <el-input
                v-model="searchMsg.mobile"
                placeholder="请输入供应商手机号"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="ID">
              <el-input
                v-model="searchMsg.supplierSn"
                placeholder="请输入供应商ID"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="search">搜索</el-button>
        </el-form-item>
      </el-form>
    </m-card>
    <el-tabs v-model="searchType" @tab-click="selectType" class="serch--type">
      <el-tab-pane label="供货商管理" name="1"></el-tab-pane>
      <el-tab-pane label="待审核" name="2"></el-tab-pane>
      <el-tab-pane label="已关闭" name="0"></el-tab-pane>
      <el-tab-pane label="禁用中" name="3"></el-tab-pane>
    </el-tabs>
    <section class="btn-line">
      <el-button size="mini" @click="back" v-if="showType"
        >返回发布商品</el-button
      >
      <el-button
        class="mr-20"
        v-show="searchType == 1"
        type="primary"
        size="mini"
        @click="addSup"
        >新 增</el-button
      >
      <el-button
        size="mini"
        @click="batchGoods"
        :disabled="selelctList.length === 0"
        style="margin-left: 0px"
        >批量删除</el-button
      >
    </section>

    <m-table :data="list" :selection="true" :checked-item.sync="selelctData">
      <template v-slot:header="{ row }">
        <div style="width: 100%">
          <span style="margin-right: 50px">ID：{{ row.supplierSn }}</span>
          <span style="margin-right: 50px">申请时间：{{ row.createTime }}</span>
        </div>
      </template>
      <m-table-column label="供应商名称">
        <template v-slot="{ row }">
          <div
            class="name-and-phone"
            style="height: 50px; display: flex; align-items: center"
          >
            <span> {{ row.name }}</span>
          </div>
        </template>
      </m-table-column>
      <m-table-column label="手机号">
        <template v-slot="{ row }">
          <div class="name-and-phone">
            <span> {{ row.mobile }}</span>
          </div>
        </template>
      </m-table-column>
      <m-table-column label="地区">
        <template v-slot="{ row }">
          <div class="name-and-phone">
            <span> {{ row.area ? row.area : "无" }}</span>
          </div>
        </template>
      </m-table-column>
      <m-table-column label="产品信息">
        <template v-slot="{ row }">
          <div class="name-and-phone">
            <span> {{ row.productInfo }}</span>
          </div>
        </template>
      </m-table-column>
      <m-table-column label="评分" v-if="['1', '3'].includes(searchType)">
        <template v-slot="{ row }">
          <div class="name-and-phone">
            <span> {{ row.score }}</span>
          </div>
        </template>
      </m-table-column>
      <m-table-column label="供应商地址">
        <template v-slot="{ row }">
          <div class="name-and-phone">
            <span> {{ row.address ? row.address : "无" }}</span>
          </div>
        </template>
      </m-table-column>
      <m-table-column label="操作">
        <template v-slot="{ row }">
          <div>
            <span
              v-if="searchType == '1'"
              style="display: flex; align-items: center"
            >
              <span style="cursor: pointer; color: #2d8cf0" @click="editor(row)"
                >编辑</span
              >
              <DropDowm
                :status="row.status"
                :sup="row"
                @delSupplier="delSupplier"
                @useSup="useSup"
              ></DropDowm>
            </span>
            <span
              v-if="searchType == '2'"
              style="display: flex; align-items: center"
            >
              <el-dropdown
                trigger="click"
                placement="bottom-end"
                @command="dealAudit"
              >
                <span class="el-dropdown-link">
                  审核<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="{ item: row, type: '1' }"
                    >同意</el-dropdown-item
                  >
                  <el-dropdown-item :command="{ item: row, type: '2' }"
                    >拒绝</el-dropdown-item
                  >
                </el-dropdown-menu>
              </el-dropdown>
            </span>
            <span
              v-if="searchType === '0'"
              style="display: flex; align-items: center"
            >
              <span
                style="cursor: pointer; color: red"
                @click="delSupplier('3', row)"
                >删除</span
              >
            </span>
            <span
              v-if="searchType === '3'"
              style="display: flex; align-items: center"
            >
              <span style="cursor: pointer; color: #2d8cf0" @click="editor(row)"
                >编辑</span
              >
              <DropDowm
                :status="row.status"
                :sup="row"
                @delSupplier="delSupplier"
                @useSup="useSup"
              ></DropDowm>
            </span>
          </div>
        </template>
      </m-table-column>
    </m-table>
    <div class="listBottom">
      <PageManage
        :pageSize="pageSize"
        :pageNum="pageNum"
        :total="total"
        @handleSizeChange="handleSizeChange"
        @handleCurrentChange="handleCurrentChange"
      ></PageManage>
    </div>

    <el-dialog
      :visible.sync="isModelAdd"
      width="35%"
      :before-close="editPickCancel"
    >
      <div slot="title" class="dialog__title">
        {{ editOrAdd ? "编辑" : "新增" }}供应商
      </div>
      <section class="dialog__title__line">
        <div class="dialog__title__line--item">
          <div class="dialog__title__line--item--lefts">
            <span style="color: #f76c84">*</span> 供应商名称：
          </div>
          <div class="dialog__title__line--item--rights">
            <el-input
              v-model="currItem.name"
              :maxlength="15"
              placeholder="输入供应商名称"
            ></el-input>
          </div>
        </div>
        <div class="dialog__title__line--item">
          <div class="dialog__title__line--item--lefts">
            <span style="color: #f76c84">*</span> 手机号码：
          </div>
          <div class="dialog__title__line--item--rights">
            <el-input
              v-model="currItem.mobile"
              :maxlength="11"
              style="margin-left: 14px"
              placeholder="输入手机号码"
            ></el-input>
          </div>
        </div>
        <div class="dialog__title__line--item">
          <div class="dialog__title__line--item--lefts">
            <span style="color: #f76c84">*</span> 地区选择：
          </div>
          <div class="dialog__title__line--item--rights">
            <el-select
              placeholder="省份选择"
              @change="
                value => {
                  return selectChang(value, 'prov');
                }
              "
              v-model="currItem.provinceId"
              style="margin-left: 14px; margin-right: 5px"
            >
              <el-option
                v-for="item in addressProv"
                :value="item.value"
                :label="item.label"
                :key="item.value"
                >{{ item.label }}</el-option
              >
            </el-select>
            <el-select
              placeholder="城市选择"
              not-found-text="请先选择省份"
              @change="
                value => {
                  return selectChang(value, 'city');
                }
              "
              v-model="currItem.cityId"
              style="margin-right: 5px"
            >
              <el-option
                v-for="item in addressCity"
                :value="item.value"
                :label="item.label"
                :key="item.value"
                >{{ item.label }}</el-option
              >
            </el-select>
            <el-select
              placeholder="区县选择"
              not-found-text="请先选择城市"
              @change="
                value => {
                  return selectChang(value, 'area');
                }
              "
              v-model="currItem.countryId"
            >
              <el-option
                v-for="item in addressArea"
                :value="item.value"
                :label="item.label"
                :key="item.value"
                >{{ item.label }}</el-option
              >
            </el-select>
          </div>
        </div>
        <div class="dialog__title__line--item">
          <div class="dialog__title__line--item--lefts">
            <span style="color: #f76c84">*</span> 供应商地址：
          </div>
          <div class="dialog__title__line--item--rights">
            <el-input
              v-model="currItem.address"
              placeholder="输入供应商地址"
            ></el-input>
          </div>
        </div>
        <div class="dialog__title__line--item">
          <div class="dialog__title__line--item--lefts">
            <span style="color: #f76c84">*</span> 产品信息：
          </div>
          <div class="dialog__title__line--item--rights">
            <el-input
              v-model="currItem.productInfo"
              type="textarea"
              placeholder="输入产品标题信息"
              :rows="5"
              style="margin-left: 14px"
            ></el-input>
          </div>
        </div>
      </section>
      <span slot="footer" class="dialog--footer">
        <el-button @click="editPickCancel">取 消</el-button>
        <el-button type="primary" @click="editPickOk">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from "vue-property-decorator";
import PageManage from "@/components/PageManage.vue";
import DropDowm from "./components/DropDowm.vue";
import { RegionType, AddGoodSupplierType } from "./supplierManageType";
import {
  getSupList,
  addSup,
  delSup,
  updateSup,
  dealApplySup,
} from "@/api/good/goods";
import { SupplierManageState } from "./supplierManageType";
import address from "../common/city.js";

@Component({
  components: {
    PageManage,
    DropDowm,
  },
})
export default class SupplierManage extends Vue implements SupplierManageState {
  showType = false;

  searchMsg = {
    name: "",
    mobile: "",
    supplierSn: "",
  };

  searchType = "1";

  addressProv: Array<RegionType> = address.getProvince();

  addressCity: Array<RegionType> = [];

  addressArea: Array<RegionType> = [];

  list: Array<AddGoodSupplierType> = [];

  hasList = false;

  isIndeterminate = false;

  seletAll = false;

  selelctList: (string | number)[] = [];

  isModelAdd = false;

  editOrAdd = false;

  pageSize = 10;

  pageNum = 1;

  total = 0;

  isDeal = false;

  currItem = {
    provinceId: "",
    cityId: "",
    countryId: "",
    name: "",
    mobile: "",
    address: "",
    productInfo: "",
    province: "",
    city: "",
    country: "",
    area: "",
    id: "0",
    status: 1,
  } as AddGoodSupplierType;

  selelctData: Array<AddGoodSupplierType> = [];

  selectName: string[] = [];

  @Watch("selelctData")
  onSelelctListChanged(val: AddGoodSupplierType[]) {
    this.selelctList = val.map(item => item.id);
    this.selectName = val.map(item => item.name);
  }

  /**
   * 选择标签
   */
  selectType(tab: { name: string }) {
    this.searchType = tab.name;
    this.getList();
  }

  /**
   * 全选
   */
  handleCheckAllChange() {
    this.selelctList = this.seletAll ? this.list.map(item => item.id) : [];
    this.isIndeterminate = false;
  }

  /**
   * 多选操作
   */
  handleGoodIdsChange(goodId: number) {
    const selelctList = this.selelctList;
    const index = selelctList.findIndex(id => id === goodId);
    index === -1 ? selelctList.push(goodId) : selelctList.splice(index, 1);

    const checkedCount = selelctList.length;
    this.seletAll = checkedCount === this.list.length;
    this.isIndeterminate = checkedCount > 0 && checkedCount < this.list.length;
  }

  /**
   * 批量删除
   */
  batchGoods() {
    this.$confirm("确定要删除所有选中供应商吗?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      delSup(this.selelctList.join(","), {})
        .then(res => {
          if (res.code === 200) {
            this.$message.success("删除成功");
          }
          this.getList();
          this.seletAll = false;
        })
        .catch(err => {
          this.$message.warning(`${err}`);
          console.log(err);
        });
    });
  }

  /**
   * 新增供应商
   */
  addSup() {
    this.isModelAdd = true;
    this.editOrAdd = false;
    this.currItem = {
      provinceId: "",
      cityId: "",
      countryId: "",
      name: "",
      mobile: "",
      address: "",
      productInfo: "",
      province: "",
      city: "",
      country: "",
      area: "",
      id: "0",
      status: 1,
    } as AddGoodSupplierType;
  }

  /**
   * 选择省市区
   */
  selectChang(value: string, type: string) {
    if (type === "prov" && value) {
      this.addressCity = address.getCity(value);
      this.addressArea = [];
      this.currItem.cityId = "";
      this.currItem.countryId = "";
    }
    if (type === "city" && value) {
      this.addressArea = address.getArea(this.currItem.provinceId, value);
      this.currItem.countryId = "";
    }
    this.currItem.province = this.getAddressName(
      this.addressProv,
      this.currItem.provinceId,
    );
    this.currItem.city = this.getAddressName(
      this.addressCity,
      this.currItem.cityId,
    );
    this.currItem.country = this.getAddressName(
      this.addressArea,
      this.currItem.countryId,
    );

    this.currItem.area =
      this.currItem.province + this.currItem.city + this.currItem.country;
  }

  /**
   * 获取省市区所有数据
   */
  getAddressName(list: RegionType[], value: string) {
    let name = "";
    if (list.length > 0) {
      for (const item of list) {
        if (item.value === value) {
          name = item.label;
          break;
        }
      }
    }
    return name;
  }

  mounted() {
    this.showType = false;
    if (this.$route.query.options === "sup") {
      this.showType = true;
    }
    this.getList();
  }

  /**
   * 获取供应商数组
   */
  getList() {
    const param = {
      current: this.pageNum,
      size: this.pageSize,
      ...this.searchMsg,
      status: this.searchType,
    };
    getSupList(param).then(res => {
      this.list = res.data.list;
      this.hasList = res.data.list.length === 0 ? true : false;
      this.total = res.data.total;
    });
  }

  /** 搜索供应商 */
  search() {
    this.getList();
  }

  /**
   * 编辑供应商
   */
  editor(item: AddGoodSupplierType) {
    this.editOrAdd = true;
    this.isModelAdd = true;
    this.addressProv = address.getProvince();
    this.addressCity = address.getCity(item.province);
    this.addressArea = address.getArea(item.province, item.city);
    const province = this.getAddressName(
      this.addressProv,
      this.currItem.provinceId,
    );
    const city = this.getAddressName(this.addressCity, this.currItem.cityId);
    const country = this.getAddressName(
      this.addressArea,
      this.currItem.countryId,
    );
    this.currItem = {
      provinceId: item.province,
      cityId: item.city,
      countryId: item.country,
      name: item.name,
      mobile: item.mobile,
      address: item.address,
      productInfo: item.productInfo,
      province: province,
      city: city,
      country: country,
      area: item.area,
      id: item.id,
      status: item.status,
    } as AddGoodSupplierType;
  }

  /**
   * 取消编辑
   */
  editPickCancel() {
    this.$confirm(
      `确定要退出${
        this.editOrAdd ? "编辑" : "新增"
      }供应商页面?退出后，未保存的信息将不会保留!`,
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      },
    ).then(() => {
      this.isModelAdd = false;
    });
  }

  /**
   * 处理供应商审核
   */
  dealAudit(command: { type: string; item: AddGoodSupplierType }) {
    if (command.type === "1") {
      command.item.status = "1";
      this.isDeal = true;
      this.editor(command.item);
    } else {
      command.item.status = "0";
      dealApplySup(command.item)
        .then(res => {
          if (res.code === 200) {
            this.$message.success("关闭成功");
            this.getList();
            this.isModelAdd = false;
          }
        })
        .catch(res => {
          this.$message.error(res);
        });
    }
  }

  /**
   * 保存取消供应商信息
   */
  editPickOk() {
    this.currItem.city = this.currItem.cityId;
    this.currItem.country = this.currItem.countryId;
    this.currItem.province = this.currItem.provinceId;
    if (!this.currItem.name) {
      this.$message.error("供应商姓名不能为空");
      return;
    }

    if (!this.currItem.mobile) {
      this.$message.error("供应商号码不能为空");
      return;
    }

    if (!/^1[3456789]\d{9}$/.test(this.currItem.mobile)) {
      this.$message.error("手机号码有误，请重填");
      return;
    }

    if (!this.currItem.area || !this.currItem.country || !this.currItem.city) {
      this.$message.error("请输入供应商地区");
      return;
    }

    if (!this.currItem.address) {
      this.$message.error("请输入供应商地址");
      return;
    }

    if (!this.currItem.productInfo) {
      this.$message.error("请输入产品信息");
      return;
    }

    if (this.currItem.id === "0") {
      addSup(this.currItem)
        .then(res => {
          const result = res as any;
          if (result.code === 200) {
            this.$message.success("添加成功");
          } else {
            this.$message.error(result.msg);
          }
          this.getList();
          this.isModelAdd = false;
        })
        .catch(res => {
          this.$message.error(res);
        });
    } else {
      if (this.isDeal) {
        dealApplySup(this.currItem)
          .then(() => {
            this.$message.success("保存成功");
            this.getList();
            this.isModelAdd = false;
          })
          .catch(res => {
            this.$message.error(res);
          });
      } else {
        updateSup(this.currItem)
          .then(() => {
            this.$message.success("编辑成功");
            this.getList();
            this.isModelAdd = false;
          })
          .catch(res => {
            this.$message.error(res);
          });
      }
    }
  }

  /**
   * 删除供应商
   */
  delSupplier(_status: any, item: AddGoodSupplierType) {
    this.$confirm("删除该供应商，确认删除？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      delSup(item.id, {})
        .then(res => {
          if (res.code === 200) {
            this.$message.success("删除成功");
            this.getList();
          }
        })
        .catch(err => {
          this.$message.warning(`${err}`);
          console.log(err);
        });
    });
  }

  /** 启用/禁用供应商 */
  useSup(status: number, item: AddGoodSupplierType) {
    this.$confirm(
      `确定要${status === 1 ? "禁用" : "启用"}选中供应商吗?`,
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      },
    ).then(() => {
      item.status = status === 1 ? 3 : 1;
      updateSup(item).then(res => {
        if (res.code === 200) {
          this.$message.success(`${status === 1 ? "禁用" : "启用"}成功`);
          this.getList();
        }
      });
    });
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val: number) {
    this.pageSize = val;
    this.getList();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val: number) {
    this.pageNum = val;
    this.getList();
  }

  /**
   * 返回上一页
   */
  back() {
    this.$router.go(-1);
  }
}
</script>

<style lang="scss" scoped>
@import "../../../assets/styles/goods/index.scss";

.search__top {
  width: 100%;
  height: 150px;
  background-color: #f8f8f8;
  padding-top: 30px;
  &__line {
    display: flex;
    justify-content: space-around;
  }

  &__btn {
    margin-top: 10px;
    margin-left: 40px;
  }
}

.btn-line {
  padding-bottom: 15px;
}

.user--list {
  border-collapse: collapse;
  thead tr td {
    border-bottom: 1px solid #eee;
    border-top: 1px solid #eee;
    padding: 10px 5px;
    background-color: #f6f8fa;
    text-align: center;
    &:first-child {
      width: 1%;
    }
    &:nth-child(2) {
      width: 50px;
      text-align: left !important;
    }
    &:last-child {
      width: 90px;
    }
  }

  tbody tr:nth-child(2) td {
    background-color: #e9f3fb;
    height: 40px;
    padding: 5px 5px;
    border-right: 1px solid #e9f3fb;
    border-left: 1px solid #e9f3fb;
    font-size: 12px !important;
    .checkItem {
      margin-left: 15px;
    }
  }
  tbody tr:nth-child(3) td {
    border-bottom: 1px solid #e9f3fb;
    padding: 10px 5px;
    text-align: center;
    height: 75px;
    border-right: 1px solid #e9f3fb;

    &:first-child {
      border-left: 1px solid #e9f3fb;
    }
  }
}

.list__cell {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.dialog__title {
  display: flex;
  justify-content: center;
  font-weight: bold;
  font-size: 16px;
}

.dialog__title__line {
  padding: 0 30px;
  &--item {
    display: flex;
    margin-bottom: 15px;
    align-items: flex-start;
    &--lefts {
      text-align: right;
      line-height: 30px;
    }
    &--rights {
      display: flex;
      flex: 1;
      justify-content: space-between;
      margin-left: 10px;
    }
  }
}

.dialog--footer {
  display: flex;
  align-items: center;
  justify-content: center;
}

/deep/ .el-tabs__nav-wrap::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 2px;
  background-color: #f5f5f5;
  z-index: 1;
}

/deep/ .m__table--empty {
  width: 790px;
  margin-left: 75px;
}

/deep/ .m__table--empty .empty__td {
  width: 920px;
}

.listBottom {
  position: fixed;
  bottom: 10px;
  width: 940px !important;
  background-color: white;
  padding-bottom: 10px;
  z-index: 10;
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
  font-size: 14px;
  color: #b3b3b3;
  border-bottom: 1px solid #ebeef5;
}
</style>
