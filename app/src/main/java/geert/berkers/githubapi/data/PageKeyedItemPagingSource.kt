package geert.berkers.githubapi.data

import androidx.paging.PagingSource
import geert.berkers.githubapi.api.GitHubApiService
import geert.berkers.githubapi.api.Item
import java.io.IOException

/**
 * Created by Zorgkluis (Geert Berkers)
 */
class PageKeyedItemPagingSource(
    private val githubApiService: GitHubApiService,
    private val searchQuery: String
) : PagingSource<Int, Item>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return try {

            val currentPage = params.key
            val nextPage = (currentPage ?: 0).inc()

            val data = githubApiService.searchUsers(
                query = searchQuery,
                page = nextPage,
                perPage = params.pageSize
            )

            LoadResult.Page(
                data = data.items ?: emptyList(),
                prevKey = currentPage,
                nextKey = nextPage.inc()
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }
}
