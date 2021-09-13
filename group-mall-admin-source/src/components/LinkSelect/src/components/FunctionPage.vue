<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:22:59
-->
<template>
  <!-- 功能页面 -->
  <div>
    <el-table :data="tableData" height="369">
      <el-table-column label="页面名称" prop="name"></el-table-column>
      <el-table-column label="操作" width="100px">
        <template slot-scope="scope">
          <el-radio
            v-model="selectId"
            @change="selectHandle"
            :label="scope.row.id"
          >
            <span></span>
          </el-radio>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, PropSync, Watch } from "vue-property-decorator";
import LinkSelectItem, { typeNameMap } from "./LinkSelectItem";
import { getPageList } from "@/api/group/group";
import { getTemplate } from "@/api/decoration/decoration";

/** 功能页面link数据的id和type值不允许更改 */
@Component
export default class LinkSelect extends Vue {
  @PropSync("link", {
    type: Object,
    default: () => {
      return {
        id: null,
        type: null,
        name: "",
        url: "",
        append: "",
      };
    },
  })
  linkSelectItem!: LinkSelectItem;

  @Prop({
    type: Boolean,
    default: false,
  })
  visible!: boolean;

  @Prop({ default: false }) // 底部导航去客服
  noProTab!: boolean;

  @Prop({ default: true }) // 底部导航限制可选范围
  limitProTab!: boolean;

  name = "FunctionPage";

  selectId: string | number = "";

  searchName = "";

  templateId = "";

  searchData: LinkSelectItem[] = []; // 暂存数据

  get tableData() {
    let tableData = [
      {
        id: 0,
        type: 0,
        name: "店铺主页",
        url: "/pages/index/index",
        append: "home",
      },
      {
        id: 3,
        type: 0,
        name: "个人中心",
        url: "/pages/index/index",
        append: "me",
      },
      {
        id: 4,
        type: 0,
        name: "购物车",
        url: "/pages/index/index",
        append: "shopCar",
      },
      {
        id: 5,
        type: 0,
        name: "资源入驻",
        url: "/pages/resourceApply/resourceApply",
        append: "",
      },
      {
        id: 7,
        type: 0,
        name: "地址管理",
        url: "/pages/address/address",
        append: "",
      },
      {
        id: 8,
        type: 0,
        name: "客服",
        url: "",
        append: "",
      },
      {
        id: 9,
        type: 0,
        name: "设置",
        url: "/pages/mySetting/mySetting",
        append: "",
      },
    ];

    if (
      !Reflect.has(this.$STORE.userStore, "isPointOpen") ||
      this.$STORE.userStore.shopInfo.level === 1
    ) {
      return tableData.filter(item => {
        return item.id !== 12;
      });
    } else {
      return tableData;
    }
  }

  mounted() {
    this.getTemplateList();
    const level = this.$STORE.userStore.shopInfo.level;
    if (level === 1) {
      this.tableData.forEach((item, index) => {
        if (item.id === 12) {
          this.tableData.splice(index, 1);
        }
      });
    }
    // 初始相同设为选中状态
    if (this.sameJudge()) {
      this.selectId = this.linkSelectItem.id;
    }
    this.searchData = this.tableData;
    if (this.noProTab) {
      const tableData = this.tableData;
      for (let i = 0; i < tableData.length; i++) {
        if (tableData[i].id == 8) {
          tableData.splice(i, 1);
          break;
        }
      }
    }
    const tableData = this.tableData;

    if (!this.limitProTab) {
      for (let i = 0; i < tableData.length; i++) {
        if (tableData[i].id == 5) {
          tableData.splice(i, 1);
        }
        if (tableData[i].id == 7) {
          tableData.splice(i, 1);
        }
        if (tableData[i].id == 8) {
          tableData.splice(i, 1);
        }
        if (tableData[i].id == 9) {
          tableData.splice(i, 1);
        }
        if (tableData[i].id == 11) {
          tableData.splice(i, 1);
        }
        if (tableData[i].id == 12) {
          tableData.splice(i, 1);
        }
        if (tableData[i].id == 14) {
          tableData.splice(i, 1);
        }
      }
    }
  }

  // 监听父弹窗显隐
  @Watch("visible")
  handleVisibleChange(v: boolean) {
    if (v) {
      // 初始相同设为选中状态
      if (this.sameJudge()) {
        this.selectId = this.linkSelectItem.id;
      }
    }
  }

  selectHandle() {
    const selectId = this.selectId;
    const currentItem: LinkSelectItem =
      this.tableData.find(item => item.id === selectId) ||
      ({} as LinkSelectItem);
    Object.assign(this.linkSelectItem, currentItem);
  }

  sameJudge(): boolean {
    return (
      typeNameMap[this.linkSelectItem.type] &&
      typeNameMap[this.linkSelectItem.type].name === this.name
    );
  }

  /**
   * 获取模板列表
   * isDevTemplate 默认模板 1是 0否
   * 模板是否使用中 0 否, 1 是
   * 自定义没有
   */
  async getTemplateList() {
    const getPageListData = {
      onlineStatus: 1,
      isAll: 0,
    };
    const res = await getTemplate(getPageListData);
    try {
      if (res.code === 200) {
        /**  获取非默认模板（即为正在使用中的模板） */
        if (res.data.length > 0) {
          /** 如果有，即为使用中模板的curd */
          this.templateId = res.data[0].id;
          this.getPageList();
        }
      } else {
        this.$message.warning(`获取数据失败`);
      }
    } catch (e) {
      console.log(e);
    }
  }

  /** 获取自定义页面数据 */
  async getPageList() {
    const data = {
      templateId: this.templateId,
      size: 100,
    };
    const res = await getPageList(data);
    try {
      if (res.code === 200) {
        this.tableData[0].id = res.data.list.filter(
          (item: { isDef: string }) => item.isDef !== "0",
        )[0].id; // 初始化获取 设为首页的页面id
      } else {
        this.$message.warning(`获取数据失败`);
      }
    } catch (e) {
      console.log(e);
    }
  }
}
</script>
<style scoped>
.search-wrap {
  display: flex;
  justify-content: space-between;
  justify-items: center;
}
.search-wrap-input {
  width: 180px;
}
</style>
