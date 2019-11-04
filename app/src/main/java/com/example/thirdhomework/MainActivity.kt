package com.example.thirdhomework

import android.content.res.Configuration.*
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.example.thirdhomework.data.CPU
import com.example.thirdhomework.fragments.DataAdapter
import com.example.thirdhomework.fragments.InfoFragment
import com.example.thirdhomework.fragments.ListFragment
import com.example.thirdhomework.listener.OnItemListener
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MainActivity : AppCompatActivity(), OnItemListener {
    private var processors = mutableListOf<CPU>()
    private lateinit var dataAdapter: DataAdapter
    private var currentCPU: CPU? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable(CPU_KEY) != null) {
                currentCPU = savedInstanceState.getSerializable(CPU_KEY) as CPU
                switchFragment(currentCPU!!)
            }

            if (savedInstanceState.getSerializable("List") != null) {
                processors = savedInstanceState.getSerializable("List") as MutableList<CPU>
            }
        }

        if (processors.isEmpty()) {
            Content().execute()
        }

        dataAdapter = DataAdapter(processors, this)
        initFragments()
        initCpuObject()
    }

    private inner class Content : AsyncTask<Void, Void, Void>() {

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            dataAdapter.notifyDataSetChanged()
        }

        override fun doInBackground(vararg voids: Void): Void? {
            val doc =
                Jsoup.connect("https://brain.com.ua/ukr/category/Procesory-c1097-128/filter=3-21501270000,528-86029031100/")
                    .get()
            val element = doc.select("div.br-static")

                for (i in 0 until element.size) {
                    val url = element.select("h3").select("a").eq(i)
                    val cpuPage: Document
                    try {

                        cpuPage = Jsoup.connect(url.attr("abs:href")).get()
                        processors.add(
                            CPU(
                                cpuPage.select("span[id=price_currency]").text(),
                                cpuPage.select("div.br-pr-about").text(),
                                cpuPage.select("img[id=product_main_image]").attr("src")
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            return null
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
        outState.putSerializable("List", ArrayList(processors))
    }

    companion object {
        const val CPU_KEY = "position"
    }
}