package com.example.my_bit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_bit.adpter.UserAdapter
import com.example.my_bit.databinding.ActivityShopBinding
import com.example.my_bit.databinding.ActivityUserListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityUserListBinding.inflate(layoutInflater) }

    lateinit var adapter : UserAdapter

    lateinit var mAuth : FirebaseAuth
    lateinit var mDbRef : DatabaseReference

    lateinit var userList : ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mAuth = Firebase.auth
        mDbRef = Firebase.database.reference

        userList = ArrayList()

        adapter = UserAdapter(this,userList)

        binding.userRecycle.layoutManager  = LinearLayoutManager(this)
        binding.userRecycle.adapter = adapter


        mDbRef.child("user").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    // 유저 정보
                    val currentUser = postSnapshot.getValue(User::class.java)
                    Log.d("데이터값확인요", currentUser?.uid.toString())




                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                        Log.d("갑확인요", "${userList}")
                        mDbRef.child("user").child("${mAuth.currentUser?.uid}").child("friend").child("${currentUser?.uid.toString()}").get().addOnSuccessListener {
                            var uId =  it.child("uid").value
                            Log.d("UId값 ", "${uId.toString()}")


                        }




                    }

                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }


}