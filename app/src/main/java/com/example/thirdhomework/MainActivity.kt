package com.example.thirdhomework

import android.content.res.Configuration.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.example.thirdhomework.data.CPU
import com.example.thirdhomework.data.DataHelper
import com.example.thirdhomework.fragments.DataAdapter
import com.example.thirdhomework.fragments.InfoFragment
import com.example.thirdhomework.fragments.ListFragment
import com.example.thirdhomework.listener.OnItemListener

class MainActivity : AppCompatActivity(), OnItemListener {
    private var processors = DataHelper().getProcessors()
    private val dataAdapter = DataAdapter(processors, this)
    private var currentCPU: CPU? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragments()

        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable(CPU_KEY) != null) {
                currentCPU = savedInstanceState.getSerializable(CPU_KEY) as CPU
                Toast.makeText(this, currentCPU.toString(), Toast.LENGTH_LONG).show()
                switchFragment(currentCPU!!)
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

    private fun initFragments() {
        val listFragment = initListFragment()
        when {
            ORIENTATION_LANDSCAPE == resources.configuration.orientation -> {
                clearBackStack()
                supportFragmentManager.beginTransaction()
                    .add(R.id.list_container, listFragment)
                    .commit()
            }
            ORIENTATION_PORTRAIT == resources.configuration.orientation -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.info_container, InfoFragment())
                    .add(R.id.list_container, listFragment)
                    .commit()
            }
        }
    }

    private fun clearBackStack() {
        supportFragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
    }

    private fun initListFragment() = ListFragment().apply {
        adapter = dataAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (currentCPU != null) {
            outState.putSerializable(CPU_KEY, currentCPU)
        }
    }

    companion object {
        const val NOT_SELECTED_ITEM = -1
        const val CPU_KEY = "position"
    }
}
