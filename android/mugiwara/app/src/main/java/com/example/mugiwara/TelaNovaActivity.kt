package com.example.mugiwara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

class TelaNovaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_nova)


        var btn = findViewById<Button>(R.id.triste)

        var name: String? = getIntent().getStringExtra("name")


        btn.setOnClickListener { l -> Toast.makeText(this, name, Toast.LENGTH_SHORT).show()}

    }
}