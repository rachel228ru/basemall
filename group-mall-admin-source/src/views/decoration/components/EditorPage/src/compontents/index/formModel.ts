/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-24 10:13:53
 */
import HomeSwiper from "../HomeSwiper/HomeSwiper";
import GoodSwiper from "../GoodSwiper/GoodSwiper";
import Goods from "../Goods/Goods";
import Search from "../Search/Search";
import TitleBar from "../TitleBar/TitleBar";
import BlankPaceholder from "../BlankPaceholder/BlankPaceholder";
import Separator from "../Separator/Separator";
import StoreNavigation from "../StoreNavigation/StoreNavigation";
import NavBar from "../NavBar/NavBar";
import CubeBox from "../CubeBox/CubeBox";
import RichText from "../RichText/RichText";
import BusinessSuper from "../../../../BusinessSuper/BusinessSuper";
import VideoCom from "../VideoCom/VideoCom";
import ImageCom from "../ImageCom/ImageCom";
class EdtiorFormData {
  HomeSwiper = HomeSwiper;

  Goods = Goods;

  Search = Search;

  TitleBar = TitleBar;

  BlankPaceholder = BlankPaceholder;

  Separator = Separator;

  StoreNavigation = StoreNavigation;

  NavBar = NavBar;

  CubeBox = CubeBox;

  RichText = RichText;

  BusinessSuper = BusinessSuper;

  ImageCom = ImageCom;

  VideoCom = VideoCom;

  GoodSwiper = GoodSwiper;

  [x: string]: any;
}
export default EdtiorFormData;
