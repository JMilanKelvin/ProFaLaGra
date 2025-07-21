package com.example.profalagra

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.profalagra.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val REQ_CODE_ASK_PERM = 111
    private lateinit var binding: ActivityMainBinding

    private val navGraphIds = listOf(
        R.navigation.nav_list,
        R.navigation.nav_create,
        R.navigation.nav_settings
    )

    private val navHosts = mutableListOf<NavHostFragment>()
    private var selectedIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        solPerm()
        setupBottomNavigation()
    }


    private fun setupBottomNavigation() {
        navGraphIds.forEachIndexed { index, navGraphId ->
            val tag = "navHost_$index"
            val existingFragment = supportFragmentManager.findFragmentByTag(tag) as? NavHostFragment

            val navHostFragment = existingFragment ?: NavHostFragment.create(navGraphId).also {
                supportFragmentManager.beginTransaction()
                    .add(R.id.nav_host_container, it, tag)
                    .hide(it)
                    .commitNow()
            }

            navHosts.add(navHostFragment)
        }

        showFragment(0)

        binding.bottomNav.setOnItemSelectedListener { item ->
            val index = when (item.itemId) {
                R.id.home_button -> 0
                R.id.add_button -> 1
                R.id.settings_button -> 2
                else -> 0
            }

            if (index != selectedIndex) {
                showFragment(index)
            }
            true
        }
    }

    private fun showFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        navHosts.forEachIndexed { i, fragment ->
            if (i == index) transaction.show(fragment)
            else transaction.hide(fragment)
        }
        transaction.commit()
        selectedIndex = index
    }

    override fun onBackPressed() {
        val currentNavController = navHosts[selectedIndex].navController
        if (!currentNavController.popBackStack()) {
            super.onBackPressed()
        }
    }


    //Metodo para dar permiso de leer archivos
    private fun solPerm() {
        val permStorage = ActivityCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (permStorage != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQ_CODE_ASK_PERM
                )
            }
        }
    }

    //Metodo para ir al activity SelectGraf
    fun botoSelectGraf(view: View?) {
        val botSelectGraf = Intent(this, SelectGrafActivity::class.java)
        startActivity(botSelectGraf)
    }

    //Metodo para ir al activity AgreGraf
    fun botoAgreGraf(view: View?) {
        val botAgreGraf = Intent(this, AgreGrafActivity::class.java) //VisGrafActivity.class);
        startActivity(botAgreGraf)
    }

    fun cerrApp(view: View?) {
        val inteClos = Intent(Intent.ACTION_MAIN)
        inteClos.addCategory(Intent.CATEGORY_HOME)
        inteClos.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(inteClos)
    }
}