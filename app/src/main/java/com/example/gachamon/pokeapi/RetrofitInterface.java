package com.example.gachamon.pokeapi;


import java.lang.reflect.Array;
import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    //@Field("name") String name,
                                    @Field("password") String password);


    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                 @Field("password") String password);

    @POST("addpokemons")
    @FormUrlEncoded
    Observable<String> toPokelist(@Field("email") String email,
                                    @Field("pokelist") String Pokelist);




}
