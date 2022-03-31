package com.example.artbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.artbook.R
import com.example.artbook.roomdb.Art
import javax.inject.Inject

class ArtRecyclerAdapter @Inject constructor(
    val glide : RequestManager
) : RecyclerView.Adapter<ArtRecyclerAdapter.ArtViewHolder>() {

    class ArtViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){}

    private val diffUtil = object : DiffUtil.ItemCallback<Art>(){
        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDuffer  = AsyncListDiffer(this,diffUtil)

    var arts : List<Art>
    get() = recyclerListDuffer.currentList
    set(value) = recyclerListDuffer.submitList(value)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.art_row,parent,false
        )
        return  ArtViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.ivArtRow)
        val artNameText = holder.itemView.findViewById<TextView>(R.id.tvRowArtName)
        val artistNameText = holder.itemView.findViewById<TextView>(R.id.tvRowArtistName)
        val descriptionText = holder.itemView.findViewById<TextView>(R.id.tvRowDescription)
        val yearText = holder.itemView.findViewById<TextView>(R.id.tvRowPublishedYear)

        val art = arts[position]
        holder.itemView.apply {
            glide.load(art.imageUrl).into(imageView)
            artNameText.text =  "ArtName : ${art.Artname}"
            artistNameText.text =  "ArtistName : ${art.ArtistName}"
            descriptionText.text =  "Description : ${art.description}"
            yearText.text =  "Year : ${art.year}"

        }
    }

    override fun getItemCount(): Int {
       return arts.size
    }
}