package com.example.distribuidorank.vista;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.SelectionAdapter;
import com.example.distribuidorank.modelo.Producto;

import java.util.ArrayList;

public class FacturacionActivity extends AppCompatActivity {
    private ArrayList<Producto> listaproductos;
    private ArrayList<String> strListProducto;
    // MultiSelect list adapter
    private ListView listviewProductos;
    private SelectionAdapter mAdapter;
    private View view;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private float precioCompra, precioVenta, dineroUtilidad, porcentajeUtilidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);

        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.my_card, null);
        builder = new AlertDialog.Builder(this);

        Bundle bundle = this.getIntent().getExtras();
        //Extrayendo el extra de tipo cadena
        listaproductos = (ArrayList<Producto>) bundle.getSerializable("productos");

        listviewProductos = findViewById(R.id.lvFacturacionProductos);
        llenarListViewProductos();
        listviewProductos.setOnItemClickListener((parent, view, position, id) -> {

            int idProducto = Integer.parseInt(listviewProductos.getItemAtPosition(position).toString().substring(0, 1));

        });
    }

    private void llenarListViewProductos() {
        try {
            strListProducto = new ArrayList<>();
            for (Producto p : listaproductos) {
                strListProducto.add(p.getId() + ": " + p.getDescripcion());
            }

            mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, strListProducto);
            listviewProductos.setAdapter(mAdapter);
            //setUpActionBar();
        } catch (NullPointerException npe) {
            Toast.makeText(FacturacionActivity.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private AlertDialog dialogEditarProducto(Producto producto) {
        view = inflater.inflate(R.layout.my_card, null);
        Button btnCalcularUtilidad = findViewById(R.id.btnCardCalcular);
        Button btnGuardarCambios = findViewById(R.id.btnCardGuardar);
        btnCalcularUtilidad.setOnClickListener(v -> {
            precioCompra = producto.getPrecio_compra();
            precioVenta = producto.getPrecio_compra();
            porcentajeUtilidad = (precioVenta - precioCompra) / precioCompra;
            
        });

        btnGuardarCambios.setOnClickListener(v -> {

        });
        return builder.create();
    }

}