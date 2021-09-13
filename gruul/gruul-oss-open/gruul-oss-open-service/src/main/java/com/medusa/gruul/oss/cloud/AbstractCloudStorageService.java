package com.medusa.gruul.oss.cloud;

import cn.hutool.core.util.IdUtil;
import com.medusa.gruul.platform.api.model.dto.OssConfigDto;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.io.InputStream;
import java.util.Date;

/**
 * 云存储(支持七牛、阿里云、腾讯云、又拍云)
 *
 * @author Mark sunlightcs@gmail.com
 */
public abstract class AbstractCloudStorageService {

    /**
     * 云存储配置信息
     */
    OssConfigDto config;

	/**
	 * 文件路径
	 *
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 返回上传路径
	 */
	public String getPath(String prefix, String suffix) {
		//生成uuid
		String uuid = IdUtil.fastSimpleUUID();
		//文件路径
		String path = DateFormatUtils.format(new Date(), "yyyyMMdd") + "/" + uuid;

		if (StringUtils.isNotBlank(prefix)) {
			path = prefix + "/" + path;
		}

        return path + "." + suffix;
	}

	/**
	 * 文件上传
	 *
	 * @param data 文件字节数组
	 * @param path 文件路径，包含文件名
	 * @return 返回http地址
	 */
	public abstract String upload(byte[] data, String path);

	/**
	 * 文件上传
	 *
	 * @param data   文件字节数组
	 * @param suffix 后缀
	 * @return 返回http地址
	 */
	public abstract String uploadSuffix(byte[] data, String suffix);

	/**
	 * 文件上传
	 *
	 * @param inputStream 字节流
	 * @param path        文件路径，包含文件名
	 * @return 返回http地址
	 */
	public abstract String upload(InputStream inputStream, String path);

	/**
	 * 文件上传
	 *
	 * @param inputStream 字节流
	 * @param suffix      后缀
	 * @return 返回http地址
	 */
	public abstract String uploadSuffix(InputStream inputStream, String suffix);

}
