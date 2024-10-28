package com.example.dopefits.network

data class PaymentLinkRequest(
    val data: PaymentLinkData
)

data class PaymentLinkData(
    val attributes: PaymentLinkAttributes
)

data class PaymentLinkAttributes(
    val amount: Int,
    val description: String,
    val remarks: String
)