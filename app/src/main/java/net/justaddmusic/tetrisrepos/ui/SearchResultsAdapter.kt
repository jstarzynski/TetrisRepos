package net.justaddmusic.tetrisrepos.ui

import android.arch.paging.PagedListAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.justaddmusic.tetrisrepos.R
import net.justaddmusic.tetrisrepos.model.SearchRequestState
import net.justaddmusic.tetrisrepos.model.SearchResult
import net.justaddmusic.tetrisrepos.utils.Utils

class SearchResultsAdapter : PagedListAdapter<SearchResult, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private val ITEM_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var searchRequestState = SearchRequestState.LOADING

    class SearchResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val repoName: TextView = itemView.findViewById(R.id.repoName)
        val userName: TextView = itemView.findViewById(R.id.userName)
        val repoSize: TextView = itemView.findViewById(R.id.repoSize)
        val wiki: TextView = itemView.findViewById(R.id.wiki)
    }

    class SearchResultFooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if (viewType == ITEM_VIEW_TYPE)
                SearchResultsViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_item_search_result, parent, false))
            else
                SearchResultFooterViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_item_search_footer, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is SearchResultsViewHolder) {

            val searchResult: SearchResult? = getItem(position)

            searchResult?.apply {
                holder.repoName.text = name
                holder.repoSize.text = Utils.humanReadableByteCount(size.toLong() * 1000)
                holder.userName.text = owner.login
                holder.wiki.visibility = if (hasWiki) View.VISIBLE else View.INVISIBLE
                (holder.itemView as? CardView)?.apply {
                    if (hasWiki) setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardBackgroundPrimaryColor))
                    else setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardBackgroundSecondaryColor))
                }
            }

        } else if (holder is SearchResultFooterViewHolder) {
            holder.message.apply {
                if (searchRequestState == SearchRequestState.ERROR)
                    text = context.getString(R.string.msg_request_error)
                else
                    text = context.getString(R.string.msg_loading)
            }
        }

    }

    override fun getItemViewType(position: Int): Int =
            if (position < super.getItemCount()) ITEM_VIEW_TYPE else FOOTER_VIEW_TYPE

    override fun getItemCount(): Int = super.getItemCount() + if (shouldShowFooter()) 1 else 0

    private fun shouldShowFooter(): Boolean =
            searchRequestState in arrayOf(SearchRequestState.LOADING, SearchRequestState.ERROR)

    fun setSearchRequestState(state: SearchRequestState) {
        searchRequestState = state
        notifyItemChanged(super.getItemCount())
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: SearchResult?, newItem: SearchResult?) = oldItem == newItem
        }
    }

}