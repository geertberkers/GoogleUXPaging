package geert.berkers.githubapi.data

import androidx.paging.PagingConfig
import androidx.paging.PagingDataFlow
import geert.berkers.githubapi.api.GitHubApiService

/**
 * Created by Zorgkluis (Geert Berkers)
 */
class InMemoryByPageKeyRepository(private val githubApiService: GitHubApiService) : GithubRepository {

    override fun searchUsers(searchQuery: String, pageSize: Int) =
        PagingDataFlow(PagingConfig(pageSize)) {
            PageKeyedItemPagingSource(
                githubApiService = githubApiService,
                searchQuery = searchQuery
            )
        }
}

