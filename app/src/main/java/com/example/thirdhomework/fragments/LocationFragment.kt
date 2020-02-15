package com.example.thirdhomework.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thirdhomework.MainActivity.Companion.COORDINATE_KEY

import com.example.thirdhomework.R
import com.example.thirdhomework.data.Coordinate
import kotlinx.android.synthetic.main.fragment_location.view.*

class LocationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_location, container, false)
        if (arguments != null) {
            rootView.apply {
                val coords = arguments!!.getSerializable(COORDINATE_KEY) as Coordinate
                latitude_text?.text = coords.latitude.toString()
                longitude_text?.text = coords.longitude.toString()
            }
        }
        return rootView
    }

    companion object {
        fun newInstance(coordinate: Coordinate) = LocationFragment().apply {
            arguments = Bundle().apply {
                putSerializable(COORDINATE_KEY, coordinate)
            }
        }
    }


}
