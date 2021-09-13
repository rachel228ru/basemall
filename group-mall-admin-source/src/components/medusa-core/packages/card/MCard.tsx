/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:11:55
 */
import { Vue, Component, Prop } from "vue-property-decorator";
import "../styles/m-card.scss";

@Component
export default class MCard extends Vue {
  static componentName = "MCard";

  /** 是否需要缩放按钮 */
  @Prop({ type: Boolean, default: false })
  needToggle: boolean;

  @Prop({ default: "收起搜索条件" })
  showText: string;

  @Prop({ default: "展开搜索条件" })
  hideText: string;

  // @Prop({ type: Boolean, default: false })
  visible: Boolean = false;

  /** 切换展开状态 */
  toggleVisible() {
    this.visible = !this.visible;
  }

  render(h) {
    return (
      <div
        class={[
          "m__card",
          !this.needToggle ? "show" : this.visible ? "show" : "hide",
        ]}
      >
        {this.$slots.default} 
        {this.needToggle && (
          <div class="m__card--btn" onClick={() => this.toggleVisible()}>
            <span>
              {this.visible ? this.showText : this.hideText}
              <i
                class={`iconfont  ${
                  !this.visible
                    ? "iconxiajiantou-tianchong"
                    : "iconxiangshangyuanjiantoushangjiantouxiangshangmianxing"
                }`}
              ></i>
            </span>
          </div>
        )}
      </div>
    );
  }
}
