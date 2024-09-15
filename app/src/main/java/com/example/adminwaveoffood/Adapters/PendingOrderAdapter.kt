package com.example.adminwaveoffood.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.databinding.PendingItemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val pnedingcustomerName: MutableList<String>,
    private val pendingQuantity: MutableList<String>,
    private val pendingImageName: MutableList<String>,
    private val itemClicked: OnItemClicked
) :
    RecyclerView.Adapter<PendingOrderAdapter.PendingViewHolder>() {
    interface OnItemClicked{
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingViewHolder {
        val binding = PendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PendingViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return pnedingcustomerName.size
    }

    inner class PendingViewHolder(private val binding: PendingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                pendingfoodnameTextView.text = pnedingcustomerName[position]
                pricetextView.text = pendingQuantity[position]
                var uriString = pendingImageName[position]
                var uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(imagefoodId)

                //imagefoodId.setImageResource(pendingImageName[position])
                orderAcceptImageId.apply {
                    if (!isAccepted) {
                        text = "Accepted"
                    } else {
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is Accepted")
                           itemClicked.onItemAcceptClickListener(position)
                        } else {
                            pnedingcustomerName.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is Dispatch")
                            itemClicked.onItemDispatchClickListener(position)


                        }
                    }
                }
                itemView.setOnClickListener{
                    itemClicked.onItemClickListener(position)
                }
            }

        }

        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }

}