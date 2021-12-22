package com.example.distribuidorank.Database;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.widget.Toast;

public class Conexiones {
    private Context context;
    private POS_DataBase posDataBase;

    /* Constructor vacio*/
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
    public void insertarProductosEnDbLocal(String listaProductos) {
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Producto", listaProductos, 1) ? "Productos almecenados correctamente!": "Error al guardar productos!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e){
            e.printStackTrace();
        }
    }

    /* Recibe string con lista de unidades, las envia al metodo insert de la clase POS_Database*/
    public void insertarUnidadesEnDbLocal(String listaUnidades){
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Unidad", listaUnidades, 1) ? "Unidades almecenadas correctamente!": "Error al guardar unidades!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e){
            e.printStackTrace();
        }
    }

    /* Recibe string con lista de clientes, los envia al metodo insert de la clase POS_Database*/
    public void insertarClientesEnDbLocal(String listaClientes){
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Cliente", listaClientes, 1) ? "Clientes almecenados correctamente!": "Error al guardar clientes!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e){
            e.printStackTrace();
        }
    }

    /* Recibe string con lista de localidades, los envia al metodo insert de la clase POS_Database*/
    public void insertarLocalidadesEnDbLocal(String listaUnidades){
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Localidad", listaUnidades, 1) ? "Localidades almecenadas correctamente!": "Error al guardar localidades!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e){
            e.printStackTrace();
        }
    }

    /* Recibe string con lista de proveedores, los envia al metodo insert de la clase POS_Database*/
    public void insertarProveedoresEnDbLocal(String listaProveedores){
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Proveedor", listaProveedores, 1) ? "Proveedores almecenadas correctamente!": "Error al guardar proveedores!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e){
            e.printStackTrace();
        }
    }

    /* Recibe string con lista de cabecera factura, los envia al metodo insert de la clase POS_Database*/
    public void insertarCabeceraFacturaEnDbLocal(String listaCabeceraFactura){
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Cabecera_Factura", listaCabeceraFactura, 1) ? "Encabezados de factura almecenadas correctamente!": "Error al guardar encabezados de factura!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e){
            e.printStackTrace();
        }
    }

    /* Recibe string con lista detalles de facturas, los envia al metodo insert de la clase POS_Database*/
    public void insertarDetalleFacturaEnDbLocal(String listaDetalleFactura){
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Detalle_Factura", listaDetalleFactura, 1) ? "Detalles de factura almecenadas correctamente!": "Error al guardar detalles de factura!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e){
            e.printStackTrace();
        }
    }
}
