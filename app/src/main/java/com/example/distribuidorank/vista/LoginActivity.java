package com.example.distribuidorank.vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distribuidorank.R;
import com.example.distribuidorank.controlador.ApiUtils;
import com.example.distribuidorank.controlador.ConnectivityService;
import com.example.distribuidorank.controlador.HashPass;
import com.example.distribuidorank.modelo.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private String dniUser;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Evento boton login
        Button button = findViewById(R.id.btnLogin);
        button.setOnClickListener(this::login);
    }

    private void login(final View v){
        HashPass hashPass = new HashPass();
        EditText txtUser = findViewById(R.id.edittext_usuario);
        EditText txtPass = findViewById(R.id.edittext_contrasena);
        dniUser = txtUser.getText().toString().trim();
        pass = txtPass.getText().toString().trim();
        if (!dniUser.equals("") && !pass.equals("")){
            // Verificamos que el dispositivo tenga coneccion a internet
            ConnectivityService con = new ConnectivityService();
            if (con.stateConnection(this)) {
                pass = hashPass.convertirSHA256(txtPass.getText().toString().trim());
                Call<List<Usuario>> requestActividad = ApiUtils.getApiServices().login(dniUser,pass);

                requestActividad.enqueue(new Callback<List<Usuario>>() {
                    @Override
                    public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {

                        if (!response.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "No se puede realizar la consulta!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Manejar datos obtenidos en el reponse de la peticion
                            List<Usuario> rpt = response.body();
                            if (rpt.size()!=0){
                                for (int i=0;i<rpt.size();i++) {
                                    if (dniUser.equals(rpt.get(i).getId()) && pass.equals(rpt.get(i).getPass())) {
                                        //db = new DbReporte (ActivityLogin.this);
                                        // Guardar encargado
                                        //db.insertNewManager(dniUser, rpt.get(i).getNombre());
                                        // Lanzamos actividad principal
                                        Intent intent = new Intent(v.getContext(), MainActivity.class);
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
                        Toast.makeText(LoginActivity.this, "ResponseMsg, la peticion fallo!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Debe rellenar todos los campos!", Toast.LENGTH_LONG);
        }
    }
}