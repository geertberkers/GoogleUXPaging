package com.example.android.codelabs.paging.ui

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

/**
 * Created by Zorgkluis (Geert Berkers)
 */
/**
 * Adapter which displays a loading spinner when `state = LoadState.Loading`, and an error
 * message and retry button when `state is LoadState.Error`.
 */
class MyLoadStateAdapter(
        private val retry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
            LoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
            holder.bind(loadState)
}