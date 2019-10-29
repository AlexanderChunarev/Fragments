package com.example.thirdhomework.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thirdhomework.MainActivity.Companion.CPU_KEY
import com.example.thirdhomework.R
import com.example.thirdhomework.data.CPU
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_info, container, false)
        if (arguments != null) {
            rootView.apply {
                val cpu = arguments!!.getSerializable(CPU_KEY) as CPU
                cpu_title?.text = cpu.name
                cpu_description?.text = cpu.description
            }
        }
        return rootView
    }

    companion object {
        fun newInstance(cpu: CPU) = InfoFragment().apply {
            arguments = Bundle().apply {
                putSerializable(CPU_KEY, cpu)
            }
        }
    }
}
