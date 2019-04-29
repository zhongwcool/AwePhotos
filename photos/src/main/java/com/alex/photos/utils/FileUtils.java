package com.alex.photos.utils;

import java.text.DecimalFormat;

public class FileUtils {

    public static String getParentFolderName(String path) {
        String[] sp = path.split("/");
        return sp[sp.length - 2];
    }

    public static String getReadableSize(long bytes) {
        String size;
        DecimalFormat df = new DecimalFormat("#.##");

        if (bytes < 1024) {
            size = df.format(bytes) + " B";
        } else if (bytes < 1024 * 1024) {
            size = df.format(bytes / 1024) + " KB";
        } else if (bytes < 1024 * 1024 * 1024) {
            size = df.format(bytes / 1024 / 1024) + " MB";
        } else if (bytes < 1024 * 1024 * 1024 * 1024) {
            size = df.format(bytes / 1024 / 1024 / 1024) + " GB";
        } else if (bytes < 1024 * 1024 * 1024 * 1024 * 1024) {
            size = df.format(bytes / 1024 / 1024 / 1024 / 1024) + " TB";
        } else if (bytes < 1024 * 1024 * 1024 * 1024 * 1024 * 1024) {
            size = df.format(bytes / 1024 / 1024 / 1024 / 1024 / 1024) + " PB";
        } else {
            size = "huge";
        }

        return size;
    }

}
