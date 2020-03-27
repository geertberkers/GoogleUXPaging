package geert.berkers.githubapi.api

import com.google.gson.annotations.SerializedName

/**
 * Created by Zorgkluis (Geert Berkers)
 */
data class UsersSearchResponse(
    @SerializedName("total_count")
    var totalCount: Int? = null,
    @SerializedName("incomplete_results")
    var incompleteResults: Boolean? = null,
    @SerializedName("items")
    var items: List<Item>? = null
)
