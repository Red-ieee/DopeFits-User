// OrdersFragment.kt
package com.example.dopefits.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dopefits.R
import com.example.dopefits.adapter.orders.OrdersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrdersFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter
    private val ordersList = mutableListOf<Order>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)

        ordersRecyclerView = view.findViewById(R.id.orders_recycler_view)
        ordersRecyclerView.layoutManager = LinearLayoutManager(context)
        ordersAdapter = OrdersAdapter(ordersList)
        ordersRecyclerView.adapter = ordersAdapter

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Fetch orders data
        fetchOrdersData()

        return view
    }

    private fun fetchOrdersData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("orders").child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    ordersList.clear()
                    for (orderSnapshot in dataSnapshot.children) {
                        val order = orderSnapshot.getValue(Order::class.java)
                        order?.let { ordersList.add(it) }
                    }
                    ordersAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                }
            })
        }
    }
}