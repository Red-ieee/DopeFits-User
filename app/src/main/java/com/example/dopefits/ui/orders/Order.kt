// Order.kt
package com.example.dopefits.ui.orders

data class Order(
    val orderId: String = "",
    val orderDate: String = "",
    val orderStatus: String = "",
    val orderTotal: String = "",
    val productName: String = "",
    val productImage: List<String> = emptyList() // Ensure this property is a list
)