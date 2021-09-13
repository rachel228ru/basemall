package com.medusa.gruul.goods.service.manager;


import com.medusa.gruul.common.core.util.Result;

/**
 * <p>
 * 运费模版 服务类
 * </p>
 *
 * @author lcysike
 * @since 2019-10-01
 */
public interface IFreightTemplateService {

    /**
     * 物流接口获取所有运费模版list
     *
     * @return 产品运费模版对象
     */
    Result findFreightTemplateListByLogistics();

    /**
     * 物流接口获取单个产品运费模版信息
     *
     * @param freightTemplateId
     * @return 运费模版对象
     */
    Result findFreightTemplateInfoByLogistics(Long freightTemplateId);
}
