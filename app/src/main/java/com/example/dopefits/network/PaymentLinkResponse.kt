// PaymentLinkResponse.kt
package com.example.dopefits.network

data class PaymentLinkResponse(
    val data: PaymentLinkResponseData
)

data class PaymentLinkResponseData(
    val id: String,
    val type: String,
    val attributes: PaymentLinkResponseAttributes
)

data class PaymentLinkResponseAttributes(
    val amount: Int,
    val archived: Boolean,
    val currency: String,
    val description: String,
    val livemode: Boolean,
    val fee: Int,
    val remarks: String,
    val status: String,
    val tax_amount: Any?,
    val taxes: List<Any>,
    val checkout_url: String,
    val reference_number: String,
    val created_at: Long,
    val updated_at: Long,
    val payments: List<Any>
)