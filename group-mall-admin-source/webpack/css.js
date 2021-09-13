module.exports = {
  loaderOptions: {
    scss: {
      prependData: `
        @import "@/assets/styles/_variable.scss";
        @import "@/assets/styles/mixins/mixins.scss";
      `,
    },
  },
};
