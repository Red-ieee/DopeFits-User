package com.example.dopefits.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dopefits.R
import com.example.dopefits.activity.LoginActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var customerNameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        customerNameTextView = view.findViewById(R.id.customer_name)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Fetch user data
        fetchUserData()

        // My Account Dropdown
        val dropdownButton = view.findViewById<MaterialButton>(R.id.btn_my_account_dropdown)
        val dropdownContent = view.findViewById<LinearLayout>(R.id.dropdown_content)

        dropdownButton.setOnClickListener {
            // Toggle dropdown visibility
            dropdownContent.visibility = if (dropdownContent.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }

            // Rotate arrow icon
            val drawable = dropdownButton.compoundDrawables[2]  // Index 2 is for right drawable
            if (dropdownContent.visibility == View.VISIBLE) {
                drawable?.setLevel(1)  // Rotate arrow up
            } else {
                drawable?.setLevel(0)  // Rotate arrow down
            }
        }

        // Notifications Dropdown
        val notificationDropdownButton = view.findViewById<MaterialButton>(R.id.btn_notification_dropdown)
        val notificationDropdownContent = view.findViewById<LinearLayout>(R.id.notification_dropdown_content)

        notificationDropdownButton.setOnClickListener {
            // Toggle notification dropdown visibility
            notificationDropdownContent.visibility = if (notificationDropdownContent.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        // Order History Dropdown
        val orderHistoryDropdownButton = view.findViewById<MaterialButton>(R.id.btn_order_history_dropdown)
        val orderHistoryDropdownContent = view.findViewById<LinearLayout>(R.id.order_history_dropdown_content)

        orderHistoryDropdownButton.setOnClickListener {
            // Toggle order history dropdown visibility
            orderHistoryDropdownContent.visibility = if (orderHistoryDropdownContent.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        // Settings Dropdown
        val settingsDropdownButton = view.findViewById<MaterialButton>(R.id.btn_settings_dropdown)
        val settingsDropdownContent = view.findViewById<LinearLayout>(R.id.settings_dropdown_content)

        settingsDropdownButton.setOnClickListener {
            // Toggle settings dropdown visibility
            settingsDropdownContent.visibility = if (settingsDropdownContent.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        // Find the Log Out button and set an OnClickListener
        val logoutButton = view.findViewById<Button>(R.id.btn_logout)
        logoutButton.setOnClickListener {
            // Navigate back to LoginActivity
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            // Optionally, finish the current activity
            activity?.finish()
        }

        return view
    }

    private fun fetchUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("users").child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val firstName = dataSnapshot.child("firstName").getValue(String::class.java) ?: ""
                    val middleName = dataSnapshot.child("middleName").getValue(String::class.java) ?: ""
                    val lastName = dataSnapshot.child("lastName").getValue(String::class.java) ?: ""
                    val fullName = "$firstName $middleName $lastName"
                    customerNameTextView.text = fullName
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                }
            })
        }
    }
}