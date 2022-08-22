package com.don.pictureselector.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.don.pictureselector.R
import com.don.pictureselector.databinding.ActivityPicturePreviewBinding


/**
 * 照片预览
 */
class PicturePreviewActivity : AppCompatActivity() {

    companion object {

        var localUri: Uri? = null

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
        setContentView(binding.root.apply {
            binding.lifecycleOwner = this@PicturePreviewActivity
        })

        localUri?.apply {
            binding.iv.let { Glide.with(it).load(this).into(it) }
        }

        binding.ivBack.setOnClickListener {
            finishAfterTransition()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }
}