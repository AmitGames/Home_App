package com.example.locationtester

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.locationtester.databinding.ActivityMainBinding

private var _binding: ActivityMainBinding? = null
private val binding get() = _binding!!




class HomeSettings : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_settings, container, false)
        val args = this.arguments
        val inputData = args?.get("data")

        view.findViewById<TextView>(R.id.test1).text = inputData.toString()
        return view
    }
}
