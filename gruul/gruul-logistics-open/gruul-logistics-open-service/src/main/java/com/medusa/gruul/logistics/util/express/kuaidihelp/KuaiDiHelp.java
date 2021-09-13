package com.medusa.gruul.logistics.util.express.kuaidihelp;

import com.alibaba.fastjson.JSON;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.logistics.model.dto.manager.express.ExpressInfoDto;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressAddressVo;
import com.medusa.gruul.logistics.model.vo.RoutingInfoVo;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


/**
 * <p>
 *  快宝物流接口
 * </p>
 *
 * @author lcysike
 * @since 2020-03-12
 */
public class KuaiDiHelp {

    public static Logger logger = LoggerFactory.getLogger(KuaiDiHelp.class);

    /**
     * 请求接口地址
     */
    public static final String HOST = "http://kop.kuaidihelp.com";
    /**
     * 请求接口地址后缀
     */
    public static final String PATH = "/api";
    /**
     * 请求方式
     */
    public static final String REQUESTMETHOD = "POST";
    /**
     * 获取运单号API接口方法名称
     */
    public static final String CREATEMETHOD = "account.waybill.get";
    /**
     * 获取物流信息API接口方法名称
     */
    public static final String ROUTEMETHOD = "express.info.get";
    /**
     * 调用打印接口接口方法名称
     */
    public static final String PRINTMETHOD = "cloud.print.do";

    /**
     * 顺丰公司快递码
     */
    public static final String SFCODE = "sf";

    /**
     * 获取快递单号物流信息
     *
     * @param waybillNo 快递单号（除顺丰需单独走丰桥接口）
     * @param companyCode 快递公司代码
     * @param kdAppId 快宝商户id
     * @param kdAppKey 快宝客户密匙
     */
    public static String getRoute(String waybillNo, String companyCode, String kdAppId, String kdAppKey){
        Map<String, String> headers = new HashMap<>(CommonConstants.NUMBER_ONE);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>(CommonConstants.NUMBER_ONE);
        Map<String, String> bodys = new HashMap<>(CommonConstants.NUMBER_FIVE);
        //组装bodys基础数据
        createExpressBody(bodys,ROUTEMETHOD, kdAppId, kdAppKey);
        // data参数是个json格式的字符串  建议使用函数或方法去生成
        Map<String, String> dataMap = new HashMap<>(CommonConstants.NUMBER_THREE);
        dataMap.put("waybill_no", waybillNo);
        dataMap.put("exp_company_code", companyCode);
        dataMap.put("result_sort", "1");
        String data = JSON.toJSONString(dataMap);
        bodys.put("data", data);
        //调用快宝接口
        return queryApi(headers, querys, bodys);
    }

    /**
     * 在线生成对应快递公司运单号（除顺丰需单独走丰桥接口）
     *
     * @param expressInfoDto 订单传输对象
     * @param logisticsExpressAddressVo 快递公司设置信息
     * @param kdAppId 快宝商户id
     * @param kdAppKey 快宝客户密匙
     */
    public static String getExpressNo(ExpressInfoDto expressInfoDto, LogisticsExpressAddressVo logisticsExpressAddressVo, String kdAppId, String kdAppKey){
        Map<String, String> headers = new HashMap<>(CommonConstants.NUMBER_ONE);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>(CommonConstants.NUMBER_ONE);
        Map<String, String> bodys = new HashMap<>(CommonConstants.NUMBER_FIVE);
        //组装bodys基础数据
        createExpressBody(bodys,CREATEMETHOD, kdAppId, kdAppKey);
        String data = createData(expressInfoDto, logisticsExpressAddressVo);
        bodys.put("data", data);
        //调用快宝接口
        return queryApi(headers, querys, bodys);
    }

    public static void createExpressBody(Map<String, String> bodys, String method, String kdAppId, String kdAppKey){
        // 当前时间戳
        String ts = System.currentTimeMillis() + "";
        // 计算签名
        String signStr = kdAppId + method + ts + kdAppKey;
        String sign = getMD5(signStr, 32);
        bodys.put("app_id", kdAppId);
        bodys.put("method", method);
        bodys.put("ts", ts);
        bodys.put("sign", sign);
    }

    /**
     * 调用打印机生成电子面单
     *
     * @param expressInfoDto 订单传输对象
     * @param logisticsExpressAddressVo 快递公司设置信息
     * @param kdAppId 快宝商户id
     * @param kdAppKey 快宝客户密匙
     * @param printKey 打印机密匙
     * @param routingInfoVo 路由及运单号信息
     */
    public static String getExpressPrint(ExpressInfoDto expressInfoDto, LogisticsExpressAddressVo logisticsExpressAddressVo,
                                         String kdAppId, String kdAppKey, RoutingInfoVo routingInfoVo, String printKey){
        Map<String, String> headers = new HashMap<>(CommonConstants.NUMBER_ONE);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>(CommonConstants.NUMBER_ZERO);
        Map<String, String> bodys = new HashMap<>(CommonConstants.NUMBER_FIVE);
        //组装bodys基础数据
        createExpressBody(bodys,PRINTMETHOD, kdAppId, kdAppKey);
        String data = createPrintData(expressInfoDto, logisticsExpressAddressVo, routingInfoVo, printKey);
        bodys.put("data", data);
        //调用快宝接口
        return queryApi(headers, querys, bodys);
    }

    /**
     * 电子面单打印数据组装
     */
    public static String createPrintData(ExpressInfoDto expressInfoDto, LogisticsExpressAddressVo logisticsExpressAddressVo,
                                         RoutingInfoVo routingInfoVo, String printKey){
        Map<String, Object> data = new HashMap<>(CommonConstants.NUMBER_FOUR);
        Map<String, Object> sender = new HashMap<>(CommonConstants.NUMBER_FOUR);
        Map<String, Object> recipient = new HashMap<>(CommonConstants.NUMBER_FOUR);
        Map<String, Object> printData = new HashMap<>(CommonConstants.NUMBER_FIVE);
        Map<String, Object> routingInfo = new HashMap<>(CommonConstants.NUMBER_THREE);
        Map<String, Object> consolidation = new HashMap<>(CommonConstants.NUMBER_TWO);

        //快递公司code
        printData.put("cp_code", expressInfoDto.getShipperType());
        //商品名称
        printData.put("goods_name", "");
        //打印类型，1:预览, 2:打印, 3:打印预览
        printData.put("print_type", "2");
        //商城系统订单号
        printData.put("tid", expressInfoDto.getOrderId());
        printData.put("waybill_code", routingInfoVo.getWaybillCode());
        //路由信息
        consolidation.put("name", routingInfoVo.getPackageName());
        consolidation.put("code", routingInfoVo.getPackageCode());
        routingInfo.put("consolidation", consolidation);
        routingInfo.put("route_code", routingInfoVo.getSortingCode());
        printData.put("routing_info", routingInfo);
        //发货人信息map
        sender.put("name", logisticsExpressAddressVo.getReceiveName());
        sender.put("mobile",logisticsExpressAddressVo.getReceivePhone());
        Map<String, String> sendAddress = new HashMap<>(CommonConstants.NUMBER_FOUR);
        sendAddress.put("province",logisticsExpressAddressVo.getProvince());
        sendAddress.put("city",logisticsExpressAddressVo.getCity());
        sendAddress.put("district",logisticsExpressAddressVo.getCountry());
        sendAddress.put("detail", logisticsExpressAddressVo.getAddress());
        sender.put("address", sendAddress);
        printData.put("sender", sender);
        //收货人信息map
        recipient.put("name",expressInfoDto.getName());
        recipient.put("mobile",expressInfoDto.getMobile());
        Map<String, String> recipientAddress = new HashMap<>(CommonConstants.NUMBER_FOUR);
        recipientAddress.put("province",expressInfoDto.getProvince());
        recipientAddress.put("city",expressInfoDto.getCity());
        recipientAddress.put("district",expressInfoDto.getDistrict());
        recipientAddress.put("detail", expressInfoDto.getAddress());
        recipient.put("address", recipientAddress);
        printData.put("recipient", recipient);
        //目标云打印机的访问密钥，在电脑端快宝云打印程序的“绑定”->“访问密钥”菜单项下查看
        data.put("agent_id", printKey);
        //打印类型，1：仅生成底单 2：仅打印 3：打印并生成底单；默认为3，若任务内有设置，则以任务内设置为准
        data.put("print_type", "2");
        //是否为批量打印，若为false，print_data为单条打印数据，否则print_data必须为多条打印数据组成的数组（每批不能超过50条）
        data.put("batch", "false");
        data.put("action", "print.json.cn");
        //打印数据
        data.put("print_data", printData);
        return JSON.toJSONString(data);
    }

    /**
     * 生成快递单号收发货及商品信息数据组装
     */
    public static String createData(ExpressInfoDto expressInfoDto, LogisticsExpressAddressVo logisticsExpressAddressVo){
        Map<String, Object> data = new HashMap<>(CommonConstants.NUMBER_TEN);
        Map<String, String> sender = new HashMap<>(CommonConstants.NUMBER_NINE);
        Map<String, String> recipient = new HashMap<>(CommonConstants.NUMBER_NINE);
        //快递平台的appid与密匙
        data.put("customer_name", logisticsExpressAddressVo.getCustomerName());
        data.put("customer_password", logisticsExpressAddressVo.getCustomerPassword());
        data.put("order_id", expressInfoDto.getOrderId());
        data.put("shipper_type", expressInfoDto.getShipperType());
        data.put("pay_type", CommonConstants.NUMBER_ONE.toString());
        //发货人信息map
        sender.put("name", logisticsExpressAddressVo.getReceiveName());
        sender.put("mobile",logisticsExpressAddressVo.getReceivePhone());
        sender.put("province",expressInfoDto.getProvince());
        sender.put("city",expressInfoDto.getCity());
        sender.put("district",expressInfoDto.getDistrict());
        sender.put("address", logisticsExpressAddressVo.getAddress());
        data.put("sender", sender);
        //收货人信息map
        recipient.put("name",expressInfoDto.getName());
        recipient.put("mobile",expressInfoDto.getMobile());
        recipient.put("province",expressInfoDto.getProvince());
        recipient.put("city",expressInfoDto.getCity());
        recipient.put("district",expressInfoDto.getDistrict());
        recipient.put("address", expressInfoDto.getAddress());
        data.put("recipient", recipient);
        //商品信息map
        List<Map<String, String>> list = new ArrayList<>();
        Map map = new HashMap(CommonConstants.NUMBER_TWO);
        map.put("name", expressInfoDto.getTradeName());
        list.add(map);
        data.put("commodities", list);
        return JSON.toJSONString(data);
    }

    /**
     *计算md5
     */
    public static String getMD5(String plainText, int length) {
        try {
            //获取MD5实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            //此处传入要加密的byte类型值
            md.update(plainText.getBytes());
            //此处得到的是md5加密后的byte类型值
            byte[] digest = md.digest();
            /**
               下边的运算就是自己添加的一些二次小加密，记住这个千万不能弄错乱，
                   否则在解密的时候，你会发现值不对的（举例：在注册的时候加密方式是一种，
                在我们登录的时候是不是还需要加密它的密码然后和数据库的进行比对，但是
            最后我们发现，明明密码对啊，就是打不到预期效果，这时候你就要想一下，你是否
             有改动前后的加密方式）
            */
            int i;
            StringBuilder sb = new StringBuilder();
            for (int offset = 0; offset < digest.length; offset++) {
                i = digest[offset];
                if (i < 0){
                    i += 256;
                }
                if (i < 16) {
                    sb.append(0);
                }
                //通过Integer.toHexString方法把值变为16进制
                sb.append(Integer.toHexString(i));
            }
            //从下标0开始，length目的是截取多少长度的值
            return sb.toString().substring(0, length);
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5加密失败！");
            return null;
        }
    }

    /**
     * 快宝API接口调用公共方法
     */
    public static String queryApi(Map<String, String> headers, Map<String, String> querys, Map<String, String> bodys){
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(HOST, PATH, REQUESTMETHOD, headers, querys, bodys);
            System.out.println(response.getEntity());
            //获取response的body
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            throw new ServiceException("快宝接口调用失败！", SystemCode.DATA_ADD_FAILED.getCode());
        }
    }


}
