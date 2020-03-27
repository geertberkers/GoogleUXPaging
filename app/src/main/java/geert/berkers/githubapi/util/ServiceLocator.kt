/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geert.berkers.githubapi.util

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import geert.berkers.githubapi.api.GitHubApi
import geert.berkers.githubapi.api.GitHubApiService
import geert.berkers.githubapi.data.GithubRepository
import geert.berkers.githubapi.data.InMemoryByPageKeyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Super simplified service locator implementation to allow us to replace default implementations
 * for testing.
 */
interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                            app = context.applicationContext as Application,
                            useInMemoryDb = false)
                }
                return instance!!
            }
        }

        /**
         * Allows tests to replace the default implementations.
         */
        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getRepository(/*type: GithubRepository.Type*/): GithubRepository

    fun getDiskIODispatcher(): CoroutineDispatcher

    fun getGitHubApi(): GitHubApi
}

/**
 * default implementation of ServiceLocator that uses production endpoints.
 */
open class DefaultServiceLocator(val app: Application, val useInMemoryDb: Boolean) : ServiceLocator {

    // thread pool used for disk access
    @Suppress("PrivatePropertyName")
    private val DISK_IO = Dispatchers.IO


    private val api by lazy {
        GitHubApi.create()
    }

    override fun getRepository(/*type: RedditPostRepository.Type*/): GithubRepository =
        InMemoryByPageKeyRepository(
            githubApiService = GitHubApiService(getGitHubApi())
        )
//        return when (type) {
//            RedditPostRepository.Type.IN_MEMORY_BY_ITEM -> InMemoryByItemRepository(
//                    redditApi = getRedditApi()
//            )
//            RedditPostRepository.Type.IN_MEMORY_BY_PAGE -> InMemoryByPageKeyRepository(
//                    redditApi = getRedditApi()
//            )
//            RedditPostRepository.Type.DB -> DbRedditPostRepository(
//                    db = db,
//                    ioDispatcher = getDiskIODispatcher()
//            )
//        }
//    }

    override fun getDiskIODispatcher(): CoroutineDispatcher = DISK_IO

    override fun getGitHubApi(): GitHubApi = api
}