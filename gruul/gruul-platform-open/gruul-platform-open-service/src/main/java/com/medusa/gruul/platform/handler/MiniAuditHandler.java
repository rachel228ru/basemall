package com.medusa.gruul.platform.handler;

import cn.binarywang.wx.miniapp.bean.code.WxMaCodeAuditStatus;
import com.medusa.gruul.common.core.constant.enums.AuditstatusEnum;
import com.medusa.gruul.common.core.constant.enums.RunFlagEnum;
import com.medusa.gruul.platform.api.entity.AuditRecord;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetailMinis;
import com.medusa.gruul.platform.service.IAuditRecordService;
import com.medusa.gruul.platform.service.IMiniInfoService;
import com.medusa.gruul.platform.service.IPlatformShopTemplateDetailMinisService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author whh
 * @date 2019/11/06
 */
@Component
public class MiniAuditHandler extends AbstractHandler {

    @Autowired
    private IAuditRecordService auditRecordService;
    @Autowired
    private IMiniInfoService miniInfoService;
    @Autowired
    private IPlatformShopTemplateDetailMinisService platformShopTemplateDetailMinisService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        MiniInfo miniInfo = miniInfoService.getByUserName(wxMessage.getToUser());
        WxMaCodeAuditStatus miniLastAuditCode = miniInfoService.getMiniLastAuditCode(miniInfo.getAppId());
        AuditRecord auditRecord = auditRecordService.getByAuditIdAndAppId(miniLastAuditCode.getAuditId(), miniInfo.getAppId());
        if (wxMessage.getEvent().equals(WxConsts.EventType.WEAPP_AUDIT_SUCCESS)) {
            auditRecord.setAuditStatus(AuditstatusEnum.PASS.getStatus());
            //审核成功审核中的版本设置为0
            miniInfo.setAiditId(0L);
            miniInfo.setRunFlag(RunFlagEnum.RUN.getStatus());
            miniInfo.setCurrentVersionId(auditRecord.getVersionId());
            miniInfo.setCurrentAiditId(auditRecord.getId());
            PlatformShopTemplateDetailMinis detailMinis = platformShopTemplateDetailMinisService.getByShopTemplateDetailIdAndCodeTemplateId(auditRecord.getVersionId(), auditRecord.getTemplateId());
            miniInfo.setTemplateDetailMinisId(detailMinis.getId());
            miniInfoService.saveOrUpdate(miniInfo);
            miniInfoService.codeRelease(miniInfo.getAppId());
            miniInfoService.updateMiniNewConfig(auditRecord.getVersionId(), miniInfo.getTenantId());
        }
        if (wxMessage.getEvent().equals(WxConsts.EventType.WEAPP_AUDIT_FAIL)) {
            auditRecord.setAuditStatus(AuditstatusEnum.REFUSE.getStatus());
            auditRecord.setReason(wxMessage.getReason());
        }
        auditRecordService.saveOrUpdate(auditRecord);
        return null;
    }
}
