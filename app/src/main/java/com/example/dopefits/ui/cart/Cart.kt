package com.example.dopefits.model

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object Cart {
    private val db = Firebase.database.reference.child("cartItems")

    suspend fun addItem(product: Product) {
        val cartItemRef = db.child(product.id.toString())
        val snapshot = cartItemRef.get().await()
        if (!snapshot.exists()) {
            val newItem = CartItem(productId = product.id, quantity = 1)
            cartItemRef.setValue(newItem).await()
        }
    }

    suspend fun getItems(): List<CartItem> {
        val snapshot = db.get().await()
        return snapshot.children.mapNotNull { it.getValue(CartItem::class.java) }
    }
}