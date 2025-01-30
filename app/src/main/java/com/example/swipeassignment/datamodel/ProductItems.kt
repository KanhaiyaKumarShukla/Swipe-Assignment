package com.example.swipeassignment.datamodel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ProductItems(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerializedName("image") val image: String = "",  // Default to empty
    @SerializedName("price") val price: Double = 0.0,
    @SerializedName("product_name") val product_name: String = "",
    @SerializedName("product_type") val product_type: String = "",
    @SerializedName("tax") val tax: Double = 0.0,
    val isPending: Boolean = false
) : Parcelable

