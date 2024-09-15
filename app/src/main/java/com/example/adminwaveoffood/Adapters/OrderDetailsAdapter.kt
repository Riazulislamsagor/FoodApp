package com.example.adminwaveoffood.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.databinding.OrderetailsItemsBinding

class OrderDetailsAdapter(
    private var context:Context,
    private var foodName:ArrayList<String>,
    private var foodImage:ArrayList<String>,
    private var foodQuantity:ArrayList<Int>,
    private var foodPrice:ArrayList<String>,
): RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
       val binding=OrderetailsItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailsViewHolder(binding)
    }


    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
       holder.bind(position)
    }
    override fun getItemCount(): Int {
       return foodName.size
    }

    inner class OrderDetailsViewHolder (private val binding:OrderetailsItemsBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
          binding.apply {
              orderdetailsfoodname.text=foodName[position]
              var uriString = foodImage[position]
              var uri = Uri.parse(uriString)
              Glide.with(context).load(uri).into(orderDetailsImage)
              orderQuantity.text=foodQuantity[position].toString()
              oderPrice.text=foodPrice[position]

          }
        }

    }
}