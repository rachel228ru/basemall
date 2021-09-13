<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 10:35:19
-->
<template>
  <div class="page">
    <div class="emptyLine" v-if="hasList">
      暂无数据~
    </div>
    <div
      v-for="(item, index) in attributeList"
      :key="index"
      class="tem__list table-border"
      v-else
    >
      <div class="tem__list--column">
        <div class="table-head-color tem__list--column--head">属性模板名称</div>
        <div
          style="background-color:#f6f7fb"
          class="tem__list--column--body table-border-top"
        >
          {{ item.name }}
        </div>
      </div>
      <div class="table-border-left tem__list--right">
        <div class="table-head-color tem__list--column--head">
          <div class="tem__list--column--head--name">属性名称</div>
          <div class="tem__list--column--head--par">属性特征</div>
          <div class="tem__list--column--head--do">
            <el-button
              style="padding:0"
              @click="editAttributeTemplate(2, item)"
              type="text"
              >编辑</el-button
            >
            <el-button
              style="padding:0"
              @click="deleteAttribute(item)"
              type="text"
              >删除</el-button
            >
          </div>
        </div>
        <div
          v-for="(listItem, listIndex) in item.attributeTemplates"
          :key="listIndex"
          class="table-border-top tem__list--navList"
        >
          <div class="tem__list--navList--param">{{ listItem.name }}</div>
          <div class="tem__list--right">{{ listItem.content }}</div>
          <div class="tem__list--right--icon">
            <i
              @click="deleteAttributeList(index, listIndex)"
              class="text-info el-icon-error"
              style="cursor:pointer"
            ></i>
          </div>
        </div>
      </div>
    </div>
    <PageManage
      :pageSize="pageSize"
      :pageNum="pageNum"
      :total="total"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    ></PageManage>
    <el-dialog
      :title="editFlag ? '编辑商品属性模板' : '新增商品属性模板'"
      :visible.sync="dialogVisible"
      width="560px"
      :before-close="cancelAttributeTemplate"
      @close="editFlag = false"
    >
      <el-form
        :model="attributeTemplate"
        ref="attributeTemplate"
        :rules="attributeTemplateRules"
      >
        <el-form-item label="模板名称" prop="name">
          <el-input
            style="width:40%"
            :maxlength="15"
            v-model="attributeTemplate.name"
            placeholder="请输入模板名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="" prop="attributeTemplates">
          <div class="attribute__table--head table-head-color">
            <div style="width:34%;margin-left:20px">属性名称</div>
            <div style="width:50%">属性特征</div>
            <div style="width:60px"></div>
          </div>
          <div
            v-for="(item, index) in attributeTemplate.attributeTemplates"
            :key="index"
            class="table-head-color attribute__table--body"
          >
            <div class="attribute__table--body--item">
              <div style="width:50%">
                <el-input
                  v-model="item.name"
                  size="lage"
                  maxlength="8"
                  style="width:80%"
                  placeholder="请输入属性名称"
                ></el-input>
              </div>
              <div style="width:50%">
                <el-input
                  v-model.trim="item.content"
                  size="lage"
                  maxlength="100"
                  style="width:80%"
                  placeholder="请输入属性特征"
                ></el-input>
              </div>
              <div
                class="tem__list--right"
                style="width:80px;  text-align: right;"
              >
                <i
                  @click="deleteParmList(index)"
                  class="text-info el-icon-error"
                ></i>
              </div>
            </div>
          </div>
          <div>
            <el-button @click="addParmList" type="text">添加</el-button>
          </div>
        </el-form-item>
      </el-form>
      <div style="text-align: center;">
        <el-button @click="cancelAttributeTemplate">取 消</el-button>
        <el-button
          type="primary"
          @click="saveAttributeTemplate('attributeTemplate')"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import PageManage from "@/components/PageManage.vue";
import {
  getAttsList,
  delAttr,
  delAttrSon,
  addAttrList,
  updateAttrList,
} from "@/api/good/goods";
import { GoodAttributeTempType } from "../../marketModel/goodType";
import { ElForm } from "element-ui/types/form";

@Component({
  components: {
    PageManage,
  },
})
export default class extends Vue {
  attributeList: Array<GoodAttributeTempType> = [];

  attributeTemplate = {
    name: "",
    content: "",
    id: "",
    parentId: "0",
    attributeTemplates: [
      {
        content: "",
        name: "",
        id: "",
        parentId: "1",
      },
    ],
  };

  attributeTemplateRules = {
    name: [{ required: true, trigger: "blur", message: "模板名称不能为空" }],
    attributeTemplates: [
      {
        required: true,
        trigger: "focus",
        validator: (
          _rule: any,
          value: Array<{ content: string; name: string }>,
          callback: (arg0: Error) => void,
        ) => {
          value.forEach(item => {
            if (item.content === "" || item.name === "") {
              callback(new Error("属性名称与特征不能为空"));
            }
          });
        },
      },
    ],
  };

  editFlag = false;

  dialogVisible = false;

  pageSize = 10;

  pageNum = 1;

  total = 0;

  hasList = false;

  mounted() {
    this.getAttList();
  }

  /**
   * 获取属性模板
   */
  getAttList() {
    const param = {
      current: this.pageNum,
      size: this.pageSize,
    };
    getAttsList(param).then(res => {
      this.attributeList = res.data.list;
      this.hasList = res.data.list.length === 0 ? true : false;
      this.total = res.data.total;
    });
  }

  /**
   * @method editParmList
   * @description 编辑模板
   */
  editAttributeTemplate(type: number, item: GoodAttributeTempType) {
    this.dialogVisible = true;
    this.attributeTemplate = JSON.parse(JSON.stringify(item));
    this.editFlag = type === 2 ? true : false;
  }

  /**
   * 删除列表中属性
   */
  deleteAttributeList(index: number, listIndex: number) {
    this.$confirm("确定要删除选中属性模板吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      const attributeTemplate = JSON.parse(
        JSON.stringify(this.attributeList[index]),
      );
      const ids = attributeTemplate.attributeTemplates[listIndex].id;
      delAttrSon(ids, {})
        .then(() => {
          this.$message.success("删除成功");
          this.getAttList();
        })
        .catch(err => {
          console.log(err);
        });
    });
  }

  /**
   * 删除属性模板
   */
  deleteAttribute(item: GoodAttributeTempType) {
    this.$confirm("确定要删除选中属性模板吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      delAttr(item.id, {})
        .then(() => {
          this.$message.success("删除成功");
          this.getAttList();
        })
        .catch(err => {
          console.log(err);
          this.$message.error(err);
        });
    });
  }

  /**
   * 添加模板名称
   */
  addParmList() {
    const list = this.attributeTemplate.attributeTemplates;
    list.push({
      content: "",
      name: "",
      id: "",
      parentId: "1",
    });
  }

  /**
   * 删除属性名称
   */
  deleteParmList(index: number) {
    if (this.attributeTemplate.attributeTemplates.length > 1) {
      this.attributeTemplate.attributeTemplates.splice(index, 1);
    } else {
      this.$message.error("不能删除最后一条数据");
    }
  }

  /**
   * 取消操作
   */
  cancelAttributeTemplate() {
    this.$confirm(
      "确定要退出编辑属性列表页面? 退出后，未保存的信息将不会保留!",
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      },
    ).then(() => {
      this.dialogVisible = false;
    });
  }

  /**
   * 保存模板
   */
  saveAttributeTemplate(formName: string) {
    let hasBlack = true;
    const formNameValid = this.$refs[formName] as ElForm;
    formNameValid.validate(valid => {
      if (!valid) {
        hasBlack = false;
      }
    });
    if (!hasBlack) {
      return;
    }
    if (this.editFlag === true) {
      this.attributeTemplate.attributeTemplates.forEach(item => {
        item.parentId = this.attributeTemplate.id;
      });
      updateAttrList(this.attributeTemplate)
        .then(res => {
          if (res.code === 200) {
            this.$message.success("编辑成功");
            this.getAttList();
            this.dialogVisible = false;
          }
        })
        .catch(err => {
          this.$message.error(`${err}`);
        });
    } else {
      addAttrList(this.attributeTemplate)
        .then(res => {
          if (res.code === 200) {
            this.$message.success("添加成功");
            this.getAttList();
            this.dialogVisible = false;
          }
        })
        .catch(err => {
          this.$message.error(`${err}`);
        });
    }
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val: number) {
    this.pageSize = val;
    this.getAttList();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val: number) {
    this.pageNum = val;
    this.getAttList();
  }
}
</script>

<style lang="scss" scoped>
@import "../../../../assets/styles/goods/index.scss";

.noList {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  color: #3d3d3d;
  width: 100%;
}

.page {
  margin-top: -20px;
  margin-left: -15px;
}

.notemp {
  margin-top: 20px;
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
}

.tem__list {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  &--column {
    width: 180px;
    display: flex;
    flex-direction: column;
  }
  &--column--head {
    display: flex;
    justify-content: center;
    padding-top: 20px;
    padding-bottom: 20px;
    &--name {
      padding-left: 20px;
      padding-right: 20px;
      width: 40%;
    }
    &--par {
      flex: auto;
    }
    &--do {
      flex: none;
      width: 120px;
      padding-left: 20px;
      padding-right: 20px;
      text-align: right;
    }
  }
  &--column--body {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    flex: auto;
  }
  &--navList {
    display: flex;
    line-height: 44px;
    &--param {
      width: 40%;
      padding-left: 20px;
      padding-right: 20px;
    }
  }
  &--right {
    flex: auto;
    &--icon {
      flex: none;
      width: 120px;
      padding-left: 20px;
      padding-right: 20px;
      text-align: right;
    }
  }
}

.attribute__table--head {
  display: flex;
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
  margin-bottom: 10px;
}

.attribute__table--body {
  padding: 8px 10px;
  display: flex;
  &--item {
    display: flex;
    align-items: center;
  }
}

.emptyLine {
  width: 100%;
  height: 80px;
  background-color: white;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
  margin-top: 15px;
  font-size: 14px;
  color: #b3b3b3;
  border-bottom: 1px solid #ebeef5;
  border-top: 1px solid #ebeef5;
}
</style>
