package com.example.profalagra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class VisGrafActivity extends AppCompatActivity {
    private String [] datosVector;
    private ImageView IVGraf, IVPuntX, IVPuntY, IVPuntO;
    private TextView TVRefChordsX, TVRefChordsY, TVRefChordsO;
    private SeekBar SBZoom;
    float [][] chordPunts = new float[3][2]; //Cordenadas de X, Y y Origen respecto el IVGraf
    float tXPunt=0.0f, tYPunt=0.0f;
    float FS=1.0f;
    Uri uriGraf;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_graf);
        //Se reciben los datos del anterior activity
        Bundle resDat = getIntent().getExtras();
        datosVector = resDat.getStringArray("Datos");

        //Se muestra la imagen de la grafica respectiva en el IV respectivo
        IVGraf=findViewById(R.id.ivGraf);
        uriGraf = Uri.parse(datosVector[0]);
        IVGraf.setImageURI(uriGraf);

        IVPuntX=findViewById(R.id.ivPuntX);
        IVPuntY=findViewById(R.id.ivPuntY);
        IVPuntO=findViewById(R.id.ivPuntO);
        TVRefChordsX=findViewById(R.id.tvRefChordsX);
        TVRefChordsY=findViewById(R.id.tvRefChordsY);
        TVRefChordsO=findViewById(R.id.tvRefChordsO);
        SBZoom=findViewById(R.id.sbZoom);

        ViewGroup.LayoutParams parmIVGraf = IVGraf.getLayoutParams();
        int hInIVGraf = parmIVGraf.height, wInIVGraf = parmIVGraf.width;
        DisplayMetrics mt = getResources().getDisplayMetrics();
        float hHalfIVPunt = 49*mt.xdpi/160;
        IVPuntX.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        tXPunt= motionEvent.getX();
                        tYPunt= motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xM, yM;
                        xM = motionEvent.getX();
                        yM = motionEvent.getY();
                        IVPuntX.setX(IVPuntX.getX()+xM-tXPunt);
                        IVPuntX.setY(IVPuntX.getY()+yM-tYPunt);
                        break;
                }
                chordPunts[0][0]=(IVPuntX.getX()-IVGraf.getX()+hHalfIVPunt)/FS;
                chordPunts[0][1]=(IVPuntX.getY()-IVGraf.getY()+hHalfIVPunt)/FS;
                TVRefChordsX.setText("Eje X:" + chordPunts[0][0]+"//"+chordPunts[0][1]);
                return true;
            }
        });
        IVPuntY.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        tXPunt= motionEvent.getX();
                        tYPunt= motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xM, yM;
                        xM = motionEvent.getX();
                        yM = motionEvent.getY();
                        IVPuntY.setX(IVPuntY.getX()+xM-tXPunt);
                        IVPuntY.setY(IVPuntY.getY()+yM-tYPunt);
                        break;
                }
                chordPunts[1][0]=(IVPuntY.getX()-IVGraf.getX()+hHalfIVPunt)/FS;
                chordPunts[1][1]=(IVPuntY.getY()-IVGraf.getY()+hHalfIVPunt)/FS;
                TVRefChordsY.setText("Eje Y:" + chordPunts[1][0]+"//"+chordPunts[1][1]);
                return true;
            }
        });
        IVPuntO.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        tXPunt= motionEvent.getX();
                        tYPunt= motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xM, yM;
                        xM = motionEvent.getX();
                        yM = motionEvent.getY();
                        IVPuntO.setX(IVPuntO.getX()+xM-tXPunt);
                        IVPuntO.setY(IVPuntO.getY()+yM-tYPunt);
                        break;
                }
                chordPunts[2][0]=(IVPuntO.getX()-IVGraf.getX()+hHalfIVPunt)/FS;
                chordPunts[2][1]=(IVPuntO.getY()-IVGraf.getY()+hHalfIVPunt)/FS;
                TVRefChordsO.setText("Origen:" + chordPunts[2][0]+"//"+chordPunts[2][1]);
                return true;
            }
        });
        IVGraf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        tXPunt = motionEvent.getX();
                        tYPunt = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xM, yM;
                        xM = motionEvent.getX();
                        yM = motionEvent.getY();
                        IVGraf.setX(IVGraf.getX()+xM-tXPunt);
                        IVGraf.setY(IVGraf.getY()+yM-tYPunt);
                        IVPuntX.setX(IVPuntX.getX()+xM-tXPunt);
                        IVPuntX.setY(IVPuntX.getY()+yM-tYPunt);
                        IVPuntY.setX(IVPuntY.getX()+xM-tXPunt);
                        IVPuntY.setY(IVPuntY.getY()+yM-tYPunt);
                        IVPuntO.setX(IVPuntO.getX()+xM-tXPunt);
                        IVPuntO.setY(IVPuntO.getY()+yM-tYPunt);
                        break;
                }
                return true;
            }
        });

        SBZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float DXZIVPuntX=0.0f, DYZIVPuntX=0.0f;
            float DXZIVPuntY=0.0f, DYZIVPuntY=0.0f;
            float DXZIVPuntO=0.0f, DYZIVPuntO=0.0f;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                FS= 1.0f + i * 0.1f;
                parmIVGraf.height = (int) (FS * hInIVGraf);
                parmIVGraf.width = (int) (FS * wInIVGraf);
                IVGraf.setLayoutParams(parmIVGraf);
                IVPuntX.setX(IVGraf.getX()+FS*DXZIVPuntX-hHalfIVPunt);
                IVPuntX.setY(IVGraf.getY()+FS*DYZIVPuntX-hHalfIVPunt);
                IVPuntY.setX(IVGraf.getX()+FS*DXZIVPuntY-hHalfIVPunt);
                IVPuntY.setY(IVGraf.getY()+FS*DYZIVPuntY-hHalfIVPunt);
                IVPuntO.setX(IVGraf.getX()+FS*DXZIVPuntO-hHalfIVPunt);
                IVPuntO.setY(IVGraf.getY()+FS*DYZIVPuntO-hHalfIVPunt);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                DXZIVPuntX=(IVPuntX.getX()-IVGraf.getX()+hHalfIVPunt)/FS;
                DYZIVPuntX=(IVPuntX.getY()-IVGraf.getY()+hHalfIVPunt)/FS;
                DXZIVPuntY=(IVPuntY.getX()-IVGraf.getX()+hHalfIVPunt)/FS;
                DYZIVPuntY=(IVPuntY.getY()-IVGraf.getY()+hHalfIVPunt)/FS;
                DXZIVPuntO=(IVPuntO.getX()-IVGraf.getX()+hHalfIVPunt)/FS;
                DYZIVPuntO=(IVPuntO.getY()-IVGraf.getY()+hHalfIVPunt)/FS;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    //Metodo para guardar y volver al mainActivity
    public void BTSavDat(View view){
        String datex = datosVector[0];
        for (int i=0;i<3;i++) {
            datosVector[1 + i] = chordPunts[i][0] + ":" + chordPunts[i][1];
        }
        for(int i=1;i<7;i++){
            datex=datex + ";" + datosVector[i];
        }
        try {
            OutputStreamWriter escritor = new OutputStreamWriter(openFileOutput("Datos.txt", Activity.MODE_APPEND));
            escritor.write(datex+"\n");
            escritor.flush();
            escritor.close();

            Toast.makeText(VisGrafActivity.this,"Se guardo correctamente", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(VisGrafActivity.this,"Error al guardar", Toast.LENGTH_SHORT).show();
        }
        Intent cancBack = new Intent(this, MainActivity.class);
        startActivity(cancBack);
    }
}