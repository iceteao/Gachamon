package com.example.gachamon.main;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.gachamon.R;
import com.example.gachamon.login.LoginActivity;
import com.example.gachamon.login.RetrofitClient;
import com.example.gachamon.models.Pokemon;
import com.example.gachamon.pokeapi.PokeapiService;
import com.example.gachamon.pokeapi.RetrofitInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class SummonPage extends AppCompatActivity {
    private Retrofit retrofit;
    private TextView PokeTextview;
    private ImageView PokeImage;
    private ImageView PokeGif;
    private LinearLayout LN;
    private int PokeNum;
    RetrofitInterface myAPI;
    private String Pokelist;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SharedPreferences pref;

    @Override
    protected void onStop(){
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summon_page);
        LN = findViewById(R.id.LN);
        LN.setClickable(false);

        PokeTextview = findViewById(R.id.pokeTextView);
        PokeImage = findViewById(R.id.pokeImage);
        PokeGif = findViewById(R.id.pokeGif);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        Context context = this;
        Random rand = new Random();
        int Chance = rand.nextInt(100);
        Retrofit retrofit2 = RetrofitClient.getInstance();
        myAPI = retrofit2.create(RetrofitInterface.class);
        String Pokelist;

        // Set the base url of the api

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create a lower probability for the draw of legendary pokemon
        if (Chance <= 50 ){
            PokeNum = rand.nextInt(810)+1;
            Glide.with(context)
                    .load(R.drawable.pokegif2)
                    .asGif()
                    .into(PokeGif);

            // Make the gif disappear and the draw after a set duration
            PokeGif.animate()
                    .translationY(PokeGif.getHeight())
                    .setDuration(7500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            LN.setClickable(true);
                            PokeGif.animate().alpha(0.0f).setDuration(300);
                            PokeGif.setVisibility(View.GONE);
                            PokeImage.animate().alpha(1.0f).setDuration(300);
                            PokeTextview.animate().alpha(1.0f).setDuration(300);
                            PokeImage.setVisibility(View.VISIBLE);
                            PokeTextview.setVisibility(View.VISIBLE);
                        }
                    });


            fetchLegendaryPokemon(context,String.valueOf(PokeNum));
            Pokelist = String.valueOf(PokeNum);
            toPokelist(pref.getString("email",""),Pokelist);


        }else{
            // If not legendary pokemon then fetch a randomly common pokemon
            PokeNum = rand.nextInt(810)+1;

            //Same concept as previously we load the gif and set it to disappear after a set duration, the draw will appear just after
            Glide.with(context)
                    .load(R.drawable.pokegif1)
                    .asGif()
                    .into(PokeGif);

            PokeGif.animate()
                    .translationY(PokeGif.getHeight())
                    .setDuration(7500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            LN.setClickable(true);
                            PokeGif.animate().alpha(0.0f).setDuration(300);
                            PokeGif.setVisibility(View.GONE);
                            PokeImage.animate().alpha(1.0f).setDuration(300);
                            PokeTextview.animate().alpha(1.0f).setDuration(300);
                            PokeImage.setVisibility(View.VISIBLE);
                            PokeTextview.setVisibility(View.VISIBLE);
                        }
                    });

            fetchPokemon(context,String.valueOf(PokeNum));
            Log.e("pokenul",String.valueOf(PokeNum));
          //  Pokelist.add(String.valueOf(PokeNum));
            Pokelist = String.valueOf(PokeNum);
           toPokelist(pref.getString("email",""),Pokelist);
            //toPokelist("damdam1","3");
        }


    }
    private void toPokelist(String email,  String Pokelist) {
            compositeDisposable.add(myAPI.toPokelist(email,Pokelist)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Toast.makeText(SummonPage.this,"Summon success", Toast.LENGTH_LONG).show();

                        }
                    })
            );

        }





    // FetchPokemon will go in the Api, and retrieve the pokemon name and picture
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
                                .skipMemoryCache(true)
                                // This part allow the picture to remain invisible even if found to not interrupt the summoning ritual
                                .into(new GlideDrawableImageViewTarget(PokeImage) {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                        super.onResourceReady(resource, null); // ignores animation, but handles GIFs properly.
                                    }
                                });


                    }else{
                        // If it's legendary look for another pokemon
                        if (Integer.valueOf(PokeNum )< 811){
                            PokeNum++;
                            fetchPokemon(context,String.valueOf(PokeNum));
                        //  If the pokenum exceed the max go back to the beginning
                        }else{
                            PokeNum = 0;
                            fetchPokemon(context,String.valueOf(PokeNum));
                        }
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

    // Same as fetchPokemon but this time with legendary one
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
                                    .skipMemoryCache(true)
                                    .into(new GlideDrawableImageViewTarget(PokeImage) {
                                        @Override
                                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                            super.onResourceReady(resource, null); // ignores animation, but handles GIFs properly.
                                        }
                                    });

                        }else{
                            if (Integer.valueOf(PokeNum )< 811){
                                PokeNum++;
                                fetchLegendaryPokemon(context,String.valueOf(PokeNum));

                            }else{
                                PokeNum=0;
                                fetchLegendaryPokemon(context,String.valueOf(PokeNum));
                            }
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

    public void editActions(final View v){

        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

}