package com.example.dopefits.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.dopefits.R

class PaymentFragment : Fragment() {

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

    private fun setupWebView() {
        paymentWebView.webViewClient = WebViewClient()
        paymentWebView.settings.javaScriptEnabled = true

        val paymentUrl = arguments?.getString("payment_url")
        paymentUrl?.let {
            paymentWebView.loadUrl(it)
        }
    }
}