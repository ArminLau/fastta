package com.linkstart.fastta.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.entity.Employee;
import com.linkstart.fastta.exception.SystemTransactionException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 通用操作的控制层
 */

@RestController
@RequestMapping("/common")
@Slf4j
@Api(tags = "通用管理接口")
public class CommonController {
    @Value("${fastta.upload-path}")
    private String uploadPath;

    @ApiOperation("上传图片或其他文件")
    @PostMapping("/upload")
    public R upload(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
        Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("员工{}上传了一个文件: {}", employee.getUsername(), file.getOriginalFilename());
        String filename = IdUtil.fastSimpleUUID()+"-"+file.getOriginalFilename();
        file.transferTo(new File(getUploadPath()+File.separator+filename));
        return R.success((Object) filename);
    }

    @ApiOperation("下载图片或其他文件")
    @GetMapping(value = "/download", params = {"name"})
    public void download(String name, HttpServletResponse response) throws IOException{
        String filePath = getUploadPath() + File.separator + name;
        if(!FileUtil.exist(filePath)){
            throw new SystemTransactionException("请求的文件:["+name+"]不存在，下载失败！");
        }
        String postfix = name.substring(name.lastIndexOf(".")+1);
        //如果请求的文件类型是图片，则配置ContentType
        if ("png,jpg,jpeg".indexOf(postfix.toLowerCase()) != -1) {
            response.setContentType("image/"+(postfix.equalsIgnoreCase("jpg") ? "jpeg" : postfix.toLowerCase()));
        }else {
            response.setHeader("Content-Disposition", "attachment; filename="+name);
        }
        ServletOutputStream outputStream = response.getOutputStream();
        BufferedInputStream fileInputStream = FileUtil.getInputStream(filePath);
        IoUtil.copy(fileInputStream, outputStream);
    }

    private String getUploadPath(){
        String path = uploadPath;
        //如果默认上传文件目录使用默认值"default"则获取classpath下面的upload目录
        if(path.equals("default")){
            path = new File(getClass().getClassLoader().getResource("").getPath()).getAbsolutePath() + File.separator + "upload";
            //upload目录如果不存在则创建该目录
            if(!FileUtil.exist(path)){
                FileUtil.mkdir(path);
            }
        }
        return path;
    }
}