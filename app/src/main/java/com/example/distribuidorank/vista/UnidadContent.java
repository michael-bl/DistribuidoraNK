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
import com.example.distribuidorank.modelo.Unidad;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnidadContent extends AppCompatActivity {
    private ArrayList<String> stringIdsListaUnidades;
    private ArrayList<String> idUnidadSeleccionada;
    private TextInputEditText tvBuscarUnidad;
    private ExistDataBaseSqlite existDb;
    private List<Unidad> listaUnidades;
    private Conexiones conexiones;
    // MultiSelect list adapter
    private SelectionAdapter mAdapter;
    private ListView listviewUnidad;
    private Unidad unidad;
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
        setContentView(R.layout.content_unidad);

        idUnidadSeleccionada = new ArrayList<>();
        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_opciones, null);
        builder = new AlertDialog.Builder(this);
        intent = new Intent(this, UnidadActivity.class);
        bundle = new Bundle();
        unidad = new Unidad();

        //Extrayendo el extra de tipo cadena
        unidad = (Unidad) bundle.getSerializable("unidad");

        Button btnSiguiente = findViewById(R.id.btnNextContentUnidad);
        btnSiguiente.setOnClickListener(v -> {
            if (unidad.getAccion() == 0) {
                dialogOpcionCrudSobreUnidad().show();
            } else {
                unidad.setAccion(0);
                unidad.setEstado(1);
                bundle.putSerializable("unidad", unidad);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        existDb = new ExistDataBaseSqlite();
        if (existDb.existeDataBaseLocal())
            getUnidadesDeDbLocal();
        else getUnidadesDeDbRemoto();
    }

    private AlertDialog dialogOpcionCrudSobreUnidad() {
        view = inflater.inflate(R.layout.dialog_opciones, null);
        final Button btnActualizar = view.findViewById(R.id.btnNuevo);
        btnActualizar.setText("Actualizar");
        final Button btnEliminar = view.findViewById(R.id.btnMasOpciones);
        btnEliminar.setText("Eliminar");
        btnActualizar.setOnClickListener(v -> {
            unidad.setAccion(1);
            unidad.setEstado(1);
            bundle.putSerializable("unidad", unidad);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btnEliminar.setOnClickListener(v -> {
            unidad.setAccion(2);
            unidad.setEstado(0);
            bundle.putSerializable("unidad", unidad);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        builder.setView(view).setTitle("Elija una opción!").setPositiveButton("", (dialog, id) -> {
            ((ViewGroup) drawerLayout.getParent()).removeView(view);
        });
        return builder.create();
    }

    private void getUnidadesDeDbLocal() {
        try {
            conexiones = new Conexiones(this);
            List<Unidad> listaUnidades = new ArrayList<>(conexiones.getUnidades());
            stringIdsListaUnidades = new ArrayList<>();
            for (Unidad unidad : listaUnidades) {
                stringIdsListaUnidades.add(unidad.getId() + "-" + unidad.getDetalle());
            }
            llenarListViewUnidades(stringIdsListaUnidades, listaUnidades);
        } catch (NullPointerException npe) {
            Toast.makeText(UnidadContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JsonSyntaxException jse) {
            Toast.makeText(UnidadContent.this, "Error, verifique por favor: " + jse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getUnidadesDeDbRemoto() {
        ConnectivityService estaConectado = new ConnectivityService();
        if (estaConectado.stateConnection(this)) {
            Call<JsonArray> requestLastReports = ApiUtils.getApiServices().getUnidades();
            requestLastReports.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(UnidadContent.this, response.message() + "Error al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        JsonArray arrayUnidades = response.body();
                        stringIdsListaUnidades = new ArrayList<>();
                        for (int i = 0; i < listaUnidades.size(); i++) {
                            unidad = new Unidad();
                            stringIdsListaUnidades.add(i, arrayUnidades.getAsJsonObject().get("id").getAsInt() + "-" + arrayUnidades.getAsJsonObject().get("detalle").getAsString());
                            unidad.setId(arrayUnidades.getAsJsonObject().get("id").getAsInt());
                            unidad.setDetalle(arrayUnidades.getAsJsonObject().get("detalle").getAsString());
                            listaUnidades.add(unidad);
                        }
                        llenarListViewUnidades(stringIdsListaUnidades, listaUnidades);
                        Toast.makeText(UnidadContent.this, "Datos cargados exitosamente!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(UnidadContent.this, t.getMessage() + "La peticion falló!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UnidadContent.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    private void llenarListViewUnidades(ArrayList<String> stringListaUnidad, List<Unidad> listaUnidad) {
        try {
            this.listaUnidades = listaUnidad;
            listviewUnidad = findViewById(R.id.lvContentUnidad);
            mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringListaUnidad);
            listviewUnidad.setAdapter(mAdapter);
            onTextChanged();
            setUpActionBar();
        } catch (NullPointerException npe) {
            Toast.makeText(UnidadContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void onTextChanged() {
        // Arraylist con datos filtrados
        final ArrayList<String> array_sort = new ArrayList<>();
        mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringIdsListaUnidades);
        listviewUnidad = findViewById(R.id.lvContentUnidad);
        listviewUnidad.setAdapter(mAdapter);
        // Inicializamos textview de busqueda
        tvBuscarUnidad = findViewById(R.id.txtBuscarContentUnidad);
        tvBuscarUnidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = tvBuscarUnidad.getText().length();
                array_sort.clear();
                for (int i = 0; i < stringIdsListaUnidades.size(); i++) {
                    if (textlength <= stringIdsListaUnidades.get(i).length()) {
                        if (stringIdsListaUnidades.get(i).contains(tvBuscarUnidad.getText().toString())) {
                            array_sort.add(stringIdsListaUnidades.get(i));
                        }
                    }
                }
                listviewUnidad.setAdapter(new SelectionAdapter(UnidadContent.this, android.R.layout.simple_list_item_1, android.R.id.text1, array_sort));
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

        listviewUnidad.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listviewUnidad.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                // If element is checked, it is added to selection; if not, it's deleted
                if (checked) {
                    idUnidadSeleccionada.add(listviewUnidad.getItemAtPosition(position).toString());
                    mAdapter.setNewSelection(position);
                    unidad = listaUnidades.get(position);
                } else {
                    idUnidadSeleccionada.remove(listviewUnidad.getItemAtPosition(position).toString());
                    mAdapter.removeSelection(position);
                }
                mode.setTitle(mAdapter.getSelectionCount() + " Items seleccionados");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // CAB menu options
                if (item.getItemId() == R.id.delete) {
                    Toast.makeText(UnidadContent.this,
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