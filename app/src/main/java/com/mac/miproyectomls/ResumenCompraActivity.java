package com.mac.miproyectomls;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResumenCompraActivity extends AppCompatActivity {

    private ListView listViewResumen;
    private TextView textTotalResumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_compra);

        listViewResumen = findViewById(R.id.listViewResumen);
        textTotalResumen = findViewById(R.id.textTotalResumen);

        ArrayList<Producto> productosSeleccionados = (ArrayList<Producto>) getIntent().getSerializableExtra("productos");
        double totalCompra = getIntent().getDoubleExtra("totalCompra", 0.0);

        if (productosSeleccionados != null && !productosSeleccionados.isEmpty()) {
            ArrayList<String> displayList = new ArrayList<>();
            for (Producto producto : productosSeleccionados) {
                displayList.add("Producto: " + producto.getNombre() +
                        "\nCantidad: " + producto.getCantidad() +
                        "\nSubtotal: $" + (producto.getPrecio() * producto.getCantidad()));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
            listViewResumen.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No hay productos en el carrito.", Toast.LENGTH_SHORT).show();
        }

        textTotalResumen.setText("Total: $" + totalCompra);
        Toast.makeText(this, "Compra realizada con éxito.", Toast.LENGTH_LONG).show();
    }
}
