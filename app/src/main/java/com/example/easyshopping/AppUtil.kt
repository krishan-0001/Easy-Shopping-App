package com.example.easyshopping

import android.widget.Toast
import android.content.Context


object AppUtil {
    fun showToast(context: Context,message : String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}