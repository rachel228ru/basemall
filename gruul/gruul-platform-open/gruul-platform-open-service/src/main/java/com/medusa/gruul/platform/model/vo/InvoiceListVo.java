package com.medusa.gruul.platform.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/8/2
 */
@Data
public class InvoiceListVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    private Long accountId;

    /**
     * 抬头类型：1个人或事业单位，2企业
     */
    @ApiModelProperty(value = "抬头类型：1个人或事业单位，2企业")
    private Integer headType;

    /**
     * 抬头名称
     */
    @ApiModelProperty(value = "抬头名称")
    private String invoiceRiseName;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String invoiceTaxpayerNum;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 是否默认使用 0-否  1-是
     */
    @ApiModelProperty(value = "是否默认使用 0-否  1-是")
    private Integer defaultStatus;

}
