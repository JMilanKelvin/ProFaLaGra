package com.example.profalagra

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private val REQ_CODE_ASK_PERM = 111
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        solPerm()
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