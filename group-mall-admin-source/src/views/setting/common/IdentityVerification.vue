<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:34:35
-->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    class="dialog"
    width="600px"
    title="身份校验"
  >
    <div class="scan-code">
      <ScanQrCode
        redirectUrl="/setting?tabName=Payment"
        scenes="AccountShopInfoCheck"
        :shopInfoId="shopInfoId"
      />
    </div>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, PropSync, Prop } from "vue-property-decorator";
import ScanQrCode from "@/components/ScanQrCode.vue";

@Component({
  components: {
    ScanQrCode,
  },
})
export default class IdentityVerification extends Vue {
  @PropSync("visible", {
    type: Boolean,
    default: false,
  })
  dialogVisible!: boolean;

  @Prop({
    type: String || Number,
    default: "",
  })
  shopInfoId!: string | number;
}
</script>
