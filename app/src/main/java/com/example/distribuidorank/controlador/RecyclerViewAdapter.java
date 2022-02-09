package com.example.distribuidorank.controlador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.distribuidorank.R;
import com.example.distribuidorank.modelo.Producto;
import com.example.distribuidorank.modelo.Targeta;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataObjectHolder> {
    private ArrayList<Targeta> mDataset;
    private static MyClickListener myClickListener;
    private ArrayList<Producto> listaProductos;
    private DataObjectHolder dataObjectHolder;
    private int posicion;
    private double utiliGeneral;


    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        private EditText cantidad;
        private EditText precioVenta;
        private TextView precioCompra;
        private TextView nombre;
        private TextView porcentajeUtilidad;
        private TextView dineroUtilidad;
        private Button btnCalcular;
        private TextInputEditText totalGeneral;
        private TextInputEditText utilidadGeneral;


        public DataObjectHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            /*btnCalcular = view.findViewById(R.id.btnCardCalcular);
            btnCalcular.setOnClickListener(v -> {
                cantidad.getText();

                utiliGeneral = Double.parseDouble(String.valueOf(precioCompra.getText()))-Double.parseDouble(String.valueOf(precioCompra.getText()));
                utilidadGeneral.setText(String.valueOf(utiliGeneral));
                mostrarDatosDelPedidoEnLaUi();
            });*/

        }

        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public RecyclerViewAdapter(ArrayList<Targeta> myDataset, ArrayList<Producto> arrayListProductos) {
        mDataset = myDataset;
        listaProductos = arrayListProductos;
    }

    public RecyclerViewAdapter() {
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_card, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        dataObjectHolder = holder;
        posicion = position;
        mostrarDatosDelPedidoEnLaUi();
    }

    public void mostrarDatosDelPedidoEnLaUi(){
        double precioVenta = Double.parseDouble(mDataset.get(posicion).getPrecioVenta());
        double precioCompra = Double.parseDouble(mDataset.get(posicion).getPrecioCompra());
        double dineroUtilidad = precioVenta-precioCompra;
        dataObjectHolder.nombre.setText(mDataset.get(posicion).getNombre());
        dataObjectHolder.precioCompra.setText(mDataset.get(posicion).getPrecioCompra());
        dataObjectHolder.precioVenta.setText(mDataset.get(posicion).getPrecioVenta());
        dataObjectHolder.porcentajeUtilidad.setText(mDataset.get(posicion).getPorcentajeUtilidad());
        dataObjectHolder.dineroUtilidad.setText(String.valueOf(dineroUtilidad));
        dataObjectHolder.cantidad.setText("0");
    }

    public void addItem(Targeta dataObj, int index) {

        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
