package com.example.swipeassignment.adapter

import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.core.widget.addTextChangedListener
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swipeassignment.datamodel.ProductItems
import com.example.swipeassignment.R

@BindingAdapter("app:text")
fun bindEditText(editText: EditText, text: MutableLiveData<String>?) {
    if (text == null) return

    // Avoid unnecessary updates to prevent infinite loops
    if (editText.text.toString() != text.value) {
        editText.setText(text.value)
        editText.setSelection(editText.text.length) // Move cursor to the end
    }

    // Remove old listener if exists
    editText.tag?.let { tag ->
        if (tag is android.text.TextWatcher) {
            editText.removeTextChangedListener(tag)
        }
    }

    // Add a new TextWatcher
    val textWatcher = object : android.text.TextWatcher {
        override fun afterTextChanged(editable: android.text.Editable?) {
            if (text.value != editable.toString()) {
                text.value = editable.toString()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    editText.addTextChangedListener(textWatcher)
    editText.tag = textWatcher // Save the TextWatcher reference
}


// Set text from Double to String
@BindingAdapter("android:text")
fun setDoubleToString(view: EditText, value: Double?) {
    val stringValue = value?.toString() ?: ""
    if (view.text.toString() != stringValue) {
        view.setText(stringValue)
    }
}

// Get text and convert it back to Double
@InverseBindingAdapter(attribute = "android:text")
fun getDoubleFromString(view: EditText): Double {
    return view.text.toString().toDoubleOrNull() ?: 0.0
}

// Listen for changes and update LiveData
@BindingAdapter("android:textAttrChanged")
fun setTextListener(view: EditText, listener: InverseBindingListener?) {
    if (listener != null) {
        view.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                listener.onChange()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}

@BindingAdapter("items")
fun setRecyclerViewItems(recyclerView: RecyclerView, items: List<ProductItems>?) {
    val adapter = recyclerView.adapter as? ProductAdapter
    items?.let { adapter?.submitList(it) }
}

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    if (url.isNullOrEmpty()) {
        // If the URL is empty, set the default image
        setImageResource(R.drawable.default_image)
    } else {
        Glide.with(this.context)
            .load(url)
            .placeholder(R.drawable.default_image) // Add a placeholder image
            .error(R.drawable.error_img) // Add an error image
            .into(this)
    }
}



