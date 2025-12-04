package com.example.a7minutesworkoutapp.presentation

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkoutapp.presentation.FinishExercise
import com.example.a7minutesworkoutapp.R
import com.example.a7minutesworkoutapp.adapter.ExerciseStatusAdapter
import com.example.a7minutesworkoutapp.databinding.ActivityExcerciseBinding
import com.example.a7minutesworkoutapp.databinding.CustomDialogBackConfirmationBinding
import com.example.a7minutesworkoutapp.domain.model.ExerciseModal
import com.example.a7minutesworkoutapp.util.Constants
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExcerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var exerciseTimer: CountDownTimer? = null
    private val totalTime = 1000L
    private val interval = 1000L

    private var exerciseList: ArrayList<ExerciseModal>? = null
    private var currentExercisePosition: Int = -1
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
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
            customDialogForBackButton()
        }

        exerciseList = Constants.getDefaultExercise()

        binding?.progressBar?.max = (totalTime / interval).toInt()

        startRestTimer()

        tts = TextToSpeech(this, this)

        setUpExerciseStatusAdapter()
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding = CustomDialogBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        customDialog.window?.setBackgroundDrawable((Color.TRANSPARENT.toDrawable()))
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
    private fun setUpExerciseStatusAdapter(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun startRestTimer(){
        //show rest timer UI
        showRestUI()
        //show the upcoming exercise name
        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition+1].name

        try{
            val soundUri = Uri.parse("android.resource://com.example.a7minutesworkoutapp/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext,soundUri)
            player?.isLooping = false
            player?.start()
        }
        catch (e: Exception){
            e.printStackTrace()
        }


        restTimer?.cancel()
        restTimer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = (millisUntilFinished / interval).toInt()
                binding?.progressBar?.progress = progress
                binding?.tvTimer?.text = (progress+1).toString()
            }
            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].isSelected = true
                exerciseAdapter!!.notifyDataSetChanged()
                if(currentExercisePosition < exerciseList?.size!!){
                    startExerciseTimer()
                }
            }
        }.start()
    }

    private fun startExerciseTimer(){
        val totalTime = 1000L  // 30 sec
        val interval = 1_000L

        showExerciseUI()

        exerciseTimer?.cancel()

        speakOut(exerciseList!![currentExercisePosition].name)
        binding?.ivExerciseImage?.setImageResource(exerciseList!![currentExercisePosition].image)
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].name

        exerciseTimer = object : CountDownTimer(totalTime, interval){
            override fun onTick(millisUntilFinished: Long){
                val progress = (millisUntilFinished / interval).toInt()
                binding?.progressBarExercise?.progress = progress
                binding?.tvTimerExercise?.text = (progress+1).toString()
            }
            override fun onFinish(){
                exerciseList!![currentExercisePosition].isSelected = false
                exerciseList!![currentExercisePosition].isCompleted = true
                exerciseAdapter!!.notifyDataSetChanged()
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    startRestTimer()
                }
                else{
                    val name = intent.getStringExtra("UserName")
                    val intent = Intent(this@ExerciseActivity, FinishExercise::class.java)
                    intent.putExtra("UserName", name)
                    startActivity(intent)
                }
            }
        }.start()
    }
    private fun showRestUI(){
        //Hide the exercise timer ui
        binding?.flProgressBarExercise?.visibility = View.INVISIBLE
        binding?.tvTimerExercise?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.ivExerciseImage?.visibility = View.INVISIBLE

        //Now show the rest time ui
        binding?.flProgressBar?.visibility = View.VISIBLE
        binding?.tvReady?.visibility = View.VISIBLE
        binding?.tvTimer?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
    }

    private fun showExerciseUI(){
        //Hide rest timer ui
        binding?.flProgressBar?.visibility = View.INVISIBLE
        binding?.tvReady?.visibility = View.INVISIBLE
        binding?.tvTimer?.visibility = View.INVISIBLE
        binding?.tvUpcomingExercise?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

        //Now show the exercise timer ui
        binding?.flProgressBarExercise?.visibility = View.VISIBLE
        binding?.tvTimerExercise?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.ivExerciseImage?.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        restTimer?.cancel()
        exerciseTimer?.cancel()
        if(tts!=null){
            tts?.stop()
            tts?.shutdown()
        }
        player!!.stop()
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ){
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
            }
            else{
                Log.e("TTS", "Initialization failed")
            }
        }
    }
    private fun speakOut(text: String){
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}