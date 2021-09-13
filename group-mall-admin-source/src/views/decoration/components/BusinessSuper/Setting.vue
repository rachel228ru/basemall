<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
<<<<<<< HEAD
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 10:47:28
=======
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 11:02:35
>>>>>>> 2ad1655cd17acb39d6973e91adac8b5bf5aa6ef1
-->
<template>
  <div class="spellPage--setting">
    <el-form
      ref="formData"
      :model="formData"
      label-width="80px"
      label-position="left"
    >
      <el-form-item label="选择模式">
        <el-radio-group v-model="formData.selectMode">
          <el-radio :label="1">顶部模式</el-radio>
          <el-radio :label="2">侧边栏模式</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="选择样式" v-if="formData.selectMode === 2">
        <el-radio-group v-model="formData.selectType">
          <el-radio :label="1">商品列表</el-radio>
          <el-radio :label="2">分类列表</el-radio>
        </el-radio-group>
      </el-form-item>
      <div class="class__box">
        <div class="select__box">
          <el-button @click="chooseCategory">选择展示分类</el-button>
          <span class="tips">已选分类（上下拖动可排序）</span>
        </div>
        <div
          class="list__item"
          v-for="(group, index) in formData.firstCatList"
          :key="index"
          :draggable="true"
          @dragstart="handleDragClass(index)"
          @dragover="handleDragoverClass"
          @drop="handleDropClass(index)"
        >
          {{ group.category.name }}
          <span
            @click="handleGoodGroupDel(group.category.id)"
            class="click__btn"
            >删除</span
          >
        </div>
      </div>

      <!-- 添加分类图片 -->
      <div
        class="poster__box"
        v-if="formData.selectMode === 2 && formData.selectType === 2"
      >
        <div class="tips">
          <span style="color: #666">添加分类图片:</span>
          鼠标拖拽调整顺序，小程序端分类按顺序显示
        </div>
        <div
          v-for="(item, index) in currentFirstCategory.classChart"
          :key="index"
          class="homeSwiperForm-item"
          style="background-color: #fff"
          :draggable="true"
          @click.capture="chartItemClick(item)"
          @dragstart="chartDragstart(index)"
          @dragover="handleDragover"
          @drop="chartDrop(index)"
        >
          <i
            class="el-icon-remove-outline remove__swiper--item"
            @click="deleteChartItem(index)"
          ></i>
          <el-upload
            class="homeSwiperForm-item__uploader"
            action
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleChartChange"
          >
            <img
              v-if="item.img"
              :src="item.img"
              class="homeSwiperForm-item__img"
            />
            <i v-else class="el-icon-plus homeSwiperForm-item__plus"></i>
          </el-upload>
          <div
            class="homeSwiperForm-item__right"
            style="padding-left: 20px; padding-top: 23px"
          >
            <el-form :model="item" label-width="70px">
              <el-form-item label="选择分类">
                <el-select
                  v-model="item.title"
                  placeholder="请选择"
                  @change="classSelectChange(item)"
                >
                  <el-option
                    v-for="(lable, inx) in currentFirstCategory.category
                      .showCategoryVos"
                    :key="inx"
                    :hidden="
                      currentFirstCategory.classChart &&
                      currentFirstCategory.classChart.find(
                        t =>
                          t.category &&
                          t.category.id === lable.id &&
                          lable.id !== item.title,
                      )
                        ? true
                        : false
                    "
                    :label="lable.name"
                    :value="lable.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <el-upload
          class="homeSwiperForm-add"
          style="background-color: #fff"
          action
          multiple
          :auto-upload="false"
          :show-file-list="false"
          :on-change="chartHandleChange"
        >
          <span>+ 添加一个分类图</span>
          <p style="margin-top: 5px">建议宽度177像素</p>
        </el-upload>
      </div>

      <!-- 添加海报 -->
      <div class="poster__box" v-if="formData.selectMode === 2">
        <div class="tips">
          <span style="color: #666">添加海报:</span>
          鼠标拖拽调整广告顺序，小程序端海报按顺序显示
        </div>
        <div
          v-for="(item, index) in currentFirstCategory.swiperList"
          :key="index"
          @click.capture="swiperItemClick(item)"
          class="homeSwiperForm-item"
          style="background-color: #fff"
          :draggable="true"
          @dragstart="handleDragstart(index)"
          @dragover="handleDragover"
          @drop="handleDrop(index)"
        >
          <i
            class="el-icon-remove-outline remove__swiper--item"
            @click="deleteSwiperItem(index)"
          ></i>
          <el-upload
            class="homeSwiperForm-item__uploader"
            action
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleItemChange"
          >
            <img
              v-if="item.img"
              :src="item.img"
              class="homeSwiperForm-item__img"
            />
            <i v-else class="el-icon-plus homeSwiperForm-item__plus"></i>
          </el-upload>
          <div class="homeSwiperForm-item__right">
            <el-form :model="item" label-width="70px">
              <el-form-item label="分类名称">
                {{ item.title }}
              </el-form-item>
              <el-form-item label="跳转路径">
                <LinkSelect :link="item.link" :inner="false" :selectType="2" />
              </el-form-item>
              <el-form-item label="">
                <span style="color: #9797a1">{{ item.link.name }}</span>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <el-upload
          class="homeSwiperForm-add"
          style="background-color: #fff"
          v-if="
            currentFirstCategory.swiperList &&
              currentFirstCategory.swiperList.length < 5
          "
          action
          multiple
          :auto-upload="false"
          :show-file-list="false"
          :on-change="handleChange"
        >
          <span>+ 添加背景图</span>
          <p style="margin-top: 5px">建议宽度500像素</p>
        </el-upload>
      </div>

      <!-- 导航信息 -->
      <div class="navigation__tips" v-if="isShowNav">
        <div class="tips">
          <span style="color: #666">导航信息:</span>
          鼠标拖拽调整顺序，小程序端分类按顺序显示
        </div>
      </div>
      <div class="navigation__box" v-if="isShowNav">
        <div v-for="(nav, iex) in currentFirstCategory.navigation" :key="iex">
          <div
            v-if="nav.navType !== 1"
            class="homeSwiperForm-item"
            style="background-color: #fff; display: block"
            :draggable="true"
            @dragstart="navigationDragstart(iex)"
            @dragover="handleDragover"
            @drop="navigationDrop(iex)"
          >
            <i
              class="el-icon-remove-outline remove__swiper--item"
              style="z-index: 11"
              @click="deletenavigationItem(iex)"
            ></i>
            <el-form-item label="导航名称">
              <el-input v-model="nav.name" placeholder="请输入内容"></el-input>
            </el-form-item>
            <el-form-item label="选择分类">
              <el-select
                v-model="nav.class"
                multiple
                placeholder="请选择"
                style="width: 100%"
                @change="navSelectClass(nav)"
              >
                <el-option
                  v-for="(cla, p) in getCanSelectClass(nav)"
                  :key="p"
                  :label="cla.category.name"
                  :value="cla.category.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <el-form-item
          label="导航样式"
          v-if="
            currentFirstCategory.navigation &&
              currentFirstCategory.navigation.length
          "
        >
          <el-radio-group v-model="currentFirstCategory.navigationStyle">
            <el-radio :label="1">样式一</el-radio>
            <el-radio :label="2">样式二</el-radio>
          </el-radio-group>
        </el-form-item>
        <div class="add__navigation">
          <span @click="addNavigation">添加导航</span>
        </div>
      </div>

      <div class="content__box">
        <el-form-item
          label="列表样式"
          v-if="formData.selectMode === 1 || formData.selectType === 1"
        >
          <el-radio-group v-model="formData.listStyle">
            <el-radio :label="1">大图样式</el-radio>
            <el-radio :label="2">列表样式</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="购买按钮" v-if="!isSideClassification">
          <el-radio-group v-model="formData.cartButton">
            <el-radio :label="3">样式一</el-radio>
            <el-radio :label="4">样式二</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="商品边距"
          class="slider__box"
          v-if="!isSideClassification"
        >
          <el-slider
            v-model="formData.goodsPadding"
            :show-tooltip="false"
            :show-input="true"
            :max="30"
          ></el-slider>
        </el-form-item>
        <el-form-item label="字体选中">
          <div class="color__select">
            <span class="color__tips">{{ formData.fontColor }}</span>
            <el-color-picker
              @change="colorSelectChange('fontColor')"
              v-model="formData.fontColor"
            ></el-color-picker>
          </div>
        </el-form-item>
        <el-form-item label="背景选中">
          <div class="color__select">
            <span class="color__tips">{{ formData.fontBg }}</span>
            <el-color-picker
              @change="colorSelectChange('fontBg')"
              v-model="formData.fontBg"
            ></el-color-picker>
          </div>
        </el-form-item>
        <el-form-item label="字体未选">
          <div class="color__select">
            <span class="color__tips">{{ formData.fontNotColor }}</span>
            <el-color-picker
              @change="colorSelectChange('fontNotColor')"
              v-model="formData.fontNotColor"
            ></el-color-picker>
          </div>
        </el-form-item>
        <el-form-item label="背景未选">
          <div class="color__select">
            <span class="color__tips">{{ formData.fontNotBg }}</span>
            <el-color-picker
              @change="colorSelectChange('fontNotBg')"
              v-model="formData.fontNotBg"
            ></el-color-picker>
          </div>
        </el-form-item>
      </div>
    </el-form>
    <Category
      v-if="dialogShow"
      :dialogShow.sync="dialogShow"
      :formData="formData"
      @choose="selectedCategory"
      ref="Category"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Ref, Watch } from "vue-property-decorator";
import BusinessSuper, {
  FirstCategory,
  SwiperListItem,
  LinkSelectItem,
  bus,
  Navigation,
  Chart,
  ICategory,
  ICategoryItemVos,
  ICategoryItem,
} from "./BusinessSuper";
import Category from "./components/Category.vue";
import { upLoad } from "@/api/index";
import LinkSelect from "@/components/LinkSelect";
import { getList } from "@/api/good/goods";

@Component({
  components: {
    Category,
    LinkSelect,
  },
})
export default class BusinessSuperSettting extends Vue {
  @Prop()
  formData!: BusinessSuper;

  // @Ref() readonly Category: HTMLFormElement;
  @Ref() readonly Category!: Category;

  currentSwiperItem: SwiperListItem = {} as SwiperListItem;

  /** 分类图片对象 */
  currentChartItem = {
    img: "",
    title: "",
  };

  /** 当前编辑的一级分类对象 */
  currentFirstCategory: FirstCategory = new FirstCategory();

  /** 拖动的组件的下角标 */
  dragStarIndex = -1;

  /** 类目拖动下角标 */
  classDragIndex = -1;

  /** 添加导航拖动下角标 */
  navigationDrag = -1;

  /** 分类图拖动下角标 */
  chartDragIndex = -1;

  /** 选择商品分类 */
  dialogShow = false;

  /** 剩余的二级分类 */
  surplusClass: ICategoryItemVos[] = [];

  /** 商品加载数据参数 */
  goodsParam = {
    /** 分类ID */
    showCategoryId: "",
    /** 页码 */
    current: 1,
    /** 大小 */
    size: 10,
  };

  /** 当前编辑的一级分类Id */
  get curruntCateId() {
    return this.currentFirstCategory
      ? this.currentFirstCategory.category.id
      : "";
  }

  get saleMode() {
    return this.$STORE.decorationStore.saleMode;
  }

  @Watch("saleMode")
  handClick() {
    this.filterCategory();
  }

  get isShowNav() {
    const { selectMode, selectType } = this.formData;
    const { navigation = [] } = this.currentFirstCategory;
    return selectMode === 2 && selectType === 2 && navigation.length;
  }

  /**
   * 是否是侧边栏、分类列表
   */
  get isSideClassification() {
    const { selectMode = 0, selectType = 0 } = this.formData;
    return selectMode === 2 && selectType === 2;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取当前可选择到二级分类
   * @param {*} nav
   * @returns {*}
   */

  getCanSelectClass(nav: Navigation): Chart[] {
    const { classChart, navigation } = this.currentFirstCategory;
    const tem = navigation.find(i => i.navType === 1) || ({} as any);
    const temp = [...tem];
    temp.push(...nav.class);
    return classChart.filter(k => temp.includes(k.title));
  }

  mounted() {
    /** 监听一级分类选择切换 */
    bus.$on("firstCategoryId", (data: string) => {
      this.curruntCateIdChange(data);
      this.reasetGoodsParam({
        showCategoryId: data === "-1" ? "" : data,
      });
    });
    this.filterCategory();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 初始化数据，需要过滤掉不可用掉一级分类
   */

  async filterCategory() {
    try {
      const param = {
        saleMode: this.saleMode,
      };
      const res = await getList(param);
      const list = res.data.list || [];
      const { firstCatList } = this.formData;
      const value = [] as any;
      if (!list.length) {
        this.formData.currentFirstCategory = this.currentFirstCategory;
      }
      firstCatList.forEach(i => {
        const category = list.find(k => k.id === i.category.id);
        if (category) {
          i.category = category;
          value.push(i);
        }
      });
      this.formData.firstCatList = value;
      this.fitlerClassSed();
    } catch (e) {
      console.log(e);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 过滤分类导航中选择的不可用的二级分类
   */

  fitlerClassSed() {
    const { firstCatList } = this.formData;
    firstCatList.forEach(i => {
      const { showCategoryVos = [] } = (i.category as any) || [];
      const { classChart, navigation } = i;
      classChart.forEach((k, d) => {
        const tm = showCategoryVos.find(
          (l: { id: string }) => l.id === k.category.id,
        );
        if (!tm) {
          classChart.splice(d, 1);
        } else {
          k.category = tm;
        }
      });
      navigation.forEach(k => {
        k.class.forEach((j, m) => {
          const idx = showCategoryVos.findIndex(
            (t: { id: string }) => t.id === j,
          );
          if (idx !== -1) {
            k.chartList[m].category = showCategoryVos.find(
              (t: { id: string }) => t.id === j,
            );
          } else {
            k.class.splice(m, 1);
            k.chartList.splice(m, 1);
          }
        });
      });
    });
    if (firstCatList.length) {
      this.currentFirstCategory = this.formData.currentFirstCategory =
        firstCatList[0];
    } else {
      this.formData.currentFirstCategory = this.currentFirstCategory;
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 重置加载商品参数
   * @param {*} data
   */

  reasetGoodsParam(data: {
    showCategoryId: string;
    current?: number;
    size?: number;
  }) {
    this.goodsParam = {
      ...this.goodsParam,
      ...data,
    };
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 一级分类选择切换
   * @param {string} v
   */

  curruntCateIdChange(v: string) {
    if (v && v !== "-1") {
      this.formData.currentFirstCategory = this.currentFirstCategory =
        this.formData.firstCatList.find(i => i.category.id === v) ||
        ({} as FirstCategory);
      this.$nextTick(() => {
        this.changeNav();
      });
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 切换一级分类
   */

  changeNav() {
    this.surplusClass = JSON.parse(
      JSON.stringify(this.currentFirstCategory.category.showCategoryVos),
    );
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择商品分组
   */

  chooseCategory() {
    this.dialogShow = true;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择一级分类
   * @param {ICategoryItem[]} list
   */

  selectedCategory(list: ICategoryItem[] = []) {
    const ls: Array<FirstCategory> = [...this.formData.firstCatList];
    list.forEach(i => {
      const temp = ls.find(t => t.category.id === i.id);
      if (!temp) {
        const first: FirstCategory = {
          category: i,
          isSelected: false,
          classChart: [],
          swiperList: [],
          navigation: [],
          navigationStyle: 1,
        };
        ls.push(first);
      }
    });
    this.formData.firstCatList = ls;
    bus.$emit("selectedFirstCategory");
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除一级分类
   * @param {string} id
   */

  handleGoodGroupDel(id: string) {
    const firstCatList = this.formData.firstCatList;
    const index = firstCatList.findIndex(item => item.category.id === id);
    const categoryId = this.currentFirstCategory.category.id;
    firstCatList.splice(index, 1);
    if (id === categoryId) {
      this.formData.currentFirstCategory = this.currentFirstCategory = {
        category: {} as ICategory,
        isSelected: false,
        classChart: [],
        swiperList: [],
        navigation: [],
        navigationStyle: 1,
      };
      bus.$emit("selectedFirstCategory");
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 被放置的容器触发事件，交换两个组件的位置
   * @param {number} i
   */

  handleDropClass(i: number) {
    if (this.classDragIndex === i) {
      return;
    }
    const temp = this.formData.firstCatList.splice(
      this.classDragIndex,
      1,
      this.formData.firstCatList[i],
    );
    this.formData.firstCatList.splice(i, 1, ...temp);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 阻止默认行为，否则drop事件不会触发
   * @param {*} e
   */

  handleDragoverClass(e: { preventDefault: () => void }) {
    e.preventDefault();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 开始拖动，记录拖动的组件下角标
   * @param {number} i
   */

  handleDragClass(i: number) {
    this.classDragIndex = i;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择图片上传
   * @param {*} file
   */

  async handleChange(file: { size: number }) {
    if (file.size > 1024 * 1024 * 2)
      return this.$message.error("文件过大，请压缩重试！");
    if (
      !(
        this.currentFirstCategory.category &&
        this.currentFirstCategory.category.id
      )
    )
      return this.$message.error("当前没有一级类目，请选择！");
    if (!this.checkoutSwiper())
      return this.$message.error("请填写完整再添加！");
    try {
      const img = await this.upLoadFile(file);
      if (!img) return;
      const swiperListItem: SwiperListItem = {
        title: this.currentFirstCategory.category.name,
        img,
        link: {
          id: 0,
          type: 0,
          name: "",
          url: "",
          append: "",
        },
        linkName: "",
      };
      this.currentFirstCategory.swiperList.push(swiperListItem);
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description:检测上一个海报是否已经填写完整
   * @returns {boolean}
   */

  checkoutSwiper(): boolean {
    const swiperList = this.currentFirstCategory.swiperList;
    let flag = true;
    swiperList.forEach(i => {
      if (!i.img) flag = false;
    });
    return flag;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 替换轮播图片
   * @param {*} file
   */

  async handleItemChange(file: any) {
    try {
      const img = await this.upLoadFile(file);
      if (!img) return;
      this.currentSwiperItem.img = img;
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击获取需要编辑的轮播项
   * @param {SwiperListItem} e
   */

  swiperItemClick(e: SwiperListItem) {
    this.currentSwiperItem = e;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择链接
   * @param {LinkSelectItem} e
   */

  handleLinkSelect(e: LinkSelectItem) {
    this.currentSwiperItem.link = e;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 图片上传方法
   * @param {*} file
   * @returns {string}
   */
  async upLoadFile(file: any): Promise<string> {
    try {
      const whiteList = ["image/jpeg", "image/jpg", "image/png", "image/gif"];
      const isLt2M = file.size < 2 * 1024 * 1024;
      if (!whiteList.includes(file.raw.type)) {
        this.$message.error("上传文件只能是 JPG或PNG 格式!");
        return "";
      }
      if (!isLt2M) {
        this.$message.error("上传文件大小不能超过 2MB!");
        return "";
      }
      const res = await upLoad({
        file: file.raw,
      });
      return res.data;
    } catch (error) {
      console.log(error);
      return "";
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除轮播组件中的图片
   * @param {number} idx
   */

  deleteSwiperItem(idx: number) {
    this.currentFirstCategory.swiperList.splice(idx, 1);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 开始拖动，记录拖动的组件下角标
   * @param {number} i
   */

  handleDragstart(i: number) {
    this.dragStarIndex = i;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 阻止默认行为，否则drop事件不会触发
   * @param {*} e
   */

  handleDragover(e: { preventDefault: () => void }) {
    e.preventDefault();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 被放置的容器触发事件，交换两个组件的位置
   * @param {number} i
   */

  handleDrop(i: number) {
    if (this.dragStarIndex === i) {
      return;
    }
    const temp = this.currentFirstCategory.swiperList.splice(
      this.dragStarIndex,
      1,
      this.currentFirstCategory.swiperList[i],
    );
    this.currentFirstCategory.swiperList.splice(i, 1, ...temp);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 开始拖动，记录拖动的组件下角标
   * @param {number} i
   */

  navigationDragstart(i: number) {
    this.navigationDrag = i;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 被放置的容器触发事件，交换两个组件的位置
   * @param {number} i
   */

  navigationDrop(i: number) {
    if (this.navigationDrag === i) {
      return;
    }
    const temp = this.currentFirstCategory.navigation.splice(
      this.navigationDrag,
      1,
      this.currentFirstCategory.navigation[i],
    );
    this.currentFirstCategory.navigation.splice(i, 1, ...temp);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除轮播组件中的图片
   * @param {number} idx
   */

  deletenavigationItem(idx: number) {
    this.currentFirstCategory.navigation.splice(idx, 1);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 添加导航
   */

  addNavigation() {
    const list = this.currentFirstCategory.navigation;
    if (list.length) {
      list.push({
        name: "",
        class: [],
        chartList: [],
        navType: 2,
      });
    } else {
      list.push({
        name: "其他",
        class: [],
        chartList: [],
        navType: 1,
      });
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 导航添加分类图
   * @param {Navigation} nav
   */

  navSelectClass(nav: Navigation) {
    const list: Chart[] = [];
    const temp = this.currentFirstCategory.classChart;
    nav.class.forEach((e: string) => {
      const i = temp.find(k => k.category.id === e) || ({} as Chart);
      list.push(i);
    });
    nav.chartList = list;
    this.resetOtherNavigation();
    this.sortNavigationByClass();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 重置其他导航数据
   */

  resetOtherNavigation() {
    const { navigation = [], classChart = [] } = this.currentFirstCategory;
    const ids: string[] = [];
    navigation.forEach(i => {
      if (i.navType !== 1) {
        ids.push(...i.class);
      }
    });
    const temp = navigation.find(i => i.navType === 1) || ({} as Navigation);
    temp.chartList = classChart.filter(k => !ids.includes(k.title));
    temp.class = [];
    temp.chartList.forEach(k => {
      temp.class.push(k.title);
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 对导航信息中的分类图进行排序
   */

  sortNavigationByClass() {
    const { navigation = [], classChart = [] } = this.currentFirstCategory;
    navigation.forEach(i => {
      i.chartList.forEach(k => {
        k.sortId = classChart.findIndex(i => i.category.id === k.category.id);
      });
      i.chartList.sort((a, b) => (a.sortId > b.sortId ? 1 : -1));
      i.class = i.chartList.reduce((a: string[], b) => {
        a.push(b.category.id);
        return a;
      }, []);
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 添加分类图 被放置的容器触发事件，交换两个组件的位置
   * @param {number} i
   */

  chartDrop(i: number) {
    if (this.chartDragIndex === i) {
      return false;
    }
    const temp = this.currentFirstCategory.classChart.splice(
      this.chartDragIndex,
      1,
      this.currentFirstCategory.classChart[i],
    );
    this.currentFirstCategory.classChart.splice(i, 1, ...temp);
    this.sortNavigationByClass();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 添加分类图 开始拖动，记录拖动的组件下角标
   * @param {number} i
   */

  chartDragstart(i: number) {
    this.chartDragIndex = i;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 添加分类图 删除轮播组件中的图片
   * @param {number} idx
   */

  deleteChartItem(idx: number) {
    const classChart = this.currentFirstCategory.classChart.splice(
      idx,
      1,
    )[0] as any;
    const { navigation = [] } = this.currentFirstCategory;
    navigation.forEach(i => {
      const tIdx = i.chartList.findIndex(k => k.title === classChart.title);
      const cIdx = i.class.findIndex(k => k === classChart.title);
      if (cIdx !== -1) {
        i.chartList.splice(tIdx, 1);
        i.class.splice(cIdx, 1);
      }
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 添加分类图 点击获取需要编辑的轮播项
   * @param {Chart} e
   */

  chartItemClick(e: Chart) {
    this.currentChartItem = e;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 添加分类图 替换分类图片
   * @param {*} file
   */

  async handleChartChange(file: any) {
    try {
      const img = await this.upLoadFile(file);
      if (!img) return;
      this.currentChartItem.img = img;
      // 检测是不是修改
      const navigations = this.currentFirstCategory.navigation;
      let flag = true,
        temp = {} as any,
        index = 0;
      navigations.forEach(i => {
        const idx = i.class.findIndex(
          s => s === (this.currentChartItem as any).category.id,
        );
        if (idx !== -1) {
          flag = false;
          temp = i;
          index = idx;
        }
      });
      if (!flag) {
        // 如果不是新增则修改对应值
        temp.chartList.splice(index, 1, this.currentChartItem);
      }
      const idx = this.formData.firstCatList.findIndex(
        i => i.category.id === this.currentFirstCategory.category.id,
      );
      this.formData.firstCatList.splice(idx, 1, this.currentFirstCategory);
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 添加分类图 选择图片上传
   * @param {*} file
   */

  async chartHandleChange(file: any) {
    if (file.size > 1024 * 1024 * 2)
      return this.$message.error("文件过大，请压缩重试！");
    if (
      !(
        this.currentFirstCategory.category &&
        this.currentFirstCategory.category.id
      )
    )
      return this.$message.error("请添加一级分类！");
    if (!this.isShowAddClass())
      return this.$message.error("已无可添加的二级分类，请先添加二级分类！");
    if (!this.checkLastClassChart())
      return this.$message.error("请将上一个分类图填写完整！");
    try {
      const img = await this.upLoadFile(file);
      if (!img) return;
      const temp: any = {
        title: "",
        img,
        category: {},
      };
      this.currentFirstCategory.classChart.push(temp);
    } catch (error) {
      console.log(error);
    }
  }
  /**
   * @LastEditors: chuyinlong
   * @description: 判断是否还显示添加分类图按钮
   * 如果当前一级分类下面的二级分类均已被分类图选了，就不再显示添加分类图按钮
   * @returns {boolean}
   */

  isShowAddClass(): boolean {
    const { showCategoryVos = [] } = this.currentFirstCategory.category;
    const { classChart = [] } = this.currentFirstCategory;
    let flag = false;
    for (const i of showCategoryVos) {
      if (!classChart.find(k => k.title === i.id)) {
        flag = true;
        break;
      }
    }
    return flag;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 判断最后一个二级分类图是否已经选择二级分类
   * 如果未选择，则不允许添加新分类图
   * @returns {boolean}
   */

  checkLastClassChart(): boolean {
    const { classChart = [] } = this.currentFirstCategory;
    let flag = true;
    if (classChart.length && !classChart[classChart.length - 1].title)
      flag = false;
    return flag;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 添加分类图 选择二级分类
   * @param {Chart} item
   */

  classSelectChange(item: Chart) {
    const navigations = this.currentFirstCategory.navigation;
    const isAlter = Boolean(item.category.id);
    let temp: Navigation = {} as Navigation,
      index = 0;
    // 如果是修改
    if (isAlter) {
      navigations.forEach(i => {
        const idx = i.class.findIndex(id => id === item.category.id);
        if (idx !== -1) {
          temp = i;
          index = idx;
        }
      });
    }
    item.category =
      this.currentFirstCategory.category.showCategoryVos.find(
        i => i.id === item.title,
      ) || ({} as ICategoryItemVos);
    // 添加的分类图默认添加到导航里面去
    if (!navigations.length) {
      navigations.push({
        name: "其他",
        class: [],
        chartList: [],
        navType: 1,
      });
    }
    if (!isAlter) {
      navigations[0].class.push(item.category.id);
      navigations[0].chartList.push(item);
    } else {
      // 如果不是新增则修改对应值
      temp.class.splice(index, 1, item.category.id);
      temp.chartList.splice(index, 1, item);
    }
    const idx = this.formData.firstCatList.findIndex(
      i => i.category.id === this.currentFirstCategory.category.id,
    );
    this.formData.firstCatList.splice(idx, 1, this.currentFirstCategory);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 颜色选择切换
   * @param {string} name
   */

  colorSelectChange(name: string) {
    const type = this.formData[name];
    const color: { [x: string]: string } = {
      fontColor: "#FFFFFF",
      fontBg: "#FD4E64",
      fontNotColor: "#000000",
      fontNotBg: "#FFFFFF",
    };
    if (!type) {
      this.formData[name] = color[name];
    }
  }
}
</script>
<style lang="scss">
@import "@/assets/styles/decoration/businessSuper";
</style>
