<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:08:31
-->
<template>
  <!-- 店铺导航 -->
  <div class="storeNavigation-item-form">
    <el-form :model="formData" label-width="85px" style="marginTop:15px">
      <el-form-item label="导航名称">
        <el-input
          v-model="formData.navName"
          :maxlength="6"
          placeholder="请输入导航名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="字体颜色">
        <el-color-picker v-model="formData.fontColor"></el-color-picker>
      </el-form-item>
      <el-form-item label="图标">
        <upload-file :img-url.sync="formData.navIcon"></upload-file>
      </el-form-item>

      <el-form-item label="跳转路径">
        <link-select :link="link"></link-select>
        <div v-if="link.type !== 6">
          <span style="color: #9797A1">{{ formData.linkName }}</span>
        </div>
      </el-form-item>
    </el-form>
    <img
      @click="delect(formData, itemIndex)"
      style="width: 35px;height: 35px;cursor: pointer;position: absolute;top: -5px;right: -5px;"
      class="bar_item_del_icon"
      src="@/assets/images/del.png"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import StoreNavigationItemTs from "./StoreNavigationItemTs";
import UploadFile from "@/views/decoration/components/NavBar/UploadFile.vue";
import LinkSelect from "@/components/LinkSelect";
import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";
@Component({
  components: {
    UploadFile,
    LinkSelect,
  },
})
export default class StoreNavigationItemForm extends Vue {
  @Prop()
  formData!: StoreNavigationItemTs;

  @Prop()
  itemIndex = 0;

  link: LinkSelectItem = {
    id: "",
    type: "",
    name: "",
    url: "",
    append: "",
  };

  handleChange(file: { raw: any }) {
    this.formData.navIcon = URL.createObjectURL(file.raw);
  }

  @Watch("link", { deep: true })
  /** 点击链接选择 */
  selectLinkHandle(linkDataItem: LinkSelectItem) {
    /** 链接地址 */
    this.formData.linkUrl = linkDataItem.url;
    /** 链接名称 */
    this.formData.linkName = linkDataItem.name;
    /** 首页 链接append */
    this.formData.append = linkDataItem.append || "";
    /** 类型 type5为自定义页面 */
    this.formData.type = linkDataItem.type;
    this.formData.id = linkDataItem.id;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除店铺导航tab
   * @param {StoreNavigationItemTs} formData
   * @param {number} itemIndex
   */

  delect(formData: StoreNavigationItemTs, itemIndex: number) {
    this.$emit("onDelect", itemIndex);
  }

  mounted() {
    const formData = this.formData;
    this.link = {
      id: formData.id,
      type: formData.type,
      name: formData.linkName,
      url: formData.linkUrl,
      append: formData.append,
    };
  }
}
</script>
