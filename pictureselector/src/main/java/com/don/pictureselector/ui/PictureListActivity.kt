package com.don.pictureselector.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.don.pictureselector.PictureItem
import com.don.pictureselector.PictureLoaderImpl
import com.don.pictureselector.R
import com.don.pictureselector.databinding.ActivityPictureListBinding
import com.don.pictureselector.databinding.ItemPictureListLayoutBinding

class PictureListActivity :  AppCompatActivity(){

    lateinit var binding: ActivityPictureListBinding

    val dataList = mutableListOf<PictureItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture_list)

        binding.apply {
            rv.adapter = PictureAdapter(dataList)
            rv.layoutManager = GridLayoutManager(this@PictureListActivity, 4)

            rv.addRecyclerListener {
                if(it is PictureAdapter.ViewHolder){
                    it.itemBinding.iv.apply { Glide.with(this).clear(this) }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            PictureLoaderImpl(this@PictureListActivity){
                dataList.apply {
                    clear()
                    addAll(it)
                }
                binding.rv.adapter?.notifyDataSetChanged()
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

        holder.itemBinding.iv.also { iv ->

            val localUri = dataList[holder.absoluteAdapterPosition].uri
            Glide.with(iv).load(localUri).into(iv)

            iv.setOnClickListener {
                PicturePreviewActivity.localUri = localUri
                PicturePreviewActivity.actionStart(iv.context, iv)
            }
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val itemBinding: ItemPictureListLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

}