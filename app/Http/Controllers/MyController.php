<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class MyController extends Controller
{

    public function login($usuario, $pass)
    {
        $resultado = DB::select('select id, pass, nombre from usuario where id=(?) and pass=(?)', [$usuario, $pass]);
        return response()->json_string = json_encode($resultado);
    }


    //********************************CRUD PROVEEDORES********************************/

    public function nuevoProveedor($id, $nombre, $telefono, $email)
    {
        $resultado = DB::insert('insert into proveedor (id, nombre, telefono, email) values (?, ?, ?, ?)', [$id, $nombre, $telefono, $email]);
        return response()->json_string = json_encode($resultado);
    }

    public function getProveedores()
    {
        $resultado = DB::select('select * from proveedor');
        return response()->json_string = json_encode($resultado);
    }

    public function getProveedorXid($id)
    {
        $resultado = DB::select('select * from proveedor where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    public function actualizaProveedor($id, $nombre, $telefono, $email)
    {
        $resultado = DB::update('update proveedor set nombre = ?, telefono = ?, email = ? where id = ?', [$nombre, $telefono, $email, $id]);
        return response()->json_string = json_encode($resultado);
    }

    public function eliminaProveedor($id)
    {
        $resultado = DB::delete('delete proveedor where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    //********************************CRUD USUARIOS********************************/
    public function nuevoUsuario($id, $fk_localidad, $nombre, $pass, $telefono, $email, $direccion)
    {
        $resultado = DB::insert('insert into usuario (id, fk_localidad, nombre, pass, telefono, email, direccion) values (?, ?, ?, ?, ?, ?, ?)', [$id, $fk_localidad, $nombre, $pass, $telefono, $email, $direccion]);
        return response()->json_string = json_encode($resultado);
    }

    public function getUsuarios()
    {
        $resultado = DB::select('select * from usuario');
        return response()->json_string = json_encode($resultado);
    }

    public function getUsuarioXid($id)
    {
        $resultado = DB::select('select * from usuario where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    public function actualizaUsuario($fk_localidad, $nombre, $pass, $telefono, $email, $direccion, $id)
    {
        $resultado = DB::update('update usuario set fk_localidad = ?, nombre = ?, pass = ?, telefono = ?, email = ?, direccion = ? where id = ?', [$fk_localidad, $nombre, $pass, $telefono, $email, $direccion, $id]);
        return response()->json_string = json_encode($resultado);
    }

    public function eliminaUsuario($id)
    {
        $resultado = DB::delete('delete usuario where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }


    //********************************CRUD PRODUCTOS********************************/
    public function nuevoProducto($id, $fk_familia, $nombre, $precio, $utilidad)
    {
        $resultado = DB::insert('insert into producto (id, fk_familia, nombre, precio, utilidad) values (?, ?, ?, ?, ?)', [$id, $fk_familia, $nombre, $precio, $utilidad]);
        return response()->json_string = json_encode($resultado);
    }

    public function getProductos()
    {
        $resultado = DB::select('select * from producto');
        return response()->json_string = json_encode($resultado);
    }

    public function getProductoXid($id)
    {
        $resultado = DB::select('select * from producto where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    public function actualizaProducto($fk_familia, $nombre, $precio, $utilidad, $id)
    {
        $resultado = DB::update('update producto set fk_familia = ?, nombre = ?, precio = ?, utilidad = ? where id = ?', [$fk_familia, $nombre, $precio, $utilidad, $id]);
        return response()->json_string = json_encode($resultado);
    }

    public function eliminaProducto($id)
    {
        $resultado = DB::delete('delete producto where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }



    //********************************CRUD CLIENTES********************************/
    public function nuevoCliente($id, $fk_localidad, $nombre, $telefono, $email, $direccion)
    {
        $resultado = DB::insert('insert into cliente (id, fk_localidad, nombre, telefono, email, direccion) values (?, ?, ?, ?, ?, ?)', [$id, $fk_localidad, $nombre, $telefono, $email, $direccion]);
        return response()->json_string = json_encode($resultado);
    }

    public function getClientes()
    {
        $resultado = DB::select('select * from cliente');
        return response()->json_string = json_encode($resultado);
    }

    public function getClienteXid($id)
    {
        $resultado = DB::select('select * from cliente where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    public function actualizaCliente($fk_localidad, $nombre, $telefono, $email, $direccion, $id)
    {
        $resultado = DB::update('update cliente set fk_localidad = ?, nombre = ?, telefono = ?, email = ?, direccion = ? where id = ?', [$fk_localidad, $nombre, $telefono, $email, $direccion, $id]);
        return response()->json_string = json_encode($resultado);
    }

    public function eliminaCliente($id)
    {
        $resultado = DB::delete('delete cliente where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }


    //********************************CRUD LOCALIDAD********************************/
    public function nuevaLocalidad($id, $localidad)
    {
        $resultado = DB::insert('insert into localidad (id, localidad) values (?, ?)', [$id, $localidad]);
        return response()->json_string = json_encode($resultado);
    }

    public function getLocalidad()
    {
        $resultado = DB::select('select * from localidad');
        return response()->json_string = json_encode($resultado);
    }

    public function getLocalidadXid($id)
    {
        $resultado = DB::select('select * from localidad where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    public function actualizaLocalidad($localidad, $id)
    {
        $resultado = DB::update('update localidad set localidad = ? where id = ?', [$localidad, $id]);
        return response()->json_string = json_encode($resultado);
    }

    public function eliminaLocalidad($id)
    {
        $resultado = DB::delete('delete localidad where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    //********************************CRUD UNIDAD DE MEDIDA********************************/
    public function nuevaUnidad($id, $detalle)
    {
        $resultado = DB::insert('insert into unidad (id, detalle) values (?, ?)', [$id, $detalle]);
        return response()->json_string = json_encode($resultado);
    }

    public function getUnidad()
    {
        $resultado = DB::select('select * from unidad');
        return response()->json_string = json_encode($resultado);
    }

    public function getUnidadXid($id)
    {
        $resultado = DB::select('select * from unidad where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    public function actualizaUnidad($detalle, $id)
    {
        $resultado = DB::update('update unidad set detalle = ? where id = ?', [$detalle, $id]);
        return response()->json_string = json_encode($resultado);
    }

    public function eliminaUnidad($id)
    {
        $resultado = DB::delete('delete unidad where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }


    //********************************CRUD CABECERA FACTURA********************************/
    public function nuevaCabeceraFactura($id, $tipo, $fk_cliente, $fecha)
    {
        $resultado = DB::insert('insert into cabecera_factura (id, tipo, fk_cliente, fecha) values (?, ?, ?, ?)', [$id, $tipo, $fk_cliente, $fecha]);
        return response()->json_string = json_encode($resultado);
    }

    public function getCabeceraFactura()
    {
        $resultado = DB::select('select * from cabecera_factura');
        return response()->json_string = json_encode($resultado);
    }

    public function getCabeceraFacturaXid($id)
    {
        $resultado = DB::select('select * from cabecera_factura where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    public function actualizaCabeceraFactura($tipo, $fk_cliente, $fecha, $id)
    {
        $resultado = DB::update('update cabecera_factura set tipo = ?, fk_cliente = ?, fecha = ? where id = ?', [$tipo, $fk_cliente, $fecha, $id]);
        return response()->json_string = json_encode($resultado);
    }

    public function eliminaCabeceraFactura($id)
    {
        $resultado = DB::delete('delete cabecera_factura where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    //********************************CRUD DETALLE FACTURA********************************/
    public function nuevoDetalleFactura($id, $fk_cabecera, $fk_producto, $utilidad, $precio_k, $precio_cliente)
    {
        $resultado = DB::insert('insert into detalle_factura (id, fk_cabecera, fk_producto, utilidad, precio_k, precio_cliente) values (?, ?, ?, ?, ?, ?)', [$id, $fk_cabecera, $fk_producto, $utilidad, $precio_k, $precio_cliente]);
        return response()->json_string = json_encode($resultado);
    }

    public function getDetalleFactura()
    {
        $resultado = DB::select('select * from detalle_factura');
        return response()->json_string = json_encode($resultado);
    }

    public function getDetalleFacturaXid($id)
    {
        $resultado = DB::select('select * from detalle_factura where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    public function actualizaDetalleFactura($fk_cabecera, $fk_producto, $utilidad, $precio_k, $precio_cliente, $id)
    {
        $resultado = DB::update('update detalle_factura set fk_cabecera = ?, fk_producto= ?, utilidad= ?, precio_k= ?, precio_cliente = ? where id = ?', [$fk_cabecera, $fk_producto, $utilidad, $precio_k, $precio_cliente, $id]);
        return response()->json_string = json_encode($resultado);
    }

    public function eliminaDetalleFactura($id)
    {
        $resultado = DB::delete('delete detalle_factura where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }
}
