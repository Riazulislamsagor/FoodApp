package com.example.adminwaveoffood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapters.PendingOrderAdapter
import com.example.adminwaveoffood.Models.OrderDetails
import com.example.adminwaveoffood.databinding.ActivityPendingOrderBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity(),PendingOrderAdapter.OnItemClicked {
    private lateinit var binding: ActivityPendingOrderBinding
    private var listOfname:MutableList<String> = mutableListOf()
    private var totallistOfPrice:MutableList<String> = mutableListOf()
    private var listofImageFirstFoodOrder:MutableList<String> = mutableListOf()
    private var listofOrderItem:ArrayList<OrderDetails> = arrayListOf()
    private lateinit var databaseOrderDetails: DatabaseReference
    private lateinit var database:FirebaseDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database= FirebaseDatabase.getInstance()
        databaseOrderDetails=database.reference.child("OrderDetails")

        getOrderDetails()
        binding.backButtonId.setOnClickListener {
            finish()
        }
        //binding.pendingRecycleViewId.layoutManager= LinearLayoutManager(this)
       // binding.pendingRecycleViewId.adapter=adapter

    }

    private fun getOrderDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot:DataSnapshot in snapshot.children){
                    val orderDetails=orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listofOrderItem.add(it)
                    }
                }
                addDataTorecycleList()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addDataTorecycleList() {
        for (orderItem in listofOrderItem){
            orderItem.username?.let { listOfname.add(it) }
            orderItem.totalprice?.let { totallistOfPrice.add(it) }
            orderItem.foodImagees?.filterNot {it.isEmpty()}?.forEach{
                listofImageFirstFoodOrder.add(it)
            }
        }
        setadapter()
    }

    private fun setadapter() {
        binding.pendingRecycleViewId.layoutManager=LinearLayoutManager(this)
        val adapter=PendingOrderAdapter(this,listOfname,totallistOfPrice,listofImageFirstFoodOrder,this)
        binding.pendingRecycleViewId.adapter=adapter
    }

    override fun onItemClickListener(position: Int) {
       val intent=Intent(this,Oreder_DetailsActivity::class.java)
        val userOrderDetails=listofOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickListener(position: Int) {
       val childItemPushKey=listofOrderItem[position].itemPushKey
        val clickedItemOrderreference=childItemPushKey.let {
            database.reference.child("OrderDetails").child(it.toString())
        }
        clickedItemOrderreference?.child("orderAccepted")?.setValue(true)
        updateorderAcceptStatus(position)
    }



    override fun onItemDispatchClickListener(position: Int) {
        val dispatchItemPushKey=listofOrderItem[position].itemPushKey
        val dispatchReference=database.reference.child("CompleteOrders").child(dispatchItemPushKey!!)
        dispatchReference.setValue(listofOrderItem[position])
            .addOnSuccessListener {
                deleteOrderDetailsItem(dispatchItemPushKey)
            }

    }

    private fun deleteOrderDetailsItem(dispatchItemPushKey: String) {
        val orderDetailsItemReference=database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Order is dispatch", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Order is not dispatch", Toast.LENGTH_SHORT).show()

            }
    }

    private fun updateorderAcceptStatus(position: Int) {
        val userIdofClickedItem:String?=listofOrderItem[position].userUId
        val pushKeyOfClickedItem=listofOrderItem[position].itemPushKey
        val buyHistoryreference=database.reference.child("user").child(userIdofClickedItem!!).child("BuyHistory").child(pushKeyOfClickedItem!!)
        buyHistoryreference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)

    }
}