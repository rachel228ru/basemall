export const sleep = (time: number) => {
  return new Promise(resolve => {
    setTimeout(resolve, time);
  });
};

/**
 * 是否为质数
 */
export const isPrimeNum = (num: number) => {
  for (let i = 2; i < num; i++) {
    if (num % i === 0) {
      return false;
    }
  }
  return true;
};
