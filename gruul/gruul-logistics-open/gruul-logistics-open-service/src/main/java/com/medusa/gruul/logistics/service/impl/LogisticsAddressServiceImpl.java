package com.medusa.gruul.logistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.DateUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.logistics.api.entity.LogisticsAddress;
import com.medusa.gruul.logistics.api.entity.LogisticsCompany;
import com.medusa.gruul.logistics.api.entity.LogisticsShop;
import com.medusa.gruul.logistics.mapper.*;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsAddressDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsBatchDeliverDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsPrintDeliverDto;
import com.medusa.gruul.logistics.model.dto.manager.express.ExpressInfoDto;
import com.medusa.gruul.logistics.model.enums.AddressDefaultEnum;
import com.medusa.gruul.logistics.model.enums.AddressTypeEnum;
import com.medusa.gruul.logistics.model.param.LogisticsAddressParam;
import com.medusa.gruul.logistics.model.param.LogisticsExpressPrintParam;
import com.medusa.gruul.logistics.model.vo.*;
import com.medusa.gruul.logistics.service.ILogisticsAddressService;
import com.medusa.gruul.logistics.util.express.kuaidihelp.KuaiDiHelp;
import com.medusa.gruul.logistics.util.express.sf.SFExpressUtil;
import com.medusa.gruul.order.api.entity.OrderDelivery;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.feign.RemoteOrderService;
import com.medusa.gruul.order.api.model.OrderDeliveryDto;
import com.medusa.gruul.order.api.model.OrderItemVo;
import com.medusa.gruul.order.api.model.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收发货地址管理业务层
 * @author zhaozheng
 */
@Service
@Slf4j
public class LogisticsAddressServiceImpl implements ILogisticsAddressService {

	@Autowired
	private LogisticsAddressMapper logisticsAddressMapper;
	@Resource
	private LogisticsShopMapper logisticsShopMapper;
	@Resource
	private LogisticsCompanyMapper logisticsCompanyMapper;
	@Resource
	private RemoteOrderService remoteOrderService;
	@Autowired
	private LogisticsExpressPrintMapper logisticsExpressPrintMapper;
	@Autowired
	private LogisticsExpressAddressMapper logisticsExpressAddressMapper;


	/**
	 * 查询收发货地址列表
	 * @param logisticsAddressParam
	 * @return IPage<LogisticsAddressVo>
	 */
	@Override
	public IPage<LogisticsAddressVo> getAddressList(LogisticsAddressParam logisticsAddressParam) {
		IPage<LogisticsAddressVo> page = new Page<>(logisticsAddressParam.getCurrent(),
				logisticsAddressParam.getSize());
		List<LogisticsAddressVo> logisticsAddressVos = this.logisticsAddressMapper
				.queryLogisticsAddressList(page, logisticsAddressParam);
		return page.setRecords(logisticsAddressVos);
	}

	/**
	 * 查询所有地址
	 * @return List<LogisticsAddressVo>
	 */
	@Override
	public List<LogisticsAddressVo> getAllAddressList() {
		List<LogisticsAddressVo> logisticsAddressVos = this.logisticsAddressMapper.queryAllLogisticsAddressList();
		return logisticsAddressVos;
	}

	/**
	 * 更新/新增 地址
	 * @param logisticsAddressDto
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void setAddress(LogisticsAddressDto logisticsAddressDto) {
		// 新增,修改地址
		if (logisticsAddressDto.getId() == null) {
			//验证是否有一模一样的地址信息 全信息匹配
			int logisticsAddressSearch = this.logisticsAddressMapper.selectCount(
					new QueryWrapper<LogisticsAddress>().eq("name", logisticsAddressDto.getName())
							.eq("province_id", logisticsAddressDto.getProvinceId())
							.eq("city_id", logisticsAddressDto.getCityId())
							.eq("country_id", logisticsAddressDto.getCountryId())
							.eq("address", logisticsAddressDto.getAddress())
							.eq("zip_code", logisticsAddressDto.getZipCode())
							.eq("phone", logisticsAddressDto.getPhone()));
			if (logisticsAddressSearch > 0) {
				throw new ServiceException("有相同信息的地址！", SystemCode.DATA_ADD_FAILED.getCode());
			}
			//新增 先判断是否有数据  没有的话设置为默认收发货地址
			int count = this.logisticsAddressMapper.selectCount(new QueryWrapper<>());
			LogisticsAddress logisticsAddress = logisticsAddressDto.coverBean();
			if (count == 0) {
				logisticsAddress.setDefSend(AddressDefaultEnum.YES.getAddressDefault());
				logisticsAddress.setDefReceive(AddressDefaultEnum.YES.getAddressDefault());
			}
			int insert = this.logisticsAddressMapper.insert(logisticsAddress);
			if (insert == 0) {
				throw new ServiceException("地址新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
			}
		} else {
			//修改
			LogisticsAddress logisticsAddress = this.logisticsAddressMapper.selectById(logisticsAddressDto.getId());
			if (BeanUtil.isEmpty(logisticsAddress)) {
				throw new ServiceException("地址不存在！", SystemCode.DATA_EXISTED.getCode());
			}
			//验证是否有一模一样的地址信息 全信息匹配
			int logisticsAddressSearch = this.logisticsAddressMapper.selectCount(
					new QueryWrapper<LogisticsAddress>().eq("name", logisticsAddressDto.getName())
							.eq("province_id", logisticsAddressDto.getProvinceId())
							.eq("city_id", logisticsAddressDto.getCityId())
							.eq("country_id", logisticsAddressDto.getCountryId())
							.eq("address", logisticsAddressDto.getAddress())
							.eq("zip_code", logisticsAddressDto.getZipCode())
							.eq("phone", logisticsAddressDto.getPhone()).ne("id", logisticsAddressDto.getId()));
			if (logisticsAddressSearch > 0) {
				throw new ServiceException("有相同信息的地址！", SystemCode.DATA_UPDATE_FAILED.getCode());
			}
			logisticsAddress.setName(logisticsAddressDto.getName());
			logisticsAddress.setAddress(logisticsAddressDto.getAddress());
			logisticsAddress.setPhone(logisticsAddressDto.getPhone());
			logisticsAddress.setZipCode(logisticsAddressDto.getZipCode());
			logisticsAddress.setProvince(logisticsAddressDto.getProvince());
			logisticsAddress.setProvinceId(logisticsAddressDto.getProvinceId());
			logisticsAddress.setCity(logisticsAddressDto.getCity());
			logisticsAddress.setCityId(logisticsAddressDto.getCityId());
			logisticsAddress.setCountry(logisticsAddressDto.getCountry());
			logisticsAddress.setCountryId(logisticsAddressDto.getCountryId());
			int update = this.logisticsAddressMapper.updateById(logisticsAddress);
			if (update == 0) {
				throw new ServiceException("地址修改失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
			}
		}
	}

	/**
	 * 设置 默认地址
	 * @param type 1-发货地址 2-收货地址
	 * @param id
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void setDefAddress(Integer type, Long id) {
		if (type == AddressTypeEnum.DEFSEND.getAddressType()) {
			//说明是设置发货地址 先去除其他默认的发货地址
			boolean updateSign = new LambdaUpdateChainWrapper<>(logisticsAddressMapper)
					.eq(LogisticsAddress::getDefSend, AddressDefaultEnum.YES.getAddressDefault())
					.set(LogisticsAddress::getDefSend, AddressDefaultEnum.NO.getAddressDefault()).update();
			if (!updateSign) {
				throw new ServiceException("设置默认发货地址失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
			} else {
				//设置为发货地址
				boolean sign = new LambdaUpdateChainWrapper<>(logisticsAddressMapper).eq(LogisticsAddress::getId, id)
						.set(LogisticsAddress::getDefSend, AddressDefaultEnum.YES.getAddressDefault()).update();
				if (!sign) {
					throw new ServiceException("设置默认发货地址失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
				}
			}
		} else {
			//说明是设置收货地址 先去除其他默认的收货地址
			boolean updateSign = new LambdaUpdateChainWrapper<>(logisticsAddressMapper)
					.eq(LogisticsAddress::getDefReceive, AddressDefaultEnum.YES.getAddressDefault())
					.set(LogisticsAddress::getDefReceive, AddressDefaultEnum.NO.getAddressDefault()).update();
			if (!updateSign) {
				throw new ServiceException("设置默认发货地址失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
			} else {
				//设置为收货地址
				boolean sign = new LambdaUpdateChainWrapper<>(logisticsAddressMapper).eq(LogisticsAddress::getId, id)
						.set(LogisticsAddress::getDefReceive, AddressDefaultEnum.YES.getAddressDefault()).update();
				if (!sign) {
					throw new ServiceException("设置默认收货地址失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
				}
			}

		}

	}

	/**
	 * 删除地址
	 * @param id 主键
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delAddress(Long id) {
		LogisticsAddress logisticsAddress = this.logisticsAddressMapper.selectById(id);
		if (BeanUtil.isEmpty(logisticsAddress)) {
			throw new ServiceException("地址不存在！", SystemCode.DATA_EXISTED.getCode());
		} else {
			//判断是否为默认收/发货地址 是的话不允许删除
			if (AddressDefaultEnum.YES.getAddressDefault() == logisticsAddress.getDefReceive()
					|| AddressDefaultEnum.YES.getAddressDefault() == logisticsAddress.getDefSend()) {
				throw new ServiceException("默认地址不能删除！", SystemCode.DATA_DELETE_FAILED.getCode());
			} else {
				int delete = this.logisticsAddressMapper.deleteById(id);
				if (delete == 0) {
					throw new ServiceException("地址删除失败！", SystemCode.DATA_DELETE_FAILED.getCode());
				}
			}
		}
	}

	/**
	 * @param type 收发货类型 1-发货地址 2-收货地址
	 * 获取默认收/发货地址
	 */
	@Override
	public LogisticsAddressVo getDefaultAddress(Integer type) {
		try {
			LogisticsAddressVo logisticsAddress = this.logisticsAddressMapper.queryDefaultAddress(type);
			if (BeanUtil.isEmpty(logisticsAddress)) {
				throw new ServiceException("暂无发货地址,请添加！", SystemCode.DATA_DELETE_FAILED.getCode());
			}
			return logisticsAddress;
		} catch (Exception e) {
			throw new ServiceException("发货地址有误,请核对发货地址！", SystemCode.DATA_DELETE_FAILED.getCode());
		}
	}

	/**
	 *  获取打印机与快递公司信息
	 * @param shopId
	 * @param tenantId
	 */
	@Override
	public Map<String, Object> listLogisticsCompany(String shopId, String tenantId) {
		Map<String, Object> res = new HashMap<>(CommonConstants.NUMBER_TWO);
		Map<String, Object> param = new HashMap<>(CommonConstants.NUMBER_THREE);
		param.put("shopId", shopId);
		param.put("tenantId", tenantId);
		List<LogisticsCompany> logisticsCompanies = logisticsCompanyMapper.selectListCompany(param);
		List<LogisticsCompanyVo> logisticsCompanyVos = new ArrayList<>(logisticsCompanies.size());
		LogisticsShop logisticsShop = logisticsShopMapper
				.selectOne(new QueryWrapper<LogisticsShop>().eq("shop_id", shopId).eq("is_default", 1));
		if (CollectionUtil.isNotEmpty(logisticsCompanies)) {
			logisticsCompanies.forEach(logisticsCompany -> {
				LogisticsCompanyVo logisticsCompanyVo = new LogisticsCompanyVo();
				logisticsCompanyVo.setCode(logisticsCompany.getCode());
				logisticsCompanyVo.setName(logisticsCompany.getName());
				logisticsCompanyVo.setIsDefault(CommonConstants.NUMBER_ZERO);
				logisticsCompanyVo.setLogisticsCompanyId(logisticsCompany.getId());
				if ((null != logisticsShop) && (logisticsCompany.getId()
						.equals(logisticsShop.getLogisticsCompanyId()))) {
					logisticsCompanyVo.setIsDefault(CommonConstants.NUMBER_ONE);
				}
				List<LogisticsExpressAddressVo> logisticsExpressAddressVos = logisticsExpressAddressMapper
						.queryByExpressCode(logisticsCompany.getCode());
				logisticsCompanyVo.setLogisticsExpressAddressVos(logisticsExpressAddressVos);
				logisticsCompanyVos.add(logisticsCompanyVo);
			});
		}
		//打印机列表
		List<LogisticsExpressPrintVo> logisticsExpressPrintVos = this.logisticsExpressPrintMapper
				.queryLogisticsExpressPrintList(null, new LogisticsExpressPrintParam());
		res.put("LogisticsPrinterVos", logisticsExpressPrintVos);
		res.put("logisticsCompanyVos", logisticsCompanyVos);

		return res;
	}

	/**
	 *  设置默认打印快递公司
	 * @param logisticsCompanyId
	 */
	@Override
	public void setCompanyDefault(Long logisticsCompanyId) {
		LogisticsShop logisticsShop = logisticsShopMapper
				.selectOne(new QueryWrapper<LogisticsShop>().eq("is_default", CommonConstants.NUMBER_ONE));
		if (BeanUtil.isEmpty(logisticsShop)) {
			logisticsShop = new LogisticsShop();
			logisticsShop.setIsDefault(CommonConstants.NUMBER_ONE);
			logisticsShop.setLogisticsCompanyId(logisticsCompanyId);
			logisticsShop.setCreateTime(DateUtils.timestampCoverLocalDateTime(System.currentTimeMillis()));
			int insert = logisticsShopMapper.insert(logisticsShop);
			if(insert == 0){
				throw new ServiceException("设置失败！", SystemCode.DATA_ADD_FAILED.getCode());
			}
		} else {
			logisticsShop.setUpdateTime(DateUtils.timestampCoverLocalDateTime(System.currentTimeMillis()));
			logisticsShop.setLogisticsCompanyId(logisticsCompanyId);
			int update = logisticsShopMapper.updateById(logisticsShop);
			if(update == 0){
				throw new ServiceException("设置失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
			}
		}
	}

	/**
	 *  快递订单打印并发货 (通知订单服务修改状态)
	 * @param logisticsPrintDeliverDto 打印并发货dto
	 */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void doPrintDeliverGoods(LogisticsPrintDeliverDto logisticsPrintDeliverDto) {
        OrderSetting orderSetting = remoteOrderService.getOrderSetting();
		if (StrUtil.isEmpty(orderSetting.getKdAppKey())){
			throw new ServiceException("请先设置快宝KEY");
		}
        if (StrUtil.isEmpty(orderSetting.getKdAppId())){
            throw new ServiceException("请先设置快宝ID");
        }
		//把订单状态设置成已发货
		Map<String, Object> param = new HashMap<>(CommonConstants.NUMBER_THREE);
		List<OrderDeliveryDto> orderDeliveryDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
		param.put("code", logisticsPrintDeliverDto.getDeliverCode());
		param.put("shopId", logisticsPrintDeliverDto.getShopId());
		param.put("tenantId", logisticsPrintDeliverDto.getTenantId());
		LogisticsCompany logisticsCompany = logisticsCompanyMapper.selectListCompanyByParam(param);
		if (null == logisticsCompany) {
			throw new ServiceException("发货物流公司已下架");
		}
		List<Long> orderIds = logisticsPrintDeliverDto.getOrderIds();
		if (CollectionUtil.isEmpty(orderIds)) {
			throw new ServiceException("订单id不存在");
		}
		orderIds.forEach(orderId -> {
			OrderDeliveryDto orderDeliveryDto = new OrderDeliveryDto();
			orderDeliveryDto.setOrderId(orderId);
			orderDeliveryDto.setDeliveryCompany(logisticsCompany.getName());
			orderDeliveryDto.setDeliveryCode(logisticsCompany.getCode());
			String resultList = getExpressInfoDto(logisticsPrintDeliverDto, logisticsCompany.getCode(), orderId);
			Map<String, String> resultMap = (Map<String, String>) JSON.parse(resultList);
			if(MapUtil.isEmpty(resultMap)){
				throw new ServiceException("快递单号生成失败！");
			}
			orderDeliveryDto.setDeliverySn(resultMap.get("waybill_no"));
			orderDeliveryDto.setSortingCode(resultMap.get("sorting_code"));
			orderDeliveryDto.setPackageName(resultMap.get("package_name"));
			orderDeliveryDto.setPackageCode(resultMap.get("package_code"));
			RoutingInfoVo routingInfoVo = new RoutingInfoVo();
			routingInfoVo.setWaybillCode(resultMap.get("waybill_no"));
			routingInfoVo.setSortingCode(resultMap.get("sorting_code"));
			routingInfoVo.setPackageName(resultMap.get("package_name"));
			routingInfoVo.setPackageCode(resultMap.get("package_code"));
			//打印物流单号
			expressPrint(logisticsPrintDeliverDto, logisticsCompany.getCode(), routingInfoVo, orderId);
			orderDeliveryDtos.add(orderDeliveryDto);

		});
		//发货
		int i = remoteOrderService.doLogisticsOrderDelivery(JSON.toJSONString(orderDeliveryDtos));
		if(i < CommonConstants.NUMBER_ONE){
			throw new ServiceException("发货失败");
		}
		log.info("更改订单为已发货，结果 ： " + i);
	}

	/**
	 * 生成快递单号
	 * @param logisticsPrintDeliverDto
	 * @param code
	 * @param orderId
	 */
	private String getExpressInfoDto(LogisticsPrintDeliverDto logisticsPrintDeliverDto, String code, Long orderId) {
		ExpressInfoDto expressInfoDto;
		expressInfoDto = convertDelivery(code, orderId);
		Result logisticsExpressNumber = getLogisticsExpressNumber(expressInfoDto, logisticsPrintDeliverDto.getExpressId(),
						logisticsPrintDeliverDto.getShopId());
		if(logisticsExpressNumber.getCode() == CommonConstants.SUCCESS){
			return (String) logisticsExpressNumber.getData();
		}else{
			//{"code":300115,"msg":"业务参数有误，请检查","data":{"reason":"请确认账户合法性"}}
			if ("业务参数有误，请检查".equals(logisticsExpressNumber.getMsg())){
				throw  new ServiceException("请检查快递公司是否配置正常");
			}
			throw new ServiceException(logisticsExpressNumber.getMsg(), SystemCode.FAILURE.getCode());
		}

	}

	/**
	 * 根据快递公司代码生成快递单号
	 * 顺丰快宝接口暂不支持，调用自己对接丰桥的接口
	 * @param expressInfoDto 订单传输信息
	 * @param expressId 物流设置表id
	 */
	@Override
	public Result getLogisticsExpressNumber(ExpressInfoDto expressInfoDto, Long expressId,String shopId) {
		log.info("getLogisticsExpressNumber {} ",JSON.toJSONString(expressInfoDto),expressId,shopId);
		LogisticsExpressAddressVo logisticsExpressAddressVo = this.logisticsExpressAddressMapper.queryByExpressId(expressId);
		if(BeanUtil.isEmpty(logisticsExpressAddressVo)){
			return Result.failed(String.valueOf("暂无此快递公司发货信息！"));
		}
		try{
			String data;
			if(KuaiDiHelp.SFCODE.equals(expressInfoDto.getShipperType())){
				log.info("sfparam {},{}",JSON.toJSONString(expressInfoDto),JSON.toJSONString(logisticsExpressAddressVo));
				cn.hutool.json.JSONObject sfNo = SFExpressUtil.getSFNo(expressInfoDto, logisticsExpressAddressVo);
				cn.hutool.json.JSONObject response = sfNo.getJSONObject("Response");
				String head =(String) response.get("Head");
				log.info("sfres {}",head);
				if("OK".equals(head)){
					Map<String, String> resultMap = new HashMap(CommonConstants.NUMBER_FOUR);
					cn.hutool.json.JSONObject jsonResult =  response.getJSONObject("Body").getJSONObject("OrderResponse");
					resultMap.put("waybill_no", String.valueOf(jsonResult.get("mailno")));
					resultMap.put("sorting_code", String.valueOf(jsonResult.getJSONObject("rls_info").getJSONObject("rls_detail").get("twoDimensionCode")));
					resultMap.put("package_name", String.valueOf(jsonResult.get("origincode")));
					resultMap.put("package_code", String.valueOf(jsonResult.get("dest_code")));
					data = JSON.toJSONString(resultMap);
					return Result.ok(data);
				}else {
					cn.hutool.json.JSONObject error = response.getJSONObject("ERROR");
					return Result.failed(String.valueOf(error.get("content")));
				}
			}else{
				OrderSetting orderSetting = remoteOrderService.getOrderSetting();
				String kdAppId = orderSetting.getKdAppId();
				String kdAppKey = orderSetting.getKdAppKey();
				log.info("financeOrderSetting kdAppId {} kdAppKey{}",kdAppId,kdAppKey);
				data = KuaiDiHelp.getExpressNo(expressInfoDto, logisticsExpressAddressVo, kdAppId, kdAppKey);
				log.info("KuaiDiHelp data {} ",data);
				JSONObject jsonObject = JSON.parseObject(data);
				if(String.valueOf(jsonObject.get("code")).equals(CommonConstants.NUMBER_ZERO.toString())) {
					Map<String, String> resultMap = new HashMap(CommonConstants.NUMBER_FOUR);
					JSONObject jsonData= jsonObject.getJSONObject("data");
					JSONObject jsonResult= jsonData.getJSONObject("result");
					resultMap.put("waybill_no", String.valueOf(jsonResult.get("waybill_no")));
					resultMap.put("sorting_code", String.valueOf(jsonResult.get("sorting_code")));
					resultMap.put("package_name", String.valueOf(jsonResult.get("package_name")));
					resultMap.put("package_code", String.valueOf(jsonResult.get("package_code")));
					data = JSON.toJSONString(resultMap);
					return Result.ok(data);
				}else {
					return Result.failed(String.valueOf(jsonObject.get("msg")));
				}
			}
		}catch(Exception e){
			log.error(e.getMessage());
			return Result.failed("快递单号生成失败！");
		}
	}

	private ExpressInfoDto convertDelivery(String code, Long orderId) {
		ExpressInfoDto expressInfoDto = new ExpressInfoDto();
		expressInfoDto.setOrderId(String.valueOf(orderId));
		expressInfoDto.setShipperType(code);
		OrderVo orderVo = remoteOrderService.orderInfo(orderId);
		log.info(" remoteOrderService.orderInfo {}", JSON.toJSONString(orderVo));
		OrderDelivery orderDelivery = orderVo.getOrderDelivery();
		expressInfoDto.setName(orderDelivery.getReceiverName());
		expressInfoDto.setMobile(orderDelivery.getReceiverPhone());
		expressInfoDto.setProvince(orderDelivery.getReceiverProvince());
		expressInfoDto.setCity(orderDelivery.getReceiverCity());
		expressInfoDto.setDistrict(orderDelivery.getReceiverRegion());
		expressInfoDto.setAddress(orderDelivery.getReceiverDetailAddress());

		List<OrderItemVo> orderItemList = orderVo.getOrderItemList();
		expressInfoDto.setTradeName(orderItemList.get(0).getProductName());
		List<String> itemNames = new ArrayList<>(orderItemList.size());
		orderItemList.forEach(orderItem -> {
			itemNames.add(orderItem.getProductName());
		});
		return expressInfoDto;
	}
	/**
	 * 打印运单
	 * @param logisticsPrintDeliverDto
	 * @param code
	 * @param routingInfoVo
	 * @param orderId
	 */
	private void expressPrint(LogisticsPrintDeliverDto logisticsPrintDeliverDto, String code, RoutingInfoVo routingInfoVo,
			Long orderId) {
		ExpressInfoDto expressInfoDto;
		//获取发货地址信息以及对应物流公司信息
		LogisticsExpressAddressVo logisticsExpressAddressVo = this.logisticsExpressAddressMapper
				.queryByExpressId(logisticsPrintDeliverDto.getExpressId());
		if (null == logisticsExpressAddressVo) {
			throw new ServiceException("暂无此快递公司发货信息！", SystemCode.DATA_ADD_FAILED.getCode());
		}
		expressInfoDto = convertDelivery(code, orderId);
		//获取快宝的客户id与密匙
		OrderSetting orderSetting = remoteOrderService.getOrderSetting();
		String kdAppId = orderSetting.getKdAppId();
		String kdAppKey = orderSetting.getKdAppKey();
		log.info("expressInfoDto {} ", JSON.toJSONString(expressInfoDto));
		log.info("financeOrderSetting {} ", JSON.toJSONString(orderSetting));
		log.info("logisticsExpressAddressVo {} ", JSON.toJSONString(logisticsExpressAddressVo));
		log.info("routingInfoVo {} ", routingInfoVo);
		log.info("printCode {} ", logisticsPrintDeliverDto.getPrintCode());
		String expressPrint = KuaiDiHelp
				.getExpressPrint(expressInfoDto, logisticsExpressAddressVo, kdAppId, kdAppKey, routingInfoVo,
						logisticsPrintDeliverDto.getPrintCode());
		JSONObject jsonObject = JSON.parseObject(expressPrint);
		if (jsonObject.isEmpty() || !CommonConstants.STATUS_NORMAL.equals(String.valueOf(jsonObject.get("code")))) {
			throw new ServiceException(String.valueOf(jsonObject.get("msg")), SystemCode.DATA_ADD_FAILED.getCode());
		}
	}

	/**
	 * 批量发货
	 * @param shopId
	 * @param tenantId
	 * @param logisticsBatchDeliverDtos
	 */
	@Override
	public void doBatchDeliver(List<LogisticsBatchDeliverDto> logisticsBatchDeliverDtos, String shopId,
			String tenantId) {
		if (null == logisticsBatchDeliverDtos || CommonConstants.NUMBER_ZERO == logisticsBatchDeliverDtos.size()) {
			throw new ServiceException("未获取到请求参数");
		}
		List<OrderDeliveryDto> list = new ArrayList<>(logisticsBatchDeliverDtos.size());
		logisticsBatchDeliverDtos.forEach(logisticsDeliverDto -> {
			Map<String, Object> param = new HashMap<>(CommonConstants.NUMBER_THREE);
			param.put("code", logisticsDeliverDto.getDeliverCode());
			param.put("shopId", shopId);
			param.put("tenantId", tenantId);
			LogisticsCompany logisticsCompany = logisticsCompanyMapper.selectListCompanyByParam(param);
			OrderDeliveryDto orderDeliveryDto = new OrderDeliveryDto();
			orderDeliveryDto.setOrderId(logisticsDeliverDto.getOrderId());
			orderDeliveryDto.setDeliveryCompany(logisticsCompany.getName());
			orderDeliveryDto.setDeliveryCode(logisticsDeliverDto.getDeliverCode());
			orderDeliveryDto.setDeliverySn(logisticsDeliverDto.getLogisticsCode());
			orderDeliveryDto.setOrderId(logisticsDeliverDto.getOrderId());
			list.add(orderDeliveryDto);
		});
		remoteOrderService.doLogisticsOrderDelivery(JSON.toJSONString(list));
	}
}
