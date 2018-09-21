package net.justaddmusic.tetrisrepos.model

import com.google.gson.annotations.SerializedName

data class SearchItems(
        @SerializedName("total_count") val totalCount: Int,
        val items: List<SearchResult>
)