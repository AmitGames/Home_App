package com.example.locationtester

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase


class AddMemberFragment : Fragment() {

    private val firebaseDatabaseReference = FirebaseDatabase.getInstance("https://android-home-app-e721d-default-rtdb.europe-west1.firebasedatabase.app/")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_member, container, false)
        val args = this.arguments
        val inputData = args?.get("data")
        val input = inputData.toString()
        view.findViewById<MaterialButton>(R.id.add_member_button).setOnClickListener{
            val user = view.findViewById<TextInputEditText>(R.id.uid_name_edit).text.toString()
            addMember(user, input, view)
        }
        return view
    }

    private fun addMember(user:String, input:String , view: View){
        val reference = firebaseDatabaseReference.reference.child("Users")
        reference.child(user).get().addOnCompleteListener{
            if(it.isSuccessful){
                if(!it.result.exists() || user == ""){
                    view.findViewById<TextInputEditText>(R.id.uid_name_edit).setError("The UID Dosen't Exist")

                }else{
                    writeData(user, input)
                }
            }
        }

    }
    private fun writeData(user: String, input: String){
        val reference = firebaseDatabaseReference.reference
        reference.child("Users").child(user).child("homes_member").child(input).child("is_admin").setValue(false)
        reference.child("Homes").child(input).child("members").child(user).child("is_admin").setValue(false)
        (activity as MainActivity).navigateToWithargs(HomeSettings(), false, input)
    }

}