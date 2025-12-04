package com.example.a7minutesworkoutapp.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkoutapp.R
import com.example.a7minutesworkoutapp.WorkOutApp
import com.example.a7minutesworkoutapp.adapter.HistoryAdapter
import com.example.a7minutesworkoutapp.data.dao.HistoryDao
import com.example.a7minutesworkoutapp.data.entity.HistoryEntity
import com.example.a7minutesworkoutapp.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding: ActivityHistoryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding?.topAppBar)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.topAppBar?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val dao = (application as WorkOutApp).db.historyDao()
        getAllData(dao)


    }

    private fun getAllData(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchAllData().collect {entityList->
                if(entityList.isNotEmpty()){
                    binding?.textView?.visibility = View.VISIBLE
                    binding?.rvDates?.visibility = View.VISIBLE
                    binding?.tvNoExercise?.visibility = View.INVISIBLE

                    binding?.rvDates?.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    val entity = ArrayList<HistoryEntity>()
                    for(entityItem in entityList){
                        entity.add(HistoryEntity(entityItem.id, entityItem.date, entityItem.name))
                    }
                    val historyAdapter = HistoryAdapter(entity) { entityToDelete ->
                        lifecycleScope.launch {
                            historyDao.delete(entityToDelete) // assumes you have a delete() in DAO
                        }
                    }
                    binding?.rvDates?.adapter = historyAdapter
                }
                else{
                    binding?.textView?.visibility = View.INVISIBLE
                    binding?.rvDates?.visibility = View.INVISIBLE
                    binding?.tvNoExercise?.visibility = View.VISIBLE
                }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}