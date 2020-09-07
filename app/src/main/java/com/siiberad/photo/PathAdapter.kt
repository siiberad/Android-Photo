package com.siiberad.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_path.view.*

class PathAdapter(private val data: List<String>) : RecyclerView.Adapter<PathAdapter.Holder>() {
    var mOnItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onClick(path: String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_path, parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onClick(data[position])
        }
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(path: String) {
            Picasso.with(itemView.context)
                .load(path.toUri())
                .into(view.imageview)
        }
    }
}