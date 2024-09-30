package com.example.dopefits.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val brand: String = "",
    val categoryId: Int = 0,
    val description: String = "",
    val dimensions: String = "",
    val issue: String = "",
    val picUrl: List<String> = listOf(),
    val price: Double = 0.0,
    val size: String = "",
    val title: String = ""
) : Parcelable