package com.medusa.gruul.common.data.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Description: 填充器
 * @Author: alan
 * @Date: 2019/8/31 9:37
 */
@Slf4j
@Component
public class IMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		log.info("start insert fill ....");
		this.setInsertFieldValByName("deleted", false, metaObject);
		this.setInsertFieldValByName("createTime", LocalDateTime.now(), metaObject);
		this.setInsertFieldValByName("updateTime", LocalDateTime.now(), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.info("start update  fill ....");
		this.setUpdateFieldValByName("updateTime", LocalDateTime.now(), metaObject);

	}

}
