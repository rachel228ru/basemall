package com.medusa.gruul.payment.router;

import cn.hutool.core.thread.ThreadUtil;
import com.medusa.gruul.payment.api.model.dto.PayRequestDto;
import com.medusa.gruul.payment.api.model.dto.PayResultDto;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author whh
 */
@Log
public class PayMessageRouter {

    private final List<PayMessageRouterRule> rules = new ArrayList<>(6);

    public PayMessageRouter() {
    }

    List<PayMessageRouterRule> getRules() {
        return this.rules;
    }

    /**
     * 开始一个新的Route规则.
     */
    public PayMessageRouterRule rule() {
        return new PayMessageRouterRule(this);
    }

    /**
     * 处理支付消息.
     */
    public PayResultDto route(PayRequestDto payRequestDto) {
        PayResultDto res = null;
        try {
            final List<PayMessageRouterRule> matchRules = new ArrayList<>(6);
            // 收集匹配的规则
            for (final PayMessageRouterRule rule : this.rules) {
                if (rule.test(payRequestDto)) {
                    matchRules.add(rule);
                    //判断该路由规则执行完毕之后是否继续执行其他路由,默认继续执行其他路由
                    if (!rule.isReEnter()) {
                        break;
                    }
                }
            }
            //判断是否有匹配路由规则
            if (matchRules.size() == 0) {
                return null;
            }
            for (final PayMessageRouterRule rule : matchRules) {
                // 返回最后一个非异步的rule的执行结果
                if (rule.isAsync()) {
                    ThreadUtil.execAsync(() -> rule.service(payRequestDto), false);
                } else {
                    res = rule.service(payRequestDto);
                }
            }

            return res;
        } catch (Exception e) {
            log.warning("error : " + e.getMessage());
            log.warning(Arrays.asList(e.getStackTrace()).toString());
            res = new PayResultDto();
            res.setReturnCode("FAIL");
            res.setReturnMsg(e.getMessage());
            return res;
        }

    }


}
