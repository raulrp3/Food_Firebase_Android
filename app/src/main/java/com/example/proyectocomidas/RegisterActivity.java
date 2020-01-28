package com.example.proyectocomidas;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText txName, txEmail, txAddress, txPhone, txPassword, txPasswordAgain;
    private Button btnSave;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = txName.getText().toString().trim();
                final String email = txEmail.getText().toString().trim();
                final String address = txAddress.getText().toString().trim();
                final String phone = txPhone.getText().toString().trim();
                final String password = txPassword.getText().toString().trim();
                String passwordAgain = txPasswordAgain.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !address.isEmpty() && !phone.isEmpty() && !password.isEmpty() && !passwordAgain.isEmpty()){
                    if (password.equals(passwordAgain)){
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Usuario user = new Usuario(name, email, address, phone, false);
                                    addNewUser(user);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Error al realizar registro", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "¡Las contraseñas no coinciden!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "¡Campos obligatorios!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initUI(){
        txName = findViewById(R.id.txName);
        txEmail = findViewById(R.id.txEmail);
        txAddress = findViewById(R.id.txAddress);
        txPhone = findViewById(R.id.txPhone);
        txPassword = findViewById(R.id.txPassword);
        txPasswordAgain = findViewById(R.id.txPasswordAgain);
        btnSave = findViewById(R.id.btnSave);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void addNewUser(Usuario user){
        mFirestore.collection("Usuarios").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
