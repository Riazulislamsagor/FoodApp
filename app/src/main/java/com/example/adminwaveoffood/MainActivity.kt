package com.example.adminwaveoffood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminwaveoffood.Models.OrderDetails
import com.example.adminwaveoffood.databinding.ActivityAddItemBinding
import com.example.adminwaveoffood.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()

        binding.addmenu.setOnClickListener{
            val intent=Intent(this,AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.allitemmenuId.setOnClickListener{
            val intent=Intent(this,AddAllItemActivity::class.java)
            startActivity(intent)
        }
        binding.cardorderDispatch.setOnClickListener{
            val intent=Intent(this,OutforDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.profileId.setOnClickListener{
            val intent=Intent(this,AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.createuser.setOnClickListener{
            val intent=Intent(this,CreateUserActivity::class.java)
            startActivity(intent)
        }
        binding.pendingordertextView.setOnClickListener{
            val intent=Intent(this,PendingOrderActivity::class.java)
            startActivity(intent)
        }
       binding.logoutId.setOnClickListener{
           auth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
       }
        pendingOrders()
        CompletedOrder()
        WholeTimeEarning()
    }

    private fun WholeTimeEarning() {
        var listpfTotalPay= mutableListOf<Int>()
         databaseReference=FirebaseDatabase.getInstance().reference.child("CompleteOrders")

        databaseReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ordersnapshot:DataSnapshot in snapshot.children){
                    var completeorder=ordersnapshot.getValue(OrderDetails::class.java)
                    completeorder?.totalprice?.replace("$","")?.toIntOrNull()
                        ?.let {i->
                            listpfTotalPay.add(i)
                        }
                }
                binding.wholeTimeEarninId.text=listpfTotalPay.sum().toString()+"$"

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }


    private fun pendingOrders() {
        database= FirebaseDatabase.getInstance()
        var pendingOrderReference=database.reference.child("OrderDetails")
        var pendingOrderitemCount=0
        pendingOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderitemCount=snapshot.childrenCount.toInt()
                binding.pendingOrder.text=pendingOrderitemCount.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    private fun CompletedOrder() {
        var completeOrderReference=database.reference.child("CompleteOrders")
        var completeOrderitemCount=0
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completeOrderitemCount=snapshot.childrenCount.toInt()
                binding.completeOrderId.text=completeOrderitemCount.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}