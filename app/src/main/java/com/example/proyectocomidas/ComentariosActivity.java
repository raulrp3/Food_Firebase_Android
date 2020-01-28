package com.example.proyectocomidas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.proyectocomidas.Assistance;
//import com.example.proyectocomidas.Assists;
import com.example.proyectocomidas.adapters.ComentariosAdapter;
import com.example.proyectocomidas.models.Comentario;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ComentariosActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Comentario> comentarios;
    private RecyclerView rvComentarios;
    private ComentariosAdapter comentarioAdapter;
    private FirebaseFirestore mFirestore;
    FirebaseAuth firebaseAuth;

    FirebaseUserMetadata metadata;

    AlertDialog alertDialog;
    View mView;
    Button addComentario;
    Button enviarComentarios;
    EditText escribeComentario;
    ProgressDialog dialog;

    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        initUI();

    }

    @SuppressLint("WrongViewCast")
    private void initUI() {

        mFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        rvComentarios = findViewById(R.id.rvComentarios);
        rvComentarios.setHasFixedSize(true);
        rvComentarios.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        addComentario = findViewById(R.id.btnAddComentario);
        addComentario.setOnClickListener(this);

        obtenerComentarios();

        comprobarSiHayUsuarioLogueado();


        boolean flag = false; // valor por defecto si aun no se ha tomado el username


    }

    private void comprobarSiHayUsuarioLogueado() {

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
       if (currentUser == null) {

           addComentario.setVisibility(View.GONE);

           // Pero si esta logueado, obtengo el nombre del nombreUsuario logueado para pasarselo al modal y asi luego poder obtener tambien el nombre del nombreUsuario
           //que ha escrito el comentario

       } else {
           // OBTENGO el nombre DEL USUARIO LOGUEADO
           mFirestore.collection("Usuarios").whereEqualTo("email", firebaseAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()){
                       for (QueryDocumentSnapshot document: task.getResult()){

                           String nombre = document.getString("nombre");

                           usuario = nombre;

                           Log.e("nombre nombreUsuario", nombre);

                       }


                   }
               }
           });

       }

    }


    private void obtenerComentarios() {

        comentarios = new ArrayList<>();

        mFirestore.collection("Comentarios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String comentario = document.getString("comentario");
                        String usuario = document.getString("usuario");

                        Log.e("error", document.getId());
                        comentarios.add(new Comentario(comentario,usuario));
                    }

                    comentarioAdapter = new ComentariosAdapter(ComentariosActivity.this, comentarios);
                    rvComentarios.setAdapter(comentarioAdapter);
                }
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnAddComentario:


                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                    mView = getLayoutInflater().inflate(R.layout.dialog_agregar_comentario, null);
                    escribeComentario = mView.findViewById(R.id.et_escribeComentario);
                    enviarComentarios = mView.findViewById(R.id.btnEnviarComentarioDialog);


                    mBuilder.setView(mView);
                    alertDialog = mBuilder.create();
                    alertDialog.show();

                    enviarComentarios.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(!Strings.isEmptyOrWhitespace(escribeComentario.getText().toString())) {

                                //Envio el comentario al modal y el nombre de nombreUsuario que lo ha escrito
                                Comentario user = new Comentario(escribeComentario.getText().toString(), usuario);

                                addComentario(user);


                            }else{
                                Toast.makeText(getApplicationContext(), "No puede dejar campos vacíos", Toast.LENGTH_LONG).show();
                            }

                        }
                    });




                break;

        }

    }

    private void addComentario(final Comentario user){

        dialog = ProgressDialog.show(this, "",
                "Cargando... espere por favor", true);

        mFirestore.collection("Comentarios").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                obtenerComentarios(); // actualizo el listView, obteniendo los datos añadidos y los que ya habia añadidos

                Toast.makeText(getApplicationContext(), "Se ha añadido el comentario", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                alertDialog.dismiss();


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error al añadir el comentario", Toast.LENGTH_LONG).show();
                Log.i("pruebas", e.getMessage());
            }
        });


    }

}
