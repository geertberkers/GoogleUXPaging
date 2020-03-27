package geert.berkers.githubapi.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import geert.berkers.githubapi.R
import geert.berkers.githubapi.api.Item
import geert.berkers.githubapi.util.GlideRequests
/**
 * A RecyclerView ViewHolder that displays a user [Item]
 */
class UserItemViewHolder(
    view: View,
    private val glide: GlideRequests
) : RecyclerView.ViewHolder(view) {

    private val title: TextView = view.findViewById(R.id.title)
    private val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    private var item: Item? = null

    init {
        view.setOnClickListener {
            item?.htmlUrl?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(item: Item?) {
        this.item = item
        title.text = item?.login ?: "loading"

        if (item?.avatarUrl?.startsWith("http") == true) {
            thumbnail.visibility = View.VISIBLE
            glide.load(item.avatarUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .into(thumbnail)
        } else {
            thumbnail.visibility = View.GONE
            glide.clear(thumbnail)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests): UserItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
            return UserItemViewHolder(view, glide)
        }
    }
}