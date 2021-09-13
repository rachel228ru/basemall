import { getRect } from '../common/utils';
import { VantComponent } from '../common/component';
import { pageScrollMixin } from '../mixins/page-scroll';
const ROOT_ELEMENT = '.van-sticky';
VantComponent({
  props: {
    zIndex: {
      type: Number,
      value: 99,
    },
    offsetTop: {
      type: Number,
      value: 0,
      observer: 'onScroll',
    },
    disabled: {
      type: Boolean,
      observer: 'onScroll',
    },
    container: {
      type: null,
      observer: 'onScroll',
    },
    scrollTop: {
      type: null,
      observer(val) {
        this.onScroll({ scrollTop: val });
      },
    },
  },
  mixins: [
    pageScrollMixin(function (event) {
      if (this.data.scrollTop != null) {
        return;
      }
      this.onScroll(event);
    }),
  ],
  data: {
    height: 0,
    fixed: false,
    transform: 0,
  },
  mounted() {
    this.onScroll();
  },
  methods: {
    onScroll({ scrollTop } = {}) {
      const { container, offsetTop, disabled } = this.data;
      if (disabled) {
        this.setDataAfterDiff({
          fixed: false,
          transform: 0,
        });
        return;
      }
      this.scrollTop = scrollTop || this.scrollTop;
      if (typeof container === 'function') {
        Promise.all([
          getRect(this, ROOT_ELEMENT),
          this.getContainerRect(),
        ]).then(([root, container]) => {
          if (offsetTop + (root ? root.height : 0) > (container ? container.height : 0) + (container ? container.top : 0)) {
            this.setDataAfterDiff({
              fixed: false,
              transform: container.height - (root ? root.height : 0),
            });
          } else if (offsetTop >= (root ? root.top : 0)) {
            this.setDataAfterDiff({
              fixed: true,
              height: (root ? root.height : 0),
              transform: 0,
            });
          } else {
            this.setDataAfterDiff({ fixed: false, transform: 0 });
          }
        });
        return;
      }
      getRect(this, ROOT_ELEMENT).then((root) => {
        
        if (offsetTop >= (root ? root.top : 0)) {
          this.setDataAfterDiff({ fixed: true, height: (root ? root.height : 0) });
          this.transform = 0;
        } else {
          this.setDataAfterDiff({ fixed: false });
        }
      });
    },
    setDataAfterDiff(data) {
      wx.nextTick(() => {
        const diff = Object.keys(data).reduce((prev, key) => {
          if (data[key] !== this.data[key]) {
            prev[key] = data[key];
          }
          return prev;
        }, {});
        if (Object.keys(diff).length > 0) {
          this.setData(diff);
        }
        this.$emit('scroll', {
          scrollTop: this.scrollTop,
          isFixed: data.fixed || this.data.fixed,
        });
      });
    },
    getContainerRect() {
      const nodesRef = this.data.container();
      return new Promise((resolve) =>
        nodesRef.boundingClientRect(resolve).exec()
      );
    },
  },
});
