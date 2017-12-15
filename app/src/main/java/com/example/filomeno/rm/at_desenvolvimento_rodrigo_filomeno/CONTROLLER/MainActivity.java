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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

    @NotEmpty(message = "Campo nao pode ficar em branco.")
    @Email(message = "Coloque um e-mail válido.")
    EditText LoginEmail;

    @NotEmpty(message = "Campo nao pode ficar em branco.")
    @Password
    EditText LoginSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validator = new Validator(this);
        validator.setValidationListener(this);


        LoginEmail = (EditText) findViewById(R.id.etEmailLogin);
        LoginSenha = (EditText) findViewById(R.id.etSenhaLogin);




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


}
