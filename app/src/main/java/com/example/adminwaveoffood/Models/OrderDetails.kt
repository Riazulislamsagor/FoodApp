package com.example.adminwaveoffood.Models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class OrderDetails():Serializable {

    var userUId: String? = null
    var username: String? = null
    var foodName: MutableList<String>?=null
    var foodImagees: MutableList<String>?=null
    var foodPricees: MutableList<String>?=null
    var foodQuantities:MutableList<Int>?=null
    var address:String?=null
    var totalprice:String?=null
    var phoneNumber:String?=null
    var OrderAccepted:Boolean=false
    var paymentRecevied:Boolean=false
    var itemPushKey:String?=null
    var currenttime:Long=0

    constructor(parcel: Parcel) : this() {
        userUId = parcel.readString()
        username = parcel.readString()
        address = parcel.readString()
        totalprice = parcel.readString()
        phoneNumber = parcel.readString()
        OrderAccepted = parcel.readByte() != 0.toByte()
        paymentRecevied = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currenttime = parcel.readLong()
    }

     fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }

}