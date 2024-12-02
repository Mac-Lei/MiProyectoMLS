package com.mac.miproyectomls;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductosActivity extends AppCompatActivity {

    // Declaración de variables
    private ListView listViewProductos;
    private EditText editCantidadSeleccionada;
    private TextView textSeleccionProducto, textTotalCompra;
    private Button btnConfirmarCompra;

    private ArrayAdapter<String> adapter;
    private ArrayList<Producto> productosList; // Lista de productos completos
    private DatabaseReference databaseReference;

    private ArrayList<Producto> productosSeleccionados; // Lista para productos seleccionados
    private double totalCompra = 0.0; // Total de la compra

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        // Inicializar vistas
        listViewProductos = findViewById(R.id.listViewProductos);
        editCantidadSeleccionada = findViewById(R.id.editCantidadSeleccionada);
        textSeleccionProducto = findViewById(R.id.textSeleccionProducto);
        textTotalCompra = findViewById(R.id.textTotalCompra);
        btnConfirmarCompra = findViewById(R.id.btnConfirmarCompra);

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        // Inicializar la lista y el adaptador
        productosList = new ArrayList<>();
        productosSeleccionados = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewProductos.setAdapter(adapter);

        // Cargar productos de Firebase
        cargarProductos();

        // Manejar selección de productos
        listViewProductos.setOnItemClickListener((parent, view, position, id) -> {
            Producto productoSeleccionado = productosList.get(position); // Obtenemos el producto completo
            if (productoSeleccionado != null) {
                textSeleccionProducto.setText("Producto seleccionado: " + productoSeleccionado.getNombre());
            }
        });

        // Manejar el botón de agregar producto
        btnConfirmarCompra.setOnClickListener(v -> agregarProductoACompra());
    }

    private void cargarProductos() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                productosList.clear();
                ArrayList<String> displayList = new ArrayList<>(); // Lista para mostrar en el ListView

                for (DataSnapshot productoSnapshot : snapshot.getChildren()) {
                    Producto producto = productoSnapshot.getValue(Producto.class);
                    if (producto != null) {
                        productosList.add(producto); // Añadir el producto completo
                        String productoInfo = "ID: " + producto.getId() +
                                "\nNombre: " + producto.getNombre() +
                                "\nPrecio: $" + producto.getPrecio() +
                                "\nCantidad: " + producto.getCantidad();
                        displayList.add(productoInfo); // Añadir solo la información para mostrar
                    }
                }
                adapter.clear();
                adapter.addAll(displayList); // Actualizar el adaptador con la lista de cadenas
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
                Toast.makeText(ProductosActivity.this, "Error al cargar los productos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarProductoACompra() {
        String cantidadStr = editCantidadSeleccionada.getText().toString();
        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingrese una cantidad válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);
        if (cantidad <= 0) {
            Toast.makeText(this, "Cantidad no válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Seleccionar el producto actual (esto puede depender de la implementación)
        Producto productoSeleccionado = productosList.get(0); // Por ejemplo, seleccionamos el primer producto de la lista. Deberías obtenerlo basado en la selección.

        // Verificar que haya suficiente cantidad
        if (cantidad > productoSeleccionado.getCantidad()) {
            Toast.makeText(this, "Cantidad supera el stock disponible.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Agregar el producto a la lista de productos seleccionados
        productosSeleccionados.add(productoSeleccionado);
        // Actualizar el total de la compra
        totalCompra += productoSeleccionado.getPrecio() * cantidad;

        // Actualizar el texto del total de la compra
        textTotalCompra.setText("Total: $" + totalCompra);

        // Limpiar el campo de cantidad
        editCantidadSeleccionada.setText("");

        Toast.makeText(this, "Producto agregado a la compra.", Toast.LENGTH_SHORT).show();
    }

    private void confirmarCompra() {
        if (productosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Debe seleccionar al menos un producto.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la fecha actual (puedes cambiar el formato si lo necesitas)
        String fecha = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        // Crear una instancia de Compra para cada producto seleccionado
        DatabaseReference comprasRef = FirebaseDatabase.getInstance().getReference("compras");
        for (Producto producto : productosSeleccionados) {
            Compra compra = new Compra(
                    producto.getId(),
                    producto.getNombre(),
                    1, // Puedes cambiar esto según la cantidad ingresada para cada producto
                    producto.getPrecio(),
                    fecha
            );

            comprasRef.push().setValue(compra)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ProductosActivity.this, "Compra confirmada y registrada.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProductosActivity.this, "Error al registrar la compra.", Toast.LENGTH_SHORT).show();
                    });
        }

        // Limpiar la lista de productos seleccionados después de confirmar la compra
        productosSeleccionados.clear();
    }
}