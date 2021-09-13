<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:32:19
-->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :before-close="beforeCloseHandle"
    width="450px"
    :append-to-body="true"
    :modal-append-to-body="true"
    title="编辑"
  >
    <el-form
      ref="form"
      :model="menuItem"
      :rules="formRules"
      label-width="120px"
      label-position="left"
    >
      <el-form-item label="菜单名称" prop="menuName">
        <el-input
          v-model="menuItem.menuName"
          placeholder="请输入菜单名称"
          maxlength="10"
          show-word-limit
        ></el-input>
      </el-form-item>
      <el-form-item label="菜单图标" prop="menuIconUrl">
        <div class="form-item__content">
          <upload-file
            :img-url.sync="menuItem.menuIconUrl"
            :defaultIcon="menuItem.defaultIcon"
          ></upload-file>
        </div>
      </el-form-item>
      <el-form-item label="跳转链接">
        <el-input
          v-model="menuItem.linkSelectItem.name"
          v-show="false"
        ></el-input>
        <link-select
          inner
          :show-clear="type === 2"
          :link="menuItem.linkSelectItem"
          @select="selectLinkHandle"
        ></link-select>
        <div>
          <span style="color: #9797A1">{{ linkInfo }}</span>
        </div>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog--footer">
      <el-button @click="closeHandle">取 消</el-button>
      <el-button type="primary" @click="confirmClickHandle">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Component, Emit, Prop, PropSync, Watch } from "vue-property-decorator";
import { mixins } from "vue-class-component";
import DialogMixin from "./formModel/DialogMixin";
import UploadFile from "@/views/decoration/components/UserCenter/UploadFile.vue";
import { MenuList, SubMenuList } from "./formModel/UserCenter";
import LinkSelect from "@/components/LinkSelect";
import LinkSelectItem, {
  typeNameMap,
} from "@/components/LinkSelect/src/components/LinkSelectItem";

@Component({
  components: {
    UploadFile,
    LinkSelect,
  },
})
export default class MenuDialog extends mixins(DialogMixin) {
  @PropSync("menuVisible", {
    type: Boolean,
    default: false,
  })
  Visible!: boolean;

  @Prop({
    type: Object,
    default() {
      return {};
    },
  })
  menuProp!: MenuList | SubMenuList;

  /**
   * 1 修改菜单
   * 2 新增菜单
   */
  @Prop({
    type: Number,
    default: 1,
  })
  type = 1;

  menuItem: MenuList | SubMenuList = {
    id: 0,
    menuName: "",
    menuIconUrl: "",
    defaultIcon: "",
    styleType: 0,
    sortIndex: 0,
    hideMenu: false,
    allowUse: 0,
    linkSelectItem: {
      id: 0,
      name: "",
      type: 0,
      url: "",
      append: "",
    },
    splitFlag: false,
    pid: "",
  };

  formRules = {
    menuName: [{ required: true, message: "请输入菜单名称", trigger: "blur" }],
    menuIconUrl: [{ required: true, message: "请选择图标", trigger: "change" }],
  };

  @Watch("Visible")
  handleVisibleChange(v: boolean) {
    this.dialogVisible = v;
    if (v) {
      this.menuItem = JSON.parse(JSON.stringify(this.menuProp));
      if (this.type === 2) {
        this.menuItem = {
          id: 0,
          menuName: "",
          menuIconUrl: "",
          defaultIcon: "",
          styleType: this.menuProp.styleType,
          sortIndex: 0,
          hideMenu: false,
          allowUse: 0,
          linkSelectItem: {
            id: 0,
            name: "",
            type: 0,
            url: "",
            append: "",
          },
          splitFlag: false,
          pid: 0,
        };
      }
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择链接
   * @param {LinkSelectItem} linkDataItem
   */

  selectLinkHandle(linkDataItem: LinkSelectItem) {
    this.menuItem.linkSelectItem = linkDataItem;
  }

  /**
   * 获取链接名称
   */
  get linkInfo() {
    if (typeof this.menuItem.linkSelectItem === "string") return;
    if (typeNameMap[this.menuItem.linkSelectItem.type]) {
      return (
        typeNameMap[this.menuItem.linkSelectItem.type].text +
        "-" +
        this.menuItem.linkSelectItem.name
      );
    } else {
      return "";
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 校验
   */

  confirmClickHandle() {
    if (!this.menuItem.menuName) {
      this.$message({
        type: "warning",
        message: "请填写菜单名称",
      });
      return;
    }
    if (!this.menuItem.menuIconUrl) {
      this.$message({
        type: "warning",
        message: "请选择图标",
      });
      return;
    }
    this.emitUpdateMenu();
    this.dialogVisible = false;
  }

  @Emit("updateMenu")
  emitUpdateMenu() {
    return this.menuItem;
  }
}
</script>

<style scoped lang="scss">
.form--item {
  display: flex;
  justify-content: flex-start;
  align-content: flex-end;
  padding: 10px;

  .form-item__label {
    width: 120px;
    text-align: left;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 14px;
    box-sizing: border-box;
  }
}

.el-upload__tip {
  color: #9797a1;
}
</style>
