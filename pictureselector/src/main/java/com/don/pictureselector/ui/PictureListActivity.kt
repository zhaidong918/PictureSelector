package com.don.pictureselector.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.don.pictureselector.PictureFactory
import com.don.pictureselector.PictureItem
import com.don.pictureselector.PictureLoaderImpl
import com.don.pictureselector.R
import com.don.pictureselector.databinding.ActivityPictureListBinding
import com.don.pictureselector.databinding.ItemPictureListLayoutBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PictureListActivity :  AppCompatActivity(){

    lateinit var binding: ActivityPictureListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bg)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.bg)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture_list)

        binding.apply {
            rv.adapter = PictureAdapter(PictureFactory.getData())
            rv.layoutManager = GridLayoutManager(this@PictureListActivity, 4)

            rv.addRecyclerListener {
                if(it is PictureAdapter.ViewHolder){
                    it.itemBinding.iv.apply { Glide.with(this).clear(this) }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            PictureLoaderImpl(this@PictureListActivity){
                PictureFactory.setData(it)
                binding.rv.adapter?.notifyDataSetChanged()
            }
        }

        lifecycleScope.launch {
            PictureFactory.selectCountFlow.collect {
                binding.tvSelectComplete.text =
                    if(it > 0) "完成(${PictureFactory.selectProgress()})" else "完成"
                binding.tvSelectComplete.isEnabled = it > 0
            }
        }
    }

}

class PictureAdapter(
    private val dataList: List<PictureItem>
): RecyclerView.Adapter<PictureAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureAdapter.ViewHolder {
        return ViewHolder(ItemPictureListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
            iv.setOnClickListener {
                PicturePreviewActivity.pictureItem = data
                PicturePreviewActivity.actionStart(iv.context, iv)
            }

            if(data.selectIndex > 0){
                tvSelected.setBackgroundResource(R.drawable.bg_green_oval)
                tvSelected.text = data.selectIndex.toString()
            }
            else {
                tvSelected.setBackgroundResource(R.drawable.bg_white_oval)
                tvSelected.text = ""
            }

            tvSelected.setOnClickListener {
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

    inner class ViewHolder(val itemBinding: ItemPictureListLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

}