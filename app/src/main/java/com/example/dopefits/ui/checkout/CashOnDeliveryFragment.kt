// File: app/src/main/java/com/example/dopefits/ui/checkout/CashOnDeliveryFragment.kt
package com.example.dopefits.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dopefits.R
import com.google.android.material.button.MaterialButton

class CashOnDeliveryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cash_on_delivery, container, false)

        val backButton: MaterialButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }
}