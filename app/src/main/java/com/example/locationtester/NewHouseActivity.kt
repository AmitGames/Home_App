package com.example.locationtester

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.locationtester.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class NewHouseFragment : Fragment() {

    private var auth = Firebase.auth
    private val firebaseDatabaseReference = FirebaseDatabase.getInstance("https://android-home-app-e721d-default-rtdb.europe-west1.firebasedatabase.app/")

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var bool = false
    private var currentLoc : LatLng = LatLng(0.0,0.0)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_new_house, container, false)
        _binding = ActivityMainBinding.inflate(inflater, container, false)
        view.findViewById<MaterialButton>(R.id.next_btn).setOnClickListener(){
            val name = view.findViewById<TextInputEditText>(R.id.home_name_edit).text
            updateDataBase(name, currentLoc)
            (activity as MainActivity).navigateTo(MyHomesFragment(), false)
        }
        
        return view
    }
    private fun updateDataBase(name : Editable?, location: LatLng = LatLng(0.0,0.0)){
        val reference = firebaseDatabaseReference.reference.child("Homes")
        val uid = auth.currentUser?.uid.toString()
        val key = reference.push().key.toString()
        reference.child(key).child("home_name").setValue(name.toString())
        reference.child(key).child("home_location").setValue(location)
        reference.child(key).child("members").child(uid).child("is_admin").setValue(true)

        val reference2 = firebaseDatabaseReference.reference.child("Users")
        reference2.child(uid).child("homes_member").child(key).child("is_admin").setValue(true)

    }
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        //val sydney = LatLng(-34.0, 151.0)
        val map = googleMap
        //googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(31.5, 34.0), 6f))

        setMapLongClick(map)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            if(!bool){
                map.addMarker(
                    MarkerOptions().position(latLng)
                )
                currentLoc = latLng

                bool = true
            }else{

            }



        }
    }
}