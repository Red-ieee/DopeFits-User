package com.example.dopefits.ui.payment

import BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.dopefits.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PaymentFragment : BaseFragment() {

    private lateinit var paymentWebView: WebView
    private lateinit var backButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)
        paymentWebView = view.findViewById(R.id.payment_webview)
        backButton = view.findViewById(R.id.back_button)
        setupWebView()
        setupBackButton()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )
        hideBottomNav()
    }

    private fun setupWebView() {
        paymentWebView.webViewClient = WebViewClient()
        paymentWebView.settings.javaScriptEnabled = true

        val paymentUrl = arguments?.getString("payment_url")
        paymentUrl?.let {
            paymentWebView.loadUrl(it)
        }
    }

    private fun setupBackButton() {
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun hideBottomNav() {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNav()
    }

    private fun showBottomNav() {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.visibility = View.VISIBLE
    }
}