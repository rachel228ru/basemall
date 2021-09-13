<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 16:14:57
-->
<template>
  <div style="width:100%;z-index:1;position:relative" ref="editor"></div>
</template>

<script>
import wEditor from "wangeditor";
import { upLoad } from "@/api/index";
export default {
  name: "wangeditor",
  props: ["text"],
  data() {
    return {
      editor: null,
    };
  },
  watch: {
    text(val) {
      val = val.replace(
        /\<img/g,
        '<img style="width:100%;height:auto;display:block"',
      );
      if (this.editor) {
        this.editor.txt.html(`<p>${val}</p>`);
      }
    },
  },
  mounted() {
    this.initEditor(this.text); // 初始化富文本编辑器
  },
  methods: {
    // 初始化富文本编辑器
    initEditor(editorHtml = "") {
      /** 服务详情富文本 */
      this.editor = new wEditor(this.$refs.editor);
      this.editor.config.pasteFilterStyle = true;
      // this.editor.config.uploadImgShowBase64 = true // 使用 base64 保存图片
      this.editor.config.uploadImgMaxSize = 5 * 1024 * 1024; // 图片大小限制
      this.editor.config.uploadImgMaxLength = 5; // 限制一次最多上传 5 张图片
      this.editor.config.uploadImgServer = "/upload"; // 配置服务器端地址
      this.editor.config.customUploadImg = (files, insert) => {
        // files 是 input 中选中的文件列表
        this.uploadFiles(files).then(res => {
          res.forEach(item => {
            insert(item.data); // insert 是获取图片 url 后，插入到编辑器的方法
          });
        });
      };
      // this.editor.config.onchange = html => {};

      this.editor.config.pasteTextHandle = function(content) {
        content = content.replace(
          /\<img/g,
          '<img style="width:100%;height:auto;display:block"',
        );
        return content;
      };

      this.editor.config.showLinkImg = false;
      this.editor.config.customAlert = info => {
        // info 是需要提示的内容
        this.$message({
          showClose: true,
          message: info,
          type: "error",
        });
      };

      this.editor.config.menus = [
        "head", // 标题
        "bold", // 粗体
        "italic", // 斜体
        "underline", // 下划线
        "strikeThrough", // 删除线
        "foreColor", // 文字颜色
        "backColor", // 背景颜色
        // "link", // 插入链接
        "list", // 列表
        "justify", // 对齐方式
        "quote", // 引用
        // "emoticon", // 表情
        "image", // 插入图片
        // "table", // 表格
        // "video", // 插入视频
        // "code", // 插入代码
        // "undo", // 撤销
        // "redo", // 重复
      ];
      // this.change();
      this.editor.create();
      this.editor.txt.html(`<p>${editorHtml}</p>`);
    },

    change() {
      return (this.editor.config.onchange = e => {
        console.log("change", e);
      });
    },

    /**
     * 获取编辑框html
     */
    getHtml() {
      // var filterHtml = filterXSS(html) // 此处进行 xss 攻击过滤
      return this.editor.txt.html(); // 获取编辑框html
    },

    /**
     * 获取编辑框text
     */
    getText() {
      return this.editor.txt.text(); // 获取编辑框text
    },

    /**
     * 清空编辑器内容
     */
    clearEditor() {
      this.editor.txt.clear(); // 清空编辑器内容
    },

    uploadFiles(files = []) {
      const promiseAll = files.map(file => {
        return upLoad({ file });
      });
      return Promise.all(promiseAll);
    },
  },
};
</script>
<style scoped></style>
