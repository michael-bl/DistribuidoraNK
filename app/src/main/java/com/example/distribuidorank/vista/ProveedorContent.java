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
import com.example.distribuidorank.modelo.Proveedor;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProveedorContent extends AppCompatActivity {
    private ArrayList<String> stringIdsListaProveedores;
    private ArrayList<String> idProveedorSeleccionado;
    private TextInputEditText tvBuscarProveedor;
    private List<Proveedor> listaProveedores;
    private ExistDataBaseSqlite existDb;
    private Conexiones conexiones;
    // MultiSelect list adapter
    private ListView listviewProveedores;
    private SelectionAdapter mAdapter;
    private Proveedor proveedor;
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
        setContentView(R.layout.content_proveedor);

        idProveedorSeleccionado = new ArrayList<>();
        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_opciones, null);
        builder = new AlertDialog.Builder(this);
        intent = new Intent(this, ProveedorActivity.class);
        bundle = new Bundle();
        proveedor = new Proveedor();

        // Extrayendo el extra de tipo cadena
        proveedor = (Proveedor) bundle.getSerializable("proveedor");

        // Evento boton siguiente, hacia ProveedorActivity.class
        Button btnSiguiente = findViewById(R.id.btnNextContentProveedor);
        btnSiguiente.setOnClickListener(v -> {
            if (proveedor.getAccion() == 0) {
                dialogAccion().show();
            } else {
                proveedor.setAccion(0);
                proveedor.setEstado(1);
                bundle.putSerializable("proveedor", proveedor);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // Solicitar proveedores
        existDb = new ExistDataBaseSqlite();
        if (existDb.existeDataBase())
            getProveedoresLocal();
        else getProveedoresRemoto();
    }

    /**
     * MsgDialogDBLocal captura opcion - Actualizar o eliminar objeto
     */
    private AlertDialog dialogAccion() {
        view = inflater.inflate(R.layout.dialog_opciones, null);
        final Button btnActualizar = view.findViewById(R.id.btnNuevo);
        btnActualizar.setText("Actualizar");
        final Button btnEliminar = view.findViewById(R.id.btnMasOpciones);
        btnEliminar.setText("Eliminar");
        btnActualizar.setOnClickListener(v -> {
            proveedor.setAccion(1);
            proveedor.setEstado(1);
            bundle.putSerializable("proveedor", proveedor);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btnEliminar.setOnClickListener(v -> {
            proveedor.setAccion(2);
            proveedor.setEstado(0);
            bundle.putSerializable("proveedor", proveedor);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        builder.setView(view).setTitle("Elija una opción!").setPositiveButton("", (dialog, id) -> {
            ((ViewGroup) drawerLayout.getParent()).removeView(view);
        }).setNegativeButton("Cancelar", (dialog, which) -> ((ViewGroup) drawerLayout.getParent()).removeView(view));
        return builder.create();
    }

    /**
     * Obtiene lista de proveedores de bd local
     */
    private void getProveedoresLocal() {
        try {
            conexiones = new Conexiones(this);
            List<Proveedor> listaProveedores = new ArrayList<>(conexiones.getProveedores());
            stringIdsListaProveedores = new ArrayList<>();
            for (Proveedor proveedor : listaProveedores) {
                stringIdsListaProveedores.add(proveedor.getId() + "-" + proveedor.getNombre());
            }
            setListaProveedores(stringIdsListaProveedores, listaProveedores);
        } catch (NullPointerException npe) {
            Toast.makeText(ProveedorContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JsonSyntaxException jse) {
            Toast.makeText(ProveedorContent.this, "Error, verifique por favor: " + jse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Obtiene lista de proveedores de bd remota
     */
    public void getProveedoresRemoto() {
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(this)) {
            Call<JsonArray> requestProveedores = ApiUtils.getApiServices().getProveedores();
            requestProveedores.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(ProveedorContent.this, response.message() + "Error al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        JsonArray listaPro = response.body();
                        stringIdsListaProveedores = new ArrayList<>();
                        listaProveedores = new ArrayList<>();
                        for (int i = 0; i < listaPro.size(); i++) {
                            Proveedor p = new Proveedor();
                            stringIdsListaProveedores.add(i, listaPro.getAsJsonObject().get("id").getAsInt() + "-" + listaPro.getAsJsonObject().get("nombre").toString());
                            p.setId(listaPro.getAsJsonObject().get("id").toString());
                            p.setNombre(listaPro.getAsJsonObject().get("nombre").toString());
                            p.setTelefono(listaPro.getAsJsonObject().get("telefono").toString());
                            p.setEmail(listaPro.getAsJsonObject().get("email").toString());
                            p.setEstado(listaPro.getAsJsonObject().get("estado").getAsInt());
                        }
                        setListaProveedores(stringIdsListaProveedores, listaProveedores);
                        Toast.makeText(ProveedorContent.this, "Datos cargados exitosamente!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(ProveedorContent.this, t.getMessage() + "La peticion falló!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ProveedorContent.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Carga datos de proveedores en listviewproveedores
     */
    private void setListaProveedores(ArrayList<String> stringListaProveedor, List<Proveedor> c) {
        try {
            this.listaProveedores = c;
            listviewProveedores = findViewById(R.id.lvContentProveedor);
            mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringListaProveedor);
            listviewProveedores.setAdapter(mAdapter);
            onTextChanged();
            setUpActionBar();
        } catch (NullPointerException npe) {
            Toast.makeText(ProveedorContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Filtro sobre el Listview de proveedores
     */
    private void onTextChanged() {
        // Arraylist con datos filtrados
        final ArrayList<String> array_sort = new ArrayList<>();
        mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringIdsListaProveedores);
        listviewProveedores = findViewById(R.id.lvContentProveedor);
        listviewProveedores.setAdapter(mAdapter);
        // Inicializamos textview de busqueda
        tvBuscarProveedor = findViewById(R.id.txtBuscarContentProveedor);
        tvBuscarProveedor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = tvBuscarProveedor.getText().length();
                array_sort.clear();
                for (int i = 0; i < stringIdsListaProveedores.size(); i++) {
                    if (textlength <= stringIdsListaProveedores.get(i).length()) {
                        if (stringIdsListaProveedores.get(i).contains(tvBuscarProveedor.getText().toString())) {
                            array_sort.add(stringIdsListaProveedores.get(i));
                        }
                    }
                }
                listviewProveedores.setAdapter(new SelectionAdapter(ProveedorContent.this, android.R.layout.simple_list_item_1, android.R.id.text1, array_sort));
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

        listviewProveedores.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listviewProveedores.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                // If element is checked, it is added to selection; if not, it's deleted
                if (checked) {
                    idProveedorSeleccionado.add(listviewProveedores.getItemAtPosition(position).toString());
                    mAdapter.setNewSelection(position);
                    proveedor = listaProveedores.get(position);
                } else {
                    idProveedorSeleccionado.remove(listviewProveedores.getItemAtPosition(position).toString());
                    mAdapter.removeSelection(position);
                }
                mode.setTitle(mAdapter.getSelectionCount() + " Items seleccionados");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // CAB menu options
                if (item.getItemId() == R.id.delete) {
                    Toast.makeText(ProveedorContent.this,
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