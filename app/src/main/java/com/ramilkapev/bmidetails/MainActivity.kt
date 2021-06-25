package com.ramilkapev.bmidetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.doOnTextChanged

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val et: EditText = findViewById(R.id.mass_index_et)

        et.doOnTextChanged { text, start, before, count ->
            if (et.text.contains(".")) {

            }
            else {
                et.textSize = 64f
            }
        }
    }
}