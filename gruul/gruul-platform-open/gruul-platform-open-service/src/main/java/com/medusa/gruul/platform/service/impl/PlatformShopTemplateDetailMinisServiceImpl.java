package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetailMinis;
import com.medusa.gruul.platform.mapper.PlatformShopTemplateDetailMinisMapper;
import com.medusa.gruul.platform.model.dto.MiniCodeVersionDto;
import com.medusa.gruul.platform.service.IMiniInfoService;
import com.medusa.gruul.platform.service.IPlatformShopTemplateDetailMinisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺模版详情小程序版本子表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-10-14
 */
@Service
public class PlatformShopTemplateDetailMinisServiceImpl extends ServiceImpl<PlatformShopTemplateDetailMinisMapper, PlatformShopTemplateDetailMinis> implements IPlatformShopTemplateDetailMinisService {

    @Autowired
    private IMiniInfoService miniInfoService;

    @Override
    public void save(List<MiniCodeVersionDto> miniCodeVersions, Long shopTemplateDetailId) {
        List<PlatformShopTemplateDetailMinis> templateDetailMinis = miniCodeVersions.stream().map(obj -> {
            PlatformShopTemplateDetailMinis detailMinis = BeanUtil.toBean(obj, PlatformShopTemplateDetailMinis.class);
            detailMinis.setShopTemplateDetailId(shopTemplateDetailId);
            return detailMinis;
        }).collect(Collectors.toList());
        this.saveBatch(templateDetailMinis);
    }

    @Override
    public void update(List<MiniCodeVersionDto> miniCodeVersions, Long shopTemplateDetailId) {
        List<MiniCodeVersionDto> newAdd = new LinkedList<>();
        List<MiniCodeVersionDto> old = new LinkedList<>();
        //对比老数据是否存在被删除的，有删除的需要判断当前是否有某个小程序使用当前版本
        List<PlatformShopTemplateDetailMinis> detailIdVersions = this.getByShopTemplateDetailId(shopTemplateDetailId);
        Map<Long, List<PlatformShopTemplateDetailMinis>> deatailMap = detailIdVersions.stream().collect(Collectors.groupingBy(PlatformShopTemplateDetailMinis::getId));
        for (MiniCodeVersionDto miniCodeVersion : miniCodeVersions) {
            //不存在id的为新数据
            if (miniCodeVersion.getId() == null || miniCodeVersion.getId() == 0) {
                newAdd.add(miniCodeVersion);
            } else {
                old.add(miniCodeVersion);
                deatailMap.remove(miniCodeVersion.getId());
            }
        }
        if (CollectionUtil.isNotEmpty(deatailMap)) {
            for (Map.Entry<Long, List<PlatformShopTemplateDetailMinis>> entry : deatailMap.entrySet()) {
                Long id = entry.getKey();
                List<MiniInfo> miniInfos = miniInfoService.getByTemplateDetailMinisId(id);
                if (CollectionUtil.isNotEmpty(miniInfos)) {
                    throw new RuntimeException("无法删除".concat(entry.getValue().get(0).getCodeTempleteVersion()).concat("版本，该版本存在使用中的小程序"));
                }
                this.baseMapper.deleteById(id);
            }
        }
        if (CollectionUtil.isNotEmpty(newAdd)) {
            this.save(newAdd, shopTemplateDetailId);
        }
        if (CollectionUtil.isNotEmpty(old)) {
            List<PlatformShopTemplateDetailMinis> update = old.stream().map(obj ->
                    BeanUtil.toBean(obj, PlatformShopTemplateDetailMinis.class)).collect(Collectors.toList());
            this.saveOrUpdateBatch(update);
        }
    }

    @Override
    public List<PlatformShopTemplateDetailMinis> getByShopTemplateDetailId(Long shopTemplateDetailId) {
        return this.baseMapper.selectList(new QueryWrapper<PlatformShopTemplateDetailMinis>().eq("shop_template_detail_id", shopTemplateDetailId));

    }

    @Override
    public PlatformShopTemplateDetailMinis getByShopTemplateDetailIdAndCodeTemplateId(Long shopTemplateDetailId, Long templateId) {
        return this.baseMapper.selectOne(new QueryWrapper<PlatformShopTemplateDetailMinis>()
                .eq("shop_template_detail_id", shopTemplateDetailId).eq("code_templete_id", templateId));
    }

}
