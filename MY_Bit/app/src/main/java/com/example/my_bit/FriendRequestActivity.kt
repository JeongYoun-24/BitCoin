package com.example.my_bit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_bit.adpter.FriendAdapter
import com.example.my_bit.adpter.UserAdapter
import com.example.my_bit.data.FriendData
import com.example.my_bit.databinding.ActivityFriendRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FriendRequestActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFriendRequestBinding.inflate(layoutInflater) }

    lateinit var adapter : FriendAdapter

    lateinit var mAuth : FirebaseAuth
    lateinit var mDbRef : DatabaseReference

    lateinit var userList : ArrayList<FriendData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var userUID = intent.getStringExtra("id")

//        binding.Texteees.text = userUID
        Log.d("","데이터 전달 ")

        mAuth = Firebase.auth
        mDbRef = Firebase.database.reference

        userList = ArrayList()

        adapter = FriendAdapter(this,userList)

        binding.friendRequest1.layoutManager  = LinearLayoutManager(this)
        binding.friendRequest1.adapter = adapter


        mDbRef.child("friend").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    // 유저 정보
                    val currentUser = postSnapshot.getValue(FriendData::class.java)
                    Log.d("데이터값확인요", currentUser?.uid.toString())


                    if(mAuth.currentUser?.uid != currentUser?.uid){

                        userList.add(currentUser!!)
                        Log.d("갑확인요", "${userList}")
                    }

                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        /*mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    // 유저 정보
                    val currentUser = postSnapshot.getValue(FriendData::class.java)
                    Log.d("데이터값확인요", currentUser?.uid.toString())


                    if(mAuth.currentUser?.uid != currentUser?.uid){

                        userList.add(currentUser!!)
                        Log.d("갑확인요", "${userList}")
                    }

                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })*/


    }




}