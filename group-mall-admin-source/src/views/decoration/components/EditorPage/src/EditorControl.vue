<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-24 10:17:15
-->
<template>
  <!-- 控件管理页面 -->
  <div class="editor_control_wrap">
    <div class="editor_control_wrap_main">
      <el-scrollbar style="height: 100%">
        <div
          class="checklist_item"
          v-for="(item, index) of controlList"
          :key="item.value"
        >
          <div>
            {{
              JSON.parse(item.pluginProperties) &&
              JSON.parse(item.pluginProperties).length > 0
                ? item.pluginNameCn
                : "未命名"
            }}
            <!-- JSON.parse(item.pluginProperties)[0].label -->
          </div>
          <img
            class="edit_icon"
            @click="
              saveControlId(item, index);
              handleAddComponent(item);
            "
            src="@/assets/images/moduleIcon/u1141.png"
          />
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Emit, Watch } from "vue-property-decorator";
import { getControl } from "@/api/decoration/decoration";
import { IControlItem } from "./interfaces/EditorControlPage";

@Component
export default class DecorationComponent extends Vue {
  /** 选择列表 */
  controlList: IControlItem[] = [];

  @Watch("currentTemplateId")
  handleTemplateIdChange() {
    this.getControlList();
  }

  @Emit("change")
  handleAddComponent(item: IControlItem) {
    const obj = {
      icon: JSON.parse(item.pluginProperties)[0].icon,
      value: JSON.parse(item.pluginProperties)[0].value,
      // label: JSON.parse(item.pluginProperties)[0].label,
      label: item.pluginNameCn,
    };
    return {
      ...obj,
      id: Date.now(),
      formData: JSON.parse(item.pluginProperties)[0].formData,
    };
  }

  @Emit("saveControlId")
  saveControlId(item: IControlItem, index: number) {
    return {
      item,
      index,
    };
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 初始获取控件数据
   */

  created() {
    this.getControlList();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 初始获取控件数据
   */

  async getControlList() {
    const res = await getControl({
      templateId: this.$STORE.decorationStore.templateId,
    });
    try {
      if (res.code === 200) {
        this.controlList = res.data;
        if (!res.data.length) {
          this.$message.warning(`获取数据为空`);
        }
      } else {
        this.$message.warning(`获取数据失败，请稍后重试！`);
      }
    } catch (error) {
      this.$message.warning(`${error}获取数据失败，请稍后重试！`);
    }
  }
}
</script>
<style lang="scss" scoped>
@import "@/assets/styles/decoration/editorControl";
@import "@/assets/styles/decoration/editorPage";
.editor_control_wrap {
  width: 300px;
}
</style>
