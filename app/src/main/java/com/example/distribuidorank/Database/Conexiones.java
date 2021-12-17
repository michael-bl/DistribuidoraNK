package com.example.distribuidorank.Database;

import android.content.Context;

import com.example.distribuidorank.modelo.Producto;

import java.util.List;

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

    /* Recibe lista de productos, los recorre e inserta uno a uno en la db local*/
    public void insertarProductosEnDbLocal(List<Producto> listaProductos){
        posDataBase = new POS_DataBase(context);
        try {
            for(int k=0; k<listaProductos.size(); k++){
                Producto producto = new Producto();
                producto.setId(listaProductos.get(k).getId());
                producto.setFk_familia(listaProductos.get(k).getFk_familia());
                producto.setDescripcion(listaProductos.get(k).getDescripcion());
                producto.setUtilidad(listaProductos.get(k).getUtilidad());
                producto.setPrecio_compra(listaProductos.get(k).getPrecio_compra());
                producto.setPrecio_venta(listaProductos.get(k).getPrecio_venta());
                producto.setEstado(listaProductos.get(k).getEstado());
                posDataBase.insertProducto(producto,0);
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

}
