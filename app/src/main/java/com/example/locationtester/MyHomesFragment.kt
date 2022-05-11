package com.example.locationtester


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtester.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import network.ProductEntry


class MyHomesFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private val auth = Firebase.auth
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var relativeLayout: View
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

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        val adapter = ProductCardRecyclerViewAdapter(
            ProductEntry.initProductEntryList(resources))
        binding.recyclerView.adapter = adapter
        binding.appBar.title = "${auth.currentUser?.displayName} Homes"
        val largePadding = resources.getDimensionPixelSize(R.dimen.shr_product_grid_spacing)
        val smallPadding = resources.getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small)
        binding.recyclerView.addItemDecoration(ProductGridItemDecoration(largePadding, smallPadding))
        drawerLayout = binding.drawarLayout
        navigationView = binding.navView
        toolbar = binding.appBar
        navigationView.bringToFront()


        val actionBarDrawerToggle = ActionBarDrawerToggle(Activity(), drawerLayout, toolbar,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)




        return view
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mainactivitymenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.isEnabled){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.log_out->logout()
        }

        return true
    }
    private fun logout(){
        Firebase.auth.signOut()
        (activity as MainActivity).navigateTo(LoginFragment(), false)
    }

}