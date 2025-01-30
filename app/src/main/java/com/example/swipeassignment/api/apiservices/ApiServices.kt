package com.example.swipeassignment.api.apiservices

import com.example.swipeassignment.datamodel.PostProductResponse
import com.example.swipeassignment.datamodel.ProductItems
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Retrofit API Interface
interface ApiService {
    @GET("get")
    suspend fun fetchData(): List<ProductItems>

    @POST("add")
    suspend fun postData(@Body data: ProductItems)//  : PostProductResponse
}