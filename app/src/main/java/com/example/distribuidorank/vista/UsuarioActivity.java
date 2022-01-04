package com.example.distribuidorank.vista;

import android.annotation.SuppressLint;
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
import com.example.distribuidorank.controlador.HashPass;
import com.example.distribuidorank.modelo.Localidad;
import com.example.distribuidorank.modelo.Usuario;
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

public class UsuarioActivity extends AppCompatActivity {
    private TextInputEditText txtIdUsuario, txtNombre, txtPass,
            txtTelefono, txtEmail, txtDireccion;
    private String idUsuario, nombre, pass, telefono, email, direccion;
    private List<Localidad> listaLocalidades;
    private int accion, estado, idLocalidad;
    private Spinner spLocalidad, spEstado;
    private Conexiones conexiones;
    private Gson objetoGson;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instancia de textviews y otros componentes de la UI
        instanciaComponentes();

        usuario = new Usuario();

        spEstado = findViewById(R.id.spEstadoUsuario);
        spLocalidad = findViewById(R.id.spLocalidadUsuario);

        // Obtener datos del bundle
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            // Extrayendo el extra de tipo cadena
            usuario = (Usuario) bundle.getSerializable("usuario");
            if (Objects.requireNonNull(usuario).getAccion() != 0) {
                mostrarDatosdelUsuario(usuario);
            } else {
                llenarSpinnerEstado(0); // 0 para setear orden por defecto
            }
        }

        Button btnGuardar = findViewById(R.id.btnGuardarUsuario);
        btnGuardar.setOnClickListener(v -> {
            int guardarLocalOremoto = conexiones.getModoDeAlmacenamiento();
            if (guardarLocalOremoto == 0) guardarUsuarioLocal();
            else guardarUsuarioRemoto();
        });

        // Solicitamos localidades
        getLocalidadLocalOremoto();
    }

    /**
     * Asigna los nuevos valores a las variables del objeto usuario a guardar
     */
    private void instanciaComponentes() {
        txtDireccion = findViewById(R.id.txtDireccionCliente);
        txtTelefono = findViewById(R.id.txtTelefonoCliente);
        txtIdUsuario = findViewById(R.id.txtCedulaUsuario);
        txtNombre = findViewById(R.id.txtNombreCliente);
        txtEmail = findViewById(R.id.txtEmailCliente);
        txtPass = findViewById(R.id.txtPassUsuario);
    }

    /**
     * Mostramos los datos del usuario en los campos de la interfaz
     */
    @SuppressLint("SetTextI18n")
    private void mostrarDatosdelUsuario(Usuario usuario) {
        try {
            txtIdUsuario.setText(usuario.getId());
            txtIdUsuario.setFocusable(false);
            txtIdUsuario.setEnabled(false);
            txtIdUsuario.setCursorVisible(false);
            txtIdUsuario.setKeyListener(null);
            txtIdUsuario.setBackgroundColor(Color.TRANSPARENT);

            txtNombre.setText(usuario.getNombre());

            txtPass.setText(usuario.getPass());
            txtPass.setFocusable(false);
            txtPass.setEnabled(false);
            txtPass.setCursorVisible(false);
            txtPass.setKeyListener(null);
            txtPass.setBackgroundColor(Color.TRANSPARENT);

            txtDireccion.setText(usuario.getDireccion());
            txtTelefono.setText(usuario.getTelefono());
            txtEmail.setText(usuario.getEmail());
            llenarSpinnerEstado(1);
        } catch (Error npe) {
            Toast.makeText(UsuarioActivity.this, "Error, favor verificar: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Asignamos los nuevos valores a las variables del usuario
     */
    private boolean crearUsuario() {
        try {
            HashPass hashPass = new HashPass();
            accion = usuario.getAccion();
            pass = txtPass.getText().toString().trim();
            email = txtEmail.getText().toString().trim();
            nombre = txtNombre.getText().toString().trim();
            telefono = txtTelefono.getText().toString().trim();
            idUsuario = txtIdUsuario.getText().toString().trim();
            direccion = txtDireccion.getText().toString().trim();
            if (accion == 0 & !pass.equals("")) {
                pass = hashPass.convertirSHA256(pass);
            } else {
                pass = usuario.getPass();
            }
            estado = Integer.parseInt(spEstado.getItemAtPosition(spEstado.getSelectedItemPosition()).toString().trim().substring(0, 1));
            idLocalidad = Integer.parseInt(spLocalidad.getItemAtPosition(spLocalidad.getSelectedItemPosition()).toString().trim().substring(0, 1));
        } catch (Error e) {
            Toast.makeText(UsuarioActivity.this, "Error, favor verificar: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        return !idUsuario.equals("") & !nombre.equals("") & !pass.equals("") & !telefono.equals("") & !email.equals("") & !direccion.equals("");
    }

    /**
     * Almacena accion sobre producto, sea nuevo o actualizado, por medio de variable accion se indica que se desa hacer
     */
    private void guardarUsuarioRemoto() {
        // Validamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(UsuarioActivity.this)) {
            // Verificamos que todos los datos del reporte esten ingresados
            if (crearUsuario()) {
                Call<JsonObject> solicitudAccionProducto = ApiUtils.getApiServices().accionUsuario(idUsuario, idLocalidad, nombre, pass, telefono, email, direccion, accion, estado);
                solicitudAccionProducto.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            //Verificamos si la transaccion fue exitosa, sino mostramos mensaje de error
                            if (!response.isSuccessful()) {
                                Toast.makeText(UsuarioActivity.this, response.message(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(UsuarioActivity.this, "Usuario guardado exitosamente!", Toast.LENGTH_LONG).show();
                                //finalizamos esta activity
                                finish();
                            }
                        } catch (java.lang.Error e) {
                            Toast.makeText(UsuarioActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(UsuarioActivity.this, "La petición falló!:  " + t.toString(), Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(UsuarioActivity.this, "Faltan datos del reporte!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(UsuarioActivity.this, "Sin conexión de red en este momento!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Almacena usuario, sea nuevo o actualizado, por medio de variable accion se indica que se debe hacer
     */
    private void guardarUsuarioLocal() {
        try {
            if (crearUsuario()) {
                objetoGson = new Gson();
                conexiones = new Conexiones(this);
                usuario.setId(idUsuario);
                usuario.setFk_localidad(idLocalidad);
                usuario.setNombre(nombre);
                usuario.setPass(pass);
                usuario.setEmail(email);
                usuario.setTelefono(telefono);
                usuario.setDireccion(direccion);
                usuario.setEstado(estado);
                if (conexiones.accionesTablaUsuario(accion, objetoGson.toJson(usuario)) == 0)
                    Toast.makeText(UsuarioActivity.this, "Error al guardar usuario!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(UsuarioActivity.this, "Usuario guardado correctamente!", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (NullPointerException npe) {
            Toast.makeText(UsuarioActivity.this, "Error al guardar usuario:  " + npe.toString(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(UsuarioActivity.this, "Error, favor verificar: " + npe.toString(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(UsuarioActivity.this, response.message() + " al cargar datos!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(UsuarioActivity.this, "La petición al servidor falló!" + t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(UsuarioActivity.this, "Sin conexión de red en este momento!", Toast.LENGTH_LONG).show();
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
            Toast.makeText(UsuarioActivity.this, "Error, favor verificar: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                if (localidadList.get(k).getId() == usuario.getFk_localidad()) {
                    posicion = k;
                }
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringLocalidades);
            this.spLocalidad.setAdapter(adapter);
            this.spLocalidad.setSelection(posicion);
        } catch (NullPointerException npe) {
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Seteamos los estados al spinner, preleccionando el que posee el usuario
     */
    private void llenarSpinnerEstado(int accion) {
        ArrayList<String> localidadList = new ArrayList<>();
        try {
            int posicion = 0;
            localidadList.add("0-Inactivo");
            localidadList.add("1-Activo");
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, localidadList);

            if (accion > 0) {
                for (int i = 0; i < localidadList.size(); i++) {
                    if (localidadList.get(i).substring(0, 1).equals(String.valueOf(usuario.getEstado()))) {
                        posicion = i;
                    }
                }
                this.spEstado.setAdapter(adapter);
                this.spEstado.setSelection(posicion);
            } else {
                this.spEstado.setAdapter(adapter);
            }
        } catch (NullPointerException npe) {
            Toast.makeText(this, npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IncompatibleClassChangeError icche) {
            Toast.makeText(this, icche.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}