package com.example.adminwaveoffood.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.Models.AllMenu
import com.example.adminwaveoffood.databinding.ItemItemBinding
import com.google.firebase.database.DatabaseReference
import java.nio.file.attribute.AclEntry.Builder

class MenuitemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val ondeleteClicklistener:(positon :Int)->Unit
): RecyclerView.Adapter<MenuitemAdapter.AlladapterViewHolder>() {
private val itemquantities=IntArray(menuList.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlladapterViewHolder {
       val binding=ItemItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AlladapterViewHolder(binding)
    }



    override fun onBindViewHolder(holder: AlladapterViewHolder, position: Int) {
        holder.bind(position)

}
    override fun getItemCount(): Int {
        return menuList.size
    }
    inner class AlladapterViewHolder(private val binding:ItemItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity=itemquantities[position]
                val menuItem=menuList[position]
                val uriString=menuItem.foodImage
                val uri= Uri.parse(uriString)
                foodnameTextView.text=menuItem.foodName
                pricetextView.text=menuItem.foodPrice
                Glide.with(context).load(uri).into(imagefoodId)


                plusButton.setOnClickListener{
                     increasequantity(position)
                }
                minusbuttton.setOnClickListener{
                    decreasequantities(position)

                }
                deleteImageId.setOnClickListener{
                    ondeleteClicklistener(position)

                }

            }
        }



        private fun increasequantity(position: Int) {
            if (itemquantities[position]<10){
                itemquantities[position]++
                binding.quantitytextId.text=itemquantities[position].toString()
            }

        }
        private fun decreasequantities(position: Int) {
            if (itemquantities[position]>1){
                itemquantities[position]--
                binding.quantitytextId.text=itemquantities[position].toString()
            }

        }
        private fun deletequantity(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,menuList .size)

        }


    }



}