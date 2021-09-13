package com.medusa.gruul.afs.model;

import com.medusa.gruul.afs.api.enums.AfsOrderTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * The type Base apply dto.
 *
 * @author alan
 * @description: 用户申请售后的参数
 * @date 2020 /8/5 21:54
 */
@Data
@ApiModel(value = "用户申请售后的参数")
public class BaseApplyDto {

    @NotNull(message = "申请类型不能为空")
    @ApiModelProperty(value = "工单类型：REFUND->退款;  " +
            "RETURN_REFUND->退货退款")
    private AfsOrderTypeEnum type;


    /**
     * 商品sku编号
     */
    @NotNull(message = "商品sku不能为空")
    @ApiModelProperty(value = "商品sku编号")
    private Long productSkuId;

    /**
     * 商品数量
     */
    @Min(1)
    @NotNull(message = "商品数量不能为空")
    @ApiModelProperty(value = "商品数量")
    private Integer productQuantity;

    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    /**
     * 说明
     */
    @NotBlank
    @Length(min = 1, max = 200)
    @ApiModelProperty(value = "说明")
    private String description;

    /**
     * 照片
     */
    @ApiModelProperty(value = "照片")
    private String images;

    /**
     * templateId
     */
    @ApiModelProperty(value = "templateId")
    private String templateId;

    /**
     * returnTemplateId
     */
    @ApiModelProperty(value = "returnTemplateId")
    private String returnTemplateId;

    /**
     * reason
     */
    @ApiModelProperty(value = "reason")
    private String reason;
}
