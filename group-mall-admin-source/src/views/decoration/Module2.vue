<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:33:08
-->
<template>
  <div class="decoration__page2">
    <!-- 自定义模板 -->
    <div class="decoration__page-1">
      <div v-for="(item, index) in templateList" :key="item.id">
        <div class="usercenter" v-if="index == 0">
          <div class="m_title">经典商城</div>
          <div class="user">
            <div class="p_template_wrap">
              <div class="p_template">
                <img src="@/assets/images/decoration/moudle.png" width="100%" />
              </div>
            </div>
            <!-- 蒙层 -->
            <div class="fullbg">
              <el-button
                style="marginLeft:0px;marginTop:20px;padding:10px 30px"
                type="primary"
                @click="toggleEditor(item)"
                >装修</el-button
              >
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from "vue-property-decorator";
import {
  addTemplate,
  getdefTemplate,
  deleteTemplate,
} from "@/api/decoration/decoration";
import UploadFile from "@/views/decoration/components/NavBar/UploadFile.vue";
import Editor from "@/views/decoration/components/EditorPage";
@Component({
  components: {
    UploadFile,
    Editor,
  },
})
export default class Page extends Vue {
  /** 模板列表 */
  templateList: any[] = [];

  /** 显示编辑页面 */
  editorVisible = false;

  mounted() {
    this.init();
  }

  /** 初始化切换tab可调 */
  init() {
    this.getTemplateList();
  }

  /** 监听弹窗，弹窗时调用获取模板 */
  @Watch("editorVisible", { immediate: true })
  editorVisibleChange() {
    if (this.editorVisible) {
      this.getTemplateList();
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击使用模板
   * @param {*} item
   */

  async emptyUse(item: { onlineStatus: number }) {
    item.onlineStatus = 1;
    const res = await addTemplate(item);
    try {
      if (res.code === 200) {
        this.$message.success(`使用模板成功`);
        this.getTemplateList();
      } else {
        this.$message.warning(`使用模板失败`);
      }
    } catch (err) {
      this.$message.warning(err || "使用模板失败");
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 装修模板
   * @param {*} item
   */

  toggleEditor(item: { id: string | number }) {
    this.$router.push({ path: "/editorPage" });
    this.$STORE.decorationStore.setFirstStatus(false);
    this.$STORE.decorationStore.updateTemplateId(item.id);
    this.$emit("hideDialog");
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除模板
   * @param {*} item
   */

  async deleteTemplate(item: { id: any }) {
    const data = {
      ids: item.id,
    };
    const res = await deleteTemplate(data);
    try {
      if (res.code === 200) {
        this.$message.success(`删除成功`);
        this.getTemplateList();
      } else {
        this.$message.warning(`删除失败`);
      }
    } catch (err) {
      this.$message.warning(err || "删除模板失败");
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 添加默认模板
   */

  async addTemplate() {
    const addData = {
      isDevTemplate: 1,
      type: 1,
      onlineStatus: 0,
      name: "商超模板",
    };
    const res = await addTemplate(addData);
    try {
      if (res.code === 200) {
        this.$message.success(`新增模板成功`);
        this.getTemplateList();
      } else {
        this.$message.warning(`添加模板失败`);
      }
    } catch (err) {
      this.$message.warning(err || "操作失败");
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取默认模板列表
   */

  async getTemplateList() {
    const getPageListData = {
      isDevTemplate: 1, // 0自定义模板 1默认模板
    };
    const res = await getdefTemplate(getPageListData);
    try {
      if (res.code === 200) {
        this.templateList = res.data;
      } else {
        this.$message.warning(`获取数据失败`);
      }
    } catch (err) {
      this.$message.warning(err || "操作失败");
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取当前时间
   */

  get now() {
    const date = new Date();
    const hour = date.getHours() > 9 ? date.getHours() : "0" + date.getHours();
    const minutes =
      date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes();
    const now = `${hour}:${minutes}`;
    return now;
  }
}
</script>

<style lang="scss" scoped>
@import "../../assets/styles/decoration/module";
.usercenter {
  width: 200px;
}
</style>
