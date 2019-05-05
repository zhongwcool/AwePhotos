package com.alex.photos.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PhotoBean implements Parcelable {
    public static final int TYPE_HEAD = 0;
    public static final int TYPE_BODY = 1;

    @IntDef({TYPE_HEAD, TYPE_BODY})
    @Retention(RetentionPolicy.SOURCE)
    @interface DataType {
    }

    private String path;
    private String name;
    private String extension;
    private long time;
    private int mediaType;
    private long size;
    private int height;
    private int width;
    private int id;
    private String parentDir;
    private String duration;
    private int dataType = TYPE_BODY; //0 头部; 1  数据

    public PhotoBean(@DataType int type, long time) {
        this.dataType = type;
        this.time = time;
    }

    public PhotoBean(String path, String name, long time, int mediaType, long size, int height, int width, int id, String parentDir) {
        this.path = path;
        this.name = name;
        if (!TextUtils.isEmpty(name) && name.contains(".")) {
            this.extension = name.substring(name.lastIndexOf("."));
        } else {
            this.extension = "null";
        }
        this.time = time;
        this.mediaType = mediaType;
        this.size = size;
        this.height = height;
        this.width = width;
        this.id = id;
        this.parentDir = parentDir;
    }

    protected PhotoBean(Parcel in) {
        path = in.readString();
        name = in.readString();
        extension = in.readString();
        time = in.readLong();
        mediaType = in.readInt();
        size = in.readLong();
        height = in.readInt();
        width = in.readInt();
        id = in.readInt();
        parentDir = in.readString();
        duration = in.readString();
        dataType = in.readInt();
    }

    public static final Creator<PhotoBean> CREATOR = new Creator<PhotoBean>() {
        @Override
        public PhotoBean createFromParcel(Parcel in) {
            return new PhotoBean(in);
        }

        @Override
        public PhotoBean[] newArray(int size) {
            return new PhotoBean[size];
        }
    };

    @DataType
    public int getDataType() {
        return dataType;
    }

    public void setDataType(@DataType int type) {
        this.dataType = type;
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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeString(extension);
        dest.writeLong(time);
        dest.writeInt(mediaType);
        dest.writeLong(size);
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeInt(id);
        dest.writeString(parentDir);
        dest.writeString(duration);
        dest.writeInt(dataType);
    }
}
