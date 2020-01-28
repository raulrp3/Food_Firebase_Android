package com.example.proyectocomidas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectocomidas.adapters.CategoriaAdapter;
import com.example.proyectocomidas.adapters.ComentariosAdapter;
import com.example.proyectocomidas.models.Categoria;
import com.example.proyectocomidas.models.Comentario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private List<Categoria> categorias;
    private RecyclerView rvCategorias;
    private CategoriaAdapter categoriaAdapter;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMenu();
        initUI();
        obtenerCategorias();

    }

    private void initMenu(){
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if(mAuth.getCurrentUser() != null){
            navigationView.inflateMenu(R.menu.menu_usuario);
        }else{
            navigationView.inflateMenu(R.menu.menu_anonimo);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemPerfil) {
            startActivity(new Intent(MainActivity.this, PerfilUsuarioActivity.class));
        } else if (id == R.id.itemLogin) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else if (id == R.id.itemFavoritos) {
            startActivity(new Intent(MainActivity.this, PedidosFavoritosActivity.class));
        } else if (id == R.id.itemComentarios) {
            startActivity(new Intent(MainActivity.this, ComentariosActivity.class));
        } else if (id == R.id.itemCarrito) {
            startActivity(new Intent(MainActivity.this, CestaCompraActivity.class));
        } else if (id == R.id.itemCerrarSesion) {
            mAuth.signOut();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initUI(){
        /*
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        */

        rvCategorias = findViewById(R.id.rvCategorias);
        rvCategorias.setHasFixedSize(true);
        rvCategorias.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        boolean flag = false; // valor por defecto si aun no se ha tomado el username
    }

    private void obtenerCategorias(){

        categorias = new ArrayList<>();

        mFirestore.collection("Categorias").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        String name = document.getString("nombre");
                        String urlFoto = document.getData().get("imagen").toString();
                        Log.e("error", document.getId());
                        String idCategoria = document.getId();
                        categorias.add(new Categoria(name, urlFoto, idCategoria));
                    }

                    categoriaAdapter = new CategoriaAdapter(MainActivity.this, categorias);
                    rvCategorias.setAdapter(categoriaAdapter);
                }
            }
        });

    }




}
