package com.example.distribuidorank.controlador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.distribuidorank.R;
import com.example.distribuidorank.modelo.Producto;

import java.util.ArrayList;

// este ejemplo se adapto desde https://www.geeksforgeeks.org/gridview-in-android-with-example/
public class CustomAdapter extends ArrayAdapter {
    ArrayList<Producto> listaProductos;
    public CustomAdapter(@NonNull Context context, @NonNull ArrayList<Producto> listProductos){
        super(context, 0, listProductos);
        listaProductos = listProductos;
    }

    @Override
    public int getCount() {
        return listaProductos.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view==null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.table_layout, parent, false);
        }
        return view;
    }
}
