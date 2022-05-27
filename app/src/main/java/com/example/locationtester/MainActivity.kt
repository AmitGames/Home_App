package com.example.locationtester

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*




class MainActivity : AppCompatActivity(), NavigationHost{


//    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val firebaseDatabaseReference = FirebaseDatabase.getInstance("https://android-home-app-e721d-default-rtdb.europe-west1.firebasedatabase.app/")
    private var auth = Firebase.auth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (auth.currentUser == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, LoginFragment())
                .commit()
        }
        else{
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, MyHomesFragment())
                .commit()
        }
    }
//        auth = Firebase.auth
//        title = "My Homes"
//        val navigationView = findViewById<NavigationView>(R.id.menuBar)
//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        val headerView = navigationView.getHeaderView(0)
//        val name = headerView.findViewById<TextView>(R.id.display_name_text)
//        name.text = "Welcome: ${auth.currentUser?.displayName.toString()}"

//        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.AddHouseBtn).setOnClickListener {
//            val intent = Intent(this@MainActivity, NewHouseActivity::class.java)
//            startActivity(intent)
//        }
//        updateDatabaseInfo()

//
//    }
    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
    fun navigateToWithargs(fragment: Fragment, addToBackstack: Boolean, input : Any) {
        val bundle = Bundle()
        bundle.putString("data", input.toString())
        fragment.arguments = bundle
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
    fun getTextView() : TextView{
        return TextView(this)
    }
    fun getButtonView() : MaterialButton{
        return MaterialButton(this)
    }
    fun getLinearView() : LinearLayout{
        return LinearLayout(this)
    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
//            return true
//        }
//
//
//        return super.onOptionsItemSelected(item)
//    }

//
//    @SuppressLint("SetTextI18n")
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if(currentUser == null){
//            val intent = Intent(this@MainActivity, LogInSystem::class.java)
//            startActivity(intent)
//        } else{
//            var name = ""
//            val user = Firebase.auth.currentUser
//            user?.let{
//                for(profile in it.providerData){
//                    name = profile.displayName.toString()
//
//                }
//            }
//
//        }
//    }
@SuppressLint("MissingPermission")
fun GetLocation (){


    if(ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 101)
        return
    }
    val task = fusedLocationProviderClient.lastLocation
    task.addOnSuccessListener {
        if(it != null){
            //Toast.makeText(baseContext,"${it.latitude}, ${it.longitude}",Toast.LENGTH_SHORT).show()
            UpdateDatabaseLocation(it)

        }
    }

}
    @SuppressLint("MissingPermission")
    fun getLocationValue(o: Map<*, *>, myHomesFragment: MyHomesFragment, home : String){
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                //Toast.makeText(baseContext,"${it.latitude}, ${it.longitude}",Toast.LENGTH_SHORT).show()
                myHomesFragment.updateUI(o, LatLng(it.latitude.toDouble(), it.longitude.toDouble()), home)

            }
        }
    }
    private fun UpdateDatabaseLocation(it : Location){
        val reference = firebaseDatabaseReference.reference
        val UID = auth.currentUser?.uid.toString()
        reference.child("Users").child(UID).child("CurrentLocation").setValue(mapOf("latitude" to it.latitude,
            "longitude" to it.longitude))
    }

//

////    override fun onNavigationItemSelected(item: MenuItem): Boolean {
////        when(item.itemId){
////            R.id.log_out->logout()
////        }
////        return true
////    }


}