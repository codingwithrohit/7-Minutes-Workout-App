package com.example.a7minutesworkoutapp.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a7minutesworkoutapp.R
import com.example.a7minutesworkoutapp.databinding.ActivityCalculateBmiBinding

class CalculateBmi : AppCompatActivity() {
    private var binding: ActivityCalculateBmiBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCalculateBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding?.appBar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.appBar?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.viewAns?.visibility = View.GONE

        //use set on checked change listener
        binding?.btnCalculate?.setOnClickListener {
            if (binding?.rbMetric?.isChecked == true) {
                if (binding?.tilWeight?.editText?.text.toString().isNotEmpty() &&
                    binding?.tilHeight?.editText?.text.toString().isNotEmpty()) {
                    calculateBMI()
                } else {
                    Toast.makeText(this, "Please enter your weight and height", Toast.LENGTH_SHORT).show()
                }
            } else if (binding?.rbUs?.isChecked == true) {
                if (binding?.tilWeight?.editText?.text.toString().isNotEmpty() &&
                    binding?.edtFoot?.text.toString().isNotEmpty() &&
                    binding?.edtInch?.text.toString().isNotEmpty()) {
                    calculateUsUnitBMI()
                } else {
                    Toast.makeText(this, "Please enter your weight, feet and inches", Toast.LENGTH_SHORT).show()
                }
            }
        }



        binding?.llUsUnits?.visibility = View.GONE
        binding?.rbUs?.setOnClickListener {
            binding?.tilHeight?.visibility = View.GONE
            binding?.llUsUnits?.visibility = View.VISIBLE
        }

        binding?.rbMetric?.setOnClickListener {
            binding?.llUsUnits?.visibility = View.GONE
            binding?.tilHeight?.visibility = View.VISIBLE
        }


    }
    @SuppressLint("SetTextI18n")
    private fun calculateBMI(){
        binding?.viewAns?.visibility = View.VISIBLE
        val weight = binding?.tilWeight?.editText?.text.toString().toFloat()
        val height = binding?.tilHeight?.editText?.text.toString().toFloat() / 100
        val bmi = weight / (height * height)
        binding?.showAns?.text = String.format("%.2f", bmi)

        getResult(bmi)
    }

    private fun calculateUsUnitBMI(){
        binding?.viewAns?.visibility = View.VISIBLE
        val weight = binding?.tilWeight?.editText?.text.toString().toFloat()
        val feet = binding?.edtFoot?.text.toString().toFloat()
        val inch = binding?.edtInch?.text.toString().toFloat()

        val heightInInches = (feet * 12) + inch
        val heightInMeters = heightInInches * 0.0254
        val bmi = (weight / (heightInMeters * heightInMeters)).toFloat()
        binding?.showAns?.text = String.format("%.2f", bmi)
        getResult(bmi)
    }

    @SuppressLint("SetTextI18n")
    private fun getResult(bmi: Float): Unit{
        when{
            bmi<18.5 ->{
                binding?.showCategory?.text = "Underweight"
                binding?.showDescription?.text = "You are underweight. A balanced diet and proper nutrition may help you reach a healthier weight."
            }
            18.5<=bmi && bmi<=24.9 ->{
                binding?.showCategory?.text = "Normal"
                binding?.showDescription?.text = "You are in the healthy weight range. Keep up your balanced lifestyle!"
            }
            bmi>25.0 && bmi<29.9 ->{
                binding?.showCategory?.text = "Overweight"
                binding?.showDescription?.text = "You are overweight. Regular exercise and healthy eating can help you get back to a healthier range."
            }
            bmi>30.0 && bmi<34.9 ->{
                binding?.showCategory?.text = "Obesity"
                binding?.showDescription?.text = "You are in Obesity (Class I). Managing your diet and increasing physical activity is recommended."
            }
            bmi>35.0 && bmi<39.9 ->{
                binding?.showCategory?.text = "Severe Obesity"
                binding?.showDescription?.text = "You are in Obesity (Class II). It’s important to take medical advice and make lifestyle changes to reduce health risks."
            }
            bmi>40.0 ->{
                binding?.showCategory?.text = "Morbid Obesity"
                binding?.showDescription?.text = "You are in severe Obesity (Class III). Please consult a healthcare professional for proper guidance."
            }
        }
    }
}