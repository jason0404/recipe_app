package com.example.myapplication.auth

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.myapplication.database.entities.User

interface AuthListner {
    fun onStarted()
    fun onSuccess(user: User)
    fun onFailure(message: String)
}