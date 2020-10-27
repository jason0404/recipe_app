package com.example.myapplication.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class Login : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

//        val viewModel = ViewModelProviders.of(this)
//            .get(AuthViewModel::class.java)
//
//        binding.viewModel = viewModel

        return binding.root
        // Inflate the layout for this fragment
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
