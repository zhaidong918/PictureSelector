package com.don.pictureselector.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.don.pictureselector.*
import com.don.pictureselector.databinding.ActivityPictureBinding
import com.don.pictureselector.databinding.FolderLayoutBinding
import com.don.pictureselector.databinding.ItemFolderLayoutBinding
import com.don.pictureselector.databinding.ItemPictureLayoutBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PictureActivity :  AppCompatActivity(){

    private val binding by lazy {
        ActivityPictureBinding.inflate(layoutInflater)
    }

    private val launch = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.tvSelectComplete.performClick()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bg)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.bg)
        setContentView(binding.root)

        initBinding()
        initFlow()

        loadPicture()
    }

    private fun initBinding(){

        binding.apply {

            rv.adapter = PictureAdapter(PictureFactory.getData()){ data, iv ->
                toPreview(data, iv)
            }
            rv.layoutManager = GridLayoutManager(this@PictureActivity, 4)
            rv.addRecyclerListener {
                if(it is PictureAdapter.ViewHolder){
                    it.itemBinding.iv.apply { Glide.with(this).clear(this) }
                }
            }

            ivBack.setOnAntiShakeClickListener{
                finish()
            }

            tvSelectComplete.setOnAntiShakeClickListener{
                setResult(PictureFactory.RESULT_CODE, Intent().putExtra(PictureFactory.RESULT_DATA, ArrayList(PictureFactory.getSelectData())))
                finish()
            }

            layoutFolder.setOnAntiShakeClickListener {
                showFolder()
            }
        }
    }

    private fun toPreview(data: PictureItem, iv: ImageView){
        PicturePreviewActivity.pictureItem = data
        launch.launch(
            Intent(this@PictureActivity, PicturePreviewActivity::class.java),
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@PictureActivity,
                iv,
                iv.context.getString(R.string.share)
            )
        )
    }

    private fun loadPicture(){
        lifecycleScope.launchWhenStarted {
            PictureLoaderImpl(this@PictureActivity)
        }
    }

    private fun initFlow(){

        lifecycleScope.apply {

            launch {
                PictureFactory.selectCountFlow.collect {
                    binding.tvSelectComplete.text =
                        if(it > 0) "完成(${PictureFactory.selectProgress()})" else "完成"
                    binding.tvSelectComplete.isEnabled = it > 0
                }
            }

            launch {
                PictureFactory.selectFolderFlow.collect {
                    binding.tvFolder.text = it.name
                    binding.rv.adapter?.notifyDataSetChanged()
                }
            }

        }
    }

    var arrowAnim: ViewPropertyAnimator ?= null

    private fun animArrow(show: Boolean){

        arrowAnim?.cancel()

        binding.ivArrow.apply {

            val targetRotation = when(show){
                true -> 180f
                else -> 0f
            }

            arrowAnim =  animate().rotation(targetRotation).setDuration(200).apply {
                start()
            }
        }
    }

    private fun showFolder(){

        animArrow(true)

        FolderLayoutBinding.inflate(layoutInflater).apply {

            PopupWindow(
                root,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            ).apply {
                rv.layoutManager = LinearLayoutManager(root.context)
                rv.adapter = FolderAdapter(PictureFactory.getFolderData()){
                    dismiss()
                }

                setOnDismissListener {
                    animArrow(false)
                }

                animationStyle = R.style.AnimationPreview
                showAsDropDown(binding.layoutTitle)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        PictureFactory.clear()
    }

}

class FolderAdapter(
    private val dataList: List<PictureFolder>,
    private val clickCallBack: () -> Unit
): RecyclerView.Adapter<FolderAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderAdapter.ViewHolder {
        return ViewHolder(ItemFolderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FolderAdapter.ViewHolder, position: Int) {

        val data = dataList[holder.absoluteAdapterPosition]

        holder.itemBinding.apply {

            Glide.with(iv).load(data.cover?.uri).into(iv)

            root.setOnAntiShakeClickListener {
                PictureFactory.selectFolder(data)
                clickCallBack.invoke()
            }

            tvFolder.text = data.name

            tvCount.text = "(${data.pictureList?.size ?: 0})"

            ivSelect.visibility =
                if(data == PictureFactory.selectFolderFlow.value){
                    View.VISIBLE
                }
                else {
                    View.GONE
                }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val itemBinding: ItemFolderLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

}



class PictureAdapter(
    private val dataList: List<PictureItem>,
    private val clickCallBack: (PictureItem, ImageView) -> Unit
): RecyclerView.Adapter<PictureAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureAdapter.ViewHolder {
        return ViewHolder(ItemPictureLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PictureAdapter.ViewHolder, position: Int) {

        val data = dataList[holder.absoluteAdapterPosition]
            .apply {
                this.position = holder.absoluteAdapterPosition
            }
            .let { item ->
                PictureFactory.getSelectData().let {
                    item.apply {
                        val index = it.indexOf(item)
                        if(index >= 0){
                            selectIndex = it[index].selectIndex
                        }
                    }
                }
            }


        holder.itemBinding.apply {

            Glide.with(iv).load(data.uri).into(iv)
            iv.setOnAntiShakeClickListener {
                clickCallBack.invoke(data, iv)
            }

            if(data.selectIndex > 0){
                tvSelect.setBackgroundResource(R.drawable.bg_green_oval)
                tvSelect.text = data.selectIndex.toString()
            }
            else {
                tvSelect.setBackgroundResource(R.drawable.bg_white_oval)
                tvSelect.text = ""
            }

            layoutSelect.setOnAntiShakeClickListener {
                PictureFactory.select(data){ data ->

                    if(data.isEmpty()){
                        Toast.makeText(this.root.context, "最多只能选择${PictureFactory.MAX_SELECT_COUNT}张图片", Toast.LENGTH_LONG).show();
                        return@select
                    }

                    data.onEach { notifyItemChanged(it) }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val itemBinding: ItemPictureLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

}