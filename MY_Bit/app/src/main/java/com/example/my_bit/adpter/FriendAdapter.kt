package com.example.my_bit.adpter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_bit.R
import com.example.my_bit.User
import com.example.my_bit.UserListActivity
import com.example.my_bit.data.FriendData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class FriendAdapter(private val context : Context, private val userList :ArrayList<FriendData>) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>(){

    lateinit var mAutn : FirebaseAuth
    private lateinit var mDBRef: DatabaseReference

    private lateinit var receiverMsg : String // 받는 친구요청
    private lateinit var sendMsg : String // 보낸 친구요청


    // 화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.friend_request_list,parent,false)
        // 인증 초기화
        mAutn = Firebase.auth
        // DB 초기화
        mDBRef = Firebase.database.reference
        Log.d("어댑터112423432312","어댑터 들어왔나요?")
        return FriendViewHolder(view).apply {
            val intent = Intent(parent.context, UserListActivity::class.java)

            val freindBut =itemView.findViewById<Button>(R.id.ReBut) // 성별

            // 친구 추가 버튼
            freindBut.setOnClickListener(){
                Log.d("값확인","클릭했으")

                val curPos : Int = adapterPosition // 현재 포지션
                val UserList : FriendData = userList.get(curPos) // 객체형태로 전달
                Log.d("현재포지션","${curPos.toString()}")
                Log.d("객체형태 데이터값","${UserList.toString()}")

                val sendUid = mAutn.currentUser?.uid // 친구요청 받은 사람 uId
                val friendUserUid = UserList.uid   // 친구요청 보낸 사람 uId
                val friendUserName = UserList.name   // 친구요청 보낸 사람 닉네임

                Log.d("데이터값 확인","${friendUserUid.toString()}")
                Log.d("데이터값 확인","${friendUserName.toString()}")

                mDBRef.child("user").child(sendUid.toString()).get().addOnSuccessListener {
                    var name =     it.child("name").value

                    // 친구요청 받은 사용자
                    mDBRef.child("user").child("${sendUid.toString()}").child("friend").child("${friendUserUid.toString()}").setValue(FriendData("친구","${friendUserName.toString()}","${friendUserUid.toString()}"))

                    // 친구요청 보낸 사용자
                    mDBRef.child("user").child("${friendUserUid.toString()}").child("friend").child("${sendUid.toString()}").setValue(FriendData("친구","${name.toString()}","${sendUid.toString()}"))

                    // 친구요청 메세지 리스트 삭제
                    mDBRef.child("friend").child("${sendUid}").removeValue().addOnCanceledListener {


                    }

                }




               /* mDBRef.child("user").child("${sendUid.toString()}").get().addOnSuccessListener {
                    var name = it.child("name").value


                    mDBRef.child("friend").child("${recipient.toString()}").setValue(FriendData("친구요청","${name.toString()}","${sendUid.toString()}"))

                }*/




                /*
                                intent.putExtra("email",UserList.email)
                                intent.putExtra("name",UserList.name)
                                intent.putExtra("Uid",UserList.uid)


                                ContextCompat.startActivity(parent.context,intent,null)*/
            }
        }


    } // Holder and


    // 데이터 설정
    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val currentUser = userList[position]
        Log.d("어댑터112312","어댑터 들어왔나요?")
        holder.nameText.text =currentUser.name
    }

    override fun getItemCount(): Int {

        return  userList.size
        
    }

    class FriendViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val nameText : TextView = itemView.findViewById(R.id.friendName)


    }




}