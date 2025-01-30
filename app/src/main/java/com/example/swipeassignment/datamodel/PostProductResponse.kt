package com.example.swipeassignment.datamodel

data class PostProductResponse(
    val message: String,
    val product_details: ProductItems,
    val product_id: Int,
    val success: Boolean
)
