/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 09:31:15
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-31 17:06:54
 */
export function Map(ak) {
  return new Promise((resolve, reject) => {
    window.init = () => {
      resolve(BMap);
    };
    const script = document.createElement("script");
    script.type = "text/javascript";
    script.src =
      "https://api.map.baidu.com/api?v=2.0&ak=" + ak + "&callback=init";
    script.onerror = reject;
    document.head.appendChild(script);
  });
}
