package com.example.my_bit.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_bit.R
import com.example.my_bit.User

class UserAdapter(private val context : Context,private val userList :ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(){


    // 화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val view : View = LayoutInflater.from(context).inflate(R.layout.user_list,parent,false)

        return UserViewHolder(view)
    }
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