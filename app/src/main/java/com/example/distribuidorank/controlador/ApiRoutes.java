package com.example.distribuidorank.controlador;

import com.example.distribuidorank.modelo.Cliente;
import com.example.distribuidorank.modelo.Usuario;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiRoutes {

    // Inicio de sesion
    @GET("login/{user}/{pass}")
    Call<List<Usuario>> login(@Path("user") String user, @Path("pass") String pass);

    // Retorna todas clientes
    @GET("clientes")
    Call<List<Cliente>> getClientes();

    // Guarda nuevo cliente
    @Headers({"Accept: application/json; Content-Type: application/json; " +
            "charset=utf-8; deviceplatform:android; User-Agent:Mozilla/5.0"})
    @POST("accioncliente2")
    Call<JsonObject> accionCliente2(@Body Cliente cliente);

    // Agrega nuevo, actualiza o desactiva cliente
      @GET("accioncliente/{id}/{localidad}/{nombre}/{telefono}/{email}/{direccion}/{accion}/{estado}")
      Call<JsonObject> accionCliente(@Path("id") String a,
                                   @Path("localidad") String b,
                                   @Path("nombre") String c,
                                   @Path("telefono") String e,
                                   @Path("email") String d,
                                   @Path("direccion") String f,
                                   @Path("accion") int g,
                                   @Path("direccion") int h);

    // Retorna lista de productos activos
    @GET("productos")
    Call<JsonArray> getProductos();
    // Agrega nuevo, actualiza o desactiva cliente
    @GET("accionproducto/{id}/{fk_unidad}/{descripcion}/{utilidad}/{precio_compra}/{precio_venta}/{accion}/{estado}")
    Call<JsonObject> accionProducto(@Path("id") int a,
                                   @Path("fk_unidad") String b,
                                   @Path("descripcion") String c,
                                   @Path("utilidad") String e,
                                   @Path("precio_compra") String d,
                                   @Path("precio_venta") String f,
                                   @Path("accion") int g,
                                   @Path("estado") int h);

    @GET("unidad")
    Call<JsonArray> getUnidades();
}
