package com.example.artbook.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.artbook.R
import com.example.artbook.databinding.FragmentArtDetailsBinding
import com.example.artbook.util.Status
import com.example.artbook.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    val glide : RequestManager
): Fragment(R.layout.fragment_art_details) {

    lateinit var viewModel : ArtViewModel
    var fragmentBinding : FragmentArtDetailsBinding ?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        val binding = FragmentArtDetailsBinding.bind(view)
        fragmentBinding = binding

        subscribeToObserver()

        binding.ivArtPic.setOnClickListener {
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToSearchImageFragment())
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.save.setOnClickListener {
            viewModel.makeArt(
                 artName = binding.etArtName.text.toString(),
                 artistName = binding.etArtistName.text.toString(),
                 year = binding.etPublishedYear.text.toString(),
                 description = binding.etDescription.text.toString()
            )
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToArtFragment())
        }
    }

    private fun subscribeToObserver(){
            viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer {url->
                fragmentBinding?.let {
                    glide.load(url).into(it.ivArtPic)
                }
            })

        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS->{
                    Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                    viewModel.resetArtMsg()
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(),"Error",Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentBinding = null
    }
}
