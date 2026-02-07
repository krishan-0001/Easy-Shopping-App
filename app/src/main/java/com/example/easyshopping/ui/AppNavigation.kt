package com.example.easyshopping.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easyshopping.pages.CategoryProductPage
import com.example.easyshopping.pages.ProductDetailsPage
import com.example.easyshopping.screens.AuthScreen
import com.example.easyshopping.screens.HomeScreen
import com.example.easyshopping.screens.LoginScreen
import com.example.easyshopping.screens.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()
    GlobalNavigation.navController = navController
    val isLoggedin = Firebase.auth.currentUser!=null
    val firstPage = if(isLoggedin) "home" else "auth"
    NavHost(navController = navController, startDestination = firstPage) {
        composable("auth"){
            AuthScreen(modifier,navController)
        }
        composable("login"){
            LoginScreen(modifier,navController)
        }
        composable("signup"){
            SignupScreen(modifier,navController)
        }
        composable("home"){
            HomeScreen(modifier,navController)
        }
        composable("category-products/{categoryId}"){
            val categoryId = it.arguments?.getString("categoryId")
            CategoryProductPage(modifier,categoryId?:"")
        }
        composable("product-details/{productId}"){
            val productId = it.arguments?.getString("productId")
            ProductDetailsPage(modifier, productId ?:"")
        }
    }
}
object GlobalNavigation{
    lateinit var navController: NavHostController
}