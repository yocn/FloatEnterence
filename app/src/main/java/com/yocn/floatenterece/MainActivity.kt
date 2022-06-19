package com.yocn.floatenterece

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this
        findViewById<TextView>(R.id.tv_show).setOnClickListener {
            val floatMenuGiftbag: FloatMenuGiftbag = FloatMenuGiftbag.getInstance()
            floatMenuGiftbag.showFloatMenu(context)
            FloatMenuManager.getInstance().showFloatMenu(context)
        }
    }
}