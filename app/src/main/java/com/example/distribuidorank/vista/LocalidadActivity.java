package com.example.distribuidorank.vista;

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
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocalidadActivity extends AppCompatActivity {
    private TextInputEditText txtIdLocalidad, txtLocalidad;
    private ArrayList<Localidad> arrayLocalidad;
    private List<Localidad> listaLocalidades;
    private int accion, estado, idLocalidad;
    private String stringLocalidad;
    private Conexiones conexiones;
    private Localidad localidad;
    private Spinner spEstado;
    private Gson objetoGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localidad);

        // Instancia de textviews y otros componentes de la UI
        instanciaComponentes();

        localidad = new Localidad();

        spEstado = findViewById(R.id.spEstadoLocalidad);

        // Obtener datos del bundle
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            // Extrayendo el extra de tipo cadena
            localidad = (Localidad) bundle.getSerializable("localidad");
            if (Objects.requireNonNull(localidad).getAccion() != 0)
                mostrarDatosdeLocalidad(localidad);
            else
                llenarSpinnerEstado(0); // 0 para setear orden por defecto
        }

        Button btnGuardar = findViewById(R.id.btnGuardarLoacalidad);
        btnGuardar.setOnClickListener(v -> {
            conexiones = new Conexiones(this);
            int guardarLocalOremoto = conexiones.getModoDeAlmacenamiento();
            if (guardarLocalOremoto == 0) guardarLocalidadLocal();
            else guardarLocalidadRemoto();
        });
    }

    /**
     * Inicializa los inputEditText de la interfaz localidad
     */
    private void instanciaComponentes() {
        txtIdLocalidad = findViewById(R.id.txtIdLocalidad);
        txtLocalidad = findViewById(R.id.txtLocalidad);
    }

    /**
     * Mostramos los datos del localidad en los campos de la interfaz
     */
    private void mostrarDatosdeLocalidad(Localidad localidad) {
        try {
            llenarSpinnerEstado(1);
            txtIdLocalidad.setText(localidad.getId());
            txtIdLocalidad.setFocusable(false);
            txtIdLocalidad.setEnabled(false);
            txtIdLocalidad.setCursorVisible(false);
            txtIdLocalidad.setKeyListener(null);
            txtIdLocalidad.setBackgroundColor(Color.TRANSPARENT);

            txtLocalidad.setText(localidad.getLocalidad());
        } catch (Error npe) {
            Toast.makeText(LocalidadActivity.this, "Error, favor verificar: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Asignamos los nuevos valores a las variables del localidad
     */
    private boolean crearLocalidad() {
        try {
            accion = localidad.getAccion();
            idLocalidad = Integer.parseInt(txtIdLocalidad.toString());
            stringLocalidad = txtLocalidad.getText().toString().trim();
            estado = Integer.parseInt(spEstado.getItemAtPosition(spEstado.getSelectedItemPosition()).toString().trim().substring(0, 1));
        } catch (Error e) {
            Toast.makeText(LocalidadActivity.this, "Error, favor verificar: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return idLocalidad > 0 & !stringLocalidad.equals("");
    }

    /**
     * Guardar la nueva localidad en db remota
     */
    private void guardarLocalidadRemoto() {
        // Validamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(LocalidadActivity.this)) {
            // Verificamos que todos los datos del reporte esten ingresados
            if (crearLocalidad()) {
                Call<JsonObject> solicitudAccionLocalidad = ApiUtils.getApiServices().accionLocalidad(stringLocalidad, idLocalidad, accion, estado);
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

    /**
     * Almacena localidad, sea nuevo o actualizado, por medio de variable accion se indica que se debe hacer
     */
    private void guardarLocalidadLocal() {
        try {
            if (crearLocalidad()) {
                int resultado = 0;
                objetoGson = new Gson();
                arrayLocalidad = new ArrayList<>();
                conexiones = new Conexiones(this);
                localidad.setId(idLocalidad);
                localidad.setEstado(estado);
                localidad.setLocalidad(stringLocalidad);
                arrayLocalidad.add(localidad);
                if (accion >= 1)
                    resultado = conexiones.accionesTablaLocalidad(accion, objetoGson.toJson(localidad));
                else
                    resultado = conexiones.accionesTablaLocalidad(accion, objetoGson.toJson(arrayLocalidad));

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

    /**
     * Seteamos los estados al spinner, preleccionando el que posee el localidad
     */
    private void llenarSpinnerEstado(int accion) {
        ArrayList<String> localidadList = new ArrayList<>();
        try {
            int posicion = 0;
            localidadList.add("0-Inactivo");
            localidadList.add("1-Activo");
            ArrayAdapter adapter = new ArrayAdapter(LocalidadActivity.this, android.R.layout.simple_list_item_1, localidadList);

            if (accion > 0) {
                for (int i = 0; i < localidadList.size(); i++) {
                    if (localidadList.get(i).substring(0, 1).equals(String.valueOf(localidad.getEstado()))) {
                        posicion = i;
                    }
                }
                this.spEstado.setAdapter(adapter);
                this.spEstado.setSelection(posicion);
            } else {
                this.spEstado.setAdapter(adapter);
            }
        } catch (NullPointerException npe) {
            Toast.makeText(LocalidadActivity.this, npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IncompatibleClassChangeError icche) {
            Toast.makeText(LocalidadActivity.this, icche.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}