package com.medusa.gruul.platform.api.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/5/22
 */
@Data
public class ShopMessageDetailVo {

    @ApiModelProperty(value = "id")
    private Long id;


    /**
     * 标题名称
     */
    @ApiModelProperty(value = "标题名称")
    private String title;

    /**
     * 小程序订阅消息是否开启 0-关闭  1-开启
     */
    @ApiModelProperty(value = "小程序订阅消息是否开启 0-关闭  1-开启")
    private Integer miniOpen;

    /**
     * 小程序订阅模板
     */
    @ApiModelProperty(value = "小程序订阅模板")
    private String miniMsg;

    /**
     * 短信模板消息是否开启 0-关闭  1-开启
     */
    @ApiModelProperty(value = "短信模板消息是否开启 0-关闭  1-开启")
    private Integer codeOpen;

    /**
     * 短信消息
     */
    @ApiModelProperty(value = "短信消息")
    private String codeMsg;

    /**
     * 公众号是否发送  0关闭- 1-开启
     */
    @ApiModelProperty(value = "公众号是否发送  0关闭- 1-开启")
    private Integer mpOpen;

    /**
     * 公众号消息
     */
    @ApiModelProperty(value = "公众号消息")
    private String mpMsg;

}
