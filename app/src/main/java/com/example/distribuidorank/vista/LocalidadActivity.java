package com.example.distribuidorank.vista;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distribuidorank.Database.Conexiones;
import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.ApiUtils;
import com.example.distribuidorank.controlador.ConnectivityService;
import com.example.distribuidorank.modelo.Localidad;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocalidadActivity extends AppCompatActivity {
    private TextInputEditText txtIdLocalidad, txtLocalidad;
    private ArrayList<Localidad> arrayLocalidad;
    private int accion, estado, idLocalidad;
    private String stringLocalidad;
    private Conexiones conexiones;
    private Localidad localidad;
    private Spinner spinnerEstado;
    private Gson objetoGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localidad);

        instanciaComponentes();

        localidad = new Localidad();

        spinnerEstado = findViewById(R.id.spEstadoLocalidad);

        // Obtener datos del bundle
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            // Extrayendo el extra de tipo cadena
            localidad = (Localidad) bundle.getSerializable("localidad");
            if (Objects.requireNonNull(localidad).getAccion() != 0)
                mostrarDatosdeLocalidadEnLaUi(localidad);
            else
                llenarSpinnerEstado(0); // 0 para setear orden por defecto
        }

        Button btnGuardar = findViewById(R.id.btnGuardarLocalidad);
        btnGuardar.setOnClickListener(v -> {
            conexiones = new Conexiones(this);
            int guardarLocalOremoto = conexiones.getModoDeAlmacenamiento();
            if (guardarLocalOremoto == 0) guardarLocalidadEnDbLocal();
            else guardarLocalidadEnDbRemoto();
        });
    }

    private void instanciaComponentes() {
        txtIdLocalidad = findViewById(R.id.txtIdLocalidad);
        txtLocalidad = findViewById(R.id.txtLocalidad);
    }

    private void mostrarDatosdeLocalidadEnLaUi(Localidad localidad) {
        try {
            llenarSpinnerEstado(1);

            txtIdLocalidad.setText(String.valueOf(localidad.getId()));
            txtIdLocalidad.setFocusable(false);
            txtIdLocalidad.setEnabled(false);
            txtIdLocalidad.setCursorVisible(false);
            txtIdLocalidad.setKeyListener(null);
            txtIdLocalidad.setBackgroundColor(Color.TRANSPARENT);

            txtLocalidad.setText(localidad.getLocalidad());
        } catch (Error npe) {
            Toast.makeText(LocalidadActivity.this, "Error, favor verificar: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Resources.NotFoundException nfe){
            Toast.makeText(LocalidadActivity.this, "Error, favor verificar: " + nfe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean llenarVariablesDeLocalidad() {
        try {
            accion = localidad.getAccion();
            idLocalidad = Integer.parseInt(txtIdLocalidad.getText().toString().trim());
            stringLocalidad = txtLocalidad.getText().toString().trim();
            estado = Integer.parseInt(spinnerEstado.getItemAtPosition(spinnerEstado.getSelectedItemPosition()).toString().trim().substring(0, 1));
        } catch (Error e) {
            Toast.makeText(LocalidadActivity.this, "Error, favor verificar: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return idLocalidad > 0 & !stringLocalidad.equals("");
    }

    private void guardarLocalidadEnDbRemoto() {
        ConnectivityService estaConectado = new ConnectivityService();
        if (estaConectado.stateConnection(LocalidadActivity.this)) {
            if (llenarVariablesDeLocalidad()) {
                // el id de la localidad puede ir null, para que mysql haga la magia y asigne el consecutivo
                Call<JsonObject> solicitudAccionLocalidad = ApiUtils.getApiServices().accionLocalidad(idLocalidad, stringLocalidad, accion, estado);
                solicitudAccionLocalidad.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            //Verificamos si la transaccion fue exitosa, sino mostramos mensaje de error
                            if (!response.isSuccessful()) {
                                Toast.makeText(LocalidadActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LocalidadActivity.this, "Localidad guardado exitosamente!", Toast.LENGTH_SHORT).show();
                                //finalizamos esta activity
                                finish();
                            }
                        } catch (java.lang.Error e) {
                            Toast.makeText(LocalidadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(LocalidadActivity.this, "La peticion falló:  " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(LocalidadActivity.this, "Faltan datos del reporte!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LocalidadActivity.this, "Sin conexión de red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarLocalidadEnDbLocal() {
        try {
            if (llenarVariablesDeLocalidad()) {
                int resultado;
                objetoGson = new Gson();
                arrayLocalidad = new ArrayList<>();
                conexiones = new Conexiones(this);
                localidad.setId(idLocalidad);
                localidad.setEstado(estado);
                localidad.setLocalidad(stringLocalidad);
                arrayLocalidad.add(localidad);
                if (accion >= 1)
                    resultado = conexiones.crudLocalidad(accion, objetoGson.toJson(localidad));
                else
                    resultado = conexiones.crudLocalidad(accion, objetoGson.toJson(arrayLocalidad));

                if (resultado > 0)
                    Toast.makeText(LocalidadActivity.this, "Localidad guardado correctamente!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(LocalidadActivity.this, "Error al guardar localidad!", Toast.LENGTH_LONG).show();
                //finalizamos esta activity
                finish();
            }
        } catch (NullPointerException npe) {
            Toast.makeText(LocalidadActivity.this, "Error al guardar localidad:  " + npe.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarSpinnerEstado(int accion) {
        ArrayList<String> listEstado = new ArrayList<>();
        try {
            int posicion = 0;
            listEstado.add("0-Inactivo");
            listEstado.add("1-Activo");
            ArrayAdapter adapter = new ArrayAdapter(LocalidadActivity.this, android.R.layout.simple_list_item_1, listEstado);

            if (accion > 0) {
                for (int i = 0; i < listEstado.size(); i++) {
                    if (listEstado.get(i).substring(0, 1).equals(String.valueOf(localidad.getEstado()))) {
                        posicion = i;
                    }
                }
                this.spinnerEstado.setAdapter(adapter);
                this.spinnerEstado.setSelection(posicion);
            } else {
                this.spinnerEstado.setAdapter(adapter);
            }
        } catch (NullPointerException npe) {
            Toast.makeText(LocalidadActivity.this, npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IncompatibleClassChangeError icche) {
            Toast.makeText(LocalidadActivity.this, icche.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}