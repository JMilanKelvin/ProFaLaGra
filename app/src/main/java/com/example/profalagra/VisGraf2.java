package com.example.profalagra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class VisGraf2 extends AppCompatActivity {
    private ImageView IVGrafx, IVPuntX;
    private TextView TVRefChordsX, TVPrueb;
    float [] chordPunts = new float[2]; //Cordenadas del puntero respecto el IVGraf
    float tXPunt=0.0f, tYPunt=0.0f;
    float FS=1.0f;
    float x0=109.0f, y0=896.0f, x1=109.0f, y1=50.0f, x2=894.0f, y2=892.0f; //Coordenadas de X, Y y O respecto el IVGraf
    float eY2=1200.0f, eY1=0.0f, eX2=2.0f, eX1=0.0f; //Valor iniciales y finales de las escalas en Y e X
    float DX=0.0f, DY=0.0f; //Los valores que leemos en cuestion
    boolean logY=false, logX=false;
    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_graf2);
        //Se reciben los datos del anterior activity
        Bundle resDat = getIntent().getExtras();
        String[] datosVector = resDat.getStringArray("Datos");

        //Se muestra la imagen de la grafica respectiva en el IV respectivo
        IVGrafx=findViewById(R.id.imageView2);
        Uri uriGrafx = Uri.parse(datosVector[0]);
        boolean absolute = uriGrafx.isAbsolute();
        IVGrafx.setImageURI(uriGrafx);
        //Se recuperan los datos guardados para usarlos
        String[] canival= datosVector[3].split(":");
        x0=Float.parseFloat(canival[0]);
        y0=Float.parseFloat(canival[1]);
        String[] canival1= datosVector[1].split(":");
        x2=Float.parseFloat(canival1[0]);
        y2=Float.parseFloat(canival1[1]);
        String[] canival2= datosVector[2].split(":");
        x1=Float.parseFloat(canival2[0]);
        y1=Float.parseFloat(canival2[1]);
        String[] canival3= datosVector[6].split("y");
        logX=Boolean.parseBoolean(canival3[0]);
        logY=Boolean.parseBoolean(canival3[1]);
        String[] canival4= datosVector[4].split("a");
        eX1=Float.parseFloat(canival4[0]);
        eX2=Float.parseFloat(canival4[1]);
        String[] canival5= datosVector[5].split("a");
        eY1=Float.parseFloat(canival5[0]);
        eY2=Float.parseFloat(canival5[1]);


        IVPuntX=findViewById(R.id.ivPuntX2);
        TVRefChordsX=findViewById(R.id.tvRefChordsX2);
        TVPrueb=findViewById(R.id.textView10);
        TVPrueb.setText("");//x0 +"|"+y0+"\n"+x1 +"|"+y1+"\n"+x2 +"|"+y2+"\n"+eX1 +" a "+eX2+"\n"+eY1 +" a "+eY2+"\n"+logX+" y "+ logY);

        SeekBar SBZoom = findViewById(R.id.sbZoom2);

        //Verificacion de escala log
        if (logY) {
            eY2= (float) (Math.log(eY2)/Math.log(10));
            eY1= (float) (Math.log(eY1)/Math.log(10));
        }
        if (logX) {
            eX2= (float) (Math.log(eX2)/Math.log(10));
            eX1= (float) (Math.log(eX1)/Math.log(10));
        }

        //Obtencion de la mitad del ancho en pixeles reales del puntero
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
                chordPunts[0]=(IVPuntX.getX()-IVGrafx.getX()+hHalfIVPunt)/FS;
                chordPunts[1]=(IVPuntX.getY()-IVGrafx.getY()+hHalfIVPunt)/FS;

                DY = ((eY2 - eY1) * ((chordPunts[0] - x0) * (y2 - y0) - (chordPunts[1] - y0) * (x2 - x0)) / (((y2 - y0) * (x1 - x0)) - ((x2 - x0) * (y1 - y0)))) + eY1;
                if (logY) {
                    DY= (float) Math.pow(10.0, DY);
                }
                DX=(eX2-eX1)*((chordPunts[0]-x0)*(y1-y0)-(chordPunts[1]-y0)*(x1-x0))/((x2-x0)*(y1-y0)-(y2-y0)*(x1-x0))+eX1;
                if (logX) {
                    DX= (float) Math.pow(10.0, DX);
                }

                TVRefChordsX.setText("x: "+DX+"\ny: "+DY);
                return true;
            }
        });
        IVGrafx.setOnTouchListener(new View.OnTouchListener() {
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
                        IVGrafx.setX(IVGrafx.getX()+xM-tXPunt);
                        IVGrafx.setY(IVGrafx.getY()+yM-tYPunt);
                        IVPuntX.setX(IVPuntX.getX()+xM-tXPunt);
                        IVPuntX.setY(IVPuntX.getY()+yM-tYPunt);
                        break;
                }
                return true;
            }
        });
        ViewGroup.LayoutParams parmIVGraf = IVGrafx.getLayoutParams();
        int hInIVGraf = parmIVGraf.height, wInIVGraf = parmIVGraf.width;
        SBZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float DXZIVPuntX=0.0f, DYZIVPuntX=0.0f;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                FS= 1.0f + i * 0.1f;
                parmIVGraf.height = (int) (FS * hInIVGraf);
                parmIVGraf.width = (int) (FS * wInIVGraf);
                IVGrafx.setLayoutParams(parmIVGraf);
                IVPuntX.setX(IVGrafx.getX()+FS*DXZIVPuntX-hHalfIVPunt);
                IVPuntX.setY(IVGrafx.getY()+FS*DYZIVPuntX-hHalfIVPunt);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                DXZIVPuntX=(IVPuntX.getX()-IVGrafx.getX()+hHalfIVPunt)/FS;
                DYZIVPuntX=(IVPuntX.getY()-IVGrafx.getY()+hHalfIVPunt)/FS;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}