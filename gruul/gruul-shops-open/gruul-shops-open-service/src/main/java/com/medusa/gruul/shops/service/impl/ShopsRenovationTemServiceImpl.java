package com.medusa.gruul.shops.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.annotation.EscapeShop;
import com.medusa.gruul.common.data.annotation.EscapeTenant;
import com.medusa.gruul.shops.api.entity.ShopsRenovationAssembly;
import com.medusa.gruul.shops.api.entity.ShopsRenovationPage;
import com.medusa.gruul.shops.api.entity.ShopsRenovationPlugin;
import com.medusa.gruul.shops.api.entity.ShopsRenovationTemplate;
import com.medusa.gruul.shops.api.model.RenovationTemplateDto;
import com.medusa.gruul.shops.mapper.ShopsRenovationTemMapper;
import com.medusa.gruul.shops.mapper.ShopsRenovationTemPageMapper;
import com.medusa.gruul.shops.model.param.ShopsRenovationAssemblyParam;
import com.medusa.gruul.shops.model.param.ShopsRenovationPageParam;
import com.medusa.gruul.shops.model.param.ShopsRenovationPluginParam;
import com.medusa.gruul.shops.model.param.ShopsRenovationTemplateParam;
import com.medusa.gruul.shops.model.vo.ShopsRenovationAssemblyVo;
import com.medusa.gruul.shops.model.vo.ShopsRenovationPageVo;
import com.medusa.gruul.shops.model.vo.ShopsRenovationTemplateVo;
import com.medusa.gruul.shops.properties.GlobalConstant;
import com.medusa.gruul.shops.properties.ShopsRenovationRedisTools;
import com.medusa.gruul.shops.service.ShopsRenovationPluginService;
import com.medusa.gruul.shops.service.ShopsRenovationTemPageAssService;
import com.medusa.gruul.shops.service.ShopsRenovationTemPageService;
import com.medusa.gruul.shops.service.ShopsRenovationTemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
@Service(value = "shopsRenovationTemServiceImpl")
public class ShopsRenovationTemServiceImpl extends ServiceImpl<ShopsRenovationTemMapper, ShopsRenovationTemplate> implements ShopsRenovationTemService {


    @Autowired
    private ShopsRenovationTemPageService shopsRenovationTemPageService;

    @Autowired
    private ShopsRenovationPluginService shopsRenovationPluginService;

    @Autowired
    private ShopsRenovationTemPageAssService shopsRenovationTemPageAssService;

    @Autowired
    ShopsRenovationTemPageMapper shopsRenovationTemPageMapper;


    /**
     * 新增商铺装修模板
     *
     * @param param
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addTemplate(ShopsRenovationTemplateParam param) {
        ShopsRenovationTemplate template = new ShopsRenovationTemplate();
        BeanUtil.copyProperties(param, template);
        if (param.getId() == null) {
            if (this.save(template)) {
                return Result.ok(template);
            }
            return Result.failed();
        }

        /** 下线之前的模板 */
        new LambdaUpdateChainWrapper<>(this.baseMapper)
                .eq(ShopsRenovationTemplate::getOnlineStatus, GlobalConstant.STRING_ONE)
                .eq(ShopsRenovationTemplate::getIsDevTemplate, GlobalConstant.STRING_ZERO)
                .set(ShopsRenovationTemplate::getOnlineStatus, GlobalConstant.STRING_ZERO)
                .update();

        /** 更新并上线当前模板 */
        if (this.updateById(template)) {

            ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
            redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_PAGE_PLUGIN);
            redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_KEY);

            return Result.ok(template);
        } else {
            log.error(String.format("update fail! %s ", template));
            throw new ServiceException("update fail!", SystemCode.DATA_UPDATE_FAILED_CODE);
        }
    }


    /**
     * 删除商铺装修模板 by ids
     *
     * @param ids
     * @return Result
     */
    @Override
    public Result delTemplate(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException(SystemCode.PARAM_MISS.getMsg());
        }
        Arrays.asList(ids.split(GlobalConstant.STRING_COMMA)).stream().forEach(id -> {
            boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
                    .eq(ShopsRenovationTemplate::getId, id)
                    .eq(ShopsRenovationTemplate::getOnlineStatus, GlobalConstant.STRING_ZERO)
                    .set(ShopsRenovationTemplate::getDeleted, GlobalConstant.STRING_ONE)
                    .update();
            if (!update) {
                throw new ServiceException("delete fail!", SystemCode.DATA_UPDATE_FAILED_CODE);
            }
        });

        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_PAGE_PLUGIN);
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_KEY);

        return Result.ok();
    }


    /**
     * 获取商铺装修模板list
     *
     * @param param
     * @return Result
     */
    @Override
    public Result listTemplate(ShopsRenovationTemplateParam param) {
        List list = this.baseMapper.listTemplate(param);
        if (ArrayUtil.isEmpty(list) && GlobalConstant.STRING_ONE.equals(param.getIsAll())) {
            list = this.baseMapper.listDefTemplate(param);
        }
        return Result.ok(list);
    }


    /**
     * 删除默认装修模板 by ids
     *
     * @param ids
     * @return Result
     */
    @Override
    @EscapeShop
    public Result delDefTemplate(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException(SystemCode.PARAM_MISS.getMsg());
        }
        Arrays.asList(ids.split(GlobalConstant.STRING_COMMA)).stream().forEach(id -> {
            boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
                    .eq(ShopsRenovationTemplate::getId, id)
                    .eq(ShopsRenovationTemplate::getOnlineStatus, GlobalConstant.STRING_ONE)
                    .eq(ShopsRenovationTemplate::getIsDevTemplate, GlobalConstant.STRING_ONE)
                    .set(ShopsRenovationTemplate::getDeleted, GlobalConstant.STRING_ONE)
                    .update();
            if (!update) {
                throw new ServiceException("delete del template fail!", SystemCode.DATA_UPDATE_FAILED_CODE);
            }
        });

        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_PAGE_PLUGIN);
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_KEY);
        return Result.ok();
    }


    /**
     * 获取默认装修模板list
     *
     * @param param
     * @return Result
     */
    @Override
    @EscapeTenant
    @EscapeShop
    public Result listDefTemplate(ShopsRenovationTemplateParam param) {
        return Result.ok(this.baseMapper.listDefTemplate(param));
    }


    /**
     * 复制模板 by id
     *
     * @param id
     * @return Result
     */
    @Override
    @EscapeShop
    @EscapeTenant
    @Transactional(rollbackFor = Exception.class)
    public Result copyTemplateById(Long id) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            throw new ServiceException("获取请求参数失败 !");
        }

        HttpServletRequest request = requestAttributes.getRequest();
        final String tenantId = request.getHeader(GlobalConstant.STRING_TENANT_ID);
        final String shopId = request.getHeader(GlobalConstant.STRING_SHOP_ID);

        if (StringUtils.isBlank(shopId) || StringUtils.isBlank(tenantId)) {
            throw new ServiceException("租户店铺信息获取失败!");
        }

        ShopsRenovationTemplate byId = this.getById(id);
        if (null == byId) {
            throw new ServiceException(String.format("template is empty ! id : %s", id));
        }

        /** 根据模板id copy 模板, 新生成数据 */
        ShopsRenovationTemplate shopsRenovationTemplate = byId.setId(null);
        byId.setShopId(shopId);
        byId.setTenantId(tenantId);
        byId.setIsDevTemplate(GlobalConstant.STRING_ZERO);
        byId.setOnlineStatus(GlobalConstant.STRING_ZERO);
        if (!this.save(byId)) {
            throw new ServiceException(String.format("copy template fail ! id : %s", id));
        }

        /** 根据模板id 获取关联控件, 执行复制保存控件至新模板 */
        List<ShopsRenovationPlugin> plugins =
                shopsRenovationPluginService.getBaseMapper()
                        .selectList(new QueryWrapper<ShopsRenovationPlugin>().eq("template_id", id));
        plugins.stream().forEach(entity -> {
            entity.setId(null);
            entity.setShopId(shopId);
            entity.setTenantId(tenantId);
            entity.setTemplateId(shopsRenovationTemplate.getId());
            if (!shopsRenovationPluginService.save(entity)) {
                throw new ServiceException(String.format("copy template plugins fail ! id : %s, plugin : %s", id, entity));
            }
        });

        /** 根据模板id 获取关联页面, 执行复制保存页面至新模板 */
        List<ShopsRenovationPage> pages = shopsRenovationTemPageService.getBaseMapper()
                .selectList(new QueryWrapper<ShopsRenovationPage>().eq("template_id", id));
        for (int i = 0; i < pages.size(); i++) {
            ShopsRenovationPage entity = pages.get(i);
            if (i == 0) {
                entity.setIsDef(GlobalConstant.STRING_ONE);
            } else {
                entity.setIsDef(GlobalConstant.STRING_ZERO);
            }
            Long pageId = entity.getId();
            entity.setId(null);
            entity.setModelId("0");
            entity.setShopId(shopId);
            entity.setTenantId(tenantId);
            entity.setTemplateId(shopsRenovationTemplate.getId());
            if (!shopsRenovationTemPageService.save(entity)) {
                throw new ServiceException(String.format("copy template page fail ! id : %s, page : %s", id, entity));
            }
            /** 根据页面id 获取关联插件, 执行复制保存插件至新页面 */
            List<ShopsRenovationAssembly> assemblies = shopsRenovationTemPageAssService.listTemplatePageAssemblyByPageId(pageId);
            assemblies.stream().forEach(assembliesEntity -> {
                assembliesEntity.setId(null);
                assembliesEntity.setShopId(shopId);
                assembliesEntity.setTenantId(tenantId);
                assembliesEntity.setPageId(entity.getId());
                if (!shopsRenovationTemPageAssService.save(assembliesEntity)) {
                    throw new ServiceException(
                            String.format("copy template page assemblies entity fail ! id : %s, assembliesEntity : %s", id, assembliesEntity));
                }
            });

        }

        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_PAGE_PLUGIN);
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_KEY);
        return Result.ok(shopsRenovationTemplate);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init(RenovationTemplateDto templateSettingDto) {
        ShopsRenovationTemplate template = new ShopsRenovationTemplate();
        BeanUtil.copyProperties(templateSettingDto, template, "shopId", "tenantId");
        if (StringUtils.isNotBlank(template.getIsDevTemplate())) {
            log.debug("isDevStatus in " + template.getIsDevTemplate());
            template.setIsDevTemplate(GlobalConstant.STRING_ZERO);
        }

        List<ShopsRenovationTemplate> list = this.getBaseMapper().selectList(new QueryWrapper<ShopsRenovationTemplate>().eq("name", template.getName()));
        log.debug(String.format("根据name : %s, 店铺id : %s, 租户id : %s ,获取历史数据 : %s",
                template.getName(), template.getShopId(), template.getTenantId(), list.toString()));
        if (!list.isEmpty()) {
            return;
        }

        this.save(template);
        Long indexPageId = 0L;
        int i = 0;
        String shopRenovationPageId = null;
        for (RenovationTemplateDto.Pages pageVo : templateSettingDto.getPages()) {
            ShopsRenovationPage page = new ShopsRenovationPage();
            BeanUtil.copyProperties(pageVo, page);
            page.setTemplateId(template.getId());
            shopsRenovationTemPageService.save(page);
            shopRenovationPageId = page.getId().toString();
            for (RenovationTemplateDto.Pages.Assemblies assemblyVo : pageVo.getAssemblies()) {
                ShopsRenovationAssembly assembly = new ShopsRenovationAssembly();
                assembly.setPageId(page.getId());
                assembly.setProperties(assemblyVo.getProperties());
                shopsRenovationTemPageAssService.save(assembly);
            }
            if (i == 0) {
                indexPageId = page.getId();
            }
            i++;
        }
        for (RenovationTemplateDto.Plugins pluginVO : templateSettingDto.getPlugins()) {
            ShopsRenovationPlugin plugin = new ShopsRenovationPlugin();
            if (pluginVO.getPluginProperties().contains("底部导航")) {
                pluginVO.setPluginProperties(pluginVO.getPluginProperties().replace("306", indexPageId.toString()));
                //471替换为t_shops_renovation_page 表的id
                pluginVO.getPluginProperties().replace("471", shopRenovationPageId == null ? "471" : shopRenovationPageId);
            }
            BeanUtil.copyProperties(pluginVO, plugin);
            plugin.setTemplateId(template.getId());
            shopsRenovationPluginService.save(plugin);
        }

    }


    /**
     * 小程序装修默认聚合接口
     *
     * @return Result
     */
    @Override
    public Result templateDefOne() {
        ShopsRenovationTemplateVo vo = null;
        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            throw new ServiceException("获取请求参数失败 !");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        final String shopId = request.getHeader(GlobalConstant.STRING_SHOP_ID);
        log.debug(String.format("shopId : %s", shopId));
        String json = redisTools.get(shopId + GlobalConstant.STRING_SHOP_TEMPLATE_KEY);

        log.debug(String.format("shopId : %s, jsonData : %s", shopId, json));
        if (StringUtils.isBlank(json)) {
            log.debug(String.format("shopId : %s, jsonData : %s, 租户id: %s", shopId, json, request.getHeader(GlobalConstant.STRING_TENANT_ID)));
            vo = innerHandlerGetCache();
            redisTools.set(shopId + GlobalConstant.STRING_SHOP_TEMPLATE_KEY, JSONObject.toJSONString(vo));
        } else {
            vo = JSONObject.parseObject(json, ShopsRenovationTemplateVo.class);
        }
        return Result.ok(vo);
    }


    private ShopsRenovationTemplateVo innerHandlerGetCache() {
        ShopsRenovationTemplateParam param = new ShopsRenovationTemplateParam();
        param.setOnlineStatus(GlobalConstant.STRING_ONE);
        param.setIsAll(GlobalConstant.STRING_ONE);
        List<ShopsRenovationTemplateVo> list = this.baseMapper.listTemplate(param);

        if (CollectionUtil.isEmpty(list)) {
            log.error("获取默认上线模板失败.");
        }

        ShopsRenovationTemplateVo vo = list.get(0);
        ShopsRenovationPluginParam pluginParam = new ShopsRenovationPluginParam();
        pluginParam.setTemplateId(vo.getId());
        Result<List> result = shopsRenovationPluginService.listPlugin(pluginParam);
        vo.setPlugins(result.getData());

        ShopsRenovationPageParam pageParam = new ShopsRenovationPageParam();
        pageParam.setTemplateId(vo.getId());
        Integer integer = shopsRenovationTemPageMapper.selectCount(new QueryWrapper<ShopsRenovationPage>().eq("template_id", vo.getId()));
        pageParam.setSize(integer);
        Result<PageUtils<ShopsRenovationPageVo>> listResult = shopsRenovationTemPageService.listTemplatePage(pageParam);
        PageUtils<ShopsRenovationPageVo> data1 = listResult.getData();
        List<ShopsRenovationPageVo> data = new ArrayList<>();
        if (null != data1 && CollectionUtil.isNotEmpty(data1.getList())) {
            data = data1.getList();
        }
        vo.setPages(data);

        data.parallelStream().forEach(entity -> {
            ShopsRenovationAssemblyParam assemblyParam = new ShopsRenovationAssemblyParam();
            assemblyParam.setPageId(entity.getId());
            Result<List<ShopsRenovationAssemblyVo>> assemblyVos = shopsRenovationTemPageAssService.listTemplatePageAssembly(assemblyParam);
            entity.setAssemblyVos(assemblyVos.getData());
        });

        return vo;
    }

}
//