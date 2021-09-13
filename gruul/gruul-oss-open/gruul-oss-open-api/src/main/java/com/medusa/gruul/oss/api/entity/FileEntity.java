package com.medusa.gruul.oss.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @Description: oss文件
 * @Author: alan
 * @Date: 2019/7/21 10:10
 */
@Data
@TableName("t_file")
public class FileEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;
	/**
	 * URL地址
	 */
	private String url;
	/**
	 * 文件原名
	 */
	private String originalName;
	/**
	 * 文件后缀名
	 */
	private String suffix;
	/**
	 * 文件大小
	 */
	private Long size;
	/**
	 * 租户ID
	 */
	private String tenantId;
	/**
	 * 创建时间
	 */
	private Date createDate;

}
