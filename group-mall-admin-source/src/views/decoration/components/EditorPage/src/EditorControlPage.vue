<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:45:42
-->
<template>
  <!-- 页面管理页面 -->
  <div class="editor_control_wrap">
    <div class="editor_control_wrap_main">
      <el-scrollbar style="height: 100%; width: 100%">
        <!-- 功能页面 -->
        <div class="tab_item" @click.prevent="fold('showUserList')">
          <i
            :class="
              showUserList ? 'el-icon-caret-bottom' : 'el-icon-caret-right'
            "
          ></i>

          <span>功能页面</span>
        </div>
        <div v-if="showUserList" :class="`${showUserList}`">
          <div
            class="page_item"
            @click="selectPage('usercenter', '', 'usercenter', -1)"
          >
            <span>用户中心</span>
            <span
              class="el-dropdown-link"
              v-if="functionpagetype === 'usercenter'"
            >
              <i class="el-icon-edit-outline"></i>
            </span>
          </div>
        </div>

        <!-- 营销页面 -->
        <div class="tab_item" @click="fold('showBussinessList')">
          <i
            :class="
              showBussinessList ? 'el-icon-caret-bottom' : 'el-icon-caret-right'
            "
          ></i>
          <span>商品专区</span>
        </div>

        <div v-if="showBussinessList">
          <div
            class="page_item"
            v-for="item in marketingPage"
            :key="item.id"
            @click="
              selectcollagePage(
                'customize',
                item.link,
                classificationPage,
                item,
                item.id,
              )
            "
          >
            <span class="page_name2">{{ item.modeName }}</span>

            <span
              class="el-dropdown-link"
              v-if="activePageId && activePageId === item.id + ''"
            >
              <i class="el-icon-edit-outline"></i>
            </span>
          </div>
        </div>

        <!-- 自定义页面 -->
        <div class="tab_item" @click="fold('showPageList')">
          <i
            :class="
              showPageList ? 'el-icon-caret-bottom' : 'el-icon-caret-right'
            "
          ></i>

          <span>自定义页面</span>
        </div>

        <div v-if="showPageList">
          <div
            v-for="item in pageList"
            :key="item.id"
            class="page_item"
            @click="
              selectPage('customize', '/pages/index/index?page=home', item, -1)
            "
            style="width: 280px"
          >
            <GoodNameEdit
              :good-name="item.pageName"
              :good="item"
              @change="updateGoodName"
            />
            <div
              style="font-size: 12px; color: #9999"
              v-if="item.isDef !== '0'"
            >
              首页
            </div>
            <el-dropdown class="page_item_icon">
              <span class="el-dropdown-link">
                <i class="el-icon-more el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <span
                    @click="setHomePage(item, item.pageName)"
                    v-if="item.isDef === '0'"
                    >设为首页</span
                  >
                </el-dropdown-item>
                <el-dropdown-item v-if="item.isDef === '0'">
                  <span @click="delPage(item.id)" class="block">删除</span>
                </el-dropdown-item>
                <el-dropdown-item v-if="item.isDef !== '0'">
                  <span
                    @click="setLinkAddress('/pages/index/index?page=home')"
                    class="block"
                    >页面路径</span
                  >
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>

        <div
          class="add_page_btn"
          @click="addPage()"
          v-if="pageList.length < 10"
        >
          <span>+ 添加页面</span>
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch, Emit } from "vue-property-decorator";
import EdtiorFormData from "./compontents/index/formModel";
import { IControlItem, INavBarPlugin } from "./interfaces/EditorControlPage";
import {
  addAssembly,
  getPageList,
  delPage,
  addPage,
  getJudgeAssembly,
} from "@/api/group/group";
import GoodNameEdit from "@/components/GoodNameEdit2.vue"; // 编辑商品名称
import UserCenterDialog from "./compontents/userCenterDialog/userCenterDialog.vue";
import { getControl, addControl } from "@/api/decoration/decoration";
import {
  IComponentItem,
  IComponent,
  IAddGoodsModeList,
} from "./interfaces/component";
import { getAllRegionList } from "@/api/good/goods";
import { IActivePage, IActivePageItem } from "./interfaces/activePage";
import { IBaseResponse } from "@/libs/axios";
import { ApiSpecArea } from "@/views/goods/marketModel/marketType";
type Test = "showPageList" | "showBussinessList" | "showUserList";
@Component({
  components: {
    GoodNameEdit,
    UserCenterDialog,
  },
})
export default class DecorationComponent extends Vue {
  @Prop()
  templateId!: string;

  @Prop()
  activePageId!: string;

  @Prop()
  components!: IComponent[];

  /** 当前自定义页面 */
  currentItem: IActivePage = {} as IActivePage;

  /** 更换模板弹窗 */
  dialogShow = false;

  /** 判断更改标题新增 */
  activeType = 0;

  /** 展示用户中心列表 */
  showUserList = true;

  /** 展示营销列表 */
  showBussinessList = true;

  /** 展示自定义列表 */
  showPageList = true;

  /** 自定义页面列表 */
  pageList: IActivePageItem[] = [];

  /** 商超分类页 */
  classificationPage: IActivePageItem = {} as IActivePageItem;

  /** 首页页面id匹配首页 */
  homeId: number | string = "";

  /** 获取的首页tab配置，设置时写回去 */
  homePlugin: IControlItem = {} as IControlItem;

  /** 营销页面 */
  marketingPage: Array<ApiSpecArea> = [];

  /** 功能页面选中 */
  get functionpagetype() {
    return this.$STORE.decorationStore.activePageType;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取专区页面
   */
  async getMarketingPage() {
    const res = await getAllRegionList({});
    const data = res.data;
    data.forEach(item => {
      item.link = "/pages/index/index?page=home";
    });
    this.marketingPage = data;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 设为首页
   * @param {number} item
   */

  async setHomePage(item: { id: number }) {
    await this.getControlList();
    this.addOrEditControl(item.id);
    await this.editOrAdd(
      {
        id: item.id,
        isDef: 1,
      },
      3,
    );
    this.getPageList();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 初始获取控件数据
   */

  async getControlList() {
    const res = await getControl({
      templateId: this.$STORE.decorationStore.templateId,
      pluginNameEn: "navBar",
      isDeleted: 0,
    });
    try {
      if (res.code === 200 && res.data.length > 0) {
        this.homePlugin = res.data[0];
        const navBarPlugin = JSON.parse(res.data[0].pluginProperties)[0]
          .formData.menuList;
        if (navBarPlugin && navBarPlugin.length > 0) {
          const navbarHome = navBarPlugin.filter(
            (item: { isHome: any }) => item.isHome,
          );
          const { linkUrl, type, id } = navbarHome[0].linkUrl;
          if (type === 5 && linkUrl === "pages/index/index") {
            this.homeId = id;
          }
        }
      }
    } catch (error) {
      this.$message.warning(`${error}获取数据失败`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 修改/新增控件
   * @param {*} id
   */

  async addOrEditControl(id: number) {
    const { homePlugin } = this;
    let pluginProperties = JSON.parse(homePlugin.pluginProperties)[0];
    let formData = JSON.parse(homePlugin.pluginProperties)[0].formData;
    const navBarPlugin = JSON.parse(homePlugin.pluginProperties)[0].formData
      .menuList;
    this.dealEditControl({
      navBarPlugin,
      id,
    });
    formData = {
      ...formData,
      menuList: navBarPlugin,
    };
    pluginProperties = [
      {
        ...pluginProperties,
        formData,
      },
    ];
    homePlugin.pluginProperties = JSON.stringify(pluginProperties);
    const res = await addControl(homePlugin);
    try {
      if (res.code === 200) {
        this.$message.success("设置首页成功");
      } else {
        this.$message.warning("设置失败");
      }
    } catch (error) {
      this.$message.warning(`${error}获取数据失败`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 处理控件数据
   * @param {INavBarPlugin[]} navBarPlugin
   * @param {string} id
   */

  dealEditControl({
    navBarPlugin,
    id,
  }: {
    navBarPlugin: INavBarPlugin[];
    id: number;
  }) {
    navBarPlugin.map((item: { isHome: any }, i: number) => {
      if (item.isHome) {
        navBarPlugin[i].id = String(id);
        navBarPlugin[i].linkUrl = "pages/index/index";
        navBarPlugin[i].type = 5;
        navBarPlugin[i].linkName = "";
        return {
          ...navBarPlugin,
        };
      }
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 展示用户中心
   */

  showUserCenter() {
    this.dialogShow = true;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 初始获取自定义页面数据
   */

  created() {
    this.getMarketingPage();
    this.getPageList();
    this.getControlList();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取自定义页面数据
   */

  @Watch("templateId")
  async getPageList() {
    const res = await getPageList({
      templateId: this.templateId,
      size: 100,
    });
    try {
      if (res.code === 200) {
        const list: IActivePageItem[] = res.data.list || [];
        this.pageList = this.filterFunctionPage(list);
        if (
          !this.$STORE.decorationStore.firstStatus &&
          !this.$STORE.decorationStore.isUsercenterCompontents &&
          this.activeType !== 1 &&
          res.data.list.length
        ) {
          const activePage = res.data.list.filter(
            (item: { isDef: string }) => item.isDef !== "0",
          )[0];
          // 初始化获取 设为首页的页面id
          const homePageId: string = res.data.list.filter(
            (item: { isDef: string }) => item.isDef !== "0",
          )[0].id;
          this.$STORE.decorationStore.setHomePageId(homePageId);
          this.$STORE.decorationStore.setActivePage(activePage); // 初始化 首页列表
          this.$STORE.decorationStore.setFirstStatus(true); // 初始化用户是不是第一次编辑
        }
      } else {
        this.$message.warning(`获取数据失败，请稍后重试`);
      }
    } catch (e) {
      this.$message.warning(`获取数据失败，请稍后重试`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 过滤功能页面
   * @param {IActivePageItem[]} list
   */

  filterFunctionPage(list: IActivePageItem[]): IActivePageItem[] {
    const temp2 = list.find((i: { type: string }) => i.type === "2");
    if (temp2) this.classificationPage = temp2;
    return list.filter(
      (i: { type: string }) => i.type !== "1" && i.type !== "2",
    );
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 修改/新增自定义页面
   * @param {*} data
   * @param {*} type
   */

  async editOrAdd(
    data: {
      id?: number | string;
      isDef?: number;
      pageName?: string;
      templateId?: string;
    },
    type: number,
  ) {
    const res = await addPage(data);
    try {
      if (res.code === 200) {
        this.activeType = 1;
        if ([1, 2].includes(type))
          this.$message.success(`${type === 1 ? "新增" : "修改"}页面成功`);
        if (type === 1) this.$STORE.decorationStore.setActivePage(res.data);
        this.getPageList();
      }
    } catch (e) {
      this.$message.warning(`编辑自定义页面失败，请稍后重试`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 折叠标签
   * @param {string} v
   */

  fold(v: string) {
    this[`${v}`] = !this[`${v}`];
  }

  /**
   * @LastEditors: chuyinlong
   * @description:新增自定义页面
   */

  addPage() {
    let data = {
      isDef: 0,
      pageName: "自定义页面",
      templateId: this.templateId,
    };
    if (!this.pageList.length) {
      data = {
        isDef: 1,
        pageName: "首页",
        templateId: this.templateId,
      };
    }
    this.editOrAdd(data, 1);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除页面
   * @param {string} id
   */

  async delPage(id: string) {
    const res = await delPage({
      ids: id,
    });
    try {
      if (res.code === 200) {
        this.$message.success(`操作成功`);
        this.activeType = 0;
        this.$STORE.decorationStore.setFirstStatus(false);
        this.getPageList();
      } else {
        this.$message.warning(`删除自定义页面失败，请稍后重试！`);
      }
    } catch (e) {
      this.$message.warning(`删除自定义页面失败，请稍后重试！`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击自定义页面装修
   * @param {string} type
   * @param {string} link
   * @param {IPageitem} item
   */

  async selectPage(type: string, link: string, item: IActivePage) {
    this.currentItem = item;
    // 如果是分类页需要检测是否输入完成
    if (typeof item !== "string" && ["1", "2"].includes(item.type)) return;
    this.$STORE.decorationStore.setActivePageType({
      type,
      link,
    });
    this.$STORE.decorationStore.setActivePage(item);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择商超分类页
   * @param {string} type
   * @param {string} link
   * @param {IActivePageItem} item
   * @param {IAddGoodsModeList} newItem
   */

  async selectcollagePage(
    type: string,
    link: string,
    item: IActivePageItem,
    newItem: IAddGoodsModeList,
  ) {
    // test tip
    if (newItem.id) {
      item.modelId = newItem.id;
    }
    const param = {
      modelId: newItem.id,
    };
    if (newItem.link) {
      getJudgeAssembly(param).then(async res => {
        // return;
        if (res.data.length === 0) {
          const data = {
            isDef: 0,
            pageName: item.name,
            templateId: this.templateId,
            type: item.type,
            modelId: newItem.id,
          };
          const res = (await addPage(data)) as any;
          if (res.code !== 200) return this.$message.error(res.msg);
          const tempdata = {
            icon: "",
            value: "BusinessSuper",
            label: "商超分类页",
          };
          const com = this.getNewComponent(tempdata);
          const addmsg = await this.addNewComponent(com, {
            pageId: res.data.id,
            templateId: res.data.templateId,
          });
          if (addmsg.code !== 200) return this.$message.error(addmsg.msg || "");
          if (item.type === "1") {
          } else if (item.type === "2") {
            this.classificationPage = res.data;
          }
          this.handleAddComponent(tempdata);
          this.$STORE.decorationStore.setActivePage(res.data);
          this.selectPage(type, link, res.data);
        } else {
          this.$STORE.decorationStore.setActivePage(item);
          this.selectPage(type, link, item);
        }
      });
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 默认推入一个
   * @param {IComponentItem}currentComponent
   * @returns {IComponent}
   */

  @Emit("change")
  handleAddComponent(currentComponent: IComponentItem): IComponent {
    currentComponent.type = true; // 专区分类页跳过限制添加组件提示
    return this.getNewComponent(currentComponent);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 返回一个新页面对象
   * @param {IComponentItem}currentComponent
   * @returns {IComponent}
   */

  getNewComponent(currentComponent: IComponentItem): IComponent {
    const Form = new EdtiorFormData();
    const FormData = Form[currentComponent.value];
    return {
      ...currentComponent,
      id: Date.now(),
      formData: new FormData(),
    };
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 手动指定插入一个组件到一个页面
   * @param {IComponent} data
   * @param {{ pageId: string; templateId: string }} option
   * @returns {IBaseResponse<any>}
   */

  async addNewComponent(
    data: IComponent,
    option: { pageId: string; templateId: string },
  ): Promise<IBaseResponse<any>> {
    const properties = JSON.stringify({
      ...data,
    });
    const assembly = {
      properties,
      ...option,
    };
    const res = await addAssembly([assembly]);
    return res;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 修改自定义页面名称
   * @param {string} newName
   */

  updateGoodName(newName: string) {
    let name = newName;
    if (newName.length > 15) {
      this.$message.warning("自定义页面名称最大15个字符！");
      name = newName.substring(0, 15);
    }
    if (typeof this.currentItem === "string") return;
    const param = {
      id: this.currentItem.id,
      pageName: name,
    };
    this.editOrAdd(param, 2);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 设置分享链接
   * @param {string} link
   */

  setLinkAddress(link: string) {
    this.$emit("changeFncMarkPage");
    this.$STORE.decorationStore.setActivePageType({
      link,
    });
  }
}
</script>
<style lang="scss" scoped>
@import "@/assets/styles/decoration/editorControlPage";

.page_item_icon {
  height: 12px;
  margin-bottom: 23px;
}
</style>
