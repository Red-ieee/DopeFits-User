package com.example.dopefits.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.input.key.key
import androidx.compose.ui.semantics.text
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dopefits.R
import com.example.dopefits.adapter.CartAdapter
import com.example.dopefits.model.Product
import com.google.firebase.database.*

class CartFragment : Fragment() {

    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalPriceTextView: TextView
    private val products: MutableList<Product> = mutableListOf()
    private val productKeys: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.cart_recycler_view)
        totalPriceTextView = view.findViewById(R.id.total_price)
        recyclerView.layoutManager = LinearLayoutManager(context)
        cartAdapter = CartAdapter(products) { position -> removeFromCart(position) } // Modified to accept position
        recyclerView.adapter = cartAdapter
        loadCartItems()
        return view
    }

    private fun loadCartItems() {
        val database = FirebaseDatabase.getInstance()
        val cartRef = database.getReference("cart")
        cartRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val product = snapshot.getValue(Product::class.java)
                if (product != null) {
                    products.add(product)
                    productKeys.add(snapshot.key ?: "")
                    cartAdapter.notifyItemInserted(products.size - 1)
                    calculateTotalPrice()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val product = snapshot.getValue(Product::class.java)
                val key = snapshot.key
                val index = productKeys.indexOf(key)
                if (product != null && index != -1) {
                    products[index] = product
                    cartAdapter.notifyItemChanged(index)
                    calculateTotalPrice()
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                view?.post {
                    val key = snapshot.key
                    val index = productKeys.indexOf(key)
                    if (index != -1) {
                        products.removeAt(index)
                        productKeys.removeAt(index)
                        cartAdapter.notifyItemRemoved(index)
                        calculateTotalPrice()
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.e("CartFragment", "Failed to load cart items: ${error.message}")
            }
        })
    }

    private fun removeFromCart(position: Int) { // Modified to accept position
        if (position in 0 until products.size) {
            val productKey = productKeys[position]
            val database = FirebaseDatabase.getInstance()
            val cartRef = database.getReference("cart").child(productKey)
            cartRef.removeValue().addOnSuccessListener {
                if (products.isEmpty()) {
                    cartAdapter.notifyDataSetChanged() // Force refresh if cart is empty
                }
                // Item removal from local lists and adapter update is handled by onChildRemoved
            }.addOnFailureListener {
                // Handle failure to remove from Firebase (e.g., show error message)
            }
        }
    }

    private fun calculateTotalPrice() {
        val totalPrice = products.sumOf { it.price }
        totalPriceTextView.text = "Total: ₱$totalPrice"
    }
}