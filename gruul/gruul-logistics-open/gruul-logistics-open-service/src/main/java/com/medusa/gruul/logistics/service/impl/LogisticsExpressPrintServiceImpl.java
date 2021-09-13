package com.medusa.gruul.logistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.logistics.api.entity.LogisticsExpress;
import com.medusa.gruul.logistics.api.entity.LogisticsExpressPrint;
import com.medusa.gruul.logistics.mapper.LogisticsExpressMapper;
import com.medusa.gruul.logistics.mapper.LogisticsExpressPrintMapper;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsExpressDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsExpressPrintDto;
import com.medusa.gruul.logistics.model.dto.manager.express.ExpressInfoDto;
import com.medusa.gruul.logistics.model.param.LogisticsExpressParam;
import com.medusa.gruul.logistics.model.param.LogisticsExpressPrintParam;
import com.medusa.gruul.logistics.model.vo.LogisticsAddressVo;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressPrintVo;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressVo;
import com.medusa.gruul.logistics.service.ILogisticsExpressPrintService;
import com.medusa.gruul.logistics.service.ILogisticsExpressService;
import com.medusa.gruul.logistics.util.express.kuaidihelp.KuaiDiHelp;
import com.medusa.gruul.logistics.util.express.sf.SFExpressUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Administrator
 */
@Service
@Slf4j
public class LogisticsExpressPrintServiceImpl implements ILogisticsExpressPrintService {

    @Autowired
    private LogisticsExpressPrintMapper logisticsExpressPrintMapper;

    /**
     * 电子面单打印设置列表查询
     * @param logisticsExpressPrintParam
     * @return IPage<LogisticsExpressPrintVo>
     */
    @Override
    public IPage<LogisticsExpressPrintVo> getExpressPrintList(LogisticsExpressPrintParam logisticsExpressPrintParam) {
        IPage<LogisticsExpressPrintVo> page = new Page<>(logisticsExpressPrintParam.getCurrent(), logisticsExpressPrintParam.getSize());
        List<LogisticsExpressPrintVo> logisticsExpressPrintVos = this.logisticsExpressPrintMapper.queryAllLogisticsExpressPrintList(page, logisticsExpressPrintParam);
        return page.setRecords(logisticsExpressPrintVos);
    }

    /**
     * 查询单条电子面单打印机信息
     * @param id
     * @return LogisticsExpressPrintVo
     */
    @Override
    public LogisticsExpressPrintVo getExpressPrintInfo(Long id) {
        return this.logisticsExpressPrintMapper.queryLogisticsExpressPrintInfo(id);
    }

    /**
     * 更新/新增 电子面单打印设置
     * @param logisticsExpressPrintDto
     */
    @Override
    public void setExpressPrintInfo(LogisticsExpressPrintDto logisticsExpressPrintDto) {
        if (logisticsExpressPrintDto.getId() == null) {
            //新增
            LogisticsExpressPrint logisticsExpressPrint = logisticsExpressPrintDto.coverBean();
            int insert = this.logisticsExpressPrintMapper.insert(logisticsExpressPrint);
            if(insert == 0){
                throw new ServiceException("电子面单打印机信息新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
        } else {
            //修改
            LogisticsExpressPrint logisticsExpressPrintSearch = this.logisticsExpressPrintMapper.selectById(logisticsExpressPrintDto.getId());
            if(BeanUtil.isEmpty(logisticsExpressPrintSearch)){
                throw new ServiceException("电子面单打印机信息已删除或不存在！", SystemCode.DATA_EXISTED.getCode());
            }
            LogisticsExpressPrint logisticsExpressPrint = logisticsExpressPrintDto.coverBean();
            int update = this.logisticsExpressPrintMapper.updateById(logisticsExpressPrint);
            if(update == 0){
                throw new ServiceException("电子面单打印机信息修改失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
            }
        }
    }

    /**
     * 启用/停用电子面单打印设置
     * @param type 0-停用 1-启用
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setExpressPrintStatus(Integer type, Long id) {
        boolean sign = new LambdaUpdateChainWrapper<>(logisticsExpressPrintMapper)
                .eq(LogisticsExpressPrint::getId, id)
                .set(LogisticsExpressPrint::getStatus, type).update();
        if (!sign) {
            throw new ServiceException("打印机信息设置失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
        }
    }

    /**
     * 删除电子面单打印设置
     * @param id 主键
     */
    @Override
    public void delExpressPrintInfo(Long id) {
        LogisticsExpressPrint logisticsExpressPrint = this.logisticsExpressPrintMapper.selectById(id);
        if(BeanUtil.isEmpty(logisticsExpressPrint)){
            throw new ServiceException("该信息不存在或已删除！", SystemCode.DATA_EXISTED.getCode());
        }else{
            int delete = this.logisticsExpressPrintMapper.deleteById(id);
            if(delete == 0){
                throw new ServiceException("电子面单打印机信息删除失败！", SystemCode.DATA_DELETE_FAILED.getCode());
            }
        }
    }
}
