package com.medusa.gruul.logistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.logistics.api.entity.LogisticsCompany;
import com.medusa.gruul.logistics.api.entity.LogisticsExpress;
import com.medusa.gruul.logistics.mapper.LogisticsCompanyMapper;
import com.medusa.gruul.logistics.mapper.LogisticsExpressMapper;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsExpressDto;
import com.medusa.gruul.logistics.model.param.LogisticsExpressParam;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressVo;
import com.medusa.gruul.logistics.model.vo.LogisticsRouteVo;
import com.medusa.gruul.logistics.service.ILogisticsExpressService;
import com.medusa.gruul.logistics.util.express.kuaidihelp.KuaiDiHelp;
import com.medusa.gruul.logistics.util.express.sf.SFExpressUtil;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.feign.RemoteOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class LogisticsExpressServiceImpl implements ILogisticsExpressService {

    @Autowired
    private LogisticsExpressMapper logisticsExpressMapper;

    @Autowired
    private LogisticsCompanyMapper logisticsCompanyMapper;

    @Resource
    private RemoteOrderService remoteOrderService;

    /**
     * 查询快递公司信息列表
     * @param logisticsExpressParam
     * @return IPage<LogisticsAddressVo>
     */
    @Override
    public List<LogisticsExpressVo> getExpressList(LogisticsExpressParam logisticsExpressParam) {
        List<LogisticsExpressVo> logisticsExpressVos = this.logisticsExpressMapper.queryLogisticsExpressList(logisticsExpressParam);
        return logisticsExpressVos;
    }

    /**
     * 查询单条物流公司信息
     * @param id
     * @return LogisticsAddressVo
     */
    @Override
    public LogisticsExpressVo getExpressInfo(Long id) {
        return this.logisticsExpressMapper.queryLogisticsExpressInfo(id);
    }

    /**
     * 更新/新增 快递公司信息
     * @param logisticsExpressDto
     */
    @Override
    public void setExpressInfo(LogisticsExpressDto logisticsExpressDto) {
        //物流公司基础信息表判断是否存在 不存在插入
        List<LogisticsCompany> logisticsCompanies = this.logisticsCompanyMapper.selectList(new QueryWrapper<LogisticsCompany>().eq("code" , logisticsExpressDto.getCode()));
        if(CollectionUtil.isEmpty(logisticsCompanies)){
            LogisticsCompany logisticsCompany = new LogisticsCompany();
            logisticsCompany.setCode(logisticsExpressDto.getCode());
            logisticsCompany.setName(logisticsExpressDto.getName());
            int insert = this.logisticsCompanyMapper.insert(logisticsCompany);
            if(insert == 0){
                throw new ServiceException("快递公司信息新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
        }
        //快递公司信息新增或修改
        if (logisticsExpressDto.getId() == null) {
            //新增
            LogisticsExpress logisticsExpress = logisticsExpressDto.coverBean();
            int insert = this.logisticsExpressMapper.insert(logisticsExpress);
            if(insert == 0){
                throw new ServiceException("快递公司信息新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
        } else {
            //修改
            LogisticsExpress logisticsExpressSearch = this.logisticsExpressMapper.selectById(logisticsExpressDto.getId());
            if(BeanUtil.isEmpty(logisticsExpressSearch)){
                throw new ServiceException("快递公司信息已删除或不存在！", SystemCode.DATA_EXISTED.getCode());
            }
            LogisticsExpress logisticsExpress = logisticsExpressDto.coverBean();
            int update = this.logisticsExpressMapper.updateById(logisticsExpress);
            if(update == 0){
                throw new ServiceException("快递公司信息修改失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
            }
        }
    }

    /**
     * 删除快递公司信息
     * @param id 主键
     */
    @Override
    public void delExpressInfo(Long id) {
        LogisticsExpress logisticsExpress = this.logisticsExpressMapper.selectById(id);
        if(BeanUtil.isEmpty(logisticsExpress)){
            throw new ServiceException("该信息不存在或已删除！", SystemCode.DATA_EXISTED.getCode());
        }else{
            int delete = this.logisticsExpressMapper.deleteById(id);
            if(delete == 0){
                throw new ServiceException("快递公司信息删除失败！", SystemCode.DATA_DELETE_FAILED.getCode());
            }
        }
    }

    /**
     * 根据快递公司代码与运单号获取快递物流信息
     * 顺丰快宝接口暂不支持，调用自己对接丰桥的接口
     * @param waybillNo 运单号
     * @param shipperType 快递公司代码
     * @return 快递物流信息封装
     */
    @Override
    public Result getLogisticsExpressRoute(String waybillNo, String shipperType) {
        String shopId = ShopContextHolder.getShopId();
        String expressInfo;
        if(KuaiDiHelp.SFCODE.equals(shipperType)){
            List<LogisticsExpress> logisticsExpresses = logisticsExpressMapper.selectList(new QueryWrapper<LogisticsExpress>().eq("code", KuaiDiHelp.SFCODE));
            if(CollectionUtil.isNotEmpty(logisticsExpresses)){
                LogisticsExpress logisticsExpress = logisticsExpresses.get(0);
                expressInfo = SFExpressUtil.getSFRoute(waybillNo, logisticsExpress.getCustomerName(), logisticsExpress.getCustomerPassword());
            }else{
                throw new ServiceException("请前往快递设置-物流服务设置顺丰快递的大客户账号！", SystemCode.DATA_EXISTED.getCode());
            }
        }else{
            OrderSetting orderSetting = remoteOrderService.getOrderSetting();
            String kdAppId = orderSetting.getKdAppId();
            String kdAppKey = orderSetting.getKdAppKey();
            expressInfo = KuaiDiHelp.getRoute(waybillNo, shipperType, kdAppId, kdAppKey);
            JSONObject jsonObject = JSON.parseObject(expressInfo);
            List<LogisticsRouteVo> logisticsRouteVos = new ArrayList<>();
            if(null != jsonObject &&  0 == Integer.parseInt(String.valueOf(jsonObject.get("code")))){
                JSONArray data = jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("data");
                Collections.reverse(data);
                data.forEach(object ->{
                    JSONObject job = (JSONObject) object;
                    LogisticsRouteVo logisticsRouteVo = new LogisticsRouteVo();
                    logisticsRouteVo.setTime(job.getString("time"));
                    logisticsRouteVo.setContext(job.getString("context"));
                    String address = job.getJSONObject("shop_info").getString("shop_name");
                    logisticsRouteVo.setAddress(address);
                    logisticsRouteVos.add(logisticsRouteVo);
                });
                expressInfo= JSON.toJSONString(logisticsRouteVos);
            }
        }
        return Result.ok(expressInfo);
    }

}
