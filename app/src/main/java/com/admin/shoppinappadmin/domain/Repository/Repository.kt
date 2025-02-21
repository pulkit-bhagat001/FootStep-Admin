package com.admin.shoppinappadmin.domain.Repository

import android.content.ContentResolver
import android.net.Uri
import com.admin.shoppinappadmin.common.PageEvent
import com.admin.shoppinappadmin.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun addProduct(product:Product):Flow<PageEvent<String>>
    suspend fun addProductImage(imageUri:Uri,contentResolver: ContentResolver,folderName:String,uploadPreset:String):Flow<PageEvent<String>>
}