package com.example.proyectocomidas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



public class PerfilUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGuardar, btnContraseña;
    EditText nombre, email, direccion, telefono;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;
    View mView;
    EditText passwordAnterior, nuevoPassword, confirmarPassword;
    Button btn_CambiarContraseña;
    AlertDialog alertDialog;
    Boolean isGoogle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        init();

    }



    private void init(){

        this.firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        nombre = findViewById(R.id.nombreUsuario);
        email = findViewById(R.id.emailUsuario);
        direccion = findViewById(R.id.direccionUsuario);
        telefono = findViewById(R.id.telefonoUsuario);

        btnGuardar = findViewById(R.id.btnGuardarCambios);
        btnContraseña = findViewById(R.id.btnCambiarContraseña);

        btnGuardar.setOnClickListener(this);
        btnContraseña.setOnClickListener(this);

        obtenerDatosUsuario();

    }

    private void obtenerDatosUsuario(){


        // OBTENGO LOS DATOS DEL USUARIO LOGUEADO y los pongo en los editText del activity perfilUsuario
        firebaseFirestore.collection("Usuarios").whereEqualTo("email", firebaseAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){


                        String nombre = document.getString("nombre");
                        String email = document.getString("email");
                        String direccion = document.getString("direccion");
                        String telefono = document.getString("telefono");
                        isGoogle = document.getBoolean("google");

                        if(isGoogle){
                            btnContraseña.setVisibility(View.GONE);
                        }else{
                            btnContraseña.setVisibility(View.VISIBLE);
                        }


                        // PONGO LOS DATOS en los EditText
                        PerfilUsuarioActivity.this.nombre.setText(nombre);
                        PerfilUsuarioActivity.this.email.setText(email);
                        PerfilUsuarioActivity.this.direccion.setText(direccion);
                        PerfilUsuarioActivity.this.telefono.setText(telefono);

                        Log.e("EMAIL usuario logueado", email);

                    }


                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGuardarCambios:


                if (!Strings.isEmptyOrWhitespace(nombre.getText().toString()) && !Strings.isEmptyOrWhitespace(direccion.getText().toString()) && !Strings.isEmptyOrWhitespace(telefono.getText().toString())) {

                    final String nombreee = nombre.getText().toString().trim();
                    final String direccionnn = direccion.getText().toString().trim();
                    final String telefonooo = telefono.getText().toString().trim();

                    Usuario user = new Usuario(nombreee, firebaseAuth.getCurrentUser().getEmail(),direccionnn,telefonooo, isGoogle);

                    editarUsuario(user);



                } else {

                    Toast.makeText(getApplicationContext(), "Debes de rellenar todos los campos", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btnCambiarContraseña:

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                mView = getLayoutInflater().inflate(R.layout.dialog_cambiar_password, null);
                passwordAnterior = mView.findViewById(R.id.antiguoPassword);
                nuevoPassword = mView.findViewById(R.id.nuevoPassword);
                confirmarPassword = mView.findViewById(R.id.confirmarPassword);
                btn_CambiarContraseña = mView.findViewById(R.id.cambiarPassword_btn);

                mBuilder.setView(mView);
                alertDialog = mBuilder.create();
                alertDialog.show();

                btn_CambiarContraseña.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!Strings.isEmptyOrWhitespace(passwordAnterior.getText().toString()) && !Strings.isEmptyOrWhitespace(nuevoPassword.getText().toString()) && !Strings.isEmptyOrWhitespace(confirmarPassword.getText().toString())){
                            if(nuevoPassword.getText().toString().equals(confirmarPassword.getText().toString())) {

                                if(nuevoPassword.getText().toString().length() >= 6) {
                                    cambiarContraseña();
                                }else{
                                    Toast.makeText(getApplicationContext(), "La contraseña deben tener mínimo 6 caracteres", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Las contraseñas deben coincidir", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "No puedes dejar campos vacíos", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                break;

        }
    }

    private void cambiarContraseña(){
        dialog = ProgressDialog.show(this, "",
                "Cargando... espere por favor", true);

        AuthCredential credential = EmailAuthProvider.getCredential(firebaseAuth.getCurrentUser().getEmail(), passwordAnterior.getText().toString());
        firebaseAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    firebaseAuth.getCurrentUser().updatePassword(nuevoPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(), "La contraseña ha sido actualizada", Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                                dialog.dismiss();


                            } else {

                                Toast.makeText(getApplicationContext(), "Se ha producido un error al actualizar la contraseña", Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                                alertDialog.dismiss();
                            }
                        }
                    });

                } else {

                    Toast.makeText(getApplicationContext(), "La contraseña antigua que ha introducido es errónea", Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                }
            }
        });

    }



    private void editarUsuario(final Usuario user){

            // Para editar el nombreUsuario y modificar sus datos

            dialog = ProgressDialog.show(this, "",
                    "Cargando... espere por favor", true);

            firebaseFirestore.collection("Usuarios").whereEqualTo("email", firebaseAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        if (task.getResult().getDocuments().size() > 0) {
                            firebaseFirestore.collection("Usuarios").document(task.getResult().getDocuments().get(0).getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(getApplicationContext(), "Se han guardado los cambios", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "No se han podido guardar los cambios", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                }
            });

    }


}
