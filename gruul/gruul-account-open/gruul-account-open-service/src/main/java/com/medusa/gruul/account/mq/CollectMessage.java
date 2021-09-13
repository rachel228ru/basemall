package com.medusa.gruul.account.mq;

import com.medusa.gruul.account.model.dto.UserCollectDto;
import lombok.Data;

import java.io.Serializable;

/**
 * @author  xiaoq
 * 用户收藏消息体
 *
 * @data 2020/2/24 13:26
 */
@Data
public class CollectMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;

    private String shopId;

    private String userId;

    private Long productId;

    private UserCollectDto userCollectDto;
}
