import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";

export default class VideoCom {
  VideoCom = "图片";

  radio = 1;

  radioBL = 1;

  radioTC = 1;

  video = "";

  videoLink = "";

  poster = "";

  width = "100%";

  height = "200px";

  link = {};
}

export interface videoItem {
  title: string;
  img: string;
  link: LinkSelectItem;
  linkName: string;
}
