<template>
  <div
    style="display: flex;justify-content: center;align-items: center;height: 100%;"
  >
    <div>认证成功，请确认</div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import storage from "@/libs/storage";

@Component
export default class Redirect extends Vue {
  created() {
    // 获取url中"?"符后的字串
    let url = window.location.search;
    url = url.substr(1);
    url = url.split("&")[0].split("=")[1];
    if (this.$route.params.type === "sign") {
      storage.set("signCode", url);
    }
    if (this.$route.params.type === "modifyAccount") {
      storage.set("modifyAccountCode", url);
      this.$router.push(process.env.VUE_APP_PUBLICPATH + "/business");
    }
  }
}
</script>
