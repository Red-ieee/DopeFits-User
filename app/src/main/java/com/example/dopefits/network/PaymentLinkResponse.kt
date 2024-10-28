package com.example.dopefits.network

data class PaymentLinkResponse(
    val data: PaymentLinkData
)

data class PaymentLinkData(
    val attributes: PaymentLinkAttributes
)

data class PaymentLinkAttributes(
    val checkout_url: String
)