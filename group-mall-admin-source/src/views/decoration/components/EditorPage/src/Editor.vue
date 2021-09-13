<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:39:39
-->
<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 19:20:54
-->
<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-21 13:07:36
-->
<template>
  <!-- 入口页面 -->
  <div class="editorWrapper">
    <ModuleDialog :dialogShow.sync="dialogShow" />
    <div class="editorPage">
      <editorTab @change="selectTab" />
      <div class="editorPage_right">
        <div class="editorPage_right_wrap">
          <!--头部编辑保存部分Start-->
          <div class="wraper_top">
            <router-link to="/overview">
              <div class="editor_tab_title_top">
                <span class="editor_tab_icon1"></span>
                <div>店铺后台</div>
              </div>
            </router-link>
            <div class="editor_tab_title_top" @click="changeComponent">
              <span class="editor_tab_icon2"></span>
              <span class="change_text">更换模板</span>
            </div>
            <div class="editor_tab_title_top no-l-b">
              <span id="savetip">
                {{ !isSaveTip ? "所有更改已保存" : "正在保存..." }}
              </span>
            </div>
            <div slot="footer" class="dec_footer">
              <el-button
                type="primary"
                v-show="currentTab == 0 || currentTab == 2"
                @click="submit()"
                >保存</el-button
              >
              <el-button
                type="primary"
                v-show="currentTab == 1"
                @click="submit2"
                >保存控件</el-button
              >
            </div>
          </div>

          <div class="tab_con">
            <!-- 组件 -->
            <EditorComponent v-if="currentTab == 0" @change="addComponents" />
            <!-- 控件 -->
            <EditorControl
              v-if="currentTab == 1"
              @saveControlId="saveControlId"
              @change="addComponents2"
            />
            <!-- 页面 -->
            <EditorControlPage
              ref="editorControlPage"
              @change="addComponents"
              @changePage="selectPage"
              :templateId="templateId"
              @changeFncMarkPage="changeFncMarkPage"
              v-show="currentTab == 2 && !isUsercenterCompontents"
              :activePageId="activePage ? activePage.modelId : ''"
            />
            <UserCenter
              v-if="
                currentTab == 2 &&
                  isUsercenterCompontents &&
                  userCenterType === 'usercenter'
              "
              ref="usercenter"
              @userCenterChang="userCenterChang"
            >
              <EditorControlPage
                ref="editorControlPage"
                v-if="currentTab == 2"
                @change="addComponents"
                @changePage="selectPage"
                :templateId="templateId"
                :activePageId="activePage ? activePage.modelId : ''"
                @changeFncMarkPage="changeFncMarkPage"
                slot="EditorControlPage"
                :components.sync="components"
              />
            </UserCenter>
            <!-- <EditorControlPage
              ref="editorControlPage"
              v-if="currentTab == 2"
              @change="addComponents"
              @changePage="selectPage"
              :templateId="templateId"
              @changeFncMarkPage="changeFncMarkPage"
              slot="EditorControlPage"
              :activePageId="activePage?activePage.modelId:''"
            /> -->
            <!-- </BusinessSuper> -->
            <!-- 组件预览 -->
            <EditorPreview
              @settingEmpty="settingEmpty"
              ref="EditorPreview"
              v-show="
                !isUsercenterCompontents && (currentTab == 0 || currentTab == 2)
              "
              @change="setCurrentComponent"
              :components.sync="components"
            />
            <!-- 控件预览 -->
            <EditorPreview
              v-show="currentTab == 1"
              @change="setCurrentComponent"
              :components.sync="components2"
            />
            <!-- 样式编辑 -->
            <EditorForm
              v-show="!isUsercenterCompontents && pageType == 0"
              :current-component="currentComponent"
              @change="changeData"
              :templateId="templateId"
            />
            <!-- 编辑链接地址 -->
            <EditorLinkAddress
              v-show="!isUsercenterCompontents && pageType == 1"
            />
          </div>
          <!--End-->
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { addAssembly, getAssembly, addPage } from "@/api/group/group";
import { IComponent, IComponentStr } from "./interfaces/component";
import { Vue, Component, Watch, Ref } from "vue-property-decorator";
import EditorComponent from "./EditorComponent.vue";
import EditorControl from "./EditorControl.vue";
import EditorControlPage from "./EditorControlPage.vue";
import EditorPreview from "./EditorPreview.vue";
import EditorForm from "./EditorForm.vue";
import EditorTab from "./EditorTab.vue";
import ModuleDialog from "./compontents/moduleDialog/moduleDialog.vue";
import {
  addControl,
  addControlMore,
  getControl,
  editControls,
} from "@/api/decoration/decoration";
import EditorLinkAddress from "./EditorLinkAddress.vue";
import { addTemplate, getTemplate } from "@/api/decoration/decoration";
import UserCenter from "@/views/decoration/UserCenter.vue";
import CubeBox, { IBanners } from "./compontents/CubeBox/CubeBox";
import StoreNavigation from "./compontents/StoreNavigation/StoreNavigation";
import { IControlItem } from "./interfaces/EditorControlPage";
import BusinessSuper from "../../BusinessSuper/BusinessSuper";
import { IActivePage } from "./interfaces/activePage";
@Component({
  components: {
    EditorComponent,
    EditorControl,
    EditorControlPage,
    EditorPreview,
    EditorForm,
    EditorTab,
    ModuleDialog,
    EditorLinkAddress,
    UserCenter,
  },
})
export default class Editor extends Vue {
  /** 更换模板弹窗 */
  dialogShow = false;

  /** 当前编辑的组件 */
  currentComponent: IComponent = {} as IComponent;

  /** 组件列表 */
  components: IComponent[] = [];

  /** 控件列表 */
  components2: IComponent[] = [];

  /** 当前编辑的控件 */
  currentComponent2: IComponent = {} as IComponent;

  /** 左侧导航栏num */
  currentTab = 0;

  /** 当前自定义页面id */
  pageId = "";

  /** */
  saveTemplateId = "";

  /** 模板id */
  templateId = "";

  /** 原来的组件ids */
  assembleIds: string[] = [];

  /** 页面类型 默认自定义页面 0 */
  pageType = 0;

  /** 保存提示 */
  isSaveTip = false;

  controlItem: IControlItem = {} as IControlItem;

  controlIndex = 0;

  controlList: IControlItem[] = [];

  @Ref() readonly EditorPreview!: EditorPreview;

  @Ref() readonly editorControlPage!: EditorControlPage;

  @Ref() usercenter!: UserCenter;

  get currentTemplateId() {
    return this.$STORE.decorationStore.templateId;
  }

  get activePage() {
    return this.$STORE.decorationStore.activePage;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取当前页面操作栏目
   */

  get activePageType() {
    return this.$STORE.decorationStore.activePageType;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 判断是不是用户中心
   */

  get isUsercenterCompontents() {
    return this.$STORE.decorationStore.isUsercenterCompontents;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 判断用户是否点击了不同的分类页
   */

  get saleMode() {
    return this.$STORE.decorationStore.saleMode;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取功能页面类型
   */

  get userCenterType() {
    return this.$STORE.decorationStore.userCenterType;
  }

  @Watch("currentTemplateId")
  handleTemplateIdChange() {
    this.templateId = this.currentTemplateId;
  }

  @Watch("currentTab")
  handleTab() {
    if (this.currentTab === 2) {
      // 获取页面中营销页面数据
      this.editorControlPage.getMarketingPage();
    }
  }

  /**
   * 页面初始化
   */
  /**
   * @LastEditors: chuyinlong
   * @description: 页面初始化
   * @param {*} id
   */

  init(id: string) {
    this.components = [];
    this.currentComponent = {} as IComponent;
    this.templateId = id;
    this.$STORE.decorationStore.setTempLated(id);
    this.$STORE.decorationStore.updateSaveTemplateId(id);
    this.pageId = "";
    this.getControl(id);
  }

  created() {
    this.getTemplateList();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 清空setting设置
   */

  settingEmpty() {
    this.currentComponent = {} as IComponent;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取模板列表
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
          this.init(res.data[0].id);
        } else {
          /** 否则新建一个非默认模板 */
          this.creatNotDefTem();
        }
      } else {
        this.$message.warning(`获取数据失败`);
      }
    } catch (e) {
      this.$message.error(`获取模版失败，请稍后重试！`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 新建非默认模板并赋值到当前模板list
   */

  async creatNotDefTem() {
    const params = { isDevTemplate: 0, onlineStatus: 1 };
    try {
      const res = await addTemplate(params);
      if (res.code === 200) {
        this.init(res.data.id);
      }
    } catch (e) {
      this.$message.error(`创建模版失败，请稍后重试！`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 保存当前模版为默认模版
   * @param {string} id
   */

  async setOnlineTemplate(id: string) {
    const params = {
      id,
      isDevTemplate: 0,
      onlineStatus: 1,
    };
    try {
      const res = await addTemplate(params);
      this.isSaveTip = false;
      if (res.code === 200) {
        this.$STORE.decorationStore.updateSaveTemplateId(id);
      }
    } catch (e) {
      this.$message.error(`设置默认模版失败，请稍后重试！`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取默认模板里的控件
   * @param {string} templateId
   * @returns {*}
   */

  async getDefaultControl(templateId: string): Promise<any> {
    const res = await getControl({ templateId });
    if (res.code === 200) {
      return res.data;
    } else {
      this.$message.error(`获取控件失败，请稍后重试！`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 初始化获取控件列表
   * 如果列表没数据，使用前端维护的默认控件及配置，如果有数据则不做处理
   * @param {string} templateId
   */

  async getControl(templateId: string) {
    const data = [
      {
        id: 12,
        templateId: this.$STORE.decorationStore.saveTemplateId,
        pluginProperties: JSON.stringify(this.components2),
        pluginNameCn: "底部导航",
        pluginNameEn: "navBar",
      },
    ];
    try {
      const res = await getControl({ templateId });
      if (res.data.length === 0) {
        this.addControlMore(data);
      }
    } catch (e) {
      console.log(e);
      // this.$message.error(`获取控件列表失败，请稍后重试！`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 新增控件
   * @param {array} data
   */

  async addControlMore(
    data: {
      id: number;
      templateId: string;
      pluginProperties: string;
      pluginNameCn: string;
      pluginNameEn: string;
    }[],
  ) {
    const res = await addControlMore(data);
    try {
      if (res.code === 200) {
        this.$message.success(`操作成功！`);
      }
    } catch (e) {
      this.$message.error(`新增控件失败，请稍后重试！`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 保存自定义页面
   * @param {number} nomessage
   */

  async submit(nomessage: number) {
    // { chanel }
    this.isSaveTip = true;
    if (this.isUsercenterCompontents) {
      this.usercenter.submit();
      return;
    }
    const defaultTemplateId = this.$STORE.decorationStore.templateId;
    const saveTemplateId = this.$STORE.decorationStore.saveTemplateId;
    // 删除原先模板里的控件
    if (defaultTemplateId !== saveTemplateId) {
      await this.deleteTemplateControl({ defaultTemplateId });
    }
    // 如果设置的模版不是默认模版
    if (defaultTemplateId !== saveTemplateId) {
      this.confirmTempalate({
        defaultTemplateId,
        saveTemplateId,
      });
    } else {
      // if (!this.pageId) return this.$message.warning(`请先选择自定义页面！`);
      // 格式话保存数据
      const data = this.formateComponents({ defaultTemplateId }) || [];
      if (this.$STORE.decorationStore.activePageType === "bussiness") {
        this.isSaveTip = false;
        return;
      }
      if (data.length && this.isEmpetyCubox(data).length) {
        this.isSaveTip = false;
        return;
      }
      if (data.length && !this.isNavLength4(data)) return;
      this.addNewPage(data, 2, nomessage);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 确认是否使用当前模版为默认模版
   * @param {string} defaultTemplateId
   * @param {string} saveTemplateId
   */

  confirmTempalate({
    defaultTemplateId,
    saveTemplateId,
  }: {
    defaultTemplateId: string;
    saveTemplateId: string;
  }) {
    this.$confirm("是否保存当前模版为默认模版", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(() => {
        this.setOnlineTemplate(defaultTemplateId);
      })
      .catch(() => {
        this.isSaveTip = false;
        this.$STORE.decorationStore.setFirstStatus(false);
        this.$STORE.decorationStore.setTempLated(saveTemplateId);
        this.$message.info("已取消");
      });
  }

  /**
   * @LastEditors: chuyinlong
   * @description:删除原先模板里的控件
   * @param {string} defaultTemplateId
   */

  async deleteTemplateControl({
    defaultTemplateId,
  }: {
    defaultTemplateId: string;
  }) {
    const ids = await this.getTempControlList();
    if (ids.length > 0) {
      this.addOrEditControl(
        this.checkIsdefaultTemp(),
        this.checkType(),
        await this.getDefaultControl(defaultTemplateId),
      );
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 格式化保存数据
   * @param {string} defaultTemplateId
   * @returns {IComponentStr[]}
   */

  formateComponents({
    defaultTemplateId,
  }: {
    defaultTemplateId: string;
  }): IComponentStr[] {
    const data: IComponentStr[] = [];
    this.components.forEach(item => {
      const properties = JSON.stringify({ ...item });
      const assembly = {
        properties,
        pageId: this.pageId,
        templateId: defaultTemplateId,
        modelId: this.saleMode ? this.saleMode : null,
      };
      data.push(assembly);
    });
    return data;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 判断魔方组件 图片是不是传完整
   * @param {IComponentStr[]} coms
   * @returns {any[]}
   */

  isEmpetyCubox(coms: IComponentStr[]): IBanners[] {
    for (let index = 0; index < coms.length; index++) {
      const item = coms[index];
      if (item.properties) {
        const properties: IComponent = JSON.parse(item.properties);
        if (
          properties.value === "CubeBox" &&
          properties.formData instanceof CubeBox &&
          properties.formData.subEntry.length
        ) {
          const arr = properties.formData.subEntry.filter(
            (item: { img: any }) => !item.img,
          );
          if (arr.length) {
            this.$message.warning(
              "该页面使用了魔方组建,蓝色区域请添加完整的图片",
            );
            (document.querySelector(
              `#editor-preview-com-${index}`,
            ) as any).click();
            return arr;
          }
        }
      }
    }
    return [];
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 判断店铺导航 至少4个
   * @param {IComponentStr[]} coms
   * @returns {boolean}
   */

  isNavLength4(coms: IComponentStr[]): boolean {
    let flag = true;
    for (let index = 0; index < coms.length; index++) {
      const item = coms[index];
      if (item.properties) {
        const properties: IComponent = JSON.parse(item.properties);
        if (
          properties.value === "StoreNavigation" &&
          properties.formData instanceof StoreNavigation &&
          properties.formData.storeNavigations.length < 4
        ) {
          this.$message.warning("店铺导航最少添加4个");
          flag = false;
          break;
        }
      }
    }
    return flag;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 判断是当前模板还是使用装修模板
   * @returns {Promise<string>}
   */

  async checkCurrentTemp(): Promise<string> {
    let pageId = "";
    const check =
      this.$STORE.decorationStore.saveTemplateId ===
      this.$STORE.decorationStore.templateId;
    if (!check) {
      const data = {
        isDef: 1,
        pageName: this.$STORE.decorationStore.pageName,
        templateId: this.$STORE.decorationStore.saveTemplateId,
      };
      const res = await addPage(data);
      if (res.code === 200) {
        pageId = res.data.id;
      } else {
        console.log(res);
      }
      return pageId;
    }
    return "";
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 检查是否默认模板
   * @returns {boolean}
   */

  checkIsdefaultTemp(): boolean {
    let isdefaultTemp = true;
    if (
      this.$STORE.decorationStore.templateId ===
      this.$STORE.decorationStore.saveTemplateId
    ) {
      isdefaultTemp = false;
    }
    return isdefaultTemp;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 是保存新模板还是修改默认模板
   * @returns {1|0}
   */

  checkType(): 1 | 0 {
    const type = this.checkIsdefaultTemp() ? 1 : 0;
    return type;
  }

  /**
   * @LastEditors: chuyinlong
   * @description:
   * @param {boolean} isdefaultTemp
   * @param {number} type
   * @param {IControlItem[]} controlList
   */
  async addOrEditControl(
    isdefaultTemp: boolean,
    type: number,
    controlList: IControlItem[],
  ) {
    const controlItem = this.controlItem || controlList[0];
    const controlIndex = this.controlIndex;
    const data = {
      ...controlItem,
      pluginProperties: JSON.stringify(this.components2),
      templateId: this.$STORE.decorationStore.templateId,
    };
    if (isdefaultTemp) {
      await this.saveDefaultTemplate({ controlIndex, controlList, type });
    } else {
      if (data.pluginNameEn == "navBar" && data.pluginProperties !== "[]") {
        // 始终保持底部导航第一位置为首页 路径为 /pages/index/index
        const pluginProperties = JSON.parse(data.pluginProperties);
        pluginProperties[0].formData.menuList.forEach(
          (item: { getFrom: string }) =>
            (item.getFrom = this.$STORE.decorationStore.getFrom),
        );
        pluginProperties[0].formData.menuList[0].linkUrl = "/pages/index/index";
        data.pluginProperties = JSON.stringify(pluginProperties);
      }
      const res = await addControl(data);
      try {
        if (res.code === 200) {
          this.$message.success(`${type === 1 ? "新增" : "修改"}控件成功`);
        }
      } catch (e) {
        this.$message.warning(`${type === 1 ? "新增" : "修改"}控件失败`);
      }
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 保存默认模版
   * @param {number} controlIndex
   * @param {IControlItem[]} controlList
   * @param {number} type
   */

  async saveDefaultTemplate({
    controlIndex,
    controlList,
    type,
  }: {
    controlIndex: number;
    controlList: IControlItem[];
    type: number;
  }) {
    const saveTemplateId = this.$STORE.decorationStore.saveTemplateId;
    controlList[controlIndex].pluginProperties = JSON.stringify(
      this.components2,
    );
    controlList.forEach(
      (item: { templateId: string }) => (item.templateId = saveTemplateId),
    );
    const res = await editControls(controlList);
    try {
      if (res.code === 200) {
        this.$message.success(`${type === 1 ? "新增" : "修改"}控件成功`);
      }
    } catch (e) {
      this.$message.warning(`${type === 1 ? "新增" : "修改"}控件失败`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击控件时拿到控件信息
   * @param {IControlItem} item
   * @param {number} index
   */

  saveControlId({ item, index }: { item: IControlItem; index: number }) {
    this.$STORE.decorationStore.setLinkForm("bottomNav");
    this.controlItem = item;
    this.controlIndex = index;
  }

  /**
   * @LastEditors: chuyinlong
   * @description:使用默认模板时获取原先模板中的数据
   * @returns {Promise<string>}
   */

  async getTempControlList(): Promise<string> {
    const data = {
      templateId: this.$STORE.decorationStore.templateId,
    };
    const res = await getControl(data);
    if (res.code === 200) {
      return res.data.map((o: { id: string }) => {
        return o.id;
      });
    } else {
      this.$message.warning(`获取数据失败`);
      return "";
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取当前templateId控件数据
   */

  async getControlList() {
    const res = await getControl({
      templateId: this.$STORE.decorationStore.templateId,
    });
    if (res.code === 200) {
      this.controlList = res.data;
      this.addOrEditControl(true, 2, res.data);
    } else {
      this.$message.error(`获取数据失败，请稍后重试！`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 判断正在使用中的模板是不是templateID
   * 模板是否使用中 0 否, 1 是 onlineStatus
   */

  async getTemplateLists() {
    const res = await getTemplate({ onlineStatus: 1 });
    try {
      if (res.code === 200) {
        /**  获取非默认模板（有即为正在使用中的模板） */
        if (res.data.length > 0) {
          const onlineTemp: string = res.data[0].id;
          if (this.$STORE.decorationStore.templateId === onlineTemp) {
            this.addOrEditControl(false, 2, []);
          } else {
            /**  templateId为默认模板的控件配置saveTemplate，然后复制保存当前修改，更新store里的tmplateid */
            this.getControlList();
          }
        }
      } else {
        this.$message.error(`获取数据失败，请稍后重试！`);
      }
    } catch (e) {
      this.$message.error(`获取数据失败，请稍后重试！`);
    }
  }

  /**
   * 1.如果当前模板是 使用中的模板调用addOrEditControl直接修改，
   * 2.如果当前模板是默认模板，需要将当前模板控件复制出，然后保存当前修改到saveTemplate，更新store里的tmplateid
   */
  /**
   * @LastEditors: chuyinlong
   * @description: 1.如果当前模板是 使用中的模板调用addOrEditControl直接修改，
   * 2.如果当前模板是默认模板，需要将当前模板控件复制出，然后保存当前修改到saveTemplate，更新store里的tmplateid
   */

  async submit2() {
    this.getTemplateLists();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 新增页面
   * @param {IComponentStr[]} data
   * @param {number} type
   * @param {number} nomessage
   */

  async addNewPage(data: IComponentStr[], type: number, nomessage: number) {
    // 说明点击保存
    if (typeof nomessage === "object") nomessage = 0;
    if (!data.length && !nomessage) {
      this.$message.warning(`当前页面组件为空，请添加组件进行保存`);
      return;
    }
    if (!data.length) {
      this.isSaveTip = false;
      return;
    } else if (!this.checkClassPage(JSON.parse(data[0].properties || "{}"))) {
      // 分类页校验输入是否完成
      this.isSaveTip = false;
      return;
    }
    const res = await addAssembly(data);
    this.isSaveTip = false;
    if (res.code === 200 && !nomessage) {
      this.$message.success(`${type === 1 ? "新增" : "修改"}页面成功`);
    } else {
      this.$message.warning(`${res.msg}操作失败`);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 校验分类页输入是否完成
   * @param {{ value: string; formData: BusinessSuper }} data
   * @returns {boolean}
   */

  checkClassPage(data: { value: string; formData: BusinessSuper }): boolean {
    let flag = true;
    if (["BusinessSuper"].includes(data.value)) {
      const formData: BusinessSuper = data.formData;
      const { firstCatList = [], selectMode, selectType } = formData;
      let errMsg = "";
      if (selectMode === 2 && selectType === 2) {
        firstCatList.forEach(
          (i: { classChart: string | any[]; category: { name: any } }) => {
            if (!i.classChart || !i.classChart.length) {
              errMsg = `${i.category.name}一级分类下未添加分类图，请添加！`;
              flag = false;
            }
          },
        );
      }
      if (!flag) {
        this.$message.error(errMsg);
      }
    }
    return flag;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 新增组件
   * @param {IComponent} com
   */

  addComponents(com: IComponent) {
    if (
      this.components.length > 0 &&
      ["BusinessSuper"].includes(this.components[0].value)
    ) {
      this.components = this.components.splice(0, 1);
      if (com.type) {
        return;
      } else {
        return this.$message.error("分类页不能添加组件！");
      }
    }
    if (this.activePageType === "bussiness") {
      return this.$message.warning("营销页面无法添加组件");
    } else {
      if (com) {
        this.components.push(com);
      }
      this.currentComponent = com;
      const i = this.components.length - 1;
      this.EditorPreview.setCurrentFlag(i);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 新增控件
   * @param {IComponent} com
   */

  addComponents2(com: IComponent) {
    this.components2 = [];
    this.components2.push(com);
    this.currentComponent = com;
  }

  @Watch("saleMode")
  pickPage() {
    const activePage = this.activePage;
    if (typeof activePage === "object") {
      this.getPageAssembly(activePage.id); //  获取页面数据
    }
  }

  @Watch("activePage")
  /** 获取当前自定义页面的id */
  selectPage(item: IActivePage) {
    if (this.isUsercenterCompontents) return;
    if (!!item) {
      if (typeof this.activePage === "string") return;
      item = this.activePage;
      const id = item.id;
      const name = item.pageName;
      this.pageType = 0;
      this.pageId = id;
      this.currentComponent = {} as IComponent;
      this.getPageAssembly(id); //  获取页面数据
      const checkTempId = this.$STORE.decorationStore.templateId;
      if (checkTempId.length > 0) {
        this.$STORE.decorationStore.updatePageName(name);
      }
    } else {
      this.isSaveTip = false;
      this.components = [];
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取页面组件
   * @param {string} id
   */

  async getPageAssembly(id: string) {
    const res = await getAssembly({
      pageId: id,
      templateId: this.templateId,
      modelId: this.saleMode ? this.saleMode : null,
    });
    try {
      this.isSaveTip = false;
      if (res.code === 200) {
        const newList: IComponent[] = [];
        this.assembleIds = [];
        res.data.forEach((item: { properties: string; id: string }) => {
          newList.push(JSON.parse(item.properties));
          this.assembleIds.push(item.id);
        });
        this.components = newList;
      }
    } catch (error) {
      this.$message.error("获取页面组件失败，请稍后重试！");
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击选择需要编辑组件
   * @param {IComponent} com
   */

  setCurrentComponent(com: IComponent) {
    this.pageType = 0;
    // this.editorControlPage.getMarketingPage();
    const saleMode = this.$STORE.decorationStore.saleMode;
    if (saleMode && Number(saleMode) > 0) {
      const selectItem = this.editorControlPage.marketingPage.filter(
        item => item.id === saleMode,
      );
      com.label = selectItem[0].modeName;
    }
    this.currentComponent = com;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击左侧导航栏
   * @param {number} currentTab
   */

  selectTab(currentTab: number) {
    this.currentTab = currentTab;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 组件编辑
   * @param {*} data
   */
  changeData(data: any) {
    this.currentComponent.formData = data;
  }

  /**
   * @LastEditors: chuyinlong
   * @description:更换模板
   */

  changeComponent() {
    this.dialogShow = true;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 切换功能页面和营销页面
   */

  changeFncMarkPage() {
    this.pageType = 1;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 初始化用户是不是第一次编辑
   */

  destroyed() {
    this.$STORE.decorationStore.setFirstStatus(false);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 个人中心页面点击保存
   * @param {boolean} e
   */
  userCenterChang(e: boolean) {
    this.isSaveTip = e;
  }
}
</script>

<style lang="scss" scope>
@import "@/assets/styles/decoration/editorPage";
#editor__preview_position {
  position: relative;
  padding-bottom: 15px;
  overflow: auto;
  width: 375px;
  height: calc(100vh - 80px);
  border: 1px solid #ccc;
  background-color: #fff;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  margin-bottom: 100px;
  visibility: hidden;
}
#editor__from_position {
  width: 435px;
  height: 667px;
  overflow-y: scroll;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  padding-bottom: 50px;
}
/**新的编辑样式**/
.editorPage_right_wrap {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
div::-webkit-scrollbar {
  width: 0 !important;
  height: 0 !important;
}
.editor__preview {
  height: calc(100vh - 80px) !important;
  border: 1px solid #dcdfe6 !important;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.12), 0 0 6px 0 rgba(0, 0, 0, 0.04);
}

#savetip {
  font-size: 14px;
  color: #888;
  margin-left: 6px;
}
.no-l-b {
  border-right: none !important;
}
</style>
