package com.don.pictureselector

import android.view.View
import android.view.View.OnClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun View.setOnAntiShakeClickListener(
    scope: CoroutineScope = MainScope(),
    antiShakeTime: Long = 500, // 单位 毫秒
    l: OnClickListener
){
    setOnClickListener{
        l.onClick(it)
        scope.launch {
            it.isEnabled = false
            delay(antiShakeTime)
            it.isEnabled = true
        }
    }
}