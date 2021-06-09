package com.learn.admin.service.impl;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.admin.dao.FileMapper;
import com.learn.admin.entity.FileInfo;
import com.learn.admin.service.FileService;
import com.learn.admin.utils.FastDFSClientUtil;
import com.learn.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static com.learn.admin.utils.FastDFSClientUtil.DEFAULT_GROUP;

/**
 * @author LD
 * @date 2021/6/9 13:48
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl extends ServiceImpl<FileMapper, FileInfo> implements FileService {

    @Override
    public FileInfo uploadFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String ext = FilenameUtils.getExtension(filename);
        String path = FastDFSClientUtil.uploadFile(file);
        FileInfo fileInfo = new FileInfo(filename, ext, DEFAULT_GROUP, path, 0);
        save(fileInfo);
        return fileInfo;
    }

    @Override
    public Boolean deleteFile(Integer fileId) {
        FileInfo file = getById(fileId);
        if (file != null) {
            FastDFSClientUtil.deleteFile(file.getPath());
        }
        return removeById(fileId);
    }

    @Override
    public void downloadFile(Integer fileId, HttpServletResponse response) throws IOException {
        FileInfo fileInfo = getById(fileId);
        if (fileInfo == null) {
            throw new BadRequestException("当前文件不存在，无法下载！");
        }
        String path = fileInfo.getPath();
        String filename = fileInfo.getFilename();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream = response.getOutputStream();
            inputStream = FastDFSClientUtil.download(path);
            IoUtil.copy(inputStream, outputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IoUtil.close(outputStream);
            IoUtil.close(inputStream);
        }
    }
}
