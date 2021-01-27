package com.kuky.comvvmhelper.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.listener.OnRecyclerItemClickListener
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityGuideBinding
import com.kuky.comvvmhelper.entity.GuideDisplay
import com.kuky.comvvmhelper.ui.adapter.GuideAdapter
import org.koin.androidx.scope.lifecycleScope
import org.koin.core.parameter.parametersOf
import java.util.*

@ActivityConfig(statusBarColorString = "#008577")
class GuideActivity : BaseActivity<ActivityGuideBinding>() {
    private val mRandom = Random()

    private val mGuideItems = mutableListOf(
        GuideDisplay("Network", randomDrawable(), HttpDemoActivity::class.java, true, "Show Download"),
        GuideDisplay("ImageDisplay", randomDrawable(), ImageDisplayActivity::class.java),
        GuideDisplay("ShapeDisplay", randomDrawable(), ShapeDisplayActivity::class.java),
        GuideDisplay("PermissionRequest", randomDrawable(), PermissionDemoActivity::class.java),
        GuideDisplay("RecyclerViewList", randomDrawable(), RecyclerViewDemoActivity::class.java, true, "Multi Layout"),
        GuideDisplay("MultiManagerDisplay", randomDrawable(), MultiItemDisplayActivity::class.java)
    )

    private val mGuideAdapter by lifecycleScope.inject<GuideAdapter> {
        parametersOf(mGuideItems)
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
    }
}