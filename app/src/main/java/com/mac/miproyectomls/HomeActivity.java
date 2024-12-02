package com.mac.miproyectomls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Asociar botones
        Button botonUsuario = findViewById(R.id.botonusuario);
        Button botonAdministrador = findViewById(R.id.botonadministrador);

        // Acción para el botón de usuario
        botonUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el rol desde el Intent
                String rol = getIntent().getStringExtra("rol");

                if (rol != null && rol.equals("admin")) {
                    // Si es admin, ir a la actividad de gestión de productos
                    Intent intent = new Intent(HomeActivity.this, LoginAdministradorActivity.class);
                    startActivity(intent);
                } else {
                    // Si es cliente, ir a la actividad de login del usuario
                    Intent intent = new Intent(HomeActivity.this, LoginUsuarioActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Acción para el botón de administrador
        botonAdministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir directamente al login del administrador
                Intent intent = new Intent(HomeActivity.this, LoginAdministradorActivity.class);
                startActivity(intent);
            }
        });
    }
}
