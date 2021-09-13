package com.medusa.gruul.sms.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.medusa.gruul.sms.constant.SmsEnum;
import com.medusa.gruul.sms.dao.entity.TSmsOrderEntity;
import com.medusa.gruul.sms.dao.entity.TSmsProviderEntity;
import com.medusa.gruul.sms.dao.entity.TSmsSignEntity;
import com.medusa.gruul.sms.dao.entity.TSmsTemplateEntity;
import com.medusa.gruul.sms.dao.mapper.*;
import com.medusa.gruul.sms.model.dto.SendSmsDto;
import com.medusa.gruul.sms.service.SmsOrderService;
import com.medusa.gruul.common.core.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Copyright(C) 2019-12-22 17:33 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2019-12-22 17:33
 **/
@Service
public class SmsOrderServiceImpl implements SmsOrderService {

    @Resource
    private TSmsOrderEntityMapper tSmsOrderEntityMapper;
    @Resource
    private TSmsProviderEntityMapper tSmsProviderEntityMapper;
    @Resource
    private TSmsTemplateEntityMapper tSmsTemplateEntityMapper;
    @Resource
    private TSmsSignEntityMapper tSmsSignEntityMapper;



    /***
    * @description:
    * @param:userAuthDto
    * @return: com.medusa.gruul.auth.model.vo.UserAuthVo
    * @throws:
    * @author: wangpeng
    * @version : v1.0
    * @date: 2019/12/29 10:56 AM
    */
    @Override
    public int doCreateOrder(SendSmsDto sendSmsDto) {


        TSmsTemplateEntity tSmsTemplateEntity = new TSmsTemplateEntity();
        tSmsTemplateEntity.setUserId(sendSmsDto.getUserId());
        tSmsTemplateEntity.setId(sendSmsDto.getTemplateId());
        tSmsTemplateEntity.setIsDeleted(0L);
        tSmsTemplateEntity.setSmsTemplateIsPass(2L);
        List<TSmsTemplateEntity> tSmsTemplateEntities = tSmsTemplateEntityMapper.searchByEntity(tSmsTemplateEntity);
        tSmsTemplateEntity.clear();
        if(CollectionUtil.isEmpty(tSmsTemplateEntities) || 1 <  tSmsTemplateEntities.size()){

            throw new ServiceException(SmsEnum.SMS_TEMPLATE_NOT_EXIST.getMsg());
        }
        TSmsProviderEntity tSmsProviderEntity = new TSmsProviderEntity();
        tSmsProviderEntity.setIsDeleted(0L);
        tSmsProviderEntity.setId(sendSmsDto.getProviderId());
        tSmsProviderEntity.setUserId(sendSmsDto.getUserId());
        List<TSmsProviderEntity> tSmsProviderEntities = tSmsProviderEntityMapper.searchByEntity(tSmsProviderEntity);
        tSmsProviderEntity.clear();
        if(CollectionUtil.isEmpty(tSmsProviderEntities) || 1 <  tSmsProviderEntities.size()){

            throw new ServiceException(SmsEnum.SMS_PROVIDER_NOT_EXIST.getMsg());
        }

        TSmsSignEntity tSmsSignEntity = new TSmsSignEntity();
        tSmsSignEntity.setId(sendSmsDto.getSignId());
        tSmsSignEntity.setSmsSignIsPass(1L);
        tSmsSignEntity.setIsDeleted(0L);
        tSmsSignEntity.setUserId(sendSmsDto.getUserId());
        List<TSmsSignEntity> tSmsSignEntities = tSmsSignEntityMapper.searchByEntity(tSmsSignEntity);
        if(CollectionUtil.isEmpty(tSmsSignEntities) || 1 <  tSmsSignEntities.size()){
            throw new ServiceException(SmsEnum.SMS_SIGN_NOT_EXIST.getMsg());
        }
        TSmsOrderEntity tSmsOrderEntity = new TSmsOrderEntity();
        BeanUtils.copyProperties(sendSmsDto,tSmsOrderEntity);
        tSmsOrderEntity.setSmsSendStatus(0);
        tSmsOrderEntity.setSmsSendType(sendSmsDto.getSmsType());
        tSmsOrderEntity.setCreateTime(new Date());
        return  tSmsOrderEntityMapper.insertSelective(tSmsOrderEntity);
    }

    @Override
    public List<TSmsOrderEntity> doListWaitSendOrder(int smsSendStatus) {
        return  tSmsOrderEntityMapper.doListWaitSendOrder(smsSendStatus);
    }


}
