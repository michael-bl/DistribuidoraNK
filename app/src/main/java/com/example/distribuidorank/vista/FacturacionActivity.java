package com.example.distribuidorank.vista;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.CustomAdapter;
import com.example.distribuidorank.modelo.Producto;

import java.util.ArrayList;

public class FacturacionActivity extends AppCompatActivity {
    private Resources resource;
    private ArrayList<Producto> listaproductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);

        Bundle bundle = this.getIntent().getExtras();
        //Extrayendo el extra de tipo cadena
        listaproductos = (ArrayList<Producto>) bundle.getSerializable("productos");

        resource = this.getResources();
        setListaAgridviewProductos();
    }
    private void setListaAgridviewProductos(){
        try {
            GridView myGridView = findViewById(R.id.gridViewProductos);
            CustomAdapter adapterPersonalizado = new CustomAdapter(this, listaproductos);
            myGridView.setAdapter(adapterPersonalizado);
        } catch (Error e){
            Toast.makeText(FacturacionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}