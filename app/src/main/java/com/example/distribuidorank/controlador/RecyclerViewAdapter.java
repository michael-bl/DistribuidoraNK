package com.example.distribuidorank.controlador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.distribuidorank.R;
import com.example.distribuidorank.modelo.Producto;
import com.example.distribuidorank.modelo.Targeta;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataObjectHolder> {
    private ArrayList<Targeta> mDataset;
    private static MyClickListener myClickListener;
    private ArrayList<Producto> listaProductos;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        private TextView nombre;
        private TextView precioVenta;
        private TextView precioCompra;
        private TextView utilidad;
        private Button btnAgregar;

        public DataObjectHolder(View view) {
            super(view);
            nombre = view.findViewById(R.id.txtCardNombre);
            precioCompra = view.findViewById(R.id.txtCardPrecioCompra);
            precioVenta = view.findViewById(R.id.txtCardPrecioVenta);
            utilidad = view.findViewById(R.id.txtCardUtilidad);
            btnAgregar = view.findViewById(R.id.btnCardComprar);
            btnAgregar.setOnClickListener(v -> {

            });

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_card, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.nombre.setText(mDataset.get(position).getNombre());
        holder.precioCompra.setText(mDataset.get(position).getPrecioCompra());
        holder.precioVenta.setText(mDataset.get(position).getPrecioVenta());
        holder.utilidad.setText(mDataset.get(position).getUtilidad());
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
