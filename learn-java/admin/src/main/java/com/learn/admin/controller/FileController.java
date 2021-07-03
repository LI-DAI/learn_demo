package com.learn.admin.controller;

import com.learn.admin.service.FileService;
import com.learn.common.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LD
 * @date 2021/6/5 12:57
 */
@Slf4j
@RestController
@RequestMapping("/dfs")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public Result<Object> uploadFile(MultipartFile file) throws IOException {
        return Result.data(fileService.uploadFile(file));
    }

    @DeleteMapping("/delete")
    public Result<Object> deleteFile(Integer fileId) {
        return Result.data(fileService.deleteFile(fileId));
    }

    @PostMapping("/download")
    public void downloadFile(Integer fileId, HttpServletResponse response) throws IOException {
        fileService.downloadFile(fileId, response);
    }

    @PostMapping("/preview")
    public void preview(Integer fileId, HttpServletResponse response) {
        fileService.preview(fileId, response);
    }
}
