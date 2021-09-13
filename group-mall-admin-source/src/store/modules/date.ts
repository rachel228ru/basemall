export default class DateUtil {
  ms: any | undefined;

  constructor(ms: Date | any = new Date()) {
    // console.warn(this)
    this.ms = ms;
  }

  /**
   * @method
   * @description 测试时间参数
   * @param {} ms
   */
  GMTTest(ms: string) {
    ms = ms || this.ms;
    const GMT = new Date(this.iosFormat(ms));
    return GMT;
  }

  /**
   * @method
   * @description 苹果兼容
   * @param {String} str
   */
  iosFormat(str: string) {
    if (String(str).match(/-/)) {
      str = String(str).replace(/-/g, "/");
    }
    return str;
  }

  /**
   * @method
   * @description 获取年
   * @param {*} ms
   */
  getY(ms?) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const y = GMT.getFullYear();
    return y;
  }

  /**
   * @method
   * @description 获取月
   * @param {*} ms
   */
  getM(ms?) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const m = GMT.getMonth() + 1;
    return this.formatLength(m);
  }

  /**
   * @method
   * @description 获取日
   * @param {*} ms
   */
  getD(ms) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const d = GMT.getDate();
    return this.formatLength(d);
  }

  /**
   * @method
   * @description 获取时
   * @param {*} ms
   */
  getH(ms) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const H = GMT.getHours();
    return this.formatLength(H);
  }

  /**
   * @method
   * @description 获取时
   * @param {*} ms
   */
  getMin(ms) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const M = GMT.getMinutes();
    return this.formatLength(M);
  }

  /**
   * @method
   * @description 获取时
   * @param {*} ms
   */
  getS(ms) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const S = GMT.getSeconds();
    return this.formatLength(S);
  }

  /**
   * @method
   * @description 获取年/月/日
   * @param {} ms
   */
  getYMD(ms) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const y = GMT.getFullYear();
    const m = GMT.getMonth() + 1;
    const d = GMT.getDate();
    // return y+'-'+m+'-'+d; // 苹果不识别'-'
    return [y, m, d].map(this.formatLength).join("/");
  }

  /**
   * @method
   * @description 获取年-月-日
   * @param {} ms
   */
  getYMDs(ms) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const y = GMT.getFullYear();
    const m = GMT.getMonth() + 1;
    const d = GMT.getDate();
    // return y+'-'+m+'-'+d; // 苹果不识别'-'
    return [y, m, d].map(this.formatLength).join("-");
  }

  /**
   * @method
   * @description 获取年-月
   * @param {} ms
   */
  getYMs(ms) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const y = GMT.getFullYear();
    const m = GMT.getMonth() + 1;
    // return y+'-'+m+'-'+d; // 苹果不识别'-'
    return [y, m].map(this.formatLength).join("-");
  }

  /**
   * @method
   * @description 获取月-日
   * @param {} ms
   */
  getMDs(ms) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const m = GMT.getMonth() + 1;
    const d = GMT.getDate();
    // return y+'-'+m+'-'+d; // 苹果不识别'-'
    return [m, d].map(this.formatLength).join("-");
  }

  /**
   * @method
   * @description 获取时: 分: 秒
   * @param {} ms
   */
  getHMS(ms) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const h = GMT.getHours();
    const m = GMT.getMinutes();
    const s = GMT.getSeconds();
    // return h+'-'+m+'-'+s; // 苹果不识别'-'
    return [h, m, s].map(this.formatLength).join(":");
  }

  /**
   * @method
   * @description 获取年/月/日 时: 分: 秒
   * @param {} ms
   */
  getYMDHMS(ms) {
    ms = ms || this.ms;
    return this.getYMD(ms) + " " + this.getHMS(ms);
  }

  /**
   * @method
   * @description 获取年-月-日 时: 分: 秒
   * @param {} ms
   */
  getYMDHMSs(ms = this.ms) {
    return this.getYMDs(ms) + " " + this.getHMS(ms);
  }

  /**
   * @method
   * @description 年月日加天数
   * @param {} ms
   */
  getAddDays(ms, day) {
    ms = ms || this.ms;
    let GMT = this.getTime(ms);
    GMT = GMT + day * 24 * 60 * 60 * 1000;
    const Y = this.getY(GMT);
    const M = this.getM(GMT);
    const D = this.getD(GMT);
    return [Y, M, D].map(this.formatLength).join("-");
  }

  /**
   * @method
   * @description 获取毫秒数
   * @param {*} ms
   */
  getTime(ms) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    return GMT.getTime();
  }

  getObj(ms) {
    ms = ms || this.ms;
    const GMT = this.GMTTest(ms);
    const Y = GMT.getFullYear();
    const M = GMT.getMonth() + 1;
    const D = GMT.getDate();
    const h = GMT.getHours();
    const m = GMT.getMinutes();
    const s = GMT.getSeconds();
    return [Y, M, D, h, m, s].map(this.formatLength);
  }

  /**
   * @method
   * @description 格式化单位长度
   * @param {} ms
   */
  formatLength(ms) {
    return String(ms)[1] ? ms : "0" + ms;
  }
}
