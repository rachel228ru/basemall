<template>
  <div>
    <el-form :model="dataForm" label-width="80px" label-position="left">
          <el-form-item label="存储类型">
            <el-radio-group v-model="dataForm.type" @change="getOssData">
              <el-radio :label="1">七牛云</el-radio>
              <el-radio :label="2">阿里云</el-radio>
              <el-radio :label="3">腾讯云</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <el-form :model="dataForm" label-width="120px" label-position="left">
          <div v-if="dataForm.type === 1">
            <el-form-item>
              <span>免费申请（七牛云）10GB存储空间</span>
            </el-form-item>
            <el-form-item label="域名">
              <el-input v-model="dataForm.qiniuDomain" clearable size="small"
                        placeholder="七牛云绑定的域名"></el-input>
            </el-form-item>
            <el-form-item label="路径前缀">
              <el-input v-model="dataForm.qiniuPrefix" clearable size="small"
                        placeholder="不设置默认为空"></el-input>
            </el-form-item>
            <el-form-item label="AccessKey">
              <el-input v-model="dataForm.qiniuAccessKey" clearable size="small"
                        placeholder="七牛云AccessKey"></el-input>
            </el-form-item>
            <el-form-item label="SecreKey">
              <el-input v-model="dataForm.qiniuSecretKey" clearable size="small"
                        placeholder="七牛云SecreKey"></el-input>
            </el-form-item>
            <el-form-item label="空间名">
              <el-input v-model="dataForm.qiniuBucketName" clearable size="small"
                        placeholder="七牛云空间名"></el-input>
            </el-form-item>
          </div>
          <div v-if="dataForm.type === 2">
            <el-form-item label="域名">
              <el-input v-model="dataForm.aliyunDomain" clearable size="small"
                        placeholder="阿里云绑定的域名"></el-input>
            </el-form-item>
            <el-form-item label="路径前缀">
              <el-input v-model="dataForm.aliyunPrefix" clearable size="small"
                        placeholder="不设置默认为空"></el-input>
            </el-form-item>
            <el-form-item label="EndPoint">
              <el-input v-model="dataForm.aliyunEndPoint" clearable size="small"
                        placeholder="阿里云EndPoint"></el-input>
            </el-form-item>
            <el-form-item label="AccessKeyld">
              <el-input v-model="dataForm.aliyunAccessKeyId" clearable size="small"
                        placeholder="阿里云AccessKeyld"></el-input>
            </el-form-item>
            <el-form-item label="AccessKeySecret">
              <el-input v-model="dataForm.aliyunAccessKeySecret" clearable size="small"
                        placeholder="阿里云AccessKeySecret"></el-input>
            </el-form-item>
            <el-form-item label="BucketName">
              <el-input v-model="dataForm.aliyunBucketName" clearable size="small"
                        placeholder="阿里云BucketName"></el-input>
            </el-form-item>
          </div>
          <div v-if="dataForm.type === 3">
            <el-form-item label="域名">
              <el-input v-model="dataForm.qcloudDomain" clearable size="small"
                        placeholder="腾讯云绑定的域名"></el-input>
            </el-form-item>
            <el-form-item label="路径前缀">
              <el-input v-model="dataForm.qcloudPrefix" clearable size="small"
                        placeholder="不设置默认为空"></el-input>
            </el-form-item>
            <el-form-item label="Appid">
              <el-input v-model="dataForm.qcloudAppId" clearable size="small"
                        placeholder="腾讯云Appid"></el-input>
            </el-form-item>
            <el-form-item label="Secretld">
              <el-input v-model="dataForm.qcloudSecretId" clearable size="small"
                        placeholder="腾讯云Secretld"></el-input>
            </el-form-item>
            <el-form-item label="SecretKey">
              <el-input v-model="dataForm.qcloudSecretKey" clearable size="small"
                        placeholder="腾讯云SecretKey"></el-input>
            </el-form-item>
            <el-form-item label="BucketName">
              <el-input v-model="dataForm.qcloudBucketName" clearable size="small"
                        placeholder="腾讯云BucketName"></el-input>
            </el-form-item>
            <el-form-item label="Bucket所属地区">
              <el-input v-model="dataForm.qcloudRegion" clearable size="small"
                        placeholder="如：sh（可选填，华南：gz华北：tj华东：sh）"></el-input>
            </el-form-item>
          </div>
        </el-form>
        <div class="footer-submit">
          <el-button size="small" @click="serviceEditVisible = false">取 消</el-button>
          <el-button type="primary" size="small" @click="saveSystemConfig">确定</el-button>
        </div>
  </div>
</template>
<script lang='ts'>
import { Component, Vue } from 'vue-property-decorator'
import { getOssConfig,modifySystemConfig } from "@/api/sign/index";

@Component
export default class OSSSettings extends Vue {

  dataForm = {
    aliyunAccessKeyId: "",
    aliyunAccessKeySecret: "",
    aliyunBucketName: "",
    aliyunDomain: "",
    aliyunEndPoint: "",
    aliyunPrefix: "",
    qiniuAccessKey: "",
    qiniuBucketName: "",
    qiniuDomain: "",
    qiniuPrefix: "",
    qiniuSecretKey: "",
    qcloudAppId: 0,
    qcloudBucketName: "",
    qcloudDomain: "",
    qcloudPrefix: "",
    qcloudRegion: "",
    qcloudSecretId: "",
    qcloudSecretKey: "",
    type: 1,
  };

  mounted(){
    this.getOssData(1);
  }

  

  getOssData(type:number) {
    getOssConfig({
      type
    }).then(res=>{
      this.dataForm = res.data
    }).catch(err=>{
      console.log(err)
    })
  }

  saveSystemConfig() {
    if (this.dataForm.type === 1) {
      const form = {
        type: 1,
        qiniouStorageConfig: {
          qiniuAccessKey: this.dataForm.qiniuAccessKey,
          qiniuBucketName: this.dataForm.qiniuBucketName,
          qiniuDomain: this.dataForm.qiniuDomain,
          qiniuPrefix: this.dataForm.qiniuPrefix,
          qiniuSecretKey: this.dataForm.qiniuSecretKey,
        },
      };
      modifySystemConfig(JSON.stringify(form)).then(() => {
        this.$message({
          type: "success",
          message: "保存成功!",
        });
      });
    }
    if (this.dataForm.type === 2) {
      const form = {
        type: 2,
        aliyunStorageConfig: {
          aliyunAccessKeyId: this.dataForm.aliyunAccessKeyId,
          aliyunAccessKeySecret: this.dataForm.aliyunAccessKeySecret,
          aliyunBucketName: this.dataForm.aliyunBucketName,
          aliyunDomain: this.dataForm.aliyunDomain,
          aliyunEndPoint: this.dataForm.aliyunEndPoint,
          aliyunPrefix: this.dataForm.aliyunPrefix,
        },
      };
      modifySystemConfig(JSON.stringify(form)).then(() => {
        this.$message({
          type: "success",
          message: "保存成功!",
        });
      });
    }
    if (this.dataForm.type === 3) {
      const form = {
        type: 3,
        tencentStorageConfig: {
          qcloudAppId: this.dataForm.qcloudAppId,
          qcloudBucketName: this.dataForm.qcloudBucketName,
          qcloudDomain: this.dataForm.qcloudDomain,
          qcloudPrefix: this.dataForm.qcloudPrefix,
          qcloudRegion: this.dataForm.qcloudRegion,
          qcloudSecretId: this.dataForm.qcloudSecretId,
          qcloudSecretKey: this.dataForm.qcloudSecretKey,
        },
      };
      modifySystemConfig(JSON.stringify(form)).then(() => {
        this.$message({
          type: "success",
          message: "保存成功!",
        });
      });
    }
  }

}
</script>
<style scoped>
</style>