package com.example.profalagra;

import static android.text.TextUtils.split;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SelectGrafActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listGrafsx;
    private List<String> listGrafsInt =  new ArrayList<>();
    private ArrayAdapter<String> listGrafsInt2;
    private int numGrafSelect=0;
    String[] datosVector=new String[8];
    String[][] names = new String[30][8];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_graf);

        listGrafsx = findViewById(R.id.listGrafs);
        listGrafsx.setOnItemClickListener(this);

        //Lectura de archivo de datos
        String L="";
        int is=0;
        try {
            InputStreamReader isr = new InputStreamReader(openFileInput("Datos.txt"));
            BufferedReader leedor = new BufferedReader(isr);
            L= leedor.readLine();
            //Toast.makeText(this,L, Toast.LENGTH_SHORT).show();
            while (L!=null){
                String[] pieces = L.split(";"); //separa la cadena almacenada en L por |, los guarda en la matriz pieces
                for (int j=0; j<7; j++) {
                    names[is][j]=pieces[j];
                }
                is=is+1;
                L= leedor.readLine();
            }
            isr.close();
            leedor.close();
        }catch (IOException e){
        }
        //Toast.makeText(this,names[0][0], Toast.LENGTH_SHORT).show();
        //Escritura sobre listView
        for(int i=0;i<is;i++){
            listGrafsInt.add(names[i][0]);
        }
        listGrafsInt2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listGrafsInt);
        listGrafsx.setAdapter(listGrafsInt2);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int posList, long l) {
        Toast.makeText(this, "Grafica selecionada:" + posList, Toast.LENGTH_SHORT).show();
        numGrafSelect=posList;
    }

    //Metodo para ir al activity VisGraf2
    public void botoOpenGraf(View view){
        Bundle bdPasDat = new Bundle();
        Intent botOpenGraf = new Intent(this, VisGraf2.class);
        datosVector = names[numGrafSelect];
        bdPasDat.putStringArray("Datos", datosVector);
        botOpenGraf.putExtras(bdPasDat);
        startActivity(botOpenGraf);
    }
    //Metodo para ir al activity EditGraf
    public void botoEditGraf(View view){
        Bundle bdPasDat = new Bundle();
        Bundle bdNumGrafSelect = new Bundle();
        Intent botEditGraf = new Intent(this, EditGrafActivity.class);
        datosVector = names[numGrafSelect];
        bdPasDat.putStringArray("Datos", datosVector);
        bdNumGrafSelect.putInt("numGrafSelect", numGrafSelect);
        botEditGraf.putExtras(bdPasDat);
        botEditGraf.putExtras(bdNumGrafSelect);
        startActivity(botEditGraf);
    }
    //Metodo para volver al activity Main
    public void cancelBack(View view){
        Intent canceBack = new Intent(this, MainActivity.class);
        startActivity(canceBack);
    }

}