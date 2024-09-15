package com.example.adminwaveoffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapters.OrderDetailsAdapter
import com.example.adminwaveoffood.Models.OrderDetails
import com.example.adminwaveoffood.databinding.ActivityOrederDetailsBinding

class Oreder_DetailsActivity : AppCompatActivity() {
    private val binding:ActivityOrederDetailsBinding by lazy {
        ActivityOrederDetailsBinding.inflate(layoutInflater)
    }
    private var userName:String?=null
    private var address:String?=null
    private var phoneNumber:String?=null
    private var totalPrice:String?=null

    private  var foodNames:ArrayList<String> = arrayListOf()
    private  var foodImage:ArrayList<String> = arrayListOf()
    private  var foodQuantity:ArrayList<Int> = arrayListOf()
    private  var foodPrices:ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backbutton.setOnClickListener{
            finish()
        }
        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val reciveOrderDetails=intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        reciveOrderDetails?.let {orderDetails ->

                userName=reciveOrderDetails.username
                foodNames=reciveOrderDetails.foodName as ArrayList<String>
                foodImage=reciveOrderDetails.foodImagees as ArrayList<String>
                foodQuantity=reciveOrderDetails.foodQuantities as ArrayList<Int>
                address=reciveOrderDetails.address
                phoneNumber=reciveOrderDetails.phoneNumber
                foodPrices=reciveOrderDetails.foodPricees as ArrayList<String>
                totalPrice=reciveOrderDetails.totalprice

                setUserDetails()
                setAdapter()


        }



    }

    private fun setAdapter() {
        binding.orderDeyailsrecycelUd.layoutManager=LinearLayoutManager(this)
        val adapter=OrderDetailsAdapter(this,foodNames,foodImage,foodQuantity,foodPrices)
        binding.orderDeyailsrecycelUd.adapter=adapter

    }

    private fun setUserDetails() {
        binding.name.text=userName
        binding.address.text=address
        binding.phone.text=phoneNumber
        binding.totalAmount.text=totalPrice

    }
}