package com.example.easyshopping.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import com.example.easyshopping.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshopping.AppUtil
import com.example.easyshopping.model.UserModel
import com.example.easyshopping.ui.GlobalNavigation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun ProfilePage(modifier: Modifier = Modifier){
    val userModel = remember {
        mutableStateOf(UserModel())
    }
    var addressInput by remember{
        mutableStateOf(userModel.value.address)
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val result = it.result.toObject(UserModel::class.java)
                    if(result!=null){
                        userModel.value = result
                        addressInput = userModel.value.address
                    }
                }
            }
    }
    Column(modifier = modifier.fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Your Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Image(painter = painterResource(id = R.drawable.profile_icon),
            contentDescription = "profile icon",
            modifier = Modifier.height(150.dp)
                .fillMaxWidth()
            )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = userModel.value.name,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Address: ", fontSize = 18.sp,
            fontWeight = FontWeight.Medium)
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = addressInput,
            onValueChange = {
                addressInput = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if(addressInput.isNotEmpty()){
                    Firebase.firestore.collection("users")
                        .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                        .update("address",addressInput)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                AppUtil.showToast(context,"Address updated successfully")
                            }
                        }
                }
                else{
                    AppUtil.showToast(context,"Address cannot be empty")
                }
            })
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Email: ", fontSize = 18.sp,
            fontWeight = FontWeight.Medium)
        Text(text = userModel.value.email)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "No. of items in cart: ", fontSize = 18.sp,
            fontWeight = FontWeight.Medium)
        Text(text = userModel.value.cartItems.values.sum().toString())
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "View my orders:", fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .clickable{
                    GlobalNavigation.navController.navigate("orders")
                }.padding(16.dp)
        )
        TextButton(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                val navController = GlobalNavigation.navController
                navController.popBackStack()
                navController.navigate("auth")
            },modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Sign out", fontSize = 18.sp)
        }
    }
}