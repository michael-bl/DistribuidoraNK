package com.example.distribuidorank.vista;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.ApiUtils;
import com.example.distribuidorank.controlador.ConnectivityService;
import com.example.distribuidorank.modelo.Producto;
import com.example.distribuidorank.modelo.Unidad;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoActivity extends AppCompatActivity {
    // Variables de un producto
    private String idUnidad, unidad, descripcion, precio_compra, precio_venta, utilidad;
    // Variables de los textviews
    private TextInputEditText txtIdProducto, txtDescripcion, txtPrecioCompra,
            txtPrecioVenta, txtUtilidad;
    //Spinner unidades
    private Spinner spUnidades;
    //Boton calcular utilidad
    Button btnAceptar, btncalcularUtilidad;
    // Objeto producto
    private Producto producto;
    private int accion, estado, id;
    private float precioCompra, precioVenta, nuevaUtilidad, resta;
    private List<Unidad> listaUnidades;
    DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        // instancia de textviews y otros componentes de la UI
        instanciaComponentes();
        // instancia nuevo producto
        producto = new Producto();
        //Decimal format
        decimalFormat = new DecimalFormat("#.##");
        spUnidades = findViewById(R.id.spUnidadProducto);

        // obtener datos del bundle
        Bundle bundle = this.getIntent().getExtras();
        if(bundle !=null){
            //Extrayendo el extra de tipo cadena
            producto = (Producto) bundle.getSerializable("producto");
            if (producto.getAccion()==0){
            } else {
                mostrarDatosdelProducto(producto);
            }
        }
        // evento del boton btnAceptarProducto
        btnAceptar = findViewById(R.id.btnAceptarProducto);
        btnAceptar.setOnClickListener(v -> guardarProducto());

        btncalcularUtilidad = findViewById(R.id.btnCalculaUtilidadProducto);
        btncalcularUtilidad.setOnClickListener(v -> calcularUtilidad());
        // obtener unidades desde remoto
        obtenerUnidades();
    }

    private void instanciaComponentes(){
        txtIdProducto = findViewById(R.id.txtIdProducto);
        txtDescripcion = findViewById(R.id.txtNombreProducto);
        txtUtilidad = findViewById(R.id.txtUtilidadProducto);
        txtPrecioCompra = findViewById(R.id.txtPrecioCompra);
        txtPrecioVenta = findViewById(R.id.txtPrecioVenta);
    }

    private void calcularUtilidad(){
        try {
            precioCompra = Float.parseFloat(txtPrecioCompra.getText().toString().trim());
            precioVenta = Float.parseFloat(txtPrecioVenta.getText().toString().trim());
            resta = precioVenta - precioCompra;
            nuevaUtilidad = (resta/precioCompra)*100;
            String conFormato = decimalFormat.format(nuevaUtilidad);
            //txtUtilidad.setText(String.valueOf(nuevaUtilidad));
            txtUtilidad.setText(conFormato);
        } catch (IllegalAccessError illegalAccessError){
            Toast.makeText(this, illegalAccessError.getMessage(),Toast.LENGTH_LONG).show();
        } catch (EmptyStackException emptyStackException){
            Toast.makeText(this, emptyStackException.getMessage(),Toast.LENGTH_LONG).show();
        } catch (NumberFormatException numberFormatException){
            Toast.makeText(this, numberFormatException.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //mostramos los datos del usuario en los campos de la interfaz
    @SuppressLint("SetTextI18n")
    private void mostrarDatosdelProducto(Producto producto){
        // Seteamos los datos en los views
        try {
            txtIdProducto.setText(String.valueOf(producto.getId()));
            txtDescripcion.setText(producto.getDescripcion());
            txtPrecioCompra.setText(producto.getPrecio_compra().toString());
            txtPrecioVenta.setText(producto.getPrecio_venta().toString());
            txtUtilidad.setText(producto.getUtilidad().toString());
        } catch (Error nullPointerException){
            Toast.makeText(ProductoActivity.this, nullPointerException.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Asigna los nuevos valores a las variables del objeto a guardar
    private boolean crearProducto() {
            estado = producto.getEstado();
            accion = producto.getAccion();
            utilidad = txtUtilidad.getText().toString().trim();
            descripcion = txtDescripcion.getText().toString().trim();
            precio_venta = txtPrecioVenta.getText().toString().trim();
            precio_compra = txtPrecioCompra.getText().toString().trim();
            id = Integer.parseInt(txtIdProducto.getText().toString().trim());
            idUnidad = spUnidades.getItemAtPosition(spUnidades.getSelectedItemPosition()).toString().trim();
            unidad = idUnidad.substring(0,1);
            return true;
    }

    private void llenarSpinnerUnidades(List<Unidad> unidadList) {
        listaUnidades = unidadList;
        ArrayList<String> stringUnidades = new ArrayList<>();
        int posicion = 0;
        try {
            for (int k = 0; k<unidadList.size();k++) {
                stringUnidades.add(unidadList.get(k).getId() +"-"+ unidadList.get(k).getDetalle());
                if (unidadList.get(k).getId() == producto.getFk_familia()){
                    posicion = k;
                }
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringUnidades);
            this.spUnidades.setAdapter(adapter);
            this.spUnidades.setSelection(posicion);
        } catch (NullPointerException nullPointerException){
            Toast.makeText(this, nullPointerException.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //guardar el cliente
    private void guardarProducto(){
        // Validamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(ProductoActivity.this)) {
            // Verificamos que todos los datos del reporte esten ingresados
            if (crearProducto()) {
                Call<JsonObject> solicitudAccionProducto = ApiUtils.getApiServices().accionProducto(id, unidad, descripcion, utilidad, precio_compra, precio_venta, accion, estado);
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
                        } catch (java.lang.Error e){
                            Toast.makeText(ProductoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ProductoActivity.this,"La peticion falló:  " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(ProductoActivity.this, "Faltan datos del reporte!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ProductoActivity.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    /**Solicitamos los datos al servidor remoto */
    private void obtenerUnidades() {
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(this)) {
            Call<JsonArray> requestProductos = ApiUtils.getApiServices().getUnidades();
            requestProductos.enqueue(new Callback<JsonArray>() {
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (!response.isSuccessful()) {
                        // Mensaje de error
                        Toast.makeText(ProductoActivity.this, response.message() +" al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Manejar datos obtenidos en la petición
                        listaUnidades = new ArrayList<>();
                        JsonArray listaPro = response.body();
                        for (int j = 0; j<listaPro.size();j++){
                            Unidad unidad = new Unidad();
                            unidad.setId(listaPro.get(j).getAsJsonObject().get("id").getAsInt());
                            unidad.setDetalle(listaPro.get(j).getAsJsonObject().get("detalle").toString());
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
}