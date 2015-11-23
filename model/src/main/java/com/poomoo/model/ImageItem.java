/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

import java.io.Serializable;

/**
 * 图片的对象
 * 作者: 李苜菲
 * 日期: 2015/11/23 15:43.
 */
public class ImageItem implements Serializable {
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public boolean isSelected = false;
}
