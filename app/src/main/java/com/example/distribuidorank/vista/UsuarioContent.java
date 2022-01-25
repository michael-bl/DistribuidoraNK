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
import com.example.distribuidorank.modelo.Usuario;
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

public class UsuarioContent extends AppCompatActivity {
    private ArrayList<String> idUsuarioSeleccionado;
    private ArrayList<String> stringIdsListaUsuarios;
    private TextInputEditText tvBuscarUsuario;
    private ExistDataBaseSqlite existDb;
    private List<Usuario> listaUsuarios;
    private Conexiones conexiones;
    // MultiSelect list adapter
    private ListView listviewUsuarios;
    private SelectionAdapter mAdapter;
    private Usuario usuario;
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
        setContentView(R.layout.content_usuario);

        idUsuarioSeleccionado = new ArrayList<>();
        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_opciones, null);
        builder = new AlertDialog.Builder(this);
        intent = new Intent(this, UsuarioActivity.class);
        bundle = new Bundle();
        usuario = new Usuario();

        //Extrayendo el extra de tipo cadena
        usuario = (Usuario) bundle.getSerializable("usuario");

        //Evento boton siguiente, hacia UsuarioActivity.class
        Button btnSiguiente = findViewById(R.id.btnNextContentUsuario);
        btnSiguiente.setOnClickListener(v -> {
            if (usuario.getAccion() == 0) {
                dialogOpcionCrudSobreUsuario().show();
            } else {
                usuario.setAccion(0);
                usuario.setEstado(1);
                bundle.putSerializable("usuario", usuario);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        existDb = new ExistDataBaseSqlite();
        if (existDb.existeDataBaseLocal())
            getUsuariosDeDbLocal();
        else getUsuariosDeDbRemoto();
    }

    private AlertDialog dialogOpcionCrudSobreUsuario() {
        view = inflater.inflate(R.layout.dialog_opciones, null);

        final Button btnActualizar = view.findViewById(R.id.btnNuevo);
        btnActualizar.setText("Actualizar");

        final Button btnEliminar = view.findViewById(R.id.btnMasOpciones);
        btnEliminar.setText("Eliminar");

        btnActualizar.setOnClickListener(v -> {
            usuario.setAccion(1);
            usuario.setEstado(1);
            bundle.putSerializable("usuario", usuario);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btnEliminar.setOnClickListener(v -> {
            usuario.setAccion(2);
            usuario.setEstado(0);
            bundle.putSerializable("usuario", usuario);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        builder.setView(view).setTitle("Escoga una opción!").setPositiveButton("", (dialog, id) -> {
            ((ViewGroup) drawerLayout.getParent()).removeView(view);
        });
        return builder.create();
    }

    private void getUsuariosDeDbLocal() {
        try {
            conexiones = new Conexiones(this);
            List<Usuario> listaUsuarios = new ArrayList<>(conexiones.getUsuarios());
            stringIdsListaUsuarios = new ArrayList<>();
            for (Usuario usuario : listaUsuarios) {
                stringIdsListaUsuarios.add(usuario.getId() + "-" + usuario.getNombre());
            }
            llenarListViewUsuarios(stringIdsListaUsuarios, listaUsuarios);
        } catch (NullPointerException npe) {
            Toast.makeText(UsuarioContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JsonSyntaxException jse) {
            Toast.makeText(UsuarioContent.this, "Error, verifique por favor: " + jse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getUsuariosDeDbRemoto() {
        ConnectivityService estaConectado = new ConnectivityService();
        if (estaConectado.stateConnection(this)) {
            Call<JsonArray> requestLastReports = ApiUtils.getApiServices().getUsuarios();
            requestLastReports.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(UsuarioContent.this, response.message() + "Error al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Manejar datos obtenidos en la petición
                        JsonArray listaUsuario = response.body();
                        stringIdsListaUsuarios = new ArrayList<>();
                        listaUsuarios = new ArrayList<>();
                        for (int j = 0; j < Objects.requireNonNull(listaUsuario).size(); j++) {
                            Usuario usuario = new Usuario();
                            stringIdsListaUsuarios.add(j, listaUsuario.get(j).getAsJsonObject().get("id").toString() + "-" + listaUsuario.get(j).getAsJsonObject().get("nombre").toString());
                            usuario.setId(listaUsuario.get(j).getAsJsonObject().get("id").toString());
                            usuario.setFk_localidad(listaUsuario.get(j).getAsJsonObject().get("fk_localidad").getAsInt());
                            usuario.setNombre(listaUsuario.get(j).getAsJsonObject().get("nombre").toString());
                            usuario.setPass(listaUsuario.get(j).getAsJsonObject().get("pass").toString());
                            usuario.setTelefono(listaUsuario.get(j).getAsJsonObject().get("telefono").toString());
                            usuario.setEmail(listaUsuario.get(j).getAsJsonObject().get("email").toString());
                            usuario.setDireccion(listaUsuario.get(j).getAsJsonObject().get("direccion").toString());
                            usuario.setEstado(listaUsuario.get(j).getAsJsonObject().get("estado").getAsInt());
                            //usuario.setLocalidad(listaPro.get(j).getAsJsonObject().get("localidad").toString()); Esta variable no esta inlcuida dentro de la clase usuario
                            listaUsuarios.add(usuario);
                        }
                        llenarListViewUsuarios(stringIdsListaUsuarios, listaUsuarios);
                        Toast.makeText(UsuarioContent.this, "Datos cargados exitosamente!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(UsuarioContent.this, "La peticion falló: " + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UsuarioContent.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    private void llenarListViewUsuarios(ArrayList<String> stringListaUsuario, List<Usuario> listaUsuario) {
        try {
            this.listaUsuarios = listaUsuario;
            listviewUsuarios = findViewById(R.id.lvContentUsuarios);
            mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringListaUsuario);
            listviewUsuarios.setAdapter(mAdapter);
            onTextChanged();
            setUpActionBar();
        } catch (NullPointerException npe) {
            Toast.makeText(UsuarioContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void onTextChanged() {
        try {
            // Arraylist con datos filtrados
            final ArrayList<String> array_sort = new ArrayList<>();
            // Inicializamos textview de busqueda
            tvBuscarUsuario = findViewById(R.id.txtBuscarContentUsuario);
            tvBuscarUsuario.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int textlength = Objects.requireNonNull(tvBuscarUsuario.getText()).length();
                    array_sort.clear();
                    for (int i = 0; i < stringIdsListaUsuarios.size(); i++) {
                        if (textlength <= stringIdsListaUsuarios.get(i).length()) {
                            if (stringIdsListaUsuarios.get(i).contains(tvBuscarUsuario.getText().toString())) {
                                array_sort.add(stringIdsListaUsuarios.get(i));
                            }
                        }
                    }
                    listviewUsuarios.setAdapter(new SelectionAdapter(UsuarioContent.this, android.R.layout.simple_list_item_1, android.R.id.text1, array_sort));
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } catch (NullPointerException npe) {
            Toast.makeText(UsuarioContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUpActionBar() {
        try {
            listviewUsuarios.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listviewUsuarios.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                    // If element is checked, it is added to selection; if not, it's deleted
                    if (checked) {
                        idUsuarioSeleccionado.add(listviewUsuarios.getItemAtPosition(position).toString());
                        mAdapter.setNewSelection(position);
                        //con position obtenemos el usuario de la listaUsuarios, el indice es el mismo
                        usuario = listaUsuarios.get(position);
                    } else {
                            idUsuarioSeleccionado.remove(listviewUsuarios.getItemAtPosition(position).toString());
                        mAdapter.removeSelection(position);
                    }
                    mode.setTitle(mAdapter.getSelectionCount() + " Items seleccionados");
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    // CAB menu options
                    if (item.getItemId() == R.id.delete) {
                        Toast.makeText(UsuarioContent.this,
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
        } catch (NullPointerException npe) {
            Toast.makeText(UsuarioContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}