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

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import java.util.ArrayList;

public class ProductosActivity extends AppCompatActivity {

    private ListView listViewProductos;
    private EditText editCantidadSeleccionada;
    private TextView textSeleccionProducto, textTotalCompra;
    private Button btnAgregarProducto, btnConfirmarCompra;

    private ArrayAdapter<String> adapter;
    private ArrayList<Producto> productosList;
    private ArrayList<Producto> productosSeleccionados;
    private DatabaseReference databaseReference;

    private double totalCompra = 0.0;
    private int selectedPosition = -1;

    // MQTTHandler
    private MQTTHandler mqttHandler;
    private static final String MQTT_TOPIC = "supermercado/productos";

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

        // Inicializar MQTTHandler
        mqttHandler = new MQTTHandler(new MQTTHandler.MQTTHandlerCallback() {
            @Override
            public void onMessageReceived(String topic, String message) {
                // Manejar mensajes si es necesario
                Log.i("MQTT", "Mensaje recibido: " + message);
            }

            @Override
            public void onConnectionLost(Throwable cause) {
                Log.e("MQTT", "Conexión perdida: " + cause.getMessage());
            }

            @Override
            public void onDeliveryComplete(IMqttDeliveryToken token) {
                Log.i("MQTT", "Mensaje entregado.");
            }
        });
        mqttHandler.connect();

        cargarProductos();

        // Manejar selección de productos
        listViewProductos.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            Producto productoSeleccionado = productosList.get(position);
            textSeleccionProducto.setText("Producto seleccionado: " + productoSeleccionado.getNombre());
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
        if (selectedPosition == -1) {
            Toast.makeText(this, "Seleccione un producto primero.", Toast.LENGTH_SHORT).show();
            return;
        }

        String cantidadStr = editCantidadSeleccionada.getText().toString();
        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingrese una cantidad válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);
        Producto productoSeleccionado = productosList.get(selectedPosition);

        if (cantidad > productoSeleccionado.getCantidad()) {
            Toast.makeText(this, "Cantidad supera el stock disponible.", Toast.LENGTH_SHORT).show();
            return;
        }

        Producto productoCarrito = new Producto(productoSeleccionado.getId(),
                productoSeleccionado.getNombre(),
                productoSeleccionado.getPrecio(),
                cantidad);

        productosSeleccionados.add(productoCarrito);
        productoSeleccionado.setCantidad(productoSeleccionado.getCantidad() - cantidad);

        totalCompra += productoCarrito.getPrecio() * cantidad;
        textTotalCompra.setText("Total: $" + totalCompra);

        // Limpiar campo de cantidad
        editCantidadSeleccionada.setText("");

        Toast.makeText(this, "Producto agregado al carrito.", Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }

    private void confirmarCompra() {
        if (productosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Carrito vacío.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generar resumen de compra y enviar a MQTT
        String resumenCompra = generarResumenCompra();
        Log.e("datos a mqtt", resumenCompra);
        mqttHandler.publishMessage(MQTT_TOPIC, resumenCompra);

        // Pasar datos al resumen de compra
        Intent intent = new Intent(this, activity_resumen_compra.class);
        intent.putExtra("productos", productosSeleccionados);
        intent.putExtra("totalCompra", totalCompra);
        startActivity(intent);

        Toast.makeText(this, "Compra confirmada.", Toast.LENGTH_SHORT).show();
    }

    private String generarResumenCompra() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("{ \"productos\": [");
        for (Producto producto : productosSeleccionados) {
            resumen.append("{")
                    .append("\"id\": \"").append(producto.getId()).append("\", ")
                    .append("\"nombre\": \"").append(producto.getNombre()).append("\", ")
                    .append("\"cantidad\": ").append(producto.getCantidad()).append(", ")
                    .append("\"precio\": ").append(producto.getPrecio())
                    .append("},");
        }
        resumen.setLength(resumen.length() - 1); // Eliminar la última coma
        resumen.append("], \"total\": ").append(totalCompra).append(" }");

        return resumen.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttHandler.disconnect();
    }
}
