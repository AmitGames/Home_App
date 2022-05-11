package com.example.locationtester




import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup


import android.widget.Button
import android.widget.TextView
import android.widget.Toast


import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialTextInputPicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.core.view.View
import com.google.firebase.ktx.Firebase



class LoginFragment : Fragment() {

    private var auth = Firebase.auth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_log_in_system, container, false)

        view.findViewById<MaterialButton>(R.id.login_btn).setOnClickListener{
            val email = view.findViewById<TextInputEditText>(R.id.Email2).text!!
            val password = view.findViewById<TextInputEditText>(R.id.Password2).text!!
            login(email, password)
        }
        view.findViewById<MaterialButton>(R.id.register).setOnClickListener{
            (activity as MainActivity).navigateTo(RegisterSystem(), false)
        }
        return view
    }

    private fun isPasswordValid(text: Editable?): Boolean {
        return text != null && text.length >= 8
    }


    private fun login(email: Editable?, password: Editable?) {

        auth.signInWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithEmail:success")
                (activity as NavigationHost).navigateTo(MyHomesFragment(), false)

            } else {
                Log.w(TAG, "signInWithEmail:failure", task.exception)


            }
        }
    }
}

