package com.example.dopefits.ui.payment

import BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.dopefits.R

class PaymentFragment : BaseFragment() {

    private lateinit var paymentWebView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)
        paymentWebView = view.findViewById(R.id.payment_webview)
        setupWebView()
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
    }

    private fun setupWebView() {
        paymentWebView.webViewClient = WebViewClient()
        paymentWebView.settings.javaScriptEnabled = true

        val paymentUrl = arguments?.getString("payment_url")
        paymentUrl?.let {
            paymentWebView.loadUrl(it)
        }
    }
}