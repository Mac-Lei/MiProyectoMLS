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

    private double totalCompra = 0.0;
    private Producto productoSeleccionado; // Objeto para el producto seleccionado

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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>()); // Aquí usamos una lista de cadenas para el adaptador
        listViewProductos.setAdapter(adapter);

        // Cargar productos de Firebase
        cargarProductos();

        // Manejar selección de productos
        listViewProductos.setOnItemClickListener((parent, view, position, id) -> {
            productoSeleccionado = productosList.get(position); // Obtenemos el producto completo
            if (productoSeleccionado != null) {
                textSeleccionProducto.setText("Producto seleccionado: " + productoSeleccionado.getNombre());
            }
        });

        // Manejar el botón de confirmar compra
        btnConfirmarCompra.setOnClickListener(v -> confirmarCompra());
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

    private void confirmarCompra() {
        if (productoSeleccionado == null) {
            Toast.makeText(this, "Debe seleccionar un producto.", Toast.LENGTH_SHORT).show();
            return;
        }

        String cantidadStr = editCantidadSeleccionada.getText().toString();
        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingrese una cantidad válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);
        if (cantidad <= 0 || cantidad > productoSeleccionado.getCantidad()) {
            Toast.makeText(this, "Cantidad no válida o supera el stock disponible.", Toast.LENGTH_SHORT).show();
            return;
        }

        totalCompra += productoSeleccionado.getPrecio() * cantidad;
        textTotalCompra.setText("Total: $" + totalCompra);
        Toast.makeText(this, "Compra confirmada.", Toast.LENGTH_SHORT).show();
    }
}
