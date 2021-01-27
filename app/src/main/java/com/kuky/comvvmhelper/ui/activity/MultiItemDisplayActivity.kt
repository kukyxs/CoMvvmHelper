package com.kuky.comvvmhelper.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.listener.OnRecyclerItemClickListener
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityMultiItemDisplayBinding
import com.kuky.comvvmhelper.entity.*
import com.kuky.comvvmhelper.ui.adapter.MultiDisplayAdapter
import com.kuky.comvvmhelper.ui.dialog.DemoDialogFragment
import org.koin.androidx.scope.lifecycleScope

@ActivityConfig(statusBarColorString = "#008577")
class MultiItemDisplayActivity : BaseActivity<ActivityMultiItemDisplayBinding>() {

    private val mMultiDisplayAdapter by lifecycleScope.inject<MultiDisplayAdapter>()

    private val mDialogFragment by lazy { DemoDialogFragment() }

    override fun layoutId() = R.layout.activity_multi_item_display

    override fun initActivity(savedInstanceState: Bundle?) {
        mBinding.rvAdapter = mMultiDisplayAdapter
        mBinding.multiClick = OnRecyclerItemClickListener { _, _ -> showDialog() }
        mBinding.displayList.layoutManager = GridLayoutManager(this, mMultiDisplayAdapter.getComplexSpanCount())
        mMultiDisplayAdapter.updateAdapterDataListWithoutAnim(
            mutableListOf<IMultiDisplay>().apply {
                for (i in 0 until 24) add(DisplayTypeFour())

                for (i in 0 until 15) add(DisplayTypeThree())

                for (i in 0 until 12) add(DisplayTypeTwo())

                for (i in 0 until 10) add(DisplayTypeOne())
            }
        )
    }

    private fun showDialog() {
        // before show dialog fragment, check has added, if has add dismiss it or cancel call show.
        // otherwise will throw IllegalStateException: Fragment already added
        if (mDialogFragment.isAdded) {
            mDialogFragment.dismiss()
        }

        mDialogFragment.showAllowStateLoss(supportFragmentManager, "demo")
    }
}