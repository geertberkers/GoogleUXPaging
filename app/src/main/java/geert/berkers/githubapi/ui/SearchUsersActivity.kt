package geert.berkers.githubapi.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadType
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import geert.berkers.githubapi.R
import geert.berkers.githubapi.util.GlideApp
import geert.berkers.githubapi.util.ServiceLocator
import kotlinx.android.synthetic.main.activity_search_repositories.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SearchUsersActivity : AppCompatActivity() {

    private val model: SearchUsersViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val repo = ServiceLocator.instance(this@SearchUsersActivity).getRepository()

                @Suppress("UNCHECKED_CAST")
                return SearchUsersViewModel(repo, handle) as T
            }
        }
    }

    private lateinit var adapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_repositories)
        initAdapter()
        initSwipeToRefresh()
        initSearch()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initAdapter() {
        val glide = GlideApp.with(this)
        adapter = UsersAdapter(glide)
        list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ItemLoadStateAdapter(adapter),
            footer = ItemLoadStateAdapter(adapter)
        )

        adapter.addLoadStateListener { loadType: LoadType, loadState: LoadState ->
            if (loadType == LoadType.REFRESH) {
                swipe_refresh.isRefreshing = loadState == LoadState.Loading
            }
        }

        lifecycleScope.launch {
            model.items.collectLatest {

                // NOTE: The API DOES NOT allow filtering on username!
                //       But I still want to try to implement this function based on login or score.
                //       But it seems that the breakpoint does not occur.
                it.insertSeparators { item, item2 ->
                    if (item2 != null) {

                        if (item != null) {

                            if (item2.login!!.first() != item.login!!.first() ) {
                                return@insertSeparators item2.copy(login = item2.login!!.first().toString())
                            }
                        }
                    }
                    null
                }
                adapter.presentData(it)
            }
        }

    }

    private fun initSwipeToRefresh() {
        swipe_refresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun initSearch() {
        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updatedUsersFromInput()
                true
            } else {
                false
            }
        }
        input.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updatedUsersFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updatedUsersFromInput() {
        input.text.trim().toString().let {
            if (it.isNotEmpty() && model.showUsers(it)) {
                list.scrollToPosition(0)
                adapter.submitData(lifecycle, PagingData.empty())
            }
        }
    }
}