package com.example.distribuidorank.Database;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.widget.Toast;

import com.example.distribuidorank.modelo.Cliente;
import com.example.distribuidorank.modelo.Localidad;
import com.example.distribuidorank.modelo.Producto;
import com.example.distribuidorank.modelo.Proveedor;
import com.example.distribuidorank.modelo.Unidad;
import com.example.distribuidorank.modelo.Usuario;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Conexiones {
    private Gson gson;
    private Context context;
    private POS_DataBase posDataBase;

    public Conexiones() {
    }

    public Conexiones(Context contexto) {
        context = contexto;
    }

    public void crearDbLocal(Context context) {
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        try {
            dbHelper.getWritableDatabase();
        } catch (IllegalStateException illegalStateException) {
            illegalStateException.printStackTrace();
        }
    }

    public void insertarProductosEnDbLocal(String listaProductos) {
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Producto", listaProductos, 1) ? "Productos almecenados correctamente!" : "Error al guardar productos!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }

   public void insertarUnidadesEnDbLocal(String listaUnidades) {
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Unidad", listaUnidades, 1) ? "Unidades almecenadas correctamente!" : "Error al guardar unidades!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }

    public void insertarClientesEnDbLocal(String listaClientes) {
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Cliente", listaClientes, 1) ? "Clientes almecenados correctamente!" : "Error al guardar clientes!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }

    public void insertarLocalidadesEnDbLocal(String listaUnidades) {
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Localidad", listaUnidades, 1) ? "Localidades almecenadas correctamente!" : "Error al guardar localidades!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }

    public void insertarProveedoresEnDbLocal(String listaProveedores) {
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Proveedor", listaProveedores, 1) ? "Proveedores almecenadas correctamente!" : "Error al guardar proveedores!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }

    public void insertarCabeceraFacturaEnDbLocal(String listaCabeceraFactura) {
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Cabecera_Factura", listaCabeceraFactura, 1) ? "Encabezados de factura almecenadas correctamente!" : "Error al guardar encabezados de factura!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }

    public void insertarDetalleFacturaEnDbLocal(String listaDetalleFactura) {
        posDataBase = new POS_DataBase(context);
        try {
            String resultado = posDataBase.INSERT("Detalle_Factura", listaDetalleFactura, 1) ? "Detalles de factura almecenadas correctamente!" : "Error al guardar detalles de factura!";
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }

    public void crudModo(String modo, String accion) {
        posDataBase = new POS_DataBase(context);
        String resultado;
        try {
            switch (accion) {
                case "Insert":
                    resultado = posDataBase.INSERT("Modo", modo, 1) ? "Modo conexi??n almecenado correctamente!" : "Error al guardar modo de conexi??n!";
                    Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
                    break;
                case "Update":
                    resultado = posDataBase.UPDATE("Modo", modo, 1) ? "Modo conexi??n almecenado correctamente!" : "Error al guardar modo de conexi??n!";
                    Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }

    public int crudUsuario(int accion, String usuario) {
        posDataBase = new POS_DataBase(context);
        int resultado = 0;
        try {
            switch (accion) {
                case 0:
                    return posDataBase.INSERT("Usuario", usuario, 0) ? 1 : 0;
                case 1:
                    return posDataBase.UPDATE("Usuario", usuario, 0) ? 1 : 0;
                case 2:
                    // M??todo para actualizar unicamente el campo estado, falta crearlo en POS_DataBase
                    return posDataBase.UPDATE("Usuario", usuario, 0) ? 1 : 0;
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public int crudCliente(int accion, String cliente) {
        posDataBase = new POS_DataBase(context);
        int resultado = 0;
        try {
            switch (accion) {
                case 0:
                    return posDataBase.INSERT("Cliente", cliente, 0) ? 1 : 0;
                case 1:
                    return posDataBase.UPDATE("Cliente", cliente, 0) ? 1 : 0;
                case 2:
                    // M??todo para actualizar unicamente el campo estado, falta crearlo en POS_DataBase
                    return posDataBase.UPDATE("Cliente", cliente, 0) ? 1 : 0;
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public int crudProducto(int accion, String producto) {
        posDataBase = new POS_DataBase(context);
        int resultado = 0;
        try {
            switch (accion) {
                case 0:
                    return posDataBase.INSERT("Producto", producto, 0) ? 1 : 0;
                case 1:
                    return posDataBase.UPDATE("Producto", producto, 0) ? 1 : 0;
                case 2:
                    // M??todo para actualizar unicamente el campo estado, falta crearlo en POS_DataBase
                    return posDataBase.UPDATE("Producto", producto, 0) ? 1 : 0;
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public int crudProveedor(int accion, String proveedor) {
        posDataBase = new POS_DataBase(context);
        int resultado = 0;
        try {
            switch (accion) {
                case 0:
                    return posDataBase.INSERT("Proveedor", proveedor, 0) ? 1 : 0;
                case 1:
                    return posDataBase.UPDATE("Proveedor", proveedor, 0) ? 1 : 0;
                case 2:
                    // M??todo para actualizar unicamente el campo estado, falta crearlo en POS_DataBase
                    return posDataBase.UPDATE("Proveedor", proveedor, 0) ? 1 : 0;
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public int crudLocalidad(int accion, String proveedor) {
        posDataBase = new POS_DataBase(context);
        int resultado = 0;
        try {
            switch (accion) {
                case 0:
                    return posDataBase.INSERT("Localidad", proveedor, 0) ? 1 : 0;
                case 1:
                    return posDataBase.UPDATE("Localidad", proveedor, 0) ? 1 : 0;
                case 2:
                    // M??todo para actualizar unicamente el campo estado, falta crearlo en POS_DataBase
                    return posDataBase.UPDATE("Localidad", proveedor, 0) ? 1 : 0;
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public int crudUnidad(int accion, String unidad) {
        posDataBase = new POS_DataBase(context);
        int resultado = 0;
        try {
            switch (accion) {
                case 0:
                    return posDataBase.INSERT("Unidad", unidad, 0) ? 1 : 0;
                case 1:
                    return posDataBase.UPDATE("Unidad", unidad, 0) ? 1 : 0;
                case 2:
                    // M??todo para actualizar unicamente el campo estado, falta crearlo en POS_DataBase
                    return posDataBase.UPDATE("Unidad", unidad, 0) ? 1 : 0;
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    /* ****************************************************************************************************************************************** */

    /* ************************************************************ M??todos SELECT ************************************************************ */

    public boolean inicarSesion(String id, String pass) {
        try {
            gson = new Gson();
            posDataBase = new POS_DataBase(context);
            Usuario user = gson.fromJson(posDataBase.SELECT("Usuario", id), Usuario.class);
            if (user.getPass().equals(pass)) return true;
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } catch (SQLiteConstraintException sce) {
            sce.printStackTrace();
        }
        return false;
    }

    public int getModoDeAlmacenamiento() {
        posDataBase = new POS_DataBase(context);
        return Integer.parseInt(posDataBase.SELECT("Modo"));
    }

    public List<Cliente> getClientes() {
        try {
            gson = new Gson();
            posDataBase = new POS_DataBase(context);
            return gson.fromJson(posDataBase.SELECT("Cliente"), new TypeToken<ArrayList<Cliente>>() {
            }.getType());
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (JsonSyntaxException jsonSyntaxException) {
            jsonSyntaxException.printStackTrace();
        }
        return null;
    }

    public List<Usuario> getUsuarios() {
        try {
            gson = new Gson();
            posDataBase = new POS_DataBase(context);
            return gson.fromJson(posDataBase.SELECT("Usuario"), new TypeToken<ArrayList<Usuario>>() {
            }.getType());
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (JsonSyntaxException jsonSyntaxException) {
            jsonSyntaxException.printStackTrace();
        }
        return null;
    }

    public List<Usuario> getUsuarioXid(String id) {
        try {
            gson = new Gson();
            posDataBase = new POS_DataBase(context);
            return gson.fromJson(posDataBase.SELECT("Usuario", id), new TypeToken<ArrayList<Usuario>>() {
            }.getType());
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (JsonSyntaxException jsonSyntaxException) {
            jsonSyntaxException.printStackTrace();
        }
        return null;
    }

    public List<Producto> getProductos() {
        try {
            gson = new Gson();
            posDataBase = new POS_DataBase(context);
            return gson.fromJson(posDataBase.SELECT("Producto"), new TypeToken<ArrayList<Producto>>() {
            }.getType());
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
        return null;
    }

    public List<Localidad> getLocalidades() {
        try {
            gson = new Gson();
            posDataBase = new POS_DataBase(context);
            return gson.fromJson(posDataBase.SELECT("Localidad"), new TypeToken<ArrayList<Localidad>>() {
            }.getType());
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
        return null;
    }

    public List<Unidad> getUnidades() {
        try {
            gson = new Gson();
            posDataBase = new POS_DataBase(context);
            return gson.fromJson(posDataBase.SELECT("Unidad"), new TypeToken<ArrayList<Unidad>>() {
            }.getType());
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
        return null;
    }

    public List<Proveedor> getProveedores() {
        try {
            gson = new Gson();
            posDataBase = new POS_DataBase(context);
            return gson.fromJson(posDataBase.SELECT("Proveedor"), new TypeToken<ArrayList<Proveedor>>() {
            }.getType());
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
        return null;
    }
}
