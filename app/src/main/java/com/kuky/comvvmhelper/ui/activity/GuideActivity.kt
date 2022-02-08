package com.kuky.comvvmhelper.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.kuky.comvvmhelper.entity.GuideDisplay
import com.kuky.comvvmhelper.ui.adapter.GuideAdapter
import kotlinx.parcelize.Parcelize
import java.util.*

@ActivityConfig(statusBarColorString = "#008577")
class GuideActivity : BaseActivity<ActivityGuideBinding>() {

    private val mRandom = Random()

    private val mGuideItems = mutableListOf(
        GuideDisplay("Network", randomDrawable(), HttpDemoActivity::class.java, true, "Show Download"),
        GuideDisplay("ImageDisplay", randomDrawable(), ImageDisplayActivity::class.java),
        GuideDisplay("PermissionRequest", randomDrawable(), PermissionDemoActivity::class.java),
        GuideDisplay("RecyclerViewList", randomDrawable(), RecyclerViewDemoActivity::class.java, true, "Multi Layout"),
        GuideDisplay("MultiManagerDisplay", randomDrawable(), MultiItemDisplayActivity::class.java)
    )

    private val mGuideAdapter by lazy {
        GuideAdapter(mGuideItems)
    }

    private fun randomDrawable() = ColorDrawable(
        Color.parseColor(
            String.format("#%06x", mRandom.nextInt(256 * 256 * 256))
        )
    )

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
    }
}

@Parcelize
data class User(val name: String) : Parcelable