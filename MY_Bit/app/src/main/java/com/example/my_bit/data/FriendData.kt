package com.example.my_bit.data

data class FriendData(
    val message :String,
    val name : String,
    val uid : String
){
    constructor():this("","","")

}
