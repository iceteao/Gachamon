package com.example.gachamon.pokeapi;

import com.example.gachamon.models.PokeRequest;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokeRequest> PokeList();
}
