package com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.HELPERS;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rodrigofilomeno on 15/12/2017.
 */

public class LoginPreferencesHelper {

    private Context context;
    private SharedPreferences preferences;
    private String NomeArquivo = "atdesenvolvimento.preferencias";
    private Integer Mode = 0;
    private SharedPreferences.Editor editor;

    private final String IdKey = "identificadorUsuarioLogado";
    private final String NameKey = "nomeUsuarioLogado";

    public LoginPreferencesHelper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(NomeArquivo, Mode);

        editor = preferences.edit();
    }

    public void salvarPreferenciasUsuario(String identificadorUsuario, String nomeUsuario){
        editor.putString(IdKey, identificadorUsuario);
        editor.putString(NameKey, nomeUsuario);
        editor.commit();

    }

    public String getIdentificador(){
        return preferences.getString(IdKey, null);
    }
    public String getNoome(){
        return preferences.getString(NameKey, null);
    }

}
