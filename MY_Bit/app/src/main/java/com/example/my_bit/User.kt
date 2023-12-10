package com.example.my_bit

data class User(
    val name :String,
    val email :String,
    val password : String,
    val uid : String
){
    constructor() : this("","","","")
}
