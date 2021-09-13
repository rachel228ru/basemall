package com.medusa.gruul.platform.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/5/23
 */
@Data
public class MiniSubscriberiMessageDto {


    @ApiModelProperty(value = "模板标题")
    private String tId;

    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题")
    private String title;

    /**
     * 模板消息id
     */
    @ApiModelProperty(value = "模板消息id")
    private String templateId;


    /**
     * 开启状态 0-未使用  1-使用
     */
    @ApiModelProperty(value = "开启状态 0-未使用  1-使用")
    private Integer useStatus;

    /**
     * kid按顺序分割,例子1,3,5
     */
    @ApiModelProperty(value = "kid按顺序分割,例子1,3,5")
    private String kIds;

    /**
     * 参数规则名跟kid排序相对应amount1,thing3,thing5
     */
    @ApiModelProperty(value = "参数规则名跟kid排序相对应amount1,thing3,thing5")
    private String rules;

    /**
     * 提供预览值
     */
    @ApiModelProperty(value = "提供预览值")
    private String exampleJson;

    /**
     * 二级类目名称
     */
    @ApiModelProperty(value = "二级类目名称")
    private String secondClass;


    /**
     * 推送规则
     */
    @ApiModelProperty(value = "推送规则 ")
    private String sendRule;

}
