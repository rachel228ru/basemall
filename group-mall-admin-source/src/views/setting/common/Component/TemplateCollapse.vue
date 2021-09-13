<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:34:08
-->
<template>
  <div class="t-container">
    <slot name="header" />
    <div class="t-content">
      <el-table :data="dataList" style="width: 100%; margin-top: 15px">
        <el-table-column
          prop="version"
          header-align="center"
          align="center"
          label="版本号"
        >
        </el-table-column>
        <el-table-column header-align="center" align="center" label="终端类型">
          <template> 微信小程序 </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          label="操作"
        >
          <template slot-scope="scope">
            <el-button type="text" @click="updateVersion(scope.row)"
              >编辑</el-button
            >
            <el-button type="text" @click="deleteVersion(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Emit } from "vue-property-decorator";
import { ITemplateVersion } from "../../Interface/TemplateVersion";
import {
  deleteTpVersion,
  getTemplateVersionList,
} from "@/api/businessCenter/setting";

export interface TemplateList {
  createTime: string;
  templateId: number;
  userDesc: string;
  userVersion: string;
}
@Component
export default class TemplateCollapse extends Vue {
  /** 模板版本 */
  @Prop({
    type: Array,
    default: () => {
      return [];
    },
  })
  dataList!: ITemplateVersion[];

  /** 小程序模板列表列表 */
  templatelist: Array<TemplateList> = [];

  get template() {
    return (codeTempleteId: number) => {
      const template = this.templatelist.find(
        item => item.templateId === codeTempleteId,
      );
      if (template) {
        return template.userDesc + "/" + template.userVersion;
      } else {
        return "";
      }
    };
  }

  created() {
    this.getTemplateList();
  }

  // 删除模板版本
  @Emit("refreshDataList")
  async deleteVersion(version: any) {
    try {
      if (version.serverCount > 0) {
        await deleteTpVersion(version.id).catch(err => {
          this.$message.error(err);
        });
      }
      if (!version.serverCount) {
        await this.$confirm(
          "删除后版本数据将清空，使用该版本店铺将无法正常使用，确定删除吗？",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          },
        );
        deleteTpVersion(version.id)
          .then(() => {
            this.$message({
              type: "success",
              message: `删除成功`,
            });
          })
          .catch(err => {
            this.$message.error(err);
          });
      }
    } catch (e) {
      console.log(e);
    }
  }

  /**
   * 获取小程序模板列表
   */
  getTemplateList() {
    getTemplateVersionList().then(res => {
      this.templatelist = res.data;
    });
  }

  // 进入该版本下店铺列表
  goShops(id: string, num: number) {
    if (num) {
      this.$router.push({
        path: "/shops",
        query: {
          versionId: id,
        },
      });
    } else {
      this.$message({
        type: "warning",
        message: "该版本下没有店铺",
      });
    }
  }

  @Emit("updateVersion")
  updateVersion(row: any) {
    return row;
  }
}
</script>

<style lang="scss" scoped>
.t-container {
  padding: 15px 15px 0;

  .t-active {
    text-align: center;
  }
}
</style>
