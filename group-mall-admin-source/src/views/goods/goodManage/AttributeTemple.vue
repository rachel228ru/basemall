<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:52
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:41:58
-->
<template>
  <div class="pageH">
    <div class="top--bottom">
      <el-button size="mini" @click="back" v-if="pageType"
        >返回发布商品</el-button
      >
      <el-button size="mini" type="primary" @click="add">新增</el-button>
    </div>
    <CategoryTem ref="getSelectData"></CategoryTem>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import CategoryTem from "./components/CategoryTem.vue";

@Component({
  components: {
    CategoryTem,
  },
})
export default class extends Vue {
  pageType = "";

  /**
   * 返回上一页
   */
  back() {
    this.$router.go(-1);
  }

  mounted() {
    this.pageType = this.$route.query.options as string;
  }

  add() {
    const getSelectData = this.$refs.getSelectData as any;
    getSelectData.dialogVisible = true;
    getSelectData.attributeTemplate = {
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
  }
}
</script>

<style lang="scss" scoped>
@import "../../../assets/styles/goods/index.scss";

.pageH {
  height: 100%;
}

.top--bottom {
  margin-top: 5px;
  margin-left: 5px;
}
</style>
