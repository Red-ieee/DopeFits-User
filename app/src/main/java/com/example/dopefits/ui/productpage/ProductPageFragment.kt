package com.example.dopefits.ui.productpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.navigation.fragment.findNavController
import com.example.dopefits.R
import com.example.dopefits.model.Product
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProductPageFragment : Fragment() {

    private lateinit var product: Product
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getParcelable("product") ?: run {
                Toast.makeText(requireContext(), "Product data is missing", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
                return
            }
            Log.d("ProductPageFragment", "Initialized product with ID ${product.id}")
        }
        lifecycleScope.launch {
            fetchUserId()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_page, container, false)

        view.findViewById<TextView>(R.id.product_title).text = product.title
        view.findViewById<TextView>(R.id.product_price).text = product.price.toString()
        view.findViewById<TextView>(R.id.product_details).text = product.description

        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        viewPager.adapter = ImagePagerAdapter(product.picUrl)

        val addToCartButton = view.findViewById<Button>(R.id.add_to_cart_button)
        addToCartButton.setOnClickListener {
            userId?.let { id ->
                addToCart(id, product, addToCartButton)
            } ?: run {
                Toast.makeText(requireContext(), "User ID is missing", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }

    private fun fetchUserId() {
        try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                userId = currentUser.uid
                Log.d("ProductPageFragment", "Fetched userId: $userId")
            } else {
                Toast.makeText(requireContext(), "No logged-in user", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("ProductPageFragment", "Error fetching user ID", e)
            Toast.makeText(requireContext(), "Failed to fetch user ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfInCart(userId: String, product: Product, addToCartButton: Button) {
        lifecycleScope.launch {
            val database = FirebaseDatabase.getInstance()
            val cartRef = database.getReference("users/$userId/Cart").child(product.id.toString())
            val snapshot = withContext(Dispatchers.IO) { cartRef.get().await() }
            if (snapshot.exists()) {
                Log.d("ProductPageFragment", "Product with ID ${product.id} is already in the cart")
                addToCartButton.isEnabled = false
                Toast.makeText(requireContext(), "Item is already in the cart", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("ProductPageFragment", "Product with ID ${product.id} is not in the cart")
                addToCartButton.isEnabled = true
            }
        }
    }

    private fun addToCart(userId: String, product: Product, addToCartButton: Button) {
        lifecycleScope.launch {
            try {
                val database = FirebaseDatabase.getInstance()
                val userCartRef = database.getReference("users/$userId/Cart")
                val productRef = userCartRef.child(product.id.toString())
                val productSnapshot = withContext(Dispatchers.IO) { productRef.get().await() }

                if (!productSnapshot.exists()) {
                    // Ensure the product ID is correctly set
                    val productToAdd = product.copy(id = product.id)
                    Log.d("ProductPageFragment", "Adding product with ID ${productToAdd.id} to the cart")
                    withContext(Dispatchers.IO) { productRef.setValue(productToAdd).await() }
                    if (isAdded) {
                        Log.d("ProductPageFragment", "Added product with ID ${product.id} to the cart")
                        Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
                        addToCartButton.isEnabled = false
                    }
                } else {
                    if (isAdded) {
                        Log.d("ProductPageFragment", "Product with ID ${product.id} is already in the cart")
                        Toast.makeText(requireContext(), "Item is already in the cart", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("ProductPageFragment", "Failed to add product with ID ${product.id} to the cart", e)
                if (isAdded) {
                    Toast.makeText(requireContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(product: Product) =
            ProductPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("product", product)
                }
            }
    }
}