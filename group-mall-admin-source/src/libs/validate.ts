//传真校验
export const faxReg = /^(\d{3,4}-)?\d{7,8}$/;

//邮箱校验
export const emailReg = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;

//邮编校验
export const postalReg = /^\d{6}$/;

//qq
export const qqReg = /^[1-9][0-9]{4,10}$/;

//手机号校验
export const phoneReg = /^((\+86)|(86))?(13|15|17|18)\d{9}$/;

//密码校验，6-16位密码，至少使用字母、数字、符号中的2种组合
export const passwordReg = /^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)]|[])+$)([^(0-9a-zA-Z)]|[]|[a-z]|[A-Z]|[0-9]){6,}$/;

//银行卡号校验
export const bankCardReg = /^([1-9]{1})(\d{14}|\d{18})$/;

//固定电话校验
export const telephoneReg = /^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;

export const testTelephone = (rule, value, callback) => {
  if (value) {
    if (!telephoneReg.test(value)) {
      callback(new Error("请输入区号-电话号码"));
    } else {
      callback();
    }
  } else {
    callback();
  }
};

//传真的验证
export const testFax = (rule, value, callback) => {
  if (value) {
    if (!faxReg.test(value)) {
      callback(new Error("请输入区号-传真号码"));
    } else {
      callback();
    }
  } else {
    callback();
  }
};

export const testEmail = (rule, value, callback) => {
  if (value) {
    if (!emailReg.test(value)) {
      callback(new Error("请输入正确的邮箱地址"));
    } else {
      callback();
    }
  } else {
    callback();
  }
};

//邮编的验证
export const testPostal = (rule, value, callback) => {
  if (value) {
    if (!postalReg.test(value)) {
      callback(new Error("请输入正确的邮编"));
    } else {
      callback();
    }
  } else {
    callback(new Error("请输入邮编"));
  }
};

//银行卡号的验证
export const testBankCard = (rule, value, callback) => {
  if (value) {
    if (!bankCardReg.test(value)) {
      callback(new Error("请输入正确的银行卡号"));
    } else {
      callback();
    }
  } else {
    callback(new Error("请输入账号"));
  }
};

//密码强度的验证
export const testPassword = (rule, value, callback) => {
  if (value) {
    if (!passwordReg.test(value)) {
      callback(
        new Error("请输入6-16位密码，至少使用字母、数字、符号中的2种组合"),
      );
    } else {
      callback();
    }
  } else {
    callback();
  }
};

//手机号码的校验
export const testPhone = (rule, value, callback) => {
  if (value) {
    if (!phoneReg.test(value)) {
      callback(new Error("请输入正确的手机号"));
    } else {
      callback();
    }
  } else {
    callback();
  }
};

//qq号码的校验
export const testQQ = (rule, value, callback) => {
  if (value) {
    if (!qqReg.test(value)) {
      callback(new Error("请输入正确的qq号"));
    } else {
      callback();
    }
  } else {
    callback();
  }
};
