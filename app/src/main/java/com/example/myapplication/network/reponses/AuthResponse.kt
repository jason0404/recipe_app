package com.example.myapplication.network.reponses
import com.example.myapplication.database.entities.User

data class AuthResponse (
    val isSuccessful: Boolean?,
    val message: String?,
    val user: User?
)