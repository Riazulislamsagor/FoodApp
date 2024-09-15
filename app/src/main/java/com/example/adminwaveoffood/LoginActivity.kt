package com.example.adminwaveoffood

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminwaveoffood.Models.UserModel
import com.example.adminwaveoffood.databinding.ActivityLoginBinding
import com.example.adminwaveoffood.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var email: String
    private lateinit var password: String
    private  var userName: String?=null
    private  var nameOfRestaurant: String?=null
    private lateinit var googlesigninClient: GoogleSignInClient
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val googlesigninoptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googlesigninClient=GoogleSignIn.getClient(this,googlesigninoptions)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        binding.liginButtonId.setOnClickListener {
            email = binding.loginemail.text.toString().trim()
            password = binding.loginPassword.text.toString().trim()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                loginAccount(email, password)
            }

        }
        binding.logingoogleButton.setOnClickListener{
            val signinIntent=googlesigninClient.signInIntent
            val launcer = null
            launcher.launch(signinIntent)
        }
        binding.donothaveAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                Toast.makeText(this, "Login is successfull", Toast.LENGTH_SHORT).show()
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        Toast.makeText(this, "Create user & Login successfull", Toast.LENGTH_SHORT).show()

                        saveData()
                        updateUi(user)
                        }else{
                        Toast.makeText(this, "Authentication faield", Toast.LENGTH_SHORT).show()
                        Log.d("Account","Createuser Account:Authentication faield",task.exception)
                    }
                    }
                }
            }
        }
    private fun saveData() {
        email=binding.loginemail.text.toString().trim()
        password=binding.loginPassword.text.toString().trim()
        val user= UserModel(email,nameOfRestaurant,email,password)
        val userId=FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
                database.child("user").child(it).setValue(user)
        }
    }
    private fun UpdateUi(user: FirebaseUser?) {
       startActivity(Intent(this, MainActivity::class.java))
       finish()
   }
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
   result->
        if (result.resultCode==Activity.RESULT_OK){
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account:GoogleSignInAccount=task.result
                val credential=GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener{authtask->
                    if (authtask.isSuccessful){
                        Toast.makeText(this, "Succesfully signin with google", Toast.LENGTH_SHORT).show()
                        updateUi(authtask.result?.user)
                        finish()
                    }else{
                        Toast.makeText(this, "Signin with google failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Signin with google failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onStart(){
        super.onStart()
        val currenuser=auth.currentUser
        if (currenuser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
    private fun updateUi(user: FirebaseUser?){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    }



