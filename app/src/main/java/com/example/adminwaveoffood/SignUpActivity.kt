package com.example.adminwaveoffood

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.adminwaveoffood.Models.UserModel
import com.example.adminwaveoffood.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String

    private val binding:ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        database=Firebase.database.reference
        binding.createAccountId.setOnClickListener{
            userName=binding.NameId.text.toString().trim()
            nameOfRestaurant=binding.resturantNameId.text.toString().trim()
            email=binding.EmailId.text.toString().trim()
            password=binding.signuppasswordId.text.toString().trim()
            if (userName.isBlank()||nameOfRestaurant.isBlank()||email.isBlank()||password.isBlank()){
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }else{
                CreateAccount(email,password)
            }

        }
        binding.alreadyAccountId.setOnClickListener{
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        val locationlist= arrayOf("Dhaka","Khulna","Barishal","Chitagong","Rajshahi","Shylet")
        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,locationlist)
        val autoCompleteTextView=binding.listoflocationId
        autoCompleteTextView.setAdapter(adapter)
    }

    private fun CreateAccount(email: String, password: String) {

auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->
    if (task.isSuccessful){
        Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
        saveData()
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }else{
        Toast.makeText(this, "Account Creation faild", Toast.LENGTH_SHORT).show()
        Log.d(TAG,"Create Account:Failure",task.exception)
    }

}

    }

    private fun saveData() {
        userName=binding.NameId.text.toString().trim()
        nameOfRestaurant=binding.resturantNameId.text.toString().trim()
        email=binding.EmailId.text.toString().trim()
        password=binding.signuppasswordId.text.toString().trim()
        val user=UserModel(userName,nameOfRestaurant,email,password)
        val userId:String=FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)

    }
}