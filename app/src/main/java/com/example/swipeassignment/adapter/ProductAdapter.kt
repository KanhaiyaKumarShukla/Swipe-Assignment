package com.example.swipeassignment.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swipeassignment.databinding.ItemProductBinding
import com.example.swipeassignment.datamodel.ProductItems

class ProductAdapter(private val onClick: (ProductItems) -> Unit) :
    ListAdapter<ProductItems, ProductAdapter.ProductViewHolder>(DiffCallback()) {

    class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductItems, onClick: (ProductItems) -> Unit) {
            binding.product = product
            binding.root.setOnClickListener { onClick(product) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class DiffCallback : DiffUtil.ItemCallback<ProductItems>() {
        override fun areItemsTheSame(oldItem: ProductItems, newItem: ProductItems) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ProductItems, newItem: ProductItems) = oldItem == newItem
    }
}
