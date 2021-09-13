<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 13:20:00
-->
<template>
  <div class="pageRoll">
    <el-form
      :model="formModel"
      :rules="formRules"
      ref="formRef"
      label-width="100px"
    >
      <div class="navLine">基本信息</div>
      <div class="baseMsg">
        <el-form-item label="商品名称" prop="name">
          <el-input
            v-model="formModel.name"
            style="width: 550px"
            maxlength="30"
            placeholder="请输入商品名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="卖点描述">
          <el-input
            v-model="formModel.saleDescribe"
            style="width: 550px"
            maxlength="60"
            placeholder="请输入60字以内卖点描述"
          ></el-input>
        </el-form-item>
        <el-form-item label="销售专区" prop="saleName" v-if="showSale">
          <el-select
            v-model="formModel.saleName"
            style="width: 550px"
            placeholder="请选择销售专区"
            @change="selectMode"
          >
            <el-option
              v-for="item in saleApartList"
              :key="item.id"
              :label="item.modeName"
              :value="item.modeName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="展示分类" prop="showCategoryIds">
          <el-select
            v-model="formModel.showCategoryIds"
            style="width: 550px"
            multiple
            placeholder="请选择分类"
            :popper-append-to-body="false"
          >
            <el-option-group
              v-for="group in temShowList"
              :key="group.showCategoryId"
              :label="group.name"
            >
              <el-option
                v-for="item in group.showCategoryVos"
                :key="item.showCategoryId"
                :label="item.name"
                :value="item.showCategoryId"
              ></el-option>
            </el-option-group>
          </el-select>
          <el-button type="text" class="ml-20" @click="jumpClass(1)"
            >前往设置</el-button
          >
        </el-form-item>
        <el-form-item label="供应商">
          <el-select
            v-model="formModel.providerId"
            style="width: 550px"
            placeholder="请选择供应商"
            :popper-append-to-body="false"
          >
            <el-option
              v-for="item in temSupList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
          <el-button type="text" class="ml-20" @click="jumpClass(3)"
            >前往设置</el-button
          >
        </el-form-item>
        <el-form-item label="商品属性">
          <el-select
            v-model="formModel.attributeId"
            placeholder="请选择属性模板"
            style="width: 550px"
            @change="selectTemAttsList"
            :popper-append-to-body="false"
          >
            <el-option
              v-for="item in temAttsList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
          <el-button type="text" class="ml-20" @click="jumpClass(4)"
            >前往设置</el-button
          >
          <div class="valueName">
            <div class="flex between">
              <div>属性名称</div>
              <div>属性值</div>
              <div>操作</div>
            </div>
            <div class="valueName__noList" v-if="!showAttributes">
              暂无数据~
            </div>
            <div
              class="valueName__List"
              v-for="(item, index) in formModel.productAttributes"
              :key="index"
              v-else
            >
              <el-input
                v-model="item.name"
                style="width: 230px"
                maxlength="10"
              ></el-input>
              <el-input
                v-model="item.value"
                style="width: 230px"
                maxlength="100"
              ></el-input>
              <div
                @click="formModel.productAttributes.splice(index, 1)"
                style="width: 30px"
              >
                删除
              </div>
            </div>
          </div>
        </el-form-item>
        <el-button
          type="primary"
          plain
          style="margin-left: 100px; margin-bottom: 20px"
          size="small"
          @click="addTemAtts"
          >+ 添加属性</el-button
        >
        <el-form-item label="商品大图">
          <div class="product" style="display: flex">
            <el-upload
              class="productImg__uploader product__productImg"
              :auto-upload="false"
              :on-change="addUploadBigProductImg"
              action
              :show-file-list="false"
              style="width: 250px; height: 120px"
            >
              <img
                v-if="widePic"
                :src="widePic"
                class="product__productImg"
                style="width: 250px; height: 120px"
              />
              <i
                class="el-icon-plus product__productImg--uploader--icon"
                v-if="!widePic"
                style="width: 250px; line-height: 100px"
              ></i>
              <div
                class="product__productImg--uploader--add"
                v-if="!widePic"
                style="top: 70px; left: 100px"
              >
                添加图片
              </div>
            </el-upload>
          </div>
          <div class="text-info fs-12" style="color: rgba(69, 64, 60, 0.6)">
            尺寸建议750x350（长方形模式）像素以上，大小1M以下
          </div>
        </el-form-item>
        <el-form-item label="商品视频">
          <div class="productVideo__uploader">
            <el-upload
              class="productImg__uploader product__productVideo"
              :auto-upload="false"
              :on-change="uploadProductVideo"
              action
              :show-file-list="false"
            >
              <video
                v-if="formModel.videoUrl !== ''"
                controls
                :src="formModel.videoUrl"
                style="width: 100%; height: auto"
              ></video>
              <i
                v-else
                class="el-icon-plus product__productVideo--uploader--icon"
              ></i>
            </el-upload>
            <el-upload
              style="margin-left: 20px"
              :auto-upload="false"
              action
              :on-change="uploadProductVideo"
              :show-file-list="false"
            >
              <el-button type="text">替换</el-button>
            </el-upload>
            <el-button
              type="text"
              style="margin-left: 20px"
              class="fc-fe5f4b"
              @click="deleteVideoUrl"
              >删除</el-button
            >
          </div>
          <div class="text-info fs-12" style="color: rgba(69, 64, 60, 0.6)">
            请上传5M以内的视图视频（该功能免费试用）
          </div>
        </el-form-item>
        <el-form-item label="商品主图" prop="pic">
          <div class="product" style="display: flex">
            <draggable v-model="productImgList">
              <div
                v-for="(item, index) in productImgList"
                :key="index"
                style="position: relative; margin-right: 20px"
                class="product__productImg"
                draggable="true"
              >
                <el-upload
                  class="productImg__uploader product__productImg"
                  :auto-upload="false"
                  :on-change="uploadProductImg"
                  action
                  :show-file-list="false"
                >
                  <img
                    v-if="item.img"
                    :src="item.img"
                    @click="productImgIndex = index"
                    class="product__productImg"
                  />
                </el-upload>
                <div v-if="index === 0" class="productImg-text">封面图</div>
                <i
                  class="el-icon-circle-close productImg-icon"
                  v-if="item.img"
                  @click="cancelImg(item, index)"
                ></i>
              </div>
            </draggable>
            <el-upload
              class="productImg__uploader product__productImg"
              :auto-upload="false"
              :on-change="addUploadProductImg"
              action
              :show-file-list="false"
              v-if="productImgList.length < 6"
            >
              <i class="el-icon-plus product__productImg--uploader--icon"></i>
              <div class="product__productImg--uploader--add">添加图片</div>
            </el-upload>
          </div>
          <div class="text-info fs-12" style="color: rgba(69, 64, 60, 0.6)">
            尺寸建议750x750（正方形模式）像素以上，大小1M以下，最多6张
            (可拖拽图片调整顺序 )
          </div>
        </el-form-item>
      </div>
      <div class="navLine">物流设置</div>
      <div class="sendMsg">
        <el-form-item label="配送方式" prop="distributionMode">
          <el-radio
            v-model="formModel.distributionMode"
            :label="1"
            @change="getLogModel"
            >快递配送</el-radio
          >
        </el-form-item>
        <el-form-item
          label="运费设置"
          prop="freightTemplateId"
          v-if="formModel.distributionMode === 1"
        >
          <el-select
            v-model="formModel.freightTemplateId"
            placeholder="请选择运费模板"
            @change="selectLogModel"
            :popper-append-to-body="false"
          >
            <el-option
              v-for="item in logModel"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
          <el-button type="text" class="ml-20" @click="jumpClass(5)"
            >前往设置</el-button
          >
          <LogModel
            :templateId="formModel.freightTemplateId"
            ref="logRef"
            @getModelOption="getModelOption"
          ></LogModel>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref, Prop, Watch } from "vue-property-decorator";
import draggable from "vuedraggable";
import LogModel from "@/views/logistics/common/LogModel.vue";
import AddGood from "@/views/goods/marketModel/AddGood.vue";
import NewGoodForm from "./NewGoodForm.vue";
import {
  GoodDetailInfo,
  GoodCategroyType,
  GoodSupplierType,
  GoodAttributeTempType,
  FreightTempType,
} from "@/views/goods/marketModel/goodType";
import { ApiSpecArea } from "@/views/goods/marketModel/marketType";
import { AddSubmitFormType } from "./newGoodType";
import {
  AnyMixinGoodDetailInfo,
  NewGoodBaseState,
} from "./newGoodBaseInfoType";
import { upLoad } from "@/api/index";
import {
  getAllCategory,
  getAllAttsList,
  getLogistics,
  getAllRegionList,
  getAllSupList,
} from "@/api/good/goods";
import storage from "@/libs/storage";
import { readFile, advanceGetImage } from "@/store/modules/voidImg";
import { ElForm } from "element-ui/types/form";
import { ElUploadInternalFileDetail } from "element-ui/types/upload";

/**
 * 编辑基本信息
 */
@Component({
  components: {
    draggable,
    LogModel,
  },
})
export default class NewGoodBaseInfo extends Vue implements NewGoodBaseState {
  @Prop({
    type: Object,
    default() {
      return null;
    },
  })
  readonly goodDetail!: GoodDetailInfo;

  @Ref()
  readonly logRef!: LogModel;

  formModel = {} as Partial<AddSubmitFormType>;

  widePic = "";

  showAttributes = false;

  /** 基本信息验证 */
  formRules = {
    name: [{ required: true, message: "请输入商品名称", trigger: "blur" }],
    saleName: [{ required: true, message: "请选择销售专区", trigger: "blur" }],
    showCategoryIds: [
      { required: true, message: "请选择分类", trigger: "blur" },
    ],
    pic: [
      {
        required: true,
        validator: (
          _rule: any,
          value: boolean,
          callback: (arg?: Error) => void,
        ) => {
          if (!value) {
            callback(new Error("请上传商品主图"));
          } else {
            callback();
          }
        },
        trigger: "change",
      },
    ],
    distributionMode: [
      {
        required: true,
        message: "请选择配送方式",
        trigger: "change",
      },
    ],
    freightTemplateId: [
      {
        required: true,
        message: "请选择物流模板",
        trigger: "change",
      },
    ],
  };

  saleApartList: Array<ApiSpecArea> = [];

  temShowList: Array<GoodCategroyType> = [];

  temSupList: Array<GoodSupplierType> = [];

  temAttsList: Array<GoodAttributeTempType> = [];

  logModel: Array<FreightTempType> = [];

  productImgList: Array<{ img: string }> = [];

  productImgIndex = 0;

  showSale = false;

  saleMode: string | number | null = null;

  @Ref()
  readonly formRef!: ElForm;

  @Watch("formModel.productAttributes")
  getShowAttributes() {
    this.showAttributes = this.formModel.productAttributes
      ? this.formModel.productAttributes.length > 0
        ? true
        : false
      : false;
  }

  /**
   * 还原表单数据
   */
  @Watch("goodDetail")
  setFormModel(nVal: AnyMixinGoodDetailInfo) {
    if (nVal) {
      const parentHtml = this.$parent as NewGoodForm;
      getAllRegionList({})
        .then(res => {
          this.saleApartList = res.data;
        })
        .then(async () => {
          const formModel = this.goodDetail
            ? (JSON.parse(
              JSON.stringify(this.goodDetail),
            ) as AnyMixinGoodDetailInfo)
            : (storage.get("formModel") as AnyMixinGoodDetailInfo);
          for (const key of Object.keys(formModel)) {
            const value = nVal[key];
            if (value) {
              formModel[key] = value;
            }
          }
          formModel.showCategoryIds = this.getShowCategoryIds(
            nVal.productShowCategorys,
          );
          formModel.freightTemplateId =
            formModel.freightTemplateId === "0"
              ? "0"
              : formModel.freightTemplateId;
          formModel.videoUrl = formModel.videoUrl ? formModel.videoUrl : "";
          this.widePic = formModel.widePic;
          this.showSale = parentHtml.from === "csv" ? true : false;
          if (parentHtml.from === "csv") {
            formModel.distributionMode = 1;
            formModel.freightTemplateId = "0";
          }
          if (this.showSale) {
            formModel.saleName = this.saleApartList[0].modeName;
          }
          this.productImgList = this.getProductImgList(nVal.albumPics);
          this.formModel = formModel;
          if (this.showSale) {
            const dropItem = this.saleApartList.filter(item => {
              return item.modeName === this.formModel.saleName;
            })[0];
            if (dropItem) {
              this.saleMode = dropItem.id;
              this.formModel.saleMode = dropItem.id;
              this.getTemShowList(); // 展示分类
            }
          }
        });
    }
  }

  @Watch("formModel.saleName")
  getDropItem() {
    const dropItem = this.saleApartList.filter(item => {
      return item.modeName === this.formModel.saleName;
    })[0];
    if (dropItem) {
      this.saleMode = dropItem.id;
      this.formModel.saleMode = dropItem.id;
      this.getTemShowList(); // 展示分类
    }
  }

  /**
   * 点击下拉销售区域
   */
  selectMode() {
    this.temShowList = [];
    this.formModel.showCategoryIds = [];
  }

  mounted() {
    const parentHtml = this.$parent.$parent.$parent.$parent.$parent as AddGood;
    this.saleMode = parentHtml.saleMode || parentHtml.saleMode[0];
    storage.delete("allFoorm");
    this.getTemShowList(); // 展示分类
    this.getTemSupList(); // 供应商模板
    this.getTemAttsList(); // 属性模板
    this.getLogis(); //  物流模板
    if (storage.get("formModel")) {
      this.formModel = JSON.parse(JSON.stringify(storage.get("formModel")));
      this.widePic = this.formModel.widePic ? this.formModel.widePic : "";
      this.formModel.videoUrl = this.formModel.videoUrl
        ? this.formModel.videoUrl
        : "";
      this.productImgList = this.getProductImgList(
        storage.get("formModel").albumPics,
      );
      if (this.formModel.saleName) {
        this.showSale = true;
        this.setFormModel(storage.get("formModel"));
      }
      if (this.formModel.skuStocks && this.formModel.skuStocks.length === 1) {
        this.formModel.originalPrice = this.formModel.skuStocks[0].originalPrice;
        this.formModel.price = this.formModel.skuStocks[0].price;
        this.formModel.stock = Number(this.formModel.skuStocks[0].stock);
      }

      if (storage.get("formModel").id) {
        this.formModel.showCategoryIds = this.getShowCategoryIds(
          storage.get("formModel").productShowCategorys,
        );
      }
    } else {
      this.getModel();
    }
  }

  getModel() {
    if (!this.goodDetail) {
      const parentHtml = this.$parent as NewGoodForm;
      this.formModel = parentHtml.goodFormModel;
      this.formModel.saleMode = String(this.saleMode);
      this.formModel.freightTemplateId = "0";
    }
  }

  /**
   * 获取表单数据
   */
  async getFormModel() {
    try {
      this.setAlbumPics();
      await this.validate();
      // this.setSortingCategoryName();
      this.setProviderName();
      const formModel = this.formModel;
      const productShowCategorys = this.getProductShowCategorys();
      this.formModel.freightTemplateId = this.logRef.templateId;
      return {
        ...formModel,
        productShowCategorys,
      };
    } catch (error) {
      return Promise.reject(error);
    }
  }

  /**
   * 对整个表单进行校验
   */
  validate() {
    return this.formRef.validate();
  }

  /**
   * 移除表单项的校验结果
   */
  clearValidate() {
    this.formRef.clearValidate();
  }

  /**
   * 获取展示分类
   */
  async getTemShowList() {
    const param = {
      saleMode: this.saleMode,
    };
    const { data } = await getAllCategory(param);
    this.temShowList = JSON.parse(JSON.stringify(data)) || [];
  }

  /**
   * 获取供应商
   */
  async getTemSupList() {
    const { data } = await getAllSupList({});
    this.temSupList = data || [];
  }

  /**
   * 设置供应商名称
   */
  setProviderName() {
    const providerId = this.formModel.providerId;
    const currentTempSup = this.temSupList.find(item => item.id === providerId);
    this.formModel.providerName = currentTempSup ? currentTempSup.name : "";
  }

  /**
   * 获取展示分类
   */
  getProductShowCategorys() {
    const vals = this.formModel.showCategoryIds as (string | number)[];
    const productShowCategorys: Omit<
      GoodCategroyType,
      "saleMode" | "showCategoryVos" | "sort"
    >[] = [];
    this.temShowList.forEach(item => {
      const productShowCategorySeconds = item.showCategoryVos.filter(child => {
        return vals.includes(child.showCategoryId);
      });
      if (productShowCategorySeconds.length) {
        const parentItem = {
          id: item.id,
          name: item.name,
          parentId: item.parentId,
          showCategoryId: item.showCategoryId,
          productShowCategorySeconds: productShowCategorySeconds.map(
            seconds => {
              return { ...seconds };
            },
          ),
        };
        productShowCategorys.push(parentItem);
      }
    });
    return productShowCategorys;
  }

  /**
   * 还原展示分类id
   */
  getShowCategoryIds(
    showCategorys: Omit<
      GoodCategroyType,
      "saleMode" | "showCategoryVos" | "sort"
    >[] = [],
  ) {
    let showCategoryId: string[] = [];
    showCategorys.forEach(item => {
      if (item.productShowCategorySeconds) {
        const ids = item.productShowCategorySeconds.map(child =>
          String(child.showCategoryId),
        );
        showCategoryId = showCategoryId.concat(ids);
      }
    });
    return showCategoryId;
  }

  /**
   * 获取商品属性
   */
  async getTemAttsList() {
    const { data } = await getAllAttsList({});
    this.temAttsList = data || [];
  }

  /**
   * 选择商品属性
   */
  selectTemAttsList(temAttsId: number) {
    const currenttemAtts = this.temAttsList.find(item => item.id === temAttsId);
    if (currenttemAtts) {
      this.formModel.attribute = currenttemAtts.name;
      this.formModel.productAttributes = currenttemAtts.attributeTemplates.map(
        item => {
          return {
            ...item,
            value: item.content,
          };
        },
      );
    }
  }

  /**
   * 添加商品属性
   */
  addTemAtts() {
    const obj = {
      content: "",
      name: "",
      id: "",
      parentId: "",
      value: "",
    };
    if (this.formModel.productAttributes) {
      this.formModel.productAttributes.push(obj);
    }
  }

  /**
   * 跳转分类/供货商/属性模板
   */
  async jumpClass(type: number) {
    let location: any;
    const parentHtml = this.$parent as NewGoodForm;
    const outerParentHtml = this.$parent.$parent.$parent.$parent
      .$parent as AddGood;
    this.setAlbumPics();
    if (parentHtml.from === "csv" && this.formModel.productShowCategorys) {
      this.formModel.productShowCategorys = this.getProductShowCategorys();
      storage.set("formModel", JSON.parse(JSON.stringify(this.formModel)));
      storage.set("allFoorm", JSON.parse(JSON.stringify(this.formModel)));
    } else {
      if (!this.goodDetail) {
        parentHtml.allFoorm = Object.assign(
          parentHtml.allFoorm,
          this.formModel,
        );
        storage.set(
          "formModel",
          JSON.parse(JSON.stringify(parentHtml.allFoorm)),
        );
        storage.set(
          "allFoorm",
          JSON.parse(JSON.stringify(parentHtml.allFoorm)),
        );
      } else {
        storage.set("formModel", JSON.parse(JSON.stringify(this.goodDetail)));
        storage.set("allFoorm", JSON.parse(JSON.stringify(this.goodDetail)));
      }
    }
    outerParentHtml.saveFlag = true;
    switch (type) {
      case 1:
        location = {
          name: "class",
          query: {
            options: "1",
            saleMode: this.saleMode || this.formModel.saleMode,
          },
          params: {
            options: "1",
            saleMode: this.saleMode || this.formModel.saleMode,
          },
        };
        break;
      case 2:
        location = {
          name: "class",
          query: {
            options: "2",
            saleMode: this.saleMode || this.formModel.saleMode,
          },
          params: {
            options: "2",
            saleMode: this.saleMode || this.formModel.saleMode,
          },
        };
        break;
      case 3:
        location = {
          name: "sup",
          query: { options: "sup" },
          params: { options: "sup" },
        };
        break;
      case 4:
        location = {
          name: "Attribute",
          query: { options: "pick" },
          params: { options: "sup" },
        };
        break;
      case 5:
        location = {
          name: "logistics",
          query: { options: "log" },
          params: { options: "sup" },
        };
        break;
    }
    this.$router.push(location);
  }

  /**
   * @method uploadProductImg
   * @description 修改商品主图
   */
  async uploadProductImg(file: ElUploadInternalFileDetail) {
    const whiteList = ["image/jpeg", "image/jpg", "image/png"];

    const isLt1M = file.size < 1 * 1024 * 1024;
    if (!whiteList.includes(file.raw.type)) {
      this.$message.error("上传文件只能是 JPG或PNG 格式!");
      return;
    }
    if (!isLt1M) {
      this.$message.error("上传文件大小不能超过 1MB!");
      return;
    }

    const res = await upLoad({
      file: file.raw,
    });
    this.productImgList[this.productImgIndex].img = res.data;
    this.formRef.clearValidate("pic");
  }

  /**
   * 上传图片
   */
  async addUploadProductImg(file: ElUploadInternalFileDetail) {
    const whiteList = ["image/jpeg", "image/jpg", "image/png"];

    const isLt1M = file.size < 1 * 1024 * 1024;
    if (!whiteList.includes(file.raw.type)) {
      this.$message.error("上传文件只能是 JPG或PNG 格式!");
      return;
    }
    if (!isLt1M) {
      this.$message.error("上传文件大小不能超过 1MB!");
      return;
    }

    const res = await upLoad({
      file: file.raw,
    });
    this.productImgList.push({
      img: res.data,
    });
  }

  /**
   * 上传大图片
   */
  async addUploadBigProductImg(file: ElUploadInternalFileDetail) {
    const whiteList = ["image/jpeg", "image/jpg", "image/png"];

    const isLt1M = file.size < 1 * 1024 * 1024;
    if (!whiteList.includes(file.raw.type)) {
      this.$message.error("上传文件只能是 JPG或PNG 格式!");
      return;
    }
    if (!isLt1M) {
      this.$message.error("上传文件大小不能超过 1MB!");
      return;
    }

    const imgFile = await readFile(file.raw);
    const newImg = await advanceGetImage(imgFile);
    if (newImg.image.width !== 750 || newImg.image.height !== 350) {
      this.$message.error("宽屏图片长宽应为750 * 350");
      return;
    }

    const res = await upLoad({
      file: file.raw,
    });
    this.widePic = res.data;
    this.formModel.widePic = res.data;
  }

  /**
   * 删除以选中图片
   */
  cancelImg(_item: any, index: number) {
    this.productImgList.splice(index, 1);
    if (this.productImgList.length == 0) {
      this.formModel.pic = "";
    }
  }

  /**
   *  设置商品图片
   */
  setAlbumPics() {
    const productImgList = this.productImgList;
    if (productImgList.length > 0) {
      this.formModel.pic = productImgList[0].img;
      this.formModel.albumPics = productImgList
        .filter(item => item.img !== "")
        .map(item => item.img)
        .join(",");
    }
  }

  /**
   * 还原商品图片
   */
  getProductImgList(albumPics: string) {
    if (albumPics) {
      const imgList = albumPics.split(",").map(item => {
        return { img: item };
      });
      return imgList;
    } else {
      return [];
    }
  }

  /**
   * 上传视频
   */
  async uploadProductVideo(file: ElUploadInternalFileDetail) {
    const whiteList = ["video/mp4"];
    const isLt = file.size < 5 * 1024 * 1024;

    if (!whiteList.includes(file.raw.type)) {
      this.$message.error("上传视频只能是 mp4 格式!");
      return;
    }

    if (!isLt) {
      this.$message.error("上传视频大小不超过5M!");
      return;
    }

    const res = await upLoad({
      file: file.raw,
    });
    this.formModel.videoUrl = res.data;
  }

  /**
   * 删除视频
   */
  deleteVideoUrl() {
    this.formModel.videoUrl = "";
  }

  /**
   * 获取物流模板
   */
  async getLogis() {
    const res = await getLogistics({});
    res.data.unshift({
      id: "0",
      name: "商家包邮",
    });
    this.logModel = res.data;
  }

  /**
   * 切换配送方式
   */
  getLogModel(e: number) {
    const parentHtml = this.$parent as NewGoodForm;
    parentHtml.isWeight = false;
    this.$emit("changeLogisticsId");
    if (e === 1) {
      this.formModel.freightTemplateId = "0";
    }
  }

  /**
   * 选择物流模板
   */
  selectLogModel(e: string) {
    const parentHtml = this.$parent as NewGoodForm;
    this.formModel.freightTemplateId = e;
    parentHtml.logisticsId = e;
    parentHtml.isWeight = e === "0" ? false : true;
  }

  /**
   * 获取模板信息
   */
  getModelOption(modelOption: FreightTempType) {
    const parentHtml = this.$parent as NewGoodForm;
    parentHtml.isWeight =
      Number(modelOption.valuationModel) === 1 ? false : true;
  }
}
</script>
