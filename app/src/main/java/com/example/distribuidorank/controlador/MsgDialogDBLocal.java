package com.example.distribuidorank.controlador;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.distribuidorank.Database.Conexiones;
import com.example.distribuidorank.R;
import com.example.distribuidorank.modelo.Cliente;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.File;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MsgDialogDBLocal {
    private List<Cliente> listaClientes;
    private AlertDialog.Builder builder;
    private DrawerLayout drawerLayout;
    private LayoutInflater inflater;
    private Conexiones conexiones;
    private Gson objetoGson;
    private View view;
    private Context context;
    private ProgressDialog progressBar;

    public MsgDialogDBLocal() {
    }

    /**
     * Recibe contexto para inflar layout y mostrar dialog
     **/
    @SuppressLint("InflateParams")
    public MsgDialogDBLocal(Context contexto) {
        context = contexto;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = Objects.requireNonNull(inflater).inflate(R.layout.dialog_opciones, null);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        builder = new AlertDialog.Builder(context);
    }

    /**
     * AlertDialog con opciones de a realizar sobre db local
     **/
    public AlertDialog opcionesDBLocal() {
        File DB_FILE = new File("/data/data/com.example.distribuidorank/databases/pos.db");
        view = inflater.inflate(R.layout.dialog_basedatos, null);
        final Button btnCreardb = view.findViewById(R.id.btnCrearBaseDatos);
        final Button btnSincroLocal = view.findViewById(R.id.btnSincronizarLocal);
        final Button btnSincroRemoto = view.findViewById(R.id.btnSincronizarRemoto);
        final Button btnEliminardb = view.findViewById(R.id.btnEliminarBaseDatos);
        btnCreardb.setOnClickListener(v -> {
            if (!DB_FILE.exists()) {
                conexiones = new Conexiones();
                conexiones.crearDbLocal(context);
                Toast.makeText(context, "Base de datos local creada correctamente!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "La base de datos local, ya existe!!!", Toast.LENGTH_LONG).show();
            }
        });
        // Ejecutar metodos que solicitan datos remotos
        // dentro de cada metodo llamar al metodo dentro de POS_DataBase
        // que se encargará de insertar la informacion de manera local
        btnSincroLocal.setOnClickListener(v -> {
            mostrarProgressBar();   // Barra de progreso, no porcentual
            getClientes();
            getUnidades();
            getProductos();
            getLocalidades();
            getProveedores();
            getCabecerasFacturas();
            getDetallesFacturas();

        });
        btnSincroRemoto.setOnClickListener(v -> { });
        btnEliminardb.setOnClickListener(v -> { });

        builder.setView(view).setTitle("Almacenamiento de datos").setPositiveButton("", (dialog, id) -> {
            ((ViewGroup) drawerLayout.getParent()).removeView(view);
        });
        return builder.create();
    }

    /** Solicita los clientes al servidor remoto*/
    public void getClientes(){
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(context)) {
            Call<List<Cliente>> requestLastReports = ApiUtils.getApiServices().getClientes();
            requestLastReports.enqueue(new Callback<List<Cliente>>() {
                @Override
                public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, response.message() + "Error al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Manejar datos obtenidos en la petición
                        objetoGson = new Gson();
                        conexiones = new Conexiones(context);
                        listaClientes = response.body();
                        // Enviamos lista clientes a clase conexion para que inserte datos en tabla local
                        conexiones.insertarClientesEnDbLocal(objetoGson.toJson(listaClientes));
                    }
                }

                @Override
                public void onFailure(Call<List<Cliente>> call, Throwable t) {
                    Toast.makeText(context, t.getMessage() +  "La petición falló!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    /**Solicitamos los datos de unidades al servidor remoto */
    private void getUnidades() {
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(context)) {
            Call<JsonArray> requestProductos = ApiUtils.getApiServices().getUnidades();
            requestProductos.enqueue(new Callback<JsonArray>() {
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (!response.isSuccessful()) {
                        // Mensaje de error
                        Toast.makeText(context, response.message() +" al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Manejar datos obtenidos en la petición
                        objetoGson = new Gson();
                        conexiones = new Conexiones(context);
                        JsonArray arrayListaUndidades = response.body();
                        conexiones.insertarUnidadesEnDbLocal(arrayListaUndidades.toString());
                    }
                }
                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(context, "La peticion falló!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    /** Solicitamos los productos al servidor remoto */
    private void getProductos() {
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(context)) {
            Call<JsonArray> requestProductos = ApiUtils.getApiServices().getProductos();
            requestProductos.enqueue(new Callback<JsonArray>() {
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (!response.isSuccessful()) {
                        // Mensaje de error
                        Toast.makeText(context, response.message() +" al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Manejar datos obtenidos en la petición
                        objetoGson = new Gson();
                        conexiones = new Conexiones(context);
                        JsonArray listaPro = response.body();
                        // Enviamos lista productos a clase conexion para que inserte datos en tabla local
                        conexiones.insertarProductosEnDbLocal(objetoGson.toJson(listaPro));
                    }
                }
                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(context, "La peticion falló!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }
    private void getLocalidades() {
    }

    private void getProveedores() {
    }
    private void getCabecerasFacturas() {
    }

    private void getDetallesFacturas() {
    }

    /* Mostramos barra de progreso mientras se descargan y almacenan los datos en db local*/
    public void mostrarProgressBar(){
        progressBar = new ProgressDialog(context);
        progressBar.setMessage("Sincronizando datos...");
        progressBar.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);
        progressBar.show();
        final int tiempoTotal = 180; // 3 segundos
        final Thread thread = new Thread(){
            @Override
            public void run() {
                int contador = 0;
                while (contador<tiempoTotal){
                    try {
                        sleep(180);
                        contador+=5;
                        progressBar.setProgress(contador);
                    } catch (InterruptedException interruptedException){
                        interruptedException.printStackTrace();
                    }
                }
                progressBar.dismiss();
            }
        };
        thread.start();
    }

}

