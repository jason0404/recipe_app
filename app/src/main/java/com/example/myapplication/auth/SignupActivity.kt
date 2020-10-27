package com.example.myapplication.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.database.entities.User
import com.example.myapplication.databinding.ActivitySignupBinding
import com.example.myapplication.util.hide
import com.example.myapplication.util.show
import com.example.myapplication.util.snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

@InternalCoroutinesApi
class SignupActivity : AppCompatActivity(), AuthListner, KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
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
