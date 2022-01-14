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
import com.example.distribuidorank.modelo.Cliente;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteContent extends AppCompatActivity {
    private ArrayList<String> idClienteSeleccionado;
    private ArrayList<String> stringIdsListaClientes;
    private TextInputEditText tvBuscarCliente;
    private ExistDataBaseSqlite existDb;
    private List<Cliente> listaClientes;
    private Conexiones conexiones;
    // MultiSelect list adapter
    private SelectionAdapter mAdapter;
    private ListView listviewClientes;
    private Cliente cliente;
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
        setContentView(R.layout.content_cliente);

        idClienteSeleccionado = new ArrayList<>();
        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_opciones, null);
        builder = new AlertDialog.Builder(this);
        intent = new Intent(this, ClienteActivity.class);
        bundle = new Bundle();
        cliente = new Cliente();

        //Extrayendo el extra de tipo cadena
        cliente = (Cliente) bundle.getSerializable("cliente");

        Button btnSiguiente = findViewById(R.id.btnNextContentCliente);
        btnSiguiente.setOnClickListener(v -> {
            if (cliente.getAccion() == 0) {
                dialogOpcionCrudSobreCliente().show();
            } else {
                cliente.setAccion(0);
                cliente.setEstado(1);
                bundle.putSerializable("cliente", cliente);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        existDb = new ExistDataBaseSqlite();
        if (existDb.existeDataBaseLocal())
            getClientesDeDbLocal();
        else getClientesDeDbRemoto();
    }

    private AlertDialog dialogOpcionCrudSobreCliente() {
        view = inflater.inflate(R.layout.dialog_opciones, null);
        final Button btnActualizar = view.findViewById(R.id.btnNuevo);
        btnActualizar.setText("Actualizar");
        final Button btnEliminar = view.findViewById(R.id.btnMasOpciones);
        btnEliminar.setText("Eliminar");
        btnActualizar.setOnClickListener(v -> {
            cliente.setAccion(1);
            cliente.setEstado(1);
            bundle.putSerializable("cliente", cliente);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btnEliminar.setOnClickListener(v -> {
            cliente.setAccion(2);
            cliente.setEstado(0);
            bundle.putSerializable("cliente", cliente);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        builder.setView(view).setTitle("Elija una opción!").setPositiveButton("", (dialog, id) -> {
            ((ViewGroup) drawerLayout.getParent()).removeView(view);
        }).setNegativeButton("Cancelar", (dialog, which) -> ((ViewGroup) drawerLayout.getParent()).removeView(view));
        return builder.create();
    }

    private void getClientesDeDbLocal() {
        try {
            conexiones = new Conexiones(this);
            List<Cliente> listaClientes = new ArrayList<>(conexiones.getClientes());
            stringIdsListaClientes = new ArrayList<>();
            for (Cliente cliente : listaClientes) {
                stringIdsListaClientes.add(cliente.getId() + "-" + cliente.getNombre());
            }
            llenarListViewClientes(stringIdsListaClientes, listaClientes);
        } catch (NullPointerException npe) {
            Toast.makeText(ClienteContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JsonSyntaxException jse) {
            Toast.makeText(ClienteContent.this, "Error, verifique por favor: " + jse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getClientesDeDbRemoto() {
        ConnectivityService estaConectado = new ConnectivityService();
        if (estaConectado.stateConnection(this)) {
            Call<List<Cliente>> requestLastReports = ApiUtils.getApiServices().getClientes();
            requestLastReports.enqueue(new Callback<List<Cliente>>() {
                @Override
                public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(ClienteContent.this, response.message() + "Error al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        listaClientes = response.body();
                        stringIdsListaClientes = new ArrayList<>();
                        for (int i = 0; i < listaClientes.size(); i++) {
                            cliente = new Cliente();
                            stringIdsListaClientes.add(i, listaClientes.get(i).getId() + "-" + listaClientes.get(i).getNombre());
                            cliente.setFk_localidad(listaClientes.get(i).getFk_localidad());
                            cliente.setNombre(listaClientes.get(i).getNombre());
                            cliente.setTelefono(listaClientes.get(i).getTelefono());
                            cliente.setEmail(listaClientes.get(i).getEmail());
                            cliente.setDireccion(listaClientes.get(i).getDireccion());
                            cliente.setId(listaClientes.get(i).getId());
                        }
                        llenarListViewClientes(stringIdsListaClientes, listaClientes);
                        Toast.makeText(ClienteContent.this, "Datos cargados exitosamente!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Cliente>> call, Throwable t) {
                    Toast.makeText(ClienteContent.this, t.getMessage() + "La peticion falló!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ClienteContent.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    private void llenarListViewClientes(ArrayList<String> stringListaCliente, List<Cliente> listaCliente) {
        try {
            this.listaClientes = listaCliente;
            listviewClientes = findViewById(R.id.lvContentClientes);
            mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringListaCliente);
            listviewClientes.setAdapter(mAdapter);
            onTextChanged();
            setUpActionBar();
        } catch (NullPointerException npe) {
            Toast.makeText(ClienteContent.this, "Error, verifique por favor: " + npe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void onTextChanged() {
        // Arraylist con datos filtrados
        final ArrayList<String> array_sort = new ArrayList<>();
        mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringIdsListaClientes);
        listviewClientes = findViewById(R.id.lvContentClientes);
        listviewClientes.setAdapter(mAdapter);
        // Inicializamos textview de busqueda
        tvBuscarCliente = findViewById(R.id.txtBuscarContentCliente);
        tvBuscarCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = tvBuscarCliente.getText().length();
                array_sort.clear();
                for (int i = 0; i < stringIdsListaClientes.size(); i++) {
                    if (textlength <= stringIdsListaClientes.get(i).length()) {
                        if (stringIdsListaClientes.get(i).contains(tvBuscarCliente.getText().toString())) {
                            array_sort.add(stringIdsListaClientes.get(i));
                        }
                    }
                }
                listviewClientes.setAdapter(new SelectionAdapter(ClienteContent.this, android.R.layout.simple_list_item_1, android.R.id.text1, array_sort));
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

        listviewClientes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listviewClientes.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                // If element is checked, it is added to selection; if not, it's deleted
                if (checked) {
                    idClienteSeleccionado.add(listviewClientes.getItemAtPosition(position).toString());
                    mAdapter.setNewSelection(position);
                    cliente = listaClientes.get(position);
                } else {
                    idClienteSeleccionado.remove(listviewClientes.getItemAtPosition(position).toString());
                    mAdapter.removeSelection(position);
                }
                mode.setTitle(mAdapter.getSelectionCount() + " Items seleccionados");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // CAB menu options
                if (item.getItemId() == R.id.delete) {
                    Toast.makeText(ClienteContent.this,
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
