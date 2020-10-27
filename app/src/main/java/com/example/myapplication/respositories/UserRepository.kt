package com.example.myapplication.respositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.entities.User
import com.example.myapplication.network.MyAPI
import com.example.myapplication.network.NetworkConnectionInterceptor
import com.example.myapplication.network.SafeAPIRequest
import com.example.myapplication.network.reponses.AuthResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(
    private val api: MyAPI,
    private val db: AppDatabase
) : SafeAPIRequest(){

    suspend fun userLogin(
        email: String,
        password: String
    ) : AuthResponse {
        return apiRequest { api.userLogin(email, password) }
    }

    suspend fun userSignup(
        name:String,
        email:String,
        password:String
    ) : AuthResponse {
        return apiRequest { api.userSignup(name, email, password) }
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun deleteUser() = db.getUserDao().deleteUser()
    fun getUser() = db.getUserDao().getUser()
}