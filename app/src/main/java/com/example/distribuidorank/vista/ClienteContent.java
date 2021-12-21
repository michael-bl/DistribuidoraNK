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

import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.ApiUtils;
import com.example.distribuidorank.controlador.ConnectivityService;
import com.example.distribuidorank.controlador.SelectionAdapter;
import com.example.distribuidorank.modelo.Cliente;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteContent extends AppCompatActivity {
    // lista de objetos tipo cliente
    private List<Cliente> listaClientes;
    // MultiSelect list adapter
    private SelectionAdapter mAdapter;
    // Variable del listview clientes
    private ListView listviewCliente;
    // Variable para ids de empleados
    private ArrayList<String> idClienteSeleccionado;
    // Variable string con lista de clientes
    private ArrayList<String> stringListaClientes;
    // Variable del textview para ingresar busqueda
    private TextInputEditText tvBuscar;
    // Objeto Cliente
    private Cliente cliente;
    // Para mostrar alertDialog
    private View view;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private Intent intent;
    private Bundle bundle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_cliente);
        // Solicita los datos al servidor remoto
        getClientes();
        idClienteSeleccionado = new ArrayList<>();
        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_opciones,null);
        builder = new AlertDialog.Builder(this);
        intent = new Intent(this, ClienteActivity.class);
        bundle = new Bundle();
        cliente = new Cliente();
        //Evento boton procesar
        Button btnSiguiente = findViewById(R.id.btnSiguienteContentCliente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cliente.getAccion()==0){
                    dialogAccion().show();
                } else {
                    cliente.setAccion(0);
                    cliente.setEstado(1);
                    bundle.putSerializable("cliente", cliente);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    /** MsgDialogDBLocal captura opcion - Actualizar o eliminar objeto */
    private AlertDialog dialogAccion() {
        view = inflater.inflate(R.layout.dialog_opciones,null);
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

        builder.setView(view).setTitle("Elija una opción!").setPositiveButton("", (dialog, id) -> {((ViewGroup)drawerLayout.getParent()).removeView(view);
        }).setNegativeButton("Cancelar", (dialog, which) -> ((ViewGroup)drawerLayout.getParent()).removeView(view));
        return builder.create();
    }

    /** Solicita los clientes al servidor remoto*/
    public void getClientes(){
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(this)) {
            Call<List<Cliente>> requestLastReports = ApiUtils.getApiServices().getClientes();
            requestLastReports.enqueue(new Callback<List<Cliente>>() {
                @Override
                public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(ClienteContent.this, response.message() + "Error al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        listaClientes = response.body();
                        stringListaClientes = new ArrayList<>();
                        cliente = new Cliente();
                        for (int i = 0; i < listaClientes.size(); i++) {
                            stringListaClientes.add(i, listaClientes.get(i).getId() + "-" + listaClientes.get(i).getNombre());
                            cliente.setFk_localidad(listaClientes.get(i).getFk_localidad());
                            cliente.setNombre(listaClientes.get(i).getNombre());
                            cliente.setTelefono(listaClientes.get(i).getTelefono());
                            cliente.setEmail(listaClientes.get(i).getEmail());
                            cliente.setDireccion(listaClientes.get(i).getDireccion());
                            cliente.setId(listaClientes.get(i).getId());
                        }
                        setListaEmpleados(stringListaClientes, cliente);
                        Toast.makeText(ClienteContent.this, "Datos cargados exitosamente!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Cliente>> call, Throwable t) {
                    Toast.makeText(ClienteContent.this, t.getMessage() +  "ResponseMsg, la peticion fallo!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ClienteContent.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    /** Carga datos de empleados en un spUnidadProducto */
    private void setListaEmpleados(ArrayList<String> listacliente, Cliente c) {
        this.cliente = c;
        listviewCliente = findViewById(R.id.lvClientes);
        mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, listacliente);
        listviewCliente.setAdapter(mAdapter);
        onTextChanged();
        setUpActionBar();
    }
    /** Filtro sobre el Listview de empleados*/
    private void onTextChanged(){
        // Arraylist con datos filtrados
        final ArrayList<String> array_sort = new ArrayList<>();
        mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringListaClientes);
        listviewCliente = findViewById(R.id.lvClientes);
        listviewCliente.setAdapter(mAdapter);
        // Inicializamos textview de busqueda
        tvBuscar = findViewById(R.id.txtBuscarCliente);
        tvBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = tvBuscar.getText().length();
                array_sort.clear();
                for (int i = 0; i< stringListaClientes.size(); i++){
                    if (textlength <= stringListaClientes.get(i).length()) {
                        if (stringListaClientes.get(i).contains(tvBuscar.getText().toString())) {
                            array_sort.add(stringListaClientes.get(i));
                        }
                    }
                }
                listviewCliente.setAdapter(new SelectionAdapter(ClienteContent.this, android.R.layout.simple_list_item_1, android.R.id.text1, array_sort));
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

        listviewCliente.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listviewCliente.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                // If element is checked, it is added to selection; if not, it's deleted
                if (checked) {
                    idClienteSeleccionado.add(listviewCliente.getItemAtPosition(position).toString());
                    mAdapter.setNewSelection(position);
                    cliente = listaClientes.get(position);
                } else {
                    idClienteSeleccionado.remove(listviewCliente.getItemAtPosition(position).toString());
                    mAdapter.removeSelection(position);
                }
                mode.setTitle(mAdapter.getSelectionCount() + " Items seleccionados");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // CAB menu options
                switch (item.getItemId()) {
                    case R.id.delete:
                        Toast.makeText(ClienteContent.this,
                                mAdapter.getSelectionCount() + " Items eliminados",
                                Toast.LENGTH_LONG).show();
                        mAdapter.clearSelection();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
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