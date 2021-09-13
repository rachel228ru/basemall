package com.medusa.gruul.common.core.constant.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * @author whh
 * @description 模板编号
 * @data: 2020/12/21
 */
@Getter
public enum TemplateCodeEnum {

    /**
     * 社区拼团模板
     */
    SQPT,

    /**
     * 商城模板
     */
    MALL;

    /**
     * 判断当前是否社区拼团模板
     * 相同返回 true  不同返回false
     *
     * @return true or false
     */
    public Boolean isSqlp() {
        String name = this.name();
        return StrUtil.isNotBlank(name) && name.equals(TemplateCodeEnum.SQPT.name());
    }

    /**
     * 判断当前是否商城模板枚举
     * 相同返回 true  不同返回false
     *
     * @return true or false
     */
    public Boolean isMall() {
        String name = this.name();
        return StrUtil.isNotBlank(name) && name.equals(TemplateCodeEnum.MALL.name());
    }
}
