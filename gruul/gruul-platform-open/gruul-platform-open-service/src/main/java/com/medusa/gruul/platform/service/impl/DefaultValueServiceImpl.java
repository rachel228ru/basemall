package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.platform.api.entity.DefaultValue;
import com.medusa.gruul.platform.api.entity.PlatformShopInfo;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail;
import com.medusa.gruul.platform.conf.MeConstant;
import com.medusa.gruul.platform.constant.DefaultUniqueIdentificationEnum;
import com.medusa.gruul.platform.mapper.DefaultValueMapper;
import com.medusa.gruul.platform.model.dto.GenerateMultipleDto;
import com.medusa.gruul.platform.service.IDefaultValueService;
import com.medusa.gruul.platform.service.IPlatformShopInfoService;
import com.medusa.gruul.platform.service.IPlatformShopTemplateDetailService;
import com.medusa.gruul.platform.service.IWechatPlatformService;
import com.medusa.gruul.shops.api.entity.ShopsPartner;
import com.medusa.gruul.shops.api.feign.RemoteShopsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 默认数据表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Service
@Log4j2
public class DefaultValueServiceImpl extends ServiceImpl<DefaultValueMapper, DefaultValue>
        implements IDefaultValueService {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private IPlatformShopTemplateDetailService platformShopTemplateDetailService;
    @Autowired
    private IPlatformShopInfoService platformShopInfoService;
    @Autowired
    private RemoteShopsService remoteShopsService;
    @Autowired
    private IWechatPlatformService wechatPlatformService;

    @Override
    public void reportDefaultValue(MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new ServiceException("文件为空", SystemCode.PARAM_TYPE_ERROR.getCode());
        }
        if (StrUtil.isEmpty(multipartFile.getOriginalFilename())) {
            throw new ServiceException("文件格式不正确,请上传.json后缀文件", SystemCode.PARAM_TYPE_ERROR.getCode());
        }
        String postfix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        if (!MeConstant.JSON.equals(postfix)) {
            throw new ServiceException("文件格式不正确,请上传.json后缀文件", SystemCode.PARAM_TYPE_ERROR.getCode());
        }
        StringBuilder builder = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
            br.lines().forEach(builder::append);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(builder.toString());
        validate(jsonObject);
        String uniqueIdentification = jsonObject.getString("uniqueIdentification");
        String version = jsonObject.getString("version");
        String kv = jsonObject.getString("kv");
        Integer buildAction = jsonObject.getInteger("buildAction");

        DefaultValue defaultValue = getByDefaultVersion(uniqueIdentification, version);

        //已存在指定默认值版本,覆盖
        if (defaultValue != null) {
            defaultValue.setBuildAction(buildAction);
            defaultValue.setKv(kv);
            defaultValue.setMq(jsonObject.getString("mq"));
            defaultValue.setUrl(jsonObject.getString("url"));
            this.baseMapper.updateById(defaultValue);
        } else {
            defaultValue = new DefaultValue();
            defaultValue.setUniqueIdentification(uniqueIdentification);
            defaultValue.setVersion(version);
            defaultValue.setBuildAction(buildAction);
            defaultValue.setUrl(jsonObject.getString("url"));
            defaultValue.setMq(jsonObject.getString("mq"));
            defaultValue.setKv(kv);
            this.baseMapper.insert(defaultValue);
        }
        //是否直接使用每次导入都重新发送一次请求(需在kv中填上租户id和shopId)
        Integer importDirectUse = jsonObject.getInteger("importDirectUse");
        if (importDirectUse != null && importDirectUse.equals(CommonConstants.NUMBER_ONE)) {
            sendDefaultValue(defaultValue, jsonObject.getString("tenantId"), jsonObject.getString("shopId"));
        }

    }

    private void validate(JSONObject jsonObject) {
        String uniqueIdentification = jsonObject.getString("uniqueIdentification");
        if (StrUtil.isEmpty(uniqueIdentification)) {
            throw new ServiceException("唯一标识不能为空");
        }
        String version = jsonObject.getString("version");
        if (StrUtil.isEmpty(version)) {
            throw new ServiceException("版本号不能为空");
        }
        Integer buildAction = jsonObject.getInteger("buildAction");
        if (buildAction == null) {
            throw new ServiceException("生成方式不能空");
        }
        if (buildAction < CommonConstants.DEFAULT_PAGE_INDEX || buildAction > CommonConstants.NUMBER_THREE) {
            throw new ServiceException("生成方式方式值");
        }
        switch (buildAction) {
            case 1:
                JSONObject mq = jsonObject.getJSONObject("mq");
                if (mq == null) {
                    throw new ServiceException("当前生成方式为mq生成,mq数据不能为空");
                }
                if (StrUtil.isEmpty(mq.getString(MeConstant.EXCHANGE)) ||
                        StrUtil.isEmpty(mq.getString("routeKey"))) {
                    throw new ServiceException("当前生成方式为mq生成,mq数据不能为空");
                }
                break;
            case 2:
                String url = jsonObject.getString("url");
                if (StrUtil.isEmpty(url)) {
                    throw new ServiceException("当前生成方式为url生成,url地址不能为空");
                }
                break;
            default:
                break;
        }
        String kv = jsonObject.getString("kv");
        if (StrUtil.isEmpty(kv)) {
            throw new ServiceException("生成数据不能为空");
        }
    }

    @Override
    public String getDefaultValue(String version, String uniqueIdentification) {
        DefaultValue defaultVersion = getByDefaultVersion(uniqueIdentification, version);
        if (defaultVersion == null) {
            return "";
        }
        return defaultVersion.getKv();
    }

    @Override
    public void informCreateDefaultValue(PlatformShopInfo info, String shopId, List<DefaultUniqueIdentificationEnum> defaultUniqueIdentificationEnums) {

        PlatformShopTemplateDetail shopTemplateDetail = platformShopTemplateDetailService.getById(info.getShopTemplateDetailId());
        List<DefaultValue> defaultValues = this.getBaseMapper().selectList(new QueryWrapper<DefaultValue>()
                .select("any_value(id)", "unique_identification")
                .in("build_action", CommonConstants.NUMBER_ONE, CommonConstants.NUMBER_TWO)
                .in("unique_identification", defaultUniqueIdentificationEnums)
                .groupBy("unique_identification"));
        if (CollectionUtil.isEmpty(defaultValues)) {
            log.debug("无所需生成数据");
            return;
        }
        for (DefaultValue defaultValue : defaultValues) {
            log.debug("发送默认值: ");
            DefaultValue defaultValue1 = getByDefaultVersion(defaultValue.getUniqueIdentification(), shopTemplateDetail.getVersion());
            if (defaultValue1 == null) {
                defaultValue1 = getByLastDefaultVersion(defaultValue.getUniqueIdentification());
            }
            if (ObjectUtil.isNull(defaultValue1)) {
                log.debug("生成默认用户中心值失败");
                return;
            }
            log.debug("默认值数据: ".concat(JSONObject.toJSONString(defaultValue1)));
            sendDefaultValue(defaultValue1, info.getTenantId(), shopId);
        }

    }


    @Override
    public String getValue(String uniqueIdentification, String version) {
        DefaultValue accountCentreDefault = getByDefaultVersion(uniqueIdentification, version);
        if (accountCentreDefault == null) {
            return null;
        }
        return accountCentreDefault.getKv();
    }


    /**
     * 发送默认值
     *
     * @param accountCentreDefault com.medusa.gruul.platform.api.entity.DefaultValue
     * @param shopId               城市合伙人id
     * @param tenantId             租户id
     */
    private void sendDefaultValue(DefaultValue accountCentreDefault, String tenantId, String shopId) {
        Integer buildAction = accountCentreDefault.getBuildAction();
        JSONObject kv = JSON.parseObject(accountCentreDefault.getKv());
        kv.put("tenantId", tenantId);
        kv.put("shopId", shopId);
        if (buildAction == 1) {
            JSONObject mq = JSON.parseObject(accountCentreDefault.getMq());
            String exchange = mq.getString("exchange");
            String routeKey = mq.getString("routeKey");
            amqpTemplate.convertAndSend(exchange, routeKey, kv.toJSONString());
        } else if (buildAction.equals(CommonConstants.NUMBER_TWO)) {
            HttpUtil.post(accountCentreDefault.getUrl(), kv.toJSONString());
        }
    }

    @Override
    public DefaultValue getByLastDefaultVersion(String uniqueIdentification) {
        return this.baseMapper.selectByLastDefaultVersion(uniqueIdentification);
    }


    @Override
    public void generateMultiple(GenerateMultipleDto generateMultipleDto) {
        List<String> uniqueIdentifications = generateMultipleDto.getUniqueIdentifications();
        if (CollectionUtil.isEmpty(uniqueIdentifications)) {
            throw new ServiceException("未选中默认值标识");
        }
        PlatformShopInfo platformShopInfo = platformShopInfoService.getById(generateMultipleDto.getShopId());
        if (platformShopInfo == null) {
            throw new ServiceException("错误数据");
        }
        ShopsPartner shopsPartner = remoteShopsService.oneByTenantId(platformShopInfo.getTenantId());
        if (shopsPartner == null) {
            throw new ServiceException("无效店铺数据");
        }
        PlatformShopTemplateDetail shopTemplateDetail = platformShopTemplateDetailService.getById(platformShopInfo.getShopTemplateDetailId());

        try {
            List<DefaultValue> defaultValues = new LinkedList<>();
            for (String uniqueIdentification : uniqueIdentifications) {
                DefaultValue byDefaultVersion = getByDefaultVersion(uniqueIdentification, shopTemplateDetail.getVersion());
                if (byDefaultVersion == null) {
                    throw new ServiceException(uniqueIdentification);
                }
                defaultValues.add(byDefaultVersion);
            }
            for (DefaultValue defaultValue : defaultValues) {
                if (DefaultUniqueIdentificationEnum.PLATFROM_SHOP_MSG.name().equals(defaultValue.getUniqueIdentification())) {
                    wechatPlatformService.addSubscribeMsg(platformShopInfo.getTenantId(), defaultValue.getVersion());
                } else {
                    sendDefaultValue(defaultValue, platformShopInfo.getTenantId(), shopsPartner.getShopId());
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMsg());
        }

    }

    @Override
    public boolean getByUniqueIdentificationIsExit(String uniqueIdentification) {
        if (StrUtil.isEmpty(uniqueIdentification)) {
            return Boolean.FALSE;
        }
        List<DefaultValue> defaultValues = this.baseMapper.selectList(new QueryWrapper<DefaultValue>()
                .eq("unique_identification", uniqueIdentification));
        return CollectionUtil.isNotEmpty(defaultValues);
    }

    @Override
    public DefaultValue getByUniqueIdentificationVersion(String uniqueIdentification, String version) {
        if (StrUtil.isEmpty(uniqueIdentification) || StrUtil.isEmpty(version)) {
            return null;
        }
        return getByDefaultVersion(uniqueIdentification, version);
    }

    @Override
    public void generateNew(String version) {
        //随便获取一条最新的模板版本号
        DefaultValue lastDefaultVersion = getByLastDefaultVersion("MiniShopMsg");
        if (lastDefaultVersion == null) {
            log.error("不存在最新的版本号，无法生成默认值");
            return;
        }
        //获取最新的版本号的所有默认值数据,进行拷贝，生成最新的指定版本号默认值
        List<DefaultValue> defaultValues = getByVersion(lastDefaultVersion.getVersion());
        for (DefaultValue defaultValue : defaultValues) {
            DefaultValue exit = getByUniqueIdentificationVersion(defaultValue.getUniqueIdentification(), version);
            if (exit == null) {
                DefaultValue newVersion = new DefaultValue();
                BeanUtil.copyProperties(defaultValue, newVersion, "createTime", "updateTime", "deleted", "id");
                newVersion.setVersion(version);
                String uniqueIdentification = defaultValue.getUniqueIdentification();
                //部分默认配置kv中需要主动设置版本
                if ("PlatfromShopMsg".equals(uniqueIdentification) || "MiniShopMsg".equals(uniqueIdentification)) {
                    JSONObject kv = JSONObject.parseObject(defaultValue.getKv());
                    kv.put("version", version);
                    defaultValue.setKv(kv.toJSONString());
                }
                this.save(newVersion);
            }
        }
    }

    /**
     * 获取指定版本的所有默认值
     *
     * @param version 版本号
     * @return com.medusa.gruul.platform.api.entity.DefaultValue
     */
    private List<DefaultValue> getByVersion(String version) {
        return this.baseMapper.selectList(new QueryWrapper<DefaultValue>()
                .eq("version", version));
    }


    /**
     * 获取指定标识指定版本默认值
     *
     * @param uniqueIdentification 唯一标识
     * @param version              上传时用使用版本号
     * @return
     */
    private DefaultValue getByDefaultVersion(String uniqueIdentification, String version) {
        return this.baseMapper.selectOne(new QueryWrapper<DefaultValue>()
                .eq("unique_identification", uniqueIdentification)
                .eq("version", version));

    }
}
