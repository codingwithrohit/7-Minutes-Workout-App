package com.example.a7minutesworkoutapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a7minutesworkoutapp.databinding.ActivityMainBinding
import com.example.a7minutesworkoutapp.presentation.CalculateBmi
import com.example.a7minutesworkoutapp.presentation.ExerciseActivity
import com.example.a7minutesworkoutapp.presentation.HistoryActivity

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding?.flStart?.setOnClickListener {
            if(binding?.edtName?.text?.isEmpty() == true){
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this, ExerciseActivity::class.java)
                intent.putExtra("UserName", binding?.edtName?.text.toString())
                startActivity(intent)
            }

        }

        binding?.flBmi?.setOnClickListener {
            val intent = Intent(this, CalculateBmi::class.java)
            startActivity(intent)
        }

        binding?.flHistory?.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}