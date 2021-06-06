package com.learn.admin.controller;

import cn.hutool.core.io.IoUtil;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.learn.admin.utils.FastDFSClientUtil;
import com.learn.common.entity.Result;
import com.learn.security.anon.AnonymousAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;

/**
 * @author LD
 * @date 2021/6/5 12:57
 */
@Slf4j
@RestController
@RequestMapping("/dfs")
public class FastDFSController {

    @PostMapping("/upload")
    @AnonymousAccess
    public Result<Object> uploadFile(MultipartFile file) throws IOException {
        return Result.data(FastDFSClientUtil.uploadFile(file));
    }

    @DeleteMapping("/delete")
    @AnonymousAccess
    public Result<Object> deleteFile(String path) {
        FastDFSClientUtil.deleteFile(path);
        return Result.data(null);
    }

    @PostMapping("/download")
    @AnonymousAccess
    public void downloadFile(String path, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        Set<MetaData> metaData = FastDFSClientUtil.getMetaData(path);
        String filename = "";
        for (MetaData data : metaData) {
            if (Objects.equals(data.getName(), FastDFSClientUtil.FILENAME)) {
                filename = data.getValue();
                break;
            }
        }
        response.setHeader("Content-Disposition", "attachment;filename" + filename);
        InputStream inputStream = FastDFSClientUtil.download(path);
        ServletOutputStream outputStream = response.getOutputStream();
        IoUtil.copy(inputStream, outputStream);
    }
}
