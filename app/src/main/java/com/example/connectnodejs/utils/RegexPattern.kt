package com.example.connectnodejs.utils

class RegexPattern {

    companion object{
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        val passwordRegex = "^.{6,}\$"

        fun isValidEmail(email: String): Boolean {
            return email.matches(emailRegex.toRegex())
        }

        fun isValidPassword(password:String):Boolean{
            return passwordRegex.matches(password.toRegex())
        }
    }

}