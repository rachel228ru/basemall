export interface ISignin {
    // 登录类型 1-密码登录  2-验证码登录 3-扫码登录
    loginType: 1 | 2 | 3;
    // 登录账号
    phone: string;
    // 密码
    password: string;
    // 校验码凭证
    certificate: string;
    // 预扫码返回的state
    state: string;
}
