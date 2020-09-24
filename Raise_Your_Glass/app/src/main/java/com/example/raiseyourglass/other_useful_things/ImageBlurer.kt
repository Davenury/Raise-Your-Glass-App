package com.example.raiseyourglass.other_useful_things

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.raiseyourglass.R
import jp.wasabeef.glide.transformations.BlurTransformation

object ImageBlurer {

    fun setImageToBlur(view: ImageView, imageToBlur: Int, context: Context){
        Glide.with(context)
            .load(imageToBlur)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
            .into(view)
    }
}