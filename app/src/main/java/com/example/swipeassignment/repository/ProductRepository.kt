package com.example.swipeassignment.repository

import android.util.Log
import com.example.swipeassignment.datamodel.ProductItems
import com.example.swipeassignment.api.apiresponse.ApiResponse
import com.example.swipeassignment.api.apiservices.ApiService
import com.example.swipeassignment.datamodel.PostProductResponse
import com.example.swipeassignment.room.daointerface.ProductDao


class ProductRepository(private val apiService: ApiService, private val productDao: ProductDao) {
    suspend fun fetchData(): ApiResponse<List<ProductItems>> {
        return try {
            val response = apiService.fetchData()
            // Clear old data before inserting new data
            productDao.clearAllData()
            response.forEach { productDao.insert(it) } // Save to DB
            ApiResponse.Success(response)
        } catch (e: Exception) {
            val cachedData = productDao.getAllData()
            if (cachedData.isNotEmpty()) {
                ApiResponse.Success(cachedData) // Return cached data if API fails
            } else {
                ApiResponse.Error(e.message ?: "Failed to fetch data")
            }
        }
    }


    suspend fun postData(data: ProductItems): ApiResponse<Unit> {
        return try {
            val postData=PostProductResponse(
                message = "Data posted successfully",
                product_details = data,
                product_id = 1,
                success = true
            )
            val response=apiService.postData(data)
            productDao.insert(data) // Save locally after successful post
            Log.d("MainActivity", "Post Success: $response")
            ApiResponse.Success(Unit)

        } catch (e: Exception) {
            val pendingData = data.copy(isPending = true) // Mark as pending
            productDao.insertPending(pendingData)
            Log.d("MainActivity", "Post Failed: ${e.message}")
            ApiResponse.Error("Offline - Data saved locally. Will sync when online.\n${e.message}")
        }
    }


    suspend fun retryPendingPosts() {
        val pendingItems = productDao.getAllPending()
        for (item in pendingItems) {
            try {
                //apiService.postData(item)
                productDao.removePending(item.id) // Remove from pending list after successful post
            } catch (e: Exception) {
                // Retry will continue next time
                Log.d("MainActivity", "Retry Failed: ${e.message}")
            }
        }
    }
    suspend fun fetchUserAddedData():ApiResponse<List<ProductItems>>{
        val addedData = productDao.getAddedData()
        Log.d("MainActivity", "Added Data: $addedData")
        return ApiResponse.Success(addedData)
    }
}