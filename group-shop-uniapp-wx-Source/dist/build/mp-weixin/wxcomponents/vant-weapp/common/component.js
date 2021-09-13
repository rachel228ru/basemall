/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:32:07
 */
import { basic } from '../mixins/basic';
function mapKeys(source, target, map) {
  Object.keys(map).forEach((key) => {
    if (source[key]) {
      target[map[key]] = source[key];
    }
  });
}
function VantComponent(vantOptions) {
  const options = {};
  mapKeys(vantOptions, options, {
    data: 'data',
    props: 'properties',
    mixins: 'behaviors',
    methods: 'methods',
    beforeCreate: 'created',
    created: 'attached',
    mounted: 'ready',
    destroyed: 'detached',
    classes: 'externalClasses',
  });
  options.externalClasses = options.externalClasses || [];
  options.externalClasses.push('custom-class');
  options.behaviors = options.behaviors || [];
  options.behaviors.push(basic);
  const { relation } = vantOptions;
  if (relation) {
    options.relations = relation.relations;
    options.behaviors.push(relation.mixin);
  }
  if (vantOptions.field) {
    options.behaviors.push('wx://form-field');
  }
  options.options = {
    multipleSlots: true,
    addGlobalClass: true,
  };
  Component(options);
}
export { VantComponent };
