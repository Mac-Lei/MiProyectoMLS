package com.mac.miproyectomls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginUsuarioActivity extends AppCompatActivity {

    private EditText correoUsuario, contrasenaUsuario;
    private Button iniciarSesionUsuario;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correoUsuario = findViewById(R.id.correousuario);
        contrasenaUsuario = findViewById(R.id.contrasenausuario);
        iniciarSesionUsuario = findViewById(R.id.iniciarsesionusuario);

        dbHelper = new DatabaseHelper(this);

        iniciarSesionUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = correoUsuario.getText().toString();
                String contrasena = contrasenaUsuario.getText().toString();

                if (correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(LoginUsuarioActivity.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    boolean esCliente = dbHelper.checkCliente(correo, contrasena);

                    if (esCliente) {
                        // Si las credenciales son correctas, redirigir a la actividad de productos
                        Intent intent = new Intent(LoginUsuarioActivity.this, ProductosActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginUsuarioActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
