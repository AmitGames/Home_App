package com.example.locationtester


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.locationtester.databinding.ActivityMainBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import network.ProductEntry


class MyHomesFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private val auth = Firebase.auth
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var flag = true
    private val uid = auth.currentUser?.uid


    private val firebaseDatabaseReference = FirebaseDatabase.getInstance("https://android-home-app-e721d-default-rtdb.europe-west1.firebasedatabase.app/")
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)


    setHasOptionsMenu(true)
}
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityMainBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as AppCompatActivity).setSupportActionBar(binding.appBar)

//        binding.recyclerView.setHasFixedSize(true)
//        binding.recyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        val adapter = ProductCardRecyclerViewAdapter(
            ProductEntry.initProductEntryList(resources))
//        binding.recyclerView.adapter = adapter
        binding.appBar.title = "${auth.currentUser?.displayName} Homes"
        val largePadding = resources.getDimensionPixelSize(R.dimen.shr_product_grid_spacing)
        val smallPadding = resources.getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small)
//        binding.recyclerView.addItemDecoration(ProductGridItemDecoration(largePadding, smallPadding))
        drawerLayout = binding.drawarLayout
        navigationView = binding.navView
        toolbar = binding.appBar
        navigationView.bringToFront()
        val headerView = navigationView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.display_name_text).text = "Welcome: ${auth.currentUser?.displayName}"
//        val data = DatabaseConn()
//        data.updateJson()
        getListOfHomes()

        val actionBarDrawerToggle = ActionBarDrawerToggle(Activity(), drawerLayout, toolbar,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)
        updateDatabaseInfo()
        thread = Thread(runnable)
        thread.start()




        return view
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mainactivitymenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.log_out-> {
                logout()
                flag = false
            }
            R.id.create_new_btn->{(activity as MainActivity).navigateTo(NewHouseFragment(), true)
            flag = false
            }
            R.id.account_btn->{(activity as MainActivity).navigateTo(MyAcount(), true)
            flag = false
            }
        }

        return true
    }
    private fun logout(){
        Firebase.auth.signOut()
        (activity as MainActivity).navigateTo(LoginFragment(), false)
    }
    private fun updateDatabaseInfo() {
        val reference = firebaseDatabaseReference.reference
        val user = auth.currentUser
        val UID = user?.uid.toString()
        reference.child("Users").child(UID).child("display_name")
            .setValue(auth.currentUser?.displayName.toString())
        reference.child("Users").child(UID).child("homes").setValue(null)
    }

    private lateinit var thread : Thread
    private val runnable = Runnable{
        while(flag){
            (activity as MainActivity).GetLocation()
            Thread.sleep(10000)

        }

    }

    fun readhome(home : String){

        val reference = firebaseDatabaseReference.reference.child("Homes")
        var o : Map<*, *> = mapOf("" to "")
        reference.child(home).get().addOnSuccessListener {
            if(it.value is Map<*,*>) {
                o = it.value as Map<*, *>
                val gson: Gson = Gson()
                val json_string = gson.toJson(o)
                Log.w(TAG, "Json is: ${o["home_name"]}")
                (activity as MainActivity).getLocationValue(o, this, home)
            }


        }
    }


    @SuppressLint("SetTextI18n")
    fun updateUI(o : Map<*,*>, Location: LatLng, home : String){
        var text = (activity as MainActivity).getTextView()
        text.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        val homeplace = o["home_location"] as Map<*,*>
        var lat1 : Double = homeplace["latitude"] as Double
        var lon1 : Double = homeplace["longitude"] as Double
        var lat2 : Double = lat1.toDouble()
        var lon2 : Double = lon1.toDouble()
        lat2 /= (180 / Math.PI)
        lon2 /= (180 / Math.PI)
        var lat3 = Location.latitude / (180/Math.PI)
        var lon3 = Location.longitude / (180/Math.PI)

        val distance = 3963.0 * Math.acos((Math.sin(lat2) * Math.sin(lat3)) + Math.cos(lat2)*Math.cos(lat3)*Math.cos(lon3 - lon2)) * 1.609344


        text.setText("\n Location Name: ${o["home_name"].toString()}\nDistance From Location: ${distance.toInt()} KM")
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        text.setOnClickListener{
            (activity as MainActivity).navigateToWithargs(HomeSettings(), true, home)
            flag = false
        }

        binding.reletive.addView(text)

        onStart()

    }

    fun getListOfHomes(){
        var list : List<Any?> = listOf("")
        var post : Map<*,*> = mapOf("" to "")
        val reference = firebaseDatabaseReference.reference.child("Users").child(uid.toString())
        reference.child("homes_member").get().addOnSuccessListener {
            if(it.value is Map<*,*>) {

                for(home in (it.value as Map<*,*>).keys){
                    Log.w(TAG, "Json is: ${home}")
                    readhome(home.toString())
                }
                post = it.value as Map<*, *>
                list = post.keys.toList()
            }
        }

    }


    }