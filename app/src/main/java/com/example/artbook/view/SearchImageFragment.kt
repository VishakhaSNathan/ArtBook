package com.example.artbook.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.artbook.R
import com.example.artbook.adapter.ImageRecyclerAdapter
import com.example.artbook.databinding.FragmentImageApiBinding
import com.example.artbook.util.Status
import com.example.artbook.viewmodel.ArtViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchImageFragment @Inject constructor(
    private  val imageRecyclerAdapter: ImageRecyclerAdapter
): Fragment(R.layout.fragment_image_api) {

    lateinit var viewModel: ArtViewModel

    private var fragmentBinding: FragmentImageApiBinding?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)
        val binding = FragmentImageApiBinding.bind(view)
        fragmentBinding = binding


        var job: Job?=null
        binding.etSearchName.addTextChangedListener{
            job?.cancel()
            job = lifecycleScope.launch{
                delay(1000)
                it?.let {
                    if(it.toString().isNotEmpty())
                    {
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }

        subscribeToObservers()

        binding.rvSearchView.adapter = imageRecyclerAdapter
        binding.rvSearchView.layoutManager = GridLayoutManager(requireContext(),3)

        imageRecyclerAdapter.setOnClickItemListener {
            findNavController().popBackStack()
            viewModel.setSelectedImageUrl(it)

        }
    }

    fun subscribeToObservers(){
        viewModel.imageList.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS->{
                    val urls = it.data?.hits?.map { imageResult ->
                        imageResult.previewURL
                    }

                    imageRecyclerAdapter.images = urls?: listOf()
                    fragmentBinding?.progressBar?.visibility= View.GONE

                }
                Status.ERROR->{
                    Toast.makeText(requireContext(),"Error",Toast.LENGTH_LONG).show()
                    fragmentBinding?.progressBar?.visibility= View.GONE
                }
                Status.LOADING->{
                    Toast.makeText(requireContext(),"Error",Toast.LENGTH_LONG).show()
                    fragmentBinding?.progressBar?.visibility= View.VISIBLE
                }
            }
        })
    }
    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}