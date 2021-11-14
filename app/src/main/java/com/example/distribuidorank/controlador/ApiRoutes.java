package com.example.distribuidorank.controlador;

import com.example.distribuidorank.modelo.Cliente;
import com.example.distribuidorank.modelo.Usuario;
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

    // Obtiene todas las actividades o labores
    @GET("clientes")
    Call<List<Cliente>> getCliente();

    // Guarda nuevo cliente
    @Headers({"Accept: application/json; Content-Type: application/json; charset=utf-8; deviceplatform:android; User-Agent:Mozilla/5.0"})
    @POST("nuevocliente2")
    Call<JsonObject> nuevoCliente(@Body Cliente c);

    // Actualiza o elimina cliente
      @GET("accioncliente/{id}/{localidad}/{nombre}/{telefono}/{email}/{direccion}/{accion}")
      Call<JsonObject> accionCliente(@Path("id") String a,
                                   @Path("localidad") String b,
                                   @Path("nombre") String c,
                                   @Path("telefono") String e,
                                   @Path("email") String d,
                                   @Path("direccion") String f,
                                   @Path("accion") int g);

    // Obtiene lista de empleados
    //@GET("employees")
    //Call<List<Empleado>> getEmployee();

    // Obtiene ultimos 5 reportes guardados por id encargado
    //@GET("lastreports/{id_manager}")
    //Call<List<RptGuardado>> getUltimoRptXEncargado(@Path("id_manager") String idManager);

    // Guarda
    //@Headers({"Accept: application/json; Content-Type: application/json; charset=utf-8; deviceplatform:android; User-Agent:Mozilla/5.0"})
    //@POST("newreport")
    //Call<JsonArray> postReport(@Body Reporte reportArray);
}
