package com.example.gachamon.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gachamon.R;
import com.example.gachamon.models.PokeRequest;
import com.example.gachamon.models.Pokemon;
import com.example.gachamon.pokeapi.PokeapiService;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SummonPage extends AppCompatActivity {
    private Retrofit retrofit;
    private TextView PokeTextview;
    private ImageView PokeImage;
    private int PokeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summon_page);

        PokeTextview = findViewById(R.id.pokeTextView);
        PokeImage = findViewById(R.id.pokeImage);
        Random rand = new Random();
        int Chance = rand.nextInt(100);

        Context context = this;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (Chance <= 1 ){
            PokeNum = rand.nextInt(810)+1;
            fetchLegendaryPokemon(context,String.valueOf(PokeNum));

        }else{
            PokeNum = rand.nextInt(810)+1;
            fetchPokemon(context,String.valueOf(PokeNum));
        }


    }

    public void fetchPokemon(final Context context, final String Pokenum) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<Pokemon> pokemonCall = service.getPokeByNum(Pokenum);

        pokemonCall.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon>  response) {
                if (response.isSuccessful()){
                   Pokemon pokemon = response.body();
                    if (pokemon.isIs_legendary() == false){
                        Log.e("Pokedex","Result" + pokemon.isIs_legendary());
                        PokeTextview.setText(pokemon.getName());
                        Glide.with(context)
                                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+PokeNum+".png")
                                .centerCrop()
                                .into(PokeImage);

                    }else{
                        if (Integer.valueOf(PokeNum )< 811){
                            PokeNum++;
                            fetchPokemon(context,String.valueOf(PokeNum));

                        }else{
                            PokeNum--;
                            fetchPokemon(context,String.valueOf(PokeNum));
                        }

                        Log.e("Pokedex","Result" + pokemon.isIs_legendary());
                        Log.e("Pokedex","Result" + PokeNum);
                    }

                    Log.e("Pokedex","Result" + pokemon.isIs_legendary());
                }else{
                    Log.e("Pokedex","onResponse" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Log.e("Pokedex","onFailure" + t.getMessage());

            }
        });


    }

    public void fetchLegendaryPokemon(final Context context, final String Pokenum) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<Pokemon> pokemonCall = service.getPokeByNum(Pokenum);

        pokemonCall.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon>  response) {
                if (response.isSuccessful()){
                    Pokemon pokemon = response.body();
                        if (pokemon.isIs_legendary() == true){
                            Log.e("Pokedex","Result" + pokemon.isIs_legendary());
                            PokeTextview.setText(pokemon.getName());
                            Glide.with(context)
                                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+PokeNum+".png")
                                    .centerCrop()
                                    .into(PokeImage);

                        }else{
                            if (Integer.valueOf(PokeNum )< 811){
                                PokeNum++;
                                fetchLegendaryPokemon(context,String.valueOf(PokeNum));

                            }else{
                                PokeNum--;
                                fetchLegendaryPokemon(context,String.valueOf(PokeNum));
                            }

                            Log.e("Pokedex","Result" + pokemon.isIs_legendary());
                            Log.e("Pokedex","Result" + PokeNum);
                        }
                }else{
                    Log.e("Pokedex","onResponse" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Log.e("Pokedex","onFailure" + t.getMessage());

            }
        });


    }
}