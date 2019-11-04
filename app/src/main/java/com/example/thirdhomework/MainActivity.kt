package com.example.thirdhomework

import android.content.res.Configuration.*
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi

import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.example.thirdhomework.data.CPU
import com.example.thirdhomework.fragments.DataAdapter
import com.example.thirdhomework.fragments.InfoFragment
import com.example.thirdhomework.fragments.ListFragment
import com.example.thirdhomework.listener.OnItemListener
import org.jsoup.Jsoup
import java.io.Serializable

class MainActivity : AppCompatActivity(), OnItemListener {
    private lateinit var dataAdapter: DataAdapter
    private var processors = mutableListOf<CPU>()
    private var currentCPU: CPU? = null
    private var content = Content()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable(LIST_KEY) != null) {
                processors = savedInstanceState.getSerializable(LIST_KEY) as MutableList<CPU>
                dataAdapter = DataAdapter(processors, this)
                initFragments()
            }
            if (savedInstanceState.getSerializable(CPU_KEY) != null) {
                currentCPU = savedInstanceState.getSerializable(CPU_KEY) as CPU
                switchFragment(currentCPU!!)
            }
        } else {
            content.execute()
            processors = content.get()
            dataAdapter = DataAdapter(processors, this)
            initFragments()
        }
        initCpuObject()
    }

    private class Content : AsyncTask<Void, Void, MutableList<CPU>>() {
        private var array = mutableListOf<CPU>()

        @RequiresApi(Build.VERSION_CODES.N)
        override fun doInBackground(vararg voids: Void): MutableList<CPU>? {
            val doc = Jsoup.connect(URL).get()
            val element = doc.select("div.br-static")
            (0 until element.size)
                .map { element.select("h3").select("a").eq(it) }
                .parallelStream()
                .forEach {
                    try {
                        Jsoup.connect(it.attr("abs:href")).get().apply {
                            array.add(
                                CPU(
                                    select("h1[class=title]").text(),
                                    select("div.br-pr-about").text(),
                                    select("img[id=product_main_image]").attr("src")
                                )
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            return array
        }
    }

    private fun initFragments() {
        val listFragment = initListFragmentAdapter()
        when {
            ORIENTATION_PORTRAIT == resources.configuration.orientation -> {
                clearBackStack()
                supportFragmentManager.beginTransaction()
                    .add(R.id.list_container, listFragment)
                    .commit()
            }
            ORIENTATION_LANDSCAPE == resources.configuration.orientation -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.info_container, InfoFragment())
                    .add(R.id.list_container, listFragment)
                    .commit()
            }
        }
    }

    private fun initCpuObject() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (ORIENTATION_PORTRAIT == resources.configuration.orientation
                && supportFragmentManager.backStackEntryCount == 0
            ) {
                currentCPU = null
            }
        }
    }

    private fun switchFragment(cpu: CPU) {
        when {
            ORIENTATION_LANDSCAPE == resources.configuration.orientation && isItemSelected() -> {
                clearBackStack()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.info_container, InfoFragment.newInstance(cpu))
                    .commit()
            }
            ORIENTATION_PORTRAIT == resources.configuration.orientation -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.list_container, InfoFragment.newInstance(cpu))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onClickItem(position: Int) {
        currentCPU = processors[position]
        switchFragment(currentCPU!!)
    }

    private fun isItemSelected() = currentCPU != null

    private fun clearBackStack() {
        supportFragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
    }

    private fun initListFragmentAdapter() = ListFragment().apply {
        adapter = dataAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (currentCPU != null) {
            outState.putSerializable(CPU_KEY, currentCPU)
        }
        outState.putSerializable(LIST_KEY, processors as Serializable)
    }

    companion object {
        const val CPU_KEY = "position"
        const val LIST_KEY = "list"
        const val URL =
            "https://brain.com.ua/ukr/category/Procesory-c1097-128/filter=19590-66000347500,19590-68000009500,19590-75000454900,528-86000152200,528-86029031100/"
    }
}