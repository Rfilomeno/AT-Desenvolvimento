package com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.HELPERS;

import android.util.Base64;

/**
 * Created by rodrigofilomeno on 15/12/2017.
 */

public class Base64Custom {
    public static String codificarBase64 (String texto){

        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");

    }

    public static String decodificarBase64 (String textoCodificado){

        return new String (Base64.decode(textoCodificado, Base64.DEFAULT));

    }
}
