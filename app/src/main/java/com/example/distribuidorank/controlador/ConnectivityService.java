package com.example.distribuidorank.controlador;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

public class ConnectivityService {
    /**
     * Método para verificar si el dispositivo cuenta con conexion a internet.
     *
     * @return falso o verdadero.
     */
    public boolean stateConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connMgr).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
