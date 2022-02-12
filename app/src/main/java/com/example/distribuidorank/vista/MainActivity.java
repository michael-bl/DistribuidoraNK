package com.example.distribuidorank.vista;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distribuidorank.Database.Conexiones;
import com.example.distribuidorank.Database.ExistDataBaseSqlite;
import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.ApiUtils;
import com.example.distribuidorank.controlador.ConnectivityService;
import com.example.distribuidorank.controlador.MsgDialogDBLocal;
import com.example.distribuidorank.controlador.RecyclerViewAdapter;
import com.example.distribuidorank.modelo.Cliente;
import com.example.distribuidorank.modelo.Factura;
import com.example.distribuidorank.modelo.Localidad;
import com.example.distribuidorank.modelo.Producto;
import com.example.distribuidorank.modelo.Proveedor;
import com.example.distribuidorank.modelo.Targeta;
import com.example.distribuidorank.modelo.Unidad;
import com.example.distribuidorank.modelo.Usuario;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private NavigationView navigationView;
    private List<Producto> listaProductos;
    private ExistDataBaseSqlite existDb;
    private AlertDialog.Builder builder;
    private ArrayList<Targeta> cardList;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private Conexiones conexiones;
    private Localidad localidad;
    private Proveedor proveedor;
    private Producto producto;
    private Usuario usuario;
    private Cliente cliente;
    private Factura factura;
    private Intent intent;
    private Bundle bundle;
    private Unidad unidad;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);

        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_opciones, null);
        builder = new AlertDialog.Builder(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //getProductosDeLocalOremoto();
        //crearTargetasDeProductos(listaProductos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_usuario) {
            dialogOpciones("Usuario").show();
        }
        if (id == R.id.nav_cliente) {
            dialogOpciones("Cliente").show();
        }
        if (id == R.id.nav_producto) {
            dialogOpciones("Producto").show();
        }
        if (id == R.id.nav_proveedor) {
            dialogOpciones("Proveedor").show();
        }
        if (id == R.id.nav_localizacion) {
            dialogOpciones("Localidad").show();
        }
        if (id == R.id.nav_facturacion) {
            dialogOpciones("Factura").show();
        }
        if (id == R.id.nav_unidadmedida) {
            dialogOpciones("Unidad").show();
        }
        if (id == R.id.nav_configdb) {
            //Dialog para crear db local, sincronizar hacia local o remoto
            MsgDialogDBLocal messageDialog = new MsgDialogDBLocal(this);
            messageDialog.opcionesDBLocal().show();
        } else if (id == R.id.nav_send) {
            return true;
        }
        if (id == R.id.nav_share) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void getProductosDeLocalOremoto() {
        conexiones = new Conexiones(this);
        existDb = new ExistDataBaseSqlite();
        if (existDb.existeDataBaseLocal())
            getProductosDeDbLocal();
        else getProductosDeDbRemoto();
    }

    private void getProductosDeDbLocal() {
        try {
            conexiones = new Conexiones(this);
            ArrayList<Producto> arrayListProductos = new ArrayList<>(conexiones.getProductos());
            crearTargetasDeProductos(arrayListProductos);
        } catch (NullPointerException e) {
            Toast.makeText(MainActivity.this, "Error, verifique por favor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void crearTargetasDeProductos(List<Producto> listaProductos) {
        // Lista de cardviews dinámicas para mostrar productos en la vista MainActivity
        cardList = new ArrayList<>();
        ArrayList<Producto> arrayListProductos = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerVCard);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        if (listaProductos == null) {
            Targeta obj = new Targeta();
            obj.setImagen("Sin datos");
            obj.setNombre("No haz realizado pedidos");
            obj.setPrecioCompra("0.00");
            obj.setPrecioVenta("0.00");
            obj.setPorcentajeUtilidad("0.00");
            cardList.add(0, obj);
        } else {
            for (int i = 0; i < listaProductos.size(); i++) {
                Targeta obj = new Targeta();
                //obj.setImagen(listaProductos.get(i).Imagen);
                Producto nuevoProducto = listaProductos.get(i);
                obj.setIdProducto(nuevoProducto.getId());
                obj.setNombre(nuevoProducto.getDescripcion());
                obj.setPrecioCompra(Float.toString(nuevoProducto.getPrecio_compra()));
                obj.setPrecioVenta(Float.toString(nuevoProducto.getPrecio_venta()));
                obj.setPorcentajeUtilidad(Float.toString(nuevoProducto.getUtilidad()));
                arrayListProductos.add(nuevoProducto);
                cardList.add(i, obj);
            }
        }
        recyclerAdapter = new RecyclerViewAdapter(cardList, arrayListProductos);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void getProductosDeDbRemoto() {
        ConnectivityService estaConectado = new ConnectivityService();
        if (estaConectado.stateConnection(this)) {
            Call<JsonArray> requestProductos = ApiUtils.getApiServices().getProductos();
            requestProductos.enqueue(new Callback<JsonArray>() {
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (!response.isSuccessful()) {
                        // Mensaje de error
                        Toast.makeText(MainActivity.this, response.message() + " al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Manejar datos obtenidos en la petición
                        listaProductos = new ArrayList<>();
                        JsonArray listaPro = response.body();
                        for (int j = 0; j < listaPro.size(); j++) {
                            Producto p = new Producto();
                            p.setId(listaPro.get(j).getAsJsonObject().get("id").getAsInt());
                            p.setDescripcion(listaPro.get(j).getAsJsonObject().get("descripcion").toString());
                            p.setFk_unidad(listaPro.get(j).getAsJsonObject().get("fk_unidad").getAsInt());
                            p.setPrecio_compra(listaPro.get(j).getAsJsonObject().get("precio_compra").getAsFloat());
                            p.setPrecio_venta(listaPro.get(j).getAsJsonObject().get("precio_venta").getAsFloat());
                            p.setUtilidad(listaPro.get(j).getAsJsonObject().get("utilidad").getAsFloat());
                            listaProductos.add(p);
                        }
                        crearTargetasDeProductos(listaProductos);
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "La peticion falló!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    private AlertDialog dialogOpciones(String objeto) {

        view = inflater.inflate(R.layout.dialog_opciones, null);
        final Button btnNuevo = view.findViewById(R.id.btnNuevo);
        final Button btnActualizar = view.findViewById(R.id.btnMasOpciones);
        btnNuevo.setOnClickListener(v -> {
            // Accion 0=nuevo
            switch (objeto) {
                case "Usuario":
                    intent = new Intent(MainActivity.this, UsuarioActivity.class);
                    bundle = new Bundle();
                    usuario = new Usuario();
                    usuario.setAccion(0); // Cero para insertar nuevo usuario
                    bundle.putSerializable("usuario", usuario);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case "Cliente":
                    intent = new Intent(MainActivity.this, ClienteActivity.class);
                    bundle = new Bundle();
                    cliente = new Cliente();
                    cliente.setAccion(0);
                    bundle.putSerializable("cliente", cliente);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case "Producto":
                    intent = new Intent(MainActivity.this, ProductoActivity.class);
                    bundle = new Bundle();
                    producto = new Producto();
                    producto.setAccion(0);
                    bundle.putSerializable("producto", producto);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case "Proveedor":
                    intent = new Intent(MainActivity.this, ProveedorActivity.class);
                    bundle = new Bundle();
                    proveedor = new Proveedor();
                    proveedor.setAccion(0);
                    bundle.putSerializable("proveedor", proveedor);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case "Factura":
                    intent = new Intent(MainActivity.this, FacturacionContent.class);
                    startActivity(intent);
                    break;
                case "Localidad":
                    intent = new Intent(this.getApplicationContext(), LocalidadActivity.class);
                    bundle = new Bundle();
                    localidad = new Localidad();
                    localidad.setAccion(0);
                    bundle.putSerializable("localidad", localidad);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case "Unidad":
                    intent = new Intent(this.getApplicationContext(), UnidadActivity.class);
                    bundle = new Bundle();
                    unidad = new Unidad();
                    unidad.setAccion(0);
                    bundle.putSerializable("unidad", unidad);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }

        });
        btnActualizar.setOnClickListener(v -> {

            switch (objeto) {
                case "Usuario":
                    intent = new Intent(MainActivity.this, UsuarioContent.class);
                    startActivity(intent);
                    break;
                case "Cliente":
                    intent = new Intent(MainActivity.this, ClienteContent.class);
                    startActivity(intent);
                    break;
                case "Producto":
                    intent = new Intent(MainActivity.this, ProductoContent.class);
                    startActivity(intent);
                    break;
                case "Proveedor":
                    intent = new Intent(MainActivity.this, ProveedorContent.class);
                    startActivity(intent);
                    break;
                case "Factura":
                    Toast.makeText(MainActivity.this, "Función en desarrollo!" , Toast.LENGTH_SHORT).show();
                    break;
                case "Localidad":
                    intent = new Intent(MainActivity.this, LocalidadContent.class);
                    startActivity(intent);
                    break;
                case "Unidad":
                    intent = new Intent(MainActivity.this, UnidadContent.class);
                    startActivity(intent);
                    break;

            }
        });

        builder.setView(view).setTitle("Opciones de " + objeto);
        return builder.create();
    }
}