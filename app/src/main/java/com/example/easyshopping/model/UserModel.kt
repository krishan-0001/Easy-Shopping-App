package com.example.easyshopping.model

import com.google.android.gms.common.internal.StringResourceValueReader

data class UserModel(
    val name : String = "",
    val email : String = "",
    val uid : String = "",
    val cartItems : Map<String,Long> = emptyMap()
)
