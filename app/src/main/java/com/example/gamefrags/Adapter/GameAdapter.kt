package com.example.gamefrags.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamefrags.R
import com.example.gamefrags.model.GameItem


class GameAdapter:RecyclerView.Adapter<GameAdapter.MyViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<GameItem>(){
        override fun areItemsTheSame(oldItem: GameItem, newItem: GameItem): Boolean {
            return oldItem.game_url == newItem.game_url
        }

        override fun areContentsTheSame(oldItem: GameItem, newItem: GameItem): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentGame = differ.currentList[position]
        holder.itemView.apply {
            holder.itemView.findViewById<TextView>(R.id.tvTitle).text = currentGame.title
            holder.itemView.findViewById<TextView>(R.id.tvDescription).text = currentGame.short_description
            holder.itemView.findViewById<TextView>(R.id.tvDev).text = currentGame.developer
            holder.itemView.findViewById<TextView>(R.id.tvPublishedAt).text = currentGame.release_date

            Glide.with(this)
                .load(currentGame.thumbnail)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.itemView.findViewById(R.id.ivGameImage))
            setOnClickListener {
                onItemClickListener?.let { it(currentGame) }
            }
        }
    }

    private var onItemClickListener: ((GameItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (GameItem) -> Unit) {
        onItemClickListener = listener
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}
}