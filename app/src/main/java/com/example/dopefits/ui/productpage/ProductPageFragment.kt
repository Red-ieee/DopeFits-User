package com.example.dopefits.ui.productpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
//import androidx.core.os.bundleOf
import androidx.core.os.BundleCompat
import androidx.navigation.fragment.findNavController
import com.example.dopefits.R
import com.example.dopefits.model.Product
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductPageFragment : Fragment() {

    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = BundleCompat.getParcelable(it, "product", Product::class.java)!!
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
            // Handle add to cart
        }

        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            findNavController().navigateUp()
        }

        return view
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

//    companion object {
//        @JvmStatic
//        fun newInstance(product: Product) =
//            ProductPageFragment().apply {
//                arguments = bundleOf("product" to product)
//            }
//    }
}