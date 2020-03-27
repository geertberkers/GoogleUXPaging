package geert.berkers.githubapi.ui

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class ItemLoadStateAdapter(
        private val adapter: UsersAdapter
) : LoadStateAdapter<NetworkStateItemViewHolder>() {
    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): NetworkStateItemViewHolder {
        return NetworkStateItemViewHolder.create(parent) { adapter.retry() }
    }
}