/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:43:39
 */
import {
  VuexModule,
  Module,
  Mutation,
  Action,
  getModule,
} from "vuex-module-decorators";
import store from "@/store";
import { DecorationState } from "@/store/modulesType/decorationType";
import { copyTemplate } from "@/api/decoration/decoration";
import { Message } from "element-ui";
import { IActivePage } from "@/views/decoration/components/EditorPage/src/interfaces/activePage";
@Module({ dynamic: true, store, namespaced: true, name: "decorationStore" })
export class DecorationStore extends VuexModule implements DecorationState {
  public templateId = "";

  public saveTemplateId = "";

  public pageName = "";

  public activePage = {} as IActivePage;

  public activeComIndex = 0;

  public homePageId = "";

  public activePageType = "customize";

  public copyLink = "/pages/index/index?page=home";

  public isUsercenterCompontents = false;

  public userCenterType = "";

  public firstStatus = false;

  public activeTab = 0;

  public getFrom = "";

  public saleMode = "";

  public isCustom = false;

  /**
   * @LastEditors: vikingShip
   * @description: 实时更新首页id
   * @param pageId
   */

  @Mutation
  private SET_HOME_PAGE_ID(pageId: number | string) {
    this.homePageId = String(pageId);
  }
  /**
   * @LastEditors: vikingShip
   * @description: 实时更新获取到链接来源
   */

  @Mutation
  private SET_LINK_FORM(form: string) {
    this.getFrom = form;
  }
  /**
   * @LastEditors: vikingShip
   * @description: 更改临时模板id
   * @param value 模板ID
   */

  @Mutation
  private SET_TEMPLATEID(value: string) {
    this.templateId = value;
  }

  /**
   * @LastEditors: vikingShip
   * @description: 判断用户是不是第一次进入编辑页面
   * @param firstStatus 标识
   */

  @Mutation
  private SET_FIRSTSTATUS(value: boolean) {
    this.firstStatus = value;
  }

  /**
   * @LastEditors: vikingShip
   * @description: 更改最终保存的模板id
   * @param value
   */

  @Mutation
  private SET_SAVETEMPLATEID(value: string) {
    this.saveTemplateId = value;
  }

  /**
   * @LastEditors: vikingShip
   * @description: 使用装修模板时的自定义页面名称
   * @param value pageName
   */
  @Mutation
  private SET_PAGE_NAME(value: string) {
    this.pageName = value;
  }

  /**
   * @LastEditors: vikingShip
   * @description: 设置当前页面预览对象
   * @param value 当前选中page
   */
  @Mutation
  private SET_ACTIVEPAGE(value: IActivePage) {
    if (!value) return;
    if (typeof value === "string" && ["usercenter"].includes(value)) {
      this.isUsercenterCompontents = true;
      this.userCenterType = value;
      this.isCustom = false;
      this.activePage = {} as IActivePage;
    } else if (typeof value === "object") {
      this.activePage = value;
      this.saleMode = String(value.modelId ? value.modelId : "");
      this.isUsercenterCompontents = false;
      this.userCenterType = "";

      if (value.modelId) {
        this.isCustom = true;
      }
    }
  }
  /**
   * @LastEditors: vikingShip
   * @description:
   * @param {*}
   * @returns {*}
   */

  @Mutation
  private SET_ISUSERCENTER(value: number) {
    this.isUsercenterCompontents = false;
    this.userCenterType = "";
    this.activeTab = value;
  }
  /**
   * @LastEditors: vikingShip
   * @description: 设置选中链接
   */

  @Mutation
  private SET_ACTIVEPAGETYPE(value: { type?: string; link?: string }) {
    const { type, link } = value;
    this.activePageType = type || "";
    this.copyLink = link || "";
  }
  /**
   * @LastEditors: vikingShip
   * @description: 设置选中下标
   */

  @Mutation
  private SET_ACTIVECOMINDEX(value: number) {
    this.activeComIndex = value;
  }
  /**
   * @LastEditors: vikingShip
   * @description: 更改临时模板id
   */

  @Action
  public updateTemplateId(templateId: string | number) {
    copyTemplate({ id: templateId }).then(res => {
      if (res.code === 200) {
        this.SET_TEMPLATEID(res.data.id);
        // this.SET_ACTIVEPAGE(null);
      } else {
        Message({
          message: res.msg || "",
        });
      }
    });
  }

  /**
   * @LastEditors: vikingShip
   * @description: 更改最终保存的模板id
   */

  @Action
  public updateSaveTemplateId(value: string) {
    this.SET_SAVETEMPLATEID(value);
  }
  /**
   * @LastEditors: vikingShip
   * @description:更改自定义页面名称
   */

  @Action
  public updatePageName(value: string) {
    this.SET_PAGE_NAME(value);
  }

  @Action
  public setLinkForm(value: string) {
    this.SET_LINK_FORM(value);
  }

  @Action
  public setFirstStatus(value: boolean) {
    this.SET_FIRSTSTATUS(value);
  }

  @Action
  public setTempLated(value: string) {
    this.SET_TEMPLATEID(value);
  }

  @Action
  public setHomePageId(value: string | number) {
    this.SET_HOME_PAGE_ID(value);
  }

  @Action
  public setActivePage(value: IActivePage) {
    this.SET_ACTIVEPAGE(value);
  }

  @Action
  public setActivePageType(value: { type?: string; link?: string }) {
    this.SET_ACTIVEPAGETYPE(value);
  }

  @Action
  public setActiveComIndex(value: number) {
    this.SET_ACTIVECOMINDEX(value);
  }

  @Action
  public setIsuercenter(value: number) {
    this.SET_ISUSERCENTER(value);
  }
}

export const decorationStore = getModule(DecorationStore);
