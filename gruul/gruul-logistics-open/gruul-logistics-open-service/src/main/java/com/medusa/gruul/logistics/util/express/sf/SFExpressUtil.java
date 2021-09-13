package com.medusa.gruul.logistics.util.express.sf;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.XML;
import com.alibaba.fastjson.JSON;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.StringUtil;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.logistics.model.dto.manager.express.ExpressInfoDto;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressAddressVo;
import com.medusa.gruul.logistics.model.vo.LogisticsRouteVo;
import com.medusa.gruul.logistics.util.CoverXMLUtil;
import com.medusa.gruul.logistics.util.express.sf.bean.send.Body;
import com.medusa.gruul.logistics.util.express.sf.bean.send.Order;
import com.medusa.gruul.logistics.util.express.sf.bean.send.Request;
import com.medusa.gruul.logistics.util.express.sf.bean.send.RouteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * <p>
 * 快递接口工具类
 * </p>
 *
 * @author lcysike
 * @since 2020-03-10
 */
public class SFExpressUtil {

    public static Logger logger = LoggerFactory.getLogger(CallExpressServiceTools.class);
    /**
     * 快递接口
     */
    private static final String REQ_URL = "https://sfapi-sbox.sf-express.com/sfexpressService";
    /**
     * 顾客编码
     */
    private static final String CLIENT_CODE = "";
    /**
     * 校验码
     */
    private static final String CHECK_WORD = "";
    /**
     * 编码格式
     */
    private static final String UNION_CODE = "UTF-8";
    /**
     * 语言
     */
    private static final String LANG = "zh-CN";
    /**
     * 获取运单号API接口方法名称
     */
    public static final String CREATE_METHOD = "OrderService";
    /**
     * 获取物流信息API接口方法名称
     */
    public static final String ROUTE_METHOD = "RouteService";


    /**
     * 生成顺丰快递单号
     *
     * @param expressInfoDto 订单传输数据
     * @param logisticsExpressAddressVo  默认发货地址
     * @return
     */
    public static JSONObject getSFNo(ExpressInfoDto expressInfoDto, LogisticsExpressAddressVo logisticsExpressAddressVo) {
        Request rt = new Request();
        rt.setService(CREATE_METHOD);
        rt.setLang(LANG);
        rt.setHead(logisticsExpressAddressVo.getCustomerName());
        Body by = new Body();
        Order or = new Order();
        or.setOrderId(expressInfoDto.getOrderId());
        or.setjContact(logisticsExpressAddressVo.getReceiveName());
        or.setjTel(logisticsExpressAddressVo.getReceivePhone());
        or.setjProvince(logisticsExpressAddressVo.getProvince());
        or.setjCity(logisticsExpressAddressVo.getCity());
        or.setjCounty(logisticsExpressAddressVo.getCountry());
        or.setjAddress(logisticsExpressAddressVo.getAddress());
        or.setPayMethod(CommonConstants.NUMBER_ONE.toString());
        or.setExpressType(CommonConstants.NUMBER_ONE.toString());
        or.setdContact(expressInfoDto.getName());
        or.setdTel(expressInfoDto.getMobile());
        or.setdProvince(expressInfoDto.getProvince());
        or.setdCity(expressInfoDto.getCity());
        or.setdCounty(expressInfoDto.getDistrict());
        or.setdAddress(expressInfoDto.getAddress());
        or.setIsUnifiedWaybillNo(CommonConstants.NUMBER_ONE.toString());
        by.setOrder(or);
        rt.setBody(by);
        String reqXml = CoverXMLUtil.convertToXml(rt, UNION_CODE);
        logger.info("请求报文:" + reqXml);
        String respXml = CallExpressServiceTools.callSfExpressServiceByCSIM(REQ_URL, reqXml, logisticsExpressAddressVo.getCustomerName(), logisticsExpressAddressVo.getCustomerPassword());
        if (respXml != null) {
            logger.info("--------------------------------------");
            logger.info("返回报文: " + respXml);
            logger.info("--------------------------------------");
        }
        return XML.toJSONObject(respXml);
    }

    /**
     * 获取顺丰快递单号路由信息
     *
     * @param orderNo
     * @return
     */
    public static String getSFRoute(String orderNo, String clientCode, String checkWord) {
        //组装请求信息
        Request rt = new Request();
        rt.setService(ROUTE_METHOD);
        rt.setHead(clientCode);
        Body by = new Body();
        RouteRequest routeRequest = new RouteRequest();
        routeRequest.setMethodType(CommonConstants.NUMBER_ONE.toString());
        routeRequest.setTrackingType(CommonConstants.NUMBER_ONE.toString());
        routeRequest.setTrackingNumber(orderNo);
        by.setRouteRequest(routeRequest);
        rt.setBody(by);
        String reqXml = CoverXMLUtil.convertToXml(rt, UNION_CODE);
        logger.info("请求报文:" + reqXml);
        String respXml = CallExpressServiceTools.callSfExpressServiceByCSIM(REQ_URL, reqXml, clientCode, checkWord);
        if (respXml != null) {
            logger.info("--------------------------------------");
            logger.info("返回报文: " + respXml);
            logger.info("--------------------------------------");
        }

        cn.hutool.json.JSONObject response = XML.toJSONObject(respXml).getJSONObject("Response");
        String head =(String) response.get("Head");
        if("OK".equals(head)){
            if(ObjectUtil.isNotEmpty(response.get("Body"))){
                List<JSONObject> list = (List<JSONObject>) response.getJSONObject("Body").getJSONObject("RouteResponse").getJSONObject("Route").toBean(ArrayList.class);
                Collections.reverse(list);
                List<LogisticsRouteVo>logisticsRouteVos = new ArrayList<>(list.size());
                list.forEach(jsonObject -> {
                    LogisticsRouteVo logisticsRouteVo = new LogisticsRouteVo();
                    logisticsRouteVo.setTime(String.valueOf(jsonObject.get("accept_time")));
                    logisticsRouteVo.setAddress(String.valueOf(jsonObject.get("accept_address")));
                    logisticsRouteVo.setContext(String.valueOf(jsonObject.get("remark")));
                    logisticsRouteVos.add(logisticsRouteVo);
                });
                return JSON.toJSONString(logisticsRouteVos);
            }else{
                throw new ServiceException(" 暂无对应物流信息 ！", SystemCode.DATA_EXISTED.getCode());
            }

        }else {
            throw new ServiceException(" 获取详情失败 ！", SystemCode.DATA_EXISTED.getCode());
        }

    }


}