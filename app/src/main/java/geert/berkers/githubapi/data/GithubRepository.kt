package geert.berkers.githubapi.data

import androidx.paging.PagingData
import geert.berkers.githubapi.api.Item
import kotlinx.coroutines.flow.Flow

/**
 * Created by Zorgkluis (Geert Berkers)
 */
interface GithubRepository {
    fun searchUsers(searchQuery: String, pageSize: Int): Flow<PagingData<Item>>
}