package com.kuky.comvvmhelper.di

import com.kuky.comvvmhelper.entity.EntityForKoinScopeTest
import com.kk.android.comvvmhelper.helper.createService
import com.kuky.comvvmhelper.helper.ApiService
import com.kuky.comvvmhelper.repository.ArticleRepository
import com.kuky.comvvmhelper.entity.GuideDisplay
import com.kuky.comvvmhelper.ui.activity.GuideActivity
import com.kuky.comvvmhelper.ui.activity.MultiItemDisplayActivity
import com.kuky.comvvmhelper.ui.activity.PagingDemoActivity
import com.kuky.comvvmhelper.ui.activity.RecyclerViewDemoActivity
import com.kuky.comvvmhelper.ui.adapter.ArticlePagingAdapter
import com.kuky.comvvmhelper.ui.adapter.GuideAdapter
import com.kuky.comvvmhelper.ui.adapter.MultiDisplayAdapter
import com.kuky.comvvmhelper.ui.adapter.MultiLayoutAdapter
import com.kuky.comvvmhelper.ui.adapter.StringAdapter
import com.kuky.comvvmhelper.ui.fragment.TestNewKoinFragment
import com.kuky.comvvmhelper.viewmodel.HttpViewModel
import com.kuky.comvvmhelper.viewmodel.PagingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author kuky.
 * @description
 */

val dataSourceModule = module {
    single {
        createService<ApiService>()
    }
}

val repositoryModule = module {
    single {
        ArticleRepository(get())
    }
}

val viewModelModule = module {
    viewModel { HttpViewModel() }

    viewModel { PagingViewModel(get()) }
}

val adapterModule = module {
    scope<GuideActivity> {
        scoped { (items: MutableList<GuideDisplay>) -> GuideAdapter(items) }
    }

    scope<RecyclerViewDemoActivity> {
        scoped { StringAdapter() }

        scoped { MultiLayoutAdapter() }
    }

    scope<MultiItemDisplayActivity> {
        scoped { MultiDisplayAdapter() }
    }

    scope<PagingDemoActivity> {
        scoped { ArticlePagingAdapter() }
    }

    scope<TestNewKoinFragment> {
        scoped { EntityForKoinScopeTest() }
    }
}