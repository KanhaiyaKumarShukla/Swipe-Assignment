package com.example.swipeassignment.di

import androidx.room.Room
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.swipeassignment.repository.ProductRepository
import com.example.swipeassignment.api.apiservices.ApiService
import com.example.swipeassignment.room.database.ProductDatabase
import com.example.swipeassignment.viewmodel.ProductViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf

val appModule: Module = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://app.getswipe.in/api/public/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), ProductDatabase::class.java, "product_database").build()
    }

    single {
        get<ProductDatabase>().productDao()
    }

    single {
        ProductRepository(get(), get())
    }

    viewModelOf(::ProductViewModel)
}