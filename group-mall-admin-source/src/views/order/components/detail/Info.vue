<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:25:14
-->
<template>
  <div class="detail" v-if="Object.keys(detail).length">
    <div class="detail__status">
      当前订单状态：{{ getOrderStatusName(detail.status) }}
      <p v-if="detail.note">
        <el-popover
          placement="bottom-start"
          width="200"
          trigger="hover"
          :disabled="!row.note"
        >
          <div>备注：{{ row.note }}</div>
          <i slot="reference" class="iconfont iconPC-beizhu"></i>
        </el-popover>
        <span style="margin-left: -5px">{{ detail.note }}</span>
      </p>
      <p v-if="changeBtnVisible">
        <!-- <el-button class="detail__btn--mini" type="primary" size="mini" round
          >发货</el-button
        > -->
        <el-popover placement="bottom" width="500" v-model="visible">
          <el-form
            v-if="addressForm"
            ref="form"
            :model="addressForm"
            label-width="90px"
            style="margin-top: 20px"
          >
            <el-form-item label="收货地址：">
              <el-row>
                <el-col :span="8" class="form__col">
                  <el-select
                    @change="provinceChange"
                    v-model="areaData.receiverProvince"
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in province"
                      :key="item.value"
                      :label="item.label"
                      :value="item.localValue"
                    >
                    </el-option>
                  </el-select>
                </el-col>
                <el-col :span="8" class="form__col">
                  <el-select
                    @change="cityChange"
                    v-model="areaData.receiverCity"
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in city"
                      :key="item.value"
                      :label="item.label"
                      :value="item.localValue"
                    >
                    </el-option>
                  </el-select>
                </el-col>
                <el-col :span="8" class="form__col">
                  <el-select
                    @change="regionChange"
                    v-model="areaData.receiverRegion"
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in region"
                      :key="item.value"
                      :label="item.label"
                      :value="item.localValue"
                    >
                    </el-option>
                  </el-select>
                </el-col>
                <el-col :span="24" class="form__col" style="margin-top: 10px">
                  <el-input
                    v-model="addressForm.receiverDetailAddress"
                  ></el-input>
                </el-col>
              </el-row>
              <!-- <el-input v-model="addressForm.name"></el-input> -->
            </el-form-item>
            <el-form-item label="买家姓名：">
              <el-input v-model="addressForm.receiverName"></el-input>
            </el-form-item>
            <el-form-item label="手机号码：">
              <el-input v-model="addressForm.receiverPhone"></el-input>
            </el-form-item>

            <el-form-item>
              <el-row type="flex" justify="end">
                <el-col :span="9" class="form__col">
                  <el-button
                    @click="toggleVisible"
                    style="margin-left: auto"
                    class="detail__btn--small"
                    round
                    >取消</el-button
                  >
                  <el-button
                    @click="updateAddress"
                    type="primary"
                    class="detail__btn--small"
                    round
                    >确认</el-button
                  >
                </el-col>
              </el-row>
            </el-form-item>
          </el-form>

          <el-button
            slot="reference"
            class="detail__btn--mini"
            size="mini"
            round
            >修改地址</el-button
          >
        </el-popover>
      </p>
    </div>
    <div class="detail__field">
      <el-row :gutter="10">
        <el-col :span="24">
          <b>买家信息</b>
        </el-col>
        <el-col :span="8"> 微信昵称：{{ detail.userName }} </el-col>
        <template v-if="detail.orderDelivery">
          <el-col :span="8">
            买家姓名：{{ detail.orderDelivery.receiverName }}
          </el-col>
          <el-col :span="8">
            买家手机号： {{ detail.orderDelivery.receiverPhone }}
          </el-col>
          <el-col :span="24">
            收货地址：
            {{ detail.orderDelivery.receiverProvince }}
            {{ detail.orderDelivery.receiverCity }}
            {{ detail.orderDelivery.receiverRegion }}
            {{ detail.orderDelivery.receiverDetailAddress }}
          </el-col>
        </template>
        <template v-for="(item, index) of forms">
          <el-col v-if="item.value" :key="index" :span="8">
            {{ item.key }}：
            <template v-if="item.type === 'image'">
              <span class="image--btn" @click="lookImage(item.value)"
                >查看大图</span
              >
            </template>
            <template v-else-if="isDateItem(item.type)">
              {{ item.time }}
            </template>
            <template v-else>
              {{ item.value }}
            </template>
          </el-col>
        </template>
        <!-- <el-col :span="24"
                v-if=" detail.userNote">
          备注：{{ detail.userNote}}
        </el-col> -->
      </el-row>
      <el-divider></el-divider>
      <el-row :gutter="10">
        <el-col :span="24">
          <b>订单信息</b>
        </el-col>
        <el-col :span="8"> 订单编号：{{ detail.id }} </el-col>
        <el-col :span="8"> 创建时间：{{ detail.createTime }} </el-col>
        <el-col :span="8"> 支付方式：{{ getPayType(detail.payType) }} </el-col>
        <el-col :span="8" v-if="detail.payTime">
          付款时间：{{ detail.payTime }}
        </el-col>
        <el-col
          :span="8"
          v-if="detail.orderDelivery && detail.orderDelivery.deliveryTime"
        >
          发货时间：{{ detail.orderDelivery.deliveryTime }}
        </el-col>
        <el-col
          :span="8"
          v-if="detail.orderDelivery && detail.orderDelivery.receiveTime"
        >
          收货时间：{{ detail.orderDelivery.receiveTime }}
        </el-col>
        <el-col :span="8" v-if="detail.commentTime">
          评价时间：{{ detail.commentTime }}
        </el-col>
        <el-col :span="8" v-if="detail.closeTime">
          关闭时间：{{ detail.closeTime }}
        </el-col>
      </el-row>
    </div>
    <m-table
      :data="list"
      :single="true"
      :custom="true"
      tableHeaderClass="detail__table--header"
      :columns="columns"
      multipleKey="orderItemList"
      style="margin-top: 20px"
    >
      <template v-slot:custom-body="{ row }">
        <template v-for="(goods, i) of row.orderItemList">
          <tr :key="i">
            <td>
              <div class="table__goods">
                <div class="table__goods--image">
                  <img :src="goods.productPic" :alt="goods.productName" />
                </div>
                <div class="table__goods--info">
                  <div class="goods--name">
                    {{ goods.productName }}
                  </div>
                  <div class="goods--specs">
                    <span class="l">{{ goods.specs }}</span>
                  </div>
                </div>
              </div>
            </td>

            <td style="border-right: 1px solid #d8eaf9">
              <div>
                <div>单价： ￥{{ goods.productPrice }}</div>
                <div>
                  数量：
                  <span>*{{ goods.productQuantity }}</span>
                </div>
              </div>
            </td>

            <td class="tc" v-if="i === 0" :rowspan="row.orderItemList.length">
              ￥{{ detail.totalAmount }}
            </td>

            <td class="tc" v-if="i === 0" :rowspan="row.orderItemList.length">
              {{ getDeliveryTypeName(row.orderDelivery.deliveryType) }}
            </td>

            <td class="tc" v-if="i === 0" :rowspan="row.orderItemList.length">
              ￥{{ detail.payAmount }} <br />
              <span v-if="detail.privileges && detail.privileges.includes('2')"
                >免配送费</span
              >
              <span v-else>(含配送:￥{{ detail.freightAmount }})</span>
            </td>
          </tr>
        </template>
      </template>
    </m-table>
    <div class="detail__price">
      {{
        detail.status === "WAIT_FOR_PAY" || isClose(detail.status)
          ? "待付款"
          : "实收款"
      }}
      ： <b>{{ detail.payAmount }}</b> 元
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import { DeliveryOrderList } from "../../orderType";
import {
  AreaDataType,
  OrderAddressType,
  OrderDetailState,
} from "./orderDetailType";
import { RegionType } from "@/views/goods/goodManage/supplierManageType";
import {
  getOrderStatusName,
  getDeliveryTypeName,
  isClose,
} from "../../common/order";
import { updateAddress } from "@/api/order";
import { getArea } from "@/api/global";
interface NewDeliveryOrderList extends DeliveryOrderList {
  localValue: string;
}
@Component
export default class Info extends Vue implements OrderDetailState {
  @Prop({
    default() {
      return {};
    },
  })
  // FIXME: 其他组件引入所需类型联合
  detail!: NewDeliveryOrderList | any;

  @Prop({ default: false })
  isGroup?: boolean;

  get forms() {
    if (!this.detail.customForm) {
      return [];
    }

    return JSON.parse(this.detail.customForm);
  }

  get list() {
    return [this.detail];
  }

  get changeBtnVisible() {
    return (
      this.detail.status === "WAIT_FOR_SEND" ||
      this.detail.status === "WAIT_FOR_PAY"
    );
  }

  addressForm = {
    orderId: null,
    receiverDetailAddress: "",
    receiverName: "",
    receiverPhone: "",
    receiverPostCode: "",
    receiverProvince: "",
    receiverCity: "",
    receiverRegion: "",
  } as OrderAddressType;

  areaData = {
    receiverProvince: "",
    receiverCity: "",
    receiverRegion: "",
    receiverPostCode: "",
  } as AreaDataType;

  province: Array<RegionType> = [];

  city: Array<RegionType> = [];

  region: Array<RegionType> = [];

  visible = false;

  columns = [
    {
      label: "商品",
      width: 270,
      coustomStyle: "text-align: left;",
    },
    {
      label: "单价&数量 ",
    },
    {
      label: "总价",
    },
    {
      label: "配送方式",
    },
    {
      label: "实收款",
    },
  ];

  @Watch("detail")
  setForm() {
    Object.assign(this.addressForm, this.detail.orderDelivery);
    this.getDefauleAddressData(this.addressForm);
  }

  provinceChange(v: string) {
    const currentProvince = this.province.find(item => {
      return item.label === v;
    }) as RegionType;
    this.getArea(
      { type: 2, id: currentProvince.value },
      "city",
      (list: NewDeliveryOrderList[]) => {
        this.areaData.receiverCity = list[0].localValue;
        this.cityChange(list[0].localValue);
        // this.cityChange[this.areaData.receiverCity];
      },
    );
  }

  cityChange(v: string) {
    const currentCity = this.city.find(item => {
      return item.label === v;
    }) as RegionType;
    this.getArea(
      { type: 3, id: currentCity.value },
      "region",
      (list: NewDeliveryOrderList[]) => {
        this.areaData.receiverRegion = list[0].localValue;
      },
    );
  }

  // @Watch("areaData.receiverRegion")
  regionChange(v: string) {
    const currentRegion = this.region.find(item => {
      return item.label === v;
    }) as RegionType | any;
    this.areaData.receiverRegion = currentRegion.localValue;
    this.areaData.receiverPostCode = currentRegion.value;
  }

  /** 获取默认地区数据 */
  getDefauleAddressData({
    receiverProvince,
    receiverCity,
    receiverRegion,
  }: AreaDataType) {
    this.getArea({ type: 1 }, "province", (province: RegionType[]) => {
      const currentProvince = province.find(item => {
        return item.label === receiverProvince;
      }) as RegionType;

      this.getArea(
        { type: 2, id: currentProvince.value },
        "city",
        (city: RegionType[]) => {
          const currentCity = city.find(item =>
            item.label === receiverCity && receiverCity === "北京市"
              ? "市辖区"
              : receiverCity,
          ) as RegionType;

          this.getArea(
            { type: 3, id: currentCity.value },
            "region",
            (region: RegionType[]) => {
              const currentRegion = region.find(
                item => item.label === receiverRegion,
              );
              console.log(currentRegion);
              Object.assign(this.areaData, {
                receiverProvince,
                receiverCity,
                receiverRegion,
              });
            },
          );
        },
      );
    });
  }

  getPayType(key: string) {
    return {
      UNPAID: "未支付",
      WECHAT: "微信支付",
    }[key];
  }

  /** 获取地区数据 */
  getArea(form: { type: number; id?: string }, key: string, cb?: any) {
    if (!this.changeBtnVisible) return;
    getArea(form)
      .then(res => {
        Reflect.set(
          this,
          key,
          res.data.map((item: { label: string; localValue: string }) => {
            item.localValue = item.label;
            return item;
          }),
        );
        cb && cb(res.data);
      })
      .catch(err => {
        console.log(err);
        this.$message.warning("地区数据获取失败");
      });
  }

  updateAddress() {
    updateAddress({
      ...this.addressForm,
      orderId: this.detail.id,
      ...this.areaData,
    })
      .then(() => {
        this.$message.success("更新成功");
        this.$emit("reset", this.detail);
        this.toggleVisible();
      })
      .catch(err => {
        this.$message.warning(err);
      });
  }

  getOrderStatusName = getOrderStatusName;

  getDeliveryTypeName = getDeliveryTypeName;

  isClose = isClose;

  lookImage(url: string) {
    this.$alert(`<img src="${url}" style="width:375px" />`, "", {
      dangerouslyUseHTMLString: true,
    });
  }

  isDateItem(type: string) {
    return ["date", "datetime", "time"].indexOf(type) !== -1;
  }

  toggleVisible() {
    this.visible = !this.visible;
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/order/detail.scss";

.iconPC-beizhu {
  color: red;
  font-size: 22px;
  position: relative;
  bottom: -3px;
  left: -5px;
}

.image--btn {
  cursor: pointer;
  color: rgb(23, 143, 241);
}

.detail__btn--mini {
  padding: 3px 5px;
  margin-top: 10px;
}

.detail__btn--small {
  padding: 10px 18px;
  margin-top: 10px;
}

.form__col {
  padding-right: 10px;
}
</style>
