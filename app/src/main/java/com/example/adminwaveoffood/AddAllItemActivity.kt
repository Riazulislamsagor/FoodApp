package com.example.adminwaveoffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapters.MenuitemAdapter
import com.example.adminwaveoffood.Models.AllMenu
import com.example.adminwaveoffood.databinding.ActivityAddAllItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddAllItemActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems:ArrayList<AllMenu> = ArrayList()


    private val binding: ActivityAddAllItemBinding by lazy {
        ActivityAddAllItemBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        databaseReference=FirebaseDatabase.getInstance().reference
        retriveMenuItem()
        binding.backButtonId.setOnClickListener{
            finish()
        }


    }

    private fun retriveMenuItem() {
        database= FirebaseDatabase.getInstance()
        val fodref:DatabaseReference=databaseReference.ref.child("menu")
        fodref.addListenerForSingleValueEvent(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()

                for (foodsnspshoot:DataSnapshot in snapshot.children){
                    val menuItem=foodsnspshoot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun setAdapter() {
        val adapter=MenuitemAdapter(this@AddAllItemActivity,menuItems,databaseReference){ position ->
            deletemenuItems(position)

        }

        binding.menuRecycleviewId.layoutManager= LinearLayoutManager(this)
        binding.menuRecycleviewId.adapter=adapter
    }

    private fun deletemenuItems(position: Int) {

        val menuItemTodelete=menuItems[position]
        val menuItemKey=menuItemTodelete.key
        val foodreference=database.reference.child("menu").child(menuItemKey!!)
        foodreference.removeValue().addOnCompleteListener() {task->
            if (task.isSuccessful){
                menuItems.removeAt(position)
                binding.menuRecycleviewId.adapter?.notifyItemRemoved(position)
            }else{
                Toast.makeText(this, "Item not deleted", Toast.LENGTH_SHORT).show()
            }

        }
    }

}