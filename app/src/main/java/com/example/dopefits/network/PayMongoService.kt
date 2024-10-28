package com.example.dopefits.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PayMongoService {
    @Headers(
        "accept: application/json",
        "content-type: application/json",
        "authorization: Basic c2tfdGVzdF96cERNN3JvNk51dHE3czc3ZjVxcEw3Z3Y6"
    )
    @POST("v1/links")
    fun createPaymentLink(@Body request: PaymentLinkRequest): Call<PaymentLinkResponse>
}