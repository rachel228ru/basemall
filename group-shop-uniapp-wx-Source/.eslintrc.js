/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:33:18
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-14 15:01:41
 * 123
 */
module.exports = {
  root: true,
  env: {
    node: true
  },
  globals: {
    uni: true,
    wx: true,
    getApp: true,
    UniApp: true,
  },
  'extends': [
    'plugin:vue/essential',
    'eslint:recommended',
    '@vue/typescript/recommended'
  ],
  parserOptions: {
    ecmaVersion: 2020
  },
  rules: {
    "@typescript-eslint/no-inferrable-types": "off",
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    '@typescript-eslint/explicit-module-boundary-types': 'off',
    "@typescript-eslint/no-explicit-any": ["off"],
    'no-empty-function': 'off',
    '@typescript-eslint/no-inferrable-types': 'off',
    '@typescript-eslint/ban-types': 'off',
    "@typescript-eslint/no-this-alias": [
      "error",
      {
        "allowDestructuring": true, // Allow `const { props, state } = this`; false by default
        "allowedNames": ["that", "self"] // Allow `const self = this`; `[]` by default
      }
    ],
    'no-async-promise-executor': "off",
    'no-prototype-builtins': "off",
    '@typescript-eslint/no-var-requires': "off",
    'no-mixed-spaces-and-tabs': "off"
  }
}