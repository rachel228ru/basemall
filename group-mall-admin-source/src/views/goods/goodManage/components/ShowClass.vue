<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:45:54
-->
<template>
  <div>
    <div class="button--line">
      <div style="display:flex;">
        <el-button @click="back" v-if="showClassType">返回专区列表</el-button>
        <el-button @click="back" v-if="showType">返回发布商品</el-button>
        <el-button @click="modifyList(3)" type="primary">新增分类</el-button>
      </div>
      <div class="button--right">拖拽可调整展示分类顺序</div>
    </div>
    <ShowSecond
      :navigationListCom="navigationListCom"
      @modifyList="modifyList"
      @btnDelClass="btnDelClass"
      @editClassTwo="editClassTwo"
      @delClassTwo="delClassTwo"
      @navigationListComChange="navigationListComChange"
    ></ShowSecond>
    <PageManage
      :pageSize="pageSize"
      :pageNum="pageNum"
      :total="total"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    ></PageManage>
    <el-dialog :visible.sync="editFlag" width="30%" :before-close="cancel">
      <span slot="title">
        <div class="dialogTitle">{{ addFlag ? "编辑" : "新增" }}商品类别</div>
      </span>
      <div class="dialog__radio">
        <span style="margin-right:30px;">*级别：</span>
        <el-radio
          v-model="rankRadio"
          label="1"
          :disabled="levelOne"
          @change="changeRadio"
          >一级</el-radio
        >
        <el-radio
          v-model="rankRadio"
          label="2"
          :disabled="levelTwo"
          @change="changeRadio"
          >二级</el-radio
        >
      </div>
      <div class="dialog__line">
        <div class="dialog__line__fir">
          *一级分类：
          <el-input
            placeholder="请输入内容"
            maxlength="8"
            v-model="currentItem.name"
            style="width:180px;"
            v-if="rankRadio == '1'"
          ></el-input>
          <el-select
            v-model="currentItem.name"
            v-if="rankRadio == '2'"
            placeholder="请选择一级模块"
            @change="select"
          >
            <el-option
              v-for="item in navigationListCom"
              :key="item.name"
              :label="item.name"
              :value="item.name"
            ></el-option>
          </el-select>
        </div>
      </div>
      <div class="dialog__line" v-if="rankRadio == '2'">
        <div class="dialog__line__sec">
          *二级分类：
          <div style="width:300px;border:1px solid #999;">
            <div class="dialog__line__sec--head">
              <div>下级分类名称</div>
              <div>操作</div>
            </div>
            <div
              v-for="(child, index) in currentItem.showCategoryVos"
              :key="index"
            >
              <div class="dialog__line__sec--body">
                <el-input
                  v-model="child.name"
                  style="width:150px;"
                  placeholder="请输入分类名称"
                  maxlength="8"
                ></el-input>
                <div v-if="!temObj || !temObj.name">
                  <div
                    class="add--text"
                    @click="getAddClass"
                    style="color:#2D8CF0;cursor:pointer"
                    v-if="index + 1 === currentItem.showCategoryVos.length"
                  >
                    新增
                  </div>
                  <span
                    style="color:#e96900;cursor:pointer;"
                    @click="delAddClass(index)"
                    >删除</span
                  >
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog--footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button
          type="primary"
          @click="editClass()"
          v-if="temObj && temObj.name"
          >确 定</el-button
        >
        <el-button type="primary" @click="editOk" v-else>确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import ShowSecond from "@/components/ShowSecond.vue";
import PageManage from "@/components/PageManage.vue";

import {
  delLevel,
  addList,
  addSecondList,
  updateList,
  Sort,
  getList,
} from "@/api/good/goods";
import { GoodCategroyType } from "../../marketModel/goodType";
@Component({
  components: {
    ShowSecond,
    PageManage,
  },
})
export default class ShowClass extends Vue {
  /** 展示分类 */
  navigationListCom: Array<GoodCategroyType> = [];

  /** 编辑一级二级分类 */
  rankRadio = "1";

  /** 列表中选择一级分类 */
  tamValue = {} as GoodCategroyType;

  /** 编辑二级暂存 */
  temObj = null as GoodCategroyType | null;

  /** 处理的分类对象 */
  currentItem = {
    id: "",
    name: "",
    level: 0,
    parentId: 0,
    saleMode: "",
    showCategoryVos: [
      {
        id: "",
        level: 1,
        name: "",
        parentId: "",
        saleMode: "",
      },
    ],
  };

  /** 编辑弹窗 */
  editFlag = false;

  /** 编辑或新增状态 */
  addFlag = false;

  /** 一级单选框 */
  levelOne = false;

  /** 二级单选框 */
  levelTwo = false;

  // 分页页数
  pageSize = 10;

  // 分页页码
  pageNum = 1;

  // 数据总数
  total = 0;

  /** 是否发布商品返回 */
  showType = false;

  showClassType = false;

  saleMode = "";

  mounted() {
    this.showType = false;
    this.showClassType = false;
    if (this.$route.query.options === "1") {
      this.showType = true;
      this.saleMode = this.$route.query.saleMode as string;
    }
    if (this.$route.query.options === "classSet") {
      this.showClassType = true;
      this.saleMode = this.$route.query.id as string;
      this.currentItem.saleMode = this.saleMode ? this.saleMode : "";
      this.currentItem.showCategoryVos[0].saleMode = this.saleMode
        ? this.saleMode
        : "";
    }
    this.getAllList();
    this.pageSize = 10;
    this.pageNum = 1;
  }

  getAllList(isEdd = false) {
    if (isEdd) {
      this.pageNum = 1;
    }
    const param = {
      current: this.pageNum,
      size: this.pageSize,
      saleMode: this.saleMode,
    };
    getList(param).then(res => {
      this.navigationListCom = res.data.list;
      this.total = res.data.total;
    });
  }

  /** 编辑 新增 操作 */
  modifyList(type: number, item: GoodCategroyType) {
    this.editFlag = true;
    this.levelOne = false;
    this.levelTwo = false;
    if (type === 3) {
      this.rankRadio = "1";
      this.addFlag = false;
    } else if (type === 2) {
      this.rankRadio = "1";
      this.levelTwo = true;
      this.addFlag = true;
      // this.currentItem = item;
      this.currentItem = JSON.parse(JSON.stringify(item));
    } else if (type === 1) {
      this.rankRadio = "2";
      this.addFlag = false;
      this.currentItem.name = item.name;
      this.currentItem.showCategoryVos = [];
      this.currentItem.showCategoryVos.push({
        id: "",
        level: 1,
        name: "",
        parentId: "",
        saleMode: "",
      });
      this.tamValue = item;
    }
  }

  /**
   * 选择一级或二级分类
   */
  changeRadio() {
    this.currentItem = {
      id: "",
      name: "",
      level: 0,
      parentId: 0,
      saleMode: "",
      showCategoryVos: [
        {
          id: "",
          level: 1,
          name: "",
          parentId: "",
          saleMode: "",
        },
      ],
    };
  }

  /**
   * 新增是获取一级分类下数据
   */
  select(e: string) {
    this.navigationListCom.forEach(item => {
      if (item.name === e) {
        this.tamValue = item;
      }
    });
  }

  /**
   * 新增下级分类
   */
  getAddClass() {
    this.currentItem.showCategoryVos.push({
      id: "",
      level: 1,
      name: "",
      parentId: "",
      saleMode: "",
    });
  }

  /**
   * 删除新增下级分类
   */
  delAddClass(index: number) {
    if (this.currentItem.showCategoryVos.length === 1) {
      this.$message.error("至少保留一行子分类");
    } else {
      this.currentItem.showCategoryVos.splice(index, 1);
    }
  }

  /**
   * 完成商品分类的新增/编辑
   */
  editOk() {
    if (this.rankRadio === "1") {
      this.dealLevelOne();
    } else if (this.rankRadio === "2") {
      this.dealLevelTwo();
    }
  }

  /** 处理一级展示分类 */
  dealLevelOne() {
    if (this.currentItem.name === "") {
      this.$message.error("一级分类不能为空");
      return;
    }
    if (this.currentItem.name !== "") {
      if (this.addFlag === false) {
        const param = {
          id: this.currentItem.id,
          name: this.currentItem.name,
          level: 0,
          parentId: "0",
          saleMode: this.saleMode,
        };
        addList(param)
          .then(res => {
            if (res.code === 200) {
              this.$message.success("添加成功");
              this.editFlag = false;
              this.getAllList(true);
              this.currentItem.name = "";
            }
          })
          .catch(res => {
            this.$message.error(res);
          });
      } else {
        this.currentItem.level = 0;
        this.currentItem.parentId = 0;
        updateList(this.currentItem)
          .then(res => {
            if (res.code === 200) {
              this.$message.success("编辑成功");
              this.getAllList();
              this.editFlag = false;
              this.currentItem.name = "";
            }
          })
          .catch(res => {
            this.$message.error(res);
          });
      }
    }
  }

  /** 处理二级展示分类 */
  dealLevelTwo() {
    this.currentItem.id = this.tamValue.id as string;
    const length = this.currentItem.showCategoryVos.filter(v => {
      return v.name === "";
    }).length;
    if (!this.currentItem.name) {
      this.$message.error("请选择一级分类");
      return;
    }
    if (length > 0) {
      this.$message.error("请输入二级分类");
      return;
    }
    const arry = this.currentItem.showCategoryVos.map(item => {
      return item.name;
    });
    let chongfu = arry.every((item, index, self) => {
      return self.indexOf(item) === index;
    });
    if (!chongfu) {
      this.$message.error("输入的二级分类重复");
      return;
    }
    this.currentItem.showCategoryVos.forEach(item => {
      item.parentId = this.currentItem.id;
    });
    addSecondList(this.currentItem.showCategoryVos)
      .then(res => {
        if (res.code === 200) {
          this.$message.success("添加成功");
          this.getAllList();
          this.editFlag = false;
          this.currentItem.name = "";
        }
      })
      .catch(res => {
        this.$message.error(res);
      });
  }

  /** 删除分类 */
  btnDelClass(item: GoodCategroyType) {
    this.$confirm("确定要删除选中分类吗?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      delLevel(item.id, {})
        .then(res => {
          if (res.code === 200) {
            this.editFlag = false;
            this.$message.success("删除成功");
            this.getAllList();
          }
        })
        .catch(err => {
          this.$message.error(err);
        });
    });
  }

  /**
   * 编辑二级分类
   */
  editClassTwo(item: GoodCategroyType) {
    this.currentItem = {
      id: "",
      name: "",
      level: 0,
      parentId: 0,
      saleMode: "",
      showCategoryVos: [
        {
          id: "",
          level: 1,
          name: "",
          parentId: "",
          saleMode: "",
        },
      ],
    };
    this.navigationListCom.forEach(v => {
      v.showCategoryVos.forEach(i => {
        if (i.id === item.id) {
          this.currentItem.showCategoryVos[0].name = i.name;
          this.currentItem.name = v.name;
        }
      });
    });
    this.addFlag = true;
    this.editFlag = true;
    this.rankRadio = "2";
    this.levelOne = true;
    this.temObj = item;
  }

  /**
   * 删除二级分类
   */
  delClassTwo(item: GoodCategroyType) {
    this.$confirm("确定要删除此二级分类吗?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      delLevel(item.id, {})
        .then(res => {
          if (res.code === 200) {
            this.$message.success("删除成功");
            this.getAllList();
          }
        })
        .catch(err => {
          this.$message.error(err);
        });
    });
  }

  /**
   * 保存二级分类编辑
   */
  editClass() {
    const temObj = this.temObj as GoodCategroyType;
    const tamValue = this.tamValue;
    this.currentItem.showCategoryVos[0].id = String(temObj.id);
    this.navigationListCom.forEach(v => {
      v.showCategoryVos.forEach(i => {
        if (i.id === temObj.id) {
          this.currentItem.showCategoryVos[0].parentId = String(v.id);
        }
      });
    });
    updateList({
      id: this.currentItem.showCategoryVos[0].id,
      level: 1,
      name: this.currentItem.showCategoryVos[0].name,
      parentId: tamValue.id
        ? tamValue.id
        : this.currentItem.showCategoryVos[0].parentId,
      saleMode: this.saleMode ? this.saleMode : "",
    })
      .then(res => {
        if (res.code === 200) {
          this.$message.success("修改成功");
          this.editFlag = false;
          this.currentItem.name = "";
          this.getAllList();
          this.temObj = null;
        }
      })
      .catch(res => {
        this.$message.error(res);
      });
  }

  /** 弹窗取消 */
  cancel() {
    this.rankRadio = "1";
    this.editFlag = false;
    // this.navigationListCom.name = "";
    this.temObj = null;
    this.levelOne = false;
    this.levelTwo = false;
    if (this.currentItem.showCategoryVos.length > 0) {
      this.currentItem.showCategoryVos[0].name = "";
    }
    this.currentItem = {
      id: "",
      name: "",
      level: 0,
      parentId: 0,
      saleMode: "",
      showCategoryVos: [
        {
          id: "",
          level: 1,
          name: "",
          parentId: "",
          saleMode: "",
        },
      ],
    };
  }

  navigationListComChange(list: GoodCategroyType[]) {
    list.forEach((item, index) => {
      item.sort = index + 10 * (this.pageNum - 1);
    });
    Sort(list);
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val: number) {
    this.pageSize = val;
    this.getAllList();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val: number) {
    this.pageNum = val;
    this.getAllList();
  }

  /**
   * 返回上一页
   */
  back() {
    this.$router.go(-1);
  }
}
</script>

<style lang="scss" scoped>
.button--line {
  padding-top: 5px;
  padding-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button--right {
  // padding-right: 20px;
  color: #7f7f7f;
  font-size: 13px;
}

.dialogTitle {
  display: flex;
  justify-content: center;
  font-weight: bold;
  font-size: 16px;
}

.dialog__radio {
  display: flex;
  margin-left: 20px;
  margin-bottom: 20px;
}

.dialog__line {
  display: flex;
  padding-left: 20px;
  padding-top: 10px;
  justify-content: flex-start;
  align-items: flex-start;
  &__fir {
    width: 100%;
    text-align: right;
    display: flex;
    align-items: center;
  }
  &__sec {
    width: 100%;
    display: flex;
    margin-top: 10px;
    &--head {
      padding: 10px;
      display: flex;
      justify-content: space-between;
      font-size: 12px;
      background-color: #f6f6f6;
    }
    &--body {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid #999;
      padding: 10px;
    }
  }
}

.dialog--footer {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
