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
import com.example.distribuidorank.modelo.Proveedor;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProveedorActivity extends AppCompatActivity {
    private TextInputEditText txtIdProveedor, txtNombre, txtTelefono, txtEmail;
    private String idProveedor, nombre, telefono, email;
    private ArrayList<Proveedor> arrayProveedor;
    private Conexiones conexiones;
    private Proveedor proveedor;
    private int accion, estado;
    private Spinner spEstado;
    private Gson objetoGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instancia de textviews y otros componentes de la UI
        instanciaComponentes();

        proveedor = new Proveedor();

        spEstado = findViewById(R.id.spEstadoProveedor);

        // Obtener datos del bundle
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            // Extrayendo el extra de tipo cadena
            proveedor = (Proveedor) bundle.getSerializable("proveedor");
            if (Objects.requireNonNull(proveedor).getAccion() != 0)
                mostrarDatosdelProveedor(proveedor);
            else
                llenarSpinnerEstado(0); // 0 para setear orden por defecto
        }

        Button btnGuardar = findViewById(R.id.btnGuardarProveedor);
        btnGuardar.setOnClickListener(v -> {
            conexiones = new Conexiones(this);
            int guardarLocalOremoto = conexiones.getModoDeAlmacenamiento();
            if (guardarLocalOremoto == 0) guardarProveedorLocal();
            else guardarProveedorRemoto();
        });
    }

    /**
     * Inicializa los inputEditText de la interfaz proveedor
     */
    private void instanciaComponentes() {
        txtTelefono = findViewById(R.id.txtTelefonoProveedor);
        txtIdProveedor = findViewById(R.id.txtCedulaProveedor);
        txtNombre = findViewById(R.id.txtNombreProveedor);
        txtEmail = findViewById(R.id.txtEmailProveedor);
    }

    /**
     * Mostramos los datos del proveedor en los campos de la interfaz
     */
    private void mostrarDatosdelProveedor(Proveedor proveedor) {
        try {
            llenarSpinnerEstado(1);
            txtEmail.setText(proveedor.getEmail());
            txtIdProveedor.setText(proveedor.getId());
            txtIdProveedor.setFocusable(false);
            txtIdProveedor.setEnabled(false);
            txtIdProveedor.setCursorVisible(false);
            txtIdProveedor.setKeyListener(null);
            txtIdProveedor.setBackgroundColor(Color.TRANSPARENT);

            txtNombre.setText(proveedor.getNombre());
            txtTelefono.setText(proveedor.getTelefono());
        } catch (Error npe) {
            Toast.makeText(ProveedorActivity.this, "Error, favor verificar: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Asignamos los nuevos valores a las variables del proveedor
     */
    private boolean crearProveedor() {
        try {
            accion = proveedor.getAccion();
            email = txtEmail.getText().toString().trim();
            nombre = txtNombre.getText().toString().trim();
            telefono = txtTelefono.getText().toString().trim();
            idProveedor = txtIdProveedor.getText().toString().trim();
            estado = Integer.parseInt(spEstado.getItemAtPosition(spEstado.getSelectedItemPosition()).toString().trim().substring(0, 1));
        } catch (Error e) {
            Toast.makeText(ProveedorActivity.this, "Error, favor verificar: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return !idProveedor.equals("") & !nombre.equals("") & !telefono.equals("") & !email.equals("");
    }

    /**
     * Guardar el nuevo proveedor
     */
    private void guardarProveedorRemoto() {
        // Validamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(ProveedorActivity.this)) {
            // Verificamos que todos los datos del reporte esten ingresados
            if (crearProveedor()) {
                Call<JsonObject> solicitudAccionProveedor = ApiUtils.getApiServices().accionProveedor(idProveedor, nombre, telefono, email, accion, estado);
                solicitudAccionProveedor.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            //Verificamos si la transaccion fue exitosa, sino mostramos mensaje de error
                            if (!response.isSuccessful()) {
                                Toast.makeText(ProveedorActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProveedorActivity.this, "Proveedor guardado exitosamente!", Toast.LENGTH_SHORT).show();
                                //finalizamos esta activity
                                finish();
                            }
                        } catch (java.lang.Error e) {
                            Toast.makeText(ProveedorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ProveedorActivity.this, "La peticion falló:  " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(ProveedorActivity.this, "Faltan datos del reporte!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ProveedorActivity.this, "Sin conexión de red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Almacena proveedor, sea nuevo o actualizado, por medio de variable accion se indica que se debe hacer
     */
    private void guardarProveedorLocal() {
        try {
            if (crearProveedor()) {
                int resultado = 0;
                objetoGson = new Gson();
                arrayProveedor = new ArrayList<>();
                conexiones = new Conexiones(this);
                proveedor.setEmail(email);
                proveedor.setId(idProveedor);
                proveedor.setEstado(estado);
                proveedor.setNombre(nombre);
                proveedor.setTelefono(telefono);
                arrayProveedor.add(proveedor);
                if (accion >= 1)
                    resultado = conexiones.accionesTablaCliente(accion, objetoGson.toJson(proveedor));
                else
                    resultado = conexiones.accionesTablaCliente(accion, objetoGson.toJson(arrayProveedor));

                if (resultado > 0)
                    Toast.makeText(ProveedorActivity.this, "Proveedor guardado correctamente!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ProveedorActivity.this, "Error al guardar proveedor!", Toast.LENGTH_LONG).show();
                //finalizamos esta activity
                finish();
            }
        } catch (NullPointerException npe) {
            Toast.makeText(ProveedorActivity.this, "Error al guardar proveedor:  " + npe.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Seteamos los estados al spinner, preleccionando el que posee el proveedor
     */
    private void llenarSpinnerEstado(int accion) {
        ArrayList<String> localidadList = new ArrayList<>();
        try {
            int posicion = 0;
            localidadList.add("0-Inactivo");
            localidadList.add("1-Activo");
            ArrayAdapter adapter = new ArrayAdapter(ProveedorActivity.this, android.R.layout.simple_list_item_1, localidadList);

            if (accion > 0) {
                for (int i = 0; i < localidadList.size(); i++) {
                    if (localidadList.get(i).substring(0, 1).equals(String.valueOf(proveedor.getEstado()))) {
                        posicion = i;
                    }
                }
                this.spEstado.setAdapter(adapter);
                this.spEstado.setSelection(posicion);
            } else {
                this.spEstado.setAdapter(adapter);
            }
        } catch (NullPointerException npe) {
            Toast.makeText(ProveedorActivity.this, npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IncompatibleClassChangeError icche) {
            Toast.makeText(ProveedorActivity.this, icche.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}