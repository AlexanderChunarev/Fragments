package com.example.thirdhomework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_info, container, false)
        if (arguments != null) {
            rootView.apply {
                cpu_title?.text = arguments?.getString("title")
                cpu_description?.text = arguments?.getString("description")
            }
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            cpu_title.text = savedInstanceState.getString("title")
            cpu_description.text = savedInstanceState.getString("description")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (arguments != null) {
            outState.putString("title", arguments?.getString("title"))
            outState.putString("description", arguments?.getString("description"))
        }
    }
}
