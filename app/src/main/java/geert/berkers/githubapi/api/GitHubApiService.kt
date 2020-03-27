package geert.berkers.githubapi.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Zorgkluis (Geert Berkers)
 */
class GitHubApiService(private val githubApi: GitHubApi) {

    suspend fun searchUsers(query: String, page: Int, perPage: Int) =
        suspendCoroutine<UsersSearchResponse> { coroutine ->
            val request = githubApi.searchUsers(query, page, perPage)

            request.enqueue(object : Callback<UsersSearchResponse> {
                override fun onFailure(call: Call<UsersSearchResponse>, t: Throwable) {
                    coroutine.resumeWith(Result.failure(t))
                }

                override fun onResponse(call: Call<UsersSearchResponse>, response: Response<UsersSearchResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            coroutine.resume(it)
                        }
                    } else {
                        coroutine.resumeWith(Result.failure(Throwable("${response.code()}")))
//                        onError("error code: ${response.code()}")
                    }
                }
            })
        }

}
