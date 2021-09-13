class Storage {
  get len() {
    return localStorage.length;
  }

  /**
   * 获取数据
   * @param key
   */
  public get(key) {
    const data = localStorage.getItem(key);
    try {
      return data && JSON.parse(data);
    } catch (e) {
      return data;
    }
  }

  /**
   * 设置缓存
   * @param key
   * @param value
   */
  public set(key, value, override?: boolean) {
    if (override) {
      const setItemEvent: any = new Event("setItemEvent");
      setItemEvent.info = {
        key,
        value,
      };
      window.dispatchEvent(setItemEvent);
    }
    localStorage.setItem(key, JSON.stringify(value));
  }

  /**
   * 删除缓存
   * @param key
   */
  public delete(key) {
    localStorage.removeItem(key);
  }
}

export default new Storage();
