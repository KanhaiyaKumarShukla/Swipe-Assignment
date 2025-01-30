package com.example.swipeassignment.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipeassignment.adapter.ProductAdapter
import com.example.swipeassignment.api.apiresponse.ApiResponse
import com.example.swipeassignment.databinding.FragmentHomeBinding
import com.example.swipeassignment.notification.showNotification
import com.example.swipeassignment.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = ProductAdapter { product ->
            findNavController().navigate(HomeFragmentDirections.actionHomeToDetail(product))
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        viewModel.fetchDataFromApi()
        viewModel.fetchDataResponse.observe(viewLifecycleOwner) { response ->
            if (response is ApiResponse.Success) {
                Log.d("MainActivity", "Response: ${response.data}")
                adapter.submitList(response.data)
            }else if(response is ApiResponse.Error){
                //binding.errorText.text = response.message
                //binding.errorText.visibility = View.VISIBLE
            }
        }
        viewModel.notificationEvent.observe(viewLifecycleOwner) { (title, message) ->
            showNotification(requireContext(), title, message)
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setSearchQuery(s.toString()) // Update searchQuery manually
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.fab.setOnClickListener {
            AddProductBottomSheet().show(childFragmentManager, "AddProduct")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
