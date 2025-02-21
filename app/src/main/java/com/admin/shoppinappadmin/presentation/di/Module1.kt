package com.admin.shoppinappadmin.presentation.di

import com.admin.shoppinappadmin.data.RepoImpl.RepoImpl
import com.admin.shoppinappadmin.data.network.CloudinaryService
import com.admin.shoppinappadmin.domain.Repository.Repository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module1{
    @Provides
    @Singleton
    fun getRepo(firestore: FirebaseFirestore,cloudinaryService: CloudinaryService):Repository=RepoImpl(firestore,cloudinaryService)
}

