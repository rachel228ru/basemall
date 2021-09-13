<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :before-close="closeHandle"
    width="500px"
    class="dialog"
    title="设置标签"
  >
    <div class="tag--container">
      <div class="tag" v-for="tag in tagList" :key="tag.tagId">
        <el-checkbox v-model="tag.option"></el-checkbox>
        <el-input
          class="tag--input"
          size="mini"
          :maxlength="6"
          v-model="tag.tagName"
        ></el-input>
      </div>
    </div>
    <el-button type="text" @click="tagAddVisible = true" v-if="!tagAddVisible"
      >新增标签</el-button
    >
    <div class="tag--add" v-if="tagAddVisible">
      <el-input
        v-model="tagName"
        size="mini"
        @input="getTagName"
        style="width: 300px"
        placeholder="请输入标签名"
      ></el-input>
      <el-button type="text" @click="addLabel" style="margin-left: 10px"
        >添加</el-button
      >
      <el-button type="text" @click="tagAddVisible = false">取消</el-button>
    </div>
    <span slot="footer" class="dialog--footer">
      <el-button @click="closeHandle">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import { editTags } from "@/api/customer/customer";
import { ApiCustomerList, CustomerTagList } from "../../customerListType";
@Component
export default class SetLabel extends Vue {
  /** 所有标签 */
  @Prop({ required: true }) allTags!: CustomerTagList[];

  dataForm = {
    add: [] as Array<Partial<CustomerTagList>>,
    del: [] as Array<Partial<CustomerTagList>>,
    update: [] as Array<Partial<CustomerTagList>>,
    userIds: [] as Array<string | number>,
  };

  /** 渲染标签列表 */
  tagList:Array<Partial<CustomerTagList>>= [];

  /** 用户原本标签列表 */
  orgTags:Array<CustomerTagList>= [];

  users:Array<string>= [];

  tagName = "";

  dialogVisible = false;

  tagAddVisible = false;

  isinput = false;

  init(multipleSelection:Array<ApiCustomerList>) {
    this.initData();
    this.dataForm.userIds = multipleSelection.map((item) => item.userId);
    this.users = multipleSelection.map((item) => item.nikeName);
    // 得到初始用户标签
    multipleSelection.forEach((item) => {
      if (item.userTagVos !== null && item.userTagVos.length > 0) {
        this.orgTags.push(...item.userTagVos);
      }
    });
    this.getTagList();
    this.dialogVisible = true;
  }

  initData() {
    this.dataForm = {
      add: [],
      del: [],
      update: [],
      userIds: [],
    };
    this.tagList = [];
    this.orgTags = [];
    this.tagName = "";
  }

  getTagList() {
    const allTags = this.allTags;
    allTags.forEach((supTag) => {
      this.orgTags.forEach((tag) => {
        if (supTag.tagId === tag.tagId) {
          supTag.option = true;
        }
      });
    });
    this.tagList = allTags;
  }

  getTagName() {
    let charLength = 0;
    const tagName = this.tagName;
    for (let i = 0; i < tagName.length; i++) {
      const a = tagName.charAt(i);
      if (a.match(/[^\x00-\xff]/gi) != null) {
        if (charLength + 2 >= 12) {
          this.tagName = tagName.substr(0, i + 1);
          return;
        }
        charLength += 2;
      } else {
        if (charLength + 1 >= 12) {
          this.tagName = tagName.substr(0, i + 1);
          return;
        }
        charLength += 1;
      }
    }
  }

  addLabel() {
    if (this.isinput) {
      return;
    }
    this.isinput = true;
    // 判空
    if (this.tagName === "") {
      return;
    }
    const item = this.tagList.find((label) => {
      return label.tagName === this.tagName;
    });
    const tagNumber = this.tagList.filter((tagItem) => {
      return tagItem.tagName !== "";
    });
    if (tagNumber.length > 99) {
      this.$message({
        message: "最多添加100个标签",
        type: "warning",
      });
      return;
    }
    if (item !== undefined) {
      this.$message({
        message: "请勿重复添加标签",
        type: "warning",
      });
      return;
    }
    const tag = {
      option: true,
      tagName: this.tagName,
    };
    this.tagList.push(tag);
    this.tagName = "";
    this.isinput = false;
  }

  submit() {
    const add = this.tagList.filter((tag) => {
      return tag.tagId === undefined && tag.tagName !== "";
    });
    const del = this.tagList.filter((tag) => {
      return tag.tagName === "" && tag.tagId !== undefined;
    });
    const update = this.tagList.filter((tag) => {
      // isDel 为初始用户标签里勾选去掉的标签
      let isDel = false;
      if (
        tag.tagId !== undefined &&
        tag.tagName !== "" &&
        tag.option === false
      ) {
        if (
          this.orgTags.find((label) => label.tagId === tag.tagId) !== undefined
        ) {
          isDel = true;
        } else {
          isDel = this.allTags.some((j) => {
            return j.tagId === tag.tagId && j.tagName !== tag.tagName;
          });
        }
      }
      return (
        (tag.tagId !== undefined &&
          tag.tagName !== "" &&
          tag.option === true) ||
        isDel
      );
    });
    this.dataForm.add = add;
    this.dataForm.del = del;
    this.dataForm.update = update;
    editTags(this.dataForm).then(() => {
      this.$message({
        message: "设置成功",
        type: "success",
      });
      this.$emit("refreshDataList", 1);
      this.dialogVisible = false;
    });
  }

  // 关闭弹窗
  closeHandle() {
    this.dialogVisible = false;
  }
}
</script>

<style scoped lang="scss">
.customers {
  color: black;
}
.tag--container {
  overflow-y: scroll;
  height: 250px;

  .tag {
    display: inline-block;
    margin: 10px 5px;

    &--input {
      width: 120px;
      margin-left: 5px;
    }
  }
}

/deep/ .tag--input .el-input__inner {
  border: 1px solid transparent;
}

/deep/ .tag--input .el-input__inner:focus {
  border-color: #c0c4cc;
}

/deep/ .tag--input .el-input__inner:hover {
  border-color: #c0c4cc;
}

/deep/ .tag--container .el-checkbox__inner {
  width: 12px;
  height: 12px;
}

/deep/ .tag--container .el-checkbox__inner::after {
  left: 3px;
  top: 0px;
}
</style>
