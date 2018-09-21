package net.justaddmusic.tetrisrepos.network

import net.justaddmusic.tetrisrepos.model.SearchItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface GitHubSearchService {

    @GET("search/repositories")
    fun searchRepos(@Query("q") searchPhrase: String, @Query("per_page") resultsPerPage: Int, @Query("page") pageIndex: Int): Call<SearchItems>

}