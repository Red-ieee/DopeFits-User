package com.example.dopefits.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val fullName: String,
    val username: String,
    val email: String,
    val phoneNumber: String
) : Parcelable