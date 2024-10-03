package com.example.dopefits.ui.productpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.dopefits.R
import com.example.dopefits.model.Product
import com.example.dopefits.model.Cart
import com.example.dopefits.ui.productpage.ImagePagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductPageFragment : Fragment() {

    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getParcelable("product")!!
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

        view.findViewById<Button>(R.id.add_to_cart_button).setOnClickListener {
            addToCart(product)
        }

        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }

    private fun addToCart(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = FirebaseDatabase.getInstance()
                val cartRef = database.getReference("cart").push()
                cartRef.setValue(product)
                    .addOnSuccessListener {
                        CoroutineScope(Dispatchers.Main).launch {
                            if (isAdded) {
                                Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .addOnFailureListener {
                        CoroutineScope(Dispatchers.Main).launch {
                            if (isAdded) {
                                Toast.makeText(requireContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    if (isAdded) {
                        Toast.makeText(requireContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show()
                    }
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
}