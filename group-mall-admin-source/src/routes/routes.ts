/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 09:54:06
 */
import MContent from "@/components/MContent.vue";
import MMain from "@/components/MMain.vue";

interface RouteMeta {
  icon: string;
  title: string;
  islink: number;
  mode: string;
}

const routes = [
  {
    path: "/overview",
    // 临时比对本地版本用
    version: +new Date(),
    component: MMain,
    meta: {
      icon: "iconjiankonggaikuang_huaban",
      title: "经营概况",
      islink: 1,
    },
    children: [
      {
        path: "",
        name: "OverView",
        meta: {
          title: "经营概况",
          isShow: 1,
        },
        show: true,
        component: () => import("@/views/manage/Index.vue"),
      },
    ],
  },
  {
    path: "",
    component: MMain,
    meta: {
      icon: "iconshangpinguanli",
      title: "商品管理",
    },
    children: [
      {
        path: "goods",
        component: MContent,
        meta: {
          title: "商品列表",
        },
        show: true,
        children: [
          {
            path: "",
            name: "Goods",
            component: () =>
              import(
                /* webpackChunkName: "goods" */ "@/views/goods/marketModel/Goods.vue"
              ),
          },
          {
            path: "addGood",
            name: "AddGood",
            component: () =>
              import(
                /* webpackChunkName: "goods" */ "@/views/goods/marketModel/AddGood.vue"
              ),
            meta: {
              title: "发布商品",
              noPadding: true,
            },
            show: true,
          },
        ],
      },
      {
        path: "goodRegion",
        // name: "goodRegion",
        component: MContent,
        meta: {
          title: "设置专区",
        },
        show: true,
        children: [
          {
            path: "",
            name: "goodRegion",
            component: () =>
              import(
                /* webpackChunkName: "goods" */ "@/views/goods/goodManage/GoodRegion.vue"
              ),
          },
          {
            path: "class",
            name: "class",
            show: true,
            component: () =>
              import(
                /* webpackChunkName: "goods" */ "@/views/goods/goodManage/GoodClass.vue"
              ),
            meta: {
              title: "商品分类",
            },
          },
        ],
      },
      {
        path: "attributeTemple",
        name: "Attribute",
        show: true,
        component: () =>
          import(
            /* webpackChunkName: "goods" */ "@/views/goods/goodManage/AttributeTemple.vue"
          ),
        meta: {
          title: "属性模板",
        },
      },
      {
        path: "supplier",
        name: "sup",
        show: true,
        component: () =>
          import(
            /* webpackChunkName: "goods" */ "@/views/goods/goodManage/SupplierManage.vue"
          ),
        meta: {
          title: "供货商",
        },
      },
      {
        path: "csvImport",
        name: "csvImport",
        show: true,
        component: MContent,
        meta: {
          title: "素材导入",
        },
        children: [
          {
            path: "",
            name: "csvList",
            component: () =>
              import(
                /* webpackChunkName: "goods" */ "@/views/goods/csvImport/CsvIndex.vue"
              ),
          },
          {
            path: "editGood",
            name: "editGood",
            component: () =>
              import(
                /* webpackChunkName: "goods" */ "@/views/goods/csvImport/EditGood.vue"
              ),
            meta: {
              title: "编辑商品",
              noPadding: true,
            },
            show: true,
          },
        ],
      },
    ],
  },
  {
    path: "/order",
    component: MMain,
    meta: {
      title: "订单管理",
      icon: "icondingdan",
    },
    children: [
      {
        path: "delivery",
        meta: {
          title: "快递订单",
        },
        show: true,
        component: MContent,
        children: [
          {
            meta: {
              title: "",
            },
            path: "",
            component: () =>
              import(
                /* webpackChunkName: "order" */ "@/views/order/DeliveryOrder.vue"
              ),
          },
          {
            path: "send",
            meta: {
              title: "批量发货",
              noPadding: true,
            },
            component: () =>
              import(
                /* webpackChunkName: "order" */ "@/views/order/DeliverySend.vue"
              ),
          },
        ],
      },
      {
        path: "evaluation",
        component: () =>
          import(
            /* webpackChunkName: "order" */ "@/views/order/Evaluation.vue"
          ),
        meta: {
          title: "评价管理",
        },
        show: true,
      },
      {
        path: "afterSale",
        component: () =>
          import(
            /* webpackChunkName: "order" */ "@/views/order/AfterSaleOrder.vue"
          ),
        meta: {
          title: "售后工单",
        },
        show: true,
      },
    ],
  },
  {
    path: "/customer",
    component: MMain,
    meta: {
      title: "客户管理",
      icon: "iconkehu",
    },
    children: [
      {
        path: "list",
        component: MContent,
        children: [
          {
            path: "",
            name: "list",
            component: () =>
              import(
                /* webpackChunkName: "goods" */ "@/views/customer/list/Index.vue"
              ),
          },
        ],
        meta: {
          title: "客户列表",
        },
        show: true,
      },
      {
        path: "blacklist",
        name: "blacklist",
        component: () =>
          import(
            /* webpackChunkName: "blacklist" */ "@/views/customer/blacklist/Index.vue"
          ),
        meta: {
          title: "黑名单",
        },
        show: true,
      },
    ],
  },
  {
    path: "/distribution",
    component: MMain,
    meta: {
      title: "配送方式",
      icon: "icondaifahuo",
    },
    children: [
      {
        path: "logistics",
        component: () =>
          import(
            /* webpackChunkName: "finance" */ "@/views/logistics/logistics/Index.vue"
          ),
        meta: {
          title: "快递配送",
        },
        show: true,
      },
    ],
  },
  {
    path: "/setting",
    component: MMain,
    meta: {
      title: "商城设置",
      icon: "iconshangpinxiangqing-dianpu",
    },
    children: [
      {
        path: "channel",
        component: () =>
          import(/* webpackChunkName: "setting" */ "@/views/channel/Index.vue"),
        meta: {
          title: "销售渠道",
        },
        show: true,
      },
      {
        path: "editorPage",
        redirect: "/editorPage",
        component: () =>
          import(
            /* webpackChunkName: "setting" */ "@/views/decoration/components/EditorPage/src/Editor.vue"
          ),
        meta: {
          title: "装修",
          // isShow: 1,
        },
        show: true,
      },
      {
        path: "",
        name: "setting",
        component: () =>
          import(/* webpackChunkName: "setting" */ "@/views/setting/Index.vue"),
        meta: {
          title: "通用设置",
        },
        show: true,
      },
    ],
  },
  {
    path: "/logistics",
    component: MMain,
    meta: {
      title: "物流管理",
      isShow: 1,
    },
    children: [
      {
        path: "logistics",
        name: "logistics",
        component: () =>
          import(
            /* webpackChunkName: "logistics" */ "@/views/logistics/logistics/Index.vue"
          ),
        meta: {
          title: "快递配送",
        },
        show: true,
      },
    ],
  },
  {
    path: "/market",
    component: MMain,
    meta: {
      title: "商超系统",
      isShow: 1,
    },
    children: [
      {
        path: "goods",
        component: MContent,
        meta: {
          title: "商品列表",
        },
        show: true,
        children: [
          {
            path: "",
            name: "Goods",
            component: () =>
              import(
                /* webpackChunkName: "goods" */ "@/views/goods/marketModel/Goods.vue"
              ),
          },
          {
            path: "addGood",
            name: "AddGood",
            component: () =>
              import(
                /* webpackChunkName: "goods" */ "@/views/goods/marketModel/AddGood.vue"
              ),
            meta: {
              title: "发布商品",
            },
          },
        ],
      },
    ],
  },
  {
    path: "/CategoryTem",
    component: MMain,
    meta: {
      title: "商品属性",
      isShow: 1,
    },
    children: [
      {
        path: "/",
        name: "CategoryTem",
        component: () =>
          import(
            /* webpackChunkName: "CategoryTem" */ "@/views/goods/goodManage/components/CategoryTem.vue"
          ),
        meta: {
          title: "商品属性",
        },
        show: true,
      },
    ],
  },
  {
    path: "/business",
    component: MMain,
    meta: {
      title: "商家中心",
      isShow: 1,
    },
    children: [
      {
        path: "/",
        name: "businessCenter",
        component: () =>
          import(
            /* webpackChunkName: "businessCenter" */ "@/views/businessCenter/Index.vue"
          ),
        meta: {
          title: "",
        },
        show: true,
      },
    ],
  },
  {
    path: "/editorPage",
    name: "editorPage",
    component: () =>
      import(
        /* webpackChunkName: "CategoryTem" */ "@/views/decoration/components/EditorPage/src/Editor.vue"
      ),
    meta: {
      title: "装修",
      isShow: 1,
    },
  },
  {
    path: "/changepass",
    name: "changepass",
    component: () =>
      import(
        /* webpackChunkName: "changepass" */ "@/views/businessCenter/Account/ChangePassword.vue"
      ),
    meta: {
      title: "修改密码",
      isShow: 1,
    },
  },
  {
    path: "/redirect/:type",
    name: "redirect",
    component: () =>
      import(/* webpackChunkName: "CategoryTem" */ "@/views/sign/Redirect.vue"),
    meta: {
      title: "重定向页面",
      isShow: 1,
    },
  },
  {
    path: "/static",
    component: MContent,
    meta: {
      title: "",
      isShow: 1,
    },
    children: [
      {
        path: "protocol",
        name: "protocol",
        component: () =>
          import(
            /* webpackChunkName: "meal" */ "@/views/businessCenter/Static/Protocol.vue"
          ),
        meta: {
          title: "注册协议",
        },
      },
      {
        path: "privacy",
        name: "privacy",
        component: () =>
          import(
            /* webpackChunkName: "meal" */ "@/views/businessCenter/Static/Privacy.vue"
          ),
        meta: {
          title: "隐私政策",
        },
      },
      {
        path: "order",
        name: "order",
        component: () =>
          import(
            /* webpackChunkName: "meal" */ "@/views/businessCenter/Static/Order.vue"
          ),
        meta: {
          title: "订购及服务协议",
        },
      },
      {
        path: "register",
        name: "register",
        component: () =>
          import(
            /* webpackChunkName: "meal" */ "@/views/businessCenter/Static/Register.vue"
          ),
        meta: {
          title: "开户及服务协议",
        },
      },
    ],
  },
  {
    path: "/",
    component: MMain,
    meta: {
      title: "商品管理",
      isShow: 1,
    },
    children: [
      {
        path: "",
        component: MContent,
        meta: {
          title: "商超商品",
        },
        show: true,
        children: [
          {
            path: "",
            name: "index",
            component: () =>
              import(
                /* webpackChunkName: "goods" */ "@/views/goods/marketModel/Goods.vue"
              ),
          },
        ],
      },
    ],
  },
  {
    path: "*",
    name: "404",
    component: () =>
      import(/* webpackChunkName: "404" */ "@/views/sign/404.vue"),
    meta: {
      title: "404",
      isShow: 1,
    },
  },
];

export default routes;
