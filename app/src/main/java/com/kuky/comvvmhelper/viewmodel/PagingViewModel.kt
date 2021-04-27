package com.kuky.comvvmhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kuky.comvvmhelper.pagingsource.ArticlePagingSource
import com.kuky.comvvmhelper.repository.ArticleRepository

/**
 * @author kuky.
 * @description
 */

class PagingViewModel(private val repository: ArticleRepository) : ViewModel() {

    fun articleList() = Pager(PagingConfig(20)) {
        ArticlePagingSource(repository)
    }.flow.cachedIn(viewModelScope)
}