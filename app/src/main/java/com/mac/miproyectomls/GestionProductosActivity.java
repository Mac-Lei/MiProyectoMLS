package com.mac.miproyectomls;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GestionProductosActivity extends AppCompatActivity {

    private EditText editId, editNombre, editPrecio, editCantidad;
    private Button btnAgregar, btnActualizar, btnEliminar;
    private ListView listViewProductos;

    private DatabaseReference databaseReference;
    private ArrayList<Producto> productosList; // Lista de objetos Producto
    private ArrayAdapter<String> adapter; // Adaptador para mostrar los productos en el ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_productos);

        // Inicializar vistas
        editId = findViewById(R.id.editId);
        editNombre = findViewById(R.id.editNombre);
        editPrecio = findViewById(R.id.editPrecio);
        editCantidad = findViewById(R.id.editCantidad);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);
        listViewProductos = findViewById(R.id.listViewProductos);

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        // Inicializar lista y adaptador
        productosList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewProductos.setAdapter(adapter);

        // Cargar productos de Firebase
        cargarProductos();

        // Configurar botones
        btnAgregar.setOnClickListener(view -> agregarProducto());
        btnActualizar.setOnClickListener(view -> actualizarProducto());
        btnEliminar.setOnClickListener(view -> eliminarProducto());

        // Configurar selección en el ListView
        listViewProductos.setOnItemClickListener((adapterView, view, position, id) -> seleccionarProducto(position));
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
                        displayList.add(
                                "ID: " + producto.getId() +
                                        "\nNombre: " + producto.getNombre() +
                                        "\nPrecio: $" + producto.getPrecio() +
                                        "\nCantidad: " + producto.getCantidad()
                        );
                    }
                }
                adapter.clear();
                adapter.addAll(displayList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
                Toast.makeText(GestionProductosActivity.this, "Error al cargar los productos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void seleccionarProducto(int position) {
        Producto producto = productosList.get(position); // Obtener el producto seleccionado
        editId.setText(producto.getId());
        editNombre.setText(producto.getNombre());
        editPrecio.setText(String.valueOf(producto.getPrecio()));
        editCantidad.setText(String.valueOf(producto.getCantidad()));
    }

    private void agregarProducto() {
        String id = editId.getText().toString().trim();
        String nombre = editNombre.getText().toString().trim();
        String precioStr = editPrecio.getText().toString().trim();
        String cantidadStr = editCantidad.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(precioStr) || TextUtils.isEmpty(cantidadStr)) {
            Toast.makeText(this, "Complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        int precio = Integer.parseInt(precioStr);
        int cantidad = Integer.parseInt(cantidadStr);

        Producto producto = new Producto(id, nombre, precio, cantidad);
        databaseReference.child(id).setValue(producto)
                .addOnSuccessListener(unused -> Toast.makeText(this, "Producto agregado.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al agregar el producto.", Toast.LENGTH_SHORT).show());
    }

    private void actualizarProducto() {
        String id = editId.getText().toString().trim();
        String nombre = editNombre.getText().toString().trim();
        String precioStr = editPrecio.getText().toString().trim();
        String cantidadStr = editCantidad.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "El ID es obligatorio para actualizar.", Toast.LENGTH_SHORT).show();
            return;
        }

        int precio = Integer.parseInt(precioStr);
        int cantidad = Integer.parseInt(cantidadStr);

        Producto producto = new Producto(id, nombre, precio, cantidad);
        databaseReference.child(id).setValue(producto)
                .addOnSuccessListener(unused -> Toast.makeText(this, "Producto actualizado.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar el producto.", Toast.LENGTH_SHORT).show());
    }

    private void eliminarProducto() {
        String id = editId.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "El ID es obligatorio para eliminar.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(id).removeValue()
                .addOnSuccessListener(unused -> Toast.makeText(this, "Producto eliminado.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar el producto.", Toast.LENGTH_SHORT).show());
    }
}
