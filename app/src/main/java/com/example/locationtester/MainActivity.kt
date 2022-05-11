package com.example.locationtester

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*




class MainActivity : AppCompatActivity(), NavigationHost{

//    private lateinit var thread : Thread
//    private val runnable = Runnable{
//        while(true){
//            GetLocation()
//            Thread.sleep(10000)
//
//        }
//
//    }
//    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
//    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val firebaseDatabaseReference = FirebaseDatabase.getInstance("https://android-home-app-e721d-default-rtdb.europe-west1.firebasedatabase.app/")
    private var auth = Firebase.auth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        if (auth?.currentUser == null) {
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
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.AddHouseBtn).setOnClickListener {
//            val intent = Intent(this@MainActivity, NewHouseActivity::class.java)
//            startActivity(intent)
//        }
//        updateDatabaseInfo()
//        thread = Thread(runnable)
//        thread.start()
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
//    private fun GetLocation (){
//        val task = fusedLocationProviderClient.lastLocation
//
//        if(ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
//            return
//        }
//        task.addOnSuccessListener {
//            if(it != null){
//                //Toast.makeText(baseContext,"${it.latitude}, ${it.longitude}",Toast.LENGTH_SHORT).show()
//                UpdateDatabaseLocation(it)
//
//            }
//        }
//    }
//
//    private fun UpdateDatabaseLocation(it : Location){
//        val reference = firebaseDatabaseReference.reference
//        val UID = auth.currentUser?.uid.toString()
//        reference.child("Users").child(UID).child("CurrentLocation").setValue(mapOf("latitude" to it.latitude,
//        "longitude" to it.longitude))
//    }
////    override fun onNavigationItemSelected(item: MenuItem): Boolean {
////        when(item.itemId){
////            R.id.log_out->logout()
////        }
////        return true
////    }
//    private fun updateDatabaseInfo(){
//        val reference = firebaseDatabaseReference.reference
//        val user = auth.currentUser
//        val UID = user?.uid.toString()
//        reference.child("Users").child(UID).child("display_name").setValue(auth.currentUser?.displayName.toString())
//        reference.child("Users").child(UID).child("homes").setValue(null)
//
//    }

}