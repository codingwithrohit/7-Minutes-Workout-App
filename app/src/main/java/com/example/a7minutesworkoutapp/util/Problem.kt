package com.example.a7minutesworkoutapp.util

class Problem{

    fun validPass(pass: String): String{
        return when{
            pass.isEmpty() ->{
                "Password is required"
            }
            pass.length<6 ->{
                "Length of password should be greater than 6"
            }
            pass.length>15 ->{
                "Length of password should be less than 15"
            }
            else ->{
                "Password is valid"
            }
        }
    }
}