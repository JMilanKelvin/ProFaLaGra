package com.example.profalagra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    final private int REQ_CODE_ASK_PERM=111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        solPerm();
    }
    //Metodo para dar permiso de leer archivos
    private void solPerm() {
        int permStorage = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permStorage!= PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},REQ_CODE_ASK_PERM);
            }
        }
    }

    //Metodo para ir al activity SelectGraf
    public void botoSelectGraf(View view){
        Intent botSelectGraf = new Intent(this, SelectGrafActivity.class);
        startActivity(botSelectGraf);
    }
    //Metodo para ir al activity AgreGraf
    public void botoAgreGraf(View view){
        Intent botAgreGraf = new Intent(this, AgreGrafActivity.class);//VisGrafActivity.class);
        startActivity(botAgreGraf);
    }
    public void cerrApp(View view) {
        Intent inteClos = new Intent(Intent.ACTION_MAIN);
        inteClos.addCategory(Intent.CATEGORY_HOME);
        inteClos.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inteClos);
    }
}