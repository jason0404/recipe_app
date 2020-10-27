package com.example.myapplication.auth

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myapplication.respositories.UserRepository
import com.example.myapplication.util.APIException
import com.example.myapplication.util.Coroutines
import com.example.myapplication.util.NoInternetException
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var name: String? = null
    var email: String? = null
    var password: String? = null
    var passwordconfirm: String? = null

    var authListner: AuthListner? = null

    fun getLoggedInUser() = repository.getUser()

    fun deleteUSer() = repository.deleteUser()

    fun onLoginButtonClick(view: View) {
        authListner?.onStarted()
        if(email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListner?.onFailure("Invalid email or password")
            return
        }

        Coroutines.main {
            try {
                val authResponse = repository.userLogin(email!!, password!!)
                authResponse.user?.let{
                    authListner?.onSuccess(it)
                    repository.saveUser(it)
                    return@main
                }
                authListner?.onFailure(authResponse.message!!)
            }catch (e: APIException){
                authListner?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                authListner?.onFailure(e.message!!)
            }
        }
    }

    fun onSignup(view: View) {
        Intent(view.context, SignupActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun onLogin(view: View) {
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun onSignupButtonClick(view: View) {
        authListner?.onStarted()

        if(name.isNullOrEmpty()) {
            authListner?.onFailure("Name is required")
            return
        }

        if(email.isNullOrEmpty()) {
            authListner?.onFailure("Email is required")
            return
        }

        if(password.isNullOrEmpty()) {
            authListner?.onFailure("Please enter a password")
            return
        }

        if(password != passwordconfirm) {
            authListner?.onFailure("Password did not match")
            return
        }

        Coroutines.main {
            try {
                val authResponse = repository.userSignup(name!!, email!!, password!!)
                authResponse.user?.let{
                    authListner?.onSuccess(it)
                    repository.saveUser(it)
                    return@main
                }
                authListner?.onFailure(authResponse.message!!)
            }catch (e: APIException){
                authListner?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                authListner?.onFailure(e.message!!)
            }
        }
    }
}