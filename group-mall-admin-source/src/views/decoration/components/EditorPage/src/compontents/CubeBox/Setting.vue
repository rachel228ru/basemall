<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 09:38:30
-->
<template>
  <!-- 魔方 -->
  <div clas="zent-design-editor-item">
    <div class="zent-design-editor__control-group">
      <div class="zent-design-editor__control-group-container">
        <div
          class="zent-design-editor__control-group-label zent-design-editor__control-group-label--top"
          title="选择模版"
        >
          模版
        </div>
        <div class="zent-design-editor__control-group-control">
          <div>
            <div
              class="rc-design-select-templates"
              :class="{ active: index4 == formData.showMethod }"
              v-for="(item, index4) in template1"
              :key="`template${index4}`"
              :data-length="index4 !== 7 ? item[`subEntry`].length : 0"
              @click="changeTemplate(item, index4)"
            >
              <div class="rc-design-select-templates__image-block">
                <img :src="item.img" width="94px" height="64px" alt="temp" />
              </div>
              <div class="rc-design-select-templates__title">
                {{ item.title }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div
      class="zent-design-editor__control-group"
      v-if="formData.showMethod == 7"
    >
      <div class="zent-design-editor__control-group-container">
        <div class="zent-design-editor__control-group-label">
          <!-- react-text: 2624 -->
          密度:
          <!-- /react-text -->
        </div>
        <div class="zent-design-editor__control-group-control">
          <div
            class="zent-popover-wrapper zent-select"
            style="display: inline-block;"
          >
            <el-select
              v-model="miduValue"
              placeholder="请选择"
              @change="onChangeMidu"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </div>
        </div>
      </div>
    </div>
    <div class="zent-design-editor__control-group">
      <div class="zent-design-editor__control-group-container">
        <div
          class="zent-design-editor__control-group-label zent-design-editor__control-group-label--top"
        >
          <!-- react-text: 2907 -->
          布局:
          <!-- /react-text -->
        </div>
        <div class="zent-design-editor__control-group-control">
          <div class="rc-design-component-cube clearfix">
            <ul
              class="cube-row"
              v-for="(item, index1) in showCubeList"
              :key="index1"
            >
              <li
                class="cube-item"
                :class="{ 'item-selected': item1.selected }"
                :style="{
                  width: item1.style.width,
                  height: item1.style.height,
                }"
                v-for="(item1, index2) in item"
                :ref="`cube-item-${index1}-${index2}`"
                :key="`cube-item-${index2}`"
                :data-coordinates="item.coordinates"
                :data-ref="`cube-item-${index1}-${index2}`"
                :data-selected="item1.selected ? 1 : 0"
                :data-start="item1.start"
                @click.stop="onSelectMidu(index1, index2)"
                @mouseover="onMouseOver(index1, index2)"
              >
                <span
                  class="plus-icon"
                  :style="{ 'line-height': item1.style.height }"
                  v-if="!item1.selected"
                  >+</span
                >
              </li>
            </ul>
            <div
              class="cube-selected"
              :class="{
                'cube-selected-click': activeShowCubeListWrap == index3,
              }"
              v-for="(item, index3) in showCubeListWrap"
              :style="{
                width: item.width,
                height: item.height,
                top: item.top,
                left: item.left,
              }"
              :key="`cube-selected-${index3}`"
              @click.stop="onShowCubeListWrap(index3)"
              @mouseover.stop="onMouseOverSeleted(index3)"
            >
              <div
                class="rc-design-editor-card-item-delete el-icon-error"
                v-if="
                  activeItem.showMethod == 7 &&
                    mouseOverSeleted == index3 &&
                    activeShowCubeListWrap == index3
                "
                @click.stop="delSelected(index3)"
              ></div>
              <div
                class="cube-selected-text"
                v-if="!activeItem[`subEntry`][index3].img"
              >
                {{ item.text }}
              </div>
              <img
                :src="activeItem[`subEntry`][index3].img"
                :style="{ width: item.width, height: item.height }"
                v-else
              />
              <div
                class="iconfont iconshangpinxiangqing-baozhuang iconshangpinxiangqing-baozhuang-2"
                v-if="
                  activeShowCubeListWrap == index3 &&
                    activeItem[`subEntry`][index3].img
                "
              ></div>
            </div>
          </div>
          <div class="zent-design-editor__control-group-help-desc">
            {{
              formData.showMethod == 7
                ? "选定布局区域，在下方添加图片，建议添加比例一致的图片"
                : "选定布局区域，在下方添加图片"
            }}
          </div>
        </div>
      </div>
    </div>
    <div class="zent-design-editor__control-group">
      <div class="zent-design-editor__control-group-container">
        <div class="zent-design-editor__control-group-label">间隙:</div>
        <div class="zent-design-editor__control-group-control">
          <el-slider
            v-model="activeItem.borderWidth"
            show-input
            class="zent-slider"
            :max="30"
          ></el-slider>
        </div>
      </div>
    </div>
    <div class="zent-design-editor__control-group">
      <div class="zent-design-editor__control-group-container">
        <div class="zent-design-editor__control-group-label">边距:</div>
        <div class="zent-design-editor__control-group-control">
          <el-slider
            v-model="activeItem.pageMargin"
            show-input
            class="zent-slider"
            :max="30"
          ></el-slider>
        </div>
      </div>
    </div>
    <div
      class="rc-design-editor-card-item"
      v-if="activeItem.subEntry.length > 0"
    >
      <div class="rc-design-component-editor_subentry-item clearfix">
        <!-- <div class="rc-design-common-choice-image-component image-editor">
      
        </div>-->
        <div class="subentry-item-editor-form-content">
          <div class="zent-design-editor__control-group subentry-control-group">
            <div
              class="zent-design-editor__control-group-container flex"
              style="align-items: flex-start;"
            >
              <div class="zent-design-editor__control-group-label">图片：</div>
              <div class="zent-design-editor__control-group-control">
                <el-upload
                  class="has-not-choose-image"
                  action
                  multiple
                  :auto-upload="false"
                  :show-file-list="false"
                  :on-change="handleChange"
                >
                  <i
                    class="el-icon-plus plus-icon"
                    v-if="!activeItem[`subEntry`][activeShowCubeListWrap].img"
                  ></i>
                  <div
                    v-if="!activeItem[`subEntry`][activeShowCubeListWrap].img"
                  >
                    添加图片
                  </div>
                  <img
                    class="thumb-image"
                    :src="activeItem[`subEntry`][activeShowCubeListWrap].img"
                    alt
                    width="80"
                    height="80"
                    v-if="activeItem[`subEntry`][activeShowCubeListWrap].img"
                  />
                  <span
                    class="modify-image"
                    v-if="activeItem[`subEntry`][activeShowCubeListWrap].img"
                    >更换图片</span
                  >
                </el-upload>
              </div>
            </div>
            <label
              class="zent-design-editor__control-group-container flex"
              style="align-items: flex-start;"
            >
              <div class="zent-design-editor__control-group-label">链接：</div>
              <div class="zent-design-editor__control-group-control">
                <div class="rc-choose-link-menu">
                  <div
                    class="zent-popover-wrapper rc-choose-link-menu-popover-wrapper"
                    style="display: inline-block;"
                  >
                    <div class="rc-choose-link-menu-trigger">
                      <LinkSelect
                        :link="
                          activeItem[`subEntry`][activeShowCubeListWrap].link
                        "
                      />
                    </div>
                  </div>
                </div>
              </div>
            </label>
            <label
              class="zent-design-editor__control-group-container flex"
              v-if="activeItem[`subEntry`][activeShowCubeListWrap].link.name"
            >
              <div class="zent-design-editor__control-group-label">页面：</div>
              <div class="zent-design-editor__control-group-control">
                <div class="rc-choose-link-menu">
                  <div
                    class="zent-popover-wrapper rc-choose-link-menu-popover-wrapper"
                    style="display: inline-block;"
                  >
                    <div class="rc-choose-link-menu-trigger">
                      {{
                        activeItem[`subEntry`][activeShowCubeListWrap].link.name
                      }}
                    </div>
                  </div>
                </div>
              </div>
            </label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, PropSync, Watch } from "vue-property-decorator";
import templateList from "./template";
import {
  CubeBoxFormData,
  IShowCubeListItem,
  IShowCubeListWrap,
  ITemplatCube,
} from "./CubeBox";
import LinkSelect from "@/components/LinkSelect";
import { upLoad } from "@/api/index";
@Component({
  components: {
    LinkSelect,
  },
})
export default class CubeBoxPreview extends Vue {
  @PropSync("formData", {
    type: Object,
  })
  activeItem!: CubeBoxFormData;

  /** 模版选择list */
  template1: ITemplatCube[] = templateList; // 模板选择list

  showCubeList: IShowCubeListItem[] = []; // 展示格子

  showCubeListWrap: IShowCubeListWrap[] = []; // 格子蒙层

  activeShowCubeListWrap = 0; // 门层下标

  mouseOverSeleted: number | null = null; // 鼠标移动到密度模块上下标

  isSeletedMidu = false; // 判断密度是是不是开始选取

  coordinates = {
    // 开始选取时候的坐标
    rowStart: 0, //x 行
    columnStart: 0, //y  列
  };

  ranksNumber = {
    // 对于选中块结算单位
    rowCount: 1,
    columnCount: 1,
  };

  styleWidth = 0; // 初始化后单个模块的宽度

  styleHeight = 0; // 初始化后单个模块的高度

  options = [
    // 尺寸属性
    {
      value: "4x4",
      label: "4x4",
    },
    {
      value: "5x5",
      label: "5x5",
    },
    {
      value: "6x6",
      label: "6x6",
    },
    {
      value: "7x7",
      label: "7x7",
    },
  ];

  miduValue = "4x4"; // 初始化属性

  get activeComIndex() {
    // 针对多个魔方处理缓存数据(当前组件下标)
    return this.$STORE.decorationStore.activeComIndex;
  }

  @Watch("activeComIndex") //切换相同组件清除缓存
  resetCube() {
    this.mouseOverSeleted = null;
    this.activeShowCubeListWrap = 0;
    this.showCubeListWrap = [];
    this.showCubeList = [];
    this.isSeletedMidu = false;
    this.coordinates = {
      rowStart: 0,
      columnStart: 0,
    };
    this.ranksNumber = {
      rowCount: 0,
      columnCount: 0,
    };
  }

  /**
   * 魔方密度选择后产生相对应魔方数据
   */
  @Watch("activeItem", { deep: true })
  drawCube(isChangeMidu = false) {
    if (!!this.activeItem) {
      // this.formData = Object.assign({}, this.activeItem);
      const perviewLayoutWidth = 322;
      // if(this.showCubeList)
      if (
        this.showCubeList.length &&
        this.activeItem.showMethod === 7 &&
        !isChangeMidu
      ) {
        //拦截密度选择截取
      } else {
        this.showCubeList = [];
        const item = this.activeItem;
        const layoutWidth = item.layoutWidth;
        const layoutHeight = item.layoutHeight;
        //画魔方

        const styleWidth = Math.ceil(perviewLayoutWidth / layoutWidth);
        const styleHeight =
          layoutHeight !== 1
            ? Math.ceil(perviewLayoutWidth / layoutHeight)
            : styleWidth;
        for (let i = 0; i < layoutWidth; i++) {
          const ul = [];
          for (let j = 0; j < layoutHeight; j++) {
            const li = {
              style: {
                width: styleWidth + "px",
                height: styleHeight + "px",
              },
              coordinates: i + ":" + j,
              selected: false,
              finish: 0,
              start: 0,
            };
            ul.push(li);
          }
          this.showCubeList.push(ul);
        }
        this.styleWidth = styleWidth;
        this.styleHeight = styleHeight;
      }
      this.drawCubeWrap(this.styleWidth, this.styleHeight);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择模块属性
   * @param {ITemplatCube} item
   * @param {number} index
   */

  changeTemplate(item: ITemplatCube, index: number) {
    this.mouseOverSeleted = null;
    this.activeShowCubeListWrap = 0;
    this.showCubeListWrap = [];
    this.showCubeList = [];
    this.isSeletedMidu = false;
    this.activeItem.showMethod = index;
    if (index == 7) {
      //自定义魔方bu hsang
      this.miduValue = "4x4";
      this.onChangeMidu();
    } else {
      this.activeItem[`layoutWidth`] = item[`layoutWidth`];
      this.activeItem[`layoutHeight`] = item[`layoutHeight`];
      if (!this.activeItem[`subEntry`].length) {
        this.activeItem[`subEntry`] = item[`subEntry`];
      } else {
        const dist =
          item[`subEntry`].length > this.activeItem[`subEntry`].length
            ? this.activeItem[`subEntry`].length
            : item[`subEntry`].length;
        for (let i = 0; i < dist; i++) {
          const item1 = item[`subEntry`][i];
          item1[`img`] = this.activeItem[`subEntry`][i][`img`]
            ? this.activeItem[`subEntry`][i][`img`]
            : "";
          item1[`link`] = this.activeItem[`subEntry`][i][`link`];
          item1[`linkName`] = this.activeItem[`subEntry`][i][`linkName`];
        }
        this.activeItem[`subEntry`] = item[`subEntry`];
      }
    }
    // this.drawCube();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 初始化魔方
   */

  mounted() {
    if (this.activeItem.showMethod == 7) {
      this.miduValue = `${this.activeItem.layoutWidth}x${this.activeItem.layoutHeight}`;
    }
    this.drawCube();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择魔方密度转变
   */

  onChangeMidu() {
    const arr = this.miduValue.split("x");
    this.activeItem[`layoutWidth`] = Number(arr[0]);
    this.activeItem[`layoutHeight`] = Number(arr[1]);
    this.activeItem[`subEntry`] = [];
    this.mouseOverSeleted = null;
    this.activeShowCubeListWrap = 0;
    this.showCubeListWrap = [];
    this.showCubeList = [];
    this.isSeletedMidu = false;
    this.drawCube(true);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 魔方密度选择后产生后针对对应魔方数据绘画出对应的魔方
   * @param {number} divWidth
   * @param {number} divHeight
   */

  drawCubeWrap(divWidth: number, divHeight: number) {
    const item = this.activeItem;
    const subEntry = item.subEntry;
    const showMethod = this.activeItem.showMethod;
    this.showCubeListWrap = [];
    if (subEntry.length) {
      for (let i = 0; i < subEntry.length; i++) {
        const coverDiv = {
          top: subEntry[i].y * divHeight + "px",
          left: subEntry[i].x * divWidth + "px",
          width: divWidth * subEntry[i].width + "px",
          height: divHeight * subEntry[i].height + "px",
          paddingTop: (divHeight * subEntry[i].height) / 2 + "px",
          img: subEntry[i].img ? subEntry[i].img : "",
          text: "",
        };
        this.showCubeList[subEntry[i].x][subEntry[i].y].selected = true;
        this.showCubeList[subEntry[i].x][subEntry[i].y].finish = 1;
        for (let j = 1; j < subEntry[i].width; j++) {
          this.showCubeList[subEntry[i].x + j][subEntry[i].y].selected = true;
          this.showCubeList[subEntry[i].x + j][subEntry[i].y].finish = 1;
        }
        for (let l = 1; l < subEntry[i].height; l++) {
          this.showCubeList[subEntry[i].x][subEntry[i].y + l].selected = true;
          this.showCubeList[subEntry[i].x][subEntry[i].y + l].finish = 1;
        }
        if (showMethod === 0 || showMethod == 1 || showMethod == 2) {
          coverDiv[`text`] =
            "宽度" + Math.ceil(750 / item.layoutWidth) + "像素";
        } else {
          coverDiv[`text`] =
            Math.ceil((750 / item.layoutWidth) * subEntry[i].width) +
            "像素" +
            Math.ceil((750 / item.layoutHeight) * subEntry[i].height) +
            "或同等比例";
        }
        this.showCubeListWrap.push(coverDiv);
      }
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 魔方密度选择后产生后针对魔方绘制画出对应的魔方
   * @param {number} index
   */

  onShowCubeListWrap(index: number) {
    this.activeShowCubeListWrap = index;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击任意方块度时候判断该方块是起始位置还是结束位置
   * @param {string|number} index1
   * @param {string|number} index2
   */

  onSelectMidu(index1: number, index2: number) {
    // 选择密度
    const selected = this.showCubeList[index1][index2].selected;
    if (this.isSeletedMidu && !selected) {
      return;
    }
    if (selected) {
      //再次确认
      let { rowStart, columnStart } = this.getItemStart();
      this.mouseOverSeleted = null;
      this.isSeletedMidu = false;
      let rowEnd = index1;
      let columnEnd = index2;
      rowStart > rowEnd
        ? ([rowStart, rowEnd] = [rowEnd, rowStart])
        : ([rowStart, rowEnd] = [rowStart, rowEnd]);
      //如果开始列大于结束列，则开始列用结束列，结束列用开始列
      columnStart > columnEnd
        ? ([columnStart, columnEnd] = [columnEnd, columnStart])
        : ([columnStart, columnEnd] = [columnStart, columnEnd]);
      this.activeItem.subEntry.push({
        x: rowStart,
        y: columnStart,
        width: this.ranksNumber.rowCount,
        height: this.ranksNumber.columnCount,
        img: "",
        link: {
          id: "",
          type: 0,
          name: "",
          url: "",
          append: "",
        },
        linkName: "",
      });
      this.showCubeList.forEach((item1, index3) => {
        //所有归零
        item1.forEach((item2, index4) => {
          this.showCubeList[index3][index4].start = 0;
        });
      });
      this.showCubeList.forEach((t, i) => {
        if (t.length) {
          t.forEach((t1, j) => {
            if (t1.selected == true) {
              this.showCubeList[i][j][`finish`] = 1;
            }
          });
        }
      });
    } else {
      this.mouseOverSeleted = null;
      this.showCubeList[index1][index2].selected = true;
      this.showCubeList[index1][index2].start = 1;
      this.isSeletedMidu = true;
      this.coordinates = {
        rowStart: index1,
        columnStart: index2,
      };
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取元素数据
   * @returns {*} { rowStart, columnStart }
   */

  getItemStart(): { rowStart: number; columnStart: number } {
    let rowStart = 0,
      columnStart = 0;
    this.showCubeList.forEach((item1, index3) => {
      item1.forEach((item2, index4) => {
        if (item2.start) {
          rowStart = index3;
          columnStart = index4;
        }
      });
    });
    return { rowStart, columnStart };
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 鼠标在任意方块的上滑动计算方块队列
   * @param {*} index1
   * @param {*} index2
   * @returns {*}
   */

  onMouseOver(index1: number, index2: number) {
    this.showCubeList.forEach((item1, index3) => {
      item1.forEach((item2, index4) => {
        if (!item2.finish) {
          this.showCubeList[index3][index4].selected = false;
        }
      });
    });
    if (this.isSeletedMidu) {
      //鼠标移动
      let rowStart = 0,
        columnStart = 0;
      this.showCubeList.forEach((item1, index3) => {
        item1.forEach((item2, index4) => {
          if (item2.start) {
            rowStart = index3;
            columnStart = index4;
          }
        });
      });
      let rowEnd = index1;
      let columnEnd = index2;
      //如果开始行大于结束行，则开始行用结束行，结束行用开始行
      rowStart > rowEnd
        ? ([rowStart, rowEnd] = [rowEnd, rowStart])
        : ([rowStart, rowEnd] = [rowStart, rowEnd]);
      //如果开始列大于结束列，则开始列用结束列，结束列用开始列
      columnStart > columnEnd
        ? ([columnStart, columnEnd] = [columnEnd, columnStart])
        : ([columnStart, columnEnd] = [columnStart, columnEnd]);

      let rowCount = 0; //总行数
      const columnCount = []; //总列数
      let isAdd = true;
      for (let forRowStart = rowStart; forRowStart <= rowEnd; forRowStart++) {
        rowCount++;
        //遍历列
        for (let i = columnStart; i <= columnEnd; i++) {
          //当前行列坐标
          // const currentCoordinates = forRowStart + ":" + i;
          //检测当前的模块是否被占用，如果被占用，那么整个都不能选择
          if (this.showCubeList[forRowStart][i].finish) {
            isAdd = false;
            break;
          }
          if (!this.inArray(i, columnCount)) columnCount.push(i);
          this.showCubeList[forRowStart][i].selected = true;
        }
      }
      if (!isAdd) {
        let rowStart1 = 0,
          columnStart1 = 0;
        this.showCubeList.forEach((item1, index1) => {
          item1.forEach((item2, index2) => {
            if (!item2.finish) {
              this.showCubeList[index1][index2].selected = false;
            }
            if (item2.start) {
              rowStart1 = index1;
              columnStart1 = index2;
            }
          });
        });
        this.showCubeList[rowStart1][columnStart1].selected = true;
      }
      this.ranksNumber = {
        rowCount: rowCount,
        columnCount: columnCount.length,
      };
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击魔方块的showCubeListWrap下标
   * @param {number} index
   */

  onMouseOverSeleted(index: number) {
    this.mouseOverSeleted = index;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击模块开始 滑动未选中监听
   */

  onMouseMoveSeleted() {
    this.mouseOverSeleted = null;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 监听该模快是否被删除
   * @param {number} i
   */

  delSelected(i: number) {
    const subEntry = this.activeItem[`subEntry`];
    this.showCubeList[subEntry[i].x][subEntry[i].y].selected = false;
    this.showCubeList[subEntry[i].x][subEntry[i].y].finish = 0;
    this.showCubeList[subEntry[i].x][subEntry[i].y].start = 0;
    for (let j = 1; j < subEntry[i].width; j++) {
      this.showCubeList[subEntry[i].x + j][subEntry[i].y].selected = false;
      this.showCubeList[subEntry[i].x + j][subEntry[i].y].finish = 0;
      this.showCubeList[subEntry[i].x][subEntry[i].y].start = 0;
    }
    for (let l = 1; l < subEntry[i].height; l++) {
      this.showCubeList[subEntry[i].x][subEntry[i].y + l].selected = false;
      this.showCubeList[subEntry[i].x][subEntry[i].y + l].finish = 0;
      this.showCubeList[subEntry[i].x][subEntry[i].y].start = 0;
    }
    this.showCubeList.forEach((item1, index3) => {
      //清除选中的
      item1.forEach((item2, index4) => {
        if (!item2.finish) {
          this.showCubeList[index3][index4].selected = false;
        }
        this.showCubeList[index3][index4].start = 0;
      });
    });
    this.mouseOverSeleted = null;
    this.isSeletedMidu = false;
    this.activeShowCubeListWrap = 0;
    this.ranksNumber = {
      rowCount: 1,
      columnCount: 1,
    };
    this.activeItem[`subEntry`].splice(i, 1);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 检查该数是否存在该数组内
   * @param {number} elem
   * @param {number} arr
   * @returns {boolean}
   */
  inArray(elem: number, arr: number[]): boolean {
    let i = 0;
    const n = arr.length;
    for (; i < n; ++i) if (arr[i] === elem) return true;
    return false;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择图片上传
   * @param {*} file
   */

  async handleChange(file: { size: number; raw: { type: string } }) {
    try {
      const img = await this.upLoadFile(file);
      const index = this.activeShowCubeListWrap;
      this.activeItem[`subEntry`][index].img = img;
      this.showCubeListWrap[index].img = img;
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 上传图片
   * @param {*} file
   */
  async upLoadFile(file: { size: number; raw: { type: string } }) {
    try {
      const whiteList = ["image/jpeg", "image/jpg", "image/png", "image/gif"];
      const isLt2M = file.size < 2 * 1024 * 1024;
      if (!whiteList.includes(file.raw.type)) {
        this.$message.error("上传文件只能是GIF, JPG或PNG 格式!");
        return;
      }
      if (!isLt2M) {
        this.$message.error("上传文件大小不能超过 2MB!");
        return;
      }
      const res = await upLoad({
        file: file.raw,
      });
      return res.data;
    } catch (error) {
      console.log(error);
    }
  }
}
</script>

<style lang="scss" scope>
@import "@/assets/styles/decoration/cubeBox.scss";
</style>
