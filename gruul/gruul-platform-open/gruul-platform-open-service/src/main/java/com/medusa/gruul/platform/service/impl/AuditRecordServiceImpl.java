package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.constant.enums.AuditstatusEnum;
import com.medusa.gruul.common.core.constant.enums.CodeReleaseState;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.platform.api.entity.AuditRecord;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetailMinis;
import com.medusa.gruul.platform.mapper.AuditRecordMapper;
import com.medusa.gruul.platform.model.dto.AuditRecordDto;
import com.medusa.gruul.platform.model.vo.AuditRecordVo;
import com.medusa.gruul.platform.service.IAuditRecordService;
import com.medusa.gruul.platform.service.IMiniInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 小程序审核记录表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Service
public class AuditRecordServiceImpl extends ServiceImpl<AuditRecordMapper, AuditRecord> implements IAuditRecordService {

    @Autowired
    private IMiniInfoService miniInfoService;

    @Override
    public void addMiniAuditRecord(MiniInfo miniInfo, String auditId, PlatformShopTemplateDetail templateDetail,
                                   PlatformShopTemplateDetailMinis detailMinis) {
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setTemplateId(Long.valueOf(detailMinis.getCodeTempleteId()));
        auditRecord.setVersionId(templateDetail.getId());
        auditRecord.setAppId(miniInfo.getAppId());
        auditRecord.setAuditId(auditId);
        auditRecord.setAuditStatus(AuditstatusEnum.AUDIT.getStatus());
        auditRecord.setReleaseState(CodeReleaseState.NOT_RELEASE.getStatus());
        this.save(auditRecord);
        miniInfo.setAiditId(auditRecord.getId());
        miniInfoService.updateById(miniInfo);
        log.debug("小程序上传审核成功--->".concat(auditRecord.toString()));
    }

    @Override
    public AuditRecord getByAuditIdAndAppId(Long auditId, String appId) {
        return this.baseMapper.selectOne(new QueryWrapper<AuditRecord>().eq("app_id", appId).eq("audit_id", auditId));
    }

    @Override
    public PageUtils<AuditRecordVo> getMiniAuditRecordList(Integer page, Integer size, String appId) {
        IPage<AuditRecordDto> auditRecordIpage = this.baseMapper.selectMiniAuditRecordList(new Page<>(page, size), appId);
        // 封装返回对象
        PageUtils<AuditRecordVo> result = new PageUtils<>(null, (int) auditRecordIpage.getTotal(), (int) auditRecordIpage.getSize(), (int) auditRecordIpage.getCurrent());
        // 拷贝部分属性
        if (CollectionUtil.isEmpty(auditRecordIpage.getRecords())) {
            return result;
        }
        List<AuditRecordVo> auditRecordVos = auditRecordIpage.getRecords().stream().map(obj -> {
            AuditRecordVo vo = new AuditRecordVo();
            BeanUtils.copyProperties(obj, vo);
            return vo;
        }).collect(Collectors.toList());
        result.setList(auditRecordVos);
        return result;
    }

    @Override
    public AuditRecord getByIdAndAppId(Long id, String appId) {
        return this.baseMapper.selectOne(new QueryWrapper<AuditRecord>().eq("id", id).eq("app_id", appId));
    }

    @Override
    public List<AuditRecord> getByAuditInAndVersionId(Long id) {
        return this.baseMapper.selectList(new QueryWrapper<AuditRecord>().eq("version_id", id).eq("audit_status", CommonConstants.NUMBER_TWO));
    }
}
