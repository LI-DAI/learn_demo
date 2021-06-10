package com.learn.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.admin.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LD
 * @date 2021/6/9 13:48
 */
public interface FileService extends IService<FileInfo> {

    /**
     * 保存文件信息
     *
     * @param file 文件信息
     * @return /
     */
    FileInfo uploadFile(MultipartFile file) throws IOException;

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return /
     */
    Boolean deleteFile(Integer fileId);

    /**
     * 下载文件
     *
     * @param fileId   文件ID
     * @param response /
     */
    void downloadFile(Integer fileId, HttpServletResponse response);
}
