package com.example.myapplication.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.entities.User
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.network.MyAPI
import com.example.myapplication.network.NetworkConnectionInterceptor
import com.example.myapplication.respositories.UserRepository
import com.example.myapplication.util.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.kodein.di.android.kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

@InternalCoroutinesApi
class LoginActivity : AppCompatActivity(), AuthListner, KodeinAware {


    override val kodein by kodein()

    private val factory: AuthViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.authListner = this

        viewModel.getLoggedInUser().observe(this, Observer { user ->
            if(user != null){
                Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })
    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(user: User) {
        progress_bar.hide()
    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        root_layout.snackbar(message)
    }
}
