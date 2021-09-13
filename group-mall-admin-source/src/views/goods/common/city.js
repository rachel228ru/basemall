/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-21 18:33:49
 */
import address from "./area.json";
import { getArea } from "@/api/global";

class City {
  address = address;

  province = [];

  constructor() {
    // this.address
    getArea({ type: 1 }).then(res => {
      this.province = res.data;
    });
  }

  getCityAsync(id) {
    return getArea({ type: 2, id }).then(res => res.data);
  }

  getProvince() {
    const list = [];
    for (const item of this.address) {
      list.push({
        label: item.label,
        value: item.value,
      });
    }
    return list;
  }

  getCity(id) {
    const list = [];
    for (const item of this.address) {
      if (item.value === id) {
        for (const province of item.list) {
          list.push({
            label: province.label,
            value: province.value,
          });
        }
      }
    }
    return list;
  }

  getArea(province, city) {
    const list = [];
    for (const item of this.address) {
      if (item.value === province) {
        for (const province of item.list) {
          if (province.value === city) {
            for (const city of province.list) {
              list.push({
                label: city.label,
                value: city.value,
              });
            }
          }
        }
      }
    }
    return list;
  }

  returnAddress(province, city) {
    const p = this.getProvince();
    const c = this.getCity(province);
    const a = this.getArea(province, city);
    return {
      province: p,
      city: c,
      area: a,
    };
  }

  getAddressName(list, value) {
    const name = "";
    if (list.length > 0) {
      for (const item of list) {
        if (item.value === value) {
          name = item.label;
          break;
        }
      }
    }
    return name;
  }
}

export default new City();
