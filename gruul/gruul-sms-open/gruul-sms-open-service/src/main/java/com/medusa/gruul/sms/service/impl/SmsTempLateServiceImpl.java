package com.medusa.gruul.sms.service.impl;

import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.sms.AliYunSmsUtil;
import com.medusa.gruul.common.core.sms.SmsSendConfig;
import com.medusa.gruul.sms.constant.SmsEnum;
import com.medusa.gruul.sms.dao.entity.TSmsProviderEntity;
import com.medusa.gruul.sms.dao.entity.TSmsTemplateEntity;
import com.medusa.gruul.sms.dao.mapper.TSmsProviderEntityMapper;
import com.medusa.gruul.sms.dao.mapper.TSmsTemplateEntityMapper;
import com.medusa.gruul.sms.model.dto.TemplateDto;
import com.medusa.gruul.sms.service.SmsTempLateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Copyright(C) 2020-01-05 10:19 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2020-01-05 10:19
 **/
@Service
@Slf4j
public class SmsTempLateServiceImpl implements SmsTempLateService {


    @Resource
    private TSmsTemplateEntityMapper tSmsTemplateEntityMapper;
    @Resource
    private TSmsProviderEntityMapper tSmsProviderEntityMapper;

    @Override
    public int doAddTempLate(TemplateDto templateDto) {
        TSmsTemplateEntity tSmsTemplateEntity = new TSmsTemplateEntity();
        tSmsTemplateEntity.setCreateTime(new Date());
        tSmsTemplateEntity.setUpdateTime(new Date());
        BeanUtils.copyProperties(templateDto,tSmsTemplateEntity);
        return tSmsTemplateEntityMapper.insertSelective(tSmsTemplateEntity);
    }

    @Override
    public List<TSmsTemplateEntity> doListWaitVerifyTempLate(long status) {
        TSmsTemplateEntity tSmsTemplateEntity = new TSmsTemplateEntity();
        tSmsTemplateEntity.setIsDeleted(0L);
        tSmsTemplateEntity.setSmsTemplateIsPass(status);
        return tSmsTemplateEntityMapper.searchByEntity(tSmsTemplateEntity);
    }


    /**
     *
     * 腾讯短信没有api
     * */
    @Override
    public void doVerify(TSmsTemplateEntity tSmsTemplateEntity) {
        TSmsProviderEntity tSmsProviderEntity=null;
        try {
            tSmsProviderEntity = tSmsProviderEntityMapper.selectByPrimaryKey(tSmsTemplateEntity.getSmsProviderId());
            if(null == tSmsProviderEntity){
                throw new ServiceException(SmsEnum.SMS_PROVIDER_NOT_EXIST.getMsg());
            }

            if(SmsSendConfig.SMS_TYPE_ALI == tSmsTemplateEntity.getTemplateType()){
                AliYunSmsUtil.doVerifyTemplate(tSmsProviderEntity.getSmsProviderAppId(),tSmsProviderEntity.getSmsProviderAppSecret(),
                        String.valueOf(tSmsTemplateEntity.getSmsTemplateType()),tSmsTemplateEntity.getSmsTemplateName(),tSmsTemplateEntity.getSmsTemplateContent()
                        ,tSmsTemplateEntity.getSmsRemark());
            }else if(SmsSendConfig.SMS_TYPE_TX == tSmsTemplateEntity.getTemplateType()){
             //todo 腾讯暂无api
            }
        }catch (Exception e){
            log.error("verify ali template error :",e);
        }finally {
            tSmsTemplateEntity.setUpdateTime(new Date());
            tSmsTemplateEntity.setSmsTemplateIsPass(1L);
            tSmsTemplateEntityMapper.updateByPrimaryKeySelective(tSmsTemplateEntity);
        }

    }
}
