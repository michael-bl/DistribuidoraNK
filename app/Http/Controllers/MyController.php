<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class MyController extends Controller
{

    public function login($usuario,  $pass)
    {
        $status = DB::select('select id, pass, nombre from usuario where id=(?) and PASS=(?)', [$usuario, $pass]);
        return response()->json_string = json_encode($status);
    }


    /** CRUD proveedores ---------------------------------------------*/

    public function nuevoProveedor($id, $nombre, $telefono, $email)
    {
        $resultado = DB::insert('insert into proveedor (id, nombre, telefono, email) values (?, ?, ?, ?)', [$id, $nombre, $telefono, $email]);
        return response()->json_string = json_encode($resultado);
    }

    public function getProveedores()
    {
        $resultado = DB::select('select * from proveedor',);
        return response()->json_string = json_encode($resultado);
    }

    public function getProveedorXid($id)
    {
        $resultado = DB::select('select * from proveedor where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    public function actualizarProveedor($id, $nombre, $telefono, $email)
    {
        $resultado = DB::update('update proveedor set nombre = ?, telefono = ?, email = ? where id = ?', [$nombre, $telefono, $email, $id]);
        return response()->json_string = json_encode($resultado);
    }

    public function eliminarProveedor($id)
    {
        $resultado = DB::delete('delete proveedor where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    /** CRUD empleados -----------------------------------------------*/
    public function nuevoEmpleado($id, $nombre, $telefono, $email)
    {
        $resultado = DB::insert('insert into proveedor (id, nombre, telefono, email) values (?, ?, ?, ?)', [$id, $nombre, $telefono, $email]);
        return response()->json_string = json_encode($resultado);
    }

    public function getEmpleados()
    {
        $resultado = DB::select('select * from proveedor',);
        return response()->json_string = json_encode($resultado);
    }

    public function getEmpleadoXid($id)
    {
        $resultado = DB::select('select * from proveedor where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }

    public function actualizarEmpleado($id, $nombre, $telefono, $email)
    {
        $resultado = DB::update('update proveedor set nombre = ?, telefono = ?, email = ? where id = ?', [$nombre, $telefono, $email, $id]);
        return response()->json_string = json_encode($resultado);
    }

    public function eliminarEmpleado($id)
    {
        $resultado = DB::delete('delete proveedor where id = ?', [$id]);
        return response()->json_string = json_encode($resultado);
    }
    /** CRUD productos -----------------------------------------------*/
    /** CRUD clientes ------------------------------------------------*/
    /** CRUD cargar datos --------------------------------------------*/
    /** CRUD facturas ------------------------------------------------*/

    public function insertNewReport(Request $data)
    {
        $reportArray = $data->reportArray;
        $result = [];
        if (is_array($reportArray)) {
            foreach ($reportArray as $key => $val) {
                $fk_id_empleado = $val['FK_ID_EMPLEADO'];
                $id_encargado = $val['ID_ENCARGADO'];
                $hora_entrada = $val['HORA_ENTRADA'];
                $hora_salida = $val['HORA_SALIDA'];
                $ordinaria = $val['ORDINARIAS'];
                $extra = $val['EXTRAS'];
                $cod_labor = $val['COD_LABOR'];
                $finca = $val['FINCA'];
                $lote = $val['LOTE'];
                $block = $val['BLOCK'];
                $fecha = $val['FECHA'];
                $detalle = $val['DETALLE'];
                $turno = $val['TURNO'];
                $rubro = $val['RUBRO'];
                $resultado = DB::select('call proc_inserta_reporte(?,?,?,?,?,?,?,?,?,?,?,?,?,?)', [$id_encargado, $fk_id_empleado, $fecha, $cod_labor, $hora_entrada, $hora_salida, $ordinaria, $extra,  $finca, $lote, $block, $detalle, $turno, $rubro]);

                if ($resultado == -1) {
                    $result = [
                        //"DETALLE" => mysqli_error($resultado)
                    ];
                }
                $result[$key] = $resultado;
            }
        } else {
            $result = ['DETALLE' => 'Objeto null'];
        }
        return  response()->json($result);
    }
}
