package com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.INTERFACE;

import com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.ENTITIES.TarefaResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by rodrigofilomeno on 15/12/2017.
 */

public interface iTarefa {
    @GET("./")
    Call<TarefaResponse> getTarefas();
}
