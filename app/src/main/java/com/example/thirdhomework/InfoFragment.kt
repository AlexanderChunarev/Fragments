package com.example.thirdhomework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        if (arguments != null) {
            view.cpu_title.text = arguments?.getString("title")
            view.cpu_description.text = arguments?.getString("description")
        }
        return view
    }
}
