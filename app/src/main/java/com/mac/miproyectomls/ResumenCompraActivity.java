package com.mac.miproyectomls;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResumenCompraActivity extends AppCompatActivity {

    private ListView listViewResumen;
    private TextView textTotalResumen;
    private Button btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_compra);

        listViewResumen = findViewById(R.id.listViewResumen);
        textTotalResumen = findViewById(R.id.textTotalResumen);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Recuperar datos enviados
        Intent intent = getIntent();
        ArrayList<Producto> productosSeleccionados = (ArrayList<Producto>) intent.getSerializableExtra("productos");
        double totalCompra = intent.getDoubleExtra("totalCompra", 0.0);

        ArrayList<String> resumenList = new ArrayList<>();
        for (Producto producto : productosSeleccionados) {
            resumenList.add(producto.getNombre() + " - $" + producto.getPrecio() + " x " + producto.getCantidad());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resumenList);
        listViewResumen.setAdapter(adapter);

        textTotalResumen.setText("Total: $" + totalCompra);

        btnCerrarSesion.setOnClickListener(v -> {
            Intent loginIntent = new Intent(this, HomeActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        });
    }
}
