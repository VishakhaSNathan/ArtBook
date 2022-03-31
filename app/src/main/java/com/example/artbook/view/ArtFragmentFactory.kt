package com.example.artbook.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.example.artbook.adapter.ArtRecyclerAdapter
import com.example.artbook.adapter.ImageRecyclerAdapter
import javax.inject.Inject

class ArtFragmentFactory @Inject constructor(
    private val glide : RequestManager,
    private val artRecyclerAdapter: ArtRecyclerAdapter,
    private val imageRecyclerAdapter: ImageRecyclerAdapter
): FragmentFactory()
{
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when(className){
            ArtFragment::class.java.name -> ArtFragment(artRecyclerAdapter)
            ArtDetailsFragment::class.java.name -> ArtDetailsFragment(glide)
            SearchImageFragment::class.java.name ->  SearchImageFragment(imageRecyclerAdapter)
            else -> super.instantiate(classLoader, className)
        }

    }
}