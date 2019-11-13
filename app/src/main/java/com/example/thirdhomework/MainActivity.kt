package com.example.thirdhomework

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration.*
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.example.thirdhomework.data.CPU
import com.example.thirdhomework.data.Coordinate
import com.example.thirdhomework.fragments.DataAdapter
import com.example.thirdhomework.fragments.InfoFragment
import com.example.thirdhomework.fragments.ListFragment
import com.example.thirdhomework.fragments.LocationFragment
import com.example.thirdhomework.listener.OnItemListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import java.io.IOException
import java.io.Serializable
import java.util.stream.Collectors.*

class MainActivity : AppCompatActivity(), OnItemListener {
    private lateinit var dataAdapter: DataAdapter
    private var processors = mutableListOf<CPU>()
    private var currentCPU: CPU? = null
    private lateinit var client: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        clearBackStack()
        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable(LIST_KEY) != null) {
                processors = savedInstanceState.getSerializable(LIST_KEY) as MutableList<CPU>
                initAdapter()
                initFragments()
            } else {
                loadData()
            }
            if (savedInstanceState.getSerializable(CPU_KEY) != null) {
                currentCPU = savedInstanceState.getSerializable(CPU_KEY) as CPU
                switchFragment(currentCPU!!)
            }
        } else {
            loadData()
        }
        initCpuObject()
    }

    private fun loadData() {
        if (checkConnection()) {
            runBlocking {
                getData()
            }
            initAdapter()
            initFragments()
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkConnection(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork != null
    }

    private suspend fun getData() = withContext(Dispatchers.IO) {
        val doc = Jsoup.connect(URL).get()
        val element = doc.select("div.br-static")
        try {
            processors = element.run {
                select("h3").select("a")
                    .parallelStream()
                    .map { url -> Jsoup.connect(url.attr("abs:href")).get() }
                    .map { info ->
                        CPU(
                            info.select("h1[class=title]").text(),
                            info.select("div.br-pr-about").text(),
                            info.select("img[id=product_main_image]").attr("src")
                        )
                    }
                    .collect(toList<CPU>())
            }
        } catch (error: IOException) {
            error.printStackTrace()
        }
    }

    private fun initFragments() {
        val listFragment = initListFragmentAdapter()
        when {
            ORIENTATION_PORTRAIT == resources.configuration.orientation -> {
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
                supportFragmentManager.beginTransaction()
                    .replace(R.id.info_container, InfoFragment.newInstance(cpu))
                    .commit()
            }
            ORIENTATION_PORTRAIT == resources.configuration.orientation -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        0,
                        0,
                        android.R.anim.slide_out_right
                    )
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
        if (!isItemSelected() && supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
        }
    }

    private fun initAdapter() {
        dataAdapter = DataAdapter(processors, this)
    }

    private fun initListFragmentAdapter() = ListFragment().apply {
        adapter = dataAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CPU_KEY, currentCPU)
        if (processors.isNotEmpty()) {
            outState.putSerializable(LIST_KEY, processors as Serializable)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.location_btn) {
            requestPermission()
            showCoordinates()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCoordinates() {
        var locationFragment: LocationFragment
        client = LocationServices.getFusedLocationProviderClient(this)
        if (isPermitted()) {
            client.lastLocation.addOnCompleteListener { task ->
                run {
                    if (task.isSuccessful && task.result != null) {
                        task.result.apply {
                            locationFragment =
                                LocationFragment.newInstance(Coordinate(longitude, latitude))
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.list_container, locationFragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
            }
        }
    }

    private fun isPermitted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    companion object {
        const val REQUEST_CODE = 100
        const val CPU_KEY = "position"
        const val LIST_KEY = "list"
        const val COORDINATE_KEY = "coordinate"
        const val URL =
            "https://brain.com.ua/ukr/category/Procesory-c1097-128/filter=19590-66000347500,19590-68000009500,19590-75000454900,528-86000152200,528-86029031100/"
    }
}