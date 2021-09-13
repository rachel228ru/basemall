/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 11:31:16
 */
import axios from "axios";

const gdkey = "f84ae9ba9009c49687d6e09c3e15c1fd";

interface IRegionSelectParams {
  /** 请求服务权限标识 */
  key: string;

  /** 查询关键字 */
  keywords: string;

  /**
   * 子级行政区
   * 规则：设置显示下级行政区级数（行政区级别包括：国家、省/直辖市、市、区/县、乡镇/街道多级数据）
   * 可选值：0、1、2、3等数字，并以此类推
   * 0：不返回下级行政区；
   * 1：返回下一级行政区；
   * 2：返回下两级行政区；
   * 3：返回下三级行政区；
   */
  subdistrict: number;

  /** 需要第几页数据 */
  page: number;

  /** 返回结果控制 可选值：base、all */
  extensions: string;
}

/**
 * 获取行政区划
 */
export function districtParameters() {
  return axios({
    url: "https://restapi.amap.com/v3/config/district?parameters",
    method: "GET",
    params: {
      key: gdkey,
      keywords: "中华人民共和国",
      subdistrict: 3,
      page: 1,
      extensions: "base",
    },
  });
}

/**
 * 根据 cityCode 获取 对应的 adCode
 * @param {string} cityCode
 */
export async function getadCodBycityCode(cityCode = "") {
  try {
    const res: any = await axios({
      url: "https://restapi.amap.com/v3/config/district?parameters",
      method: "GET",
      params: {
        key: gdkey,
        keywords: cityCode,
        subdistrict: 0,
        page: 1,
        extensions: "base",
      },
    });
    if (res.data.districts) {
      const districtItem = res.data.districts.find(
        (item: { level: string }) => item.level === "city",
      );
      return districtItem.adcode;
    } else {
      return Promise.reject(false);
    }
  } catch (error) {
    return Promise.reject(error);
  }
}

/**
 * 根据经纬度查询地址详情
 * @param {string} location 经纬度 116.473168,39.993015
 */
export function placeAround(location = "") {
  return axios({
    url: "https://restapi.amap.com/v3/place/around?parameters",
    method: "GET",
    params: {
      key: gdkey,
      extensions: "all",
      location,
    },
  });
}
