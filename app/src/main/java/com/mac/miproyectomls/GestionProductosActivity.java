package com.mac.miproyectomls;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GestionProductosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_productos);

        // Asociar elementos de la interfaz
        EditText Nombre = findViewById(R.id.nombre_producto);
        EditText Precio = findViewById(R.id.precio_producto);
        EditText Cantidad = findViewById(R.id.cantidad_producto);
        Button btnGuardarProducto = findViewById(R.id.btn_guardar_producto);

        // Inicializar referencia de Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        // Configurar evento de clic para el botón
        btnGuardarProducto.setOnClickListener(v -> {
            String nombre = Nombre.getText().toString().trim(); // Método corregido
            String precio = Precio.getText().toString().trim();
            String cantidad = Cantidad.getText().toString().trim();

            if (nombre.isEmpty() || precio.isEmpty() || cantidad.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // Validar y guardar el producto
                    double precioDouble = Double.parseDouble(precio);
                    int cantidadInt = Integer.parseInt(cantidad);
                    guardarProducto(databaseReference, nombre, precioDouble, cantidadInt);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Precio o cantidad inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void guardarProducto(DatabaseReference databaseReference, String nombre, double precio, int cantidad) {
        // Generar un ID único para el producto
        String id = databaseReference.push().getKey();

        if (id == null) {
            Toast.makeText(this, "Error al generar ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto del producto
        Producto producto = new Producto(nombre, precio, cantidad);

        // Guardar en la base de datos
        databaseReference.child(id).setValue(producto)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Producto guardado con éxito", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar el producto", Toast.LENGTH_SHORT).show());
    }
}
