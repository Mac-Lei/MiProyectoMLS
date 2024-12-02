package com.mac.miproyectomls;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class activity_resumen_compra extends AppCompatActivity {

    private ListView listViewResumen;
    private TextView textTotalResumen;
    private Button btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_compra);

        // Inicializar vistas
        listViewResumen = findViewById(R.id.listViewResumen);
        textTotalResumen = findViewById(R.id.textTotalResumen);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Obtener datos pasados desde ProductosActivity
        Intent intent = getIntent();
        ArrayList<Producto> productosSeleccionados = (ArrayList<Producto>) intent.getSerializableExtra("productos");
        double totalCompra = intent.getDoubleExtra("totalCompra", 0.0);

        // Configurar la lista de productos seleccionados
        ArrayList<String> detallesProductos = new ArrayList<>();
        if (productosSeleccionados != null) {
            for (Producto producto : productosSeleccionados) {
                detallesProductos.add(producto.getNombre() + " - Cantidad: " + producto.getCantidad() +
                        " - Subtotal: $" + (producto.getCantidad() * producto.getPrecio()));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, detallesProductos);
        listViewResumen.setAdapter(adapter);

        // Mostrar el total de la compra
        textTotalResumen.setText("Total: $" + totalCompra);

        // Configurar botón de cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> {
            Intent intentHome = new Intent(activity_resumen_compra.this, HomeActivity.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentHome);
            finish(); // Cierra la actividad actual
        });
    }
}
