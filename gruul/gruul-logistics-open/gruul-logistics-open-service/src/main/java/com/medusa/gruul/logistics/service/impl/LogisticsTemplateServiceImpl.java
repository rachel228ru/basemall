package com.medusa.gruul.logistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.BigDecimalUtil;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.goods.api.feign.RemoteGoodsService;
import com.medusa.gruul.logistics.api.entity.LogisticsIncludePostage;
import com.medusa.gruul.logistics.api.entity.LogisticsShippingModel;
import com.medusa.gruul.logistics.api.entity.LogisticsTemplate;
import com.medusa.gruul.logistics.mapper.LogisticsIncludePostageMapper;
import com.medusa.gruul.logistics.mapper.LogisticsShippingModelMapper;
import com.medusa.gruul.logistics.mapper.LogisticsTemplateMapper;
import com.medusa.gruul.logistics.model.dto.manager.*;
import com.medusa.gruul.logistics.model.param.LogisticsTemplateParam;
import com.medusa.gruul.logistics.model.vo.LogisticsTemplateVo;
import com.medusa.gruul.logistics.service.ILogisticsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 物流模版设置业务层
 * @author lcysike
 */
@Service
@Slf4j
public class LogisticsTemplateServiceImpl extends ServiceImpl<LogisticsTemplateMapper, LogisticsTemplate> implements ILogisticsTemplateService {

    @Autowired
    private LogisticsTemplateMapper logisticsTemplateMapper;

    @Autowired
    private LogisticsShippingModelMapper logisticsShippingModelMapper;

    @Autowired
    private LogisticsIncludePostageMapper logisticsIncludePostageMapper;

    @Resource
    private RemoteGoodsService remoteGoodsService;


    /**
     * 获取模板列表
     *
     * @see ILogisticsTemplateService#getTemplateList(LogisticsTemplateParam)
     */
    @Override
    public IPage<LogisticsTemplateVo> getTemplateList(LogisticsTemplateParam logisticsTemplateParam) {
        IPage<LogisticsTemplateVo> page = new Page<>(logisticsTemplateParam.getCurrent(),
                logisticsTemplateParam.getSize());
        List<LogisticsTemplateVo> logisticsTemplateVos = this.logisticsTemplateMapper.queryTemplateList(page,
                logisticsTemplateParam);
        return page.setRecords(logisticsTemplateVos);
    }

    /**
     * 获取模板详情
     *
     * @see ILogisticsTemplateService#getTemplateInfo(Long)
     */
    @Override
    public LogisticsTemplateVo getTemplateInfo(Long id) {
        return this.logisticsTemplateMapper.selectByPrimaryKey(id);
    }


    /**
     * @see ILogisticsTemplateService#addOrUpdateTemplate(LogisticsTemplateDto)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateTemplate(LogisticsTemplateDto logisticsTemplateDto) {
        if (logisticsTemplateDto.getId() == null) {
            //模版新增
            LogisticsTemplate logisticsTemplate = logisticsTemplateDto.coverBean();
            int insert = this.logisticsTemplateMapper.insert(logisticsTemplate);
            if (insert == 0) {
                throw new ServiceException("模版新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
            //模版首费方式新增
            List<LogisticsShippingModelDto> logisticsShippingModelDtos =
                    logisticsTemplateDto.getLogisticsShippingModelDtos();
            if (CollectionUtil.isNotEmpty(logisticsShippingModelDtos)) {
                addLogisticsShippingModel(logisticsShippingModelDtos, logisticsTemplate.getId());
            }
            //模版续费方式新增 先判断是否勾选指定包邮条件
            if (CommonConstants.NUMBER_ONE.equals(logisticsTemplateDto.getChoiceConditionPostage())) {
                List<LogisticsIncludePostageDto> logisticsIncludePostageDtos =
                        logisticsTemplateDto.getLogisticsIncludePostageDtos();
                if (CollectionUtil.isNotEmpty(logisticsIncludePostageDtos)) {
                    addLogisticsIncludePostage(logisticsIncludePostageDtos, logisticsTemplate.getId());
                }
            }
        } else {
            //update
            LogisticsTemplate logisticsTemplate = this.logisticsTemplateMapper.selectById(logisticsTemplateDto.getId());
            if (BeanUtil.isEmpty(logisticsTemplate)) {
                throw new ServiceException("模版不存在！", SystemCode.DATA_EXISTED.getCode());
            }
            BeanUtil.copyProperties(logisticsTemplateDto, logisticsTemplate);
            int update = this.logisticsTemplateMapper.updateById(logisticsTemplate);
            if (update == 0) {
                throw new ServiceException("模版修改失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
            }
            //模版首费方式修改 先删除原先首费方式
            this.logisticsShippingModelMapper.delete(new QueryWrapper<LogisticsShippingModel>().eq("logistics_id",
                    logisticsTemplateDto.getId()));
            List<LogisticsShippingModelDto> logisticsShippingModelDtos =
                    logisticsTemplateDto.getLogisticsShippingModelDtos();
            if (CollectionUtil.isNotEmpty(logisticsShippingModelDtos)) {
                addLogisticsShippingModel(logisticsShippingModelDtos, logisticsTemplate.getId());
            }
            //模版续费方式修改 先删除原先续费方式 先判断是否勾选指定包邮条件
            this.logisticsIncludePostageMapper.delete(new QueryWrapper<LogisticsIncludePostage>().eq("logistics_id",
                    logisticsTemplateDto.getId()));
            if (CommonConstants.NUMBER_ONE.equals(logisticsTemplateDto.getChoiceConditionPostage())) {
                List<LogisticsIncludePostageDto> logisticsIncludePostageDtos =
                        logisticsTemplateDto.getLogisticsIncludePostageDtos();
                if (CollectionUtil.isNotEmpty(logisticsIncludePostageDtos)) {
                    addLogisticsIncludePostage(logisticsIncludePostageDtos, logisticsTemplate.getId());
                }
            }
        }
    }

    /**
     * 新增首费方式公共方法
     *
     * @param logisticsShippingModelDtos
     * @param id
     */
    public void addLogisticsShippingModel(List<LogisticsShippingModelDto> logisticsShippingModelDtos, Long id) {
        logisticsShippingModelDtos.stream().forEach(bean -> {
            bean.setLogisticsId(id);
            LogisticsShippingModel logisticsShippingModel = bean.coverBean();
            int insertLogisticsShippingModel = this.logisticsShippingModelMapper.insert(logisticsShippingModel);
            if (insertLogisticsShippingModel == 0) {
                throw new ServiceException("模版首费方式新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
        });
    }

    /**
     * 新增续费方式公共方法
     *
     * @param logisticsIncludePostageDtos
     * @param id
     */
    public void addLogisticsIncludePostage(List<LogisticsIncludePostageDto> logisticsIncludePostageDtos, Long id) {
        logisticsIncludePostageDtos.stream().forEach(bean -> {
            bean.setLogisticsId(id);
            LogisticsIncludePostage logisticsIncludePostage = bean.coverBean();
            int insertLogisticsIncludePostage = this.logisticsIncludePostageMapper.insert(logisticsIncludePostage);
            if (insertLogisticsIncludePostage == 0) {
                throw new ServiceException("模版续费方式新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
        });
    }

    /**
     * @see ILogisticsTemplateService#removeTemplateById(Long)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTemplateById(Long id) {
        LogisticsTemplate logisticsTemplate = this.logisticsTemplateMapper.selectById(id);
        if (BeanUtil.isEmpty(logisticsTemplate)) {
            throw new ServiceException("模版不存在！", SystemCode.DATA_EXISTED.getCode());
        }
        if (remoteGoodsService.checkProductByTemplateId(id)) {
            throw new ServiceException("有商品正在使用该模版，无法删除！", SystemCode.DATA_ADD_FAILED.getCode());
        }
        this.logisticsShippingModelMapper.delete(new QueryWrapper<LogisticsShippingModel>().eq("logistics_id", id));
        this.logisticsIncludePostageMapper.delete(new QueryWrapper<LogisticsIncludePostage>().eq("logistics_id", id));
        this.logisticsTemplateMapper.deleteById(id);
    }

    /**
     * 公共接口获取运费模板列表
     *
     * @see ILogisticsTemplateService#feignGetList()
     */
    @Override
    public List<LogisticsTemplateVo> feignGetList() {
        List<LogisticsTemplate> logisticsTemplates = this.logisticsTemplateMapper.selectList(new QueryWrapper<>());
        List<LogisticsTemplateVo> logisticsTemplateVos = new ArrayList<>(logisticsTemplates.size());
        logisticsTemplates.stream().forEach(bean -> {
            LogisticsTemplateVo logisticsTemplateVo = new LogisticsTemplateVo();
            logisticsTemplateVo.setId(bean.getId());
            logisticsTemplateVo.setName(bean.getName());
            logisticsTemplateVos.add(logisticsTemplateVo);
        });
        return logisticsTemplateVos;
    }

    /**
     * 公共接口根据id获取运费模板详情
     *
     * @see ILogisticsTemplateService#feignGetInfo(Long)
     */
    @Override
    public LogisticsTemplateVo feignGetInfo(Long id) {
        return this.logisticsTemplateMapper.selectByPrimaryKey(id);
    }

    /**
     * 计算运费
     * @param logisticsFreightDtos
     * @param region 运送地区 json 格式 {"provinceid": ["cityId","cityId2"]}
     * @return: java.math.BigDecimal
     * @author: wangpeng
     * @version : v1.0
     * @date: 2020/2/29 11:12 上午
     */
    @Override
    public BigDecimal freightCalculation(List<LogisticsFreightDto> logisticsFreightDtos, String region) {
        BigDecimal res = new BigDecimal(0);
        region = region.substring(0, 4);
        Map<Long, List<LogisticsFreightDto>> collectByTemplateId =
                logisticsFreightDtos.stream().collect(Collectors.groupingBy(LogisticsFreightDto::getFreightTemplateId));
        Map<Long, LogisticsAggregationDto> aggregation = getAggregation(collectByTemplateId);
        //只有一个模版
        if (1 == collectByTemplateId.size()) {
            BigDecimal oneTemplateAggregation = getOneTemplateAggregation(collectByTemplateId, aggregation, region);
            return res.add(oneTemplateAggregation);
        }
        if (1 < collectByTemplateId.size()) {
            //获取首重或者首件中最大的值和模版ID
            Map<Long, LogisticsShippingModel> moreTemplateAggregation =
                    getMoreTemplateAggregation(collectByTemplateId, aggregation, region);

            return getRes(moreTemplateAggregation, aggregation, region);

        }
        return null;
    }


    /**
     * 计算出最终运费
     */
    private BigDecimal getRes(Map<Long, LogisticsShippingModel> moreTemplateAggregation, Map<Long,
            LogisticsAggregationDto> aggregation, String region) {
        //按类型分组,并取出最大首重的模版信息
        final List<LogisticsShippingModel> numTemplate = new LinkedList<>();
        final List<LogisticsShippingModel> weightTemplate = new LinkedList<>();
        //总件数
        final List<Integer> numSum = new LinkedList<>();
        //总重量
        final List<BigDecimal> weightSum = new LinkedList<>();

        //先根据首重/件运费排序
        moreTemplateAggregation = moreTemplateAggregation.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().getFirstAmount().compareTo(e1.getValue().getFirstAmount()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        moreTemplateAggregation.forEach((k, v) -> {
            LogisticsAggregationDto logisticsAggregationDto = aggregation.get(k);
            log.info(logisticsAggregationDto.toString());
            if (1 == v.getValuationModel()) {
                numTemplate.add(v);
                numSum.add(logisticsAggregationDto.getNumSum());
            }
            if (2 == v.getValuationModel()) {
                weightTemplate.add(v);
                weightSum.add(logisticsAggregationDto.getWeightSum());
            }
        });

        return getFinalRes(numTemplate, numSum, weightTemplate, weightSum);

    }


    /**
     * 最终计算运费
     */
    private BigDecimal getFinalRes(List<LogisticsShippingModel> numTemplate, List<Integer> numSum
            , List<LogisticsShippingModel> weightTemplate, List<BigDecimal> weightSum) {
        BigDecimal all = new BigDecimal(0);
        if (CollUtil.isNotEmpty(numTemplate) && CollUtil.isNotEmpty(weightTemplate)) {
            //件数首费大于重量首费
            if (CollUtil.getFirst(numTemplate).getFirstAmount().compareTo(CollUtil.getFirst(weightTemplate).getFirstAmount()) > 0) {
                BigDecimal allNumAmt = getAllNumAmt(numTemplate, numSum);
                all = all.add(allNumAmt);
                //加上重量模版的件数
                //剩余重量
                for (int i = 0; i < weightTemplate.size(); i++) {
                    LogisticsShippingModel model = weightTemplate.get(i);
                    BigDecimal sum = weightSum.get(i);
                    BigDecimal calculation = getCalculation(sum,
                            model.getSecondWeight(), model.getSecondAmount());
                    all = all.add(calculation);
                }
                return all;
            } else {
                BigDecimal allWeightAmt = getAllWeightAmt(weightTemplate, weightSum);
                all = all.add(allWeightAmt);
                //剩余件数价格
                for (int i = 0; i < numTemplate.size(); i++) {
                    LogisticsShippingModel model = numTemplate.get(i);
                    Integer sum = numSum.get(i);
                    //剩余件数价格
                    BigDecimal calculation =getCalculation(BigDecimal.valueOf(sum),
                            BigDecimal.valueOf(model.getSecondPiece()), model.getSecondAmount());
                    all = all.add(calculation);
                }

                return all;
            }
        }
        //只有按件数计费的模版
        if (CollUtil.isNotEmpty(numTemplate)) {
            return getAllNumAmt(numTemplate, numSum);
        }
        //只有按重量计费的模版
        if (CollUtil.isNotEmpty(weightTemplate)) {
            return getAllWeightAmt(weightTemplate, weightSum);
        }
        return all;
    }


    /**
     * 按重量计费运算
     */
    private BigDecimal getAllWeightAmt(List<LogisticsShippingModel> weightTemplate, List<BigDecimal> weightSum) {
        BigDecimal all = new BigDecimal(0);
        for (int i = 0; i < weightTemplate.size(); i++) {
            LogisticsShippingModel model = weightTemplate.get(i);
            BigDecimal sum = weightSum.get(i);
            if (i == 0) {
                //剩余重量
                BigDecimal sub = NumberUtil.sub(sum,model.getFirstWeight());
                all=all.add(model.getFirstAmount());

                if(NumberUtil.isGreater(sub,BigDecimal.ZERO)){
                    BigDecimal calculation = getCalculation(sub,
                            model.getSecondWeight(), model.getSecondAmount());
                    all = all.add(calculation);
                }

            } else {
                BigDecimal calculation = getCalculation(sum,
                        model.getSecondWeight(), model.getSecondAmount());
                all = all.add(calculation);
            }
        }
        return all;
    }


    /**
     * 按件数计费运算
     */
    private BigDecimal getAllNumAmt(List<LogisticsShippingModel> numTemplate, List<Integer> numSum) {
        BigDecimal all = new BigDecimal(0);
        for (int i = 0; i < numTemplate.size(); i++) {
            LogisticsShippingModel model = numTemplate.get(i);
            Integer sum = numSum.get(i);
            if (i == 0) {

                int surplus = sum - model.getFirstPiece();
                all=all.add(model.getFirstAmount());
                if(surplus>0){
                    //剩余件数价格
                    BigDecimal calculation = getCalculation(BigDecimal.valueOf(surplus),
                            BigDecimal.valueOf(model.getSecondPiece()), model.getSecondAmount());
                    all = all.add(calculation);
                }

            } else {
                //剩余件数价格
                BigDecimal calculation = getCalculation(BigDecimal.valueOf(sum),
                        BigDecimal.valueOf(model.getSecondPiece()), model.getSecondAmount());
                all = all.add(calculation);
            }
        }
        return all;
    }

    /**
     * 取所有的模版和对应的首费
     */
    private Map<Long, LogisticsShippingModel> getMoreTemplateAggregation(Map<Long, List<LogisticsFreightDto>> collectByTemplateId,
                                                                         Map<Long, LogisticsAggregationDto> aggregation, String region) {
        Map<Long, LogisticsShippingModel> map = new HashMap<>(collectByTemplateId.size());
        collectByTemplateId.forEach((k, v) ->
                {
                    LogisticsAggregationDto logisticsAggregationDto = aggregation.get(k);
                    LogisticsTemplate logisticsTemplate = logisticsTemplateMapper.selectById(k);
                    Map<String, Object> param = new HashMap<>(CommonConstants.NUMBER_TWO);
                    param.put("logistics_id", k);
                    param.put("region_json", region);
                    List<LogisticsShippingModel> logisticsShippingModels =
                            logisticsShippingModelMapper.selectByParamMap(param);
                    //如果当前模板开启包邮，指定包邮条件，取出最大首重/首件价格
                    if (1 == logisticsTemplate.getChoiceConditionPostage()) {
                        List<LogisticsIncludePostage> logisticsIncludePostages =
                                logisticsIncludePostageMapper.selectList(
                                        new QueryWrapper<LogisticsIncludePostage>().lambda()
                                                .eq(LogisticsIncludePostage::getLogisticsId, k)
                                                .like(LogisticsIncludePostage::getRegion, region));
                        if (1 == logisticsIncludePostages.size()) {
                            //判断判断是否达到模版包邮标准，如果没有达到，取出最大的首件/首重金额 如果达到标准运费为0
                            //包邮条件类型: 0= 按件数包邮,1=按重量包邮,2=按金额包邮,3=件数+金额 4=重量+金额 包邮
                            Integer type = logisticsIncludePostages.get(0).getType();
                            if (0 == type) {
                                //没有达到指定件数包邮
                                if (!(logisticsIncludePostages.get(0).getPieceNum() <= logisticsAggregationDto.getNumSum())) {
                                    map.put(k, logisticsShippingModels.get(0));
                                } else {
                                    aggregation.remove(k);
                                }
                            } else if (1 == type) {
                                //没有到达指定包邮重量
                                if (!((logisticsIncludePostages.get(0).getWeight().compareTo(logisticsAggregationDto.getWeightSum())) <= 0)) {
                                    map.put(k, logisticsShippingModels.get(0));
                                } else {
                                    aggregation.remove(k);
                                }
                            } else if (2 == type) {
                                //没有到达指定包邮金额
                                if (!((logisticsIncludePostages.get(0).getAmountNum().compareTo(logisticsAggregationDto.getPriceSum())) <= 0)) {
                                    map.put(k, logisticsShippingModels.get(0));
                                } else {
                                    aggregation.remove(k);
                                }
                            } else if (3 == type) {
                                //没有达到指定件数➕金额
                                if (!((logisticsIncludePostages.get(0).getPieceNum() <= logisticsAggregationDto.getNumSum())
                                        && (logisticsIncludePostages.get(0).getAmountNum().compareTo(logisticsAggregationDto.getPriceSum()) <= 0))) {
                                    map.put(k, logisticsShippingModels.get(0));
                                } else {
                                    aggregation.remove(k);
                                }
                            } else if (4 == type) {
                                //没有达到指定重量➕金额
                                if (!((logisticsIncludePostages.get(0).getWeight().compareTo(logisticsAggregationDto.getWeightSum())) <= 0
                                        && (logisticsIncludePostages.get(0).getAmountNum().compareTo(logisticsAggregationDto.getPriceSum()) <= 0))) {
                                    map.put(k, logisticsShippingModels.get(0));
                                } else {
                                    aggregation.remove(k);
                                }
                            } else {
                                //不在包邮条件之内
                                map.put(k, logisticsShippingModels.get(0));
                            }
                        } else {
                            //不在包邮区域
                            map.put(k, logisticsShippingModels.get(0));
                        }
                    }//不包邮返回基础运算价格
                    else {
                        map.put(k, logisticsShippingModels.get(0));
                    }
                }
        );
        return map;
    }


    /**
     * 运算方式：（剩余件数/（续件或者续重））*续费 + 首件，如果有余数就加上续费*1
     */
    private BigDecimal getCalculation(BigDecimal secondPiece, BigDecimal continuation, BigDecimal secondAmount) {
        //剩余件数/续件 向上取整
        BigDecimal number = BigDecimal.ZERO;
        if(continuation.compareTo(BigDecimal.ZERO) != 0){
            number = NumberUtil.div(secondPiece, continuation, 0, RoundingMode.UP);
        }
        return NumberUtil.mul(number, secondAmount);
    }


    private BigDecimal getOneTemplateAggregation(Map<Long, List<LogisticsFreightDto>> collectByTemplateId, Map<Long
            , LogisticsAggregationDto> aggregation, String region) {
        BigDecimal res = new BigDecimal(0);
        Long templateId = 0L;
        for (Long key : collectByTemplateId.keySet()) {
            templateId = key;
        }
        //如果运费模版id为0 说明是商家包邮  只有这一个模版的话运费返回0
        if(templateId == 0){
            return res;
        }
        LogisticsTemplate logisticsTemplate = logisticsTemplateMapper.selectById(templateId);
        //是否包邮，如果包邮检查件数或者重量是否满足要求
        if (1 == logisticsTemplate.getChoiceConditionPostage()) {
            LogisticsAggregationDto logisticsAggregationDto = aggregation.get(templateId);
            //指定包邮条件
            if (1 == logisticsTemplate.getChoiceConditionPostage()) {
                Map<String, Object> param = new HashMap<>(CommonConstants.NUMBER_TWO);
                param.put("logistics_id", templateId);
                param.put("region_json", region);
                List<LogisticsIncludePostage> logisticsIncludePostages =
                        logisticsIncludePostageMapper.selectByParams(param);
                //签收地区不在设置的指定包邮条件的地区,按基础计费方式计算
                if (1 != logisticsIncludePostages.size()) {
                    BigDecimal bigDecimal = baseAggregation(templateId, region, aggregation);
                    res = res.add(bigDecimal);
                    return res;
                }
                //包邮条件类型: 0= 按件数包邮,1=按重量包邮,2=按金额包邮,3=件数+金额 4=重量+金额 包邮
                Integer type = logisticsIncludePostages.get(0).getType();
                if (0 == type) {
                    //达到指定件数包邮
                    if (logisticsIncludePostages.get(0).getPieceNum() <= logisticsAggregationDto.getNumSum()) {
                        res = res.add(new BigDecimal(0));
                    }
                    //按基础计费计费计算
                    else {
                        BigDecimal bigDecimal = baseAggregation(templateId, region, aggregation);
                        res = res.add(bigDecimal);
                    }
                } else if (1 == type) {
                    //达到指定重量包邮
                    if ((logisticsIncludePostages.get(0).getWeight().compareTo(logisticsAggregationDto.getWeightSum())) <= 0) {
                        res = res.add(new BigDecimal(0));
                    } else {
                        //按基础计费计费计算
                        BigDecimal bigDecimal = baseAggregation(templateId, region, aggregation);
                        res = res.add(bigDecimal);
                    }
                } else if (2 == type) {
                    //达到指定金额包邮
                    if ((logisticsIncludePostages.get(0).getAmountNum().compareTo(logisticsAggregationDto.getPriceSum())) <= 0) {
                        res = res.add(new BigDecimal(0));
                    } else {
                        //按基础计费计费计算
                        BigDecimal bigDecimal = baseAggregation(templateId, region, aggregation);
                        res = res.add(bigDecimal);
                    }
                } else if (3 == type) {
                    //达到指定件数➕金额
                    if ((logisticsIncludePostages.get(0).getPieceNum() <= logisticsAggregationDto.getNumSum())
                            && (logisticsIncludePostages.get(0).getAmountNum().compareTo(logisticsAggregationDto.getPriceSum()) <= 0)) {
                        res = res.add(new BigDecimal(0));
                    } else {
                        //按基础计费计费计算
                        BigDecimal bigDecimal = baseAggregation(templateId, region, aggregation);
                        res = res.add(bigDecimal);
                    }
                } else if (4 == type) {
                    //达到指定重量➕金额
                    if ((logisticsIncludePostages.get(0).getWeight().compareTo(logisticsAggregationDto.getWeightSum())) <= 0
                            && (logisticsIncludePostages.get(0).getAmountNum().compareTo(logisticsAggregationDto.getPriceSum()) <= 0)) {
                        res = res.add(new BigDecimal(0));
                    } else {
                        //按基础计费计费计算
                        BigDecimal bigDecimal = baseAggregation(templateId, region, aggregation);
                        res = res.add(bigDecimal);
                    }
                }
            }
        } else {
            BigDecimal bigDecimal = baseAggregation(templateId, region, aggregation);
            res = res.add(bigDecimal);
        }
        return res;

    }

    /**
     * 根据基础计费方式计费
     */
    private BigDecimal baseAggregation(Long templateId, String region, Map<Long, LogisticsAggregationDto> aggregation) {
        Map<String, Object> param = new HashMap<>(CommonConstants.NUMBER_FOUR);
        param.put("logistics_id", templateId);
        param.put("region_json", region);
        List<LogisticsShippingModel> logisticsShippingModels = logisticsShippingModelMapper.selectByParamMap(param);
        BigDecimal all = new BigDecimal(0);
        if (null == logisticsShippingModels || 1 != logisticsShippingModels.size()) {
            //数据异常
            return new BigDecimal(-1);
        } else {
            LogisticsAggregationDto logisticsAggregationDto = aggregation.get(templateId);
            Integer valuationModel = logisticsShippingModels.get(0).getValuationModel();
            //按件数
            if (1 == valuationModel) {
                if (logisticsAggregationDto.getNumSum() >= logisticsShippingModels.get(0).getFirstPiece()) {
                    //总件数-首件
                    int surplus = logisticsAggregationDto.getNumSum() - logisticsShippingModels.get(0).getFirstPiece();
                    all=all.add(logisticsShippingModels.get(0).getFirstAmount());

                    BigDecimal calculation =
                            getCalculation(BigDecimal.valueOf(surplus),
                                    BigDecimal.valueOf(logisticsShippingModels.get(0).getSecondPiece()),
                                    logisticsShippingModels.get(0).getSecondAmount());
                    all = all.add(calculation);
                } else {
                    //不足首件数按首件计费
                    all = all.add(logisticsShippingModels.get(0).getFirstAmount());
                }
            }
            //按重量
            else if (2 == valuationModel) {
                if ((logisticsAggregationDto.getWeightSum().compareTo(logisticsShippingModels.get(0).getFirstWeight())) >= 0) {
                    //剩余重量
                    BigDecimal sub = BigDecimalUtil.sub(logisticsShippingModels.get(0).getFirstWeight().toString(),
                            logisticsAggregationDto.getWeightSum().toString());
                    all=all.add(logisticsShippingModels.get(0).getFirstAmount());

                    BigDecimal calculation = getCalculation(sub,logisticsShippingModels.get(0).getSecondWeight(),
                            logisticsShippingModels.get(0).getSecondAmount());
                    all = all.add(calculation);
                } else {
                    //不足首重按首重计费
                    all = all.add(logisticsShippingModels.get(0).getFirstAmount());
                }
            } else {
                return new BigDecimal(-1);
            }
        }
        return all;
    }

    /**
     * 分组
     */
    private Map<Long, LogisticsAggregationDto> getAggregation(Map<Long, List<LogisticsFreightDto>> collectByTemplateId) {
        Map<Long, LogisticsAggregationDto> aggregation = new HashMap<>(collectByTemplateId.size());
        collectByTemplateId.forEach((k, v) ->
                {
                    LogisticsAggregationDto logisticsAggregationDto = new LogisticsAggregationDto();
                    //个数
                    int num = v.stream().mapToInt(LogisticsFreightDto::getNum).sum();
                    //重量
                    List<BigDecimal> weightList = v.stream()
                            .map(n -> n.getWeight().multiply(new BigDecimal(n.getNum())))
                            .collect(Collectors.toList());
                    Optional<BigDecimal> weightSum = weightList.stream().reduce((x, y) -> x.add(y));
                    //金额
                    List<BigDecimal> priceList = v.stream()
                            .map(n -> n.getPrice().multiply(new BigDecimal(n.getNum())))
                            .collect(Collectors.toList());
                    Optional<BigDecimal> priceSum = priceList.stream().reduce((x, y) -> x.add(y));
                    logisticsAggregationDto.setFreightTemplateId(k);
                    logisticsAggregationDto.setNumSum(num);
                    logisticsAggregationDto.setWeightSum(weightSum.get());
                    logisticsAggregationDto.setPriceSum(priceSum.get());
                    aggregation.put(k, logisticsAggregationDto);
                }
        );
        return aggregation;
    }


}
