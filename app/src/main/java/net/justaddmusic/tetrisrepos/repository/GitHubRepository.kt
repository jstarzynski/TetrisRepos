package net.justaddmusic.tetrisrepos.repository

import net.justaddmusic.tetrisrepos.network.GitHubSearchService
import javax.inject.Inject

class GitHubRepository @Inject constructor(private val searchService: GitHubSearchService) {

    fun search(phrase: String, pageResultsLimit: Int, pageIndex: Int) =
            searchService.searchRepos(phrase, pageResultsLimit, pageIndex)

}