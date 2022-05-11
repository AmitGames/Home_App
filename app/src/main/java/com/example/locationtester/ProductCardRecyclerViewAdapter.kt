package com.example.locationtester

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import network.ImageRequester

import network.ProductEntry

class ProductCardRecyclerViewAdapter(private val productList: List<ProductEntry>) : RecyclerView.Adapter<ProductCardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.product_cards, parent, false)
        return ProductCardViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        if (position < productList.size) {
            val product = productList[position]
            holder.productTitle.text = product.title
            holder.productPrice.text = product.price
            //ImageRequester.setImageFromUrl(holder.productImage, product.url)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}