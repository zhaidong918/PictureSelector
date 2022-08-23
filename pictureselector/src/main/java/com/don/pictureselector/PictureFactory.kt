package com.don.pictureselector

import kotlinx.coroutines.flow.MutableStateFlow

object PictureFactory {

    const val MAX_SELECT_COUNT = 9

    const val RESULT_CODE = 2022
    const val RESULT_DATA = "RESULT_DATA"

    //展示到界面上的数据
    private val pictureList = mutableListOf<PictureItem>()

    //当前操作的文件夹
    val selectFolderFlow = MutableStateFlow(PictureFolder(name = "所有图片"))
    private val folderList = mutableListOf<PictureFolder>()

    //已选择图片数量
    val selectCountFlow = MutableStateFlow(0)
    private val selectPictureList = mutableListOf<PictureItem>()

    fun getSelectData() = selectPictureList
    fun getData() = pictureList
    fun getFolderData() = folderList

    /**
     * 清除数据
     */
    fun clear(){
        pictureList.clear()
        folderList.clear()
        selectPictureList.clear()
        selectFolderFlow.value = PictureFolder()
        selectCountFlow.value = 0
    }

    /**
     * 选择某一个文件夹
     */
    fun selectFolder(pictureFolder: PictureFolder){
        if(pictureFolder == selectFolderFlow.value){
            return
        }

        setPictureData(pictureFolder.pictureList ?: mutableListOf())
        selectFolderFlow.value = pictureFolder
    }

    fun setFolderData(dataList: MutableList<PictureFolder>){
        folderList.apply {

            clear()
            addAll(dataList)

            val index = indexOf(selectFolderFlow.value)

            selectFolder(
                if(index < 0){
                    folderList.firstOrNull() ?: PictureFolder()
                }
                else{
                    //预览界面返回后触发界面更新 （预览界面 选中或者取消）
                    selectFolderFlow.value = PictureFolder()
                    folderList[index]
                }
            )
        }
    }

    /**
     * 设置需要显示到界面的图片数据
     */
    fun setPictureData(dataList: MutableList<PictureItem>){
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