package com.example.adminwaveoffood

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminwaveoffood.Models.AllMenu
import com.example.adminwaveoffood.databinding.ActivityAddItemBinding
import com.example.adminwaveoffood.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import android.util.Log // Added for logging

class AddItemActivity : AppCompatActivity() {

    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.addbutton.setOnClickListener {
            foodName = binding.enterfoodnameId.text.toString().trim()
            foodPrice = binding.enterfoodpriceId.text.toString().trim()
            foodDescription = binding.descriotion.text.toString().trim()
            foodIngredient = binding.ingredient.text.toString().trim()

            if (!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngredient.isBlank())) {
                UploadData()
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }

        binding.selectedImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.backButtonId.setOnClickListener {
            finish()
        }
    }

    private fun UploadData() {
        val menuRef = database.getReference("menu")
        val newItemkey = menuRef.push().key

        if (foodImageUri != null && newItemkey != null) {
            val storageref = FirebaseStorage.getInstance().reference
            val imageref = storageref.child("menu_image/${newItemkey}.jpg")

            val uploadtask = imageref.putFile(foodImageUri!!)
            uploadtask.addOnSuccessListener {
                imageref.downloadUrl.addOnSuccessListener { downloadurl ->
                    val newItem = AllMenu(
                        newItemkey,
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodIngredient = foodIngredient,
                        foodImage = downloadurl.toString(),
                    )
                    menuRef.child(newItemkey).setValue(newItem).addOnSuccessListener {
                        Toast.makeText(this, "Data uploaded successfully", Toast.LENGTH_SHORT).show()
                        finish() // Finish the activity after success
                    }.addOnFailureListener { databaseError ->
                        Log.e("UploadData", "Database upload failed", databaseError)
                        Toast.makeText(this, "Data upload failed: ${databaseError.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener { storageError ->
                Log.e("UploadData", "Image upload failed", storageError)
                Toast.makeText(this, "Image upload failed: ${storageError.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            if (newItemkey == null) {
                Log.e("UploadData", "Failed to generate a new item key")
            }
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
        } else {
            Log.e("AddItemActivity", "Image selection failed or was canceled")
            Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show()
        }
    }
}
