package com.example.swipeassignment.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.swipeassignment.databinding.ActivityMainBinding
import com.example.swipeassignment.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: ProductViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check if the system is running Android 12 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Show splash screen for a default time before transitioning to your main content
            window.setDecorFitsSystemWindows(false)
            val splashScreen = splashScreen
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // Customize splash screen exit animation if necessary
                splashScreenView.remove()
            }
        }

        // Inflate the layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE)
            } else {
                // Permission already granted
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
            }
        }
        /*

        val fetchButton: Button = findViewById(R.id.fetchButton)
        val postButton: Button = findViewById(R.id.postButton)

        fetchButton.setOnClickListener {
            viewModel.fetchDataFromApi()
            Toast.makeText(this, "Fetching data...", Toast.LENGTH_SHORT).show()
        }

        postButton.setOnClickListener {
            val newData = ProductItems(
                //image = "",
                price = 119.99,
                product_name = "New Product",
                product_type = "Electronicsss",
                tax = 5.0
            )
            viewModel.postData(newData, this)
            Toast.makeText(this, "Posting data...", Toast.LENGTH_SHORT).show()
        }
        var total=0
        viewModel.fetchDataResponse.observe(this) { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val data = response.data
                    Log.d("MainActivity", "Data fetched successfully: $data")
                    data.forEach {
                        total++
                    }
                    Log.d("MainActivity", "Pending Data: ${data.filter { it.product_type == "Electronicsss" }}")
                    Log.d("MainActivity", "Data: $total")
                    findViewById<TextView>(R.id.text).text = data.toString()
                }
                is ApiResponse.Error -> {
                    Log.e("MainActivity", "Error fetching data: ${response.message}")
                    // Toast.makeText(this, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
        viewModel.notificationEvent.observe(this) { (title, message) ->
            showNotification(this, title, message)
        }

         */


    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}