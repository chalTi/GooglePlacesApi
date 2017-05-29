package com.example.wentongwang.mygoogleplacesapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_kotlin.*

class KotlinActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        text.text = getString(R.string.app_name)
        var toto : Int ? = 1
        when (toto) {
            1 -> text.text = toto.toString()
        }

        var b : Boolean = false
        b = toto!! > 1

        toto = if (b) {
            0
        } else {
            1
        }


        text.text = toto.toString()
    }
}
