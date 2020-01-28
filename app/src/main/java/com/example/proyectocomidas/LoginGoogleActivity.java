package com.example.proyectocomidas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

public class LoginGoogleActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authstateListener;
    private static final int CODE = 1;
    private SignInButton buttonGoogle;
    private LoginButton buttonFacebook;
    private Button btnSignOut;
    private CallbackManager callbackmanager;
    private String btnPressed = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_google);

        callbackmanager = CallbackManager.Factory.create();

        initUI();

        buttonFacebook.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Inicio de sesión cancelado",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"¡Error!",Toast.LENGTH_SHORT).show();
            }
        });


        buttonGoogle.setOnClickListener(this);
        buttonFacebook.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);

        configureSignIn();

    }

    private void configureSignIn(){
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,options)
                .build();
    }

    private void initUI(){
        firebaseAuth = FirebaseAuth.getInstance();
        buttonGoogle = findViewById(R.id.button_google);
        buttonGoogle.setSize(SignInButton.SIZE_WIDE);
        btnSignOut = findViewById(R.id.btn_SignOut);
        buttonFacebook = findViewById(R.id.button_facebook);
        buttonFacebook.setReadPermissions("email", "public_profile");

        changeUI(firebaseAuth.getCurrentUser());

    }

    private void changeUI(FirebaseUser user){

        if(user == null && Profile.getCurrentProfile() == null){
            buttonGoogle.setVisibility(View.VISIBLE);
            buttonFacebook.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        }else{
            buttonGoogle.setVisibility(View.GONE);
            buttonFacebook.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
        }
    }

    private void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent,CODE);
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);


        if(btnPressed == getString(R.string.google_pressed_code)){
            if (requestCode == CODE){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()){
                    GoogleSignInAccount account = result.getSignInAccount();

                    Toast.makeText(getApplicationContext(),"¡Has iniciado sesión correctamente!",Toast.LENGTH_SHORT).show();
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                    firebaseAuthWithGoogle(credential,account);
                }else{
                    Toast.makeText(getApplicationContext(),"¡Error!",Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            callbackmanager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential, final GoogleSignInAccount account){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                   //refresco la pantalla para mostrar cambio de botón
                    finish();
                    startActivity(getIntent());

                }else{
                    Toast.makeText(getApplicationContext(),"Autentiación fallida",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onStart(){
        super.onStart();
        changeUI(firebaseAuth.getCurrentUser());

        //Obtener información del nombreUsuario logueado con google

        if(firebaseAuth.getCurrentUser() != null){
            Log.i("Account", firebaseAuth.getCurrentUser().getEmail());
        }

        //Una forma de obtener información del nombreUsuario logueado en facebook

        if(Profile.getCurrentProfile() != null){
            Log.i("Account", Profile.getCurrentProfile().getName());

        }

        //Otra forma de obtener información del nombreUsuario logueado en facebook

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            useLoginInformation(accessToken);
        }
    }

    protected void onStop(){
        super.onStop();
        changeUI(firebaseAuth.getCurrentUser());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_google:
                btnPressed = getString(R.string.google_pressed_code);
                signIn();
                break;
            case R.id.button_facebook:
                btnPressed = getString(R.string.facebook_pressed_code);
                break;
            case R.id.btn_SignOut:
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    firebaseAuth.signOut();
                }else{
                    LoginManager.getInstance().logOut();
                }
                changeUI(user);
                break;

        }
    }

    private void handleFacebookAccessToken(final AccessToken accessToken){
        final AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //refresco la pantalla para mostrar cambio de botón
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void useLoginInformation(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.i("Account", object.toString());
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,nombre,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }
}

