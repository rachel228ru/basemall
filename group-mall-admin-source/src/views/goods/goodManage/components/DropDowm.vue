<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-12 17:34:39
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-25 15:13:34
-->
<template>
  <div>
    <el-dropdown style="margin-left:10px;" placement="bottom-start">
      <span style="cursor:pointer;">
        <span>更多</span>
        <i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <span @click="dialogVisible = true">查看</span>
        </el-dropdown-item>
        <el-dropdown-item>
          <span @click="useSup">{{ status === 1 ? "禁用" : "启用" }}</span>
        </el-dropdown-item>
        <el-dropdown-item>
          <span style="color:#FA6465" @click="delSupplier">删除</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <el-dialog :visible.sync="dialogVisible" width="750px">
      <div slot="title" class="diaTitle">查看商品</div>
      <SupProduct :supProduct="sup"></SupProduct>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import SupProduct from "./SupProduct.vue";
import { AddGoodSupplierType } from "../supplierManageType";
@Component({
  components: {
    SupProduct,
  },
})
export default class DropDowm extends Vue {
  @Prop({
    type: Number,
    default() {
      return null;
    },
  })
  readonly status!: number;

  @Prop({
    type: Object,
    default() {
      return null;
    },
  })
  readonly sup!: AddGoodSupplierType;

  dialogVisible = false;

  /** 启用/禁用供应商 */
  useSup() {
    this.$emit("useSup", this.status, this.sup);
  }

  /** 删除供应商 */
  delSupplier() {
    this.$emit("delSupplier", this.status, this.sup);
  }
}
</script>

<style lang="scss" scoped>
.el-icon-arrow-down {
  font-size: 8px;
  margin-left: 1px;
}

.diaTitle {
  display: flex;
  justify-content: flex-start;
  font-size: 16px;
}
</style>
