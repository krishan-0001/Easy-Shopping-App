package com.example.easyshopping

import android.widget.Toast
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


object AppUtil {
    fun showToast(context: Context,message : String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
    fun addItemToCart(productId : String,context: Context){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            if(it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String,Long> ?:emptyMap()
                val currentQuantity = currentCart[productId]?:0
                val updatedQuantity = currentQuantity+1
                val updatedCart = mapOf("cartItems.$productId" to updatedQuantity)
                userDoc.update(updatedCart)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            showToast(context,"Items added to the cart")
                        }
                        else{
                            showToast(context,"Failed adding items to the cart")
                        }
                    }
            }
        }
    }
}