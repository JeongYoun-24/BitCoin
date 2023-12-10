package com.example.my_bit.data

data class FriendListData(
    val name : String,
    val uid : String,
    val coin : String
){
    constructor():this("","","")

}
