package com.example.gachamon.pokeapi;

import com.example.gachamon.models.PokeRequest;
import com.example.gachamon.models.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokeapiService {

    @GET("pokemon-species?limit=895")
    Call<PokeRequest> PokeList();

    @GET("pokemon-species/{Pokenum}/")
    Call<Pokemon> getPokeByNum(@Path("Pokenum") String Pokenum);

    @GET("pokemon/{Pokenum}/")
    Call<PokeRequest> getPokeSByNum(@Path("Pokenum") String Pokenum);
}
