package com.example.adminwaveoffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adminwaveoffood.Models.UserModel
import com.example.adminwaveoffood.databinding.ActivityAdminProfileBinding
import com.example.adminwaveoffood.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var adminReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()
        adminReference=database.reference.child("user")
        binding.backButtonId.setOnClickListener{
            finish()
        }
        binding.saveInformationId.setOnClickListener{
            UpdateUserData()
        }

        binding.name.isEnabled=false
        binding.address.isEnabled=false
        binding.email.isEnabled=false
        binding.phone.isEnabled=false
        binding.password.isEnabled=false
        var isenable=false
        binding.saveInformationId.isEnabled=true

        binding.editbutton.setOnClickListener{
            isenable=!isenable
            binding.name.isEnabled=isenable
            binding.address.isEnabled=isenable
            binding.email.isEnabled=isenable
            binding.phone.isEnabled=isenable
            binding.password.isEnabled=isenable
            if (isenable){
                binding.name.requestFocus()
            }
            binding.saveInformationId.isEnabled=isenable
        }
        retriveUserData()
    }



    private fun retriveUserData() {
        val currenUserId=auth.currentUser?.uid
        if (currenUserId!=null){
            val userReference=adminReference.child(currenUserId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var ownerName=snapshot.child("name").getValue()
                        var email=snapshot.child("email").getValue()
                        var password=snapshot.child("password").getValue()
                        var address=snapshot.child("address").getValue()
                        var phone=snapshot.child("phone").getValue()

                        setDataToTextView(ownerName,email,password,address,phone)

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

    }

    private fun setDataToTextView(
        ownerName: Any?,
        email: Any?,
        password: Any?,
        address: Any?,
        phone: Any?
    ) {
        binding.name.setText(ownerName.toString())
        binding.email.setText(email.toString())
        binding.password.setText(password.toString())
        binding.address.setText(address.toString())
        binding.phone.setText(phone.toString())
    }
    private fun UpdateUserData() {
        val updateName=binding.name.text.toString()
       val updateEmail=binding.email.text.toString()
       val updatePassword= binding.password.text.toString()
       val updateAddress=binding.address.text.toString()
       val updatePhone=binding.phone.text.toString()
        val currentUserUid=auth.currentUser?.uid
        if (currentUserUid!=null){
            val userReference=adminReference.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("password").setValue(updatePassword)
            userReference.child("address").setValue(updateAddress)
            userReference.child("phone").setValue(updatePhone)
            Toast.makeText(this, "Profile update successfull", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        }
        else{
            Toast.makeText(this, "Profile updated failed", Toast.LENGTH_SHORT).show()
        }

    }


}