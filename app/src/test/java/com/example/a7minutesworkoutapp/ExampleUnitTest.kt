package com.example.a7minutesworkoutapp

import com.example.a7minutesworkoutapp.util.Problem
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//class ExampleUnitTest {
//    @Test
//    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//    }
//}
class ProblemTest {
    @Test
    fun validatePassword_emptyPassword_exp_reqPass() {
        val problem = Problem()
        val result = problem.validPass("")
        assertEquals("Password is required", result)
    }

    @Test
    fun validatePassword_shortPass_exp_validationMsg() {
        val problem = Problem()
        val result = problem.validPass("ab")
        assertEquals("Length of password should be greater than 6", result)
    }
}