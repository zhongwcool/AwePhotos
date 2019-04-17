package com.alex.awephotos.utils;

public class FileUtils {


    public static String getParentFolderName(String path) {
        String sp[] = path.split("/");
        return sp[sp.length - 2];
    }

}
