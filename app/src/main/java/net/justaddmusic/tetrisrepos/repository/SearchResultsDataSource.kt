package net.justaddmusic.tetrisrepos.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import net.justaddmusic.tetrisrepos.model.SearchItems
import net.justaddmusic.tetrisrepos.model.SearchRequestState
import net.justaddmusic.tetrisrepos.model.SearchResult
import net.justaddmusic.tetrisrepos.repository.GitHubRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SearchResultsDataSource(
        private val gitHubRepository: GitHubRepository,
        private val searchQuery: String)
    : PageKeyedDataSource<Int, SearchResult>() {

    private val retryExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    var state: MutableLiveData<SearchRequestState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, SearchResult>) {
        state.postValue(SearchRequestState.LOADING)
        gitHubRepository.search(searchQuery, params.requestedLoadSize, 1)
                .enqueue(object: Callback<SearchItems?> {

                    override fun onFailure(call: Call<SearchItems?>?, t: Throwable?) {
                        state.postValue(SearchRequestState.ERROR)
                        retryExecutor.schedule({ loadInitial(params, callback) }, 60, TimeUnit.SECONDS)
                    }

                    override fun onResponse(call: Call<SearchItems?>?, response: Response<SearchItems?>?) {
                        if (response?.isSuccessful == true) {
                            state.postValue(SearchRequestState.STANDBY)
                            callback.onResult(response.body()?.items ?: mutableListOf(), null, 2)
                        } else {
                            state.postValue(SearchRequestState.ERROR)
                            retryExecutor.schedule({ loadInitial(params, callback) }, 60, TimeUnit.SECONDS)
                        }

                    }
                })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SearchResult>) {
        state.postValue(SearchRequestState.LOADING)
        gitHubRepository.search(searchQuery, params.requestedLoadSize, params.key)
                .enqueue(object: Callback<SearchItems?> {

                    override fun onFailure(call: Call<SearchItems?>?, t: Throwable?) {
                        state.postValue(SearchRequestState.ERROR)
                        retryExecutor.schedule({ loadAfter(params, callback) }, 60, TimeUnit.SECONDS)
                    }

                    override fun onResponse(call: Call<SearchItems?>?, response: Response<SearchItems?>?) {
                        if (response?.isSuccessful == true) {
                            state.postValue(SearchRequestState.STANDBY)
                            callback.onResult(response.body()?.items ?: mutableListOf(), params.key + 1)
                        } else {
                            state.postValue(SearchRequestState.ERROR)
                            retryExecutor.schedule({ loadAfter(params, callback) }, 60, TimeUnit.SECONDS)
                        }
                    }
                })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SearchResult>) {}


}