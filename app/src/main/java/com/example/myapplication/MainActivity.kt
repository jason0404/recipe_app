package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.common.Adapter.Adapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabs = findViewById<TabLayout>(R.id.tab)
        val viewPage = findViewById<ViewPager>(R.id.pager)
        val fragmentAdapter = Adapter(supportFragmentManager)

        fragmentAdapter.addFragment(page1(), "")
        fragmentAdapter.addFragment(page2(), "")

        viewPage.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPage)
        tabs.getTabAt(0)?.setIcon(R.drawable.ic_home_black_24dp)
        tabs.getTabAt(1)?.setIcon(R.drawable.ic_dashboard_black_24dp)
        tabs.getTabAt(2)?.setIcon(R.drawable.ic_videogame_asset_black_24dp)

    }
}
