package com.example.myapplication.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.common.Adapter
import com.example.myapplication.ingredient_fragment
import com.example.myapplication.page3
import com.example.myapplication.util.toast
import com.google.android.material.tabs.TabLayout

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_auth)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val fragmentAdapter = Adapter.Adapter(supportFragmentManager)

        fragmentAdapter.addFragment(Login(), "")
        fragmentAdapter.addFragment(SignUp(), "")

        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.setText("Login")
        tabLayout.getTabAt(1)?.setText("SignUp")
    }
}
