package net.justaddmusic.tetrisrepos.model

import com.google.gson.annotations.SerializedName

data class SearchResult(
        val id: Int,
        val name: String,
        val size: Int,
        @SerializedName("has_wiki") val hasWiki: Boolean,
        val owner: RepoOwner
)