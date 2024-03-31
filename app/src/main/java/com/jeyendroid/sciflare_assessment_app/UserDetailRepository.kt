package com.jeyendroid.sciflare_assessment_app

import kotlinx.coroutines.flow.Flow

interface UserDetailRepository {

    suspend fun addUser(userDataModel: UserDataModel)

    suspend fun getAllUsers(): Flow<List<UserDataModel>>

    suspend fun updateUserDetail(userDataModel: UserDataModel)

    suspend fun deleteUser(userDataModel: UserDataModel)

    suspend fun deleteAllUsers()

    suspend fun getUserById(id : Long)

}