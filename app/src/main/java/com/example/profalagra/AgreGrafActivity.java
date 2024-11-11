package com.example.profalagra;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.nio.file.Paths;

public class AgreGrafActivity extends AppCompatActivity {
    TextView urlShow;
    EditText inXEdTx, fiXEdTx, inYEdTx, fiYEdTx;
    Spinner escXSp, escYSp;
    Button boton;
    String[] sp = {"Lineal", "Log"};
    String[] datosVector = new String[7]; //Aqui se guardara toda la informacion de la grafica
    boolean As=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agre_graf);
        urlShow = findViewById(R.id.textView4);
        inXEdTx = findViewById(R.id.editTextNumber);
        fiXEdTx = findViewById(R.id.editTextNumber2);
        inYEdTx = findViewById(R.id.editTextNumber3);
        fiYEdTx = findViewById(R.id.editTextNumber4);
        escXSp = findViewById(R.id.spinner);
        escYSp = findViewById(R.id.spinner2);
        boton = findViewById(R.id.button8);
        ArrayAdapter <String> spAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sp);
        escXSp.setAdapter(spAd);
        escYSp.setAdapter(spAd);
        cargarImagen(); //Para escoger el archivo

        //Metodo para guardar los datos e ir al activity VisGrafActivity
        boton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle pasDat = new Bundle();
                Intent botChordGraf = new Intent(AgreGrafActivity.this, VisGrafActivity.class);
                datosVector[4]= inXEdTx.getText() + "a" + fiXEdTx.getText();
                datosVector[5]= inYEdTx.getText() + "a" + fiYEdTx.getText();
                if(escXSp.getSelectedItem()=="Log"){
                    datosVector[6]="truey";
                }else{
                    datosVector[6]="falsey";
                }
                if(escYSp.getSelectedItem()=="Log"){
                    datosVector[6]=datosVector[6]+"true";
                }else{
                    datosVector[6]=datosVector[6]+"false";
                }
                pasDat.putStringArray("Datos",datosVector);
                botChordGraf.putExtras(pasDat);
                startActivity(botChordGraf);
            }
        });
    }
    //Metodos para escoger el archivo imagen
    public void cargarImagen() {
        Intent intObj = new Intent(Intent.ACTION_GET_CONTENT); //Nose que hace esto
        intObj.setType("image/");
        startActivityForResult(intObj, 10);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null){
                return;
            }
            Uri path = data.getData();
            /*As=path.isAbsolute();
            if(As) {
                urlShow.setText("Absolute");
            }else{
                urlShow.setText("No prr");
            }*/
            File imageFile = new File(getRealPathFromURI(path));
            System.out.println(imageFile.getAbsolutePath());
            System.out.println(imageFile.exists());
            datosVector[0]=imageFile.getAbsolutePath(); //Se guarda en el primer dato
            urlShow.setText("Archivo de imagen: " + datosVector[0]);
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    //Metodo para volver al activity Main
    public void cancelBack1(View view){
        Intent canceBack1 = new Intent(this, MainActivity.class);
        startActivity(canceBack1);
    }
}