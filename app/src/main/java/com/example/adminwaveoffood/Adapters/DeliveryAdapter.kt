package com.example.adminwaveoffood.Adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminwaveoffood.databinding.DeliveryItemBinding
import com.example.adminwaveoffood.databinding.ItemItemBinding

class DeliveryAdapter(private val customerName:MutableList<String>,private val moneyStatus:MutableList<Boolean>): RecyclerView.Adapter<DeliveryAdapter.DeliveryViewolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewolder {
        val binding= DeliveryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeliveryViewolder(binding)
    }



    override fun onBindViewHolder(holder: DeliveryViewolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int {
     return customerName.size
    }
    inner class DeliveryViewolder (private val binding:DeliveryItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
           binding.apply {
               customernameId.text=customerName[position]
               if (moneyStatus[position]==true){
                   notreceivedId.text="Received"
           }else{
                   notreceivedId.text="Notreceived"
               }

               val colorMp= mapOf(
                   true to Color.GREEN,false to Color.RED
               )
               notreceivedId.setTextColor(colorMp[moneyStatus[position]]?:Color.BLACK)
               statusColor.backgroundTintList= ColorStateList.valueOf(colorMp[moneyStatus[position]]?:Color.BLACK)
           }
        }

    }
}