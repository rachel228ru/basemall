// base64图片本地存储 不清楚ts如何实现 用原项目js暂替
// 自定义文件名
const fsm = wx.getFileSystemManager();
const FILE_BASE_NAME = "tmp_base64src";

function base64src(base64data: string, cb: (arg0: string) => void) {
  const [, format, bodyData] =
    /data:image\/(\w+);base64,(.*)/.exec(base64data) || [];
  if (!format) {
    return new Error("ERROR_BASE64SRC_PARSE");
  }
  const filePath = `${wx.env.USER_DATA_PATH}/${FILE_BASE_NAME}.${format}`;
  const buffer = wx.base64ToArrayBuffer(bodyData);
  return fsm.writeFile({
    filePath,
    data: buffer,
    encoding: "binary",
    success() {
      cb(filePath);
    },
    fail() {
      return new Error("ERROR_BASE64SRC_WRITE");
    },
  });
}
export { base64src };
