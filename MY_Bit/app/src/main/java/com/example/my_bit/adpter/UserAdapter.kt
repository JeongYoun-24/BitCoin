package com.example.my_bit.adpter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.my_bit.R
import com.example.my_bit.User
import com.example.my_bit.UserBit
import com.example.my_bit.UserListActivity
import com.example.my_bit.data.FriendData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserAdapter(private val context : Context,private val userList :ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    lateinit var mAutn : FirebaseAuth
    private lateinit var mDBRef: DatabaseReference

    private lateinit var receiverMsg : String // 받는 친구요청
    private lateinit var sendMsg : String // 보낸 친구요청


    // 화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.user_list,parent,false)
        // 인증 초기화
        mAutn = Firebase.auth
        // DB 초기화
        mDBRef = Firebase.database.reference

        return UserViewHolder(view).apply{
            val intent = Intent(parent.context, UserListActivity::class.java)

            val freindBut =itemView.findViewById<ImageView>(R.id.FriendBut) // 성별
            freindBut.setOnClickListener(){
                Log.d("값확인","클릭했으")

                val curPos : Int = adapterPosition // 현재 포지션
                val UserList : User = userList.get(curPos) // 객체형태로 전달
                Log.d("현재포지션","${curPos.toString()}")
                Log.d("객체형태 데이터값","${UserList.toString()}")

                val sendUid = mAutn.currentUser?.uid // 보낸 사람 Uid
                val recipient = UserList.uid

                Log.d("보낸 사람의Uid","${sendUid.toString()}")
                Log.d("받는 사람 사람의Uid","${recipient.toString()}")
                // 보낸 친구요청
                sendMsg = UserList.uid + sendUid

                // 받는 친구요청
                receiverMsg = sendUid + UserList.uid

                Log.d("보낸 친구요청","${sendMsg.toString()}")
                Log.d("받는 친구요청","${receiverMsg.toString()}")

                mDBRef.child("user").child("${sendUid.toString()}").get().addOnSuccessListener {
                    var name = it.child("name").value


                    mDBRef.child("user").child("${sendUid.toString()}").child("friend").child("${recipient.toString()}").get().addOnSuccessListener {
                        val uId = it.child("uid").value

                        if(uId == null){
                            mDBRef.child("friend").child("${recipient.toString()}").setValue(FriendData("친구요청","${name.toString()}","${sendUid.toString()}"))

                        }else{
                            Toast.makeText(context,"이미 친구추가 되어있습니다.",Toast.LENGTH_LONG).show()
                        }


                    }

                }




                /*
                                intent.putExtra("email",UserList.email)
                                intent.putExtra("name",UserList.name)
                                intent.putExtra("Uid",UserList.uid)


                                ContextCompat.startActivity(parent.context,intent,null)*/
            }

        }



    } // Holder and


    // 데이터 설정
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.nameText.text =currentUser.name
    }

    override fun getItemCount(): Int {
        return  userList.size
    }

    class UserViewHolder(itemView : View ) : RecyclerView.ViewHolder(itemView){

        val nameText : TextView = itemView.findViewById(R.id.userName)

    }




}