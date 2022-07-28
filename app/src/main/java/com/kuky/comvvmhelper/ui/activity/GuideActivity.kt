package com.kuky.comvvmhelper.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.extension.delayLaunch
import com.kk.android.comvvmhelper.helper.ePrint
import com.kk.android.comvvmhelper.listener.OnRecyclerItemClickListener
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kk.android.comvvmhelper.utils.decodeInt
import com.kk.android.comvvmhelper.utils.decodeParcelable
import com.kk.android.comvvmhelper.utils.encodeInt
import com.kk.android.comvvmhelper.utils.encodeParcelable
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityGuideBinding
import com.kuky.comvvmhelper.ui.adapter.GuideAdapter
import com.kuky.comvvmhelper.ui.dialog.TestFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@AndroidEntryPoint
@ActivityConfig(statusBarColorString = "#008577")
class GuideActivity : BaseActivity<ActivityGuideBinding>() {

    @Inject
    lateinit var mGuideAdapter: GuideAdapter

    @Inject
    lateinit var mFragment: TestFragment

    override fun layoutId() = R.layout.activity_guide

    override fun initActivity(savedInstanceState: Bundle?) {
        mBinding.guideAdapter = mGuideAdapter
        mBinding.decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        mBinding.guideClick = OnRecyclerItemClickListener { position, view ->
            mGuideAdapter.getItemData(position)?.run {
                startActivity(Intent(this@GuideActivity, targetDestination).apply {
                    putExtra(
                        "switchOn", view.findViewById<SwitchCompat>(R.id.inner_switch)?.isChecked ?: false
                    )
                })
            }
        }

        encodeInt("new", 100)
        encodeParcelable("user", User("kuky"))

        delayLaunch(1_000) {
            ePrint { decodeInt("new") }
            ePrint { decodeParcelable("user", User::class.java) }
        }

        supportFragmentManager.beginTransaction().add(R.id.append_part, mFragment)
            .commitNowAllowingStateLoss()
    }
}

@Parcelize
data class User(val name: String) : Parcelable