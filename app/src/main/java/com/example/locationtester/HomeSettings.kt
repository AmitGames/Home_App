package com.example.locationtester

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.locationtester.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson






class HomeSettings : Fragment() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val firebaseDatabaseReference = FirebaseDatabase.getInstance("https://android-home-app-e721d-default-rtdb.europe-west1.firebasedatabase.app/")
    private lateinit var input : String
    private var auth = Firebase.auth
    private val currentuser = auth.currentUser?.uid
    private lateinit var Location : LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
/**
 * SECTION
 * Running on the start of the fragment
 *
 * */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_settings, container, false)
        val args = this.arguments
        val inputData = args?.get("data")
        input = inputData.toString()
        val reference = firebaseDatabaseReference.reference.child("Users")
        reference.child(currentuser.toString()).child("homes_member").child(input).child("is_admin").get().addOnSuccessListener{
            if(it.value is Boolean){
                updatefromdatabase(inputData.toString(), it.value as Boolean)
                if(!(it.value as Boolean)){
                    view.findViewById<ExtendedFloatingActionButton>(R.id.add_button).hide()
                }else{
                    view.findViewById<ExtendedFloatingActionButton>(R.id.add_button).setOnClickListener{
                        addMember(input)
                    }
                }
            }
        }

        return view

    }
    fun updatefromdatabase(home_name : String, isUserAdmin: Boolean){
        val reference = firebaseDatabaseReference.reference.child("Homes")
        var o : Map<*, *> = mapOf("" to "")
        reference.child(home_name).get().addOnSuccessListener {
            if(it.value is Map<*,*>) {
                o = it.value as Map<*, *>
                Log.w(TAG, "$o")
                updateUI(o, home_name, isUserAdmin)
                updateMap(o["home_location"] as Map<*, *>)

            }
    }
    }
    private fun updateMap(location : Map<*,*>){
        Location = LatLng(location["latitude"] as Double, location["longitude"] as Double)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @SuppressLint("SetTextI18n")
    fun updateUI(o: Map<*,*>, home_name: String, isUserAdmin: Boolean){
        val text = (activity as MainActivity).getTextView()

        text.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        text.setText("${o["home_name"].toString()}: Settings")
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
        val linear = view?.findViewById<LinearLayout>(R.id.linear1)
        linear?.addView(text)
        val text2 = (activity as MainActivity).getTextView()
        text2.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        text2.setText("Home UID: ${home_name}")
        text2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        text.textAlignment = ViewGroup.TEXT_ALIGNMENT_CENTER
        text2.textAlignment = ViewGroup.TEXT_ALIGNMENT_CENTER
        linear?.addView(text2)
        updatemembersList(home_name, isUserAdmin)
    }
    @SuppressLint("SetTextI18n")
    fun updatemembersList(home_name: String, isUserAdmin: Boolean){
        val reference = firebaseDatabaseReference.reference.child("Homes")
        var o : Map<*, *> = mapOf("" to "")
        reference.child(home_name).get().addOnSuccessListener {
            if(it.value is Map<*,*>) {
                o = it.value as Map<*, *>
                val i = o["members"] as Map<*,*>
                val text = (activity as MainActivity).getTextView()

                text.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                text.setText("Members: ")
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                val linear = view?.findViewById<LinearLayout>(R.id.linear1)
                linear?.addView(text)
                for(user in (i).keys){
                    Log.w(TAG, "Json is: ${user}")
                    readuser(user.toString(), i, o, isUserAdmin)
                }
            }
        }
    }
    fun readuser(user : String, h : Map<*,*>, home : Map<*,*>, isUserAdmin: Boolean){
        val reference = firebaseDatabaseReference.reference.child("Users")
        var o : Map<*, *> = mapOf("" to "")
        reference.child(user).get().addOnSuccessListener {
            if(it.value is Map<*,*>) {
                o = it.value as Map<*, *>
                Log.w(TAG, "$o")
                updateUIUser(o, user, h, home, isUserAdmin)

            }
        }
    }
    @SuppressLint("SetTextI18n")
    fun updateUIUser(o : Map<*,*>, user: String, h : Map<*,*>, home : Map<*,*>, isCurrentUserAdmin: Boolean) {
        val text = (activity as MainActivity).getTextView()

        val m = o["homes_member"] as Map<*, *>
        val n = m[input] as Map<*, *>
        val isUserAdmin = n["is_admin"] as Boolean

        text.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        if (isUserAdmin) {
            text.setText("   ${o["display_name"]} UID: $user Type: Admin")
        } else {
            text.setText("   ${o["display_name"]} UID: $user Type: Member")
        }


        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

        val linear = view?.findViewById<LinearLayout>(R.id.linear1)
        linear?.addView(text)

        readLocation(o, user, h, home, isCurrentUserAdmin)
    }
    private fun readLocation(o : Map<*,*>, user: String, h : Map<*,*>, home : Map<*,*>, isUserAdmin: Boolean){
        val user1 = o["CurrentLocation"] as Map<*,*>
        val userLocation = LatLng(user1["latitude"] as Double, user1["longitude"] as Double)
        val home1 = home["home_location"] as Map<*,*>
        val homeLocation = LatLng(home1["latitude"] as Double, home1["longitude"] as Double)
        updateLocationView(userLocation, homeLocation)
        updateUIButtons(user, h, isUserAdmin)


    }



    @SuppressLint("SetTextI18n")
    private fun updateLocationView(location1: LatLng, location2: LatLng){
        val text2 = (activity as MainActivity).getTextView()
        text2.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        text2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        val distance = calculateDistance(location1, location2)
        if(distance < 1){
            text2.setText("   Distance From Location: ${(distance * 100).toInt() * 10} M")
        }else {
            text2.setText("   Distance From Location: ${distance.toInt()} KM")
        }
        val linear = view?.findViewById<LinearLayout>(R.id.linear1)
        linear?.addView(text2)
    }



    @SuppressLint("SetTextI18n")
    private fun updateUIButtons(user: String, h: Map<*, *>, isUserAdmin : Boolean) {
        val u = h[user] as Map<*, *>
        val isCurrentUserAdmin = u["is_admin"] as Boolean
        Log.w(TAG, "$isCurrentUserAdmin")
        Log.w(TAG, "$isUserAdmin")
        val linear = view?.findViewById<LinearLayout>(R.id.linear1)
        val relativeLayout = (activity as MainActivity).getLinearView()
        relativeLayout.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        relativeLayout.setPadding(24, 16, 24, 24)
        relativeLayout.clipChildren = false
        relativeLayout.clipToPadding = false
        if (!isCurrentUserAdmin && isUserAdmin || user == auth.currentUser?.uid) {
            val button = (activity as MainActivity).getButtonView()
            button.layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            button.setText("Remove")
            button.setOnClickListener {
                removeMember(user)
            }
            relativeLayout.addView(button)


        }
        if (!isCurrentUserAdmin && isUserAdmin) {
            val button1 = (activity as MainActivity).getButtonView()
            button1.layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            button1.setText("Promote")
            button1.setOnClickListener {
                promoteMember(user)
            }
            relativeLayout.addView(button1)
        }
        if (isCurrentUserAdmin && isUserAdmin) {
            val button2 = (activity as MainActivity).getButtonView()
            button2.layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            button2.setText("Demote")
            button2.setOnClickListener {
                demoteMember(user)
            }
            relativeLayout.addView(button2)
        }
        linear?.addView(relativeLayout)


    }
    private val callback = OnMapReadyCallback { googleMap ->


        //val sydney = LatLng(-34.0, 151.0)
        val map = googleMap
        //googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.addMarker(MarkerOptions().position(Location).title("Home"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Location, 12f))


    }

/**
 * END OF SECTION
 * Running on the start of the fragment
 *
 * */
/**
 * SECTION
 * functions for buttons
 *
 * */
    private fun removeMember(user: String) {
        val reference = firebaseDatabaseReference.reference
        reference.child("Users").child(user).child("homes_member").child(input).setValue(null)
        reference.child("Homes").child(input).child("members").child(user).setValue(null)
        (activity as MainActivity).navigateToWithargs(HomeSettings(), false, input)
    }

    private fun demoteMember(user: String) {
        val reference = firebaseDatabaseReference.reference
        reference.child("Users").child(user).child("homes_member").child(input).child("is_admin").setValue(false)
        reference.child("Homes").child(input).child("members").child(user).child("is_admin").setValue(false)
        (activity as MainActivity).navigateToWithargs(HomeSettings(), false, input)
    }

    private fun promoteMember(user: String) {
        val reference = firebaseDatabaseReference.reference
        reference.child("Users").child(user).child("homes_member").child(input).child("is_admin").setValue(true)
        reference.child("Homes").child(input).child("members").child(user).child("is_admin").setValue(true)
        (activity as MainActivity).navigateToWithargs(HomeSettings(), false, input)
    }
    private fun addMember(home : String){
        (activity as MainActivity).navigateToWithargs(AddMemberFragment(), true, home)
    }
/**
 * END OF SECTION
 * functions for buttons
 *
 * */
// calculating function
    private fun calculateDistance(location1: LatLng, location2: LatLng): Double {
        val lat1 = location1.latitude / (180 / Math.PI)
        val lon1 = location1.longitude / (180 / Math.PI)
        val lat2 = location2.latitude / (180 / Math.PI)
        val lon2 = location2.longitude / (180 / Math.PI)

        return 3963.0 * Math.acos(
            (Math.sin(lat1) * Math.sin(lat2)) + Math.cos(lat1) * Math.cos(
                lat2
            ) * Math.cos(lon2 - lon1)
        ) * 1.609344
    }


}
