<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 17:16:52
-->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :before-close="beforeCloseHandle"
    :title="this.version.id ? '编辑' : '新增'"
  >
    <el-form
      :model="queryForm"
      :rules="queryRules"
      class="queryForm"
      ref="queryFormRef"
      label-width="140px"
      label-position="left"
    >
      <el-form-item label="模板名称">
        {{ template.name }}
      </el-form-item>
      <el-form-item label="模板版本" prop="version">
        <el-input
          type="text"
          v-model="queryForm.version"
          placeholder="请填写模板版本"
        />
      </el-form-item>
      <el-form-item label="微信小程序" prop="miniCodeVersions">
        <div
          class="pcUrlMapItem"
          v-for="(item, index) in queryForm.miniCodeVersions"
          :key="index"
        >
          <el-select
            @change="selectMiniHandle(item)"
            v-model="item.codeTempleteId"
            placeholder="请选择微信小程序模板"
          >
            <el-option
              v-for="template in templatelist"
              :key="template.templateId"
              :label="template.userVersion"
              :value="template.templateId"
            />
          </el-select>
          <el-input
            placeholder="请输入版本说明"
            v-model="item.versionExplain"
            class="pcUrlMapItem__input"
          >
          </el-input>
          <span>
            <i class="el-icon-circle-plus" @click="addMiniVersion(index)"></i>
            <i class="el-icon-remove" @click="removeMiniVersion(index)"></i>
          </span>
        </div>
      </el-form-item>
      <el-form-item label="移动web" prop="mobileTerminalUrl">
        <el-input
          placeholder="请输入地址"
          v-model="queryForm.mobileTerminalUrl"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="PC端后台" prop="pcTerminaUrl">
        <el-input placeholder="请输入地址" v-model="queryForm.pcTerminaUrl">
        </el-input>
      </el-form-item>
      <el-form-item
        :label="`pc端关联键值${index + 1}`"
        v-for="(item, index) in pcUrlMap"
        :key="index"
      >
        <div class="pcUrlMapItem">
          <el-input
            placeholder="请输入key"
            v-model="item.key"
            class="pcUrlMapItem__input"
          >
          </el-input>
          <el-input
            placeholder="请输入value"
            v-model="item.value"
            class="pcUrlMapItem__input"
          >
          </el-input>
          <span>
            <i class="el-icon-circle-plus" @click="addMap(index)"></i>
            <i class="el-icon-remove" @click="removeMap(index)"></i>
          </span>
        </div>
      </el-form-item>
      <el-form-item label="更新日志" prop="versionLog">
        <el-input
          type="textarea"
          :autosize="{ minRows: 4 }"
          placeholder="请填写更新内容"
          v-model="queryForm.versionLog"
        >
        </el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog--footer">
      <el-button @click="closeHandle">取 消</el-button>
      <el-button type="primary" @click="comfirmClickHandle">保存</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import {
  Component,
  Emit,
  Prop,
  PropSync,
  Ref,
  Vue,
  Watch,
} from "vue-property-decorator";
import {
  createTemplateVersion,
  getTemplateVersionList,
  updateTemplateVersion,
} from "@/api/businessCenter/setting";
import { ITemplateVersion } from "../../Interface/TemplateVersion";
import { ITemplate } from "../../Interface/Template";
import { TemplateList } from "./TemplateCollapse.vue";
import { ElForm } from "element-ui/types/form";

@Component
export default class VersionAddOrUpdate extends Vue {
  @Ref() readonly queryFormRef?: ElForm;

  @PropSync("visible", {
    type: Boolean,
    default: false,
  })
  dialogVisible!: boolean;

  @Prop({
    type: Object,
    default: () => {
      return {
        codeTempleteId: "",

        codeTempleteVersion: "",

        // id,更新接口时使用
        id: null,

        // 业务基础库id
        librariesInfoId: null,

        // 业务基础库名称
        librariesInfoName: "",

        // 移动端路径
        mobileTerminalUrl: "",

        // 移动端版本
        mobileTerminalVersion: "",

        // pc端路径
        pcTerminaUrl: "",

        // pc端版本
        pcTerminaVersion: "",

        // 店铺模版id
        shopTemplateId: null,

        // pc端关联页面,json存储,键值对
        pcUrlMap: "",

        serverCount: null,

        // 版本号
        version: "",

        // 更新日志
        versionLog: "",

        miniCodeVersions: [],
      };
    },
  })
  version!: ITemplateVersion;

  @Prop({
    type: Object,
    default: () => {
      return {};
    },
  })
  template!: ITemplate;

  queryForm = {
    miniCodeVersions: [
      {
        codeTempleteId: "",
        codeTempleteVersion: "",
        id: 0,
        versionExplain: "",
      },
    ],
    // id,更新接口时使用
    id: null as number | null,
    // 业务基础库名称
    librariesInfoName: "",
    // 移动端路径
    mobileTerminalUrl: "",
    // 移动端版本
    mobileTerminalVersion: "",
    // pc端路径
    pcTerminaUrl: "",
    // pc端版本
    pcTerminaVersion: "",
    // pc端关联页面,json存储,键值对
    pcUrlMap: "",
    // 店铺模版id
    shopTemplateId: null as number | null,
    // 版本号
    version: "",
    // 更新日志
    versionLog: "",
  };

  pcUrlMap = [
    {
      key: "",
      value: "",
    },
  ];

  /** 小程序模板列表列表 */
  templatelist: Array<TemplateList> = [];

  queryRules = {
    version: [
      {
        required: true,
        message: "请填写模板版本",
        trigger: "blur",
      },
    ],
    codeTempleteId: [
      {
        required: true,
        message: "请选择微信小程序模板",
        trigger: "blur",
      },
    ],
    mobileTerminalUrl: [
      {
        required: true,
        message: "请输入地址",
        trigger: "blur",
      },
    ],
    pcTerminaUrl: [
      {
        required: true,
        message: "请输入地址",
        trigger: "blur",
      },
    ],
    pcUrlMap: [
      {
        required: true,
        message: "请输入pc端关联页面",
        trigger: "blur",
      },
    ],
  };

  @Watch("dialogVisible")
  handleVisibleChange(v: boolean) {
    this.pcUrlMap = [
      {
        key: "",
        value: "",
      },
    ];
    if (v) {
      const miniCode = [
        {
          codeTempleteId: "",
          codeTempleteVersion: "",
          id: 0,
          versionExplain: "",
        },
      ];
      this.queryForm = {
        miniCodeVersions: [
          {
            codeTempleteId: "",
            codeTempleteVersion: "",
            id: 0,
            versionExplain: "",
          },
        ],
        id: null,
        librariesInfoName: "",
        mobileTerminalUrl: "",
        mobileTerminalVersion: "",
        pcTerminaUrl: "",
        pcTerminaVersion: "",
        // pc端关联页面,json存储,键值对
        pcUrlMap: "",
        shopTemplateId: this.template.id,
        version: "",
        versionLog: "",
      };
      this.getTemplateList();
      if (this.version.id) {
        this.queryForm.id = this.version.id;
        this.queryForm.version = this.version.version;
        this.queryForm.miniCodeVersions =
          this.version.miniCodeVersions || miniCode;
        this.queryForm.mobileTerminalUrl = this.version.mobileTerminalUrl;
        this.queryForm.mobileTerminalVersion = this.version.mobileTerminalVersion;
        this.queryForm.pcTerminaUrl = this.version.pcTerminaUrl;
        this.queryForm.pcTerminaVersion = this.version.pcTerminaVersion;
        this.queryForm.versionLog = this.version.versionLog;
        this.queryForm.shopTemplateId = this.template.id;
        if (this.version.pcUrlMap) {
          const obj = JSON.parse(this.version.pcUrlMap);
          this.pcUrlMap = this.objecToArray(obj);
        }
      }
    }
  }

  // 获取小程序版本id时，选中自定义版本号
  selectMiniHandle(item: {
    codeTempleteId: string;
    codeTempleteVersion: string;
  }) {
    const template = this.templatelist.find(i => {
      return String(i.templateId) === item.codeTempleteId;
    });
    if (template) {
      item.codeTempleteVersion = template.userVersion;
    }
  }

  addMiniVersion(index: number) {
    const mapItem = {
      codeTempleteId: "",
      codeTempleteVersion: "",
      id: 0,
      versionExplain: "",
    };
    this.queryForm.miniCodeVersions.splice(index, 0, mapItem);
  }

  removeMiniVersion(index: number) {
    if (this.queryForm.miniCodeVersions.length > 1) {
      this.queryForm.miniCodeVersions.splice(index, 1);
    } else {
      this.$message({
        message: "删除失败",
        type: "warning",
      });
    }
  }

  addMap(index: number) {
    const mapItem = {
      key: "",
      value: "",
    };
    this.pcUrlMap.splice(index, 0, mapItem);
  }

  removeMap(index: number) {
    if (this.pcUrlMap.length > 1) {
      this.pcUrlMap.splice(index, 1);
    } else {
      this.$message({
        message: "删除失败",
        type: "warning",
      });
    }
  }

  objecToArray(object: { key: string; value: string; [x: string]: any }) {
    const result = [];
    for (const key in object) {
      if (object.hasOwnProperty(key)) {
        const obj = {
          key: "",
          value: "",
        };
        obj.key = key;
        obj.value = object[key];
        result.push(obj);
      }
    }
    return result;
  }

  arrayToObject(array: Array<{ key: string; value: string }>) {
    const result: { [x: string]: any } = {};
    array.forEach(item => {
      result[item.key] = item.value;
    });
    return result;
  }

  /**
   * 关闭弹窗
   */
  async closeHandle() {
    try {
      const result = await this.confirmHandle();
      if (result === "confirm") {
        this.dialogVisible = false;
      }
    } catch (e) {
      console.log(e);
    }
  }

  async beforeCloseHandle(done: () => void) {
    try {
      const result = await this.confirmHandle();
      if (result === "confirm") {
        done();
      }
    } catch (e) {
      console.log(e);
    }
  }

  /**
   * 关闭确认
   */
  async confirmHandle() {
    return await this.$confirm(
      "确定退出该窗口？退出后，数据将不会保留？",
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      },
    );
  }

  /**
   * 获取小程序模板列表
   */
  getTemplateList() {
    getTemplateVersionList().then(res => {
      this.templatelist = res.data;
    });
  }

  @Emit("refreshDataList")
  async comfirmClickHandle() {
    try {
      const obj = this.arrayToObject(this.pcUrlMap);
      this.queryForm.pcUrlMap = JSON.stringify(obj);
      await (this.queryFormRef as ElForm).validate();
      let response;
      if (this.version.id) {
        response = await updateTemplateVersion(this.queryForm);
      } else {
        response = await createTemplateVersion(this.queryForm);
      }
      const { code } = response;
      if (code === 200) {
        this.dialogVisible = false;
        this.$message({
          type: "success",
          message: `${this.version.id ? "更新" : "新增"}模板版本成功`,
        });
      }
    } catch (e) {
      console.log(e);
    }
  }
}
</script>

<style lang="scss" scoped>
.pcUrlMapItem {
  display: flex;

  .pcUrlMapItem__input {
    width: 40%;
    margin-right: 10px;
  }
}
</style>
