package com.alex.awephotos.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class AlbumInfoBean implements Serializable {
    private String path;
    private String name;
    private String extension;
    private long time;
    private int mediaType;
    private long size;
    private int id;
    private String parentDir;
    private String duration;
    private int dataType = 0; //0 头部  ； 1  数据

    public AlbumInfoBean(int dataType, long time) {
        this.dataType = dataType;
        this.time = time;
    }

    public AlbumInfoBean(String path, String name, long time, int mediaType, long size, int id, String parentDir, int dataType) {
        this.path = path;
        this.name = name;
        if (!TextUtils.isEmpty(name) && name.indexOf(".") != -1) {
            this.extension = name.substring(name.lastIndexOf("."));
        } else {
            this.extension = "null";
        }
        this.time = time;
        this.mediaType = mediaType;
        this.size = size;
        this.id = id;
        this.parentDir = parentDir;
        this.dataType = dataType;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        dataType = dataType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String name) {

        if (!TextUtils.isEmpty(name) && name.indexOf(".") != -1) {
            this.extension = name.substring(name.lastIndexOf("."));
        } else {
            this.extension = "null";
        }

    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParentDir() {
        return parentDir;
    }

    public void setParentDir(String parentDir) {
        this.parentDir = parentDir;
    }
}
