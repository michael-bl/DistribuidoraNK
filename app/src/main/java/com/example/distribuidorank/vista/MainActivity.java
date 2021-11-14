package com.example.distribuidorank.vista;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.distribuidorank.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private View view;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);

        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_opciones,null);
        builder = new AlertDialog.Builder(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up btnSiguienteContent, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         if(id==R.id.nav_usuario){
             Intent intent = new Intent(this.getApplicationContext(), UsuarioActivity.class);
             startActivity(intent);
        } if(id==R.id.nav_cliente){
            dialogOpciones().show();
        }if(id==R.id.nav_producto){
            Intent intent = new Intent(this.getApplicationContext(),ProductoActivity.class);
            startActivity(intent);
        }if(id==R.id.nav_proveedor){
            Intent intent = new Intent(this.getApplicationContext(), ProveedorActivity.class);
            startActivity(intent);
        }if(id==R.id.nav_localizacion){
            Intent intent = new Intent(this.getApplicationContext(),LocalidadActivity.class);
            startActivity(intent);
        }if(id==R.id.nav_facturacion){
            Intent intent = new Intent(this.getApplicationContext(),FacturacionActivity.class);
            startActivity(intent);
        }if(id==R.id.nav_unidadmedida){
            Intent intent = new Intent(this.getApplicationContext(), UnidadActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_send) {
            return true;
        } if(id==R.id.nav_share){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * AlertDialog con opciones a realizar sobre objeto
     */
    private AlertDialog dialogOpciones() {

        view = inflater.inflate(R.layout.dialog_opciones,null);
        final Button btnNuevo = view.findViewById(R.id.btnNuevo);
        final Button btnActualizar = view.findViewById(R.id.btnMasOpciones);
        btnNuevo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClienteActivity.class);
            startActivity(intent);
        });
        btnActualizar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClienteContent.class);
            startActivity(intent);
        });
        
        builder.setView(view).setTitle("Cliente: seleccione una opción").setPositiveButton("", (dialog, id) -> {((ViewGroup)drawerLayout.getParent()).removeView(view);
        }).setNegativeButton("Cancelar", (dialog, which) -> ((ViewGroup)drawerLayout.getParent()).removeView(view));
        return builder.create();
    }
}