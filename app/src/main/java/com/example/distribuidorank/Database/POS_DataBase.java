package com.example.distribuidorank.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.distribuidorank.modelo.Cabecera_factura;
import com.example.distribuidorank.modelo.Cliente;
import com.example.distribuidorank.modelo.Detalle_factura;
import com.example.distribuidorank.modelo.Localidad;
import com.example.distribuidorank.modelo.Modo;
import com.example.distribuidorank.modelo.Producto;
import com.example.distribuidorank.modelo.Proveedor;
import com.example.distribuidorank.modelo.Unidad;
import com.example.distribuidorank.modelo.Usuario;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class POS_DataBase extends DataBaseHelper {

    private final Context context;
    private final Calendar calendar = Calendar.getInstance(); // Devuelve una instancia del calendario del dispositivo
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Fija un formato (el mismo de MySQL DateTime) para las fechas
    private final Gson gson;

    public POS_DataBase(@Nullable Context _context) {
        super(_context);
        this.context = _context;
        this.gson = new Gson();
    }

    /* ************************************************************ Métodos INSERT ************************************************************ */
    public boolean insertCabecera_Factura(Cabecera_factura cabecera_factura, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", cabecera_factura.getId());
            values.put("tipo", cabecera_factura.getTipo());
            values.put("fk_cliente", cabecera_factura.getFk_cliente());
            values.put("fecha", this.simpleDateFormat.format(cabecera_factura.getFecha()));
            values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

            long trans_ID = db.insert("cabecera_factura", null, values); // Guarda el id de la transacción en la base de datos
            Log.i("SQLite DataBase", String.valueOf(trans_ID));


            // Respaldo para la tabla log_eventos
            if (trans_ID != -1) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO cabecera_factura (id, tipo, fk_cliente, fecha) VALUES (%d, %s, %s, %s)",
                        cabecera_factura.getId(),
                        cabecera_factura.getTipo(),
                        cabecera_factura.getFk_cliente(),
                        cabecera_factura.getFecha());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));
            }

            db.close();

            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertCabecera_Factura(1)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertCabecera_Factura(Cabecera_factura[] cabecera_facturas, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            db.beginTransaction();

            for (Cabecera_factura cabecera_factura : cabecera_facturas) {
                ContentValues values = new ContentValues();

                values.put("id", cabecera_factura.getId());
                values.put("tipo", cabecera_factura.getTipo());
                values.put("fk_cliente", cabecera_factura.getFk_cliente());
                values.put("fecha", this.simpleDateFormat.format(cabecera_factura.getFecha()));
                values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

                long trans_ID = db.insert("cabecera_factura", null, values); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));


                // Respaldo para la tabla log_eventos
                if (trans_ID != -1) {
                    @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO cabecera_factura (id, tipo, fk_cliente, fecha) VALUES (%d, %s, %s, %s)",
                            cabecera_factura.getId(),
                            cabecera_factura.getTipo(),
                            cabecera_factura.getFk_cliente(),
                            cabecera_factura.getFecha());

                    ContentValues values2 = new ContentValues();

                    values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                    values2.put("string_sql", sql_Command);
                    values2.put("actualizado", actualizado);

                    trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                    Log.i("SQLite DataBase", String.valueOf(trans_ID));
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertCabecera_Factura(2)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertCliente(Cliente cliente, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", cliente.getId());
            values.put("fk_localidad", cliente.getFk_localidad());
            values.put("nombre", cliente.getNombre());
            values.put("telefono", cliente.getTelefono());
            values.put("email", cliente.getEmail());
            values.put("direccion", cliente.getDireccion());
            values.put("estado", cliente.getEstado());
            values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

            long trans_ID = db.insert("cliente", null, values); // Guarda el id de la transacción en la base de datos
            Log.i("SQLite DataBase", String.valueOf(trans_ID));


            // Respaldo para la tabla log_eventos
            if (trans_ID != -1) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO cliente (id, fk_localidad, nombre, telefono, email, direccion, estado) VALUES (%s, %d, %s, %s, %s, %s, %d)",
                        cliente.getId(),
                        cliente.getFk_localidad(),
                        cliente.getNombre(),
                        cliente.getTelefono(),
                        cliente.getEmail(),
                        cliente.getDireccion(),
                        cliente.getEstado());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertCliente(1)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertCliente(Cliente[] clientes, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            db.beginTransaction();

            for (Cliente cliente : clientes) {
                ContentValues values = new ContentValues();

                values.put("id", cliente.getId());
                values.put("fk_localidad", cliente.getFk_localidad());
                values.put("nombre", cliente.getNombre());
                values.put("telefono", cliente.getTelefono());
                values.put("email", cliente.getEmail());
                values.put("direccion", cliente.getDireccion());
                values.put("estado", cliente.getEstado());
                values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

                long trans_ID = db.insert("cliente", null, values); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));


                // Respaldo para la tabla log_eventos
                if (trans_ID != -1) {
                    @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO cliente (id, fk_localidad, nombre, telefono, email, direccion, estado) VALUES (%s, %d, %s, %s, %s, %s, %d)",
                            cliente.getId(),
                            cliente.getFk_localidad(),
                            cliente.getNombre(),
                            cliente.getTelefono(),
                            cliente.getEmail(),
                            cliente.getDireccion(),
                            cliente.getEstado());

                    ContentValues values2 = new ContentValues();

                    values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                    values2.put("string_sql", sql_Command);
                    values2.put("actualizado", actualizado);

                    trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                    Log.i("SQLite DataBase", String.valueOf(trans_ID));
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertCliente(2)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertDetalle_Factura(Detalle_factura detalle_factura, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", detalle_factura.getId());
            values.put("fk_cabecera", detalle_factura.getFk_cabecera());
            values.put("fk_producto", detalle_factura.getFk_producto());
            values.put("utilidad", String.valueOf(detalle_factura.getUtilidad()));
            values.put("precio_compra", String.valueOf(detalle_factura.getPrecio_compra()));
            values.put("precio_venta", String.valueOf(detalle_factura.getPrecio_venta()));
            values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

            long trans_ID = db.insert("detalle_factura", null, values); // Guarda el id de la transacción en la base de datos
            Log.i("SQLite DataBase", String.valueOf(trans_ID));

            // Respaldo para la tabla log_eventos
            if (trans_ID != -1) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO detalle_factura (id, fk_cabecera, fk_producto, utilidad, precio_compra, precio_venta) VALUES (%d, %d, %d, %s, %s, %s)",
                        detalle_factura.getId(),
                        detalle_factura.getFk_cabecera(),
                        detalle_factura.getFk_producto(),
                        detalle_factura.getUtilidad(),
                        detalle_factura.getPrecio_compra(),
                        detalle_factura.getPrecio_venta());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertDetalle_Factura(1)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertDetalle_Factura(Detalle_factura[] detalle_facturas, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            db.beginTransaction();

            for (Detalle_factura detalle_factura : detalle_facturas) {
                ContentValues values = new ContentValues();

                values.put("id", detalle_factura.getId());
                values.put("fk_cabecera", detalle_factura.getFk_cabecera());
                values.put("fk_producto", detalle_factura.getFk_producto());
                values.put("utilidad", String.valueOf(detalle_factura.getUtilidad()));
                values.put("precio_compra", String.valueOf(detalle_factura.getPrecio_compra()));
                values.put("precio_venta", String.valueOf(detalle_factura.getPrecio_venta()));
                values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

                long trans_ID = db.insert("detalle_factura", null, values); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));

                // Respaldo para la tabla log_eventos
                if (trans_ID != -1) {
                    @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO detalle_factura (id, fk_cabecera, fk_producto, utilidad, precio_compra, precio_venta) VALUES (%d, %d, %d, %s, %s, %s)",
                            detalle_factura.getId(),
                            detalle_factura.getFk_cabecera(),
                            detalle_factura.getFk_producto(),
                            detalle_factura.getUtilidad(),
                            detalle_factura.getPrecio_compra(),
                            detalle_factura.getPrecio_venta());

                    ContentValues values2 = new ContentValues();

                    values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                    values2.put("string_sql", sql_Command);
                    values2.put("actualizado", actualizado);

                    trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                    Log.i("SQLite DataBase", String.valueOf(trans_ID));
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertDetalle_Factura(2)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertLocalidad(Localidad localidad, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", localidad.getId());
            values.put("localidad", localidad.getLocalidad());
            values.put("estado", localidad.getEstado());
            values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

            long trans_ID = db.insert("localidad", null, values); // Guarda el id de la transacción en la base de datos
            Log.i("SQLite DataBase", String.valueOf(trans_ID));


            // Respaldo para la tabla log_eventos
            if (trans_ID != -1) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO localidad (id, localidad, estado) VALUES (%d, %s, %d)",
                        localidad.getId(),
                        localidad.getLocalidad(),
                        localidad.getEstado());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertLocalidad(1)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertLocalidad(Localidad[] localidades, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            db.beginTransaction();

            for (Localidad localidad : localidades) {
                ContentValues values = new ContentValues();

                values.put("id", localidad.getId());
                values.put("localidad", localidad.getLocalidad());
                values.put("estado", localidad.getEstado());
                values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

                long trans_ID = db.insert("localidad", null, values); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));

                // Respaldo para la tabla log_eventos
                if (trans_ID != -1) {
                    @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO localidad (id, localidad, estado) VALUES (%d, %s, %d)",
                            localidad.getId(),
                            localidad.getLocalidad(),
                            localidad.getEstado());

                    ContentValues values2 = new ContentValues();

                    values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                    values2.put("string_sql", sql_Command);
                    values2.put("actualizado", actualizado);

                    trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                    Log.i("SQLite DataBase", String.valueOf(trans_ID));
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertLocalidad(2)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertProducto(Producto producto, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", producto.getId());
            values.put("fk_unidad", producto.getFk_unidad());
            values.put("descripcion", producto.getDescripcion());
            values.put("utilidad", String.valueOf(producto.getUtilidad()));
            values.put("precio_compra", String.valueOf(producto.getPrecio_compra()));
            values.put("precio_venta", String.valueOf(producto.getPrecio_venta()));
            values.put("estado", String.valueOf(producto.getEstado()));
            values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

            long trans_ID = db.insert("producto", null, values); // Guarda el id de la transacción en la base de datos
            Log.i("SQLite DataBase", String.valueOf(trans_ID));


            // Respaldo para la tabla log_eventos
            if (trans_ID != -1) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO producto (id, fk_familia, descripcion, utilidad, precio_compra, precio_venta, estado) VALUES (%d, %d, %s, %s, %s, %s, %d)",
                        producto.getId(),
                        producto.getFk_unidad(),
                        producto.getDescripcion(),
                        producto.getUtilidad(),
                        producto.getPrecio_compra(),
                        producto.getPrecio_venta(),
                        producto.getEstado());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertProducto(1)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertProducto(Producto[] productos, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            db.beginTransaction();

            for (Producto producto : productos) {
                ContentValues values = new ContentValues();

                values.put("id", producto.getId());
                values.put("fk_unidad", producto.getFk_unidad());
                values.put("descripcion", producto.getDescripcion());
                values.put("utilidad", String.valueOf(producto.getUtilidad()));
                values.put("precio_compra", String.valueOf(producto.getPrecio_compra()));
                values.put("precio_venta", String.valueOf(producto.getPrecio_venta()));
                values.put("estado", String.valueOf(producto.getEstado()));
                values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

                long trans_ID = db.insert("producto", null, values); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));

                // Respaldo para la tabla log_eventos
                if (trans_ID != -1) {
                    @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO producto (id, fk_familia, descripcion, utilidad, precio_compra, precio_venta, estado) VALUES (%d, %d, %s, %s, %s, %s, %d)",
                            producto.getId(),
                            producto.getFk_unidad(),
                            producto.getDescripcion(),
                            producto.getUtilidad(),
                            producto.getPrecio_compra(),
                            producto.getPrecio_venta(),
                            producto.getEstado());

                    ContentValues values2 = new ContentValues();

                    values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                    values2.put("string_sql", sql_Command);
                    values2.put("actualizado", actualizado);

                    trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                    Log.i("SQLite DataBase", String.valueOf(trans_ID));
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertProducto(2)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertProveedor(Proveedor proveedor, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", proveedor.getId());
            values.put("nombre", proveedor.getNombre());
            values.put("telefono", proveedor.getTelefono());
            values.put("email", proveedor.getEmail());
            values.put("estado", proveedor.getEstado());
            values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

            long trans_ID = db.insert("proveedor", null, values); // Guarda el id de la transacción en la base de datos
            Log.i("SQLite DataBase", String.valueOf(trans_ID));

            // Respaldo para la tabla log_eventos
            if (trans_ID != -1) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO proveedor (id, nombre, telefono, email, estado) VALUES (%s, %s, %s, %s, %d)",
                        proveedor.getId(),
                        proveedor.getNombre(),
                        proveedor.getTelefono(),
                        proveedor.getEmail(),
                        proveedor.getEstado());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertProveedor(1)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertProveedor(Proveedor[] proveedores, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            db.beginTransaction();

            for (Proveedor proveedor : proveedores) {
                ContentValues values = new ContentValues();

                values.put("id", proveedor.getId());
                values.put("nombre", proveedor.getNombre());
                values.put("telefono", proveedor.getTelefono());
                values.put("email", proveedor.getEmail());
                values.put("estado", proveedor.getEstado());
                values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

                long trans_ID = db.insert("proveedor", null, values); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));

                // Respaldo para la tabla log_eventos
                if (trans_ID != -1) {
                    @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO proveedor (id, nombre, telefono, email, estado) VALUES (%s, %s, %s, %s, %d)",
                            proveedor.getId(),
                            proveedor.getNombre(),
                            proveedor.getTelefono(),
                            proveedor.getEmail(),
                            proveedor.getEstado());

                    ContentValues values2 = new ContentValues();

                    values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                    values2.put("string_sql", sql_Command);
                    values2.put("actualizado", actualizado);

                    trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                    Log.i("SQLite DataBase", String.valueOf(trans_ID));
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertProveedor(2)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertUnidad(Unidad unidad, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", unidad.getId());
            values.put("detalle", unidad.getDetalle());
            values.put("estado", unidad.getEstado());
            values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

            long trans_ID = db.insert("unidad", null, values); // Guarda el id de la transacción en la base de datos
            Log.i("SQLite DataBase", String.valueOf(trans_ID));

            // Respaldo para la tabla log_eventos
            if (trans_ID != -1) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO unidad (id, detalle, estado) VALUES (%d, %s, %d)",
                        unidad.getId(),
                        unidad.getDetalle(),
                        unidad.getEstado());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertUnidad(1)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertUnidad(Unidad[] unidades, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            db.beginTransaction();

            for (Unidad unidad : unidades) {
                ContentValues values = new ContentValues();

                values.put("id", unidad.getId());
                values.put("detalle", unidad.getDetalle());
                values.put("estado", unidad.getEstado());
                values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

                long trans_ID = db.insert("unidad", null, values); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));

                // Respaldo para la tabla log_eventos
                if (trans_ID != -1) {
                    @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO unidad (id, detalle, estado) VALUES (%d, %s, %d)",
                            unidad.getId(),
                            unidad.getDetalle(),
                            unidad.getEstado());

                    ContentValues values2 = new ContentValues();

                    values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                    values2.put("string_sql", sql_Command);
                    values2.put("actualizado", actualizado);

                    trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                    Log.i("SQLite DataBase", String.valueOf(trans_ID));
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertUnidad(2)\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertUsuario(Usuario usuario, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", usuario.getId());
            values.put("fk_localidad", usuario.getFk_localidad());
            values.put("nombre", usuario.getNombre());
            values.put("pass", usuario.getPass());
            values.put("telefono", usuario.getTelefono());
            values.put("email", usuario.getEmail());
            values.put("direccion", usuario.getDireccion());
            values.put("estado", usuario.getEstado());
            values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));

            long trans_ID = db.insert("usuario", null, values); // Guarda el id de la transacción en la base de datos
            Log.i("SQLite DataBase", String.valueOf(trans_ID));

            // Respaldo para la tabla log_eventos
            if (trans_ID != -1) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO usuario (id, fk_localidad, nombre, pass, telefono, email, direccion, estado) VALUES (%s, %d, %s, %s, %s, %s, %s, %d)",
                        usuario.getId(),
                        usuario.getFk_localidad(),
                        usuario.getNombre(),
                        usuario.getPass(),
                        usuario.getTelefono(),
                        usuario.getEmail(),
                        usuario.getDireccion(),
                        usuario.getEstado());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertUsuario\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean insertModo(Modo modo, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", modo.getId());
            values.put("mode", modo.getMode());

            long trans_ID = db.insert("modo", null, values); // Guarda el id de la transacción en la base de datos
            Log.i("SQLite DataBase", String.valueOf(trans_ID));

            // Respaldo para la tabla log_eventos
            if (trans_ID != -1) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("INSERT INTO modo (id, mode) VALUES (%d, %d)",
                        modo.getId(),
                        modo.getMode());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos
                Log.i("SQLite DataBase", String.valueOf(trans_ID));
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: insertModo\nError:%s", e.toString()));
            return false;
        }
    }

    /* ****************************************************************************************************************************************** */


    /* ************************************************************ Métodos SELECT ************************************************************ */
    public ArrayList<Cabecera_factura> selectCabecera_Factura() {

        ArrayList<Cabecera_factura> cabecera_facturas = new ArrayList<>();
        Cabecera_factura cabecera_factura = new Cabecera_factura();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM cabecera_factura", null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");


            if (cursor.moveToFirst()) {
                do {

                    cabecera_factura.setId(cursor.getInt(0));
                    cabecera_factura.setTipo(cursor.getString(1));
                    cabecera_factura.setFk_cliente(cursor.getString(2));
                    cabecera_factura.setFecha(this.simpleDateFormat.parse(cursor.getString(3)));

                    cabecera_facturas.add(cabecera_factura);

                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectCabecera_Factura(1)\nError:%s", e.toString()));
        }
        return cabecera_facturas;
    }

    @SuppressLint("DefaultLocale")
    public Cabecera_factura selectCabecera_Factura(int id) {

        Cabecera_factura cabecera_factura = new Cabecera_factura();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.rawQuery(String.format("SELECT * FROM cabecera_factura WHERE id = %d LIMIT 1", id), null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {

                cabecera_factura.setId(cursor.getInt(0));
                cabecera_factura.setTipo(cursor.getString(1));
                cabecera_factura.setFk_cliente(cursor.getString(2));
                cabecera_factura.setFecha(this.simpleDateFormat.parse(cursor.getString(3)));

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectCabecera_Factura(2)\nError:%s", e.toString()));
        }
        return cabecera_factura;
    }

    public ArrayList<Cliente> selectCliente() {

        ArrayList<Cliente> clientes = new ArrayList<>();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM cliente", null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {
                do {
                    Cliente cliente = new Cliente();
                    cliente.setId(cursor.getString(0));
                    cliente.setFk_localidad(cursor.getInt(1));
                    cliente.setNombre(cursor.getString(2));
                    cliente.setTelefono(cursor.getString(3));
                    cliente.setEmail(cursor.getString(4));
                    cliente.setDireccion(cursor.getString(5));
                    cliente.setEstado(cursor.getInt(6));
                    cliente.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(7)));

                    clientes.add(cliente);

                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectCliente(1)\nError:%s", e.toString()));
        }
        return clientes;
    }

    public Cliente selectCliente(String id) {

        Cliente cliente = new Cliente();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.rawQuery(String.format("SELECT * FROM cliente WHERE id = %s LIMIT 1", id), null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {

                cliente.setId(cursor.getString(0));
                cliente.setFk_localidad(cursor.getInt(1));
                cliente.setNombre(cursor.getString(2));
                cliente.setTelefono(cursor.getString(3));
                cliente.setEmail(cursor.getString(4));
                cliente.setDireccion(cursor.getString(5));
                cliente.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(6)));

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectCliente(2)\nError:%s", e.toString()));
        }
        return cliente;
    }

    public ArrayList<Detalle_factura> selectDetalle_Factura() {

        ArrayList<Detalle_factura> detalle_facturas = new ArrayList<>();
        Detalle_factura detalle_factura = new Detalle_factura();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM detalle_factura", null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {
                do {

                    detalle_factura.setId(cursor.getInt(0));
                    detalle_factura.setFk_cabecera(cursor.getInt(1));
                    detalle_factura.setFk_producto(cursor.getInt(2));
                    detalle_factura.setUtilidad(cursor.getString(3));
                    detalle_factura.setPrecio_compra(cursor.getString(4));
                    detalle_factura.setPrecio_venta(cursor.getString(5));
                    detalle_factura.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(6)));

                    detalle_facturas.add(detalle_factura);

                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectDetalle_Factura(1)\nError:%s", e.toString()));
        }
        return detalle_facturas;
    }

    @SuppressLint("DefaultLocale")
    public Detalle_factura selectDetalle_Factura(int id) {

        Detalle_factura detalle_factura = new Detalle_factura();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.rawQuery(String.format("SELECT * FROM detalle_factura WHERE id = %d LIMIT 1", id), null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {

                detalle_factura.setId(cursor.getInt(0));
                detalle_factura.setFk_cabecera(cursor.getInt(1));
                detalle_factura.setFk_producto(cursor.getInt(2));
                detalle_factura.setUtilidad(cursor.getString(3));
                detalle_factura.setPrecio_compra(cursor.getString(4));
                detalle_factura.setPrecio_venta(cursor.getString(5));
                detalle_factura.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(6)));

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectDetalle_Factura(2)\nError:%s", e.toString()));
        }
        return detalle_factura;
    }

    public ArrayList<Localidad> selectLocalidad() {

        ArrayList<Localidad> localidades = new ArrayList<>();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM localidad", null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {
                do {
                    Localidad localidad = new Localidad();
                    localidad.setId(cursor.getInt(0));
                    localidad.setLocalidad(cursor.getString(1));
                    localidad.setEstado(cursor.getInt(2));
                    localidad.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(3)));

                    localidades.add(localidad);

                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectLocalidad(1)\nError:%s", e.toString()));
        }
        return localidades;
    }

    @SuppressLint("DefaultLocale")
    public Localidad selectLocalidad(int id) {

        Localidad localidad = new Localidad();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.rawQuery(String.format("SELECT * FROM localidad WHERE id = %d LIMIT 1", id), null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {

                localidad.setId(cursor.getInt(0));
                localidad.setLocalidad(cursor.getString(1));
                localidad.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(2)));

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectLocalidad(2)\nError:%s", e.toString()));
        }
        return localidad;
    }

    public ArrayList<Producto> selectProducto() {

        ArrayList<Producto> productos = new ArrayList<>();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM producto", null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {
                do {
                    Producto producto = new Producto();
                    producto.setId(cursor.getInt(0));
                    producto.setFk_unidad(cursor.getInt(1));
                    producto.setDescripcion(cursor.getString(2));
                    producto.setUtilidad(cursor.getString(3));
                    producto.setPrecio_compra(cursor.getString(4));
                    producto.setPrecio_venta(cursor.getString(5));
                    producto.setEstado(cursor.getInt(6));
                    producto.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(7)));

                    productos.add(producto);

                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectProducto(1)\nError:%s", e.toString()));
        }
        return productos;
    }

    @SuppressLint("DefaultLocale")
    public Producto selectProducto(int id) {

        Producto producto = new Producto();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.rawQuery(String.format("SELECT * FROM producto WHERE id = %d LIMIT 1", id), null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {

                producto.setId(cursor.getInt(0));
                producto.setFk_unidad(cursor.getInt(1));
                producto.setDescripcion(cursor.getString(2));
                producto.setUtilidad(cursor.getString(3));
                producto.setPrecio_compra(cursor.getString(4));
                producto.setPrecio_venta(cursor.getString(5));
                producto.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(6)));

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectProducto(2)\nError:%s", e.toString()));
        }
        return producto;
    }

    public ArrayList<Proveedor> selectProveedor() {

        ArrayList<Proveedor> proveedores = new ArrayList<>();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM proveedor", null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {
                do {
                    Proveedor proveedor = new Proveedor();
                    proveedor.setId(cursor.getString(0));
                    proveedor.setNombre(cursor.getString(1));
                    proveedor.setTelefono(cursor.getString(2));
                    proveedor.setEmail(cursor.getString(3));
                    proveedor.setEstado(cursor.getInt(4));
                    proveedor.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(5)));

                    proveedores.add(proveedor);

                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectProveedor(1)\nError:%s", e.toString()));
        }
        return proveedores;
    }

    public Proveedor selectProveedor(String id) {

        Proveedor proveedor = new Proveedor();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.rawQuery(String.format("SELECT * FROM proveedor WHERE id = %s LIMIT 1", id), null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {

                proveedor.setId(cursor.getString(0));
                proveedor.setNombre(cursor.getString(1));
                proveedor.setTelefono(cursor.getString(2));
                proveedor.setEmail(cursor.getString(3));
                proveedor.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(4)));

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectProveedor(2)\nError:%s", e.toString()));
        }
        return proveedor;
    }

    public ArrayList<Unidad> selectUnidad() {

        ArrayList<Unidad> unidades = new ArrayList<>();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM unidad", null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {
                do {
                    Unidad unidad = new Unidad();
                    unidad.setId(cursor.getInt(0));
                    unidad.setDetalle(cursor.getString(1));
                    unidad.setEstado(cursor.getInt(2));
                    unidad.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(3)));

                    unidades.add(unidad);

                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectUnidad(1)\nError:%s", e.toString()));
        }
        return unidades;
    }

    @SuppressLint("DefaultLocale")
    public Unidad selectUnidad(int id) {

        Unidad unidad = new Unidad();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.rawQuery(String.format("SELECT * FROM unidad WHERE id = %d LIMIT 1", id), null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {

                unidad.setId(cursor.getInt(0));
                unidad.setDetalle(cursor.getString(1));
                unidad.setEstado(cursor.getInt(2));
                unidad.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(3)));

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectUnidad(2)\nError:%s", e.toString()));
        }
        return unidad;
    }

    public ArrayList<Usuario> selectUsuario() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        Cursor cursor;
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM usuario", null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {
                do{
                    Usuario usuario = new Usuario();
                    usuario.setId(cursor.getString(0));
                    usuario.setFk_localidad(cursor.getInt(1));
                    usuario.setNombre(cursor.getString(2));
                    usuario.setPass(cursor.getString(3));
                    usuario.setTelefono(cursor.getString(4));
                    usuario.setEmail(cursor.getString(5));
                    usuario.setDireccion(cursor.getString(6));
                    usuario.setEstado(cursor.getInt(7));
                    usuario.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(8)));
                    usuarios.add(usuario);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectUsuario(1)\nError:%s", e.toString()));
        }
        return usuarios;
    }

    public Usuario selectUsuario(String id) {

        Usuario usuario = new Usuario();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.rawQuery(String.format("SELECT * FROM usuario WHERE id = %s LIMIT 1", id), null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {

                usuario.setId(cursor.getString(0));
                usuario.setFk_localidad(cursor.getInt(1));
                usuario.setNombre(cursor.getString(2));
                usuario.setPass(cursor.getString(3));
                usuario.setTelefono(cursor.getString(4));
                usuario.setEmail(cursor.getString(5));
                usuario.setDireccion(cursor.getString(6));
                usuario.setEstado(cursor.getInt(7));
                usuario.setUltima_actualizacion(this.simpleDateFormat.parse(cursor.getString(8)));

                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectUsuario(2)\nError:%s", e.toString()));
        }
        return usuario;
    }

    @SuppressLint("DefaultLocale")
    public int selectModo() {

        Modo modo = new Modo();
        Cursor cursor;

        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.rawQuery("SELECT mode FROM modo WHERE id = 1", null);
            Log.i("SQLite DataBase", "Sentencia 'SELECT' ejecutada");

            if (cursor.moveToFirst()) {
                modo.setMode(cursor.getInt(0));
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: selectModo(1)\nError:%s", e.toString()));
        }
        return modo.getMode();
    }
    /* ****************************************************************************************************************************************** */


    /* ************************************************************ Métodos UPDATE ************************************************************ */
    public boolean updateCabecera_Factura(Cabecera_factura cabecera_factura, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", cabecera_factura.getId());
            values.put("tipo", cabecera_factura.getTipo());
            values.put("fk_cliente", cabecera_factura.getFk_cliente());
            values.put("fecha", this.simpleDateFormat.format(cabecera_factura.getFecha()));

            @SuppressLint("DefaultLocale") long trans_ID = db.update("cabecera_factura", values, String.format("id = %d", cabecera_factura.getId()), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("UPDATE cabecera_factura SET tipo = '%s', fk_cliente = '%s', fecha = '%s' WHERE id = %d",
                        cabecera_factura.getTipo(),
                        cabecera_factura.getFk_cliente(),
                        cabecera_factura.getFecha(),
                        cabecera_factura.getId());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'UPDATE' ejecutada");
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: updateCabecera_Factura\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean updateCliente(Cliente cliente, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", cliente.getId());
            values.put("fk_localidad", cliente.getFk_localidad());
            values.put("nombre", cliente.getNombre());
            values.put("telefono", cliente.getTelefono());
            values.put("email", cliente.getEmail());
            values.put("direccion", cliente.getDireccion());

            long trans_ID = db.update("cliente", values, String.format("id = %s", cliente.getId()), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("UPDATE cliente SET fk_localidad = '%d', nombre = '%s', telefono = '%s', email = '%s', direccion = '%s' WHERE id = %s",
                        cliente.getFk_localidad(),
                        cliente.getNombre(),
                        cliente.getTelefono(),
                        cliente.getEmail(),
                        cliente.getDireccion(),
                        cliente.getId());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'UPDATE' ejecutada");
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: updateCliente\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean updateDetalle_Factura(Detalle_factura detalle_factura, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", detalle_factura.getId());
            values.put("fk_cabecera", detalle_factura.getFk_cabecera());
            values.put("fk_producto", detalle_factura.getFk_producto());
            values.put("utilidad", String.valueOf(detalle_factura.getUtilidad()));
            values.put("precio_compra", String.valueOf(detalle_factura.getPrecio_compra()));
            values.put("precio_venta", String.valueOf(detalle_factura.getPrecio_venta()));

            @SuppressLint("DefaultLocale") long trans_ID = db.update("detalle_factura", values, String.format("id = %d", detalle_factura.getId()), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("UPDATE detalle_factura SET fk_cabecera = '%s', fk_producto = '%s', utilidad = '%s', precio_compra = '%s', precio_venta = '%s' WHERE id = %d",
                        detalle_factura.getFk_cabecera(),
                        detalle_factura.getFk_producto(),
                        detalle_factura.getUtilidad(),
                        detalle_factura.getPrecio_compra(),
                        detalle_factura.getPrecio_venta(),
                        detalle_factura.getId());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'UPDATE' ejecutada");
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: updateDetalle_Factura\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean updateLocalidad(Localidad localidad, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", localidad.getId());
            values.put("fk_localidad", localidad.getLocalidad());

            @SuppressLint("DefaultLocale") long trans_ID = db.update("localidad", values, String.format("id = %d", localidad.getId()), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("UPDATE localidad SET localidad = '%s' WHERE id = %d",
                        localidad.getLocalidad(),
                        localidad.getId());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'UPDATE' ejecutada");
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: updateLocalidad\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean updateProducto(Producto producto, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", producto.getId());
            values.put("fk_unidad", producto.getFk_unidad());
            values.put("descripcion", producto.getDescripcion());
            values.put("utilidad", String.valueOf(producto.getUtilidad()));
            values.put("precio_compra", String.valueOf(producto.getPrecio_compra()));
            values.put("precio_venta", String.valueOf(producto.getPrecio_venta()));
            values.put("estado", String.valueOf(producto.getEstado()));

            @SuppressLint("DefaultLocale") long trans_ID = db.update("producto", values, String.format("id = %d", producto.getId()), null);

            if (trans_ID > actualizado) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("UPDATE producto SET fk_unidad = '%d', descripcion = '%s', utilidad = '%s', precio_compra = '%s', precio_venta = '%s', estado = '%d' WHERE id = %d",
                        producto.getFk_unidad(),
                        producto.getDescripcion(),
                        producto.getUtilidad(),
                        producto.getPrecio_compra(),
                        producto.getPrecio_venta(),
                        producto.getEstado(),
                        producto.getId());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", 0);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'UPDATE' ejecutada");
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: updateProducto\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean updateProveedor(Proveedor proveedor, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", proveedor.getId());
            values.put("nombre", proveedor.getNombre());
            values.put("telefono", proveedor.getTelefono());
            values.put("email", proveedor.getEmail());

            long trans_ID = db.update("proveedor", values, String.format("id = %s", proveedor.getId()), null);

            if (trans_ID > 0) {
                String sql_Command = String.format("UPDATE proveedor SET nombre = '%s', telefono = '%s', email = '%s' WHERE id = %s",
                        proveedor.getNombre(),
                        proveedor.getTelefono(),
                        proveedor.getEmail(),
                        proveedor.getId());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'UPDATE' ejecutada");
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: updateProveedor\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean updateUnidad(Unidad unidad, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", unidad.getId());
            values.put("detalle", unidad.getDetalle());
            values.put("estado", unidad.getEstado());
            values.put("ultima_actualizacion", unidad.getUltima_actualizacion().toString());

            @SuppressLint("DefaultLocale") long trans_ID = db.update("unidad", values, String.format("id = %d", unidad.getId()), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("UPDATE unidad SET detalle = '%s' WHERE id = %d",
                        unidad.getDetalle(),
                        unidad.getId(),
                        unidad.getEstado(),
                        unidad.getUltima_actualizacion());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'UPDATE' ejecutada");
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: updateUnidad\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean updateUsuario(Usuario usuario, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", usuario.getId());
            values.put("fk_localidad", usuario.getFk_localidad());
            values.put("nombre", usuario.getNombre());
            values.put("pass", usuario.getPass());
            values.put("telefono", usuario.getTelefono());
            values.put("email", usuario.getEmail());
            values.put("direccion", usuario.getDireccion());
            values.put("estado", usuario.getEstado());
            //values.put("ultima_actualizacion", usuario.getUltima_actualizacion().toString()); verificar con Mario la fecha a insertar
            // esta linea comentada o la que esta en uso
            values.put("ultima_actualizacion", this.simpleDateFormat.format(this.calendar.getTime()));
            long trans_ID = db.update("usuario", values, String.format("id = %s", usuario.getId()), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("UPDATE usuario SET fk_localidad = '%d', nombre = '%s', pass = '%s', telefono = '%s', email = '%s', direccion = '%s', estado = '%s', ultima_actualizacion = '%s' WHERE id = %s",
                        usuario.getFk_localidad(),
                        usuario.getNombre(),
                        usuario.getPass(),
                        usuario.getTelefono(),
                        usuario.getEmail(),
                        usuario.getDireccion(),
                        usuario.getEstado(),
                        usuario.getUltima_actualizacion(),
                        usuario.getId());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'UPDATE' ejecutada");
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: updateUsuario\nError:%s", e.toString()));
            return false;
        }
    }

    @SuppressLint("DefaultLocale")
    public boolean updateModo(Modo modo, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("id", modo.getId());
            values.put("fk_localidad", modo.getMode());

            long trans_ID = db.update("modo", values, String.format("id = %d", modo.getId()), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("UPDATE modo SET mode = '%d' WHERE id = %d",
                        modo.getId(),
                        modo.getMode());

                ContentValues values2 = new ContentValues();

                values2.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values2.put("string_sql", sql_Command);
                values2.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values2); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'UPDATE' ejecutada");
            }

            db.close();
            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: updateModo\nError:%s", e.toString()));
            return false;
        }
    }
    /* ****************************************************************************************************************************************** */


    /* ************************************************************ Métodos DELETE ************************************************************ */
    public boolean deleteCabecera_Factura(int id, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            @SuppressLint("DefaultLocale") long trans_ID = db.delete("cabecera_factura", String.format("id = %d", id), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("DELETE FROM cabecera_factura WHERE id = '%d'", id);

                ContentValues values = new ContentValues();

                values.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values.put("string_sql", sql_Command);
                values.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'DELETE' ejecutada");
            }

            db.close();

            return true;
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: deleteCabecera_Factura\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean deleteCliente(String id, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            long trans_ID = db.delete("cliente", String.format("id = %s", id), null);

            if (trans_ID > 0) {
                String sql_Command = String.format("DELETE FROM cliente WHERE id = %s", id);

                ContentValues values = new ContentValues();

                values.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values.put("string_sql", sql_Command);
                values.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'DELETE' ejecutada");
            }

            db.close();

            return true;
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: deleteCliente\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean deleteDetalle_Factura(int id, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            @SuppressLint("DefaultLocale") long trans_ID = db.delete("detalle_factura", String.format("id = %d", id), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("DELETE FROM detalle_factura WHERE id = %d", id);

                ContentValues values = new ContentValues();

                values.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values.put("string_sql", sql_Command);
                values.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'DELETE' ejecutada");
            }

            db.close();

            return true;
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: deleteDetalle_Factura\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean deleteLocalidad(int id, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            @SuppressLint("DefaultLocale") long trans_ID = db.delete("localidad", String.format("id = %d", id), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("DELETE FROM localidad WHERE id = %d", id);

                ContentValues values = new ContentValues();

                values.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values.put("string_sql", sql_Command);
                values.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'DELETE' ejecutada");
            }

            db.close();

            return true;

        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: deleteLocalidad\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean deleteProducto(int id, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            @SuppressLint("DefaultLocale") long trans_ID = db.delete("producto", String.format("id = %d", id), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("DELETE FROM producto WHERE id = %d", id);

                ContentValues values = new ContentValues();

                values.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values.put("string_sql", sql_Command);
                values.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'DELETE' ejecutada");
            }

            db.close();

            return true;
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: deleteProducto\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean deleteProveedor(String id, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            long trans_ID = db.delete("proveedor", String.format("id = %s", id), null);

            if (trans_ID > 0) {
                String sql_Command = String.format("DELETE FROM proveedor WHERE id = %s", id);

                ContentValues values = new ContentValues();

                values.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values.put("string_sql", sql_Command);
                values.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'DELETE' ejecutada");
            }

            db.close();

            return true;
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: deleteProveedor\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean deleteUnidad(int id, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            @SuppressLint("DefaultLocale") long trans_ID = db.delete("unidad", String.format("id = %d", id), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("DELETE FROM unidad WHERE id = %d", id);

                ContentValues values = new ContentValues();

                values.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values.put("string_sql", sql_Command);
                values.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'DELETE' ejecutada");
            }

            db.close();

            return true;
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: deleteUnidad\nError:%s", e.toString()));
            return false;
        }
    }

    public boolean deleteUsuario(String id, int actualizado) {
        try {
            DataBaseHelper helper = new DataBaseHelper(this.context);
            SQLiteDatabase db = helper.getWritableDatabase();

            long trans_ID = db.delete("usuario", String.format("id = %s", id), null);

            if (trans_ID > 0) {
                @SuppressLint("DefaultLocale") String sql_Command = String.format("DELETE FROM usuario WHERE id = %s", id);

                ContentValues values = new ContentValues();

                values.put("fecha", this.simpleDateFormat.format(this.calendar.getTime()));
                values.put("string_sql", sql_Command);
                values.put("actualizado", actualizado);

                trans_ID = db.insert("log_eventos", null, values); // Guarda el id de la transacción en la base de datos

                Log.i("SQLite DataBase", String.valueOf(trans_ID));
                Log.i("SQLite DataBase", "Sentencia 'DELETE' ejecutada");
            }

            db.close();

            return true;
        } catch (Exception e) {
            Log.e("SQLite DataBase", String.format("Clase: POS_Database\tMétodo: deleteUsuario\nError:%s", e.toString()));
            return false;
        }
    }
    /* ****************************************************************************************************************************************** */


    /* ********************************************************** Métodos Simplificados ********************************************************** */
    public boolean INSERT(@NonNull String tabla, String object, int actualizado) {

        boolean result;

        switch (tabla) {

            case "Cabecera_Factura":
                Cabecera_factura[] cabecera_facturas = this.gson.fromJson(object, Cabecera_factura[].class);

                if(cabecera_facturas.length == 1){
                    result = insertCabecera_Factura(cabecera_facturas[0], actualizado);
                } else {
                    result = insertCabecera_Factura(cabecera_facturas, actualizado);
                }
                break;

            case "Cliente":
                Cliente[] clientes = this.gson.fromJson(object, Cliente[].class);

                if(clientes.length == 1){
                    result = insertCliente(clientes[0], actualizado);
                } else {
                    result = insertCliente(clientes, actualizado);
                }
                break;

            case "Detalle_Factura":
                Detalle_factura[] detalle_facturas = this.gson.fromJson(object, Detalle_factura[].class);

                if(detalle_facturas.length == 1){
                    result = insertDetalle_Factura(detalle_facturas[0], actualizado);
                } else {
                    result = insertDetalle_Factura(detalle_facturas, actualizado);
                }
                break;

            case "Localidad":
                Localidad[] localidades = this.gson.fromJson(object, Localidad[].class);

                if(localidades.length == 1){
                    result = insertLocalidad(localidades[0], actualizado);
                } else {
                    result = insertLocalidad(localidades, actualizado);
                }
                break;

            case "Producto":
                Producto[] productos = this.gson.fromJson(object, Producto[].class);

                if(productos.length == 1){
                    result = insertProducto(productos[0], actualizado);
                } else {
                    result = insertProducto(productos, actualizado);
                }
                break;

            case "Proveedor":
                Proveedor[] proveedores = this.gson.fromJson(object, Proveedor[].class);

                if(proveedores.length == 1){
                    result = insertProveedor(proveedores[0], actualizado);
                } else {
                    result = insertProveedor(proveedores, actualizado);
                }
                break;

            case "Unidad":
                Unidad[] unidades = this.gson.fromJson(object, Unidad[].class);

                if(unidades.length == 1){
                    result = insertUnidad(unidades[0], actualizado);
                } else {
                    result = insertUnidad(unidades, actualizado);
                }
                break;

            case "Usuario":
                result = insertUsuario(this.gson.fromJson(object, Usuario.class), actualizado);
                break;

            case "Modo":
                result = insertModo(this.gson.fromJson(object, Modo.class), actualizado);
                break;

            default:
                result = false;
                break;
        }

        return result;
    }

    public String SELECT(@NonNull String tabla) {

        String object;

        switch (tabla) {

            case "Cabecera_Factura":
                object = this.gson.toJson(selectCabecera_Factura());
                break;

            case "Cliente":
                object = this.gson.toJson(selectCliente());
                break;

            case "Detalle_Factura":
                object = this.gson.toJson(selectDetalle_Factura());
                break;

            case "Localidad":
                object = this.gson.toJson(selectLocalidad());
                break;

            case "Producto":
                object = this.gson.toJson(selectProducto());
                break;

            case "Proveedor":
                object = this.gson.toJson(selectProveedor());
                break;

            case "Unidad":
                object = this.gson.toJson(selectUnidad());
                break;

            case "Usuario":
                object = this.gson.toJson(selectUsuario());
                break;

            case "Modo":
                object = this.gson.toJson(selectModo());
                break;

            default:
                object = "El criterio de búsqueda no coincidió con ninguno de los casos";
        }

        return object;
    }

    public String SELECT(@NonNull String tabla, String id) {

        String object;

        switch (tabla) {

            case "Cabecera_Factura":
                object = this.gson.toJson(selectCabecera_Factura(Integer.parseInt(id)));
                break;

            case "Cliente":
                object = this.gson.toJson(selectCliente(id));
                break;

            case "Detalle_Factura":
                object = this.gson.toJson(selectDetalle_Factura(Integer.parseInt(id)));
                break;

            case "Localidad":
                object = this.gson.toJson(selectLocalidad(Integer.parseInt(id)));
                break;

            case "Producto":
                object = this.gson.toJson(selectProducto(Integer.parseInt(id)));
                break;

            case "Proveedor":
                object = this.gson.toJson(selectProveedor(id));
                break;

            case "Unidad":
                object = this.gson.toJson(selectUnidad(Integer.parseInt(id)));
                break;

            case "Usuario":
                object = this.gson.toJson(selectUsuario(id));
                break;

            default:
                object = "El criterio de búsqueda no coincidió con ninguno de los casos";
        }

        return object;
    }

    public boolean UPDATE(@NonNull String tabla, String object, int actualizado) {

        boolean result;

        switch (tabla) {

            case "Cabecera_Factura":
                result = updateCabecera_Factura(this.gson.fromJson(object, Cabecera_factura.class), actualizado);
                break;

            case "Cliente":
                result = updateCliente(this.gson.fromJson(object, Cliente.class), actualizado);
                break;

            case "Detalle_Factura":
                result = updateDetalle_Factura(this.gson.fromJson(object, Detalle_factura.class), actualizado);
                break;

            case "Localidad":
                result = updateLocalidad(this.gson.fromJson(object, Localidad.class), actualizado);
                break;

            case "Producto":
                result = updateProducto(this.gson.fromJson(object, Producto.class), actualizado);
                break;

            case "Proveedor":
                result = updateProveedor(this.gson.fromJson(object, Proveedor.class), actualizado);
                break;

            case "Unidad":
                result = updateUnidad(this.gson.fromJson(object, Unidad.class), actualizado);
                break;

            case "Usuario":
                result = updateUsuario(this.gson.fromJson(object, Usuario.class), actualizado);
                break;

            case "Modo":
                result = updateModo(this.gson.fromJson(object, Modo.class), actualizado);
                break;

            default:
                result = false;
                break;
        }

        return result;
    }

    public boolean DELETE(@NonNull String tabla, String id, int actualizado) {

        boolean result;

        switch (tabla) {

            case "Cabecera_Factura":
                result = deleteCabecera_Factura(Integer.parseInt(id), actualizado);
                break;

            case "Cliente":
                result = deleteCliente(id, actualizado);
                break;

            case "Detalle_Factura":
                result = deleteDetalle_Factura(Integer.parseInt(id), actualizado);
                break;

            case "Localidad":
                result = deleteLocalidad(Integer.parseInt(id), actualizado);
                break;

            case "Producto":
                result = deleteProducto(Integer.parseInt(id), actualizado);
                break;

            case "Proveedor":
                result = deleteProveedor(id, actualizado);
                break;

            case "Unidad":
                result = deleteUnidad(Integer.parseInt(id), actualizado);
                break;

            case "Usuario":
                result = deleteUsuario(id, actualizado);
                break;

            default:
                result = false;
                break;
        }

        return result;
    }
    /* ****************************************************************************************************************************************** */
}
