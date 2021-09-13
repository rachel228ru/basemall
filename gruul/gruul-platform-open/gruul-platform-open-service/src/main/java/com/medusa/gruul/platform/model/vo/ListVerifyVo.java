package com.medusa.gruul.platform.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/10/9
 */
@Data
public class ListVerifyVo {

    @ApiModelProperty(value = "id")
    private Long id;


    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "联系人")
    private String linkName;

    @ApiModelProperty(value = "所在区域")
    private String region;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "0:待审核1:审核通过 2:审核拒绝")
    private Integer status;

    @ApiModelProperty(value = "邀请码")
    private String code;

    @ApiModelProperty(value = "企业名称")
    private String enterprise;

    @ApiModelProperty(value = "企业类型编号")
    private String enterpriseType;

    @ApiModelProperty(value = "公司规模编号")
    private String scaleCode;

    @ApiModelProperty(value = "注册方式 1.官网自行注册  2.商户控制台注册")
    private Integer registerType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
