package com.example.distribuidorank.vista;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.distribuidorank.Database.Conexiones;
import com.example.distribuidorank.Database.ExistDataBaseSqlite;
import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.ApiUtils;
import com.example.distribuidorank.controlador.ConnectivityService;
import com.example.distribuidorank.controlador.FormatoFecha;
import com.example.distribuidorank.controlador.SelectionAdapter;
import com.example.distribuidorank.modelo.Cabecera_factura;
import com.example.distribuidorank.modelo.Cliente;
import com.example.distribuidorank.modelo.Detalle_factura;
import com.example.distribuidorank.modelo.Producto;
import com.example.distribuidorank.modelo.Usuario;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacturacionActivity extends AppCompatActivity {
    private TextInputEditText tvFecha, tvAgente, tvDineroTotaldelPedido, tvDineroUtilidadGeneral, txtCardNombreProducto,
            txtCardPrecioCompra, txtCardPrecioVenta, txtCardPorcentajeUtilidad, txtCardDineroGanancia, txtCardCantidadProducto;
    private float precioCompra, precioVenta, dineroGanancia, porcentajeUtilidad;
    private String stringFecha, stringFkCliente, stringFkUsuario;
    private ArrayList<Producto> listaproductos;
    private List<Cliente> listaClientes;
    private ArrayList<String> strListProducto;
    private FormatoFecha fecha;
    private Producto producto;
    // MultiSelect list adapter
    private ListView listviewProductos;
    private SelectionAdapter mAdapter;
    // Para mostrar alertDialog
    private NavigationView navigationView;
    private AlertDialog.Builder builder;
    private DrawerLayout drawerLayout;
    private LayoutInflater inflater;
    private Spinner spCliente;
    private View view;
    private Cliente cliente;
    private int cantidadProductos;
    private Conexiones conexiones;
    private ExistDataBaseSqlite existDb;
    private ArrayList<String> stringIdsListaClientes;


    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);

        spCliente = findViewById(R.id.spFacturaCliente);

        instanciaComponentesDeLaUi();

        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.my_card, null);
        builder = new AlertDialog.Builder(this);

        drawerLayout = findViewById(R.id.drawer_layout);

        Bundle bundle = this.getIntent().getExtras();
        //Extrayendo el extra de tipo cadena
        listaproductos = (ArrayList<Producto>) bundle.getSerializable("productos");

        mostrarDatosResumenEnLaUi();

        existDb = new ExistDataBaseSqlite();
        if (existDb.existeDataBaseLocal())
            getClientesDeDbLocal();
        else getClientesDeDbRemoto();

        listviewProductos = findViewById(R.id.lvFacturacionProductos);
        llenarListViewProductos();
        listviewProductos.setOnItemClickListener((parent, view, position, id) -> {
            producto = new Producto();
            producto = listaproductos.get(position);
            dialogEditarProducto(producto, position).show();
        });

        Button btnGuardarFactura = findViewById(R.id.btnGuardarFactura);
        btnGuardarFactura.setOnClickListener(v -> guardarInfoDeFacturaEnDbLocal());
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
            tvAgente.setText(stringFkUsuario);
            tvDineroTotaldelPedido.setText(calcularDineroTotalDelPedido());
            tvDineroUtilidadGeneral.setText(calcularDineroUtilidadTotalDelPedido());
        } catch (Error npe) {
            Toast.makeText(FacturacionActivity.this, "Error, favor verificar: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void asignarDatosParaResumenDelPedido() {
        try {
            fecha = new FormatoFecha();
            stringFkUsuario = "Michael Busto L";
            stringFecha = fecha.getFechaConFormato(this);
            stringFkCliente = "Algun cliente";
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
            int cantidad;
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

    @SuppressLint("InflateParams")
    private AlertDialog dialogEditarProducto(Producto product, int position) {
        view = inflater.inflate(R.layout.my_card, null);
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
        builder.setView(view).setTitle("Editar valores de producto");
        return builder.create();
    }

    private void getClientesDeDbLocal() {
        try {
            conexiones = new Conexiones(this);
            List<Cliente> listaClientes = new ArrayList<>(conexiones.getClientes());
            stringIdsListaClientes = new ArrayList<>();
            for (Cliente cliente : listaClientes) {
                stringIdsListaClientes.add(cliente.getId() + "-" + cliente.getNombre());
            }
            llenarSpinnerClientes(stringIdsListaClientes, listaClientes);
        } catch (NullPointerException npe) {
            Toast.makeText(FacturacionActivity.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JsonSyntaxException jse) {
            Toast.makeText(FacturacionActivity.this, "Error, verifique por favor: " + jse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getClientesDeDbRemoto() {
        ConnectivityService estaConectado = new ConnectivityService();
        if (estaConectado.stateConnection(this)) {
            Call<List<Cliente>> requestLastReports = ApiUtils.getApiServices().getClientes();
            requestLastReports.enqueue(new Callback<List<Cliente>>() {
                @Override
                public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(FacturacionActivity.this, response.message() + "Error al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        listaClientes = response.body();
                        stringIdsListaClientes = new ArrayList<>();
                        for (int i = 0; i < listaClientes.size(); i++) {
                            cliente = new Cliente();
                            stringIdsListaClientes.add(i, listaClientes.get(i).getId() + "-" + listaClientes.get(i).getNombre());
                            cliente.setFk_localidad(listaClientes.get(i).getFk_localidad());
                            cliente.setNombre(listaClientes.get(i).getNombre());
                            cliente.setTelefono(listaClientes.get(i).getTelefono());
                            cliente.setEmail(listaClientes.get(i).getEmail());
                            cliente.setDireccion(listaClientes.get(i).getDireccion());
                            cliente.setId(listaClientes.get(i).getId());
                        }
                        llenarSpinnerClientes(stringIdsListaClientes, listaClientes);
                        Toast.makeText(FacturacionActivity.this, "Datos cargados exitosamente!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Cliente>> call, Throwable t) {
                    Toast.makeText(FacturacionActivity.this, t.getMessage() + "La peticion falló!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(FacturacionActivity.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    private void llenarSpinnerClientes(ArrayList<String> stringListaClientes, List<Cliente> listaCliente) {
        try {
            this.listaClientes = listaCliente;
            stringIdsListaClientes = stringListaClientes;
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringListaClientes);
            this.spCliente.setAdapter(adapter);
        } catch (NullPointerException npe) {
            Toast.makeText(FacturacionActivity.this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void guardarInfoDeFacturaEnDbLocal() {
        try {
            //los campos son: id, fk_cabecera, fk_producto, utilidad, precio_compra, precio_venta, cantidad_productos
            Detalle_factura detalle_factura;
            ArrayList<Detalle_factura> listaDetalleFactura = new ArrayList<>();

            //los campos son: id, tipo, fk_cliente, fk_usuario, fecha
            Cabecera_factura cabecera_factura = new Cabecera_factura();

            Usuario usuario = new Usuario();
            conexiones = new Conexiones(this);
            usuario = (Usuario) conexiones.getUsuarioXid("1");

            stringFkUsuario = usuario.getId();
            String[] dividirId = spCliente.getItemAtPosition(spCliente.getSelectedItemPosition()).toString().trim().split("-");
            stringFkCliente = dividirId[0];
            for(Producto producto: listaproductos){
                detalle_factura = new Detalle_factura();
                detalle_factura.setFk_producto(producto.getId());
                detalle_factura.setUtilidad(producto.getUtilidad());
                detalle_factura.setPrecio_compra(producto.getPrecio_compra());
                detalle_factura.setPrecio_venta(producto.getPrecio_venta());
                detalle_factura.setCantidad(producto.getCantidad());
                listaDetalleFactura.add(detalle_factura);
            }

            Toast.makeText(FacturacionActivity.this, "Ced cliente===> " + stringFkCliente, Toast.LENGTH_LONG).show();
        } catch (NullPointerException npe) {
            Toast.makeText(FacturacionActivity.this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}