package com.example.thirdhomework.data

import android.os.AsyncTask
import androidx.fragment.app.Fragment
import com.example.thirdhomework.fragments.DataAdapter
import com.example.thirdhomework.fragments.ListFragment
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class DataHelper {
    private var processors = mutableListOf<CPU>()

    init {
//        processors.run {
//            add(CPU("i9-9900k", "description", R.drawable.i9))
//            add(CPU("i3-8100", "description", R.drawable.intel_core_i3_8100))
//            add(CPU("i3-8400", "description", R.drawable.intel_core_i5_8400))
//            add(CPU("i5_9400f", "description", R.drawable.intel_core_i5_9400f))
//            add(CPU("i7_8700k", "description", R.drawable.intel_core_i7_9700k))
//            add(CPU("i9-9900k", "description", R.drawable.i9))
//            add(CPU("i3-8100", "description", R.drawable.intel_core_i3_8100))
//            add(CPU("i3-8400", "description", R.drawable.intel_core_i5_8400))
//            add(CPU("i5_9400f", "description", R.drawable.intel_core_i5_9400f))
//            add(CPU("i7_8700k", "description", R.drawable.intel_core_i7_9700k))
//            add(CPU("i9-9900k", "description", R.drawable.i9))
//            add(CPU("i3-8100", "description", R.drawable.intel_core_i3_8100))
//            add(CPU("i3-8400", "description", R.drawable.intel_core_i5_8400))
//            add(CPU("i5_9400f", "description", R.drawable.intel_core_i5_9400f))
//            add(CPU("i7_8700k", "description", R.drawable.intel_core_i7_9700k))
//        }
    }
    fun getProcessors() = processors

    companion object {

    }
}