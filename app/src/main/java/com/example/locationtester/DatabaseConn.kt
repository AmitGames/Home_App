package com.example.locationtester

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.content.res.Resources
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.text.Charsets.UTF_8

class DatabaseConn {
    private var auth = Firebase.auth
    private val firebaseDatabaseReference = FirebaseDatabase.getInstance("https://android-home-app-e721d-default-rtdb.europe-west1.firebasedatabase.app/")
    private val uid = auth.currentUser?.uid

    fun updateJson(){
        val reference = firebaseDatabaseReference.reference.child("Homes")


//        Log.w(TAG, "List is ${list.toString()}")
//        var json = "["
//        var i = 0
//        val imax = list.size - 1
//        for(item in list) {
//            var o : Map<*, *> = mapOf("" to "")
//            val result = reference.child(item.toString()).get().addOnSuccessListener {
//                o = it.value as Map<*, *>
//                val gson: Gson = Gson()
//                val json_string = gson.toJson(o)
//                if(i == imax){
//                    json += "$json_string]"
//                }else{
//                    json += "$json_string,"
//                }
//                Log.w(TAG, "Json is: ${json}")
//                Log.w(TAG, "Json is: ${json_string}")
//            }
//        }

        //Thread.sleep(5000)



    }



}