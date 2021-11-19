package com.example.distribuidorank.vista;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.ApiUtils;
import com.example.distribuidorank.controlador.ConnectivityService;
import com.example.distribuidorank.controlador.SelectionAdapter;
import com.example.distribuidorank.modelo.Cliente;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteActivity extends AppCompatActivity {
    // lista de objetos tipo cliente
    private List<Cliente> listaCliente;
    // MultiSelect list adapter
    private SelectionAdapter mAdapter;
    // Variable del listview clientes
    private ListView lvCliente;
    // Variable del textview para ingresar busqueda
    private TextInputEditText tvBuscar;
    // Variable con lista de clientes
    private ArrayList<String> stringArrayListCliente;
    // Variable para ids de clientes
    private ArrayList<Cliente> arrayCliente = new ArrayList<>();
    // Variables de un cliente
    private String cedula, localidad, nombre, telefono, email, direccion;
    // Objeto cliente
    private Cliente cliente;
    private int accion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // cliente
        cliente = new Cliente();
        // obtener datos del bundle
        Bundle bundle = this.getIntent().getExtras();
        if(bundle !=null){
            //Extrayendo el extra de tipo cadena
            cliente = (Cliente) bundle.getSerializable("cliente");
            mostrarDatosdelCliente(cliente);
        }
        // Metodo para solicitar los datos al server
        //obtenerClientes();
        Button btnGuardar = findViewById(R.id.btnSiguienteContent);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCliente();
            }
        });
    }

    //mostramos los datos del usuario en los campos de la interfaz
    private void mostrarDatosdelCliente(Cliente cliente){

        TextInputEditText txtCedula = findViewById(R.id.txtCedulaCliente);
        TextInputEditText txtNombre = findViewById(R.id.txtNombreCliente);
        TextInputEditText txtDireccion = findViewById(R.id.txtDireccionCliente);
        TextInputEditText txtEmail = findViewById(R.id.txtEmailCliente);
        TextInputEditText txtLocalidad = findViewById(R.id.txtLocalidadCliente);
        TextInputEditText txtTelefono = findViewById(R.id.txtTelefonoCliente);
        // Seteamos los datos en los views
        txtCedula.setText( cliente.getId());
        txtNombre.setText( cliente.getNombre());
        txtDireccion.setText( cliente.getDireccion());
        txtEmail.setText( cliente.getEmail());
        txtLocalidad.setText(cliente.getFk_localidad());
        txtTelefono.setText( cliente.getTelefono());
    }

    //guardar el nuevo cliente
    private void guardarCliente(){
        // Validamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(ClienteActivity.this)) {
            // Verificamos que todos los datos del reporte esten ingresados
            if (makeReport()) {
                Call<JsonObject> requestReport = ApiUtils.getApiServices().accionCliente(cedula, localidad, nombre, telefono, email, direccion, accion);
                //Call<JsonObject> requestReport = ApiUtils.getApiServices().nuevoCliente(cliente);
                requestReport.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            //Verificamos si la transaccion fue exitosa y mostramos mensaje de error
                            if (!response.isSuccessful()) {
                                Toast.makeText(ClienteActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ClienteActivity.this, "Reporte guardado!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (java.lang.Error e){
                            Toast.makeText(ClienteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ClienteActivity.this,"La peticion falló:  " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(ClienteActivity.this, "Faltan datos del reporte!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ClienteActivity.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean makeReport() {
        arrayCliente = new ArrayList<>();
        Spinner sp = findViewById(R.id.spLocalidadCliente);
        TextView txtCedula = findViewById(R.id.txtCedulaCliente);
        TextView txtNombre = findViewById(R.id.txtNombreCliente);
        TextView txtTelefono = findViewById(R.id.txtTelefonoCliente);
        TextView txtLocalidad = findViewById(R.id.txtLocalidadCliente);
        TextView txtDireccion = findViewById(R.id.txtDireccionCliente);
        TextView txtEmail = findViewById(R.id.txtEmailCliente);
        /*cliente.setId(txtCedula.getText().toString());
        cliente.setNombre(txtCardNombre.getText().toString());
        cliente.setTelefono(txtTelefono.getText().toString());
        cliente.setFk_localidad(txtLocalidad.getText().toString());
        cliente.setDireccion(txtDireccion.getText().toString());
        cliente.setEmail(txtEmail.getText().toString());
        arrayCliente.add(cliente);
        cliente.setArrayCliente(arrayCliente);*/

        accion = cliente.getAccion();
        cedula=txtCedula.getText().toString();
        nombre=txtNombre.getText().toString();
        telefono=txtTelefono.getText().toString();
        localidad=txtLocalidad.getText().toString();
        direccion=txtDireccion.getText().toString();
        email = txtEmail.getText().toString();
        return true;
    }

    /**Solicitamos los datos al servidor remoto */
    private void obtenerClientes() {
        // Verificamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(this)) {
            Call<List<Cliente>> requestEmpleado = ApiUtils.getApiServices().getCliente();

            requestEmpleado.enqueue(new Callback<List<Cliente>>() {
                @Override
                public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {

                    if (!response.isSuccessful()) {
                        // Mensaje de error
                        Toast.makeText(ClienteActivity.this, "ResponseMsg al cargar datos!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Manejar datos obtenidos en la petición
                        listaCliente = response.body();
                        stringArrayListCliente = new ArrayList<>();
                        for (int i = 0; i < listaCliente.size(); i++) {
                            stringArrayListCliente.add(i, listaCliente.get(i).getId() + "-" + listaCliente.get(i).getNombre());
                        }
                        setListaEmpleados(stringArrayListCliente);
                    }
                }
                @Override
                public void onFailure(Call<List<Cliente>> call, Throwable t) {
                    Toast.makeText(ClienteActivity.this, "ResponseMsg, la peticion fallo!" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ClienteActivity.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Carga datos de empleados en un spinner
     */
    private void setListaEmpleados(ArrayList<String> mylist) {
        lvCliente = findViewById(R.id.lvClientes);
        mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, mylist);
        lvCliente.setAdapter(mAdapter);
        onTextChanged();
        setUpActionBar();
    }

    /**Filtro sobre el Listview de empleados*/
    private void onTextChanged(){
        // Arraylist con datos filtrados
        final ArrayList<String> array_sort = new ArrayList<>();
        mAdapter = new SelectionAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArrayListCliente);
        lvCliente = findViewById(R.id.lvClientes);
        lvCliente.setAdapter(mAdapter);
        // Inicializamos textview de busqueda
        tvBuscar = findViewById(R.id.txtLocalidadCliente);
        tvBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = tvBuscar.getText().length();
                array_sort.clear();
                for (int i = 0; i< stringArrayListCliente.size(); i++){
                    if (textlength <= stringArrayListCliente.get(i).length()) {
                        if (stringArrayListCliente.get(i).contains(tvBuscar.getText().toString())) {
                            array_sort.add(stringArrayListCliente.get(i));
                        }
                    }
                }
                lvCliente.setAdapter(new SelectionAdapter(ClienteActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, array_sort));
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

        lvCliente.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvCliente.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                // If element is checked, it is added to selection; if not, it's deleted
                if (checked) {
                    //idClienteSeleccionado.add(lvEmpleado.getItemAtPosition(position).toString());
                    mAdapter.setNewSelection(position);
                } else {
                    //idClienteSeleccionado.remove(lvEmpleado.getItemAtPosition(position).toString());
                    mAdapter.removeSelection(position);
                }

                mode.setTitle(mAdapter.getSelectionCount() + " Items seleccionados");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // CAB menu options
                switch (item.getItemId()) {
                    case R.id.delete:
                        Toast.makeText(ClienteActivity.this,
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