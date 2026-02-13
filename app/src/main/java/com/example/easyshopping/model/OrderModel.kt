package com.example.easyshopping.model

import com.google.firebase.Timestamp

data class OrderModel(
    val id : String ="",
    val userId : String = "",
    val time : Timestamp = Timestamp.now(),
    val items : Map<String,Long> = mapOf(),
    val status : String = "",
    val address : String = "",
)
