/**
 * 计算价格公式
 * @param packagePrice 套餐价格
 * @param packagePriceUnit 套餐单位
 * @param days 购买天数
 */
export function computePrice(packagePrice, packagePriceUnit, days) {
  const map = {
    1: 1,
    2: 30,
    3: 365,
  };
  const unitPrice = packagePrice / map[packagePriceUnit];
  return days * unitPrice;
}

/**
 * 获取基本价格公式
 * @param packagePrice 套餐价格
 * @param packagePriceUnit 套餐单位
 * @param days 购买天数
 */
export function getPrice(packagePrice, packagePriceUnit, days) {
  let price = 0;
  if (packagePrice && packagePriceUnit && days) {
    price = computePrice(packagePrice, packagePriceUnit, days);
  }
  return price.toFixed(2);
}

/**
 * 升级价格计算公式
 * @param oldMeal 原本老套餐
 * @param newMeal 升级新套餐
 * @param remainDay 剩余天数
 */
export function upgradePrice(oldMeal, newMeal, remainDay) {
  const oldPrice = getPrice(
    oldMeal.packagePrice,
    oldMeal.packagePriceUnit,
    remainDay,
  );
  const newPrice = getPrice(
    newMeal.packagePrice,
    newMeal.packagePriceUnit,
    remainDay,
  );
  return (Number(newPrice) - Number(oldPrice)).toFixed(2);
}
