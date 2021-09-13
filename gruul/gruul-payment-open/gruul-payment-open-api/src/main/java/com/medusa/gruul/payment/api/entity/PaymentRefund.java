package com.medusa.gruul.payment.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author create by zq
 * @date created in 2020/03/13
 */
@Data
@TableName("t_payment_refund")
@ApiModel(value = "t_payment_refund对象", description = "t_payment_refund实体")
public class PaymentRefund extends BaseEntity {


	private static final long serialVersionUID = 1L;


	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;


	/**
	 * 订单id
	 */
	@ApiModelProperty(value = "订单id")
	private String orderId;


	/**
	 * 同步请求参数
	 */
	@ApiModelProperty(value = "同步请求参数")
	@TableField(value = "asyn_request")
	private String asynRequest;


	/**
	 * 同步返回结果
	 */
	@ApiModelProperty(value = "同步返回结果")
	@TableField("asyn_result")
	private String asynResult;


	/**
	 * 异步回调数据
	 */
	@ApiModelProperty(value = "异步回调数据")
	@TableField("syn_callback")
	private String synCallback;


	/**
	 * 路由键
	 */
	@ApiModelProperty(value = "路由键")
	@TableField("route_key")
	private String routeKey;

}
