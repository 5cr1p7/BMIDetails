package com.ramilkapev.bmidetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.MobileAds
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    lateinit var mToolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BMIDetails)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        MobileAds.initialize(this) { }
    }

    private fun init() {
        mToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(mToolbar)
        mToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}