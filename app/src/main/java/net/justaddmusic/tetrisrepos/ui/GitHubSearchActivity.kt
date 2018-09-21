package net.justaddmusic.tetrisrepos.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_git_hub_search.*
import net.justaddmusic.tetrisrepos.R
import net.justaddmusic.tetrisrepos.model.SearchRequestState
import net.justaddmusic.tetrisrepos.viewmodel.GitHubSearchViewModel

class GitHubSearchActivity : AppCompatActivity() {

    private lateinit var viewAdapter: SearchResultsAdapter
    private lateinit var viewModel: GitHubSearchViewModel

    private lateinit var searchMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_hub_search)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.title_activity_git_hub_search)

        viewModel = ViewModelProviders.of(this).get(GitHubSearchViewModel::class.java)

        searchQueryChanged(getString(R.string.default_search_query))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_activity_menu, menu)
        searchMenuItem = menu.findItem(R.id.search)

        (searchMenuItem.actionView as SearchView).apply {
            queryHint = context.getString(R.string.hint_query)
            setOnQueryTextListener(SearchViewListener())
        }

        return true
    }

    private inner class SearchViewListener : SearchView.OnQueryTextListener {

        override fun onQueryTextChange(newText: String?) = true

        override fun onQueryTextSubmit(query: String?): Boolean {
            searchMenuItem.collapseActionView()
            return query?.let {
                searchQueryChanged(query)
                true
            } ?: false
        }
    }

    private fun searchQueryChanged(searchQuery: String) {
        if (viewModel.onNewSearchQuery(searchQuery)) {
            supportActionBar?.subtitle = searchQuery

            viewAdapter = SearchResultsAdapter()

            cardView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = viewAdapter
            }

            viewModel.searchResultsData.observe(this,
                    Observer { viewAdapter.submitList(it) })

            viewModel.getSearchRequestState().observe(this,
                    Observer { viewAdapter.setSearchRequestState(it ?: SearchRequestState.STANDBY) })
        }

    }

}
