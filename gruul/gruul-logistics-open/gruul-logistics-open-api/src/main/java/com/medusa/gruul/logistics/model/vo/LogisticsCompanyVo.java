package com.medusa.gruul.logistics.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 收发货地址管理
 * t_logistics_address
 *
 * @author
 */
@Data
public class LogisticsCompanyVo {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺物流公司绑定表主键 id
     */
    private Long logisticsCompanyId;

    /**
     * 物流公司名称
     */
    private String name;

    /**
     * 物流公司名称
     */
    private String code;

    /**
     * 是否为默认物流公司
     */
    private Integer isDefault;
    /**
     * 物流公司发货地址
     */
    List<LogisticsExpressAddressVo> logisticsExpressAddressVos;

}