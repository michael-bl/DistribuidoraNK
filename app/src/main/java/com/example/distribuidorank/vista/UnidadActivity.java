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
import com.example.distribuidorank.modelo.Unidad;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnidadActivity extends AppCompatActivity {
    private TextInputEditText txtIdUnidad, txtDetalleUnidad;
    private ArrayList<Unidad> arrayUnidad;
    private int accion, estado, idUnidad;
    private Conexiones conexiones;
    private String detalleUnidad;
    private Spinner spEstado;
    private Gson objetoGson;
    private Unidad unidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidad);

        instanciaComponentes();

        unidad = new Unidad();

        spEstado = findViewById(R.id.spEstadoUnidad);

        // Obtener datos del bundle
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            // Extrayendo el extra de tipo cadena
            unidad = (Unidad) bundle.getSerializable("unidad");
            if (Objects.requireNonNull(unidad).getAccion() != 0)
                mostrarDatosdeUnidadEnLaUi(unidad);
            else
                llenarSpinnerEstado(0); // 0 para setear orden por defecto
        }

        Button btnGuardar = findViewById(R.id.btnGuardarUnidad);
        btnGuardar.setOnClickListener(v -> {
            conexiones = new Conexiones(this);
            int guardarLocalOremoto = conexiones.getModoDeAlmacenamiento();
            if (guardarLocalOremoto == 0) guardarUnidadEnDbLocal();
            else guardarUnidadEnDbRemoto();
        });
    }

    private void instanciaComponentes() {
        txtIdUnidad = findViewById(R.id.txtIdUnidad);
        txtDetalleUnidad = findViewById(R.id.txtDetalleUnidad);
    }

    private void mostrarDatosdeUnidadEnLaUi(Unidad unidad) {
        try {
            llenarSpinnerEstado(1);

            txtIdUnidad.setText(String.valueOf(unidad.getId()));
            txtIdUnidad.setFocusable(false);
            txtIdUnidad.setEnabled(false);
            txtIdUnidad.setCursorVisible(false);
            txtIdUnidad.setKeyListener(null);
            txtIdUnidad.setBackgroundColor(Color.TRANSPARENT);

            txtDetalleUnidad.setText(unidad.getDetalle());
        } catch (Error npe) {
            Toast.makeText(UnidadActivity.this, "Error, favor verificar: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Resources.NotFoundException nfe){
            Toast.makeText(UnidadActivity.this, "Error, favor verificar: " + nfe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean llenarVariablesDeUnidad() {
        try {
            accion = unidad.getAccion();
            idUnidad = Integer.parseInt(txtIdUnidad.getText().toString().trim());
            detalleUnidad = txtDetalleUnidad.getText().toString().trim();
            estado = Integer.parseInt(spEstado.getItemAtPosition(spEstado.getSelectedItemPosition()).toString().trim().substring(0, 1));
        } catch (Error e) {
            Toast.makeText(UnidadActivity.this, "Error, favor verificar: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return idUnidad > 0 & !detalleUnidad.equals("");
    }

    private void guardarUnidadEnDbRemoto() {
        ConnectivityService estaConectado = new ConnectivityService();
        if (estaConectado.stateConnection(UnidadActivity.this)) {
            if (llenarVariablesDeUnidad()) {
                // el id de la unidad puede ir null, para que mysql haga la magia y asigne el consecutivo
                Call<JsonObject> solicitudAccionLocalidad = ApiUtils.getApiServices().accionLocalidad(idUnidad, detalleUnidad, accion, estado);
                solicitudAccionLocalidad.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            //Verificamos si la transaccion fue exitosa, sino mostramos mensaje de error
                            if (!response.isSuccessful()) {
                                Toast.makeText(UnidadActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UnidadActivity.this, "Localidad guardado exitosamente!", Toast.LENGTH_SHORT).show();
                                //finalizamos esta activity
                                finish();
                            }
                        } catch (java.lang.Error e) {
                            Toast.makeText(UnidadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(UnidadActivity.this, "La peticion falló:  " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(UnidadActivity.this, "Faltan datos del reporte!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(UnidadActivity.this, "Sin conexión de red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarUnidadEnDbLocal() {
        try {
            if (llenarVariablesDeUnidad()) {
                int resultado;
                objetoGson = new Gson();
                arrayUnidad = new ArrayList<>();
                conexiones = new Conexiones(this);
                unidad.setDetalle(detalleUnidad);
                unidad.setEstado(estado);
                unidad.setId(idUnidad);
                arrayUnidad.add(unidad);
                if (accion >= 1)
                    resultado = conexiones.crudUnidad(accion, objetoGson.toJson(unidad));
                else
                    resultado = conexiones.crudUnidad(accion, objetoGson.toJson(arrayUnidad));

                if (resultado > 0)
                    Toast.makeText(UnidadActivity.this, "Unidad guardado correctamente!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(UnidadActivity.this, "Error al guardar unidad!", Toast.LENGTH_LONG).show();
                //finalizamos esta activity
                finish();
            }
        } catch (NullPointerException npe) {
            Toast.makeText(UnidadActivity.this, "Error al guardar unidad:  " + npe.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void llenarSpinnerEstado(int accion) {
        ArrayList<String> listEstado = new ArrayList<>();
        try {
            int posicion = 0;
            listEstado.add("0-Inactivo");
            listEstado.add("1-Activo");
            ArrayAdapter adapter = new ArrayAdapter(UnidadActivity.this, android.R.layout.simple_list_item_1, listEstado);

            if (accion > 0) {
                for (int i = 0; i < listEstado.size(); i++) {
                    if (listEstado.get(i).substring(0, 1).equals(String.valueOf(unidad.getEstado()))) {
                        posicion = i;
                    }
                }
                this.spEstado.setAdapter(adapter);
                this.spEstado.setSelection(posicion);
            } else {
                this.spEstado.setAdapter(adapter);
            }
        } catch (NullPointerException npe) {
            Toast.makeText(UnidadActivity.this, npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IncompatibleClassChangeError icche) {
            Toast.makeText(UnidadActivity.this, icche.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}