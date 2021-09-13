package com.medusa.gruul.common.core.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.medusa.gruul.common.dto.AreaDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Description:  省市区选择器工具类</P>
 * <p>Version: 1.0</p>
 * <p>Author: Mr.zhaozheng </P>
 * <p>Date: 2019-11-20 23:14 </p>
 *
 * @author Mr.zhaozheng
 */
public final class AreaUtil {

    private static Map<String, List<AreaDto>> AREA_CACHE = new HashMap<>(3);

    /**
     * <p>@Description: getProvinceList 获取省级列表</P>
     * <p>Version: 1.0</p>
     * <p>ModifyVersion: </p>
     * <pre>
     *  AreaUtil.getProvinceList(null) =查询所有
     *  AreaUtil.getProvinceList("110000") =查询所有
     * </pre>
     * <p>Author: Mr.zhaozheng </P>
     * <p>Date: 2019-11-20 23:01 </p>
     * <p>Param: []</P>
     * <p>return: java.util.List<com.medusa.gruul.common.dto.AreaDto></P>
     */
    public static List<AreaDto> getProvinceList(String id) {
        return getList(0, id);
    }

    /**
     * <p>@Description: getCityList 获取市级列表.</P>
     * <p>Version: 1.0</p>
     * <p>ModifyVersion: </p>
     * <pre>
     *  AreaUtil.getCityList(null) =返回空数组 :[]
     *  AreaUtil.getCityList("110000") =查询所有该省下所有的市列表
     * </pre>
     * <p>Author: Mr.zhaozheng </P>
     * <p>Date: 2019-11-20 23:04 </p>
     * <p>Param: [lastId]</P>
     * <p>return: java.util.List<com.medusa.gruul.common.dto.AreaDto></P>
     */
    public static List<AreaDto> getCityList(String lastId) {

        if (StringUtils.isEmpty(lastId)) {
            return new ArrayList<>();
        }
        return getList(1, lastId);

    }

    /**
     * <p>@Description: getDistrictList 获取市级下所有区列表</P>
     * <p>Version: 1.0</p>
     * <p>ModifyVersion: </p>
     * <pre>
     *  AreaUtil.getDistrictList(null) =返回空数组 :[]
     *  AreaUtil.getDistrictList("110100") =查询所有该市级下所有的区列表
     * </pre>
     * <p>Author: Mr.zhaozheng </P>
     * <p>Date: 2019-11-20 23:07 </p>
     * <p>Param: [lastId]</P>
     * <p>return: java.util.List<com.medusa.gruul.common.dto.AreaDto></P>
     */
    public static List<AreaDto> getDistrictList(String lastId) {

        if (StringUtils.isEmpty(lastId)) {
            return new ArrayList<>();
        }
        return getList(2, lastId);
    }

    /**
     * <p>@Description: getList </P>
     * <p>Version: </p>
     * <p>ModifyVersion: </p>
     * <p>Author: Mr.zhaozheng </P>
     * <p>Date: 2019-11-20 22:49 </p>
     * <pre>
     * AreaUtil.getList(0,null)         =查询所有省列表
     * AreaUtil.getList(0,"110000")      =查询所有省列表
     * AreaUtil.getList(1,"110000")      =查询某省下所有市级列表
     * AreaUtil.getList(1,null)      =返回空数组
     * AreaUtil.getList(2,"110100")      =返回某市下所有区列表
     * AreaUtil.getList(2,null)      =返回空数组
     * </pre>
     *
     * @param type   0=查询所有列表,1=查询省下所有市,2=市下所有区
     * @param lastId 上一级id
     *               <p>Param: [type, lastId]</P>
     *               <p>return: java.util.List<com.medusa.gruul.common.dto.AreaDto></P>
     */
    private static List<AreaDto> getList(Integer type, String lastId) {
        try {
            String key = type.toString() + lastId;
            List<AreaDto> areaDtos = AREA_CACHE.get(key);
            if (CollectionUtil.isNotEmpty(areaDtos)) {
                return areaDtos;
            }
            String path = "/file/";
            if (type == 0) {
                path = path + "province.json";
            } else if (type == 1) {
                path = path + "city.json";
            } else if (type == 2) {
                path = path + "district.json";
            }

            ClassPathResource resource = new ClassPathResource(path);
            InputStream inputStream = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            List<AreaDto> dtos = JSONArray.parseArray(builder.toString(), AreaDto.class);
            if (type == 0) {
                return dtos;
            }
            List<AreaDto> outDto = new ArrayList<>();
            if (StringUtils.isEmpty(lastId)) {
                return dtos;
            }
            dtos.forEach(item -> {
                if (item.getFatherid().equals(lastId)) {
                    outDto.add(item);
                }
            });
            AREA_CACHE.put(key, outDto);
            return outDto;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * <p>@Description: getNameById 根据id 获取对应名称</P>
     * <p>Version: 1.0</p>
     * <p>Author: Mr.zhaozheng </P>
     * <pre>
     * AreaUtil.getNameById("")         =""
     * AreaUtil.getNameById(null)         =""
     * AreaUtil.getNameById("110000")      = "北京市"
     * AreaUtil.getNameById("110100")      ="市辖区"
     * AreaUtil.getNameById("110101")      ="XX区"
     * </pre>
     *
     * <p>Date: 2019-11-21 11:26 </p>
     * <p>Param: [id]</P>
     * <p>return: java.lang.String</P>
     */
    public static String getNameById(String id) {

        StringBuffer buffer = new StringBuffer();
        try {
            if (StringUtils.isEmpty(id)) {
                throw new Exception();
            }
            AtomicInteger atomicInteger = new AtomicInteger(0);
            while (atomicInteger.get() < 3) {
                List<AreaDto> list = getList(atomicInteger.get(), null);
                for (AreaDto item : list) {
                    if (item.getValue().equals(id)) {
                        buffer.append(item.getLabel());
                        atomicInteger.set(4);
                        break;
                    }
                }
                atomicInteger.incrementAndGet();
            }

            return buffer.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * <p>@Description: getByLabel 根据label值获取对应数据对象</P>
     * <p>Version: 1.0</p>
     * <p>Author: whh </P>
     * <pre>
     * AreaUtil.getByLabel(0,"浙江省")         =com.medusa.gruul.common.dto.AreaDto
     * AreaUtil.getByLabel(1,"宁波市")         =com.medusa.gruul.common.dto.AreaDto
     * AreaUtil.getByLabel(2,"市辖区")      = com.medusa.gruul.common.dto.AreaDto
     * </pre>
     *
     * <p>Date: 2019-12-1 11:26 </p>
     * <p>Param: [type]  0-省 1-市 1-区</P>
     * <p>Param: [label]  名称</P>
     * <p>return: com.medusa.gruul.common.dto.AreaDto</P>
     */
    public static AreaDto getByLabel(Integer type, String label) {

        if (type == null || StringUtils.isEmpty(label)) {
            return null;
        }
        List<AreaDto> list = getList(type, null);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        for (AreaDto item : list) {
            if (item.getLabel().equals(label)) {
                return item;
            }
        }
        return null;
    }

    /**
     * <p>@Description: getByLabel 根据value值获取对应数据对象</P>
     * <p>Version: 1.0</p>
     * <p>Author: whh </P>
     * <pre>
     * AreaUtil.getByLabel(2,"110108")      = com.medusa.gruul.common.dto.AreaDto
     * </pre>
     *
     * <p>Date: 2019-12-1 11:26 </p>
     * <p>Param: [type]  0-省 1-市 1-区</P>
     * <p>Param: [value]  区域编码</P>
     * <p>return: com.medusa.gruul.common.dto.AreaDto</P>
     */
    public static AreaDto getByValue(Integer type, String value) {

        if (type == null || StringUtils.isEmpty(value)) {
            return null;
        }
        List<AreaDto> list = getList(type, null);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        for (AreaDto item : list) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 获取指定城市下的指定区域
     * todo s1.0.6下线之后删除
     *
     * @param cityLabel 城市名
     * @param areaLabel 区域名
     * @return
     */
    @Deprecated
    public static Optional<AreaDto> getByCityLabelAndAreaLabel(String cityLabel, String areaLabel) {

        if (StringUtils.isEmpty(cityLabel) || StringUtils.isEmpty(areaLabel)) {
            return Optional.empty();
        }
        AreaDto cityAreaDto = getByLabel(1, cityLabel);
        if (cityAreaDto == null) {
            return Optional.empty();
        }
        List<AreaDto> list = AreaUtil.getList(2, cityAreaDto.getValue());
        if (CollectionUtil.isEmpty(list)) {
            return Optional.empty();
        }
        for (AreaDto areaDto : list) {
            if (areaDto.getLabel().equals(areaLabel)) {
                return Optional.of(areaDto);
            }
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
        System.out.println(getByCityLabelAndAreaLabel("市辖区", "渝中区"));
    }

}
