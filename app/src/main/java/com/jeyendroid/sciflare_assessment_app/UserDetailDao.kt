package com.jeyendroid.sciflare_assessment_app

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetail(userDetail : UserDataModel)

    @Update
    suspend fun updateUserDetail(userDetail: UserDataModel)

    @Query("select * from `user_detail`")
    fun getAllUsers() : Flow<List<UserDataModel>>

    @Query("select * from `user_detail` where name=:userName")
    fun getUserDetailByName(userName:String) : List<UserDataModel>

    @Query("update `user_detail` set Mobile=:userNumber")
    suspend fun updateUserNumber(userNumber : String)

    @Query("select * from `user_detail` where _id=:userId")
    suspend fun getUserById(userId : Long) : UserDataModel?

    @Delete
    suspend fun deleteUser(userDetail: UserDataModel)

    @Query("DELETE FROM user_detail")
    suspend fun deleteAll()

}