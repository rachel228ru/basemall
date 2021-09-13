package com.medusa.gruul.platform.constant;

import lombok.Getter;

/**
 * 消息分类枚举
 *
 * @author whh
 */
@Getter
public enum AgentNoticeEnum {

    /**
     *
     */
    MSG_001(1, "{}【{}】套餐续费成功"),
    MSG_002(2, "{}【{}】套餐升级成功"),
    MSG_003(3, "{}【{}】套餐订购成功"),
    MSG_004(4, "{}【{}】将在{}天后到期"),
    MSG_005(5, "{}【{}】已到期"),
    MSG_006(6, "您旗下(渠道商账号: {}) {}【{}】订购成功"),
    MSG_007(7, "您旗下(渠道商账号: {}) {}【{}】续费成功"),
    MSG_008(8, "您旗下(渠道商账号: {}) {}【{}】升级成功"),
    MSG_009(9, "您旗下(渠道商账号: {}) {}【{}】将在{}天后到期"),
    MSG_0010(10, "您旗下(渠道商账号: {}) {}【{}】到期消息"),
    MSG_0011(11, "{}【{}】试用期已结束"),
    MSG_0012(12, "您旗下(渠道商账号: {}) {}【{}】试用期已结束"),
    MSG_0021(21, "提现申请提交成功通知"),
    MSG_0022(22, "提现申请已拒绝通知"),
    MSG_0023(23, "佣金提现成功通知"),
    MSG_0031(31, "商户账户余额充值成功"),
    ;

    private final Integer type;
    private final String title;


    AgentNoticeEnum(Integer type, String title) {
        this.type = type;
        this.title = title;
    }

}
