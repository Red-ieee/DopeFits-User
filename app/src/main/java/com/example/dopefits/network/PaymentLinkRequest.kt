package com.example.dopefits.network

data class PaymentLinkRequest(
    val amount: Int,
    val description: String,
    val redirect: Redirect
)

data class Redirect(
    val success: String,
    val failed: String
)