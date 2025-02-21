package com.admin.shoppinappadmin.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.admin.shoppinappadmin.common.CommonTextField
import com.admin.shoppinappadmin.common.SubmitEvent

import com.admin.shoppinappadmin.presentation.AdminViewModel.AdminViewModel

@Composable
fun AddProduct(adminViewModel: AdminViewModel) {
    val context = LocalContext.current
    val productPageState by adminViewModel.productPageState.collectAsState()

    val productPageTextField by adminViewModel.productPageTextField.collectAsState()

    val buttonEnabledOrDisabled by adminViewModel.finalError.collectAsState()

    val productTextFieldError by adminViewModel.productPageTextFieldError.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        var imageUri by remember { mutableStateOf<Uri?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
                    if (it != null) {
                        imageUri = it
                    }
                }
            if (imageUri != null) {
                adminViewModel.getImageUri(imageUri)
                adminViewModel.validateFinal()
                Box {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clickable { imageUri = null
                                adminViewModel.getImageUri(null)
                                adminViewModel.validateFinal()

                            },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF6464))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            modifier = Modifier.padding(vertical = 3.dp, horizontal = 10.dp),
                            tint = Color.White
                        )
                    }
                }

            } else {

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFF497CE3).copy(alpha = 0.7f))
                        .clickable {
                            launcher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))

                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                }
            }
            Spacer(Modifier.height(20.dp))

            Text(text = "Add Product", fontSize = 40.sp, color = Color.White)
            Spacer(Modifier.height(2.dp))

            CommonTextField(
                text = "Name",
                onTextChanged = {
                    adminViewModel.productPageTextFieldChanged("name",it)
                }, value = productPageTextField.name, isError = productTextFieldError.nameError)

            Spacer(Modifier.height(4.dp))
            Row {
                CommonTextField(
                    text = "Regular Price",
                    onTextChanged = {
                        adminViewModel.productPageTextFieldChanged("rp",it)

                    }, modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    value = productPageTextField.regularPrice,
                    isError = productTextFieldError.regularPriceError

                )
                CommonTextField(
                    text = "Final Price",
                    onTextChanged = {
                        adminViewModel.productPageTextFieldChanged("fp",it)
                    }, modifier = Modifier
                        .weight(1f)
                        .padding(start = 5.dp),
                    value = productPageTextField.finalPrice,
                    isError = productTextFieldError.finalPriceError
                )
            }
            Spacer(Modifier.height(4.dp))

            CommonTextField(
                text = "Description",
                onTextChanged = {
                    adminViewModel.productPageTextFieldChanged("desc",it)

                }, modifier = Modifier.height(100.dp), singleLine = false,
                value = productPageTextField.desc,
                isError = true
            )

            Spacer(Modifier.height(4.dp))
            CommonTextField(
                text = "Category",
                onTextChanged = {
                   adminViewModel.productPageTextFieldChanged("cat",it)
                }, value = productPageTextField.category,isError = true)
            Spacer(Modifier.height(4.dp))
            CommonTextField(
                text = "Available Units",
                onTextChanged = {
                    adminViewModel.productPageTextFieldChanged("availUnit",it)
                }, value = productPageTextField.availableUnits, isError = productTextFieldError.availUnitsError)
            Spacer(Modifier.height(4.dp))
            CommonTextField(
                text = "Created By",
                onTextChanged = {
                   adminViewModel.productPageTextFieldChanged("c",it)
                }, value = productPageTextField.createdBy, isError = productTextFieldError.createdByError)
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = { adminViewModel.submitEvent(SubmitEvent.Submit)},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF508E54)),
                modifier = Modifier.fillMaxWidth(),
                enabled = buttonEnabledOrDisabled

            ) {
                Text("Submit", color = Color.White, fontSize = 15.sp)
            }
        }

        if (productPageState.loading||productPageState.loadingImage) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f)), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else if (productPageState.productError.isNotEmpty()||productPageState.imageError.isNotEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f))
            ) {
                Text(text = productPageState.productError, color = Color.White)
            }
        }

        else {
            if (productPageState.success.isNotEmpty()) {
                Toast.makeText(context, productPageState.success, Toast.LENGTH_SHORT).show()
                imageUri=null
                adminViewModel.clearAll()

            }
        }
    }

}