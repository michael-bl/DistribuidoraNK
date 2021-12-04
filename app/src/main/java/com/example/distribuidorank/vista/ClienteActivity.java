package com.example.distribuidorank.vista;

import android.os.Bundle;
import android.view.View;
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
    private TextInputEditText txtCedula , txtNombre ,txtDireccion ,txtEmail , txtLocalidad , txtTelefono ;
    // Objeto cliente
    private Cliente cliente;
    private int accion, estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Instanica de los inputtext
        instanciaComponentes();
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
        Button btnGuardar = findViewById(R.id.btnSiguienteContentProducto);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCliente();
            }
        });
    }

    private void instanciaComponentes() {
        txtCedula = findViewById(R.id.txtCedulaCliente);
        txtNombre = findViewById(R.id.txtNombreCliente);
        txtDireccion = findViewById(R.id.txtDireccionCliente);
        txtEmail = findViewById(R.id.txtEmailCliente);
        txtLocalidad = findViewById(R.id.txtBuscarProducto);
        txtTelefono = findViewById(R.id.txtTelefonoCliente);
    }

    //mostramos los datos del usuario en los campos de la interfaz
    private void mostrarDatosdelCliente(Cliente cliente){
        // Seteamos los datos en los views
        try {
            txtCedula.setText( cliente.getId());
            txtNombre.setText( cliente.getNombre());
            txtDireccion.setText( cliente.getDireccion());
            txtEmail.setText( cliente.getEmail());
            txtLocalidad.setText(cliente.getFk_localidad());
            txtTelefono.setText( cliente.getTelefono());
        } catch (Error nullPointerException){
            Toast.makeText(ClienteActivity.this, nullPointerException.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //guardar el nuevo cliente
    private void guardarCliente(){
        // Validamos que el dispositivo tenga coneccion a internet
        ConnectivityService con = new ConnectivityService();
        if (con.stateConnection(ClienteActivity.this)) {
            // Verificamos que todos los datos del reporte esten ingresados
            if (makeReport()) {
                Call<JsonObject> solicitudAccionCliente = ApiUtils.getApiServices().accionCliente(cedula, localidad, nombre, telefono, email, direccion, accion, estado);
                //Call<JsonObject> solicitudAccionCliente = ApiUtils.getApiServices().accionCliente2(cliente);
                solicitudAccionCliente.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            //Verificamos si la transaccion fue exitosa y mostramos mensaje de error
                            if (!response.isSuccessful()) {
                                Toast.makeText(ClienteActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ClienteActivity.this, "Cliente guardado exitosamente!", Toast.LENGTH_SHORT).show();
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
        TextView txtLocalidad = findViewById(R.id.txtBuscarProducto);
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

        estado = cliente.getEstado();
        accion = cliente.getAccion();
        cedula=txtCedula.getText().toString();
        nombre=txtNombre.getText().toString();
        telefono=txtTelefono.getText().toString();
        localidad=txtLocalidad.getText().toString();
        direccion=txtDireccion.getText().toString();
        email = txtEmail.getText().toString();
        return true;
    }
}