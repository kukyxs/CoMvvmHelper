package com.kuky.comvvmhelper.ui.activity

import android.os.Bundle
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityShapeDisplayBinding

@ActivityConfig(statusBarColorString = "#008577")
class ShapeDisplayActivity : BaseActivity<ActivityShapeDisplayBinding>() {

    override fun layoutId() = R.layout.activity_shape_display

    override fun initActivity(savedInstanceState: Bundle?) {

    }
}