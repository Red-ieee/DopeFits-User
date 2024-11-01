package com.example.dopefits.adapter.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dopefits.R
import com.example.dopefits.ui.orders.Order

class OrdersAdapter(private val ordersList: List<Order>) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]
        holder.orderIdTextView.text = order.orderId
        holder.orderDateTextView.text = order.orderDate
        holder.orderStatusTextView.text = order.orderStatus
        holder.orderTotalTextView.text = order.orderTotal
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderIdTextView: TextView = itemView.findViewById(R.id.order_id)
        val orderDateTextView: TextView = itemView.findViewById(R.id.order_date)
        val orderStatusTextView: TextView = itemView.findViewById(R.id.order_status)
        val orderTotalTextView: TextView = itemView.findViewById(R.id.order_total)
    }
}