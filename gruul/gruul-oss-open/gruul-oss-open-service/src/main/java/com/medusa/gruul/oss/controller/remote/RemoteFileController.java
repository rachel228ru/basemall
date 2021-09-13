package com.medusa.gruul.oss.controller.remote;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.oss.controller.FileUploadController;
import com.medusa.gruul.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

/**
 * @description: Feign接口
 * @author: alan
 * @date: 2019/12/16 19:41
 */
@RestController
@RequestMapping("/remote")
@Api(tags = "Feign接口")
public class RemoteFileController {
    @Resource
    private FileService fileService;
    @Resource
    private FileUploadController fileUploadController;

    /**
     * h5文件上传
     *
     * @param bytes
     * @return com.medusa.gruul.common.core.util.Result<java.lang.String>
     * @Author alan
     * @Date 2019/8/10 15:39
     */
    @PostMapping("/wx/upload")
    @EscapeLogin
    @ApiOperation(value = "H5图片上传")
    public Result wxUpload(@RequestBody byte[] bytes, @RequestParam(value = "prefix", required = false) String prefix) {
        Result upload = null;
        InputStream inputStream = new ByteArrayInputStream(bytes);
        MultipartFile file = null;
        System.out.println(prefix);
        try {
            file = new MockMultipartFile("tmp." + prefix, "tmp." + prefix, ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
            upload = fileUploadController.upload(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upload;
    }

    /**
     * @description: RemoteFileController.java
     * @author: alan
     * @date: 2020/5/11 18:11
     */
    @EscapeLogin
    @PostMapping("/download/batch")
    public List<String> batchDownload(@RequestBody List<String> linkList) {
        return fileService.batchDownload(linkList);
    }


}
