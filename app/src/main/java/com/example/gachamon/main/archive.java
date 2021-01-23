package com.example.gachamon.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gachamon.R;
import com.example.gachamon.models.ListPokemonAdapter;
import com.example.gachamon.models.PokeRequest;
import com.example.gachamon.models.Pokemon;
import com.example.gachamon.pokeapi.PokeapiService;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class archive extends AppCompatActivity {
    private Retrofit retrofit;
    private ListPokemonAdapter listPokemonAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listPokemonAdapter = new ListPokemonAdapter(this);
        recyclerView.setAdapter(listPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        fetchPokemon();

    };

    public void fetchPokemon() {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokeRequest> pokeResquestCall = service.PokeList();

        pokeResquestCall.enqueue(new Callback<PokeRequest>() {
            @Override
            public void onResponse(Call<PokeRequest> call, Response<PokeRequest> response) {
                if (response.isSuccessful()) {
                    PokeRequest pokeRequest = response.body();
                    ArrayList<Pokemon> pokeList = pokeRequest.getResults();
                    listPokemonAdapter.addListPokemon(pokeList);


                } else {
                    Log.e("Pokedex", "onResponse" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokeRequest> call, Throwable t) {
                Log.e("Pokedex", "onFailure" + t.getMessage());

            }
        });
    }

    public void Return(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}