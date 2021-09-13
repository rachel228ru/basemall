module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
    es6: true,
  },
  parser: "vue-eslint-parser",
  extends: ["plugin:@typescript-eslint/recommended"],
  plugins: ["@typescript-eslint"],
  parserOptions: {
    parser: "@typescript-eslint/parser",
  },
  rules: {
    indent: ["warn", 2, { SwitchCase: 1 }],
    "@typescript-eslint/explicit-function-return-type": 0,
    '@typescript-eslint/explicit-module-boundary-types': 'off',
    "@typescript-eslint/no-explicit-any": 0,
    "@typescript-eslint/interface-name-prefix": 0,
    "@typescript-eslint/no-var-requires": 0,
    "prefer-rest-params": 0,
    "prefer-spread": 0,
    "@typescript-eslint/ban-ts-ignore": 0,
    'lines-between-class-members': 1
  },
};
