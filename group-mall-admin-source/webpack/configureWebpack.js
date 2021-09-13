/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:48:42
 */
const CompressionWebpackPlugin = require("compression-webpack-plugin");
const cdnDependencies = require("./dependencies-cdn");

const externals = {};
cdnDependencies.forEach(pkg => {
  externals[pkg.name] = pkg.library;
});

exports.externals = externals;

module.exports = config => {
  if (process.env.NODE_ENV === "production") {
    config.externals = externals;


    config.plugins = [
      ...config.plugins,
      // gzip
      new CompressionWebpackPlugin({
        algorithm: "gzip",
        test: /\.(js|css|json|txt|html|ico|svg)(\?.*)?$/i,
        threshold: 10240,
        minRatio: 0.8,
        deleteOriginalAssets: false,
      }),
    ];
  }
};
