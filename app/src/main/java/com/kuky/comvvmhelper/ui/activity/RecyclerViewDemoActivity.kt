package com.kuky.comvvmhelper.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import com.kk.android.comvvmhelper.anko.toast
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.extension.delayLaunch
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes
import com.kk.android.comvvmhelper.listener.MultiLayoutImp
import com.kk.android.comvvmhelper.listener.OnRecyclerItemClickListener
import com.kk.android.comvvmhelper.listener.OnRecyclerItemLongClickListener
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kk.android.comvvmhelper.ui.BaseRecyclerViewAdapter
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityRecyclerViewDemoBinding
import com.kuky.comvvmhelper.entity.IntLayoutEntity
import com.kuky.comvvmhelper.entity.StringLayoutEntity
import com.kuky.comvvmhelper.ui.adapter.FooterAdapter
import com.kuky.comvvmhelper.ui.adapter.HeaderAdapter
import com.kuky.comvvmhelper.ui.adapter.MultiLayoutAdapter
import com.kuky.comvvmhelper.ui.adapter.StringAdapter
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope

@ActivityConfig(statusBarColorString = "#008577")
class RecyclerViewDemoActivity : BaseActivity<ActivityRecyclerViewDemoBinding>(), AndroidScopeComponent {
    override val scope: Scope by activityScope()

    private val mAdapterSwitch by lazy { intent.getBooleanExtra("switchOn", false) }

    private val mStringAdapter by inject<StringAdapter>()

    private val mMultiLayoutAdapter by inject<MultiLayoutAdapter>()

    private val mHeaderAdapter by inject<HeaderAdapter>()

    private val mFooterAdapter by inject<FooterAdapter>()

    override fun layoutId() = R.layout.activity_recycler_view_demo

    override fun initActivity(savedInstanceState: Bundle?) {
        mBinding.recyclerAdapter = mAdapterSwitch.yes { mMultiLayoutAdapter }.otherwise { ConcatAdapter(mHeaderAdapter, mStringAdapter, mFooterAdapter) }
        mBinding.divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        mBinding.singleTap = OnRecyclerItemClickListener { position, _ ->
            val content = (mBinding.recyclerAdapter as BaseRecyclerViewAdapter<*>).getItemData(position).toString()
            toast("single tap at ${position}th item => $content")
        }
        mBinding.longClick = OnRecyclerItemLongClickListener { position, _ ->
            toast("long click at ${position}th item")
            if (!mAdapterSwitch) mStringAdapter.removeDataAtPosition(position)
            true
        }

        mAdapterSwitch.yes {
            mMultiLayoutAdapter.updateAdapterDataListWithoutAnim(
                mutableListOf<MultiLayoutImp>().apply {
                    for (i in 0 until 50) {
                        (i % 2 == 0).yes { add(IntLayoutEntity()) }
                            .otherwise { add(StringLayoutEntity()) }
                    }
                }
            )
        }.otherwise {
            mStringAdapter.updateAdapterDataListWithoutAnim(
                mutableListOf<String>().apply {
                    for (i in 0 until 20) add("Adapter Item #${i + 1}#")
                })

            mHeaderAdapter.appendHeader()
            mFooterAdapter.appendFooter()

//            delayLaunch(2_000) {
//                mStringAdapter.updateAdapterDataListWithAnim(object : BaseDiffCallback<String>(
//                    mutableListOf<String>().apply {
//                        for (i in 0 until 10) add("Animator Adapter Item #${i + 1}#")
//                    }
//                ) {
//                    override fun areSameItems(old: String, new: String): Boolean {
//                        return false
//                    }
//
//                    override fun areSameContent(old: String, new: String): Boolean {
//                        return false
//                    }
//                })
//            }

            delayLaunch(2_000) {
                mHeaderAdapter.removeHeader()
                toast("header disappear")
            }

            delayLaunch(4_000) {
                mFooterAdapter.removeFooter()
                toast("footer disappear")
            }
        }
    }
}