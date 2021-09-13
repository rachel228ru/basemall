// const path = require("path");
const cdnDependencies = require("./dependencies-cdn");
const { set, each } = require("lodash");

const cdn = {
  css: cdnDependencies.map(e => e.css).filter(e => e),
  js: cdnDependencies.map(e => e.js).filter(e => e),
};

module.exports = config => {
  config.plugins.delete("fork-ts-checker");

  const targetHtmlPluginNames = ["html"];

  each(targetHtmlPluginNames, name => {
    config.plugin(name).tap(options => {
      set(options, "[0].cdn", process.env.NODE_ENV === "production" ? cdn : []);
      return options;
    });
  });

  // 容量分析
  if (process.env.ANALYZER === "1") {
    config
      .plugin("webpack-bundle-analyzer")
      .use(require("webpack-bundle-analyzer").BundleAnalyzerPlugin);
  }

  // // add eslint
  // config.module
  //   .rule("eslint")
  //   .exclude.add(path.resolve(__dirname, "node_modules/"))
  //   .end()
  //   .test(/\.ts|vue|tsx$/)
  //   .use("eslint-loader")
  //   .loader("eslint-loader")
  //   .end();

  // // 添加eslint提示
  // config.plugin("fork-ts-checker").tap((option) => {
  //   return [
  //     {
  //       async: false,
  //       eslint: false,
  //       vue: true,
  //       formatter: "codeframe",
  //     },
  //   ];
  // });
};
