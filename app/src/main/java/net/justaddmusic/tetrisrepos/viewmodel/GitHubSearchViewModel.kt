package net.justaddmusic.tetrisrepos.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import net.justaddmusic.tetrisrepos.model.SearchRequestState
import net.justaddmusic.tetrisrepos.model.SearchResult
import net.justaddmusic.tetrisrepos.repository.SearchResultsDataFactory
import net.justaddmusic.tetrisrepos.repository.SearchResultsDataSource
import net.justaddmusic.tetrisrepos.ui.TetrisReposApplication

class GitHubSearchViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var searchResultsDataFactory: SearchResultsDataFactory
    lateinit var searchResultsData: LiveData<PagedList<SearchResult>>
    private var currentSearchQuery: String? = null

    fun onNewSearchQuery(searchQuery: String): Boolean {

        if (searchQuery != currentSearchQuery) {

            currentSearchQuery = searchQuery

            val pagedListConfig = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build()

            searchResultsDataFactory = SearchResultsDataFactory(
                    getApplication<TetrisReposApplication>().appComponent.gitHubRepository(),
                    searchQuery)

            searchResultsData = LivePagedListBuilder(
                    searchResultsDataFactory,
                    pagedListConfig)
                    .build()

            return true
        }

        return false

    }

    fun getSearchRequestState() : LiveData<SearchRequestState> = Transformations.switchMap(
            searchResultsDataFactory.searchResultsDataSourceLiveData,
            SearchResultsDataSource::state)

}