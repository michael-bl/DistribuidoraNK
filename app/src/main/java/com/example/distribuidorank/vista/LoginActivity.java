package com.example.distribuidorank.vista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distribuidorank.Database.Conexiones;
import com.example.distribuidorank.Database.ExistDataBaseSqlite;
import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.ApiUtils;
import com.example.distribuidorank.controlador.ConnectivityService;
import com.example.distribuidorank.controlador.HashPass;
import com.example.distribuidorank.modelo.Modo;
import com.example.distribuidorank.modelo.Usuario;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ExistDataBaseSqlite existDb;
    private EditText txtUser, txtPass;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private Conexiones conexiones;
    private HashPass hashPass;
    private Usuario usuario;
    private Intent intent;
    private String user;
    private String pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Evento boton login
        Button button = findViewById(R.id.btnLogin);
        button.setOnClickListener(v -> loginLocalOremoto());
    }

    /**
     * Escoge de donde obtener los datos para el inicio de sesion
     */
    private void loginLocalOremoto() {
        conexiones = new Conexiones(this);
        existDb = new ExistDataBaseSqlite();
        if (!existDb.existeDataBase()) loginRemoto();
        else loginLocal();
    }

    /**
     * Realiza inicio de sesion en db local
     */
    private void loginLocal() {
        conexiones = new Conexiones(this);
        hashPass = new HashPass();
        getUsuarioYpass();
        pass = hashPass.convertirSHA256(txtPass.getText().toString().trim());
        if (!conexiones.inicarSesion(user, pass))
            Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecto!", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Obtiene y valida el llenado de los datos ingresados en los inputs del formulario
     */
    private void getUsuarioYpass() {
        txtUser = findViewById(R.id.edittext_usuario);
        txtPass = findViewById(R.id.edittext_contrasena);
        user = txtUser.getText().toString().trim();
        pass = txtPass.getText().toString().trim();
        if (user.equals("") & pass.equals(""))
            Toast.makeText(this, "Debe rellenar todos los campos!", Toast.LENGTH_LONG).show();
    }

    /**
     * Realiza inicio de sesion en db remota
     */
    private void loginRemoto() {
        hashPass = new HashPass();
        getUsuarioYpass();
        if (!user.equals("") && !pass.equals("")) {
            // Verificamos que el dispositivo tenga conexion a internet
            ConnectivityService con = new ConnectivityService();
            if (con.stateConnection(this)) {
                pass = hashPass.convertirSHA256(txtPass.getText().toString().trim());
                Call<List<Usuario>> requestLogin = ApiUtils.getApiServices().login(user, pass);
                requestLogin.enqueue(new Callback<List<Usuario>>() {
                    @Override
                    public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "No se puede realizar la consulta!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Manejar datos obtenidos en el reponse de la peticion
                            List<Usuario> respuesta = response.body();
                            if (respuesta.size() != 0) {
                                for (int i = 0; i < respuesta.size(); i++) {
                                    if (user.equals(respuesta.get(i).getId()) && pass.equals(respuesta.get(i).getPass())) {
                                        existDb = new ExistDataBaseSqlite();
                                        if (!existDb.existeDataBase()) {
                                            // Enviamos el usuario al metodo crearDblocal para almacenarlo
                                            usuario = new Usuario();
                                            usuario.setId(respuesta.get(i).getId());
                                            usuario.setPass(respuesta.get(i).getPass());
                                            usuario.setFk_localidad(respuesta.get(i).getFk_localidad());
                                            usuario.setNombre(respuesta.get(i).getNombre());
                                            usuario.setTelefono(respuesta.get(i).getTelefono());
                                            usuario.setEmail(respuesta.get(i).getEmail());
                                            usuario.setDireccion(respuesta.get(i).getDireccion());
                                            usuario.setEstado(respuesta.get(i).getEstado());
                                            usuario.setAccion(0);
                                            crearDBlocal(usuario);
                                        }
                                        // Lanzamos actividad principal
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecto!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Usuario>> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "La petición falló!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Debe rellenar todos los campos!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Crear BD local e insertar usuario logeado
     */
    private void crearDBlocal(Usuario usuario) {
        // Guardamos por defecto, almacenamiento y lectura en modo local
        Gson gson = new Gson();
        Modo modo = new Modo();
        modo.setId(1);
        modo.setMode(0);
        conexiones = new Conexiones(LoginActivity.this);
        conexiones.crearDbLocal(LoginActivity.this);
        conexiones.accionesTablaModo(gson.toJson(modo), "Insert");
        conexiones.accionesTablaUsuario(0, gson.toJson(usuario));
    }
}