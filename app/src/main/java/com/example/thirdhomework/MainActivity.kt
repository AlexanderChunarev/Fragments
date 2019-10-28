package com.example.thirdhomework

import android.content.res.Configuration.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*


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

        if (savedInstanceState != null) {
            val fragment = supportFragmentManager.getFragment(savedInstanceState, "info_fragment")
            if (fragment != null) {
                when (ORIENTATION_PORTRAIT) {
                    this.resources.configuration.orientation ->  supportFragmentManager.beginTransaction()
                        .remove(fragment)
                        .replace(R.id.container, recreateFragment(fragment))
                        .commit()
                    else -> supportFragmentManager.beginTransaction()
                        .remove(fragment)
                        .replace(R.id.info_fragment, recreateFragment(fragment))
                        .commit()
                }
            }

        }


//        if (supportFragmentManager.backStackEntryCount > 0) {
//            val fragment = supportFragmentManager.findFragmentById(R.id.info_fragment)
//            val ft = supportFragmentManager.beginTransaction()
//            if (fragment != null) {
//                ft.remove(fragment)
//                val newInstance = recreateFragment(fragment)
//                ft.replace(R.id.container, newInstance)
//                ft.commit()
//            }
//
////
////            if (fr != null) {
////                supportFragmentManager.beginTransaction()
////                    .replace(R.id.container, fr)
////                    .addToBackStack(null)
////                    .commit()
////            }
//        }
    }

    private fun recreateFragment(f: Fragment): Fragment {
        try {
            val savedState = supportFragmentManager.saveFragmentInstanceState(f)

            val newInstance = f::class.java.newInstance()
            newInstance.setInitialSavedState(savedState)

            return newInstance
        } catch (e: Exception) // InstantiationException, IllegalAccessException
        {
            throw RuntimeException("Cannot reinstantiate fragment " + f::class.java.getName(), e)
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
            resources.configuration.orientation -> supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
            else -> supportFragmentManager.beginTransaction()
                .replace(R.id.info_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val fragment = supportFragmentManager.findFragmentById(R.id.info_fragment)
        if (fragment != null) {
            supportFragmentManager.putFragment(outState, "info_fragment", fragment)
        }
    }
}
