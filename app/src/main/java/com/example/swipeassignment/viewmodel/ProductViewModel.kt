package com.example.swipeassignment.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeassignment.WorkManager.SyncManager
import com.example.swipeassignment.datamodel.ProductItems
import com.example.swipeassignment.repository.ProductRepository
import com.example.swipeassignment.api.apiresponse.ApiResponse
import kotlinx.coroutines.launch

// ViewModel
class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _fetchDataResponse = MutableLiveData<ApiResponse<List<ProductItems>>>()
    val fetchDataResponse: LiveData<ApiResponse<List<ProductItems>>> get() = _fetchDataResponse

    private val _postDataResponse = MutableLiveData<ApiResponse<Unit>>()
    val postDataResponse: LiveData<ApiResponse<Unit>> get() = _postDataResponse

    private val _notificationEvent = MutableLiveData<Pair<String, String>>()
    val notificationEvent: LiveData<Pair<String, String>> get() = _notificationEvent

    val searchQuery = MutableLiveData<String>()

    private val _productList = MutableLiveData<List<ProductItems>>()
    val productList: LiveData<List<ProductItems>> get() = _productList

    val productName = MutableLiveData<String>()
    val productPrice = MutableLiveData<String>()
    val productTax = MutableLiveData<String>()
    val selectedProductType = MutableLiveData<String>()
    val imageUri = MutableLiveData<Uri?>() // Holds selected image URI

    fun setSearchQuery(query: String) {
        searchQuery.value = query
        filterProducts(query)
    }

    private fun filterProducts(query: String) {
        val allProducts = (_fetchDataResponse.value as? ApiResponse.Success)?.data ?: emptyList()
        _productList.value = allProducts.filter { it.product_name.contains(query, ignoreCase = true) }
    }

    fun fetchDataFromApi() {
        _fetchDataResponse.value = ApiResponse.Loading
        viewModelScope.launch {
            val result = repository.fetchData()
            _fetchDataResponse.value = result

            when (result) {
                is ApiResponse.Success -> {
                    _productList.value = result.data
                    _notificationEvent.postValue("Success" to "Data fetched successfully")
                }
                is ApiResponse.Error -> _notificationEvent.postValue("Error" to result.message)
                else -> {}
            }
        }
    }

    fun postData(data: ProductItems, context: Context) {
        _postDataResponse.value = ApiResponse.Loading
        viewModelScope.launch {
            val result = repository.postData(data)
            _postDataResponse.value = result
            if (result is ApiResponse.Success) {
                Log.d("MainActivity", "Data posted successfully")
                _notificationEvent.postValue("Success" to "Data posted successfully")
            } else if (result is ApiResponse.Error) {
                Log.d("MainActivity", "Error posting data: ${result.message}")
                _notificationEvent.postValue("Error" to result.message)
                SyncManager.scheduleSync(context) // Schedule sync when online
            }
        }
    }
    fun onFabClicked() {
        _notificationEvent.postValue("Action" to "Floating button clicked!")
    }
    private val _userAddedData = MutableLiveData<ApiResponse<List<ProductItems>>>()
    val userAddedData: LiveData<ApiResponse<List<ProductItems>>> get() = _userAddedData
    fun fetchUserAddedDataFromDb(){
        _userAddedData.value = ApiResponse.Loading
        viewModelScope.launch {
            val result = repository.fetchUserAddedData()
            _userAddedData.value = result
        }
    }

}