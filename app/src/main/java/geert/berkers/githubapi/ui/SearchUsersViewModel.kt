package geert.berkers.githubapi.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import geert.berkers.githubapi.data.GithubRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class SearchUsersViewModel(
    private val repository: GithubRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val KEY_GITHUB_USER = "github_user"
        const val DEFAULT_USER = "google"
    }

    init {
        if (!savedStateHandle.contains(KEY_GITHUB_USER)) {
            savedStateHandle.set(KEY_GITHUB_USER, DEFAULT_USER)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val items = savedStateHandle.getLiveData<String>(KEY_GITHUB_USER)
        .asFlow()
        .map { repository.searchUsers(it, 100) }
        .flatMapLatest { it }

    fun showUsers(searchQuery: String): Boolean {
        if (savedStateHandle.get<String>(KEY_GITHUB_USER) == searchQuery) {
            return false
        }
        savedStateHandle.set(KEY_GITHUB_USER, searchQuery)
        return true
    }
}
