

package com.medusa.gruul.oss.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.oss.api.entity.FileEntity;
import com.medusa.gruul.platform.api.model.dto.OssConfigDto;

import java.util.List;

/**
 * 文件上传
 */
public interface FileService extends IService<FileEntity> {
    /**
     * oos文件配置
     * @return
     */
    OssConfigDto getConfig();

    /**
     * 批量下载
     * @param linkList
     * @return 。
     */
    List<String> batchDownload(List<String> linkList);

}
