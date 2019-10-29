package com.example.thirdhomework.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thirdhomework.MainActivity.Companion.NOT_SELECTED_ITEM
import com.example.thirdhomework.R
import com.example.thirdhomework.data.DataHelper
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

    companion object {
        fun newInstance(position: Int) = InfoFragment().apply {
            if (position > NOT_SELECTED_ITEM)
            arguments = Bundle().apply {
                putString("title", DataHelper().getProcessors()[position].name)
                putString("description", DataHelper().getProcessors()[position].description)
            }
        }
    }
}
