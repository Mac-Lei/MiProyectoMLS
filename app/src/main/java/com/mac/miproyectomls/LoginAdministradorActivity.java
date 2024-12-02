package com.mac.miproyectomls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginAdministradorActivity extends AppCompatActivity {

    private EditText correoAdministrador, contrasenaAdministrador;
    private Button iniciarSesionAdministrador;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginadmin);

        correoAdministrador = findViewById(R.id.correoadmin);
        contrasenaAdministrador = findViewById(R.id.contrasenaadmin);
        iniciarSesionAdministrador = findViewById(R.id.iniciarsesionadmin);

        dbHelper = new DatabaseHelper(this);

        iniciarSesionAdministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = correoAdministrador.getText().toString();
                String contrasena = contrasenaAdministrador.getText().toString();

                if (correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(LoginAdministradorActivity.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    boolean esAdmin = dbHelper.checkAdmin(correo, contrasena);

                    if (esAdmin) {
                        // Si las credenciales son correctas, redirigir a la actividad de gestión de productos
                        Intent intent = new Intent(LoginAdministradorActivity.this, GestionProductosActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginAdministradorActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
