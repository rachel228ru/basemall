/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:24:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-03 10:25:11
 */
const chainWebpack = require("./webpack/chainWebpack");
const configureWebpack = require("./webpack/configureWebpack");
const css = require("./webpack/css");

module.exports = {
  lintOnSave: true,
  publicPath: process.env.VUE_APP_BASEPATH,
  chainWebpack,
  configureWebpack,
  css,
  productionSourceMap: false,
  devServer: {
    proxy: {
      '/api/': {
        target: 'https://*', // 接口域名
        changeOrigin: true  //是否跨域
      },
    },
    overlay: {
      warnings: true,
      errors: true,
    },
    progress: false,
  },
};
