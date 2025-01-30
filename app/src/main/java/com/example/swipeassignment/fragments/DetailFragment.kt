package com.example.swipeassignment.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.swipeassignment.R
import com.example.swipeassignment.databinding.FragmentDetailBinding
import com.example.swipeassignment.datamodel.ProductItems

class DetailFragment : Fragment() {

    private var product: ProductItems? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get the product argument from the bundle
        arguments?.let {
            product = it.getParcelable("product")
        }

        val binding = FragmentDetailBinding.inflate(inflater, container, false)

        // Set the product details in the view
        product?.let {
            binding.product = it
        }

        return binding.root
    }
}
