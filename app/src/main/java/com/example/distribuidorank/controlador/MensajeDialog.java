package com.example.distribuidorank.controlador;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.distribuidorank.R;

public class MensajeDialog {

    private AlertDialog.Builder builder;
    private DrawerLayout drawerLayout;
    private LayoutInflater inflater;
    private View view;
    
    
    public MensajeDialog(){
    }

    public MensajeDialog(Context contexto, Object any ){
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);;
        view = inflater.inflate(R.layout.dialog_opciones,null);
        builder = new AlertDialog.Builder(contexto);
    }

    /** AlertDialog con opciones a realizar sobre objeto a guardar*/
    private AlertDialog dialogOpciones(String objeto) {

        view = inflater.inflate(R.layout.dialog_opciones,null);
        final Button btnNuevo = view.findViewById(R.id.btnNuevo);
        final Button btnActualizar = view.findViewById(R.id.btnMasOpciones);
        btnNuevo.setOnClickListener(v -> {
            Intent intent;
            switch (objeto){
                case "Clientes":
                    break;
                case "Productos":
                    break;
                case "Facturacion":
                    break;
            }

        });
        btnActualizar.setOnClickListener(v -> {
            Intent intent;
            switch (objeto){
                case "Clientes":
                    break;
                case "Productos":
                    break;
            }
        });

        builder.setView(view).setTitle("Opciones de " + objeto).setPositiveButton("", (dialog, id) -> {((ViewGroup)drawerLayout.getParent()).removeView(view);
        }).setNegativeButton("Cancelar", (dialog, which) -> ((ViewGroup)drawerLayout.getParent()).removeView(view));
        return builder.create();
    }
}

