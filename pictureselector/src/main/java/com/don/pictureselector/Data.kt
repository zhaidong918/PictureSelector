package com.don.pictureselector

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PictureFolder(
    val name: String? = null,
    val path: String? = null,
    val cover: PictureItem? = null,
    val pictureList: MutableList<PictureItem>? = null,
): Parcelable {

    /** 只要文件夹的路径和名字相同，就认为是相同的文件夹  */
    override fun equals(o: Any?): Boolean {
        if(o is PictureFolder){
            return path.equals(o.path, ignoreCase = true)
                    && name.equals(o.name, ignoreCase = true)
        }
        return super.equals(o)
    }
}


@Parcelize
data class PictureItem(
    var uri: Uri,
    var name: String,
    var path: String,
    var size: Long,
    var width: Int,
    var height: Int,
    var mimeType : String,
    var addTime: Long,
    var position: Int = -1,
    var selectIndex: Int = -1
) : Parcelable {

    /** 图片的路径和创建时间相同就认为是同一张图片  */
    override fun equals(o: Any?): Boolean {
        if (o is PictureItem) {
            return path.equals(o.path, ignoreCase = true)
                    && addTime == o.addTime
        }
        return super.equals(o)
    }
}



