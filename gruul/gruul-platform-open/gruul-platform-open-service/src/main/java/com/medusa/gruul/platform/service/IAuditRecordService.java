package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.platform.api.entity.AuditRecord;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetailMinis;
import com.medusa.gruul.platform.model.vo.AuditRecordVo;

import java.util.List;

/**
 * <p>
 * 小程序审核记录表 服务类
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
public interface IAuditRecordService extends IService<AuditRecord> {

    /**
     * 添加小程序上传审核记录
     *
     * @param miniInfo       mini
     * @param auditId        审核id
     * @param templateDetail 店铺模版详情表
     * @param detailMinis    店铺模版详情小程序版本子表
     */
    void addMiniAuditRecord(MiniInfo miniInfo, String auditId, PlatformShopTemplateDetail templateDetail, PlatformShopTemplateDetailMinis detailMinis);


    /**
     * 获取小程序指定的审核记录
     *
     * @param auditId 微信审核id非表id
     * @param appId   appid
     * @return com.medusa.gruul.platform.api.entity.AuditRecord
     */
    AuditRecord getByAuditIdAndAppId(Long auditId, String appId);

    /**
     * 获取指定小程序审核记录
     *
     * @param page  页数
     * @param size  条数
     * @param appId 小程序appId
     * @return com.medusa.gruul.platform.model.vo.AuditRecordVo
     */
    PageUtils<AuditRecordVo> getMiniAuditRecordList(Integer page, Integer size, String appId);

    /**
     * 根据审核表id和appId 获取审核记录
     *
     * @param id    表id
     * @param appId 小程序appId
     * @return com.medusa.gruul.platform.api.entity.AuditRecord
     */
    AuditRecord getByIdAndAppId(Long id, String appId);

    /**
     * 获取正在审核某个模板版本的审核记录
     *
     * @param id 版本id
     * @return com.medusa.gruul.platform.api.entity.AuditRecord
     */
    List<AuditRecord> getByAuditInAndVersionId(Long id);
}
