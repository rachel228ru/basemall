<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:27:22
-->
<template>
  <div class="auth">
    <table class="auth__table">
      <tr>
        <td>小程序名称</td>
        <td>{{ miniInfo.miniName }}</td>
      </tr>
      <tr>
        <td>小程序头像</td>
        <td>
          <el-avatar :src="miniInfo.logo" />
        </td>
      </tr>
      <tr>
        <td>小程序码</td>
        <td>
          <el-image
            style="width: 100px; height: 100px;margin-right: 20px;"
            :src="miniInfo.qrcode"
            fit="cover"
          />
          <a :href="miniInfo.qrcode" download>下载小程序码</a>
        </td>
      </tr>
      <tr>
        <td>小程序介绍</td>
        <td>{{ miniInfo.signature }}</td>
      </tr>
      <tr>
        <td>主体信息</td>
        <td>{{ miniInfo.principalName }}</td>
      </tr>
      <tr>
        <td>服务类目</td>
        <td>{{ miniInfo.serviceClass }}</td>
      </tr>
      <tr>
        <td>更新授权</td>
        <td>
          <el-button @click="updataBaseInfo" type="primary"
            >同步基本信息
          </el-button>
        </td>
      </tr>
      <tr>
        <td>授权管理状态</td>
        <td v-if="miniInfo.authorizerFlag">
          <span>已授权</span>
        </td>
        <td v-if="!miniInfo.authorizerFlag">
          <span>未授权</span>
          <el-button type="primary" @click="authPublic">重新授权</el-button>
        </td>
      </tr>
      <tr>
        <td>小程序状态</td>
        <td v-if="miniInfo.runFlag">
          <span>已上线</span>
        </td>
        <td v-if="!miniInfo.runFlag">
          <span>未上线</span>
        </td>
      </tr>
      <tr>
        <td>当前版本</td>
        <td v-if="miniInfo.currentVersionNumName">
          {{ miniInfo.currentVersionNumName }}
          <span class="info"
            >(发布时间： {{ miniInfo.currentVersionSendTime }}）
          </span>
        </td>
        <td v-else>无</td>
      </tr>
      <tr>
        <td>审核中版本</td>
        <td>
          <span class="error" v-if="miniInfo.auditStatus === 2">{{
            miniInfo.auditingVersionNumName
              ? miniInfo.auditingVersionNumName
              : "无"
          }}</span>
          <span class="error" v-if="miniInfo.auditStatus === null">无</span>
          <span class="error" v-if="miniInfo.auditStatus === 1"
            >审核失败
            <el-popover placement="top-start" trigger="hover">
              你的小程序 {{ miniInfo.auditingVersionNumName }},提审时间{{
                miniInfo.auditingVersionSummitTime
              }},代码发布审核未通过，原因如下：<br />
              <div v-html="miniInfo.auditingComeToNothingReason"></div>
              <i slot="reference" class="el-icon-question"></i>
            </el-popover>
          </span>
          <span v-if="miniInfo.auditStatus === 1" class="info"
            >(审核完成时间：{{ miniInfo.auditingVersionEndTime }}）</span
          >
          <span v-if="miniInfo.auditStatus === 2" class="info"
            >(提交审核时间：{{ miniInfo.auditingVersionSummitTime }}）</span
          >
          <el-button
            type="primary"
            :loading="authBtnLoading"
            v-if="miniInfo.auditStatus === 1"
            @click="updateVersion('authBtnLoading')"
            >重新提交审核
          </el-button>
          <el-button
            type="primary"
            v-if="miniInfo.auditStatus === 2"
            @click="revocationVersion"
            >撤销审核
          </el-button>
        </td>
      </tr>
      <tr>
        <td>版本更新</td>
        <td
          v-if="
            miniInfo.currentVersionNumName === miniInfo.versionUpdateNumName ||
              miniInfo.versionUpdateNumName === null
          "
        >
          当前为最新版本
        </td>
        <td v-else>
          <span
            class="error"
            v-if="
              miniInfo.versionUpdateNumName &&
                miniInfo.currentVersionNumName !== miniInfo.versionUpdateNumName
            "
            >有新版本{{ miniInfo.versionUpdateNumName }}</span
          >
          <span
            class="error"
            v-if="miniInfo.auditStatus === 2 && miniInfo.versionUpdateNumName"
          >
            <el-popover placement="top-start" trigger="hover">
              当前有审核中的版本，请撤销后重新提交审核
              <i slot="reference" class="el-icon-question"></i>
            </el-popover> </span
          ><span v-if="miniInfo.versionUpdateTime"
            >(更新时间：{{ miniInfo.versionUpdateTime }}）</span
          >
          <el-select
            v-model="codeVersion"
            value-key="id"
            placeholder="请选择要更新的版本"
          >
            <el-option
              v-for="item in miniInfo.codeVersionVos"
              :key="item.id"
              :label="item.codeTempleteVersion + item.versionExplain"
              :value="item"
            ></el-option>
          </el-select>
          <el-button
            type="primary"
            :loading="updateBtnLoading"
            v-if="
              miniInfo.auditStatus !== 2 &&
                miniInfo.versionUpdateNumName &&
                miniInfo.currentVersionNumName !== miniInfo.versionUpdateNumName
            "
            @click="updateVersion('updateBtnLoading')"
            >更新
          </el-button>
        </td>
      </tr>
    </table>

    <hr />
    <table class="auth__table">
      <tr>
        <td>添加体验者</td>
        <td>
          <el-input
            class="develop--input"
            v-model="newWechatId"
            suffix-icon="el-icon-search"
          ></el-input>
          <el-button type="primary" @click="addMemberHandle"
            >确认添加
          </el-button>
        </td>
        <td @click="generateQrcode" class="cursor-pointer">
          <i class="el-icon-s-grid"></i>
          <span>点击生成小程序体验码</span>
        </td>
      </tr>
      <tr>
        <td></td>
        <td>
          <div>
            <div>成员管理</div>
            <div>
              <div class="member" v-for="member in memberList" :key="member">
                {{ member }}
                <i
                  class="el-icon-error delete"
                  @click="setMemberHandle(member, 2)"
                />
              </div>
            </div>
          </div>
        </td>
        <td>
          <img class="qrcode" :src="qrcode" />
        </td>
      </tr>
    </table>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Emit } from "vue-property-decorator";
import {
  getMemberAuthList,
  setMiniExperience,
} from "@/api/businessCenter/setting";
import {
  revocationMiniVersion,
  updateMiniVersion,
  updateMiniBaseInfo,
  getQrcode,
  authPreauthcode,
} from "@/api/channel/channel";
import storage from "@/libs/storage";
import { MpSettingType } from "../channelType";

@Component
export default class AppAuth extends Vue {
  @Prop({
    default: () => {
      return {};
    },
  })
  miniInfo!: MpSettingType;

  qrcode = "";

  memberList: string[] = [];

  newWechatId = "";

  codeVersion = {
    codeTempleteVersion: "",
    id: 0,
    versionExplain: "",
  };

  updateBtnLoading = false;

  authBtnLoading = false;

  created() {
    this.getDevelopMemberList();
  }

  async getDevelopMemberList() {
    try {
      const res = await getMemberAuthList();
      if (res && res.code === 200) {
        this.memberList = res.data;
      } else {
        this.$message({
          type: "warning",
          message: "获取开发设置失败！",
        });
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  /**
   * 解除绑定或绑定体验者
   * @param option 操作类型 1-绑定 2-解除绑定
   * @param wechatid 微信号
   */
  async setMemberHandle(wechatid: number | string, option: number) {
    try {
      const form = {
        wechatid,
        option,
      };
      const response = await setMiniExperience(form);
      const { code } = response;
      if (code === 200) {
        this.$message({
          type: "success",
          message: `${option === 1 ? "绑定" : "解除绑定"}体验者成功！`,
        });
      } else {
        this.$message({
          type: "warning",
          message: `${option === 1 ? "绑定" : "解除绑定"}体验者失败！`,
        });
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
    await this.getDevelopMemberList();
  }

  addMemberHandle() {
    if (!this.newWechatId) {
      this.$message({
        type: "warning",
        message: "请输入微信名！",
      });
      return;
    }
    if (
      this.memberList.length > 0 &&
      this.memberList.includes(this.newWechatId)
    ) {
      this.$message({
        type: "warning",
        message: "微信名已存在！",
      });
      return;
    }
    this.setMemberHandle(this.newWechatId, 1);
    this.newWechatId = "";
  }

  async generateQrcode() {
    try {
      const response = await getQrcode();
      const { code, data } = response;
      if (code === 200) {
        this.qrcode = data;
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  // 立刻授权
  @Emit("refreshData")
  async updataBaseInfo() {
    try {
      const response = await updateMiniBaseInfo();
      const { code } = response;
      if (code === 200) {
        this.$message({
          type: "success",
          message: "更新小程序基本信息成功",
        });
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  // 小程序版本更新
  @Emit("refreshData")
  async updateVersion(loadingName: "updateBtnLoading" | "authBtnLoading") {
    try {
      let versionId =
        loadingName === "authBtnLoading"
          ? this.miniInfo.auditingTemplateDetailMinisId
          : this.codeVersion.id;
      if (!versionId) {
        return;
      }
      this[loadingName] = true;
      const response = await updateMiniVersion({
        templateDetailMinisId: versionId,
      });
      const { code } = response;
      if (code === 200) {
        this.$message({
          type: "success",
          message: "更新小程序版本成功",
        });
        this[loadingName] = false;
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
      this[loadingName] = false;
    }
  }

  // 小程序版本更新
  @Emit("refreshData")
  async revocationVersion() {
    try {
      const response = await revocationMiniVersion();
      const { code } = response;
      if (code === 200) {
        this.$message({
          type: "success",
          message: "撤销小程序审核版本成功",
        });
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  // 重新授权小程序
  async authPublic() {
    let platformShopId;
    if (
      storage.get("userInfo") &&
      storage.get("userInfo").shopInfoVo.platformShopId
    ) {
      platformShopId = storage.get("userInfo").shopInfoVo.platformShopId;
    } else {
      return;
    }
    const response = await authPreauthcode({
      // 要授权的帐号类型：1-公众号、2-小程序
      authType: 2,
      // 平台店铺id
      platformShopId,
      // 授权成功后跳转地址,前端地址
      successPage: window.location.href,
    });
    const { code, data } = response;
    if (code === 200) {
      open(data, "_top");
    }
  }
}
</script>

<style lang="scss" scoped>
table tr td {
  padding: 20px;
}

.qrcode {
  width: 180px;
}

.cursor-pointer {
  cursor: pointer;
}

.develop--input {
  width: 180px;
  margin-right: 15px;
}

.member {
  display: inline-block;
  padding: 15px;
  position: relative;

  .delete {
    position: absolute;
    right: 0;
    top: 0;
  }
}

.info {
  color: #cccccc;
}
</style>
