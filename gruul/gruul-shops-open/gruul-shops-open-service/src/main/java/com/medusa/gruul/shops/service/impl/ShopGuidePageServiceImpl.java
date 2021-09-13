package com.medusa.gruul.shops.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.shops.api.entity.ShopGuidePage;
import com.medusa.gruul.shops.api.entity.ShopGuidePageSwitch;
import com.medusa.gruul.shops.mapper.ShopGuidePageMapper;
import com.medusa.gruul.shops.model.dto.ShopGuidePageDto;
import com.medusa.gruul.shops.model.vo.ShopGuidePageVo;
import com.medusa.gruul.shops.service.IShopGuidePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 店铺引导页服务实现类
 * @Author: xiaoq
 * @Date : 2020/10/15 16:48
 */
@Service
public class ShopGuidePageServiceImpl extends ServiceImpl<ShopGuidePageMapper, ShopGuidePage>
		implements IShopGuidePageService {


	@Autowired
	private ShopGuidePageMapper shopGuidePageMapper;

	/**
	 *  获取店铺自定义引导页 没值情况下返回null
	 * @return 获取店铺自定义引导页VO
	 */
	@Override
	public List<ShopGuidePageDto> getShopGuidePage() {
		return shopGuidePageMapper.selectGuidePage();
	}


	/**
	 * 自定义引导页更新 
	 * @param shopGuidePageDtos 自定义引导页dto
	 */

	@Override
	public void updateShopGuidePage(List<ShopGuidePageDto> shopGuidePageDtos) {
		int i = 0;
		List<ShopGuidePageDto> shopGuidePageCustomDtos = shopGuidePageMapper.selectGuidePage();
		for (ShopGuidePageDto shopGuidePageCustomDto : shopGuidePageCustomDtos) {
			ShopGuidePageDto shopGuidePageDto = shopGuidePageDtos.get(i);
			ShopGuidePage shopGuidePage = shopGuidePageDto.coverShopGuidePage();
			shopGuidePage.setId(shopGuidePageCustomDto.getId());
			int insert = shopGuidePageMapper.updateById(shopGuidePage);
			if (insert < 1) {
				throw new ServiceException("自定义引导页更新失败");
			}
			i++;
		}
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShopGuide(List<ShopGuidePageDto> shopGuidePageDtos){
        if (shopGuidePageDtos.size()>CommonConstants.DEFAULT_MAX_RETRIES){
            throw new ServiceException("引导页不能超过五张");
        }
        //取出该店铺引导页配置
        List<ShopGuidePage> list = this.list();
        //cop到 ShopGuidePage
        List<ShopGuidePage> collect = shopGuidePageDtos.stream().map(e -> {
            ShopGuidePage shopGuidePage = new ShopGuidePage();
            BeanUtil.copyProperties(e, shopGuidePage);
            return shopGuidePage;
        }).collect(Collectors.toList());
        //计算删除了哪些引导页
        for (int i = 0; i < collect.size(); i++) {
            //如果id是空 为新加引导页，跳出本次循环
            if (collect.get(i).getId() == null || collect.get(i).getId().equals(CommonConstants.NUMBER_ZERO)){
                continue;
            }
            //循环原来引导页list 去id相同 剩下的为要删除的
            for (int i1 = 0; i1 < list.size(); i1++) {
                if (list.get(i1).getId().equals(collect.get(i).getId())){
                    list.remove(i1);
                }
            }
        }
        //判断处理后要删除的list是否为空 不为空则进入删除
        if (list.size() > CommonConstants.NUMBER_ZERO){
            //取出要删除的id list
            List<Long> collect1 = list.stream().map(e -> e.getId()).collect(Collectors.toList());
            //批量删除 通过id
            int i = shopGuidePageMapper.deleteBatchIds(collect1);
            if (i < CommonConstants.NUMBER_ONE){
                throw new ServiceException("Failed to delete data");
            }
        }
        if (ArrayUtil.isEmpty(collect)){
            return;
        }
        boolean i = false;
        //批量修改新增
        for (ShopGuidePage shopGuidePage:collect){
            if (shopGuidePage.getId() == null){
                i = this.save(shopGuidePage);
            }else {
                i = this.updateById(shopGuidePage);
            }
            if (!i){
                throw new ServiceException("自定义引导页更新失败");
            }
        }
    }

	/**
	 *  店铺默认引导页获取
	 * @return
	 */
	@Override
	public List<ShopGuidePageDto> getShopGuidePageDefault() {

		return shopGuidePageMapper.selectGuidePageDefault();
	}

	/**
	 * 店铺默认引导页信息新增
	 * @param jsonStr
	 */
	@Override
	public void init(String jsonStr) {
		int i = 0;
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		String tenantId = jsonObject.getString("tenantId");
		String shopId = jsonObject.getString("shopId");
		if (StrUtil.isEmpty(tenantId) || StrUtil.isEmpty(shopId)) {
			throw new ServiceException("jsonStr:".concat(jsonStr).concat("--->数据为空"));
		}
		TenantContextHolder.setTenantId(tenantId);
		ShopContextHolder.setShopId(shopId);
		List<ShopGuidePageDto> shopGuidePageDtos = shopGuidePageMapper.selectGuidePageDefault();
        ShopGuidePage shopGuidePage = new ShopGuidePage();
		for (ShopGuidePageDto shopGuidePageDto:shopGuidePageDtos){
            shopGuidePage.setPath(shopGuidePageDto.getPath());
            shopGuidePage.setLink(shopGuidePageDto.getLink());
            shopGuidePage.setType(shopGuidePageDto.getType());
            shopGuidePage.setUrl(shopGuidePageDto.getUrl());
            shopGuidePage.setAppId(shopGuidePageDto.getAppId());
            shopGuidePageMapper.insert(shopGuidePage);
        }
	}
}
