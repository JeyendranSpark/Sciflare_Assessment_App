package com.jeyendroid.sciflare_assessment_app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_detail")
data class UserDataModel(
    @PrimaryKey var _id : String = "0",
    var Name: String = "",
    var Email: String = "",
    var Mobile : String = "",
    var Gender : String = "") {}

