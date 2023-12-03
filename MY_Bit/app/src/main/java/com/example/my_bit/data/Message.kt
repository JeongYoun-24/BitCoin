package com.example.my_bit.data

data class Message(
    val message : String,
    val name : String,
    val sendId : String
){
    constructor():this("","","")

}
