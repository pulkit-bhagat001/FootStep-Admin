package com.admin.shoppinappadmin.data.RepoImpl

import android.content.ContentResolver
import android.net.Uri
import com.admin.shoppinappadmin.common.PRODUCT
import com.admin.shoppinappadmin.common.PageEvent
import com.admin.shoppinappadmin.data.network.CloudinaryService
import com.admin.shoppinappadmin.domain.Repository.Repository
import com.admin.shoppinappadmin.domain.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Multipart
import javax.inject.Inject

class RepoImpl @Inject constructor(private val firestore: FirebaseFirestore,private val cloudinary:CloudinaryService) :Repository{
    override suspend fun addProduct(product: Product): Flow<PageEvent<String>> = callbackFlow {
        trySend(PageEvent.Loading)
        firestore.collection(PRODUCT).add(product).addOnSuccessListener{
            trySend(PageEvent.Success("Product added Successfully"))
        }.addOnFailureListener { trySend(PageEvent.Error(it.toString())) }
        awaitClose { close() }

    }

    override suspend fun addProductImage(
        imageUri: Uri,
        contentResolver: ContentResolver
        ,folderName:String,uploadPreset:String
    ): Flow<PageEvent<String>> = callbackFlow {

        val inputStream=contentResolver.openInputStream(imageUri)
        val requestFile =inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
        val filePart=MultipartBody.Part.createFormData("file","image.jpg",requestFile!!)
        val folderRequest=folderName.toRequestBody("text/plain".toMediaTypeOrNull())
        val uploadPresetRequest=uploadPreset.toRequestBody("text/plain".toMediaTypeOrNull())
        trySend(PageEvent.Loading)

        try {
            val response=cloudinary.uploadImage(filePart,folderRequest,uploadPresetRequest)
            if(response.isSuccessful){
               response.body()?.secure_url?.let {
                   trySend(PageEvent.Success(it))
               }
            }else{
                trySend(PageEvent.Error("Upload not Successful"))
            }
        }catch (e:Exception){
            trySend(PageEvent.Error(e.toString()))

        }
        finally {
            inputStream?.close()
        }
        awaitClose{
            close()
        }
    }


}


