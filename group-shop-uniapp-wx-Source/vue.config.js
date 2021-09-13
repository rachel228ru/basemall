// FIXME 体验优化 node-bash-title
// FIXME 包体积分析 webpack-bundle-analyzer
const setTitle = require("node-bash-title")
setTitle("lsm测试环境")
const CopyPlugin = require("copy-webpack-plugin");
const {
  parse
} = require("dotenv");
const {
  resolve
} = require("path");
const {
  readFileSync,
  writeFileSync
} = require("fs");


const getEnv = () => {
  const getEnvString = file =>
    Buffer.from(readFileSync(resolve(__dirname, file)));
  const baseConf = parse(getEnvString(`.env`));

  const environmentConf = parse(
    getEnvString(`.env.${process.env.NODE_ENV}`),
  );

  const localPath = `.env.${process.env.NODE_ENV}.local`;
  let localConf = null;

  try {
    localConf = parse(getEnvString(localPath)) || {};
  } catch (err) {
    localConf = {};
  }

  return Object.assign(baseConf, environmentConf, localConf);
};

const env = getEnv();

const setExt = () => {
  const filePath = resolve(__dirname, "./src/ext.json");
  const data = JSON.parse(readFileSync(filePath).toString());

  data.extAppid = env.EXAPP_ID;
  data.ext.tenantId = env.VUE_APP_TENAN_ID;
  writeFileSync(filePath, JSON.stringify(data));
};

const setProjectJson = () => {
  const filePath = resolve(__dirname, "./src/manifest.json");
  const data = JSON.parse(readFileSync(filePath).toString());
  data['mp-weixin'].appid = env.APP_ID;

  writeFileSync(filePath, JSON.stringify(data));
};

setExt();

setProjectJson();

module.exports = {
  configureWebpack: {
    plugins: [
      new CopyPlugin([{
        from: "src/ext.json",
        to: "./"
      }]),
    ],
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src/'),
      },
      extensions:[".ts",".js"]
    }
  },
  css: {
    loaderOptions: {
      scss: {
        additionalData: `@import "@/assets/styles/common.scss";@import "@/assets/styles/mixins/mixins.scss";`
      }
    }
  }
}