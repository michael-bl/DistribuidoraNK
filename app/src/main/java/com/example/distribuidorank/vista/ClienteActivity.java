package com.example.distribuidorank.vista;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.distribuidorank.Database.Conexiones;
import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.ApiUtils;
import com.example.distribuidorank.controlador.ConnectivityService;
import com.example.distribuidorank.modelo.Cliente;
import com.example.distribuidorank.modelo.Localidad;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteActivity extends AppCompatActivity {
    private TextInputEditText txtIdCliente, txtNombre, txtTelefono, txtEmail, txtDireccion;
    private String idCliente, nombre, telefono, email, direccion;
    private List<Localidad> listaLocalidades;
    private int accion, estado, idLocalidad;
    private ArrayList<Cliente> arrayCliente;
    private Spinner spLocalidad, spEstado;
    private Conexiones conexiones;
    private Cliente cliente;
    private Gson objetoGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instancia de textviews y otros componentes de la UI
        instanciaComponentes();

        cliente = new Cliente();

        spEstado = findViewById(R.id.spEstadoCliente);
        spLocalidad = findViewById(R.id.spLocalidadCliente);

        // Obtener datos del bundle
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            // Extrayendo el extra de tipo cadena
            cliente = (Cliente) bundle.getSerializable("cliente");
            if (Objects.requireNonNull(cliente).getAccion() != 0)
                mostrarDatosdelCliente(cliente);
            else
                llenarSpinnerEstado(0); // 0 para setear orden por defecto
        }

        Button btnGuardar = findViewById(R.id.btnGuardarCliente);
        btnGuardar.setOnClickListener(v -> {
            int guardarLocalOremoto = conexiones.getModoDeAlmacenamiento();
            if (guardarLocalOremoto == 0) guardarClienteLocal();
            else guardarClienteRemoto();
        });

        // Solicitamos localidades
        getLocalidadLocalOremoto();
    }

    /**
     * Inicializa los inputEditText de la interfaz cliente
     */
    private void instanciaComponentes() {
        txtDireccion = findViewById(R.id.txtDireccionCliente);
        txtTelefono = findViewById(R.id.txtTelefonoCliente);
        txtIdCliente = findViewById(R.id.txtCedulaCliente);
        txtNombre = findViewById(R.id.txtNombreCliente);
        txtEmail = findViewById(R.id.txtEmailCliente);
    }

    /**
     * Mostramos los datos del cliente en los campos de la interfaz
     */
    private void mostrarDatosdelCliente(Cliente cliente) {
        try {
            llenarSpinnerEstado(1);
            txtEmail.setText(cliente.getEmail());
            txtIdCliente.setText(cliente.getId());
            txtIdCliente.setFocusable(false);
            txtIdCliente.setEnabled(false);
            txtIdCliente.setCursorVisible(false);
            txtIdCliente.setKeyListener(null);
            txtIdCliente.setBackgroundColor(Color.TRANSPARENT);

            txtNombre.setText(cliente.getNombre());
            txtTelefono.setText(cliente.getTelefono());
            txtDireccion.setText(cliente.getDireccion());
        } catch (Error npe) {
            Toast.makeText(ClienteActivity.this, "Error, favor verificar: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Asignamos los nuevos valores a las variables del cliente
     */
    private boolean crearCliente() {
        try {
            accion = cliente.getAccion();
            email = txtEmail.getText().toString().trim();
            nombre = txtNombre.getText().toString().trim();
            telefono = txtTelefono.getText().toString().trim();
            idCliente = txtIdCliente.getText().toString().trim();
            direccion = txtDireccion.getText().toString().trim();
            estado = Integer.parseInt(spEstado.getItemAtPosition(spEstado.getSelectedItemPosition()).toString().trim().substring(0, 1));
            idLocalidad = Integer.parseInt(spLocalidad.getItemAtPosition(spLocalidad.getSelectedItemPosition()).toString().trim().substring(0, 1));
        } catch (Error e) {
            Toast.makeText(ClienteActivity.this, "Error, favor verificar: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return !idCliente.equals("") & !nombre.equals("") & !telefono.equals("") & !email.equals("") & !direccion.equals("");
    }

    /**
     * Guardar el nuevo cliente en db remota
     */
    private void guardarClienteRemoto() {
        // Validamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(ClienteActivity.this)) {
            // Verificamos que todos los datos del reporte esten ingresados
            if (crearCliente()) {
                Call<JsonObject> solicitudAccionCliente = ApiUtils.getApiServices().accionCliente(idCliente, idLocalidad, nombre, telefono, email, direccion, accion, estado);
                solicitudAccionCliente.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            //Verificamos si la transaccion fue exitosa, sino mostramos mensaje de error
                            if (!response.isSuccessful()) {
                                Toast.makeText(ClienteActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ClienteActivity.this, "Cliente guardado exitosamente!", Toast.LENGTH_SHORT).show();
                                //finalizamos esta activity
                                finish();
                            }
                        } catch (java.lang.Error e) {
                            Toast.makeText(ClienteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ClienteActivity.this, "La peticion falló:  " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(ClienteActivity.this, "Faltan datos del reporte!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ClienteActivity.this, "Sin conexión de red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Almacena cliente, sea nuevo o actualizado, por medio de variable accion se indica que se debe hacer
     */
    private void guardarClienteLocal() {
        try {
            if (crearCliente()) {
                int resultado = 0;
                objetoGson = new Gson();
                arrayCliente = new ArrayList<>();
                conexiones = new Conexiones(this);
                cliente.setEmail(email);
                cliente.setId(idCliente);
                cliente.setEstado(estado);
                cliente.setNombre(nombre);
                cliente.setTelefono(telefono);
                cliente.setDireccion(direccion);
                cliente.setFk_localidad(idLocalidad);
                arrayCliente.add(cliente);
                if (accion >= 1)
                    resultado = conexiones.accionesTablaCliente(accion, objetoGson.toJson(cliente));
                else
                    resultado = conexiones.accionesTablaCliente(accion, objetoGson.toJson(arrayCliente));

                if (resultado > 0)
                    Toast.makeText(ClienteActivity.this, "Cliente guardado correctamente!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ClienteActivity.this, "Error al guardar cliente!", Toast.LENGTH_LONG).show();
                //finalizamos esta activity
                finish();
            }
        } catch (NullPointerException npe) {
            Toast.makeText(ClienteActivity.this, "Error al guardar cliente:  " + npe.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Escoge de donde obtener los datos
     */
    private void getLocalidadLocalOremoto() {
        try {
            conexiones = new Conexiones(this);
            int modo = conexiones.getModoDeAlmacenamiento();
            if (modo == 0) getLocalidaLocal();
            else getLocalidadRemoto();
        } catch (NullPointerException npe) {
            Toast.makeText(ClienteActivity.this, "Error, favor verificar: " + npe.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Solicitamos los datos de localidades al servidor remoto
     */
    private void getLocalidadRemoto() {
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(this)) {
            Call<JsonArray> requestProductos = ApiUtils.getApiServices().getUnidades();
            requestProductos.enqueue(new Callback<JsonArray>() {
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (!response.isSuccessful()) {
                        // Mensaje de error
                        Toast.makeText(ClienteActivity.this, response.message() + " al cargar datos!", Toast.LENGTH_LONG).show();
                    } else {
                        // Manejar datos obtenidos en la petición
                        listaLocalidades = new ArrayList<>();
                        JsonArray arrayListLocalidades = response.body();
                        for (int j = 0; j < arrayListLocalidades.size(); j++) {
                            Localidad localidad = new Localidad();
                            localidad.setId(arrayListLocalidades.get(j).getAsJsonObject().get("id").getAsInt());
                            localidad.setLocalidad(arrayListLocalidades.get(j).getAsJsonObject().get("localidad").toString());
                            listaLocalidades.add(localidad);
                        }
                        llenarSpinnerLocalidades(listaLocalidades);
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(ClienteActivity.this, "La petición al servidor falló!" + t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(ClienteActivity.this, "Sin conexión de red en este momento!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Solicitamos los datos de localidades a db local
     */
    private void getLocalidaLocal() {
        try {
            conexiones = new Conexiones(this);
            List<Localidad> arrayListLocalidades = conexiones.getLocalidades();
            llenarSpinnerLocalidades(arrayListLocalidades);
        } catch (NullPointerException e) {
            Toast.makeText(ClienteActivity.this, "Error, favor verificar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Seteamos las localidades al spinner
     */
    private void llenarSpinnerLocalidades(List<Localidad> localidadList) {
        ArrayList<String> stringLocalidades = new ArrayList<>();
        try {
            listaLocalidades = localidadList;
            int posicion = 0;
            for (int k = 0; k < localidadList.size(); k++) {
                stringLocalidades.add(localidadList.get(k).getId() + "-" + localidadList.get(k).getLocalidad());
                if (localidadList.get(k).getId() == cliente.getFk_localidad()) {
                    posicion = k;
                }
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringLocalidades);
            this.spLocalidad.setAdapter(adapter);
            this.spLocalidad.setSelection(posicion);
        } catch (NullPointerException npe) {
            Toast.makeText(ClienteActivity.this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Seteamos los estados al spinner, preleccionando el que posee el cliente
     */
    private void llenarSpinnerEstado(int accion) {
        ArrayList<String> localidadList = new ArrayList<>();
        try {
            int posicion = 0;
            localidadList.add("0-Inactivo");
            localidadList.add("1-Activo");
            ArrayAdapter adapter = new ArrayAdapter(ClienteActivity.this, android.R.layout.simple_list_item_1, localidadList);

            if (accion > 0) {
                for (int i = 0; i < localidadList.size(); i++) {
                    if (localidadList.get(i).substring(0, 1).equals(String.valueOf(cliente.getEstado()))) {
                        posicion = i;
                    }
                }
                this.spEstado.setAdapter(adapter);
                this.spEstado.setSelection(posicion);
            } else {
                this.spEstado.setAdapter(adapter);
            }
        } catch (NullPointerException npe) {
            Toast.makeText(ClienteActivity.this, npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IncompatibleClassChangeError icche) {
            Toast.makeText(ClienteActivity.this, icche.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}