package com.medusa.gruul.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.mapper.OrderSettingMapper;
import com.medusa.gruul.order.service.IOrderSettingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 订单设置表 服务实现类
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
@Service
public class OrderSettingServiceImpl extends ServiceImpl<OrderSettingMapper, OrderSetting> implements IOrderSettingService {

    @Override
    public void init() {
        OrderSetting setting = this.getOne(null);
        if (ObjectUtil.isNull(setting)) {
            setting = new OrderSetting();
            setting.setFlashOrderOvertime(30L);
            setting.setNormalOrderOvertime(60L);
            setting.setConfirmOvertime(7);
            setting.setFinishOvertime(7);
            setting.setCommentOvertime(3);
            setting.setOpenEvaluate(true);
            setting.setAfsApplyNumber(3);
            setting.setMerchantConfirmOvertime(3);
            setting.setUserReturnOvertime(7);
            setting.setKdAppId("");
            setting.setKdAppKey("");
            setting.setPaymentModel("1");
            setting.setCustomFrom("[{\"key\":\"买家留言\",\"type\":\"text\",\"required\":false," +
                    "\"placeholder\":\"请输入45个字内说明\"}]");
            setting.setOrderNotify(false);
            this.save(setting);
        }
    }

    @Override
    public OrderSetting update(OrderSetting setting) {
        if (ObjectUtil.isNotNull(setting.getId())) {
            this.saveOrUpdate(setting);
            return setting;
        }
        return null;
    }
}
