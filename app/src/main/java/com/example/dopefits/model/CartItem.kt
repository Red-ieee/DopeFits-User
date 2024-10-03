package com.example.dopefits.model

data class CartItem(
    val productId: Int = 0,
    val quantity: Int = 1
) {
    // No-argument constructor for Firebase
    constructor() : this(0, 1)
}