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
import com.example.distribuidorank.controlador.FormatoFecha;
import com.example.distribuidorank.controlador.SelectionAdapter;
import com.example.distribuidorank.modelo.Producto;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class FacturacionActivity extends AppCompatActivity {
    private ArrayList<Producto> listaproductos;
    private ArrayList<String> strListProducto;
    private Producto producto;
    private FormatoFecha fecha;
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
    private float precioCompra, precioVenta, dineroGanancia, porcentajeUtilidad;
    private String stringFecha, stringCliente, strinAgente;
    private TextInputEditText tvFecha, tvAgente, tvDineroTotaldelPedido, tvDineroUtilidadGeneral, txtCardNombreProducto,
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

    private void instanciaComponentesDeLaUi() {
        tvFecha = findViewById(R.id.textViewFecha);
        tvAgente = findViewById(R.id.textViewAgente);
        tvDineroTotaldelPedido = findViewById(R.id.textViewTotaldelPedido);
        tvDineroUtilidadGeneral = findViewById(R.id.textViewUtilidadGeneral);
    }

    private void mostrarDatosResumenEnLaUi() {
        try {
            asignarDatosParaResumenDelPedido();
            tvFecha.setText(stringFecha);
            tvAgente.setText(strinAgente);
            tvDineroTotaldelPedido.setText(calcularDineroTotalDelPedido());
            tvDineroUtilidadGeneral.setText(calcularDineroUtilidadTotalDelPedido());
        } catch (Error npe) {
            Toast.makeText(FacturacionActivity.this, "Error, favor verificar: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void asignarDatosParaResumenDelPedido() {
        try {
            fecha = new FormatoFecha();
            strinAgente = "Michael Busto L";
            stringFecha = fecha.getFechaConFormato(this);
            stringCliente = "Algun cliente";
        } catch (Error npe) {
            Toast.makeText(FacturacionActivity.this, "Error, favor verificar: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String calcularDineroTotalDelPedido() {
        try {
            float dineroTotalDelPedido = 0;
            for (Producto producto : listaproductos) {
                dineroTotalDelPedido += producto.getPrecio_venta() * producto.getCantidad();
            }
            return String.valueOf(dineroTotalDelPedido);
        } catch (NullPointerException npe) {
            Toast.makeText(FacturacionActivity.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private String calcularDineroUtilidadTotalDelPedido() {
        try {
            float dineroUtilidadTotal = 0;
            int cantidad = 0;
            for (Producto producto : listaproductos) {
                cantidad = producto.getCantidad();
                dineroUtilidadTotal += (producto.getPrecio_venta() * cantidad) - (producto.getPrecio_compra() * cantidad);
            }
            return String.valueOf(dineroUtilidadTotal);
        } catch (NullPointerException npe) {
            Toast.makeText(FacturacionActivity.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
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
        txtCardCantidadProducto = view.findViewById(R.id.txtCardCantidad);
        txtCardNombreProducto = view.findViewById(R.id.txtCardNombreProducto);
        txtCardPorcentajeUtilidad = view.findViewById(R.id.txtCardPorcentajeUtilidad);

        txtCardNombreProducto.setText(product.getDescripcion());
        txtCardPrecioVenta.setText(String.valueOf(product.getPrecio_venta()));
        txtCardCantidadProducto.setText(String.valueOf(product.getCantidad()));
        txtCardPrecioCompra.setText(String.valueOf(product.getPrecio_compra()));
        txtCardPorcentajeUtilidad.setText(String.valueOf(product.getUtilidad()));
        txtCardDineroGanancia.setText(String.valueOf(product.getPrecio_venta() - product.getPrecio_compra()));

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

            tvDineroTotaldelPedido.setText(calcularDineroTotalDelPedido());
            tvDineroUtilidadGeneral.setText(calcularDineroUtilidadTotalDelPedido());
        });

        btnGuardarCambios.setOnClickListener(v -> {
            // cerrar el dialog y conservar informacion modificada
        });
        builder.setView(view).setTitle("Editar valores de producto");
        return builder.create();
    }
}