<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-24 11:40:54
-->
<template>
  <!-- 设置编辑页面 -->
  <div class="editor__form_wrap">
    <div slot="header" class="clearfix editor__form_header">
      <span
        >{{
          currentComponent && currentComponent.label
            ? `${currentComponent.label}`
            : ""
        }}组件设置</span
      >
    </div>
    <div class="editor__form_wrap_main">
      <el-scrollbar style="height: 100%">
        <div class="pd20">
          <transition name="el-fade-in-linear" mode="out-in">
            <component
              v-if="currentComponent && activePageType !== 'bussiness'"
              :is="currentComponent.value"
              :formData="currentComponent.formData"
              @change="changeData"
            ></component>
            <div v-else class="form-tips">
              <div class="form-tips-logo">
                <div class="el-icon-setting"></div>
                <p>
                  {{
                    activePageType == "bussiness"
                      ? "当前组件,无法进行设置"
                      : "当前未设置组件,请选择组件进行设置"
                  }}
                </p>
              </div>
            </div>
          </transition>
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { IComponent } from "./interfaces/component";
import { Vue, Component, Prop } from "vue-property-decorator";
import setting from "./compontents/index/setting";

@Component({
  components: {
    ...setting,
  },
})
export default class EditorForm extends Vue {
  @Prop({
    default() {
      return null;
    },
  })
  currentComponent?: IComponent | null;

  /**
   * 获取当前页面操作栏目
   */
  get activePageType() {
    return this.$STORE.decorationStore.activePageType;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击操作
   * @param {*} data
   */
  changeData(data: any) {
    this.$emit("change", data);
  }
}
</script>
<style lang="scss" scoped>
@import "@/assets/styles/decoration/editorPage";
.editor__form_wrap {
  width: 435px;
  height: calc(100vh - 80px) !important;
  padding-bottom: 0 !important;
  border: 1px solid #dcdfe6;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.12), 0 0 6px 0 rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: column;
  .editor__form_wrap_main {
    width: 100%;
    flex: 1;
    overflow: hidden;
    position: relative;
  }
  .editor__form_header {
    padding: 18px 20px;
    border-bottom: 1px solid #dcdfe6;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
  }
  .pd20 {
    padding: 20px;
    padding-bottom: 0;
  }
  .form-tips {
    .form-tips-logo {
      width: 435px;
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
      text-align: center;
      color: #b2b2b2;
      .el-icon-setting {
        font-size: 100px;
        margin-bottom: 20px;
      }
    }
  }
}
</style>
