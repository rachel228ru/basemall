<template>
  <div class="borderBox">
    <m-drag-list
      :data.sync="list"
      child-key="showCategoryVos"
      :defaultOpen="showList"
      @onMove="onMove"
    >
      <template #item="{itemData}">
        <div class="borderBox__first">
          <div>{{ itemData.name }}</div>
          <div class="borderBox__first--deal">
            <span style="color:#2D8CF0" @click.stop="modifyList(1, itemData)"
              >新增二级分类</span
            >
            <span style="color:#2D8CF0" @click.stop="modifyList(2, itemData)"
              >编辑</span
            >
            <span style="color:#FA6465" @click.stop="btnDelClass(itemData)"
              >删除</span
            >
          </div>
        </div>
      </template>
      <template #child="{childData}">
        <div class="borderBox__child" @onMove="onMove">
          <div class="borderBox__child--left">
            {{ childData.name }}
            <div style="text-align:left;width:92px">
              商品数量： {{ childData.productNumber }}
            </div>
          </div>
          <div class="borderBox__child--right">
            <div style="color:#2D8CF0" @click="editClassTwo(childData)">
              编辑
            </div>
            <div style="color:#FA6465" @click="delClassTwo(childData)">
              删除
            </div>
          </div>
        </div>
      </template>
    </m-drag-list>
    <div class="emptyLine" v-if="hasList">
      暂无数据~
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from "vue-property-decorator";
import ShowClass from '@/views/goods/goodManage/components/ShowClass.vue'
import { GoodCategroyType } from "@/views/goods/marketModel/goodType";
@Component
export default class ShowSecond extends Vue {
  @Watch("navigationListCom", { deep: true })
  handleNavigationListCom() {
    this.list = JSON.parse(JSON.stringify(this.navigationListCom));
    this.hasList = this.list.length === 0 ? true : false;
    this.list.forEach((item, index) => {
      if (item.showCategoryVos.length > 0) {
        this.showList.push(index);
      }
    });
  }

  get navigationListCom() {
    const parentHtml=this.$parent as ShowClass
    return parentHtml.navigationListCom;
  }

  /** 处理数组 */
  list: GoodCategroyType[] = [];

  /** 展开数组 */
  showList: number[] = [];

  /** 是否有数据 */
  hasList = false;

  onMove() {
    setTimeout(() => {
      this.$emit("navigationListComChange", this.list);
    }, 500);
  }

  /**
   * 一级编辑
   */
  modifyList(type:number, item:GoodCategroyType) {
    this.$emit("modifyList", type, item);
  }

  /**
   * 一级删除
   */
  btnDelClass(item:GoodCategroyType) {
    this.$emit("btnDelClass", item);
  }

  /**
   * 二级编辑
   */
  editClassTwo(item:GoodCategroyType) {
    this.$emit("editClassTwo", item);
  }

  /**
   * 二级删除
   */
  delClassTwo(item:GoodCategroyType) {
    this.$emit("delClassTwo", item);
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/mixins/mixins.scss";

@include b(borderBox) {
  @include e(first) {
    background-color: #f2f2f6;
    // padding: 0px 10px;
    display: flex;
    justify-content: space-between;
    @include m(deal) {
      margin-right: 10px;
      display: flex;
      justify-content: space-between;
      width: 200px;
      cursor: pointer;
    }
  }
  @include e(child) {
    padding: 15px 10px 15px 20px;
    border-bottom: 1px solid #f2f2f2;
    display: flex;
    justify-content: space-between;
    @include m(left) {
      display: flex;
      justify-content: space-between;
      width: 350px;
      padding-left: 10px;
    }
    @include m(right) {
      display: flex;
      justify-content: space-between;
      width: 88px;
      cursor: pointer;
    }
  }

  /deep/ .el-icon-arrow-right:before {
    content: "\e791";
  }

  /deep/ .el-collapse-item__content {
    padding-bottom: 0;
  }

  /deep/ .el-collapse-item__header {
    background-color: #f2f2f6;
    padding-left: 10px;
  }
}

.borderBox__child:hover {
  background-color: #f5f5f5;
  cursor: move;
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
  font-size: 14px;
  color: #b3b3b3;
  border-bottom: 1px solid #ebeef5;
  border-top: 1px solid #ebeef5;
}
</style>
