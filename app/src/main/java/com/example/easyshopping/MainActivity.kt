package com.example.easyshopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import android.app.AlertDialog
import com.example.easyshopping.ui.AppNavigation
import com.example.easyshopping.ui.GlobalNavigation
import com.example.easyshopping.ui.theme.EasyShoppingTheme
import com.razorpay.PaymentResultListener

class MainActivity : ComponentActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyShoppingTheme {
                Scaffold { innerPadding ->
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        AppUtil.clearCartAndAddToOrders()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Payment Successful")
            .setMessage("Your Payment is Successful and your order is successfully placed")
            .setPositiveButton("OK"){ _,_->
                val navController = GlobalNavigation.navController.navigate("home")
            }
            .setCancelable(false)
            .show()

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        AppUtil.showToast(this,"Payment Failed")
    }
}
