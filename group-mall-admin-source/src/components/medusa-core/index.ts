import { VueConstructor } from "vue";
import ElementUI from "element-ui";
import MedusaUI from "./packages";
import "element-ui/lib/theme-chalk/index.css";

// plugins
import draggable from "vuedraggable";

const install = (vm: VueConstructor) => {

  vm.use(ElementUI, { size: "small" });
  vm.use(MedusaUI);

  vm.component("m-draggable", draggable);
};

export default { install };
