package com.medusa.gruul.oss.api.feign;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @Description: RemoteSysOssService.java
 * @Author: alan
 * @Date: 2019/7/17 20:44
 */
@FeignClient(value = "oss")
@ApiIgnore
public interface RemoteSysOssService {

    /**
     * 文件上传
     *
     * @param file
     * @return com.medusa.gruul.common.core.util.Result<java.lang.String>
     * @Author alan
     * @Date 2019/8/10 15:39
     */
    @EscapeLogin
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<String> upload(@RequestPart("file") MultipartFile file);

    /**
     * 文件批量下载
     *
     * @param linkList
     * @return java.util.List<java.lang.String>
     * @author alan
     * @date 2020/5/11 20:12
     */
    @EscapeLogin
    @PostMapping("/remote/download/batch")
    List<String> batchDownload(@RequestBody List<String> linkList);

    /**
     * 文件上传
     * @param prefix 后缀
     * @param bytes
     * @return com.medusa.gruul.common.core.util.Result<java.lang.String>
     * @Author alan
     * @Date 2019/8/10 15:39
     */
    @RequestMapping(value = "remote/wx/upload", method = RequestMethod.POST)
    @ApiOperation(value = "H5图片上传")
    Result wxUpload(@RequestBody byte[] bytes,@RequestParam(value = "prefix",required = false) String prefix);


}
