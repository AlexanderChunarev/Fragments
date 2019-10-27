package com.example.thirdhomework

import android.content.res.Configuration.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), OnItemListener {

    private var processors: MutableList<CentralProcessUnit> = ArrayList()
    private val dataAdapter = DataAdapter(processors, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setData()
        val listFragment = initListFragment()
        when (ORIENTATION_PORTRAIT) {
            this.resources.configuration.orientation -> supportFragmentManager.beginTransaction()
                .add(R.id.container, listFragment)
                .commit()
            else -> supportFragmentManager.beginTransaction()
                .add(R.id.info_fragment, InfoFragment())
                .add(R.id.list_fragment, listFragment)
                .commit()
        }
    }

    private fun initListFragment() = ListFragment().apply {
        adapter = dataAdapter
    }

    private fun setData() {
        processors.add(CentralProcessUnit("i9-9900K", "asdasd", R.drawable.i9))
        processors.add(CentralProcessUnit("i10-9900K", "asda2q111sd", R.drawable.i9))
    }

    override fun onClickItem(position: Int) {
        val fragment = InfoFragment().apply {
            arguments = Bundle().apply {
                putString("title", processors[position].name)
                putString("description", processors[position].description)
            }
        }

        when (ORIENTATION_PORTRAIT) {
            this.resources.configuration.orientation -> supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
            else -> supportFragmentManager.beginTransaction()
                .replace(R.id.info_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
