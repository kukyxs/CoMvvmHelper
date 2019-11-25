package com.kuky.comvvmhelper

import android.Manifest
import android.os.Bundle
import com.kuky.android.comvvmhelper.extension.onSingleClick
import com.kuky.android.comvvmhelper.ui.BaseActivity
import com.kuky.comvvmhelper.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.jetbrains.anko.toast

@ObsoleteCoroutinesApi
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun layoutId(): Int = R.layout.activity_main

    override fun initActivity(savedInstanceState: Bundle?) {

        permissionsRequest(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), {
            toast("GRANTED")
        }, { _, denied ->
            toast("deny ${denied.size}")
        })

        hello.onSingleClick {
            startActivity(Main2Activity::class.java)
        }
    }
}