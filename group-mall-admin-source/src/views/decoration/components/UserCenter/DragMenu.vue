<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:25:30
-->
<template>
  <div class="drag--menu">
    <div class="draggable__menu">
      <draggable v-model="menu">
        <transition-group type="transition">
          <div
            v-for="(item, index) in menu"
            :key="item.id"
            @drop.stop="drop($event)"
            @dragover.stop="dragover($event)"
          >
            <div class="menu__item error-menuItem" :id="item.id">
              <el-checkbox v-model="item.hideMenu">
                {{ item.menuName }}
              </el-checkbox>
              <span>
                <i
                  class="el-icon-edit-outline el-collapse-item__arrow menu__item--icon"
                  @click="menuClickHandle(item, 1)"
                />
                <i
                  class="el-icon-error"
                  @click="expandMenuDeleteHandle(index)"
                />
              </span>
            </div>
            <div
              class="menu__item error-menuItem"
              v-if="splitFlag"
              v-show="item.splitFlag"
            >
              <div
                class="drag--menuitem"
                :id="item.id"
                draggable="true"
                @dragstart.stop="dragStart($event, 'splitFlag')"
                @dragend.stop="dragEnd($event)"
              ></div>
              <i class="el-icon-error" @click="dragDeleteHandle(index)" />
            </div>
          </div>
        </transition-group>
      </draggable>
      <slot name="qrcodeVisible"></slot>
      <div
        class="drag--menuList drag--shadow__item error-menuItem"
        v-if="dragItemVisible"
      >
        <div
          class="drag--menuitem drag--shadow"
          draggable="true"
          @dragstart="dragStart($event, 'splitFlag')"
        ></div>
        <i class="el-icon-error" @click="dragItemVisible = false" />
      </div>
    </div>
    <div class="footer">
      <div class="footer__button">
        <el-button
          size="medium"
          @click="
            menuClickHandle(
              { styleType: menu[0] ? menu[0].styleType : menuStyle },
              2,
            )
          "
          type="primary"
          plain
          >添加菜单
        </el-button>
        <el-button
          size="medium"
          v-if="splitFlag"
          @click="createDragItem(true)"
          type="primary"
          plain
          >添加分隔栏
        </el-button>
      </div>
    </div>
    <menu-dialog
      :menuVisible.sync="menuVisible"
      @updateMenu="updateMenuHandle"
      :menuProp="menuProp"
      :type="optionType"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, PropSync, Prop } from "vue-property-decorator";
import draggable from "vuedraggable";
import MenuDialog from "@/views/decoration/components/UserCenter/MenuDialog.vue";
import {
  MenuList,
  SubMenuList,
} from "@/views/decoration/components/UserCenter/formModel/UserCenter";

@Component({
  components: {
    draggable,
    MenuDialog,
  },
})
export default class DragMenu extends Vue {
  @PropSync("menuList", {
    type: Array,
    default() {
      return [];
    },
  })
  menu!: MenuList[] | SubMenuList[];

  /**
   * 1. 列表式 2. 九宫格
   */
  @Prop({
    type: Number,
    default: 1,
  })
  menuStyle!: number;

  /**
   * 展开式是否显示分隔
   */
  @Prop({
    type: Boolean,
    default: true,
  })
  splitFlag!: boolean;

  /**
   * 1 修改菜单
   * 2 新增菜单
   */
  optionType = 1;

  /** 传入菜单属性 */
  menuProp: MenuList | SubMenuList = {
    id: 0,
    menuName: "",
    menuIconUrl: "",
    defaultIcon: "",
    styleType: 0,
    sortIndex: 0,
    hideMenu: false,
    allowUse: 0,
    linkSelectItem: {
      id: 0,
      name: "",
      type: 0,
      url: "",
      append: "",
    },
    splitFlag: false,
    pid: "",
  };

  /** 菜单设置弹窗 */
  menuVisible = false;

  /** 展开式菜单分隔栏 */
  dragItemVisible = false;

  /**
   * @LastEditors: chuyinlong
   * @description: 菜单点击
   * @param {MenuList} item
   * @param {number} type
   */

  menuClickHandle(item: MenuList, type: number) {
    Object.assign(this.menuProp, item);
    this.optionType = type;
    this.menuVisible = true;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除菜单
   * @param {number} index
   */

  expandMenuDeleteHandle(index: number) {
    this.menu.splice(index, 1);
  }

  /**
   * @LastEditors: chuyinlong
   * @description:当前表的拖动
   * @param {*} e
   * @param {*} arg1
   * @param {*} type
   */

  dragStart(
    e: {
      stopPropagation: () => void;
      dataTransfer: { setData: (arg0: string, arg1: any) => void };
    },
    type: any,
  ) {
    e.stopPropagation();
    e.dataTransfer.setData("Txt", type);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 拖动结束
   * @param {*} e
   */

  dragEnd(e: { target: { id: number } }) {
    const index = this.menu.findIndex(item => item.id === e.target.id);
    this.menu[index].splitFlag = false;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 拖动删除
   * @param {number} index
   */

  dragDeleteHandle(index: number) {
    this.menu[index].splitFlag = false;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 阻止冒泡
   * @param {*} e
   */

  dragover(e: {
    preventDefault: () => void;
    dataTransfer: { dropEffect: string };
  }) {
    e.preventDefault();
    e.dataTransfer.dropEffect = "move";
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 替换
   * @param {*} e
   */

  drop(e: {
    preventDefault: () => void;
    dataTransfer: { getData: (arg0: string) => any };
    target: { id: number };
  }) {
    e.preventDefault();
    const data = e.dataTransfer.getData("Txt");
    if (data === "splitFlag") {
      const index = this.menu.findIndex(item => item.id === e.target.id);
      if (this.menu[index] && typeof this.menu[index].splitFlag === "boolean") {
        this.menu[index].splitFlag = true;
        this.dragItemVisible = false;
      }
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 展开式菜单分隔栏现隐
   * @param {boolean} visible
   */

  createDragItem(visible: boolean) {
    this.dragItemVisible = visible;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 前端更新菜单数据值
   * @param { MenuList | SubMenuList} menuItem
   */

  updateMenuHandle(menuItem: MenuList | SubMenuList) {
    if (this.optionType === 1) {
      this.updateMenu(menuItem);
    }
    if (this.optionType === 2) {
      this.addMenu(menuItem);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 更新菜单信息
   * @param {MenuList | SubMenuList} menuItem
   */

  updateMenu(menuItem: MenuList | SubMenuList) {
    const index = this.menu.findIndex(item => item.id === menuItem.id);
    this.menu[index].menuName = menuItem.menuName;
    this.menu[index].menuIconUrl = menuItem.menuIconUrl;
    this.menu[index].linkSelectItem = menuItem.linkSelectItem;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 新增一个菜单
   * @param {MenuList | SubMenuList} menuItem
   */

  addMenu(menuItem: MenuList | SubMenuList) {
    const menu = {
      id: this.menu.length,
      menuName: menuItem.menuName,
      menuIconUrl: menuItem.menuIconUrl,
      defaultIcon: "",
      styleType: this.menu[0] ? this.menu[0].styleType : this.menuStyle,
      sortIndex: 0,
      hideMenu: true,
      allowUse: 0,
      linkSelectItem: menuItem.linkSelectItem,
      splitFlag: false,
      pid: "",
    };
    this.menu.push(menu);
  }
}
</script>

<style lang="scss" scoped>
@include b(menu) {
  @include e(item) {
    position: relative;

    @include flex(space-between);
    height: 48px;
    padding: 0 10px;
    margin: 8px 0;
    background-color: #ffffff;

    @include m(icon) {
      margin-right: 0;
      color: #9797a1;
      font-size: 16px;
    }
  }
}

@include b(footer) {
  padding: 10px 0 15px 0;
  background-color: #ffffff;

  @include e(button) {
    padding: 10px 0 0 10px;
  }
}

.drag--menuList {
  background-color: #ffffff;
  padding: 10px;
}

.draggable__menu {
  background-color: #f1f2f6;
  padding: 2px 10px;

  .drag--menuitem {
    position: relative;
    height: 15px;
    width: 100%;
    background-color: #e9e9e9;
  }
}
.draggable__menu--fold {
  background-color: #f1f2f6;
  padding: 8px 10px;

  .drag--menuitem {
    position: relative;
    height: 15px;
    width: 100%;
    background-color: #e9e9e9;
  }
}

.drag--shadow__item {
  @include flex(space-between);
  height: 48px;
  padding: 0 15px;
  margin: 8px 0;
  background-color: #ffffff;
}

.drag--shadow {
  position: relative;
  height: 15px;
  width: 100%;
  background-color: #e9e9e9;
}

.drag--shadow:hover {
  box-shadow: #9797a1 0 0 5px;
}

// hover显示隐藏
.error-menuItem {
  position: relative;
  font-size: 16px;
  z-index: 2;

  .el-icon-error {
    opacity: 0;
    color: #9797a1;
    position: absolute;
    right: -6px;
    top: -6px;
    z-index: 9999;
  }
}

.error-menuItem:hover {
  position: relative;
  z-index: 2;

  .el-icon-error {
    opacity: 1;
  }
}
</style>
