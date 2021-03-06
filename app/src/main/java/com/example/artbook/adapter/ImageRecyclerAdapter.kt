package com.example.artbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.artbook.R
import javax.inject.Inject

class ImageRecyclerAdapter @Inject constructor(
    val glide: RequestManager
) : RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder>() {

    private var onItemClickedListener: ((String)-> Unit)?=null

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffUtil = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDuffer  = AsyncListDiffer(this,diffUtil)

    var images : List<String>
        get() = recyclerListDuffer.currentList
        set(value) = recyclerListDuffer.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.image_row,parent,false
        )
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.singleArtImageView)

        val url = images[position]
        holder.itemView.apply {
            glide.load(url).into(imageView)
            setOnClickListener{
                onItemClickedListener?.let{
                    it(url)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setOnClickItemListener(listener: (String)->Unit){
        onItemClickedListener = listener
    }
}