package com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by rodrigofilomeno on 15/12/2017.
 */

public class FirebaseDao {

    private static DatabaseReference DBReference;
    private static FirebaseAuth FbAuth;

    public static DatabaseReference getFirebase(){
        if ( DBReference == null){

            DBReference = FirebaseDatabase.getInstance().getReference();
        }
        return DBReference;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){
        if(FbAuth == null){
            FbAuth = FirebaseAuth.getInstance();
        }
        return FbAuth;
    }





}
