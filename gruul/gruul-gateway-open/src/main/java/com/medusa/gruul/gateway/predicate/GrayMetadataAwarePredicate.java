

package com.medusa.gruul.gateway.predicate;

import cn.hutool.core.util.StrUtil;
import com.medusa.gruul.common.core.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.http.HttpHeaders;

import java.util.Map;

/**
 * 基于 Metadata version 的服务筛选
 *
 * @author L.cm
 * @author lengleng
 */
@Slf4j
public class GrayMetadataAwarePredicate extends AbstractDiscoveryEnabledPredicate {

    @Override
    protected boolean apply(NacosServer server, HttpHeaders headers) {

        final Map<String, String> metadata = server.getMetadata();
        String version = metadata.get(CommonConstants.VERSION);
        // 判断Nacos服务是否有版本标签
        if (StrUtil.isBlank(version)) {
            log.trace("nacos未配置version字段,GrayMetadataAwarePredicate将被跳过");
            return true;
        }

        // 判断请求中是否有版本
        String target = headers.getFirst(CommonConstants.VERSION);
        if (StrUtil.isBlank(target)) {
            log.trace("请求头中未配置version字段,GrayMetadataAwarePredicate将被跳过");
            return true;
        }

        log.info("请求版本:{} ,当前服务版本:{}", target, version);
        return target.equals(version);
    }

}
