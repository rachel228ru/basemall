<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:01:53
-->
<template>
  <!-- 富文本组件 -->
  <div
    style="width:100%;z-index:1;position:relative"
    ref="editorRef"
    id="editor"
  ></div>
</template>

<script lang="ts">
import { Vue, Component, PropSync, Watch, Ref } from "vue-property-decorator";
import wEditor from "wangeditor";
import RichEditor from "@/components/RichEditor.vue";
import RichText from "./RichText";
import { upLoad } from "@/api/index";
@Component({
  components: {
    RichEditor,
  },
})
export default class CubeBoxPreview extends Vue {
  @PropSync("formData")
  data!: RichText;

  editor: wEditor = {} as wEditor;

  info = null;

  width = 0;

  height = 0;

  @Ref() readonly editorRef!: wEditor;

  get activeComIndex() {
    return this.$STORE.decorationStore.activeComIndex;
  }

  mounted() {
    // 初始化富文本编辑器
    this.initEditor();
  }

  /**
   * 切换相同组件清除缓存
   */
  @Watch("activeComIndex")
  initEditor() {
    /** 服务详情富文本 */
    this.editor = new wEditor("#editor");
    this.editor.config.pasteFilterStyle = true; // 粘贴过滤
    this.editor.config.showLinkImg = false; // 隐藏网络显示
    // this.editor.customConfig.uploadImgShowBase64 = true // 使用 base64 保存图片
    this.editor.config.uploadImgMaxSize = 5 * 1024 * 1024; // 图片大小限制
    this.editor.config.uploadImgMaxLength = 5; // 限制一次最多上传 5 张图片
    this.editor.config.uploadImgServer = "/upload"; // 配置服务器端地址
    this.editor.config.customUploadImg = (
      files: File[],
      insert: (arg0: any) => void,
    ) => {
      // files 是 input 中选中的文件列表
      this.uploadFiles(files).then(res => {
        res.forEach(item => {
          this.checkImgWidth(item.data).then(res2 => {
            insert(item.data);
            if (res2.width > 375) {
              let html: string = this.editor.txt.html() || "";
              const reg = new RegExp(
                `<img src="${item.data}" style="max-width: 100%;">`,
                "g",
              );
              html = html.replace(
                reg,
                `<img style="width:100%;height:auto;display:block" src="${item.data}">`,
              );
              this.editor.txt.html(html);
            }
          });
        });
      });
    };
    this.editor.config.onchange = (html: string) => {
      // html 即变化之后的内容
      this.data.text = html;
    };
    this.editor.config.showLinkImg = false;
    this.editor.config.pasteTextHandle = function(content: string) {
      content = content.replace(
        /\<img/g,
        '<img style="width:100%;height:auto;display:block"',
      );
      return content;
    };
    this.editor.config.customAlert = (info: any) => {
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
      "list", // 列表
      "justify", // 对齐方式
      "quote", // 引用
      "image", // 插入图片
      "undo", // 撤销
    ];
    // this.change();
    this.editor.create();
    this.editor.txt.html(`${this.data.text}`);
  }

  uploadFiles(files: any[]) {
    const promiseAll = files.map(file => {
      return upLoad({ file });
    });
    return Promise.all(promiseAll);
  }

  checkImgWidth(fileUrl: string): Promise<{ width: number; height: number }> {
    return new Promise(resolve => {
      const img = new Image();
      let res: { width: number; height: number } = { width: 0, height: 0 };
      img.onload = function() {
        res = {
          width: img.width,
          height: img.height,
        };
        resolve(res);
      };
      img.src = fileUrl;
    });
  }
}
</script>

<style lang="scss" scope></style>
