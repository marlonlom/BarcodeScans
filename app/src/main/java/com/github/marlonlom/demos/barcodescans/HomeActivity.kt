package com.github.marlonlom.demos.barcodescans

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

/**
 * Home activity class.

 * @author marlonlom
 */
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupTabs()
    }

    private fun setupTabs() {
        val viewPager = findViewById(R.id.viewpager_main) as ViewPager
        setupViewPager(viewPager)

        val tabLayout = findViewById(R.id.tablayout_main) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DummyBarcodeScannerFragment())
        adapter.addFragment(CameraBarcodeScannerFragment())
        viewPager.adapter = adapter
    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar_main) as Toolbar
        setSupportActionBar(toolbar)
    }
}
