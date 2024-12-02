package com.mac.miproyectomls;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    private ArrayAdapter<String> adapter;
    private ArrayList<String> productosList;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        // Inicializar vistas
        listViewProductos = findViewById(R.id.listViewProductos);

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        // Inicializar la lista y el adaptador
        productosList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productosList);
        listViewProductos.setAdapter(adapter);

        // Cargar productos de Firebase
        cargarProductos();
    }

    private void cargarProductos() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                productosList.clear();
                for (DataSnapshot productoSnapshot : snapshot.getChildren()) {
                    Producto producto = productoSnapshot.getValue(Producto.class);
                    if (producto != null) {
                        String productoInfo = "ID: " + producto.getId() +
                                "\nNombre: " + producto.getNombre() +
                                "\nPrecio: $" + producto.getPrecio() +
                                "\nCantidad: " + producto.getCantidad();
                        productosList.add(productoInfo);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
                Toast.makeText(ProductosActivity.this, "Error al cargar los productos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
