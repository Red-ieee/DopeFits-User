package com.example.dopefits.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dopefits.R
import com.google.android.material.button.MaterialButton

class ConfirmationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirmation, container, false)

        val backButton: MaterialButton = view.findViewById(R.id.back_button)
        val btnCashOnDelivery: Button = view.findViewById(R.id.btn_cash_on_delivery)
        val btnEwalletPayment: Button = view.findViewById(R.id.btn_ewallet_payment)

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        btnCashOnDelivery.setOnClickListener {
            findNavController().navigate(R.id.action_confirmationFragment_to_cashOnDeliveryFragment)
        }

        btnEwalletPayment.setOnClickListener {
            val bundle = arguments
            findNavController().navigate(R.id.action_confirmationFragment_to_paymentFragment, bundle)
        }

        return view
    }
}