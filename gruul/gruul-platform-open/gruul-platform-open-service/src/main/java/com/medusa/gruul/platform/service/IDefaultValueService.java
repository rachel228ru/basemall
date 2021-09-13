package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.DefaultValue;
import com.medusa.gruul.platform.api.entity.PlatformShopInfo;
import com.medusa.gruul.platform.constant.DefaultUniqueIdentificationEnum;
import com.medusa.gruul.platform.model.dto.GenerateMultipleDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 默认数据表 服务类
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
public interface IDefaultValueService extends IService<DefaultValue> {

    /**
     * 导入默认值文件
     *
     * @param file 导入默认值
     */
    void reportDefaultValue(MultipartFile file);

    /**
     * 查询指定标识版本kv
     *
     * @param version              版本号
     * @param uniqueIdentification 唯一标识
     * @return kv字符串
     */
    String getDefaultValue(String version, String uniqueIdentification);


    /**
     * 获取指定版本默认值
     *
     * @param uniqueIdentification 唯一标识
     * @param version              版本号
     * @return jsonKY
     */
    String getValue(String uniqueIdentification, String version);

    /**
     * 通知生成默认值
     *  @param info   店铺信息
     * @param shopId 店铺shopId
     * @param defaultUniqueIdentificationEnums
     */
    void informCreateDefaultValue(PlatformShopInfo info, String shopId, List<DefaultUniqueIdentificationEnum> defaultUniqueIdentificationEnums);

    /**
     * 获取指定标识最后一个版本默认值
     *
     * @param uniqueIdentification 唯一标识
     * @return com.medusa.gruul.platform.api.entity.DefaultValue
     */
    DefaultValue getByLastDefaultVersion(String uniqueIdentification);

    /**
     * 生成当前版本指定默认值配置
     *
     * @param generateMultipleDto com.medusa.gruul.platform.model.dto.GenerateMultipleDto
     */
    void generateMultiple(GenerateMultipleDto generateMultipleDto);

    /**
     * 不区分版本获取指定标识数据是否存在
     *
     * @param uniqueIdentification 标识
     * @return true 存在  false不存在
     */
    boolean getByUniqueIdentificationIsExit(String uniqueIdentification);

    /**
     * 获取指定标识的指定版本号的默认值
     *
     * @param uniqueIdentification 套餐标识
     * @param version              指定版本
     * @return 版本默认数据
     */
    DefaultValue getByUniqueIdentificationVersion(String uniqueIdentification, String version);

    /**
     * 复制上个版本生成最新的版本默认值
     *
     * @param version 最新版本号
     */
    void generateNew(String version);
}
