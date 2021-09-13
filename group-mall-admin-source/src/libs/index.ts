/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:43:04
 */
import storage from "@/libs/storage";
import { userStore } from "@/store/modules/user";
import Component from "vue-class-component";
import Vue from "vue";

// You can declare a mixin as the same style as components.
@Component
export default class DialogMixin extends Vue {
  dialogVisible = false;

  /**
   * 关闭弹窗
   */
  async closeHandle() {
    try {
      const result = await this.confirmHandle();
      if (result === "confirm") {
        this.dialogVisible = false;
      }
    } catch (e) {
      console.log(e);
    }
  }

  async beforeCloseHandle(done) {
    try {
      const result = await this.confirmHandle();
      if (result === "confirm") {
        done();
      }
    } catch (e) {
      console.log(e);
    }
  }

  /**
   * 关闭确认
   */
  async confirmHandle() {
    return await this.$confirm(
      "确定退出该窗口？退出后，数据可能不保留！",
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      },
    );
  }
}

/**
 * 前端获取UUid
 */
export function getUuid() {
  const s = [];
  const hexDigits = "0123456789abcdef";
  for (let i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23] = "-";
  const uuid = s.join("");
  return uuid;
}

/**
 * 创建微信扫码iframe窗口
 */
export function createQrCode(object) {
  const d = document.createElement("iframe");
  d.src = object.url;
  setTimeout(() => {
    d.frameBorder = "0";
    d.scrolling = "no";
    d.width = "100%";
    d.height = object.height;
    d.id = "iframeId";
    const f = document.getElementById(object.id);
    f.innerHTML = "";
    f.appendChild(d);
  }, 100);
}

/**
 * 简单的节流函数
 */
export function throttle(func, wait, mustRun) {
  let timeout;
  let startTime: any = new Date();

  return () => {
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    const context = this;
    const args = arguments;
    const curTime: any = new Date();

    clearTimeout(timeout);
    // 如果达到了规定的触发时间间隔，触发 handler
    if (curTime - startTime >= mustRun) {
      func.apply(context, args);
      startTime = curTime;
      // 没达到触发间隔，重新设定定时器
    } else {
      timeout = setTimeout(func, wait);
    }
  };
}

/**
 * 通过url下载图片
 */
export function getUrlBase64(url, callback, ext = "") {
  // 创建canvas DOM元素
  let canvas = document.createElement("canvas");
  const ctx = canvas.getContext("2d");
  const img = new Image();
  img.crossOrigin = "Anonymous"; // 支持跨域
  img.src = url;
  img.onload = () => {
    // 指定画板的高度,自定义
    canvas.height = 300;
    // 指定画板的宽度，自定义
    canvas.width = 300;
    // 参数可自定义
    ctx.drawImage(img, 0, 0);
    // 传递的自定义的参数
    const dataURL = canvas.toDataURL("image/" + ext);
    // 回掉函数获取Base64编码
    callback.call(this, dataURL);
    canvas = null;
  };
}

/** 修复el Dropdown下拉 */
export const dropdownHack = () => {
  const admin = document.querySelector(".admin");

  if (admin) {
    admin.addEventListener("scroll", () => {
      const els = document.querySelectorAll(".el-dropdown-menu") as any;
      Array.from(els).map((el: HTMLElement) => {
        el.style.display = "none";
      });
    });
  }
};

/**
 * 切换为登录状态
 */
export function modifySignStatus(token, userInfo) {
  userStore.setToken(token);
  userStore.setUserInfo(userInfo);
}

/**
 * localStorage里存入登录信息
 * @param token
 * @param userInfo
 */
export function login(token, userInfo) {
  const time = new Date();
  storage.set("time", time.getTime());
  storage.set("token", token);
  storage.set("userInfo", userInfo);
}

/**
 * 清除localStorage登录信息
 */
export function logout() {
  storage.delete("time");
  storage.delete("token");
  storage.delete("userInfo");
  storage.delete("shopInfo");
}
