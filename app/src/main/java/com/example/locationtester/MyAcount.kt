package com.example.locationtester

import android.R.attr.label
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.locationtester.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class MyAcount : Fragment() {
    val firebaseAuth = Firebase.auth
    val firebaseDatabase = FirebaseDatabase.getInstance("https://android-home-app-e721d-default-rtdb.europe-west1.firebasedatabase.app/")
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_acount, container, false)
        getAcountInfo()
        return view
    }
    private fun getAcountInfo(){
        val reference = firebaseDatabase.reference.child("Users")
        val uid = firebaseAuth.currentUser?.uid as String
        reference.child(uid).get().addOnSuccessListener {
            if(it.value is Map<*,*>){
                updateUI(it.value as Map<*, *>)
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun updateUI(value : Map<*,*>){
        val text = (activity as MainActivity).getTextView()
        text.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        text.setText("${value["display_name"]}'s Account")
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
        text.textAlignment = ViewGroup.TEXT_ALIGNMENT_CENTER
        val linear = view?.findViewById<LinearLayout>(R.id.linear2)
        val text2 = (activity as MainActivity).getTextView()
        text2.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        val uid = firebaseAuth.currentUser?.uid as String
        text2.setText("UID: ${uid}")
        text2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
        text2.textAlignment = ViewGroup.TEXT_ALIGNMENT_CENTER
        text2.setOnLongClickListener{
            (activity as MainActivity).CopyUID()
            (activity as MainActivity).sendToastMessage("Copied UID successfully")
        }
        text.setOnLongClickListener{
            (activity as MainActivity).CopyUID()
            (activity as MainActivity).sendToastMessage("Copied UID successfully")
        }
        linear?.addView(text)
        linear?.addView(text2)
        onStart()
    }

}