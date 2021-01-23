package com.example.gachamon.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gachamon.R;
import com.example.gachamon.main.Inventory;
import com.example.gachamon.main.archive;
import com.example.gachamon.models.PokeRequest;
import com.example.gachamon.models.PokeStat;
import com.example.gachamon.models.Pokemon;
import com.example.gachamon.pokeapi.PokeapiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokeDetails extends AppCompatActivity {

    private Retrofit retrofit;
    private TextView pokeName;
    private TextView HPTextView;
    private TextView AttackTextView;
    private TextView DefenseTextView;
    private ImageView pokeImageDetails;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_details);
        pokeName = findViewById(R.id.PokeName);
        pokeImageDetails = findViewById(R.id.pokeImageDetails);
        HPTextView = findViewById(R.id.HpTextView);
        AttackTextView = findViewById(R.id.attackTextView);
        DefenseTextView = findViewById(R.id.defTextView);
        Intent intent = getIntent();
        String Pos = intent.getStringExtra("Pokename");

        System.out.print(Pos);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        fetchPokeDetails(this, Pos);
        fetchPokeStats(this, Pos);
    }

    ;

    public void fetchPokeDetails(final Context context, final String Pokenum) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<Pokemon> pokemonCall = service.getPokeByNum(Pokenum);

        pokemonCall.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful()) {
                    Pokemon pokemon = response.body();
                    Glide.with(context)
                            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + Pokenum + ".png")
                            .centerCrop()
                            .skipMemoryCache(true)
                            .into(pokeImageDetails);
                    pokeName.setText(pokemon.getName());


                } else {
                    Log.e("Pokedex", "onResponse" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Log.e("Pokedex", "onFailure" + t.getMessage());

            }
        });
    }

    public void fetchPokeStats(final Context context, final String Pokenum) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokeRequest> pokemonCall = service.getPokeSByNum(Pokenum);

        pokemonCall.enqueue(new Callback<PokeRequest>() {
            @Override
            public void onResponse(Call<PokeRequest> call, Response<PokeRequest> response) {
                PokeRequest pokemon = response.body();
                ArrayList<PokeStat> stats = pokemon.getStats();

                HPTextView.setText("HP : "+ stats.get(0).getBase_stat());
                AttackTextView.setText("Attack power: " +stats.get(1).getBase_stat());
                DefenseTextView.setText("Defense : " +stats.get(2).getBase_stat());
                StringBuilder builder = new StringBuilder();


            }

            @Override
            public void onFailure(Call<PokeRequest> call, Throwable t) {
                Log.e("Pokedex", "onFailure" + t.getMessage());

            }
        });
    }


    public void Return(View view) {
        Intent intent = new Intent(this, archive.class);
        this.startActivity(intent);
    }
}
