import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import { VNode } from "vue";
import cloneDeep from "lodash/cloneDeep";
import MTableRow from "./MTableRow.vue";

@Component
export default class MTableBody extends Vue {
  static componentName = "MTableBody";

  /** 是否全选 */
  get checkAll() {
    return (
      this.tableData.length &&
      this.tableData.filter(item => item.checked).length ===
        this.tableData.length
    );
  }

  set checkAll(v) {
    this.onCheckAllChange(v);
    this.$emit("onCheck", this.getCheckedItem());
  }

  /** 表列 */
  @Prop({ type: Array })
  columns: any[];

  @Prop({ type: Array })
  data: any[];

  /** 选择框 */
  @Prop({ type: Boolean, default: false })
  selection: boolean;

  @Prop({ type: Boolean, default: false })
  custom: boolean;

  /** 表头部样式 */
  @Prop({ type: String, default: "" })
  tableHeaderClass: string;

  /** 表内容头部样式 */
  @Prop({ type: String, default: "" })
  rowHeaderClass: string;

  @Prop({ type: Boolean, default: false })
  needHoverBorder: boolean;

  @Prop({ type: Boolean, default: false })
  needBorder: boolean;

  /** 多行字段 */
  @Prop({ type: String, default: "" })
  multipleKey: string;

  tableData = [];

  keyIndex = 0;

  get isIndeterminate() {
    const len = this.tableData.filter(item => item.checked).length;
    return len !== 0 && len !== this.tableData.length;
  }

  @Watch("data", { deep: true, immediate: true })
  handleDataChange(v) {
    this.tableData = cloneDeep(this.data);
  }

  /** 获取选中的条目 */
  getCheckedItem() {
    return this.tableData.filter(item => item.checked);
  }

  /** 设置组件propData */
  private setPropData(component: VNode, data: any) {
    Object.assign(
      component.componentOptions ? component.componentOptions.propsData : {},
      data,
    );
  }

  /** 全选  */
  private onCheckAllChange(v) {
    this.tableData = this.tableData.map(item => {
      item.checked = v;
      return item;
    });
  }

  /** 单选  */
  private onItemCheckChange(checked = false, index) {
    this.$set(
      this.tableData,
      index,
      Object.assign(this.tableData[index], { checked: !checked }),
    );
    this.$emit("onCheck", this.getCheckedItem());
  }

  render(h) {
    return (
      <table class={["m__table"]} cellspacing="0" cellpadding="0">
        <colgroup>
          {this.columns.map(item => {
            return <col width={item.width}></col>;
          })}
        </colgroup>

        <thead
          class={[
            "m__table--head",
            this.tableHeaderClass,
            this.$scopedSlots.header && "padding",
          ]}
        >
          <tr>
            {this.columns.map((item, i) => {
              return (
                <th style={item.coustomStyle}>
                  {i === 0 && this.selection && (
                    <el-checkbox
                      indeterminate={this.isIndeterminate}
                      vModel={this.checkAll}
                    ></el-checkbox>
                  )}
                  {item.label}
                </th>
              );
            })}
          </tr>
        </thead>
        {!this.tableData.length ? (
          <tbody class="m__table--empty">
            <tr>
              {/* {this.columns.map(() => {
                return <td>暂无数据</td>;
              })} */}
              <td class="empty__td" colspan={this.columns.length} align="center">
                暂无数据~
              </td>
            </tr>
          </tbody>
        ) : (
          this.tableData.map((row, index) => {
            return (
              <tbody
                class={[
                  "m__table--body",
                  this.custom ? "custom" : "default",
                  this.needBorder && "need--border",
                  this.needHoverBorder && row.close
                    ? "hover--class"
                    : "ordinary--class",
                ]}
              >
                {this.$scopedSlots.header && (
                  <tr>
                    <td colspan={this.columns.length}>
                      <div
                        class={[
                          "body--header",
                          this.rowHeaderClass,
                          { close: row.close },
                        ]}
                      >
                        {this.selection && (
                          <el-checkbox
                            vModel={row.checked}
                            key={row.checked}
                            onChange={this.onItemCheckChange.bind(
                              this,
                              row.checked,
                              index,
                            )}
                          ></el-checkbox>
                        )}
                        {this.$scopedSlots.header({ row, index })}
                      </div>
                    </td>
                  </tr>
                )}
                {this.custom ? (
                  this.$scopedSlots["custom-body"]({ row, index }) &&
                  this.$scopedSlots["custom-body"]({ row, index }).map(
                    child => {
                      if (!child.data) {
                        child.data = {};
                      }
                      child.data.staticClass = `body--content ${this
                        .multipleKey &&
                        row[this.multipleKey].length > 1 &&
                        "is--multiple "}`;

                      return child;
                    },
                  )
                ) : (
                  <MTableRow
                    {...{
                      props: { row },
                    }}
                  >
                    {this.$scopedSlots.default({ row }).map((child, i) => {
                      this.setPropData(child, {
                        row,
                        index,
                      });
                      return (
                        <td class={["m__table--item", !child.tag && "hide"]}>
                          <span
                            class={[
                              "selection__checkbox",
                              this.selection && i === 0 && "selection",
                            ]}
                          >
                            {i === 0 && this.selection && (
                              <el-checkbox
                                vModel={row.checked}
                                key={row.checked}
                                onChange={this.onItemCheckChange.bind(
                                  this,
                                  row.checked,
                                  index,
                                )}
                              ></el-checkbox>
                            )}
                            {child}
                          </span>
                        </td>
                      );
                    })}
                  </MTableRow>
                )}
              </tbody>
            );
          })
        )}
      </table>
    );
  }
}
