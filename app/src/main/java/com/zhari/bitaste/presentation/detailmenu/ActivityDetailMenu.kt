package com.zhari.bitaste.presentation.detailmenu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhari.bitaste.R
import com.zhari.bitaste.model.product.Menu

class ActivityDetailMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_menu)
    }

    companion object {
        const val MENU_KEY = "MENU_KEY"
        fun startActivity(context: Context, product: Menu?) {
            val intent = Intent(context, ActivityDetailMenu::class.java)
            intent.putExtra(MENU_KEY, product)
            context.startActivity(intent)
        }
    }
}
