package com.example.distribuidorank.controlador;

import android.content.Context;
import android.widget.Toast;

import com.example.distribuidorank.modelo.Cliente;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControladorCliente {
    private List<Cliente> listaClientes;
    private ArrayList<String> stringListaClientes ;
    public ControladorCliente(){
    }

    public ArrayList<String> obtenerClientes(Context context){
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();

        if (con.stateConnection(context)) {
            Call<List<Cliente>> requestLastReports = ApiUtils.getApiServices().getCliente();
            requestLastReports.enqueue(new Callback<List<Cliente>>() {
                @Override
                public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(context, response.message() + "Error al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        listaClientes = response.body();
                        setListaEmpleados(listaClientes);

                        stringListaClientes = new ArrayList<>();
                        for (int i = 0; i < listaClientes.size(); i++) {
                            stringListaClientes.add(i, listaClientes.get(i).getId() + "-" + listaClientes.get(i).getNombre());
                        }
                        Toast.makeText(context, "Datos cargados exitosamente!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Cliente>> call, Throwable t) {
                    Toast.makeText(context, t.getMessage() +  "ResponseMsg, la peticion fallo!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
       return stringListaClientes;
    }

    private ArrayList<String> setListaEmpleados(List<Cliente> listaClientes){
        stringListaClientes = new ArrayList<>();
        for (int i = 0; i < listaClientes.size(); i++) {
            stringListaClientes.add(i, listaClientes.get(i).getId() + "-" + listaClientes.get(i).getNombre());
        }
        return stringListaClientes;
    }

}
