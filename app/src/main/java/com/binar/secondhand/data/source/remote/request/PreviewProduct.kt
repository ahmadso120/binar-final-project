package com.binar.secondhand.data.source.remote.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class PreviewProduct(
    var productName :String,
    var productPrice : Int,
    var productDescription: String,
    var category :String,
    var location : String,
    var file : File?,
    val isBackCamera: Boolean,
    val isGalery : Boolean
):Parcelable