package com.example.swipeassignment.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.swipeassignment.R
import com.example.swipeassignment.databinding.BottomSheetAddProductBinding
import com.example.swipeassignment.datamodel.ProductItems
import com.example.swipeassignment.viewmodel.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import kotlin.getValue
import android.database.Cursor
import android.provider.OpenableColumns
import android.util.Log
import com.example.swipeassignment.notification.showNotification
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody

class AddProductBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: ProductViewModel by viewModel()
    private lateinit var binding: BottomSheetAddProductBinding
    private var imageUri: Uri? = null
    private var imageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate layout and bind with DataBinding
        binding = BottomSheetAddProductBinding.inflate(inflater, container, false)

        // Set up the ViewModel and a new product object for binding
        val product = ProductItems() // New product instance
        binding.viewModel = viewModel
        binding.product = product
        binding.lifecycleOwner = viewLifecycleOwner

        // Set up spinner for product types
        val productTypes = arrayOf("Electronics", "Clothing", "Accessories", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, productTypes)
        binding.spinnerProductType.adapter = adapter

        // Image Picker
        binding.productImageView.setOnClickListener {
            pickImage()
        }

        viewModel.notificationEvent.observe(viewLifecycleOwner) { (title, message) ->
            showNotification(requireContext(), title, message)
        }

        // Post Button
        binding.btnPostProduct.setOnClickListener {
            val name = binding.etProductName.text.toString()
            val price = binding.etProductPrice.text.toString().toDoubleOrNull() ?: 0.0
            val tax = binding.etProductTax.text.toString().toDoubleOrNull() ?: 0.0
            val productType = binding.spinnerProductType.selectedItem.toString()

            //Log.d("MainActivity", "Post button clicked: Name: $name, Price: $price, Tax: $tax, Product Type: $productType")
            if (name.isNotEmpty() && price > 0) {
                val newProduct = product.copy(
                    product_type = productType,
                    image = imageUri?.toString() ?: "" // Save image URI as a string
                )
                Log.d("MainActivity", "New Product: $newProduct")
                viewModel.postData(newProduct, requireContext()) // Call post function from ViewModel
                // dismiss() // Close bottom sheet
            } else {
                Toast.makeText(requireContext(), "Enter valid details", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            binding.productImageView.setImageURI(imageUri) // Display picked image
            uploadImageToCloudinary(imageUri) // Upload image to Cloudinary
        }
    }



    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    private fun uploadImageToCloudinary(uri: Uri?) {
        Log.d("MainActivity", "uploadImageToCloudinary called with URI: $uri")
        if (uri != null) {
            val filePath = getRealPathFromURI(uri)
            if (filePath == null) {
                Log.e("Upload", "Invalid file path")
                return
            }

            val file = File(filePath)
            val mediaType = "image/*".toMediaType()
            val requestBody = file.asRequestBody(mediaType)

            val requestBodyMultipart = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name, requestBody)
                .addFormDataPart("upload_preset", "ImageStorage") // Replace with your Cloudinary preset
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/dvzwyzysj/image/upload")
                .post(requestBodyMultipart)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    Log.d("MainActivity", "Cloudinary upload failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val json = JSONObject(responseBody)
                        Log.d("MainActivity", "Cloudinary response: $json")
                        imageUrl = json.getString("secure_url")
                    }
                }
            })
        }
    }
    private fun getRealPathFromURI(uri: Uri): String? {
        Log.d("MainActivity", "getRealPathFromURI called with URI: $uri")
        var filePath: String? = null
        val cursor: Cursor? = context?.contentResolver?.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex != -1) {
                    filePath = context?.cacheDir?.absolutePath + "/" + it.getString(columnIndex)
                }
            } else {
                Log.e("getRealPathFromURI", "Cursor is empty")
            }
        }
        return filePath
    }



}

