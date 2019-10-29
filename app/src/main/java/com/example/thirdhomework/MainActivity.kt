package com.example.thirdhomework

import android.content.res.Configuration.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.example.thirdhomework.data.DataHelper
import com.example.thirdhomework.fragments.DataAdapter
import com.example.thirdhomework.fragments.InfoFragment
import com.example.thirdhomework.fragments.ListFragment
import com.example.thirdhomework.listener.OnItemListener

class MainActivity : AppCompatActivity(), OnItemListener {
    private var processors = DataHelper().getProcessors()
    private val dataAdapter = DataAdapter(processors, this)
    private var currentPosition = NOT_SELECTED_ITEM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragments()
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(POSITION_KEY)
            clearBackStack()
            changeFragment(InfoFragment.newInstance(currentPosition))
        }
    }

    override fun onClickItem(position: Int) {
        val fragment = InfoFragment.newInstance(position)
        currentPosition = position
        changeFragment(fragment)
    }

    private fun changeFragment(fragment: InfoFragment) {
        when (ORIENTATION_PORTRAIT) {
            resources.configuration.orientation -> {
                if (isItemSelected()) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.list_container, fragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .show(supportFragmentManager.findFragmentById(R.id.list_container)!!)
                        .commit()
                }
            }
            else -> {
                if (isItemSelected()) {
                    clearBackStack()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.info_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    private fun isItemSelected() = currentPosition > NOT_SELECTED_ITEM

    private fun initFragments() {
        val listFragment = initListFragment()
        when (ORIENTATION_PORTRAIT) {
            this.resources.configuration.orientation -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.list_container, listFragment)
                    .commit()
            }
            else -> supportFragmentManager.beginTransaction()
                .add(R.id.info_container, InfoFragment())
                .add(R.id.list_container, listFragment)
                .commit()
        }
    }

    private fun clearBackStack() =
        supportFragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)

    private fun initListFragment() = ListFragment().apply {
        adapter = dataAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (supportFragmentManager.backStackEntryCount != 0) {
            outState.putInt(POSITION_KEY, currentPosition)
        } else {
            outState.putInt(POSITION_KEY, NOT_SELECTED_ITEM)
        }
    }

    companion object {
        const val NOT_SELECTED_ITEM = -1
        const val POSITION_KEY = "position"
    }
}
