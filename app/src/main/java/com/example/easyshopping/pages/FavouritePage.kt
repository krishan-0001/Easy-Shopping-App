package com.example.easyshopping.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshopping.AppUtil
import com.example.easyshopping.components.ProductItemView
import com.example.easyshopping.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun FavouritePage(modifier: Modifier = Modifier){
    val productList = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val favouriteList = AppUtil.getFavouriteList(context)
        if(favouriteList.isEmpty()){
            productList.value = emptyList()
        }
        else{
            Firebase.firestore.collection("data").document("stocks")
                .collection("products")
                .whereIn("id",favouriteList.toList())
                .get().addOnCompleteListener() {
                    if(it.isSuccessful){
                        val resultList = it.result.documents.mapNotNull {doc->
                            doc.toObject(ProductModel::class.java)
                        }
                        productList.value = resultList
                    }
                }
        }
    }
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(
            text = "Your Favourites", style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (productList.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(productList.value.chunked(2)) { rowItems ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        rowItems.forEach {
                            ProductItemView(product = it, modifier = Modifier.weight(1f))
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No Favourite items here", fontSize = 32.sp)
            }
        }


    }
}