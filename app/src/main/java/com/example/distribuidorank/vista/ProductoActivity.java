package com.example.distribuidorank.vista;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distribuidorank.R;
import com.example.distribuidorank.modelo.Producto;

import java.util.List;

public class ProductoActivity extends AppCompatActivity {
    private List<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
    }

    /**Solicitamos los datos al servidor remoto */
    /*private void obtenerProductos() {
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(this)) {
            Call<List<Producto>> requestEmpleado = ApiUtils.getApiServices().getProductos();
            requestEmpleado.enqueue(new Callback<List<Producto>>() {

                public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                    if (!response.isSuccessful()) {
                        // Mensaje de error
                        Toast.makeText(ProductoActivity.this, "ResponseMsg al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Manejar datos obtenidos en la petición
                        listaProductos = response.body();
                        setProductos(listaProductos);
                    }
                }
                @Override
                public void onFailure(Call<List<Producto>> call, Throwable t) {
                    Toast.makeText(ProductoActivity.this, "ResponseMsg, la peticion fallo!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ProductoActivity.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setProductos(List<Producto> listaProductos) {

    }*/
}