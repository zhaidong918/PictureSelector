package com.don.pictureselector

import android.content.ContentUris
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import java.io.File


enum class LoaderCategory{
    ALL, PATH
}


class PictureLoaderImpl(
    private var activity: AppCompatActivity,
    val folderPath: String?= null,
) : LoaderManager.LoaderCallbacks<Cursor> {

    companion object{
        const val KEY_PATH = "KEY_PATH"
        private val IMAGE_PROJECTION = arrayOf( //查询图片需要的数据列
            MediaStore.Images.Media._ID,  //获取文件的uri
            MediaStore.Images.Media.DISPLAY_NAME,  //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,  //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,  //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,  //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,  //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,  //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED //图片被添加的时间，long型  1450518608
        )
    }

    init {
        LoaderManager.getInstance(activity).apply {
            if(folderPath.isNullOrEmpty()){
                initLoader(LoaderCategory.ALL.ordinal, null, this@PictureLoaderImpl)
            }
            else{
                initLoader(LoaderCategory.PATH.ordinal, Bundle().apply { putString(KEY_PATH, folderPath) }, this@PictureLoaderImpl)
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return when(id){
            LoaderCategory.ALL.ordinal -> {
                CursorLoader(
                    activity,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION,
                    null,
                    null,
                    IMAGE_PROJECTION[7] + " DESC")
            }
            LoaderCategory.PATH.ordinal -> {
                CursorLoader(
                    activity,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION,
                    IMAGE_PROJECTION[2] + " like '%?%'",
                    arrayOf(args?.getString(KEY_PATH).orEmpty()) ,
                    IMAGE_PROJECTION[7] + " DESC")
            }
            else -> throw RuntimeException("onCreateLoader not support id: $id")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {

        val folderList = mutableListOf<PictureFolder>()

        data?.apply {
            val pictureList = mutableListOf<PictureItem>().apply {
                moveToFirst()
                while (moveToNext()){
                    PictureItem(
                        uri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                        ),
                        name = data.getStringOrNull(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1])).orEmpty(),
                        path = data.getStringOrNull(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2])).orEmpty(),
                        size = data.getLongOrNull(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3])) ?: 0,
                        width = data.getIntOrNull(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4])) ?: 0,
                        height = data.getIntOrNull(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5])) ?: 0,
                        mimeType = data.getStringOrNull(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6])).orEmpty(),
                        addTime = data.getLongOrNull(data.getColumnIndexOrThrow(IMAGE_PROJECTION[7])) ?: 0,
                    ).apply {
                        if(File(path).let { it.exists().and(it.length() > 0) }){
                            add(this)
                        }
                    }
                }
            }

            pictureList.groupBy { File(it.path).parent.orEmpty() }.map {
                folderList.add(
                    PictureFolder(
                        name = File(it.key).name,
                        path = it.key,
                        cover = it.value.firstOrNull(),
                        pictureList = it.value.toMutableList()
                    )
                )
            }
            Log.e("PictureLoaderImpl", "callBack -> ${pictureList.size}")

            PictureFactory.setFolderData(folderList.apply {
                add(0, PictureFolder(
                    name = "所有图片",
                    path = "/",
                    cover = pictureList.firstOrNull(),
                    pictureList = pictureList
                ))
            })
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        Log.e("PictureLoaderImpl", "onLoaderReset is called")
    }
}