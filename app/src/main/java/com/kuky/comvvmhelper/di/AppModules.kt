package com.kuky.comvvmhelper.di

import com.kuky.comvvmhelper.ui.HttpViewModel
import com.kuky.comvvmhelper.ui.activity.MultiItemDisplayActivity
import com.kuky.comvvmhelper.ui.activity.RecyclerViewDemoActivity
import com.kuky.comvvmhelper.ui.adapter.MultiDisplayAdapter
import com.kuky.comvvmhelper.ui.adapter.MultiLayoutAdapter
import com.kuky.comvvmhelper.ui.adapter.StringAdapter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author kuky.
 * @description
 */

val viewModelModule = module {
    viewModel { HttpViewModel() }
}

val adapterModule = module {
    scope<RecyclerViewDemoActivity> {
        scoped { StringAdapter() }

        scoped { MultiLayoutAdapter() }
    }

    scope<MultiItemDisplayActivity> {
        scoped { MultiDisplayAdapter() }
    }
}