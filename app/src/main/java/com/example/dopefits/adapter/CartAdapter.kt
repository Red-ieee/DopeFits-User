package com.example.dopefits.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dopefits.R
import com.example.dopefits.model.Product
import java.net.HttpURLConnection
import java.net.URL

class CartAdapter(
    private var products: MutableList<Product>,
    private val onRemoveClick: (Int, Button) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val isRemoving = mutableMapOf<Int, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product, position, onRemoveClick, isRemoving)
    }

    override fun getItemCount(): Int = products.size

    fun setRemovingFlag(position: Int, value: Boolean) {
        isRemoving[position] = value
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.product_image)
        private val titleTextView: TextView = itemView.findViewById(R.id.product_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.product_price)
        private val removeButton: Button = itemView.findViewById(R.id.remove_button)

        fun bind(
            product: Product,
            position: Int,
            onRemoveClick: (Int, Button) -> Unit,
            isRemoving: MutableMap<Int, Boolean>
        ) {
            titleTextView.text = product.title
            priceTextView.text = product.price.toString()

            if (product.picUrl.isNotEmpty()) {
                val imageUrl = product.picUrl[0]
                Thread {
                    try {
                        val url = URL(imageUrl)
                        val connection = url.openConnection() as HttpURLConnection
                        connection.doInput = true
                        connection.connect()
                        val inputStream = connection.inputStream
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        itemView.post {
                            productImage.setImageBitmap(bitmap)
                        }
                    } catch (e: Exception) {
                        Log.e("CartViewHolder", "Error loading image: ${e.message}")
                    }
                }.start()
            }
            removeButton.setOnClickListener {
                if (isRemoving[position] != true) {
                    isRemoving[position] = true
                    onRemoveClick(position, removeButton)
                }
            }
        }
    }
}