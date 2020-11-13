package com.kuky.comvvmhelper.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.extension.delayLaunch
import com.kk.android.comvvmhelper.extension.layoutToDataBinding
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes
import com.kk.android.comvvmhelper.listener.MultiLayoutImp
import com.kk.android.comvvmhelper.listener.OnRecyclerItemClickListener
import com.kk.android.comvvmhelper.listener.OnRecyclerItemLongClickListener
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kk.android.comvvmhelper.ui.BaseDiffCallback
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityRecyclerViewDemoBinding
import com.kuky.comvvmhelper.databinding.RecyclerFootViewBinding
import com.kuky.comvvmhelper.databinding.RecyclerHeaderViewBinding
import com.kuky.comvvmhelper.entity.IntLayoutEntity
import com.kuky.comvvmhelper.entity.StringLayoutEntity
import com.kuky.comvvmhelper.ui.adapter.MultiLayoutAdapter
import com.kuky.comvvmhelper.ui.adapter.StringAdapter
import org.jetbrains.anko.toast
import org.koin.androidx.scope.lifecycleScope

@ActivityConfig(statusBarColor = Color.BLACK)
class RecyclerViewDemoActivity : BaseActivity<ActivityRecyclerViewDemoBinding>() {

    private val mStringAdapter by lifecycleScope.inject<StringAdapter>()

    private val mMultiLayoutAdapter by lifecycleScope.inject<MultiLayoutAdapter>()

    private val mHeaderView by lazy<RecyclerHeaderViewBinding> {
        R.layout.recycler_header_view.layoutToDataBinding(this, mBinding.recyclerList)
    }

    private val mFooterView by lazy<RecyclerFootViewBinding> {
        R.layout.recycler_foot_view.layoutToDataBinding(this, mBinding.recyclerList)
    }

    override fun layoutId() = R.layout.activity_recycler_view_demo

    override fun initActivity(savedInstanceState: Bundle?) {
//        mBinding.recyclerAdapter = mStringAdapter
        mBinding.recyclerAdapter = mMultiLayoutAdapter
        mBinding.divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        mBinding.singleTap = OnRecyclerItemClickListener { position, _ ->
            toast("single tap at ${position}th item")
        }
        mBinding.longClick = OnRecyclerItemLongClickListener { position, _ ->
            toast("long click at ${position}th item")
            true
        }

        //region Adapter Update
        mMultiLayoutAdapter.updateAdapterDataListWithoutAnim(
            mutableListOf<MultiLayoutImp>().apply {
                for (i in 0 until 50) {
                    (i % 2 == 0).yes { add(IntLayoutEntity()) }
                        .otherwise { add(StringLayoutEntity()) }
                }
            }
        )

        mStringAdapter.updateAdapterDataListWithoutAnim(
            mutableListOf<String>().apply {
                for (i in 0 until 10) add("Adapter Item #${i + 1}#")
            })

        delayLaunch(2_000) {
            mStringAdapter.updateAdapterDataListWithAnim(object : BaseDiffCallback<String>(
                mutableListOf<String>().apply {
                    for (i in 0 until 10) add("Animator Adapter Item #${i + 1}#")
                }
            ) {
                override fun areSameItems(old: String, new: String): Boolean {
                    return false
                }

                override fun areSameContent(old: String, new: String): Boolean {
                    return false
                }
            })
        }
        //endregion

        //region HeaderView And FooterView
        mStringAdapter.addHeaderView(mHeaderView)
        mStringAdapter.addFooterView(mFooterView)

        delayLaunch(5_000) {
            mStringAdapter.removeHeaderView(mHeaderView)
            toast("header disappear")
        }

        delayLaunch(8_000) {
            mStringAdapter.removeFooterView(mFooterView)
            toast("footer disappear")
        }
        //endregion
    }
}