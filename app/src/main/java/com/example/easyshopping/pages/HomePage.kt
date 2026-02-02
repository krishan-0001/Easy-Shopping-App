package com.example.easyshopping.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshopping.components.BannerView
import com.example.easyshopping.components.CategoriesView
import com.example.easyshopping.components.HeaderView

@Composable
fun HomePage(modifier: Modifier){
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        HeaderView(modifier)
        Spacer(modifier = Modifier.height(10.dp))
        BannerView(modifier = Modifier.height(200.dp))
        Text(text =
            "Categories",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ))
        Spacer(modifier = Modifier.height(10.dp))
        CategoriesView(modifier)
    }
}