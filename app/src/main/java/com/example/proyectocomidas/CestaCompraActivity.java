package com.example.proyectocomidas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CestaCompraActivity extends AppCompatActivity {

    private RecyclerView rvCestaCarrito;
    private ProductosCarritoAdapter mAdapter;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    ProductosCompra productos;
    SharedPreferences preferences;
    Gson gson;
    Button pagarBtn;
    Double precioTotal;
    TextView txtPrecioTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta_compra);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String json = preferences.getString("productos", "");
        Log.i("PRUEBA", json);
        productos = new ProductosCompra();

        if(! json.equals("")) {
            productos = new ProductosCompra(productos.fromJSON(json).getListaProductos());
        }

        txtPrecioTotal = findViewById(R.id.precioTotal);
        precioTotal = 0.0;

        for (Producto producto: productos.getListaProductos()) {
            precioTotal += producto.getPrecio();
        }

        txtPrecioTotal.setText(precioTotal+"€");

        mAdapter = new ProductosCarritoAdapter(this, productos.getListaProductos(), mStorage, mAuth, precioTotal, txtPrecioTotal);
        rvCestaCarrito.setAdapter(mAdapter);

    }

    private void init(){
        rvCestaCarrito = findViewById(R.id.rvCestaCompra);
        rvCestaCarrito.setHasFixedSize(true);
        rvCestaCarrito.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        gson = new Gson();
        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        pagarBtn = findViewById(R.id.pagarBtn);
        pagarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productos.getListaProductos().size() <= 0){
                    Toast.makeText(CestaCompraActivity.this,"La cesta de la compra no puede estar vacía.",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(CestaCompraActivity.this, DatosCompraActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
