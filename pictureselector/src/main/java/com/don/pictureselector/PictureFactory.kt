package com.don.pictureselector

import android.widget.Toast
import kotlinx.coroutines.flow.MutableStateFlow

object PictureFactory {

    const val MAX_SELECT_COUNT = 9

    private val pictureList = mutableListOf<PictureItem>()
    private val selectPictureList = mutableListOf<PictureItem>()

    val selectCountFlow = MutableStateFlow(0)

    fun getSelectData() = selectPictureList
    fun getData() = pictureList

    fun setData(dataList: MutableList<PictureItem>){
        pictureList.apply {
            clear()
            addAll(dataList)
        }
    }

    private fun notifyCount(){
        selectCountFlow.value = selectPictureList.size
    }

    fun selectProgress() = "${selectPictureList.size}/$MAX_SELECT_COUNT"

    fun isCantSelect() = selectPictureList.size >= MAX_SELECT_COUNT

    fun select(
        pictureItem: PictureItem,
        notifyItems: (MutableList<Int>)->Unit
    ){

        pictureItem.apply {

            val index = selectPictureList.indexOf(this)

            if(index < 0){

                if(isCantSelect()){
                    notifyItems.invoke(mutableListOf())
                    return
                }

                selectIndex = selectPictureList.size + 1
                selectPictureList.add(pictureItem)
                notifyCount()
                notifyItems.invoke(mutableListOf(position))
                return
            }

            mutableListOf<Int>().apply {
                //调整被选择标识
                selectPictureList.subList(index, selectPictureList.size).onEach {
                    it.selectIndex --

                    add(it.position)
                }
                selectPictureList.removeAt(index)
                selectIndex = -1
                notifyCount()
                notifyItems.invoke(this)
                return
            }
        }
    }

}