/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

import java.io.File;

/**
 * 文件
 * 作者: 李苜菲
 * 日期: 2015/12/2 14:39.
 */
public class FileBO {
    private String type;
    private File ImgFile;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getImgFile() {
        return ImgFile;
    }

    public void setImgFile(File imgFile) {
        ImgFile = imgFile;
    }
}
