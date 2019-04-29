package com.alex.photos.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.alex.photos.bean.PhotoBean;
import com.alex.photos.utils.DateUtils;
import com.alex.photos.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class GalleryLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private String[] MEDIA_PROJECTION = {
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.HEIGHT,
            MediaStore.Files.FileColumns.WIDTH,
            MediaStore.Files.FileColumns._ID,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.Files.FileColumns.PARENT};
    private Context mContext;
    private LoadCallback mLoader;

    public GalleryLoader(Context context, LoadCallback callback) {
        this.mContext = context;

        if (null != callback) {
            mLoader = callback;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoadCallback");
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int picker_type, Bundle bundle) {
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        String selection1 = MediaStore.Files.FileColumns.DISPLAY_NAME + " like '%JSV%'";

        Uri queryUri = MediaStore.Files.getContentUri("external");
        CursorLoader cursorLoader = new CursorLoader(
                mContext,
                queryUri,
                MEDIA_PROJECTION,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        ArrayList<PhotoBean> albumInfoList = new ArrayList<>();//所有文件夹
        List<Integer> headPositions = new ArrayList<>();

        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
            long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED));
            int mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
            int height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT));
            int width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));

            //获取所在的文件夹
            String dirName = FileUtils.getParentFolderName(path);
            PhotoBean albumInfoBean = new PhotoBean(path, name, dateTime, mediaType, size, height, width, id, dirName, 1);

            if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                //处理视频的时长信息
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION));
                albumInfoBean.setDuration(DateUtils.stringForTime(duration));
            }

            albumInfoList.add(albumInfoBean);
        }
        mLoader.onData(albumInfoList, headPositions);
        //cursor.close();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public interface LoadCallback {
        void onData(ArrayList<PhotoBean> list, List<Integer> heads);
    }
}
