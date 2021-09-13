package com.medusa.gruul.oss.controller;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.oss.api.entity.FileEntity;
import com.medusa.gruul.oss.api.feign.RemoteSysOssService;
import com.medusa.gruul.oss.cloud.OSSFactory;
import com.medusa.gruul.oss.service.FileService;
import com.medusa.gruul.oss.task.UpdateConfigTask;
import com.medusa.gruul.oss.ueditor.ActionEnter;
import com.medusa.gruul.oss.utls.OssRedisTools;
import io.swagger.annotations.Api;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/")
@Api(value = "file", tags = "文件上传")
public class FileUploadController {
    @Autowired
    private FileService fileService;
    @Autowired
    private RemoteSysOssService remoteSysOssService;
    @Autowired
    private OSSFactory ossFactory;
    @Autowired
    private UpdateConfigTask updateConfigTask;


    /**
     * 上传文件
     */
    @EscapeLogin
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new ServiceException("上传文件不能为空");
        }

        //上传文件
        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());

        String url = ossFactory.build().uploadSuffix(file.getBytes(), suffix);

        //保存文件信息
        FileEntity fileEntity = new FileEntity();
        fileEntity.setUrl(url);
        fileEntity.setOriginalName(file.getOriginalFilename());
        fileEntity.setSize(file.getSize());
        fileEntity.setSuffix(suffix);
        fileEntity.setCreateDate(new Date());
        fileService.save(fileEntity);
        Result<String> ok = Result.ok(url);
        ok.setMsg(file.getOriginalFilename());
        return ok;
    }


    @PostMapping("/download/batch")
    public List<String> orderInfo(@RequestBody List<String> linkList) {
        return remoteSysOssService.batchDownload(linkList);
    }


    /**
     * 删除
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody Long[] ids) {
        fileService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }


    @GetMapping(value = "ueditor/exec")
    @EscapeLogin
    public void exec(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        System.out.println(rootPath);
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @EscapeLogin
    @GetMapping("/test1")
    public Result test1() {
        updateConfigTask.run();
        return Result.ok();
    }

    @EscapeLogin
    @GetMapping("/version")
    public Result version() {
        return Result.ok("1.2");
    }


    /**
     * baiDu 自定义配置 /上传接口
     *
     * @param action
     */
    @GetMapping(value = "/bai_du")
    @EscapeLogin
    @ResponseBody
    public Object action(@RequestParam(value = "action") String action, HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        final String upload = "uploadimage";
        response.setContentType("application/json");
        if (upload.equals(action)) {
            //            ServletInputStream inputStream = request.getInputStream();
            //            Result result = this.upload();
            //            HashMap<String, String> resMap = new HashMap<>(4);
            //            resMap.put("original", result.getMsg());
            //            resMap.put("state", result.getCode() == 200 ? "SUCCESS" : "FAIL");
            //            resMap.put("title", result.getMsg());
            //            resMap.put("url", result.getData().toString());
            //            return resMap;
        }

        OssRedisTools redisTools = new OssRedisTools();
        final String fileName = "config.json";
        String s = redisTools.get(fileName);
        if (StringUtils.isBlank(s)) {
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream(fileName);
            StringWriter writer = new StringWriter();
            IOUtils.copy(resource, writer, StandardCharsets.UTF_8.name());
            s = writer.toString();
            redisTools.set(fileName, s);
        }
        return s;
    }


}
