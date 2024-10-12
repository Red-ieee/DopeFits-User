package com.example.dopefits.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int = 0,
    val title: String = "",
    val categoryId: Int = 0,
    val condition: String = "",
    val description: String = "",
    val brand: String = "",
    val picUrl: List<String> = emptyList(),
    val price: Double = 0.0,
    val issue: String = "",
    val size: String = "",
    val dimensions: String = "",
    val stability: Int = 0,
    var quantity: Int = 0
) : Parcelable {
    // No-argument constructor required for Firebase
    constructor() : this(
        id = 0,
        title = "",
        categoryId = 0,
        condition = "",
        description = "",
        brand = "",
        picUrl = emptyList(),
        price = 0.0,
        issue = "",
        size = "",
        dimensions = "",
        stability = 0,
        quantity = 0
    )
}