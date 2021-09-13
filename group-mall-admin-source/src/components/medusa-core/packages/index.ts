import { VueConstructor } from "vue";
import { MCard } from "./card";
import { MTable, MTableColumn } from "./table";
import { DragList } from "./drag-list";

const components = [MTable, MTableColumn, MCard, DragList] as any;

const install = (vm: VueConstructor) => {
  components.map(component => {
    return vm.component(component.componentName, component);
  });
};

export default { install };
