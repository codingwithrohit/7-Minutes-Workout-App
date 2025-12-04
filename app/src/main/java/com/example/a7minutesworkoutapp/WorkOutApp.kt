package com.example.a7minutesworkoutapp

import android.app.Application
import com.example.a7minutesworkoutapp.data.HistoryDatabase

class WorkOutApp: Application() {
    val db by lazy{
        HistoryDatabase.getInstance(this)
    }
}