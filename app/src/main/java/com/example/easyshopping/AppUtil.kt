package com.example.easyshopping

import android.app.Activity
import android.widget.Toast
import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.core.content.edit
import com.example.easyshopping.model.OrderModel
import com.example.easyshopping.ui.GlobalNavigation
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.razorpay.Checkout
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID


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
    fun removeFromCart(productId : String,context: Context,removeAll : Boolean = false){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            if(it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String,Long> ?:emptyMap()
                val currentQuantity = currentCart[productId]?:0
                val updatedQuantity = currentQuantity-1
                val updatedCart =
                    if(updatedQuantity<=0 || removeAll){
                        mapOf("cartItems.$productId" to FieldValue.delete())
                    }
                else
                    mapOf("cartItems.$productId" to updatedQuantity)
                userDoc.update(updatedCart)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            showToast(context,"Items removed from the cart")
                        }
                        else{
                            showToast(context,"Failed removing items from the cart")
                        }
                    }
            }
        }
    }
    fun clearCartAndAddToOrders(){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            if(it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String,Long> ?:emptyMap()
                val order = OrderModel(
                    id ="ORD_"+UUID.randomUUID().toString().replace("-","").take(10).uppercase(),
                    userId = FirebaseAuth.getInstance().currentUser?.uid!!,
                    time = Timestamp.now(),
                    items = currentCart,
                    status = "Ordered",
                    address = it.result.get("address").toString()
                    )
                Firebase.firestore.collection("orders")
                    .document(order.id).set(order)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            userDoc.update("cartItems", FieldValue.delete())
                        }
                    }
            }

        }
    }

    fun getDiscountPercentage() : Float{
        return 10.0f
    }
    fun getTaxPercentage() : Float{
        return 13.0f
    }
    fun razorpayApiKey() : String{
        return "rzp_test_SEOlemjn7Qg9Sf"
    }

    fun startPayment(amount : Double){
        val checkout = Checkout()
        checkout.setKeyID(razorpayApiKey())
        val options = JSONObject()
        options.put("name","Easy Shopping")
        options.put("description","")
        options.put("amount",amount*100)
        options.put("currency","INR")
        checkout.open(GlobalNavigation.navController.context as Activity,options)

    }
    fun formatDate(timestamp: Timestamp) : String{
        val sdf = SimpleDateFormat("dd MMM yyyy,hh:mm a", Locale.getDefault())
        return sdf.format(timestamp.toDate().time)
    }
    private const val PREF_NAME = "favourite_pref"
    private const val KEY_FAVOURITES = "favourite_list"
    fun addOrRemove(context: Context, productId: String){
        val list = getFavouriteList(context).toMutableSet()
        if(list.contains(productId)){
            list.remove(productId)
            showToast(context,"Items removed to favourites")
        }
        else{
            list.add(productId)
            showToast(context,"Items added to favourites")
        }
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit{
            putStringSet(KEY_FAVOURITES,list)
        }
    }
    fun checkFavourite(context: Context,productId: String) : Boolean{
        if(getFavouriteList(context).contains(productId)){
            return true
        }
        return false
    }
    fun getFavouriteList(context: Context) : Set<String>{
        val prefs = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_FAVOURITES,emptySet()) ?: emptySet()
    }
    fun formatToINR(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        format.maximumFractionDigits = 0
        return format.format(amount)
    }

}