<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 16:06:34
-->
<template>
  <!-- 底部导航 -->
  <div class="bar_item navBar__setting">
    <div class="bar_item_top">
      <span v-if="formData.isHome">
        <img class="bar_home_icon" src="@/assets/images/decoration/u325.png" />
        <span style="color:#7F7F7F;fontSize:16px;marginLeft:5px;">首页</span>
      </span>
    </div>
    <el-form-item label="名称" prop="name">
      <el-input v-model="formData.text" :maxlength="3"></el-input>
    </el-form-item>
    <el-form-item label="链接" prop="link" v-if="itemIndex != 0">
      <link-select
        getFrom="bottomNav"
        :inner="true"
        :link="link"
        :noProTab="true"
        :limitProTab="false"
      ></link-select>
      <div>
        <span style="color: #9797A1" v-if="link.type != 6">{{
          formData.linkName
        }}</span>
      </div>
    </el-form-item>
    <el-form-item label="图标" prop="icon">
      <el-radio-group
        v-model="formData.iconType"
        @change="changeIcon(itemIndex, formData)"
      >
        <el-radio :label="1" v-if="formData.isAdd">系统图标</el-radio>
        <el-radio :label="2">自定义图标</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="图片" prop="pic" v-if="formData.iconType === 2">
      <div style="display:flex">
        <div>
          <upload-file :img-url.sync="formData.iconPath"></upload-file>
          <span style="color: #9797A1;marginLeft: 12px;">未选中</span>
        </div>
        <div style="margin-left:10px">
          <upload-file :img-url.sync="formData.selectedIconPath"></upload-file>
          <span style="color: #9797A1;marginLeft: 17px;">选中</span>
        </div>
      </div>
      <div>
        <span style="color: #9797A1">建议尺寸为50*50，支持png格式</span>
      </div>
    </el-form-item>
    <el-form-item label="图片" prop="pic" v-if="formData.iconType === 1">
      <div style="display:flex">
        <div>
          <img v-if="formData.defIcon" :src="formData.defIcon" class="avatar" />
          <span style="color: #9797A1;marginLeft: 12px;">未选中</span>
        </div>
        <div style="margin-left:10px">
          <img v-if="formData.actIcon" :src="formData.actIcon" class="avatar" />
          <span style="color: #9797A1;marginLeft: 17px;">选中</span>
        </div>
      </div>
      <div>
        <span style="color: #9797A1">建议尺寸为50*50，支持png格式</span>
      </div>
    </el-form-item>

    <img
      @click="delect(formData, itemIndex)"
      class="bar_item_del_icon"
      src="@/assets/images/del.png"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import NavBarItem from "./NavBarItem";
import LinkSelect from "@/components/LinkSelect";
import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";
import UploadFile from "@/views/decoration/components/NavBar/UploadFile.vue";
@Component({
  components: {
    LinkSelect,
    UploadFile,
  },
})
export default class NavBarItemForm extends Vue {
  @Prop()
  formData!: NavBarItem;

  @Prop()
  itemIndex!: number;

  link: LinkSelectItem = {
    id: "",
    type: "",
    name: "",
    url: "",
    append: "",
  };

  @Watch("link", { deep: true })
  /** 点击链接选择 */
  selectLinkHandle(linkDataItem: LinkSelectItem) {
    /** 链接地址 */
    this.formData.linkUrl = linkDataItem.url;
    /** 链接名称 */
    this.formData.linkName = linkDataItem.name;
    this.formData.name = linkDataItem.append || "";
    this.formData.type = linkDataItem.type;
    this.formData.id = linkDataItem.id;
    if (!this.formData.isAdd) {
      this.formData.iconType = 2;
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择图标
   * @param {number} index
   * @param {NavBarItem} formData
   */
  changeIcon(index: number, formData: NavBarItem) {
    if (formData.iconType === 1) {
      this.$emit("setDefIcon", index);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除底部导航tab
   * @param {NavBarItem} formData
   * @param {number} itemIndex
   */

  delect(formData: NavBarItem, itemIndex: number) {
    if (formData.isHome) {
      this.$message.error("该导航已设为首页,不允许删除");
    } else {
      this.$emit("onDelect", itemIndex);
    }
  }

  mounted() {
    const formData = this.formData;
    if (formData.iconType === 1) {
      this.changeIcon(this.itemIndex, formData);
    }

    this.link = {
      id: formData.id,
      type: formData.type,
      name: formData.linkName,
      url: formData.linkUrl,
      append: formData.name,
    };
  }
}
</script>
<style lang="scss" scoped>
@import "@/assets/styles/decoration/navBar";
.navBar__setting {
  .avatar {
    height: 50px !important;
    width: 50px !important;
  }
}
</style>
