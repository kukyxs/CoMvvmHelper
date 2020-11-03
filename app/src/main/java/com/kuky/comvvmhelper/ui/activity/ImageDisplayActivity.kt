package com.kuky.comvvmhelper.ui.activity

import android.graphics.Color
import android.os.Bundle
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityImageDisplayBinding

@ActivityConfig(statusBarColor = Color.BLUE)
class ImageDisplayActivity : BaseActivity<ActivityImageDisplayBinding>() {

    override fun layoutId() = R.layout.activity_image_display

    override fun initActivity(savedInstanceState: Bundle?) {

    }
}