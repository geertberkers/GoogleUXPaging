package com.example.android.codelabs.paging.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.android.codelabs.paging.R

/**
 * Created by Zorgkluis (Geert Berkers)
 */
class LoadStateViewHolder(
        parent: ViewGroup,
        retry: () -> Unit
) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.load_state_item, parent, false)
) {
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
    private val errorMsg: TextView = itemView.findViewById(R.id.error_msg)
    private val retry: Button = itemView.findViewById<Button>(R.id.retry_button).also {
        it.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        println("Binding LoadState: $loadState")
        if (loadState is LoadState.Error) {
            errorMsg.text = loadState.error.localizedMessage
        }
        progressBar.visibility = toVisibility(loadState == LoadState.Loading)
        retry.visibility = toVisibility(loadState != LoadState.Loading)
        errorMsg.visibility = toVisibility(loadState != LoadState.Loading)
    }

    private fun toVisibility(constraint: Boolean): Int = if (constraint) {
        println("LoadState: VISIBLE!")
        View.VISIBLE
    } else {
        println("LoadState: GONE!")
        View.GONE
    }
}