package com.medusa.gruul.oss.cloud;


import cn.hutool.core.io.FileUtil;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.platform.api.model.dto.OssConfigDto;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 腾讯云存储
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
public class QcloudCloudStorageService extends AbstractCloudStorageService {
    private COSClient client;

    public QcloudCloudStorageService(OssConfigDto config) {
        this.config = config;
        //初始化
        init();
    }

    private void init() {

        COSCredentials cred = new BasicCOSCredentials(config.getQcloudSecretId(),
                config.getQcloudSecretKey());

        Region region = new Region(config.getQcloudRegion());

        ClientConfig clientConfig = new ClientConfig(region);
        client = new COSClient(cred, clientConfig);
    }

    @Override
    public String upload(byte[] data, String path) {
        //腾讯云必需要以"/"开头
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        //上传到腾讯云
        PutObjectRequest putObjectRequest =
                new PutObjectRequest(
                        config.getQcloudBucketName().concat("-").concat(config.getQcloudAppId().toString()),
                        path,
                        FileUtil.writeBytes(data, FileUtil.getTmpDirPath() + path));
        PutObjectResult putObjectResult = client.putObject(putObjectRequest);
        log.info(putObjectResult.toString());
        client.shutdown();


        return config.getQcloudDomain() + path;

    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new ServiceException("上传文件失败", e);
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getQcloudPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQcloudPrefix(), suffix));
    }
}
