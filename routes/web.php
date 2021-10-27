<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\MyController;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});
//********************************RUTA LOGIN***************************************/
Route::get('login/{user}/{pass}', [MyController::class, 'login']);

//********************************RUTAS PROVEEDORES********************************/
Route::get('nuevoproveedor', [MyController::class, 'nuevoProveedor']);
Route::get('proveedores', [MyController::class, 'getProveedores']);
Route::get('proveedorxid', [MyController::class, 'getProveedorXid']);
Route::get('actualizaproveedor', [MyController::class, 'actualizaProveedor']);
Route::get('eliminaproveedor/{id}', [MyController::class, 'eliminaProveedor']);

//********************************RUTAS USUARIOS***********************************/
Route::get('nuevousuario', [MyController::class, 'nuevoUsuario']);
Route::get('usuarios', [MyController::class, 'getUsuarios']);
Route::get('usuarioxid', [MyController::class, 'getUsuarioXid']);
Route::get('actualizausuario', [MyController::class, 'actualizaUsuario']);
Route::get('eliminausuario/{id}', [MyController::class, 'eliminaUsuario']);

//********************************RUTAS PRODUCTOS**********************************/
Route::get('nuevoproducto', [MyController::class, 'nuevoProducto']);
Route::get('productos', [MyController::class, 'getProductos']);
Route::get('productoxid', [MyController::class, 'getProductoXid']);
Route::get('actualizaproducto', [MyController::class, 'actualizaProducto']);
Route::get('eliminaproducto/{id}', [MyController::class, 'eliminaProducto']);

//********************************RUTAS CLIENTES***********************************/
Route::get('nuevocliente', [MyController::class, 'nuevoCliente']);
Route::get('clientes', [MyController::class, 'getClientes']);
Route::get('clientexid', [MyController::class, 'getClienteXid']);
Route::get('actualizacliente', [MyController::class, 'actualizaCliente']);
Route::get('eliminacliente/{id}', [MyController::class, 'eliminaCliente']);

//********************************RUTAS LOCALIDAD**********************************/
Route::get('nuevalocalidad', [MyController::class, 'nuevaLocalidad']);
Route::get('localidad', [MyController::class, 'getLocalidad']);
Route::get('localidadxid', [MyController::class, 'getLocalidadXid']);
Route::get('actualizalocalidad', [MyController::class, 'actualizaLocalidad']);
Route::get('eliminalocalidad/{id}', [MyController::class, 'eliminaLocalidad']);

//********************************RUTAS UNIDAD DE MEDIDA***************************/
Route::get('nuevaunidad', [MyController::class, 'nuevaUnidad']);
Route::get('unidad', [MyController::class, 'getUnidad']);
Route::get('unidadxid', [MyController::class, 'getUnidadXid']);
Route::get('actualizaunidad', [MyController::class, 'actualizaUnidad']);
Route::get('eliminaunidad/{id}', [MyController::class, 'eliminaUnidad']);

//********************************RUTAS CABECERA FACTURA***************************/
Route::get('nuevacabecerafactura', [MyController::class, 'nuevaCabeceraFactura']);
Route::get('cabecerafactura', [MyController::class, 'getCabeceraFactura']);
Route::get('cabecerafacturaxid', [MyController::class, 'getCabeceraFacturaXid']);
Route::get('actualizacabecerafactura', [MyController::class, 'actualizaCabeceraFactura']);
Route::get('eliminacabecerafactura/{id}', [MyController::class, 'eliminaCabeceraFactura']);

//********************************RUTAS DETALLE FACTURA****************************/
Route::get('nuevodetallefactura', [MyController::class, 'nuevoDetalleFactura']);
Route::get('detallefactura', [MyController::class, 'getDetalleFactura']);
Route::get('detallefacturaxid', [MyController::class, 'getDetalleFacturaXid']);
Route::get('actualizadetallefactura', [MyController::class, 'actualizaDetalleFactura']);
Route::get('eliminadetallefactura/{id}', [MyController::class, 'eliminaDetalleFactura']);
