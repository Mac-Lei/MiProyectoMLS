package com.mac.miproyectomls;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    private ListView listViewProductos;
    private ArrayAdapter<String> adapter;
    private List<String> productosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productos);

        listViewProductos = findViewById(R.id.listViewProductos); // ID del ListView en el layout
        productosList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productosList);
        listViewProductos.setAdapter(adapter);

        cargarProductos();
    }

    private void cargarProductos() {
        // Referencia a la tabla "productos" en Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
