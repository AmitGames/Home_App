package com.example.locationtester

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var auth: FirebaseAuth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        val navigationView = findViewById<NavigationView>(R.id.menuBar)
        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val headerView = navigationView.getHeaderView(0)
        val name = headerView.findViewById<TextView>(R.id.display_name_text)
        name.text = "Welcome: ${auth.currentUser?.displayName.toString()}"
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.AddHouseBtn).setOnClickListener {
            val intent = Intent(this@MainActivity, NewHouseActivity::class.java)
            startActivity(intent)
        }
    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater : MenuInflater = menuInflater
//        inflater.inflate(R.menu.mainactivitymenu, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }


        return super.onOptionsItemSelected(item)
    }
    private fun logout(){
        Firebase.auth.signOut()
        onStart()
    }

    @SuppressLint("SetTextI18n")
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser == null){
            val intent = Intent(this@MainActivity, LogInSystem::class.java)
            startActivity(intent)
        } else{
            var name = ""
            val user = Firebase.auth.currentUser
            user?.let{
                for(profile in it.providerData){
                    name = profile.displayName.toString()

                }
            }

        }
    }
    private fun fetchLocation(){
        val database = FirebaseDatabase.getInstance("https://android-home-app-e721d-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference("/test")
        myRef.setValue("Whats Up")




    }
    private fun GetLocation(){
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if(it != null){
                Toast.makeText(applicationContext, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun writeToDB(user: String, password: String, path:String, deta:String){


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.log_out->logout()
        }
        return true
    }
}