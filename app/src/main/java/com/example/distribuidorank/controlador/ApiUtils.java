package com.example.distribuidorank.controlador;

public class ApiUtils {

    private ApiUtils() {}
    // La ip desde afuera es 186.26.118.98:3306
    // mbustos
    // 1234
    // public static final String BASE_URL = "186.26.118.98:3306";
    public static final String BASE_URL = "https://distribuidorank.herokuapp.com/";

    public static ApiRoutes getApiServices() {
        return RetrofitAdapter.getClient(BASE_URL).create(ApiRoutes.class);
    }

}
