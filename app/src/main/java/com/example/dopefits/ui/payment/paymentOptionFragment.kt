package com.example.dopefits.ui.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dopefits.R
import com.example.dopefits.com.example.dopefits.FCM.MyFirebaseMessagingService
import com.example.dopefits.model.Product
import com.example.dopefits.ui.orders.Order
import com.example.dopefits.network.PayMongoService
import com.example.dopefits.network.PaymentLinkRequest
import com.example.dopefits.network.PaymentLinkData
import com.example.dopefits.network.PaymentLinkAttributes
import com.example.dopefits.network.PaymentLinkResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentOptionFragment : Fragment() {

    private lateinit var paymongoButton: Button
    private lateinit var codButton: Button
    private lateinit var backButton: Button
    private var totalAmount: Int = 0
    private var selectedProducts: List<Product> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment_option, container, false)

        // Initialize buttons
        paymongoButton = view.findViewById(R.id.paymongo_button)
        codButton = view.findViewById(R.id.cod_button)
        backButton = view.findViewById(R.id.back_button)

        // Retrieve total amount and selected products if passed from CartFragment
        arguments?.let {
            totalAmount = it.getInt("total_amount") // Convert to cents
            selectedProducts = it.getParcelableArrayList("selected_products") ?: listOf()
        }

        // Set up button click listeners
        paymongoButton.setOnClickListener { startOnlinePayment() }
        codButton.setOnClickListener { proceedWithCOD() }
        backButton.setOnClickListener { findNavController().popBackStack() }


        return view
    }

    private fun startOnlinePayment() {
        Log.d("PaymentOptionFragment", "Total amount in PHP: $totalAmount")
        val orderId = saveOrderToFirebase(selectedProducts, totalAmount) ?: return
        val description = "Purchase of ${selectedProducts.size} items"
        val remarks = "sample remarks"

        val amountInCents = totalAmount*100

        // Retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.paymongo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PayMongoService::class.java)
        val request = PaymentLinkRequest(
            data = PaymentLinkData(
                attributes = PaymentLinkAttributes(
                    amount = amountInCents,  // Amount in cents
                    description = description,
                    remarks = remarks
                )
            )
        )

        service.createPaymentLink(request).enqueue(object : Callback<PaymentLinkResponse> {
            override fun onResponse(call: Call<PaymentLinkResponse>, response: Response<PaymentLinkResponse>) {
                if (response.isSuccessful) {
                    Log.d("PaymentOptionFragment", "Payment link created successfully")
                    val paymentLink = response.body()?.data?.attributes?.checkout_url
                    paymentLink?.let {
                        removePurchasedItems(selectedProducts)
                        val selectedProductIds = selectedProducts.map { it.id.toString() }
                        val bundle = Bundle().apply {
                            putString("payment_url", it)
                            putStringArrayList("selected_product_ids", ArrayList(selectedProductIds))
                        }
                        findNavController().navigate(R.id.action_paymentOptionFragment_to_paymentFragment, bundle)
                    }
                } else {
                    Log.e("PaymentOptionsFragment", "Failed to create payment link: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PaymentLinkResponse>, t: Throwable) {
                Log.e("PaymentOptionsFragment", "Error creating payment link", t)
            }
        })
    }

    private fun proceedWithCOD() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("PaymentOptionFragment", "User not logged in")
            return // Handle user not logged in scenario
        }

        val orderId = saveOrderToFirebase(selectedProducts, totalAmount)?: return

        removePurchasedItems(selectedProducts)

        // Show a confirmation message
        AlertDialog.Builder(requireContext())
            .setTitle("Order Confirmation")
            .setMessage("Your order has been placed successfully!")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                findNavController().navigate(R.id.action_paymentOptionFragment_to_homeFragment) // Navigate to your desired destination
            }
            .show()
    }

    private fun saveOrderToFirebase(products: List<Product>, totalAmount: Int): String? {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference
        val orderId = database.child("orders").child(userId!!).push().key // Assuming userId is not null here

        if (orderId != null) {
            val orderDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val productNames = products.joinToString(", ") { it.title }
            val productImages = products.flatMap { it.picUrl }

            val order = Order(
                orderId = orderId,
                orderDate = orderDate,
                orderStatus = "Pending",
                orderTotal = totalAmount.toString(),
                productName = productNames,
                productImage = productImages
            )
            database.child("orders").child(userId).child(orderId).setValue(order)
                .addOnSuccessListener {
                    Log.d("PaymentOptionFragment", "Order saved to Firebase: $order")
                }
                .addOnFailureListener {
                    Log.e("PaymentOptionFragment", "Failed to save order to Firebase", it)
                }
        }
        return orderId
    }

    private fun removePurchasedItems(purchasedProducts: List<Product>) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val database = FirebaseDatabase.getInstance().reference
            val cartRef = database.child("users").child(userId).child("Cart")

            purchasedProducts.forEach { product ->
                cartRef.child(product.id.toString()).removeValue()
                    .addOnSuccessListener {
                        Log.d("CartFragment", "Product removed from cart: ${product.title}")
                    }
                    .addOnFailureListener {
                        Log.e("CartFragment", "Failed to remove product from cart: ${product.title}", it)
                    }
            }
        } else {
            Log.e("CartFragment", "User not logged in")
        }
    }
}
