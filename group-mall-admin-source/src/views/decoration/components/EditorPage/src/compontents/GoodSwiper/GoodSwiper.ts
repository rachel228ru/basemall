import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";

export interface SwiperListItem {
  title: string;
  img: string;
  link: LinkSelectItem;
  linkName: string;
}

/**
 * 轮播图
 */
export default class GoodSwiperFormData {
  type = "GoodSwiper";

  radio = 1;

  /** 底图输入框 */
  btmInput = "";

  /** 底图图片 */
  btnImg = "https://qiniu-app.qtshe.com/u391.png";

  /** 底图链接 */
  btnlink = "";

  /** 轮播间距 */
  margin = 8;

  swiperList: SwiperListItem[] = [];

  padding = 14;

  /** 商品侧边距 */
  sidePadding = 14;

  /** 图片样式 1常规 2投影 */
  imageStyle = 1;

  /** 图片倒角 1直角 2圆角 */
  imageAngle = 1;

  /** 指示器 1样式一 2样式二 3样式三 */
  indicator = 1;

  /** 图片高度 */
  height = 420;

  shownum = 3;
}
