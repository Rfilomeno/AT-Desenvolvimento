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
import com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.HELPERS.Base64Custom;
import com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.HELPERS.LoginPreferencesHelper;
import com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.R;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class CadastroActivity extends AppCompatActivity implements Validator.ValidationListener {

    Validator validator;
    User user;
    private FirebaseAuth fbAutenticacao;


    @NotEmpty(message = "Campo nao pode ficar em branco.")
    EditText Nome;

    @NotEmpty(message = "Campo nao pode ficar em branco.")
    @com.mobsandgeeks.saripaar.annotation.Email(message = "Coloque um E-mail válido.")
    EditText Email;

    @NotEmpty(message = "Campo nao pode ficar em branco.")
    @Password
    EditText Senha;

    @NotEmpty(message = "Campo nao pode ficar em branco.")
    @ConfirmPassword(message = "As senhas devem ser iguais.")
    EditText ConfirmaSenha;

    @NotEmpty(message = "Campo nao pode ficar em branco.")
    @Length(min = 14, max = 14, message = "O CPF deve conter 11 números.")
    EditText Cpf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        validator = new Validator(this);
        validator.setValidationListener(this);

        Nome = (EditText) findViewById(R.id.etNome);
        Email = (EditText) findViewById(R.id.etEmail);
        Senha = (EditText) findViewById(R.id.etSenha);
        ConfirmaSenha = (EditText) findViewById(R.id.etConfirmaSenha);
        Cpf = (EditText) findViewById(R.id.etCpf);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(Cpf, smf);
        Cpf.addTextChangedListener(mtw);

    }

    @Override
    public void onValidationSucceeded() {

        user = new User();
        user.setNome(Nome.getText().toString());
        user.setEmail(Email.getText().toString());
        user.setSenha(Senha.getText().toString());
        user.setCpf(Cpf.getText().toString());

        cadastrar();



    }

    private void cadastrar() {

        fbAutenticacao = FirebaseDao.getFirebaseAutenticacao();
        fbAutenticacao.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getSenha()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    String identificadorUsuario = Base64Custom.codificarBase64(user.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    user.setId(identificadorUsuario);
                    user.salvar();

                    LoginPreferencesHelper preferencias = new LoginPreferencesHelper(CadastroActivity.this);
                    preferencias.salvarPreferenciasUsuario(identificadorUsuario, user.getNome());

                    irParaTarefas();

                }else{
                    String erroExcecao = "";

                    try {
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte.";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O email digitado é inválido.";

                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse email já está cadastrado no sistema";

                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {

                ((EditText) view).setError(message);

            } else {
                Toast.makeText(CadastroActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void btnSalvar(View v){
        validator.validate();
    }

    public void btnLimpar(View v){
        clean();
    }


    public void clean(){
        Nome.setText("");
        Email.setText("");
        Senha.setText("");
        ConfirmaSenha.setText("");
        Cpf.setText("");
    }

    public void irParaTarefas(){
        Intent intentTarefas = new Intent(CadastroActivity.this, TarefasActivity.class);
        startActivity(intentTarefas);
        finish();


    }

}
