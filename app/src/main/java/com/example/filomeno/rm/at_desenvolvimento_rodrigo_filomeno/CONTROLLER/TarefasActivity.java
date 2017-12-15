package com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.ENTITIES.Tarefa;
import com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.ENTITIES.TarefaResponse;
import com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.CONTROLLER.INTERFACE.iTarefa;
import com.example.filomeno.rm.at_desenvolvimento_rodrigo_filomeno.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TarefasActivity extends AppCompatActivity {

    ListView listagem;
    ArrayAdapter<Tarefa> adapter = null;

    private static final String TAG = TarefasActivity.class.getSimpleName();
    public static final String BASE_URL = "http://infnet.educacao.ws/dadosAtividades.php/";
    private static Retrofit retrofit = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefas);

        listagem = (ListView) findViewById(R.id.lvTarefas);

        connectApiData();


    }


    private void connectApiData() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        iTarefa tarefaInterface = retrofit.create(iTarefa.class);
        Call<TarefaResponse> call = tarefaInterface.getTarefas();

        call.enqueue(new Callback<TarefaResponse>() {
            @Override
            public void onResponse(Call<TarefaResponse> call, Response<TarefaResponse> response) {
                List<Tarefa> tarefas = response.body().getTarefas();
                //Log.d("123", "Retorno" + tarefas.size());
                adapter = new ArrayAdapter<Tarefa>(getApplicationContext(), android.R.layout.simple_list_item_1, tarefas);
                listagem.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<TarefaResponse> call, Throwable t) {

            }
        });
    }
}
