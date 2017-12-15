package com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.DAO.FirebaseDao;
import com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.ENTITIES.User;
import com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener{

    Validator validator;
    private FirebaseAuth FbAuth;
    private User user;
    LoginButton loginFacebook;
    private FirebaseAuth FirebaseAuth;
    private CallbackManager mCallbackManager;

    @NotEmpty(message = "Campo nao pode ficar em branco.")
    @Email(message = "Coloque um e-mail válido.")
    EditText LoginEmail;

    @NotEmpty(message = "Campo nao pode ficar em branco.")
    @Password
    EditText LoginSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        validator = new Validator(this);
        validator.setValidationListener(this);


        LoginEmail = (EditText) findViewById(R.id.etEmailLogin);
        LoginSenha = (EditText) findViewById(R.id.etSenhaLogin);


        init();

    }

    public void btnLogar(View v){
        validator.validate();
    }
    public void btnCadastre(View v){
        Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(intent);
    }


    @Override
    public void onValidationSucceeded() {

        user = new User();
        user.setEmail(LoginEmail.getText().toString());
        user.setSenha(LoginSenha.getText().toString());
        validaLogin();


    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {

                ((EditText) view).setError(message);

            } else {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void validaLogin(){

        FbAuth = FirebaseDao.getFirebaseAutenticacao();
        FbAuth.signInWithEmailAndPassword(user.getEmail(), user.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    irParaTarefas();
                    Toast.makeText(MainActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Login e/ou senha inválidos.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

public void irParaTarefas(){
    Intent intentTarefas = new Intent(MainActivity.this, TarefasActivity.class);
    startActivity(intentTarefas);


}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        FirebaseAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        loginFacebook = (LoginButton) findViewById(R.id.btnLoginFacebook);
        loginFacebook.setReadPermissions("email","public_profile");


        loginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                startActivity(new Intent(getApplicationContext(),TarefasActivity.class));
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Facebook login failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getCurrentUser();
                        }else{

                        }
                    }
                });
    }


}
