package com.example.distribuidorank.vista;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.distribuidorank.modelo.Localidad;
import com.example.distribuidorank.modelo.Producto;
import com.example.distribuidorank.modelo.Proveedor;
import com.example.distribuidorank.modelo.Targeta;
import com.example.distribuidorank.modelo.Usuario;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;

import java.io.Serializable;
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
    private Intent intent;
    private Bundle bundle;
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

        getProductosLocalOremoto();
    }

    private void getProductosLocalOremoto() {
        //Solicitar productos
        conexiones = new Conexiones(this);
        existDb = new ExistDataBaseSqlite();
        if (existDb.existeDataBase())
            getProductosLocal();
        else getProductosRemoto();
    }

    private void getProductosLocal() {
        try {
            conexiones = new Conexiones(this);
            ArrayList<Producto> arrayListProductos = new ArrayList<>(conexiones.getProductos());
            crearTargetasProductos(arrayListProductos);
        } catch (NullPointerException e) {
            Toast.makeText(MainActivity.this, "Error, verifique por favor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up btnSiguienteContent, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.nav_usuario) {
            dialogOpciones("Usuarios").show();
        }
        if (id == R.id.nav_cliente) {
            dialogOpciones("Clientes").show();
        }
        if (id == R.id.nav_producto) {
            dialogOpciones("Productos").show();
        }
        if (id == R.id.nav_proveedor) {
            dialogOpciones("Proveedor").show();
        }
        if (id == R.id.nav_localizacion) {
            Intent intent = new Intent(this.getApplicationContext(), LocalidadActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_facturacion) {
            dialogOpciones("Facturacion").show();
        }
        if (id == R.id.nav_unidadmedida) {
            Intent intent = new Intent(this.getApplicationContext(), UnidadActivity.class);
            startActivity(intent);
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

    /**
     * Recibe lista de productos guardados en bd, luego crea las targetas para cada uno
     */
    private void crearTargetasProductos(List<Producto> listaProductos) {
        // Lista de cardviews dinámicas para mostrar productos
        cardList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerVCard);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        if (listaProductos == null) {
            Targeta obj = new Targeta();
            obj.setImagen("Sin imagen");
            obj.setNombre("Sin productos");
            obj.setPrecioCompra("0.00");
            obj.setPrecioVenta("0.00");
            obj.setUtilidad("0.00");
            cardList.add(0, obj);
        } else {
            for (int i = 0; i < listaProductos.size(); i++) {
                Targeta obj = new Targeta();
                //obj.setImagen(listaProductos.get(i).Imagen);
                Producto p = listaProductos.get(i);
                obj.setNombre(p.getDescripcion());
                obj.setPrecioCompra(Float.toString(p.getPrecio_compra()));
                obj.setPrecioVenta(Float.toString(p.getPrecio_venta()));
                obj.setUtilidad(Float.toString(p.getUtilidad()));
                cardList.add(i, obj);
            }
        }
        recyclerAdapter = new RecyclerViewAdapter(cardList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * Solicitamos los datos al servidor remoto
     */
    private void getProductosRemoto() {
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(this)) {
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
                            p.setFk_familia(listaPro.get(j).getAsJsonObject().get("fk_unidad").getAsInt());
                            p.setPrecio_compra(listaPro.get(j).getAsJsonObject().get("precio_compra").getAsFloat());
                            p.setPrecio_venta(listaPro.get(j).getAsJsonObject().get("precio_venta").getAsFloat());
                            p.setUtilidad(listaPro.get(j).getAsJsonObject().get("utilidad").getAsFloat());
                            listaProductos.add(p);
                        }
                        crearTargetasProductos(listaProductos);
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

    /**
     * AlertDialog con opciones a realizar sobre tipo de objeto seleccionado
     */
    private AlertDialog dialogOpciones(String objeto) {

        view = inflater.inflate(R.layout.dialog_opciones, null);
        final Button btnNuevo = view.findViewById(R.id.btnNuevo);
        final Button btnActualizar = view.findViewById(R.id.btnMasOpciones);
        btnNuevo.setOnClickListener(v -> {
            // Accion 0=nuevo
            switch (objeto) {
                case "Usuarios":
                    intent = new Intent(MainActivity.this, UsuarioActivity.class);
                    bundle = new Bundle();
                    usuario = new Usuario();
                    usuario.setAccion(0); // Cero para insertar nuevo usuario
                    bundle.putSerializable("usuario", usuario);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case "Clientes":
                    intent = new Intent(MainActivity.this, ClienteActivity.class);
                    bundle = new Bundle();
                    cliente = new Cliente();
                    cliente.setAccion(0);
                    bundle.putSerializable("cliente", cliente);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case "Productos":
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
                case "Facturacion":
                    intent = new Intent(this.getApplicationContext(), FacturacionActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable("productos", (Serializable) listaProductos); //OJO CON ESTE DATO ENVIADO
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case "Localidad":
                    intent = new Intent(this.getApplicationContext(), LocalidadActivity.class);
                    bundle = new Bundle();
                    localidad = new Localidad();
                    localidad.setAccion(0);
                    bundle.putSerializable("localidades", (Serializable) localidad);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }

        });
        btnActualizar.setOnClickListener(v -> {

            switch (objeto) {
                case "Usuarios":
                    intent = new Intent(MainActivity.this, UsuarioContent.class);
                    startActivity(intent);
                    break;
                case "Clientes":
                    intent = new Intent(MainActivity.this, ClienteContent.class);
                    startActivity(intent);
                    break;
                case "Productos":
                    intent = new Intent(MainActivity.this, ProductoContent.class);
                    startActivity(intent);
                    break;
                case "Proveedor":
                    intent = new Intent(MainActivity.this, ProveedorContent.class);
                    startActivity(intent);
                    break;
            }
        });

        builder.setView(view).setTitle("Opciones de " + objeto).setPositiveButton("", (dialog, id) -> {
            ((ViewGroup) drawerLayout.getParent()).removeView(view);
        });
        return builder.create();
    }
}