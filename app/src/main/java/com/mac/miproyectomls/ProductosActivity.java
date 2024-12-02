package com.mac.miproyectomls;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private ListView listViewProductos;
    private EditText editCantidadSeleccionada;
    private TextView textSeleccionProducto, textTotalCompra;
    private Button btnAgregarProducto, btnConfirmarCompra;

    private ArrayAdapter<String> adapter;
    private ArrayList<Producto> productosList;
    private ArrayList<Producto> productosSeleccionados; // Carrito de compras
    private DatabaseReference databaseReference;

    private double totalCompra = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        // Inicializar vistas
        listViewProductos = findViewById(R.id.listViewProductos);
        editCantidadSeleccionada = findViewById(R.id.editCantidadSeleccionada);
        textSeleccionProducto = findViewById(R.id.textSeleccionProducto);
        textTotalCompra = findViewById(R.id.textTotalCompra);
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto);
        btnConfirmarCompra = findViewById(R.id.btnConfirmarCompra);

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        // Inicializar listas y adaptador
        productosList = new ArrayList<>();
        productosSeleccionados = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewProductos.setAdapter(adapter);

        cargarProductos();

        // Manejar selección de productos
        listViewProductos.setOnItemClickListener((parent, view, position, id) -> {
            Producto productoSeleccionado = productosList.get(position);
            if (productoSeleccionado != null) {
                textSeleccionProducto.setText("Producto seleccionado: " + productoSeleccionado.getNombre());
            }
        });

        // Botón para agregar producto al carrito
        btnAgregarProducto.setOnClickListener(v -> agregarProductoACompra());

        // Botón para confirmar compra
        btnConfirmarCompra.setOnClickListener(v -> confirmarCompra());
    }

    private void cargarProductos() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                productosList.clear();
                ArrayList<String> displayList = new ArrayList<>();

                for (DataSnapshot productoSnapshot : snapshot.getChildren()) {
                    Producto producto = productoSnapshot.getValue(Producto.class);
                    if (producto != null) {
                        productosList.add(producto);
                        displayList.add("ID: " + producto.getId() +
                                "\nNombre: " + producto.getNombre() +
                                "\nPrecio: $" + producto.getPrecio() +
                                "\nCantidad: " + producto.getCantidad());
                    }
                }
                adapter.clear();
                adapter.addAll(displayList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
                Toast.makeText(ProductosActivity.this, "Error al cargar productos.", Toast.LENGTH_SHORT).show();
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
        int position = listViewProductos.getCheckedItemPosition();
        if (position < 0 || position >= productosList.size()) {
            Toast.makeText(this, "Seleccione un producto.", Toast.LENGTH_SHORT).show();
            return;
        }

        Producto productoSeleccionado = productosList.get(position);
        if (cantidad > productoSeleccionado.getCantidad()) {
            Toast.makeText(this, "Cantidad supera el stock disponible.", Toast.LENGTH_SHORT).show();
            return;
        }

        productoSeleccionado.setCantidad(cantidad);
        productosSeleccionados.add(productoSeleccionado);

        totalCompra += productoSeleccionado.getPrecio() * cantidad;
        textTotalCompra.setText("Total: $" + totalCompra);

        Toast.makeText(this, "Producto agregado al carrito.", Toast.LENGTH_SHORT).show();
    }

    private void confirmarCompra() {
        if (productosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Carrito vacío.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ResumenCompraActivity.class);
        intent.putExtra("productos", productosSeleccionados);
        intent.putExtra("totalCompra", totalCompra);
        startActivity(intent);
    }
}
