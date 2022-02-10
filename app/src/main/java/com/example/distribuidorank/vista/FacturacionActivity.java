package com.example.distribuidorank.vista;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.SelectionAdapter;
import com.example.distribuidorank.modelo.Producto;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class FacturacionActivity extends AppCompatActivity {
    private ArrayList<Producto> listaproductos;
    private ArrayList<String> strListProducto;
    private Producto producto;
    // MultiSelect list adapter
    private ListView listviewProductos;
    private SelectionAdapter mAdapter;
    // Para mostrar alertDialog
    private NavigationView navigationView;
    private AlertDialog.Builder builder;
    private DrawerLayout drawerLayout;
    private LayoutInflater inflater;
    private View view;
    private int cantidadProductos;
    private float precioCompra, precioVenta, dineroGanancia, porcentajeUtilidad, dineroTotalDelPedido, dineroUtilidadTotal;
    private String stringFecha, stringCliente, strinAgente;
    private TextInputEditText tvFecha, tvCliente, tvAgente , tvTotaldelPedido , tvDineroUtilidadTotal, txtCardNombreProducto,
            txtCardPrecioCompra, txtCardPrecioVenta, txtCardPorcentajeUtilidad, txtCardDineroGanancia, txtCardCantidadProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);

        instanciaComponentesDeLaUi();

        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.my_card, null);
        builder = new AlertDialog.Builder(this);

        drawerLayout = findViewById(R.id.drawer_layout);

        Bundle bundle = this.getIntent().getExtras();
        //Extrayendo el extra de tipo cadena
        listaproductos = (ArrayList<Producto>) bundle.getSerializable("productos");

        mostrarDatosResumenEnLaUi();

        listviewProductos = findViewById(R.id.lvFacturacionProductos);
        llenarListViewProductos();
        listviewProductos.setOnItemClickListener((parent, view, position, id) -> {
            producto = new Producto();
            producto = listaproductos.get(position);
            dialogEditarProducto(producto, position).show();
        });
    }

    private void instanciaComponentesDeLaUi(){
        tvFecha = findViewById(R.id.textViewFecha);
        tvAgente = findViewById(R.id.textViewAgente);
        tvCliente = findViewById(R.id.textViewCliente);
        tvTotaldelPedido = findViewById(R.id.textViewTotaldelPedido);
        tvDineroUtilidadTotal = findViewById(R.id.textViewUtilidadGeneral);
    }

    private void mostrarDatosResumenEnLaUi(){
        try {
            realizarCalculoGeneralDelPedido();
            tvFecha.setText(stringFecha);
            tvCliente.setText(stringCliente);
            tvAgente.setText(strinAgente);
            tvTotaldelPedido.setText(String.valueOf(dineroTotalDelPedido));
        } catch (Error npe) {
            Toast.makeText(FacturacionActivity.this, "Error, favor verificar: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void realizarCalculoGeneralDelPedido(){
        strinAgente ="Michael Busto L";
        stringFecha="08-02-2022";
        stringCliente="Algun cliente";
        for (Producto producto: listaproductos) {
            dineroTotalDelPedido+= producto.getPrecio_venta();
        }
    }

    private void llenarListViewProductos() {
        try {
            strListProducto = new ArrayList<>();
            for (Producto p : listaproductos) {
                strListProducto.add(p.getId() + ": " + p.getDescripcion());
            }

            mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, strListProducto);
            listviewProductos.setAdapter(mAdapter);

        } catch (NullPointerException npe) {
            Toast.makeText(FacturacionActivity.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private AlertDialog dialogEditarProducto(Producto product, int position) {
        view = inflater.inflate(R.layout.my_card, null);
        Button btnGuardarCambios = view.findViewById(R.id.btnCardGuardar);
        Button btnCalcularUtilidad = view.findViewById(R.id.btnCardCalcular);

        txtCardPrecioVenta = view.findViewById(R.id.txtCardPrecioVenta);
        txtCardDineroGanancia = view.findViewById(R.id.txtCardGanancia);
        txtCardPrecioCompra = view.findViewById(R.id.txtCardPrecioCompra);
        txtCardNombreProducto = view.findViewById(R.id.txtCardNombreProducto);
        txtCardPorcentajeUtilidad = view.findViewById(R.id.txtCardPorcentajeUtilidad);
        txtCardCantidadProducto = view.findViewById(R.id.txtCardCantidad);

        txtCardPrecioVenta.setText(String.valueOf(product.getPrecio_venta()));
        txtCardPorcentajeUtilidad.setText(String.valueOf(product.getUtilidad()));
        txtCardNombreProducto.setText(product.getDescripcion());
        txtCardPrecioCompra.setText(String.valueOf(product.getPrecio_compra()));
        txtCardDineroGanancia.setText(String.valueOf(product.getPrecio_venta()- product.getPrecio_compra()));

        btnCalcularUtilidad.setOnClickListener(v -> {
            precioCompra = product.getPrecio_compra();
            precioVenta = product.getPrecio_venta();
            cantidadProductos = Integer.parseInt(txtCardCantidadProducto.getText().toString().trim());
            dineroGanancia = (precioVenta - precioCompra) * cantidadProductos;
            porcentajeUtilidad = (dineroGanancia / precioCompra) * 100;

            product.setUtilidad(porcentajeUtilidad);
            product.setPrecio_venta(Float.parseFloat(txtCardPrecioVenta.getText().toString().trim()));
            product.setCantidad(cantidadProductos);
            listaproductos.set(position, product);

            txtCardPorcentajeUtilidad.setText(String.valueOf(product.getUtilidad()));
            txtCardDineroGanancia.setText(String.valueOf(dineroGanancia));

            for (Producto producto1: listaproductos) {
                dineroTotalDelPedido+= producto1.getPrecio_venta() * producto1.getCantidad();
            }
            tvDineroUtilidadTotal.setText(String.valueOf(dineroTotalDelPedido));
        });

        btnGuardarCambios.setOnClickListener(v -> {
            // cerrar el dialog y conservar informacion modificada
        });
        builder.setView(view).setTitle("    Edición de producto");
        return builder.create();
    }
}