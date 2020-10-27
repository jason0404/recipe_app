package com.example.myapplication.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.respositories.UserRepository
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory (
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }
}