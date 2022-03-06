package com.example.hw_3.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw_3.data.model.PersonGitHub
import com.example.hw_3.domain.model.Person
import com.example.hw_3.domain.usecase.GetPersonsList
import com.example.hw_3.presentation.paging3.PagingDataLce
import com.example.hw_3.presentation.paging3.PagingDataSource
import kotlinx.coroutines.flow.*

class PersonViewModel(private val getPersonsList: GetPersonsList) : ViewModel() {

    private val pagingDataSource = PagingDataSource(viewModelScope, PAGE_SIZE) {
        getPersonsList(it.countToLoad, it.countMax)
    }

    val personPagingData: Flow<PagingDataLce<PersonGitHub>> = pagingDataSource.subscribePagingData()

    fun onLoadMore() {
        pagingDataSource.loadMore()
    }

    fun onRefresh() {
        pagingDataSource.refreshData()
    }

    fun onRetry() {
        pagingDataSource.getCurrentData()
    }

    companion object {
        private const val PAGE_SIZE = 30
    }

}