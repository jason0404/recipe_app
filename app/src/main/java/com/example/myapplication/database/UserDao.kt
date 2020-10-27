package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.database.entities.CURRENT_USER_ID
import com.example.myapplication.database.entities.User

@Dao
interface UserDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: User): Long
    
    @Query("SELECT * FROM user WHERE uid = $CURRENT_USER_ID")
    fun getUser(): LiveData<User>

    @Query("DELETE FROM user WHERE uid = $CURRENT_USER_ID")
    fun deleteUser()
    
}