<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:09:20
-->
<template>
  <!-- 标题栏 -->
  <el-form :model="formData" label-width="100px">
    <el-form-item label="选择样式">
      <el-radio-group v-model="formData.showStyle">
        <el-radio
          v-for="item in showStyles"
          :key="item.value"
          :label="item.value"
          >{{ item.label }}</el-radio
        >
      </el-radio-group>
    </el-form-item>
    <el-form-item label="标题名称">
      <el-input
        v-model="formData.titleName"
        :maxlength="10"
        placeholder="请输入标题名称"
      ></el-input>
    </el-form-item>
    <el-form-item label="背景颜色">
      <el-color-picker v-model="formData.backgroundColor"></el-color-picker>
    </el-form-item>
    <el-form-item label="文字颜色">
      <el-color-picker v-model="formData.color"></el-color-picker>
    </el-form-item>
    <el-form-item v-if="formData.showStyle === 'is-style-two'" label="跳转路径">
      <link-select :inner="true" :link="link"></link-select>
      <div v-if="link.type !== 6">
        <span style="color: #9797A1">{{ formData.linkName }}</span>
      </div>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import TitleBar from "./TitleBar";
import LinkSelect from "@/components/LinkSelect";
import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";
interface Sytles {
  label: string;
  value: string;
}

@Component({
  components: { LinkSelect },
})
export default class TitleBarForm extends Vue {
  @Prop()
  formData!: TitleBar;

  showStyles: Sytles[] = [
    {
      label: "样式一",
      value: "is-style-one",
    },
    {
      label: "样式二",
      value: "is-style-two",
    },
  ];

  link: LinkSelectItem = {
    id: "",
    type: "",
    name: "",
    url: "",
    append: "",
  };

  /**
   * 点击链接选择
   */
  @Watch("link", { deep: true })
  selectLinkHandle(linkDataItem: LinkSelectItem) {
    /** 链接地址 */
    this.formData.id = linkDataItem.id;
    this.formData.type = linkDataItem.type;
    this.formData.append = linkDataItem.append || "";
    this.formData.pathLink = linkDataItem.url;
    this.formData.linkName = linkDataItem.name;
  }

  mounted() {
    const formData = this.formData;
    this.link = {
      id: formData.id,
      type: formData.type,
      name: formData.linkName,
      url: formData.pathLink,
      append: formData.append,
    };
  }
}
</script>
