import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import MTableBody from "./MTableBody";
import "../styles/m-table.scss";

@Component({
  name: "MTable",
  components: { MTableBody },
})
export default class MTable extends Vue {
  static componentName = "MTable";

  @Prop()
  data: any[];

  @Prop({ type: Boolean, default: false })
  selection: boolean;

  @Prop({ type: Boolean, default: false })
  custom: boolean;

  @Prop({ type: Boolean, default: false })
  needBorder: boolean;

  @Prop()
  columns: any[];

  /**
   * @description 是否为单表格模式
   * @type {boolean}
   * @memberof MTable
   */
  @Prop()
  single: boolean;

  @Prop({ type: String, default: "" })
  tableHeaderClass: string;

  @Prop({ type: String, default: "" })
  rowHeaderClass: string;

  @Prop()
  needHoverBorder: boolean;

  @Prop()
  checkedItem: any;

  /** 多行字段 */
  @Prop({ type: String, default: "" })
  multipleKey: string;

  /** 表列 */
  getColumns() {
    return (
      this.columns ||
      this.$scopedSlots
        .default({})
        .filter(component => !!component.componentOptions)
        .map(component => {
          const propData = component.componentOptions.propsData as any;
          return {
            label: propData.label,
            width: propData.width,
            prop: propData.prop,
          };
        })
    );
  }

  @Watch("data", { immediate: true })
  handleDataChnage(v) {
    this.$nextTick(() => {
      this.onCheck(this.getCheckedItem());
    });
  }

  getCheckedItem() {
    return (this.$refs.table as any).getCheckedItem();
  }

  onCheck(v) {
    this.$emit("update:checkedItem", v);
  }

  render(h) {
    return (
      <div class={["m__table--container", this.single && "single"]}>
        <MTableBody
          {...{
            props: {
              columns: this.getColumns(),
              data: this.data,
              custom: this.custom,
              selection: this.selection,
              tableHeaderClass: this.tableHeaderClass,
              rowHeaderClass: this.rowHeaderClass,
              needHoverBorder: this.needHoverBorder,
              multipleKey: this.multipleKey,
              needBorder: this.needBorder,
            },
            on: {
              onCheck: this.onCheck,
            },
            scopedSlots: {
              /* tslint:disable */
              [this.$scopedSlots.header && "header"]: row =>
                this.$scopedSlots.header(row),
              [this.$scopedSlots["custom-body"] && "custom-body"]: row =>
                this.$scopedSlots["custom-body"](row),
              default: row => {
                return (
                  this.$scopedSlots.default && this.$scopedSlots.default(row)
                );
              },
              /* tslint:enable */
            },
            ref: "table",
          }}
        ></MTableBody>
      </div>
    );
  }
}
