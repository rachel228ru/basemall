package com.medusa.gruul.oss.service.impl;


import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.oss.api.entity.FileEntity;
import com.medusa.gruul.oss.cloud.OSSFactory;
import com.medusa.gruul.oss.dao.FileDao;
import com.medusa.gruul.oss.service.FileService;
import com.medusa.gruul.platform.api.feign.RemoteMiniInfoService;
import com.medusa.gruul.platform.api.model.dto.OssConfigDto;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("fileService")
public class FileServiceImpl extends ServiceImpl<FileDao, FileEntity> implements FileService {

    @Resource
    private RemoteMiniInfoService remoteMiniInfoService;

    @Autowired
    private OSSFactory ossFactory;

    @Override
    public OssConfigDto getConfig() {
        Result<OssConfigDto> result = remoteMiniInfoService.currentOssConfig();
        OssConfigDto ossConfigDto = result.getData();
        return ossConfigDto;
    }

    @Override
    public List<String> batchDownload(List<String> linkList) {
        List<String> files = new ArrayList<>();
        for (String s : linkList) {
            String tmpFilePath = FileUtil.getTmpDirPath().concat(FileUtil.getName(s));
            HttpUtil.downloadFile(s, tmpFilePath);
            //上传文件
            File file = new File(tmpFilePath);
            String suffix = FilenameUtils.getExtension(file.getAbsolutePath());
            String url = ossFactory.build().uploadSuffix(FileUtil.getInputStream(file), suffix);

            //保存文件信息
            FileEntity fileEntity = new FileEntity();
            fileEntity.setUrl(url);
            fileEntity.setOriginalName(file.getName());
            fileEntity.setSize(FileUtil.size(file));
            fileEntity.setSuffix(suffix);
            fileEntity.setCreateDate(new Date());
            this.save(fileEntity);
            files.add(url);
        }
        return files;
    }
    
}
