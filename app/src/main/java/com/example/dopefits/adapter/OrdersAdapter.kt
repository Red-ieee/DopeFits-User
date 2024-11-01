// OrdersAdapter.kt
package com.example.dopefits.adapter.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopefits.R
import com.example.dopefits.ui.orders.Order
import java.text.NumberFormat
import java.util.Locale

class OrdersAdapter(private val ordersList: List<Order>) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]
        holder.orderId.text = order.orderId
        holder.orderDate.text = order.orderDate
        holder.orderStatus.text = order.orderStatus

        // Format the order total as currency
        val formattedTotal = NumberFormat.getCurrencyInstance(Locale("en", "PH")).format(order.orderTotal.toDouble() / 100)
        holder.orderTotal.text = formattedTotal

        holder.orderProductName.text = order.productName

        // Load the first product image from the list
        if (order.productImage.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(order.productImage[0])
                .into(holder.orderImage)
        } else {
            holder.orderImage.setImageResource(R.drawable.ic_placeholder) // Set a placeholder image if the list is empty
        }
    }

    override fun getItemCount(): Int = ordersList.size

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId: TextView = itemView.findViewById(R.id.order_id)
        val orderDate: TextView = itemView.findViewById(R.id.order_date)
        val orderStatus: TextView = itemView.findViewById(R.id.order_status)
        val orderTotal: TextView = itemView.findViewById(R.id.order_total)
        val orderProductName: TextView = itemView.findViewById(R.id.order_product_name)
        val orderImage: ImageView = itemView.findViewById(R.id.order_image)
    }
}