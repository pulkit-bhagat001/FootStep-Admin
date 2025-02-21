package com.admin.shoppinappadmin.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.admin.shoppinappadmin.presentation.AdminViewModel.AdminViewModel
import kotlinx.serialization.Serializable

@Composable
fun AdminApp(adminViewModel: AdminViewModel= hiltViewModel()) {

    Scaffold(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = ProductScreen,
            modifier = Modifier.padding(it)
        ) {
            composable<ProductScreen> { AddProduct(adminViewModel) }
        }
    }


}

@Serializable
object CategoryScreen

@Serializable
object ProductScreen