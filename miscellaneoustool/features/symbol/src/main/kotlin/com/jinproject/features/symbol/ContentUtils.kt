package com.jinproject.features.symbol

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toUri
import com.jinproject.design_ui.R

fun getBitmapFromContentUri(
    context: Context,
    imageUri: String,
): Bitmap = when {
    imageUri.isBlank() -> {
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.test,
        )
    }

    Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(context.contentResolver, imageUri.toUri())
        ) { decoder, _, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = true
        }
    }

    else -> {
        MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri.toUri())
    }
}
