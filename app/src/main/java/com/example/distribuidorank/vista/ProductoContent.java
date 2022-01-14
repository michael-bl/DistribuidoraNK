package com.example.distribuidorank.vista;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.distribuidorank.Database.Conexiones;
import com.example.distribuidorank.Database.ExistDataBaseSqlite;
import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.ApiUtils;
import com.example.distribuidorank.controlador.ConnectivityService;
import com.example.distribuidorank.controlador.SelectionAdapter;
import com.example.distribuidorank.modelo.Producto;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoContent extends AppCompatActivity {
    private ArrayList<String> idProductoSeleccionado;
    private ArrayList<String> stringIdsListaProductos;
    private TextInputEditText tvBuscarProducto;
    private List<Producto> listaProductos;
    private ExistDataBaseSqlite existDb;
    private Conexiones conexiones;
    // MultiSelect list adapter
    private ListView listviewProductos;
    private SelectionAdapter mAdapter;
    private Producto producto;
    // Para mostrar alertDialog
    private NavigationView navigationView;
    private AlertDialog.Builder builder;
    private DrawerLayout drawerLayout;
    private LayoutInflater inflater;
    private Intent intent;
    private Bundle bundle;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_producto);

        idProductoSeleccionado = new ArrayList<>();
        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_opciones, null);
        builder = new AlertDialog.Builder(this);
        intent = new Intent(this, ProductoActivity.class);
        bundle = new Bundle();
        producto = new Producto();

        // Extrayendo el extra de tipo cadena
        producto = (Producto) bundle.getSerializable("producto");

        // Evento boton siguiente, hacia ProductoActivity.class
        Button btnSiguiente = findViewById(R.id.btnNextContentProducto);
        btnSiguiente.setOnClickListener(v -> {
            if (producto.getAccion() == 0) {
                dialogOpcionCrudSobreProducto().show();
            } else {
                producto.setAccion(0);
                producto.setEstado(1);
                bundle.putSerializable("producto", producto);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        existDb = new ExistDataBaseSqlite();
        if (existDb.existeDataBaseLocal())
            getProductosDeDbLocal();
        else getProductosDeDbRemoto();
    }

    private AlertDialog dialogOpcionCrudSobreProducto() {
        view = inflater.inflate(R.layout.dialog_opciones, null);

        final Button btnActualizar = view.findViewById(R.id.btnNuevo);
        btnActualizar.setText("Actualizar");

        final Button btnEliminar = view.findViewById(R.id.btnMasOpciones);
        btnEliminar.setText("Eliminar");

        btnActualizar.setOnClickListener(v -> {
            producto.setAccion(1);
            producto.setEstado(1);
            bundle.putSerializable("producto", producto);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btnEliminar.setOnClickListener(v -> {
            producto.setAccion(2);
            producto.setEstado(0);
            bundle.putSerializable("producto", producto);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        builder.setView(view).setTitle("Escoga una opción!").setPositiveButton("", (dialog, id) -> {
            ((ViewGroup) drawerLayout.getParent()).removeView(view);
        }).setNegativeButton("Cancelar", (dialog, which) -> ((ViewGroup) drawerLayout.getParent()).removeView(view));
        return builder.create();
    }

    private void getProductosDeDbLocal() {
        try {
            conexiones = new Conexiones(ProductoContent.this);
            List<Producto> listaProductos = new ArrayList<>(conexiones.getProductos());
            stringIdsListaProductos = new ArrayList<>();
            for (Producto producto : listaProductos) {
                stringIdsListaProductos.add(producto.getId() + "-" + producto.getDescripcion());
            }
            llenarListViewProductos(stringIdsListaProductos, listaProductos);
        } catch (NullPointerException npe) {
            Toast.makeText(ProductoContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JsonSyntaxException jse) {
            Toast.makeText(ProductoContent.this, "Error, verifique por favor: " + jse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getProductosDeDbRemoto() {
        ConnectivityService estaConectado = new ConnectivityService();
        if (estaConectado.stateConnection(this)) {
            Call<JsonArray> requestProductos = ApiUtils.getApiServices().getProductos();
            requestProductos.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(ProductoContent.this, response.message() + "Error al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Manejar datos obtenidos en la petición
                        JsonArray listaPro = response.body();
                        stringIdsListaProductos = new ArrayList<>();
                        listaProductos = new ArrayList<>();
                        for (int j = 0; j < listaPro.size(); j++) {
                            Producto p = new Producto();
                            stringIdsListaProductos.add(j, listaPro.get(j).getAsJsonObject().get("id").getAsInt() + "-" + listaPro.get(j).getAsJsonObject().get("descripcion").toString());
                            p.setId(listaPro.get(j).getAsJsonObject().get("id").getAsInt());
                            p.setDescripcion(listaPro.get(j).getAsJsonObject().get("descripcion").toString());
                            p.setFk_familia(listaPro.get(j).getAsJsonObject().get("fk_unidad").getAsInt());
                            p.setPrecio_compra(listaPro.get(j).getAsJsonObject().get("precio_compra").getAsFloat());
                            p.setPrecio_venta(listaPro.get(j).getAsJsonObject().get("precio_venta").getAsFloat());
                            p.setUtilidad(listaPro.get(j).getAsJsonObject().get("utilidad").getAsFloat());
                            p.setDetalle(listaPro.get(j).getAsJsonObject().get("detalle").toString());
                            listaProductos.add(p);
                        }
                        llenarListViewProductos(stringIdsListaProductos, listaProductos);
                        Toast.makeText(ProductoContent.this, "Datos cargados exitosamente!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(ProductoContent.this, "La peticion falló: " + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ProductoContent.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    private void llenarListViewProductos(ArrayList<String> listaproducto, List<Producto> listaProducto) {
        try {
            this.listaProductos = listaProducto;
            listviewProductos = findViewById(R.id.lvContentProductos);
            mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaproducto);
            listviewProductos.setAdapter(mAdapter);
            onTextChanged();
            setUpActionBar();
        } catch (NullPointerException npe) {
            Toast.makeText(ProductoContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void onTextChanged() {
        // Arraylist con datos filtrados
        final ArrayList<String> array_sort = new ArrayList<>();
        mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringIdsListaProductos);
        listviewProductos = findViewById(R.id.lvContentProductos);
        listviewProductos.setAdapter(mAdapter);
        // Inicializamos textview de busqueda
        tvBuscarProducto = findViewById(R.id.txtBuscarContentProducto);
        tvBuscarProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = Objects.requireNonNull(tvBuscarProducto.getText()).length();
                array_sort.clear();
                for (int i = 0; i < stringIdsListaProductos.size(); i++) {
                    if (textlength <= stringIdsListaProductos.get(i).length()) {
                        if (stringIdsListaProductos.get(i).contains(tvBuscarProducto.getText().toString())) {
                            array_sort.add(stringIdsListaProductos.get(i));
                        }
                    }
                }
                listviewProductos.setAdapter(new SelectionAdapter(ProductoContent.this, android.R.layout.simple_list_item_1, android.R.id.text1, array_sort));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUpActionBar() {

        listviewProductos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listviewProductos.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                // If element is checked, it is added to selection; if not, it's deleted
                if (checked) {
                    idProductoSeleccionado.add(listviewProductos.getItemAtPosition(position).toString());
                    mAdapter.setNewSelection(position);
                    //con position obtenemos el producto de la listaProductos, el indice es el mismo
                    producto = listaProductos.get(position);
                } else {
                    idProductoSeleccionado.remove(listviewProductos.getItemAtPosition(position).toString());
                    mAdapter.removeSelection(position);
                }
                mode.setTitle(mAdapter.getSelectionCount() + " Items seleccionados");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // CAB menu options
                if (item.getItemId() == R.id.delete) {
                    Toast.makeText(ProductoContent.this,
                            mAdapter.getSelectionCount() + " Items eliminados",
                            Toast.LENGTH_LONG).show();
                    mAdapter.clearSelection();
                    mode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // CAB is initialized
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.main_cab, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mAdapter.clearSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });
    }
}