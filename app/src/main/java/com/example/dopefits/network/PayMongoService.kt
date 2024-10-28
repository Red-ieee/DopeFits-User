package com.example.dopefits.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PayMongoService {
    @Headers("Authorization: Basic pk_test_q9AKnMVCh1qGaAZjRT6kXhio")
    @POST("v1/payment_links")
    fun createPaymentLink(@Body request: PaymentLinkRequest): Call<PaymentLinkResponse>
}