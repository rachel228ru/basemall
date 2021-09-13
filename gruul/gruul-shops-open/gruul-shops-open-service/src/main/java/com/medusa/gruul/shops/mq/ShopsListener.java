package com.medusa.gruul.shops.mq;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.shops.api.constant.QueueNameConstant;
import com.medusa.gruul.shops.api.model.AccountCenterSettingDto;
import com.medusa.gruul.shops.api.model.RenovationTemplateDto;
import com.medusa.gruul.shops.service.IAccountCenterService;
import com.medusa.gruul.shops.service.IShopGuidePageService;
import com.medusa.gruul.shops.service.IShopGuidePageSwitchService;
import com.medusa.gruul.shops.service.ShopsRenovationTemService;
import com.medusa.gruul.shops.service.impl.ShopGuidePageSwitchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author whh
 * @description 用户队列监听
 * @data: 2019/12/11
 */
@Slf4j
@Component
public class ShopsListener {


	@Autowired
	private IAccountCenterService accountCenterService;

	@Autowired
	private ShopsRenovationTemService shopsRenovationTemService;

	@Autowired
	private IShopGuidePageSwitchService shopGuidePageSwitchService;

	@Autowired
	private IShopGuidePageService shopGuidePageService;

	/**
	 * 用户中心初始化队列
	 *
	 * @param jsonStr 初始化值字符串
	 */
	@RabbitListener(queues = QueueNameConstant.SHOPS_CENTER_QUEUE_INIT)
	public void accountCenterInitReceive(String jsonStr) {
		log.info("用户中心==============>>>>>>>>>>receive message:" + jsonStr);
		try {
			AccountCenterSettingDto accountCenterSettingDto = JSONObject
					.parseObject(jsonStr, AccountCenterSettingDto.class);
			TenantContextHolder.setTenantId(accountCenterSettingDto.getTenantId());
			accountCenterService.add(accountCenterSettingDto);
		} catch (Exception e) {
            log.error(e.getMessage(), e);
		}
	}

	/**
	 * 装修模板初始化队列
	 *
	 * @param jsonStr 初始化值字符串
	 */
	@RabbitListener(queues = QueueNameConstant.SHOPS_TEMPLATE_QUEUE_INIT)
	public void renovationTemplateInitReceive(String jsonStr) {
		log.info("receive message:" + jsonStr);
		try {
			RenovationTemplateDto templateSettingDto = JSONObject.parseObject(jsonStr, RenovationTemplateDto.class);
			TenantContextHolder.setTenantId(templateSettingDto.getTenantId());
			ShopContextHolder.setShopId(templateSettingDto.getShopId());
			shopsRenovationTemService.init(templateSettingDto);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			log.info("装修模板初始化队列异常捕获,防止无限抛出异常");
		}
	}

	@RabbitListener(queues = QueueNameConstant.SHOP_GUIDE_PAGE_QUEUE_INIT)
	public void shopGuidePageInitReceive(String jsonStr) {
		log.info("receive message:" + jsonStr);
		shopGuidePageSwitchService.init(jsonStr);
		shopGuidePageService.init(jsonStr);

	}
}

//