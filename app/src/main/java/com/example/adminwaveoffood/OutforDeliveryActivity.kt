package com.example.adminwaveoffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapters.DeliveryAdapter
import com.example.adminwaveoffood.Models.OrderDetails
import com.example.adminwaveoffood.databinding.ActivityAddItemBinding
import com.example.adminwaveoffood.databinding.ActivityOutforDeliveryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutforDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutforDeliveryBinding by lazy {
        ActivityOutforDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database:FirebaseDatabase
    private  var listOfCompleteOrderlist:ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButtonId.setOnClickListener{
            finish()
        }
        retriveCompleteOrderdetails()


//
    }

    private fun retriveCompleteOrderdetails() {
        database= FirebaseDatabase.getInstance()
        val completeOrderReference=database.reference.child("CompleteOrders").orderByChild("Currenttime")
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrderlist.clear()

                for (ordersnapshot:DataSnapshot in snapshot.children){
                    val completeOrder=ordersnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderlist.add(it)
                    }
                }
                listOfCompleteOrderlist.reverse()
                setDataIntoRecycleView()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setDataIntoRecycleView() {
        val customerName = mutableListOf<String>()
        val moneyStatus= mutableListOf<Boolean>()
        for(order:OrderDetails in listOfCompleteOrderlist){
            order.username?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentRecevied)
        }
        val adapter=DeliveryAdapter(customerName,moneyStatus)
        binding.deliveryRecycleViewId.layoutManager=LinearLayoutManager(this)
        binding.deliveryRecycleViewId.adapter=adapter

    }
}