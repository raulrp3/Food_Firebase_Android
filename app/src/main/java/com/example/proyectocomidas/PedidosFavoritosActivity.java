package com.example.proyectocomidas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PedidosFavoritosActivity extends AppCompatActivity {

    private List<PedidoFavorito> orders;
    private RecyclerView rvOrdersFav;
    private PedidosFavortitosAdapter ordersAdapter;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private String idUser;
    private List<Producto> products;
    private List<String> idProducts;
    private List<Producto> productsOrder;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_favoritos);

        initUI();
    }

    private void initUI(){
        preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        rvOrdersFav = findViewById(R.id.rvPedidosFav);
        rvOrdersFav.setHasFixedSize(true);
        rvOrdersFav.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        orders = new ArrayList<>();
        products = new ArrayList<>();
        idProducts = new ArrayList<>();
        productsOrder = new ArrayList<>();

        mFirestore.collection("Productos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        String id = document.getId();
                        String name = document.getString("nombre");
                        String description = document.getString("descripcion");
                        Boolean available = document.getBoolean("disponible");
                        String image = document.getString("foto");
                        String idCatgeory = document.getString("idcategorias");
                        Double precio = document.getDouble("precio");
                        products.add(new Producto(id, name, description, image, available, idCatgeory, precio));
                    }
                }
            }
        });

        mFirestore.collection("Usuarios").whereEqualTo("email", mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    idUser = task.getResult().getDocuments().get(0).getId();

                    mFirestore.collection("Pedidos").whereEqualTo("idUser", idUser).whereEqualTo("favorito", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document: task.getResult()){
                                    String id = document.getId();
                                    String name = document.getString("nombrePedido");
                                    String comments = document.getString("comentarios");
                                    orders.add(new PedidoFavorito(id, name, comments));
                                }

                                ordersAdapter = new PedidosFavortitosAdapter(PedidosFavoritosActivity.this, orders, new CustomClickPedido() {
                                    @Override
                                    public void onClick(View view, int index) {
                                        idProducts.clear();
                                        productsOrder.clear();
                                        String id = orders.get(index).getId();
                                        final String comments = orders.get(index).getComments();
                                        mFirestore.collection("PedidoProductos").whereEqualTo("idPedido", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot document: task.getResult()){
                                                        final String idProducto = document.getString("idProducto");
                                                        idProducts.add(idProducto);
                                                    }

                                                    for (String idProduct: idProducts) {
                                                        for (Producto product : products) {
                                                            if (idProduct.equals(product.getId())) {
                                                                productsOrder.add(product);
                                                            }
                                                        }
                                                    }

                                                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(PedidosFavoritosActivity.this);
                                                    final View mView = getLayoutInflater().inflate(R.layout.dialog_detalle_pedidos, null);
                                                    RecyclerView rvDetalle = mView.findViewById(R.id.rvDetallePedido);
                                                    rvDetalle.setHasFixedSize(true);
                                                    rvDetalle.setLayoutManager(new LinearLayoutManager(PedidosFavoritosActivity.this));
                                                    DetallePedidoAdapter mAdapter = new DetallePedidoAdapter(PedidosFavoritosActivity.this, productsOrder);
                                                    rvDetalle.setAdapter(mAdapter);

                                                    Button addProductsButton = mView.findViewById(R.id.btnAddProducts);
                                                    addProductsButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            SharedPreferences.Editor editor = preferences.edit();
                                                            editor.clear();
                                                            ProductosCompra pc = new ProductosCompra(productsOrder);
                                                            String json = pc.toJson();
                                                            editor.putString("productos", json);
                                                            editor.putString("observaciones", comments);
                                                            editor.commit();

                                                            Snackbar snackbar = Snackbar.make(mView, "¡Productos añadidos con éxito!", Snackbar.LENGTH_LONG);
                                                            snackbar.show();
                                                            //Intent intent = new Intent(PedidosFavoritosActivity.this, CestaCompraActivity.class);
                                                            //startActivity(intent);
                                                        }
                                                    });

                                                    mBuilder.setView(mView);
                                                    AlertDialog alertDialog = mBuilder.create();
                                                    alertDialog.show();
                                                }
                                            }
                                        });
                                    }
                                });

                                rvOrdersFav.setAdapter(ordersAdapter);
                            }
                        }
                    });
                }
            }
        });
    }
}
