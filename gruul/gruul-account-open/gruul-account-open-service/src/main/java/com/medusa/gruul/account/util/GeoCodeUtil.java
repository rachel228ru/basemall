package com.medusa.gruul.account.util;


import cn.hutool.http.HttpUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 高德地图工具类
 *
 * @author whh
 */
@Component
@ConfigurationProperties(prefix = "geo")
public class GeoCodeUtil {

    /**
     * 高德地图秘钥
     */
    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 获取地理/地理编码 API
     */
    private static String ADDRESSENCODED_URL = "https://restapi.amap.com/v3/geocode/geo";


    /**
     * 获取地理/逆地理编码  API
     */
    private static String REGEO_URL = "https://restapi.amap.com/v3/geocode/regeo";


    /**
     * 获取地理
     *
     * @param address 结构化地址信息 规则遵循：国家、省份、城市、区县、城镇、乡村、街道、门牌号码、屋邨、大厦，如：北京市朝阳区阜通东大街6号。
     *                如果需要解析多个地址的话，请用"|"进行间隔，并且将 batch 参数设置为 true，最多支持 10 个地址进进行"|"分割形式的请求。
     * @param batch   是否批量查询控制
     *                batch 参数设置为 true 时进行批量查询操作，最多支持 10 个地址进行批量查询。
     *                batch 参数设置为 false 时进行单点查询，此时即使传入多个地址也只返回第一个地址的解析查询结果。
     * @return {
     * count: "1"
     * geocodes: [{formatted_address: "北京市朝阳区阜通东大街|6号", country: "中国", province: "北京市", citycode: "010", city: "北京市"},{...}]
     * info: "OK"
     * infocode: "10000"
     * status: "1"
     * }
     * @url https://lbs.amap.com/api/webservice/guide/api/georegeo
     */
    public String getAddressEncoded(String address, boolean batch) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("key", key);
        params.put("address", address);
        params.put("batch", batch);
        return HttpUtil.get(ADDRESSENCODED_URL, params, -1);
    }


    /**
     * 逆地理编码
     *
     * @param location 传入内容规则：经度在前，纬度在后，经纬度间以“,”分割，经纬度小数点后不要超过 6 位。
     *                 如果需要解析多个经纬度的话，请用"|"进行间隔，并且将 batch 参数设置为 true，最多支持传入 20 对坐标点。每对点坐标之间用"|"分割。
     * @return 参照高德返回值 @url:https://lbs.amap.com/api/webservice/guide/api/georegeo#regeo
     */
    public String getAddressRegeo(String location, boolean batch) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("key", key);
        params.put("location", location);
        params.put("batch", batch);
        params.put("extensions", "base");
        return HttpUtil.get(REGEO_URL, params, -1);
    }


}
