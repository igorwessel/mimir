package com.example.mugiwara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.tabs.TabLayout

private fun getCurrentImg(tab: TabLayout.Tab?): Int {
    return when (tab?.text.toString()) {
        "Normal" -> R.drawable.bg_luffy
        "Gear 2" -> R.drawable.bg_gear_2
        "Gear 3" -> R.drawable.bg_gear_3
        "Gear 4" -> R.drawable.bg_gear_4
        else -> R.drawable.bg_gear_2
    }
}


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        var tabL: TabLayout = findViewById<TabLayout>(R.id.luffy_tab)
        var luffyImage: ImageView = findViewById<ImageView>(R.id.bg_luffy)
        var navigateScreen = Intent(this, TelaNovaActivity::class.java)
        var btn = findViewById<Button>(R.id.navigate_another_screen)

        navigateScreen.putExtra("name", "luffy")

        btn.setOnClickListener { _ -> startActivity(navigateScreen)}


        tabL.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                luffyImage.setImageResource(getCurrentImg(tab))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}




