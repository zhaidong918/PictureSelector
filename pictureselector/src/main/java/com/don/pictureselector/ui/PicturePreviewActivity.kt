package com.don.pictureselector.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.don.pictureselector.PictureFactory
import com.don.pictureselector.PictureItem
import com.don.pictureselector.R
import com.don.pictureselector.databinding.ActivityPicturePreviewBinding
import com.don.pictureselector.setOnAntiShakeClickListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * 照片预览
 */
class PicturePreviewActivity : AppCompatActivity() {

    companion object {
        var pictureItem: PictureItem? = null
    }

    private val binding by lazy {
        ActivityPicturePreviewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bg)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.bg)
        setContentView(binding.root)

        initBinding()
        initFlow()
    }

    private fun initBinding(){

        binding.apply {

            ivBack.setOnAntiShakeClickListener {
                finishAfterTransition()
            }

            pictureItem?.uri?.apply {
                iv.let { Glide.with(it).load(this).into(it) }
            }

            layoutSelect.setOnAntiShakeClickListener {
                pictureItem?.apply {
                    PictureFactory.select(this){}
                }
            }

            tvSelectComplete.setOnAntiShakeClickListener{

                if(PictureFactory.getSelectData().isEmpty()){
                    pictureItem?.apply {
                        PictureFactory.select(this){}
                    }
                }

                setResult(Activity.RESULT_OK)
                finish()

            }

            updateSelectState()
        }
    }


    private fun initFlow(){

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

    override fun onDestroy() {
        super.onDestroy()

        pictureItem = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }
}