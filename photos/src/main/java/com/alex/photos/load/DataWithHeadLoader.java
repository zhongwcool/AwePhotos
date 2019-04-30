package com.alex.photos.load;

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

public class DataWithHeadLoader implements LoaderManager.LoaderCallbacks<Cursor> {

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

    public DataWithHeadLoader(Context context, LoadCallback callback) {
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
                selection1,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        ArrayList<PhotoBean> photoList = new ArrayList<>();//所有文件
        List<Integer> headList = new ArrayList<>();
        String allLastDate = "0";

        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
            long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED));
            int mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
            int height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT));
            int width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));

            boolean isToday = DateUtils.isToday(allLastDate, dateTime + "");
            if (!isToday) {
                //添加头部
                photoList.add(new PhotoBean(PhotoBean.TYPE_HEAD, dateTime));
                allLastDate = dateTime + "";

                headList.add(photoList.size() - 1);
            }

            //获取所在的文件夹
            String dirName = FileUtils.getParentFolderName(path);
            PhotoBean photoBean = new PhotoBean(path, name, dateTime, mediaType, size, height, width, id, dirName);

            if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                //处理视频的时长信息
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION));
                photoBean.setDuration(DateUtils.stringForTime(duration));
            }
            //添加Body
            photoList.add(photoBean);
        }
        mLoader.onData(photoList, headList);
        //cursor.close();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public interface LoadCallback {
        void onData(ArrayList<PhotoBean> list, List<Integer> heads);
    }
}
