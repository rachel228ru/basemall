<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:58:56
-->
<template>
  <div>
    <div class="serveTop">
      <div style="width: 15%" class="center">服务商名称（编号）</div>
      <div style="width: 15%" class="center">面单数量</div>
      <div style="width: 40%" class="center">发货地址</div>
      <div style="width: 15%" class="center">状态</div>
      <div style="width: 15%" class="center">操作</div>
    </div>
    <div
      v-for="(item, index) in logCompany"
      :key="index"
      style="margin-top: 10px"
    >
      <div class="comPanyList">
        <div>
          {{ item.name }}
          <span style="margin-left: 10px; color: #0e76ff">{{
            item.phone
          }}</span>
          <span
            style="margin-left: 40px; cursor: pointer"
            @click="showLogistc(item)"
            >查看模板</span
          >
        </div>
        <div @click="addAddress(item)" style="color: #0e76ff; cursor: pointer">
          新增地址
        </div>
      </div>
      <div v-if="item.addressList.length > 0" class="comPanyAddress">
        <div class="comPanyAddress__logName">{{ item.name }}</div>
        <div class="comPanyAddress__logName">{{ item.number }}123</div>
        <div class="comPanyAddress__addList">
          <div
            v-for="(v, i) in item.addressList"
            :key="i"
            style="display: flex"
          >
            <div class="comPanyAddress__addList--content">
              <div style="width: 400px">
                负责人：{{ v.linkName }}({{ v.linkTel }})
              </div>
              <div style="width: 400px">
                发货信息：{{ v.name }}({{ v.phone }})
              </div>
              <div style="width: 400px">
                {{ v.province }}{{ v.city }}{{ v.country }}{{ v.address }}
              </div>
            </div>
            <div class="comPanyAddress__addList--apply">审核通过</div>
            <div class="comPanyAddress__addList--deal">
              <div class="comPanyAddress__addList--deal--way">
                <div @click="editCompany(item, v)">修改</div>
                <div style="margin-top: 10px" @click="delCompany(v.cid)">
                  删除
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="comPanyApply" @click="addAddress(item)">
        申请开通服务
      </div>
    </div>
    <el-dialog
      :visible.sync="showModel"
      width="500px"
      :before-close="handleModeClose"
    >
      <div slot="title" class="digTitle" style="color: black">面单预览</div>
      <img :src="imgList[imgIndex]" style="width: 100%; height: 100%" />
    </el-dialog>
    <el-dialog
      :visible.sync="addDialog"
      width="600px"
      :before-close="handleClose"
    >
      <div slot="title" class="digTitle">开通服务商</div>
      <AddModel
        ref="addModel"
        :editCurrent="editCurrent"
        :addDialog="addDialog"
      ></AddModel>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="openLogistic">开通</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import AddModel from "./common/AddModel.vue";
import {
  openLogAddress,
  getLogAddress,
  delCompony,
} from "@/api/logistics/logistics";
import { ApiLogisticsCompanyList, MixinsAddress } from "../../logisticType";

@Component({
  components: {
    AddModel,
  },
})
export default class LogisticsServe extends Vue {
  @Ref()
  readonly addModel!: AddModel;

  logCompany: Array<Partial<ApiLogisticsCompanyList>> = [
    {
      name: "顺丰快递",
      code: "sf",
      phone: "95546",
      id: "",
      number: "",
      addressList: [],
      linkName: "",
      customerName: "",
      customerPassword: "",
      linkTel: "",
    },
    {
      name: "中通快递",
      phone: "",
      code: "zt",
      id: "",
      number: "",
      addressList: [],
      linkTel: "",
      linkName: "",
      customerName: "",
      customerPassword: "",
    },
    {
      name: "圆通快递",
      phone: "",
      code: "yt",
      id: "",
      number: "",
      addressList: [],
      linkTel: "",
      linkName: "",
      customerName: "",
      customerPassword: "",
    },
    {
      name: "韵达快递",
      phone: "",
      code: "yd",
      id: "",
      number: "",
      addressList: [],
      linkTel: "",
      linkName: "",
      customerName: "",
      customerPassword: "",
    },
    {
      name: "百世快递",
      phone: "",
      code: "ht",
      id: "",
      number: "",
      addressList: [],
      linkTel: "",
      linkName: "",
      customerName: "",
      customerPassword: "",
    },
  ];

  imgList = [
    "http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/sf.png",
    "http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/logistics/zt.jpg",
    "http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/logistics/yt.jpg",
    "http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/logistics/yd.jpg",
    "http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/logistics/bs.jpg",
  ];

  imgIndex = 0;

  /** 新增模板 */
  addDialog = false;

  /** 编辑时选中模板 */
  editCurrent = {} as Partial<ApiLogisticsCompanyList>;

  /** 预览模板 */
  showModel = false;

  /** 新增地址是选择的物流公司 */
  chooseCompany = {
    name: "",
    code: "",
  };

  mounted() {
    this.getList();
  }

  /**
   * 获取已开通物流公司信息
   */
  getList() {
    getLogAddress({})
      .then(res => {
        res.data.forEach(item => {
          item.logisticsAddressVos.forEach(logItem => {
            logItem.cid = item.id;
            logItem.customerName = item.customerName;
            logItem.customerPassword = item.customerPassword;
            logItem.linkName = item.linkName;
            logItem.linkTel = item.linkTel;
          });
        });
        this.logCompany.forEach(comItem => {
          comItem.addressList = [];
          res.data.forEach(resItem => {
            if (comItem.code === resItem.code && comItem.addressList) {
              comItem.addressList = comItem.addressList.concat(
                resItem.logisticsAddressVos,
              );
            }
          });
        });
      })
      .catch(err => {
        this.$message.error(err.msg || "网络异常");
      });
  }

  /**
   * 根据选择的服务上新增模板
   */
  addAddress(item: ApiLogisticsCompanyList) {
    this.addDialog = true;
    this.editCurrent = {};
    this.chooseCompany.name = item.name;
    this.chooseCompany.code = item.code;
  }

  /**
   * 开通物流公司
   */
  openLogistic() {
    if (this.addModel.serveOption.code !== "sf") {
      if (
        !this.addModel.serveOption.customerName ||
        !this.addModel.serveOption.customerPassword
      ) {
        this.$message.warning("请完善页面内容");
        return;
      }
    }
    if (
      !this.addModel.serveOption.linkName ||
      !this.addModel.serveOption.linkTel ||
      !this.addModel.serveOption.name ||
      !this.addModel.serveOption.addressId
    ) {
      this.$message.warning("请完善页面内容");
      return;
    }
    this.addModel.serveOption.code = this.chooseCompany.code;
    this.addModel.serveOption.name = this.chooseCompany.name;
    openLogAddress(this.addModel.serveOption)
      .then(() => {
        this.$message.success("开通成功,请等待审核通过");
        this.addDialog = false;
        this.getList();
      })
      .catch(err => {
        this.$message.error(err || "网络异常");
      });
  }

  /**
   * 编辑物流公司
   */
  editCompany(item: ApiLogisticsCompanyList, v: MixinsAddress) {
    this.addDialog = true;
    this.editCurrent = item;
    this.editCurrent.getAddress = v;
    this.chooseCompany.name = item.name;
    this.chooseCompany.code = item.code;
  }

  /**
   * 删除选中物流公司
   */
  delCompany(cid: string) {
    this.$confirm("确定删除此发货地址吗?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      delCompony(cid, {})
        .then(() => {
          this.$message.success("删除成功");
          this.getList();
        })
        .catch(err => {
          this.$message.error(err || "网络异常");
        });
    });
  }

  /**
   * 关闭新增弹窗
   */
  handleClose() {
    this.addDialog = false;
  }

  /**
   * 查看不同物流电子面单
   */
  showLogistc(item: ApiLogisticsCompanyList) {
    this.showModel = true;
    switch (item.code) {
      case "sf":
        this.imgIndex = 0;
        break;
      case "zt":
        this.imgIndex = 1;
        break;
      case "yt":
        this.imgIndex = 2;
        break;
      case "yd":
        this.imgIndex = 3;
        break;
      case "ht":
        this.imgIndex = 4;
        break;
    }
  }

  /**
   * 关闭模板弹窗
   */
  handleModeClose() {
    this.showModel = false;
  }
}
</script>

<style lang="scss" scoped>
.serveTop {
  width: 100%;
  height: 40px;
  display: flex;
  align-items: center;
  background-color: #f6f8fa;
  color: #586884;
  font-size: 14px;
}

.center {
  display: flex;
  justify-content: center;
}

.comPanyList {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 40px;
  background-color: #f6f8fa;
  padding: 0px 20px;
  font-size: 12px;
  color: #0e76ff;
  border: 1px solid #cccccc;
}

@include b(comPanyAddress) {
  border: 1px solid #cccccc;
  display: flex;
  border-top: none;
  border-bottom: none;
  @include e(logName) {
    width: 15%;
    display: flex;
    align-items: center;
    justify-content: center;
    border-right: 1px solid #cccccc;
    border-bottom: 1px solid #cccccc;
  }
  @include e(addList) {
    width: 40%;
    display: flex;
    flex-wrap: wrap;
    @include m(content) {
      width: 400px;
      height: 100px;
      border-bottom: 1px solid #cccccc;
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      padding: 20px;
      border-right: 1px solid #cccccc;
    }

    @include m(apply) {
      width: 150px;
      border-bottom: 1px solid #cccccc;
      display: flex;
      align-items: center;
      justify-content: center;
      border-right: 1px solid #cccccc;
    }

    @include m(deal) {
      width: 136px;
      border-bottom: 1px solid #cccccc;
      display: flex;
      align-items: center;
      justify-content: center;
      @include m(way) {
        cursor: pointer;
        color: #006eff;
        width: 30px;
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
      }
    }
  }
}

.comPanyApply {
  width: 120px;
  height: 30px;
  border-radius: 5px;
  color: #006eff;
  border: 1px solid #006eff;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
  cursor: pointer;
}

.digTitle {
  font-size: 17px;
  font-weight: bold;
}
</style>
