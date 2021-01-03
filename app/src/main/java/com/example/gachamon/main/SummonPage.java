package com.example.gachamon.main;

import androidx.appcompat.app.AppCompatActivity;

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
        PokeNum = rand.nextInt(20);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        fetchPokemon();

        Glide.with(this)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+PokeNum+".png")
                .centerCrop()
                .into(PokeImage);

    }

    public void fetchPokemon() {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokeRequest> pokeResquestCall = service.PokeList();

        pokeResquestCall.enqueue(new Callback<PokeRequest>() {
            @Override
            public void onResponse(Call<PokeRequest> call, Response<PokeRequest> response) {
                if (response.isSuccessful()){
                   PokeRequest pokeRequest = response.body();
                   ArrayList<Pokemon> pokeList = pokeRequest.getResults();


                   Pokemon pokemon =  pokeList.get(PokeNum);
                   PokeTextview.setText(pokemon.getName());


                }else{
                    Log.e("Pokedex","onResponse" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokeRequest> call, Throwable t) {
                Log.e("Pokedex","onFailure" + t.getMessage());

            }
        });


    }
}