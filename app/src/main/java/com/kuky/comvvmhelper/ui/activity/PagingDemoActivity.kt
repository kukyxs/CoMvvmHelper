package com.kuky.comvvmhelper.ui.activity

import android.os.Bundle
import androidx.paging.LoadState
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.extension.scopeInject
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityPagingDemoBinding
import com.kuky.comvvmhelper.ui.adapter.ArticlePagingAdapter
import com.kuky.comvvmhelper.ui.adapter.PagingLoadStateAdapter
import com.kuky.comvvmhelper.viewmodel.PagingViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope

@ActivityConfig(statusBarColorString = "#008577")
class PagingDemoActivity : BaseActivity<ActivityPagingDemoBinding>(), KoinScopeComponent {
    override val scope: Scope by lazy { activityScope() }

    private val mViewModel by viewModel<PagingViewModel>()

    private val mAdapter by scopeInject<ArticlePagingAdapter>()

    private var mLoadJob: Job? = null

    override fun layoutId() = R.layout.activity_paging_demo

    override fun onDestroy() {
        super.onDestroy()
        scope.close()
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        mBinding.adapter = mAdapter.apply {
            addLoadStateListener { loadState ->
                when (loadState.refresh) {
                    is LoadState.Error -> toast("加载数据出错啦")
                    is LoadState.NotLoading -> {
                        if (itemCount == 0) toast("未查找到数据")
                    }
                    is LoadState.Loading -> {

                    }
                }
            }
        }.withLoadStateFooter(PagingLoadStateAdapter { mAdapter.retry() })

        loadArticle()
    }

    private fun loadArticle() {
        mLoadJob?.cancel()
        mLoadJob = launch {
            mViewModel.articleList().collectLatest {
                mAdapter.submitData(it)
            }
        }
    }
}