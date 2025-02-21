package com.admin.shoppinappadmin.data.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface CloudinaryService{
    @Multipart
    @POST("image/upload")
    suspend fun uploadImage(
        @Part file:MultipartBody.Part,
        @Part("folder") folder:RequestBody,
        @Part("upload_preset") uploadPreset:RequestBody
    ):Response<CloudinaryResponse>
}
data class CloudinaryResponse(
    val secure_url:String
)