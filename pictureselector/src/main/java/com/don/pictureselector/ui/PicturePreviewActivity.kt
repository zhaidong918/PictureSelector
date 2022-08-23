package com.don.pictureselector.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.don.pictureselector.PictureFactory
import com.don.pictureselector.PictureItem
import com.don.pictureselector.R
import com.don.pictureselector.databinding.ActivityPicturePreviewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * 照片预览
 */
class PicturePreviewActivity : AppCompatActivity() {

    companion object {

        var pictureItem: PictureItem? = null

        fun actionStart(
            context: Context,
            imageView: ImageView,
        ) {
            val intent = Intent(context, PicturePreviewActivity::class.java)
            context.startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (context as Activity),
                    imageView,
                    context.getString(R.string.share)
                ).toBundle()
            )
        }
    }

    private val binding by lazy {
        ActivityPicturePreviewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bg)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.bg)
        setContentView(binding.root.apply {
            binding.lifecycleOwner = this@PicturePreviewActivity
        })

        pictureItem?.uri?.apply {
            binding.iv.let { Glide.with(it).load(this).into(it) }
        }

        binding.ivBack.setOnClickListener {
            finishAfterTransition()
        }

        binding.layoutSelect.setOnClickListener {
            pictureItem?.apply {
                PictureFactory.select(this){}
            }
        }

        updateSelectState()
        lifecycleScope.launch {
            PictureFactory.selectCountFlow.collect {
                binding.tvSelectComplete.text =
                    if(it > 0) "完成(${PictureFactory.selectProgress()})" else "完成"
                updateSelectState()
            }
        }
    }

    private fun updateSelectState(){
        binding.ivSelect.setBackgroundResource(
            if((pictureItem?.selectIndex ?: 0) > 0){
                R.drawable.bg_green_oval
            }
            else R.drawable.bg_white_oval
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }
}