package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.myapplication.common.Adapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_recipe_details.*

class RecipeDetails : AppCompatActivity() {

    private lateinit var rid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val fragmentAdapter = Adapter.Adapter(supportFragmentManager)

        fragmentAdapter.addFragment(ingredient_fragment(), "")
        fragmentAdapter.addFragment(fragment_direction(), "")

        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.setText("Ingredients")
        tabLayout.getTabAt(1)?.setText("Directions")

        rid = intent.getStringExtra("recipe_id").orEmpty()

        FirebaseStorage.getInstance().reference.child("pics/${rid}.jpg")
            .downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(this).load(task.result).into(bg)
                    Glide.with(this).load(task.result).into(profile_img)
                }
            }

    }
    fun getData(): String{
        rid = intent?.getStringExtra("recipe_id").toString()
        return intent?.getStringExtra("recipe_id").toString()
    }
}
