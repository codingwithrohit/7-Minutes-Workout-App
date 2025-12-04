package com.example.a7minutesworkoutapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.a7minutesworkoutapp.MainActivity
import com.example.a7minutesworkoutapp.R
import com.example.a7minutesworkoutapp.WorkOutApp
import com.example.a7minutesworkoutapp.data.dao.HistoryDao
import com.example.a7minutesworkoutapp.data.entity.HistoryEntity
import com.example.a7minutesworkoutapp.databinding.ActivityFinishExerciseBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FinishExercise : AppCompatActivity() {
    private var binding: ActivityFinishExerciseBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFinishExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding?.topAppBar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.topAppBar?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding?.btnFinish?.setOnClickListener {
            finish()
        }

        val dao = (application as WorkOutApp).db.historyDao()
        addToDatabase(dao)

    }

    override fun finish(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun addToDatabase(historyDao: HistoryDao){
        val c = Calendar.getInstance()
        val dateTime = c.time
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)
        val userName = intent.getStringExtra("UserName") ?: "Unknown"
        lifecycleScope.launch {
            historyDao.insert(
                HistoryEntity(
                    id = 0,
                    date = date,
                    name = userName
                )
            )
        }
    }
}