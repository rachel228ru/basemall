/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:52
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:17:28
 */
import {
  VuexModule,
  Module,
  Mutation,
  Action,
  getModule,
} from "vuex-module-decorators";
import store from "@/store";
import { EditorState, ComponentItemType } from "../modulesType/editorType";
const banner = {
  type: "banner",
  id: 1,
  banners: [
    {
      img:
        "http://thirdwx.qlogo.cn/mmopen/vi_32/qh1wmJHk0LMSnj3cVT7QlRbNJNoyianTia7LcQYTniclDHuTer7cqXEicg3Wg8BXRLPR5BzRKSM8ibvct3PBrnciakrA/132",
      link: "",
      title: "测试",
    },
  ],
  padding: 0,
  imageStyle: "custom",
  angle: "custom",
  indicator: "1",
};
@Module({ dynamic: true, store, namespaced: true, name: "editorStore" })
export class EditorStore extends VuexModule implements EditorState {
  public components = [banner];

  public currentComponent = "";

  @Mutation
  private SET_COMPONENTS(value: Array<ComponentItemType>) {
    this.components = value;
  }

  @Mutation
  private SET_CURRENT_COMPNENT(value: string) {
    this.currentComponent = value;
  }
  /**
   * @LastEditors: vikingShip
   * @description: 添加元素
   */

  @Action
  public addComponent(component: ComponentItemType = banner) {
    this.components = this.components.concat(
      Object.assign({}, component, { id: +new Date() }),
    );
  }
  /**
   * @LastEditors: vikingShip
   * @description: 更新元素
   */

  @Action
  public updateComponent(component: ComponentItemType) {
    const list = this.components.map(item =>
      item.id === component.id ? component : item,
    );
    this.SET_COMPONENTS(list);
  }
}

export const editorStore = getModule(EditorStore);
