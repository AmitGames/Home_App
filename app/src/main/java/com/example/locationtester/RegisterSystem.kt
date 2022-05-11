package com.example.locationtester

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.logging.Logger.global

class RegisterSystem : Fragment(){
    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_register_system, container, false)

        view.findViewById<MaterialButton>(R.id.sign_up).setOnClickListener(){
            val email = view.findViewById<TextInputEditText>(R.id.email_edit).text
            val password = view.findViewById<TextInputEditText>(R.id.password_edit).text
            val username = view.findViewById<TextInputEditText>(R.id.username_edit).text
            signUp(email, password, username)
        }

        return view
    }

    public override fun onStart() {
        super.onStart()

    }
    private fun signUp(email : Editable?, password : Editable?, username : Editable?){

        auth.createUserWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener(){ task->
            if(task.isSuccessful){
                Log.d(TAG,"createUserWithEmail:success")
                updateUserInfo(username)
                (activity as MainActivity).navigateTo(MyHomesFragment(), false)
            }else{
                Log.w(TAG, "createUserWithEmail:failure", task.exception)

            }
        }
    }
    private fun updateUserInfo(username: Editable?){
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = username.toString()
        }
        user!!.updateProfile(profileUpdates).addOnCompleteListener{task->
            if(task.isSuccessful){
                Log.d(TAG, "User profile updated.")
            }
        }
    }

}