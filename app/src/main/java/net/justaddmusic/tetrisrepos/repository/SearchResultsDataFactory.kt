package net.justaddmusic.tetrisrepos.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import net.justaddmusic.tetrisrepos.model.SearchResult

class SearchResultsDataFactory(private val gitHubRepository: GitHubRepository,
                               private val searchQuery: String)
    : DataSource.Factory<Int, SearchResult>() {

    val searchResultsDataSourceLiveData = MutableLiveData<SearchResultsDataSource>()

    override fun create(): DataSource<Int, SearchResult> {
        return SearchResultsDataSource(gitHubRepository, searchQuery).also {
            searchResultsDataSourceLiveData.postValue(it)
        }
    }
}