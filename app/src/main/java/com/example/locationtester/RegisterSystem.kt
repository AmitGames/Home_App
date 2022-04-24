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
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class RegisterSystem : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "SignUp"
        setContentView(R.layout.activity_register_system)
        auth = Firebase.auth
        findViewById<Button>(R.id.SignUp).setOnClickListener {
            signUp()
        }

    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this@RegisterSystem, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun signUp(){
        val email : String = findViewById<TextView>(R.id.Email).text.toString()
        val password : String = findViewById<TextView>(R.id.Password).text.toString()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(){ task->
            if(task.isSuccessful){
                Log.d(TAG,"createUserWithEmail:success")
                updateUserInfo()
                val intent = Intent(this@RegisterSystem, MainActivity::class.java)
                startActivity(intent)
            }else{
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateUserInfo(){
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = findViewById<TextView>(R.id.username).text.toString()
        }
        user!!.updateProfile(profileUpdates).addOnCompleteListener{task->
            if(task.isSuccessful){
                Log.d(TAG, "User profile updated.")
            }
        }
    }
}