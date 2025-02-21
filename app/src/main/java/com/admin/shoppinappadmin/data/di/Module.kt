package com.admin.shoppinappadmin.data.di


import com.admin.shoppinappadmin.common.BASE_URL_CLOUDINARY
import com.admin.shoppinappadmin.data.network.CloudinaryService
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun firebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun getCloudinaryInstance(): CloudinaryService {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL_CLOUDINARY).addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(CloudinaryService::class.java)
    }
}