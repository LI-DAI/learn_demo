package com.learn.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LD
 * @date 2021/6/9 13:49
 */
@Data
@TableName(value = "bmsys_file")
public class FileInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer fileId;

    private String filename;

    private String ext;

    private String groupName;

    private String path;

    private Integer type;

    public FileInfo() {
    }

    public FileInfo(String filename, String ext, String groupName, String path, Integer type) {
        this.filename = filename;
        this.ext = ext;
        this.groupName = groupName;
        this.path = path;
        this.type = type;
    }
}
