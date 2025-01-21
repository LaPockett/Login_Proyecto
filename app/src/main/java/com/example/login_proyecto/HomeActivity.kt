package com.example.login_proyecto

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.login_proyecto.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.menuNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.inicio -> fragmentos(Inicio())
                R.id.calendario -> fragmentos(Calendario())
                R.id.mascota -> fragmentos(Mascota())
                R.id.social -> fragmentos(Social())
            }
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    fun fragmentos (fragmento: Fragment){
        val fragment_manager = supportFragmentManager
        val transaction = fragment_manager.beginTransaction()
        transaction.replace(R.id.frame_layout,fragmento).commit()
    }
}