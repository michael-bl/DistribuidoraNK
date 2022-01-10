package com.example.distribuidorank.vista;

import android.annotation.SuppressLint;
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
import com.example.distribuidorank.modelo.Producto;
import com.example.distribuidorank.modelo.Unidad;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoActivity extends AppCompatActivity {
    private TextInputEditText txtIdProducto, txtDescripcion, txtPrecioCompra, txtPrecioVenta, txtUtilidad;
    private String descripcion, precio_compra, precio_venta, utilidad;
    private float precioCompra, precioVenta, nuevaUtilidad, resta;
    private int accion, estado, idUnidad, idProducto;
    private Button btnGuardar, btncalcularUtilidad;
    private ArrayList<Producto> arrayProducto;
    private Spinner spUnidades, spEstado;
    private DecimalFormat decimalFormat;
    private List<Unidad> listaUnidades;
    private Conexiones conexiones;
    private Producto producto;
    private Gson objetoGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        // instancia de textviews y otros componentes de la UI Producto
        instanciaComponentes();

        producto = new Producto();

        decimalFormat = new DecimalFormat("#.##");
        spUnidades = findViewById(R.id.spUnidadProducto);
        spEstado = findViewById(R.id.spEstadoProducto);

        // obtener datos del bundle
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            // Extrayendo el extra de tipo cadena
            producto = (Producto) bundle.getSerializable("producto");
            if (Objects.requireNonNull(producto).getAccion() != 0)
                mostrarDatosdelProducto(producto);
            else
                llenarSpinnerEstado(0); // 0 para setear orden por defecto
        }

        // Evento del boton que guarda el producto
        btnGuardar = findViewById(R.id.btnGuardarProducto);
        btnGuardar.setOnClickListener(v -> {
            int guardarLocalOremoto = conexiones.getModoDeAlmacenamiento();
            if (guardarLocalOremoto == 0) guardarProductoLocal();
            else guardarProductoRemoto();
        });

        // Evento del boton que calcula utilidad
        btncalcularUtilidad = findViewById(R.id.btnCalculaUtilidadProducto);
        btncalcularUtilidad.setOnClickListener(v -> calcularUtilidad());

        // Obtener unidades según indica la tabla local Modo
        getUnidadLocalOremoto();
    }

    /**
     * Inicializa los inputEditText de la interfaz producto
     */
    private void instanciaComponentes() {
        txtIdProducto = findViewById(R.id.txtIdProducto);
        txtDescripcion = findViewById(R.id.txtNombreProducto);
        txtUtilidad = findViewById(R.id.txtUtilidadProducto);
        txtPrecioCompra = findViewById(R.id.txtPassUsuario);
        txtPrecioVenta = findViewById(R.id.txtPrecioVenta);
    }

    /**
     * Realiza el calculo de nueva utilidad de un producto nuevo o editado
     */
    private void calcularUtilidad() {
        try {
            precioCompra = Float.parseFloat(txtPrecioCompra.getText().toString().trim());
            precioVenta = Float.parseFloat(txtPrecioVenta.getText().toString().trim());
            resta = precioVenta - precioCompra;
            nuevaUtilidad = (resta / precioCompra) * 100;
            String conFormato = decimalFormat.format(nuevaUtilidad);
            txtUtilidad.setText(conFormato);
        } catch (IllegalAccessError illegalAccessError) {
            Toast.makeText(this, illegalAccessError.getMessage(), Toast.LENGTH_LONG).show();
        } catch (EmptyStackException emptyStackException) {
            Toast.makeText(this, emptyStackException.getMessage(), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException numberFormatException) {
            Toast.makeText(this, numberFormatException.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Mostramos los datos del producto en los campos de la interfaz
     *
     * @param producto
     */
    @SuppressLint("SetTextI18n")
    private void mostrarDatosdelProducto(Producto producto) {
        try {
            txtIdProducto.setText(String.valueOf(producto.getId()));
            txtIdProducto.setFocusable(false);
            txtIdProducto.setEnabled(false);
            txtIdProducto.setCursorVisible(false);
            txtIdProducto.setKeyListener(null);
            txtIdProducto.setBackgroundColor(Color.TRANSPARENT);

            txtDescripcion.setText(producto.getDescripcion());
            txtPrecioCompra.setText(Float.toString(producto.getPrecio_compra()));
            txtPrecioVenta.setText(Float.toString(producto.getPrecio_venta()));
            txtUtilidad.setText(Float.toString(producto.getUtilidad()));
            llenarSpinnerEstado(1);
        } catch (Error nullPointerException) {
            Toast.makeText(ProductoActivity.this, nullPointerException.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Asigna los nuevos valores a las variables del objeto producto a guardar
     */
    private boolean crearProducto() {
        try {
            accion = producto.getAccion();
            utilidad = txtUtilidad.getText().toString().trim();
            descripcion = txtDescripcion.getText().toString().trim();
            precio_venta = txtPrecioVenta.getText().toString().trim();
            precio_compra = txtPrecioCompra.getText().toString().trim();
            idProducto = Integer.parseInt(txtIdProducto.getText().toString().trim());
            estado = Integer.parseInt(spEstado.getItemAtPosition(spEstado.getSelectedItemPosition()).toString().trim().substring(0, 1));
            idUnidad = Integer.parseInt(spUnidades.getItemAtPosition(spUnidades.getSelectedItemPosition()).toString().trim().substring(0, 1));
        } catch (Error e) {
            Toast.makeText(ProductoActivity.this, "Error, favor verificar: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return !utilidad.equals("") & !descripcion.equals("") & !precio_venta.equals("") & !precio_compra.equals("") & idUnidad != 0 & idProducto != 0;
    }

    /**
     * Almacena producto, por medio de la variable accion se indica si se  actualiza o uno nuevo
     */
    private void guardarProductoRemoto() {
        // Validamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(ProductoActivity.this)) {
            // Verificamos que todos los datos del reporte esten ingresados
            if (crearProducto()) {
                Call<JsonObject> solicitudAccionProducto = ApiUtils.getApiServices().accionProducto(idProducto, idUnidad, descripcion, utilidad, precio_compra, precio_venta, accion, estado);
                solicitudAccionProducto.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            //Verificamos si la transaccion fue exitosa y mostramos mensaje de error
                            if (!response.isSuccessful()) {
                                Toast.makeText(ProductoActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProductoActivity.this, "Producto guardado exitosamente!", Toast.LENGTH_SHORT).show();
                                //finalizamos la activity
                                finish();
                            }
                        } catch (java.lang.Error e) {
                            Toast.makeText(ProductoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ProductoActivity.this, "La peticion falló:  " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(ProductoActivity.this, "Faltan datos del reporte!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ProductoActivity.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Almacena cliente, sea nuevo o actualizado, por medio de variable accion se indica que se debe hacer
     */
    private void guardarProductoLocal() {
        try {
            if (crearProducto()) {
                int resultado = 0;
                objetoGson = new Gson();
                arrayProducto = new ArrayList<>();
                conexiones = new Conexiones(this);
                producto.setId(idProducto);
                producto.setEstado(estado);
                producto.setUtilidad(utilidad);
                producto.setFk_unidad(idUnidad);
                producto.setDescripcion(descripcion);
                producto.setPrecio_venta(precioVenta);
                producto.setPrecio_compra(precioCompra);
                producto.setEstado(Integer.parseInt(spEstado.getItemAtPosition(spEstado.getSelectedItemPosition()).toString().trim().substring(0, 1)));
                arrayProducto.add(producto);
                if (accion >= 1)
                    resultado = conexiones.crudProducto(accion, objetoGson.toJson(producto));
                else
                    resultado = conexiones.crudProducto(accion, objetoGson.toJson(arrayProducto));

                if (resultado > 0)
                    Toast.makeText(ProductoActivity.this, "Producto guardado correctamente!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ProductoActivity.this, "Error al guardar producto!", Toast.LENGTH_LONG).show();
                //finalizamos esta activity
                finish();
            }
        } catch (NullPointerException npe) {
            Toast.makeText(ProductoActivity.this, "Error al guardar producto:  " + npe.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Escoge de donde obtener los datos
     */
    private void getUnidadLocalOremoto() {
        try {
            conexiones = new Conexiones(this);
            int modo = conexiones.getModoDeAlmacenamiento();
            if (modo == 0) getUnidaLocal();
            else getUnidadRemoto();
        } catch (NullPointerException npe) {
            Toast.makeText(ProductoActivity.this, "Error, favor verificar: " + npe.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Solicitamos los datos de unidades al servidor remoto
     */
    private void getUnidadRemoto() {
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(this)) {
            Call<JsonArray> requestProductos = ApiUtils.getApiServices().getUnidades();
            requestProductos.enqueue(new Callback<JsonArray>() {
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (!response.isSuccessful()) {
                        // Mensaje de error
                        Toast.makeText(ProductoActivity.this, response.message() + " al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Manejar datos obtenidos en la petición
                        listaUnidades = new ArrayList<>();
                        JsonArray arrayListaProductos = response.body();
                        for (int j = 0; j < arrayListaProductos.size(); j++) {
                            Unidad unidad = new Unidad();
                            unidad.setId(arrayListaProductos.get(j).getAsJsonObject().get("idProducto").getAsInt());
                            unidad.setDetalle(arrayListaProductos.get(j).getAsJsonObject().get("detalle").toString());
                            listaUnidades.add(unidad);
                        }
                        llenarSpinnerUnidades(listaUnidades);
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(ProductoActivity.this, "La peticion falló!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ProductoActivity.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Solicitamos los datos de localidades a db local
     */
    private void getUnidaLocal() {
        try {
            conexiones = new Conexiones(this);
            List<Unidad> arrayListUnidades = conexiones.getUnidades();
            llenarSpinnerUnidades(arrayListUnidades);
        } catch (NullPointerException e) {
            Toast.makeText(ProductoActivity.this, "Error, favor verificar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Seteamos las unidades al spinner correspondiente
     *
     * @param unidadList
     */
    private void llenarSpinnerUnidades(List<Unidad> unidadList) {
        int posicion = 0;
        listaUnidades = unidadList;
        ArrayAdapter adapterUnidades;
        ArrayList<String> stringUnidades = new ArrayList<>();
        try {
            for (int k = 0; k < unidadList.size(); k++) {
                stringUnidades.add(unidadList.get(k).getId() + "-" + unidadList.get(k).getDetalle());
                if (unidadList.get(k).getId() == producto.getFk_familia()) {
                    posicion = k;
                }
            }
            adapterUnidades = new ArrayAdapter(ProductoActivity.this, android.R.layout.simple_list_item_1, stringUnidades);
            if (producto.getAccion() > 0) {
                this.spUnidades.setAdapter(adapterUnidades);
                this.spUnidades.setSelection(posicion);
            } else {
                this.spUnidades.setAdapter(adapterUnidades);
            }
        } catch (NullPointerException nullPointerException) {
            Toast.makeText(ProductoActivity.this, nullPointerException.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Seteamos los estados al spinner, preleccionando el que posee el producto
     *
     * @param accion
     */
    private void llenarSpinnerEstado(int accion) {
        ArrayList<String> localidadList = new ArrayList<>();
        try {
            int posicion = 0;
            localidadList.add("0-Inactivo");
            localidadList.add("1-Activo");
            ArrayAdapter adapter = new ArrayAdapter(ProductoActivity.this, android.R.layout.simple_list_item_1, localidadList);

            if (accion > 0) {
                for (int i = 0; i < localidadList.size(); i++) {
                    if (localidadList.get(i).substring(0, 1).equals(String.valueOf(producto.getEstado()))) {
                        posicion = i;
                    }
                }
                this.spEstado.setAdapter(adapter);
                this.spEstado.setSelection(posicion);
            } else {
                this.spEstado.setAdapter(adapter);
            }
        } catch (NullPointerException npe) {
            Toast.makeText(ProductoActivity.this, npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IncompatibleClassChangeError icche) {
            Toast.makeText(ProductoActivity.this, icche.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}