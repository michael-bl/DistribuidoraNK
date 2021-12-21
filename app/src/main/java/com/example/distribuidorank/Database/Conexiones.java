package com.example.distribuidorank.Database;

import android.content.Context;
import android.widget.Toast;

public class Conexiones {
    private Context context;
    private POS_DataBase posDataBase;

    /* constructor vacio*/
    public Conexiones(){
    }

    /* Constructor recibe contexto*/
    public Conexiones(Context contexto){
        context = contexto;
    }

    /* Crea la db local*/
    public void crearDbLocal(Context context){
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        try {
            dbHelper.getWritableDatabase();
        } catch (IllegalStateException illegalStateException){
            illegalStateException.printStackTrace();
        }
    }

    /* Recibe string con lista de productos, los envia al metodo insert de la clase POS_Database*/
    public void insertarProductosEnDbLocal(String listaProductos){
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Producto", listaProductos, 1) ? "Productos almecenados correctamente!": "Error al guardar!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

    /* Recibe string con lista de unidades, las envia al metodo insert de la clase POS_Database*/
    public void insertarUnidadesEnDbLocal(String listaUnidades){
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Unidad", listaUnidades, 1) ? "Unidades almecenadas correctamente!": "Error al guardar!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

    /* Recibe string con liste de clientes, los envia al metodo insert de la clase POS_Database*/
    public void insertarClientesEnDbLocal(String listaClientes){
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Cliente", listaClientes, 1) ? "Clientes almecenados correctamente!": "Error al guardar!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }
}
