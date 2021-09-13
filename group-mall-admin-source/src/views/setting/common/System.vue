<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 15:13:51
-->
<template>
  <div class="system__template">
    <el-button
      type="primary"
      @click="addOrUpdateVersion(template)"
      style="align-self:flex-start"
      >新增模板</el-button
    >
    <div class="system__template--content">
      <TemplateCollapse
        v-for="template in dataList"
        @refreshDataList="getDataList"
        :key="template.id"
        :dataList="template.shopTemplateVos"
        @updateVersion="addOrUpdateVersion(template)"
      >
      </TemplateCollapse>
    </div>
    <VersionAddOrUpdate
      :visible.sync="versionVisible"
      :version="version"
      :template="template"
      @refreshDataList="getDataList"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import TemplateCollapse from "./Component/TemplateCollapse.vue";
import VersionAddOrUpdate from "./Component/VersionAddOrUpdate.vue";
import { getTemplateList } from "@/api/businessCenter/setting";
import { Template, ITemplate } from "../Interface/Template";
import { TemplateVersion } from "../Interface/TemplateVersion";
@Component({
  components: {
    TemplateCollapse,
    VersionAddOrUpdate,
  },
})
export default class System extends Vue {
  /** 模板类型 */
  @Prop({
    type: Number,
    default: 0,
  })
  templateType!: number;

  dataList: ITemplate[] = [];

  template: ITemplate = new Template();

  version = new TemplateVersion();

  pageIndex = 1;

  pageSize = 10;

  totalPage = 0;

  // 模板版本弹窗
  versionVisible = false;

  created() {
    this.getDataList();
  }

  /**
   * 获取模板列表
   */
  async getDataList() {
    const response = await getTemplateList({
      type: this.templateType,
      page: this.pageIndex,
      size: this.pageSize,
    });
    this.dataList = response.data.list;
    this.totalPage = response.data.total;
  }

  addOrUpdateVersion(template: any, version?: TemplateVersion) {
    this.version = version ? version : new TemplateVersion();
    this.template = template;
    this.versionVisible = true;
  }
}
</script>

<style lang="scss" scoped>
.system__template {
  display: flex;
  align-items: center;
  flex-direction: column;

  .system__template--content {
    width: 100%;
    flex: 1 0 500px;
  }

  .system__template--footer {
    width: 100%;
    margin-top: 30px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.t-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 40px;
  border: 1px solid rgba(228, 228, 228, 1);
  background-color: rgba(239, 248, 255, 1);

  .name {
    font-size: 15px;
    font-weight: bolder;
    padding-right: 30px;
  }

  .date {
    font-size: 14px;
    color: #999999;
  }
}
</style>
