package com.example.locationtester

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInSystem : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_system)
        title = "Log In"
        auth = Firebase.auth

        findViewById<Button>(R.id.register).setOnClickListener {
            val intent = Intent(this@LogInSystem, RegisterSystem::class.java);
            startActivity(intent)
        }
        findViewById<Button>(R.id.login).setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = findViewById<TextView>(R.id.Email1).text.toString()
        val password = findViewById<TextView>(R.id.Password1).text.toString()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){task->
            if(task.isSuccessful){
                Log.d(TAG, "signInWithEmail:success")
                val intent = Intent(this@LogInSystem, MainActivity::class.java);
                startActivity(intent)
            }else {
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
