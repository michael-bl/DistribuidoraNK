package com.example.distribuidorank.controlador;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormatoFecha {
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public FormatoFecha(){
    }

    public String getFechaConFormato(Context context) {
        try {
            return this.simpleDateFormat.format(this.calendar.getTime());
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
