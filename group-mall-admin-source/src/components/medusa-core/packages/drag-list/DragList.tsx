/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:12:13
 */
import { Vue, Component, Prop, Watch, PropSync } from "vue-property-decorator";
import "../styles/m-drag.scss";

@Component
export default class MDragList extends Vue {
  static componentName = "MDragList";

  @PropSync("data", {
    type: Array,
    default() {
      return [];
    },
  })
  list: any[];

  @PropSync("defaultOpen", {
    type: Array,
    default: () => {
      return [];
    },
  })
  open: any[];

  @Prop({
    type: String,
    default: "",
  })
  childKey: string;

  @Prop({
    type: Object,
    default: () => {
      return {};
    },
  })
  options: any;

  onMove() {
    this.$emit("onMove");
  }

  render(h) {
    return (
      <div class="drag__list"> 
        <m-draggable
          vModel={this.list}
          options={this.options}
          move={() => this.onMove()}
        >
          {this.list.map((itemData, i) => {
            return (
              <el-collapse vModel={this.open}>
                <el-collapse-item
                  name={i}
                  disabled={
                    itemData[this.childKey] && !itemData[this.childKey].length
                  }
                >
                  <div class="drag__list--item" slot="title">
                    {this.$scopedSlots.item({ itemData, index: i })}
                  </div>

                  <m-draggable
                    vModel={this.list[i][this.childKey]}
                    move={() => this.onMove()}
                  >
                    {itemData[this.childKey].map((childData, index) => {
                      return this.$scopedSlots.child({
                        itemData,
                        childData,
                        parentIndex: i,
                        index,
                      });
                    })}
                  </m-draggable>
                </el-collapse-item>
              </el-collapse>
            );
          })}
        </m-draggable>
      </div>
    );
  }
}
