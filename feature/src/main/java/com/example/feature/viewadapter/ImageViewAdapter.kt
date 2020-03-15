package com.example.feature.viewadapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageBase64")
fun ImageView.imageBase64(imageBase64: String) {
    val decodedString: ByteArray = Base64.decode(imageBase64, Base64.DEFAULT)
    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    setImageBitmap(decodedByte)
}
