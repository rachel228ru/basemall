export interface IGoods {
  goods: any[];
  type: "big" | "swiper" | "line" | "detail";
  style: "white" | "card" | "border";
}

export interface IComponentItem {
  value: string;
  label: string;
  icon: string;
}

export interface IComponent extends IComponentItem {
  id: number;
  formData: any;
}
