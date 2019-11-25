package com.kuky.comvvmhelper

import android.Manifest
import android.os.Bundle
import com.kuky.android.comvvmhelper.ui.BaseActivity
import com.kuky.comvvmhelper.databinding.ActivityMain2Binding
import org.jetbrains.anko.toast

class Main2Activity : BaseActivity<ActivityMain2Binding>() {

    override fun layoutId(): Int = R.layout.activity_main2

    override fun initActivity(savedInstanceState: Bundle?) {
        permissionsRequest(arrayOf(Manifest.permission.WRITE_CONTACTS), {
            toast("GRANTED")
        }, { _, denied ->
            toast("deny ${denied.size}")
        })
    }
}
