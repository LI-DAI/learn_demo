package com.learn.admin.utils;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.collect.Sets;
import com.learn.common.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author LD
 * @date 2021/6/5 12:35
 */
@Slf4j
@Component
public class FastDFSClientUtil {

    public static final String FILENAME = "filename";
    public static final String EXTENSION = "extension";

    private static final String DEFAULT_GROUP = "group1";

    private static FastFileStorageClient storageClient;

    static {
        SpringContextUtil.addCallBack(() -> {
            try {
                storageClient = SpringContextUtil.getBean(FastFileStorageClient.class);
            } catch (Exception e) {
                log.error("获取 FastFileStorageClient 异常: {}", e.getMessage());
            }
        });
    }

    public static String uploadFile(MultipartFile file) throws IOException {
        HashSet<MetaData> metaData = Sets.newHashSet(new MetaData(FILENAME, file.getOriginalFilename()),
                new MetaData(EXTENSION, FilenameUtils.getExtension(file.getOriginalFilename())));
        return uploadFileAndMetaData(file, metaData);
    }

    public static String uploadFileAndMetaData(MultipartFile file, Set<MetaData> metaData) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), metaData);
        return storePath.getPath();
    }

    public static void deleteFile(String path) {
        deleteFile(DEFAULT_GROUP, path);
    }

    public static void deleteFile(String path, String group) {
        storageClient.deleteFile(group, path);
    }

    public static InputStream download(String path) {
        return download(DEFAULT_GROUP, path);
    }

    public static InputStream download(String groupName, String path) {
        return storageClient.downloadFile(groupName, path, ins -> ins);
    }

    public static Set<MetaData> getMetaData(String path) {
        return getMetaData(DEFAULT_GROUP, path);
    }

    public static Set<MetaData> getMetaData(String groupName, String path) {
        return storageClient.getMetadata(groupName, path);
    }
}
