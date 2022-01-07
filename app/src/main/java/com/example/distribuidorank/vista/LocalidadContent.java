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
import com.example.distribuidorank.modelo.Localidad;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocalidadContent extends AppCompatActivity {
    private ArrayList<String> stringIdsListaLocalidades;
    private ArrayList<String> idLocalidadSeleccionada;
    private TextInputEditText tvBuscarLocalidad;
    private List<Localidad> listaLocalidades;
    private ExistDataBaseSqlite existDb;
    private Conexiones conexiones;
    // MultiSelect list adapter
    private ListView listviewLocalidad;
    private SelectionAdapter mAdapter;
    private Localidad localidad;
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
        setContentView(R.layout.content_localidad);

        idLocalidadSeleccionada = new ArrayList<>();
        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_opciones, null);
        builder = new AlertDialog.Builder(this);
        intent = new Intent(this, LocalidadActivity.class);
        bundle = new Bundle();
        localidad = new Localidad();

        //Extrayendo el extra de tipo cadena
        localidad = (Localidad) bundle.getSerializable("localidad");

        //Evento boton siguiente, hacia LocalidadActivity.class
        Button btnSiguiente = findViewById(R.id.btnNextContentLocalidad);
        btnSiguiente.setOnClickListener(v -> {
            if (localidad.getAccion() == 0) {
                dialogAccion().show();
            } else {
                localidad.setAccion(0);
                localidad.setEstado(1);
                bundle.putSerializable("localidad", localidad);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //Solicitar localidades
        existDb = new ExistDataBaseSqlite();
        if (existDb.existeDataBase())
            getLocalidadesLocal();
        else getLocalidadesRemoto();
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
            localidad.setAccion(1);
            localidad.setEstado(1);
            bundle.putSerializable("localidad", localidad);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btnEliminar.setOnClickListener(v -> {
            localidad.setAccion(2);
            localidad.setEstado(0);
            bundle.putSerializable("localidad", localidad);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        builder.setView(view).setTitle("Elija una opción!").setPositiveButton("", (dialog, id) -> {
            ((ViewGroup) drawerLayout.getParent()).removeView(view);
        });
        return builder.create();
    }

    /**
     * Obtiene lista de localidades de bd local
     */
    private void getLocalidadesLocal() {
        try {
            conexiones = new Conexiones(this);
            List<Localidad> listaLocalidades = new ArrayList<>(conexiones.getLocalidades());
            stringIdsListaLocalidades = new ArrayList<>();
            for (Localidad localidad : listaLocalidades) {
                stringIdsListaLocalidades.add(localidad.getId() + "-" + localidad.getLocalidad());
            }
            setListaLocalidades(stringIdsListaLocalidades, listaLocalidades);
        } catch (NullPointerException npe) {
            Toast.makeText(LocalidadContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JsonSyntaxException jse) {
            Toast.makeText(LocalidadContent.this, "Error, verifique por favor: " + jse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Obtiene lista de localidades de bd remota
     */
    public void getLocalidadesRemoto() {
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(this)) {
            Call<JsonArray> requestLastReports = ApiUtils.getApiServices().getLocalidades();
            requestLastReports.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(LocalidadContent.this, response.message() + "Error al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        JsonArray arrayLocalidades = response.body();
                        stringIdsListaLocalidades = new ArrayList<>();
                        for (int i = 0; i < listaLocalidades.size(); i++) {
                            localidad = new Localidad();
                            stringIdsListaLocalidades.add(i, arrayLocalidades.getAsJsonObject().get("id").getAsInt() + "-" + arrayLocalidades.getAsJsonObject().get("localidad").getAsString());
                            localidad.setId(arrayLocalidades.getAsJsonObject().get("id").getAsInt());
                            localidad.setLocalidad(arrayLocalidades.getAsJsonObject().get("localidad").getAsString());
                            listaLocalidades.add(localidad);
                        }
                        setListaLocalidades(stringIdsListaLocalidades, listaLocalidades);
                        Toast.makeText(LocalidadContent.this, "Datos cargados exitosamente!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(LocalidadContent.this, t.getMessage() + "La peticion falló!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(LocalidadContent.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Carga datos de localidades en listviewclocalidades
     */
    private void setListaLocalidades(ArrayList<String> stringListaLocalidad, List<Localidad> c) {
        try {
            this.listaLocalidades = c;
            listviewLocalidad = findViewById(R.id.lvContentLocalidad);
            mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringListaLocalidad);
            listviewLocalidad.setAdapter(mAdapter);
            onTextChanged();
            setUpActionBar();
        } catch (NullPointerException npe) {
            Toast.makeText(LocalidadContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Filtro sobre el Listview de localidades
     */
    private void onTextChanged() {
        // Arraylist con datos filtrados
        final ArrayList<String> array_sort = new ArrayList<>();
        mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringIdsListaLocalidades);
        listviewLocalidad = findViewById(R.id.lvContentLocalidad);
        listviewLocalidad.setAdapter(mAdapter);
        // Inicializamos textview de busqueda
        tvBuscarLocalidad = findViewById(R.id.txtBuscarContentLocalidad);
        tvBuscarLocalidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = tvBuscarLocalidad.getText().length();
                array_sort.clear();
                for (int i = 0; i < stringIdsListaLocalidades.size(); i++) {
                    if (textlength <= stringIdsListaLocalidades.get(i).length()) {
                        if (stringIdsListaLocalidades.get(i).contains(tvBuscarLocalidad.getText().toString())) {
                            array_sort.add(stringIdsListaLocalidades.get(i));
                        }
                    }
                }
                listviewLocalidad.setAdapter(new SelectionAdapter(LocalidadContent.this, android.R.layout.simple_list_item_1, android.R.id.text1, array_sort));
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

        listviewLocalidad.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listviewLocalidad.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                // If element is checked, it is added to selection; if not, it's deleted
                if (checked) {
                    idLocalidadSeleccionada.add(listviewLocalidad.getItemAtPosition(position).toString());
                    mAdapter.setNewSelection(position);
                    localidad = listaLocalidades.get(position);
                } else {
                    idLocalidadSeleccionada.remove(listviewLocalidad.getItemAtPosition(position).toString());
                    mAdapter.removeSelection(position);
                }
                mode.setTitle(mAdapter.getSelectionCount() + " Items seleccionados");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // CAB menu options
                if (item.getItemId() == R.id.delete) {
                    Toast.makeText(LocalidadContent.this,
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