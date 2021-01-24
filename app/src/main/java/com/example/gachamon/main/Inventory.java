package com.example.gachamon.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gachamon.R;
import com.example.gachamon.login.RetrofitClient;
import com.example.gachamon.models.ListPokemonAdapter;
import com.example.gachamon.models.PokeRequest;
import com.example.gachamon.models.Pokemon;
import com.example.gachamon.pokeapi.PokeapiService;
import com.example.gachamon.pokeapi.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Inventory extends AppCompatActivity {
    private Retrofit retrofit;
    private ListPokemonAdapter listPokemonAdapter;
    private RecyclerView recyclerView;
    SharedPreferences prf;
    RetrofitInterface myAPI;
    private String[] mylist;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Retrofit retrofit2 = RetrofitClient.getInstance();
        myAPI = retrofit2.create(RetrofitInterface.class);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listPokemonAdapter = new ListPokemonAdapter(this);

        recyclerView.setAdapter(listPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        prf = getSharedPreferences("user_details",MODE_PRIVATE);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        getPokelist(prf.getString("email",""));

        fetchPokemon();
        
    }
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

    private void getPokelist(String email) {
        Log.e("inventory",email);
        compositeDisposable.add(myAPI.getPokelist(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("inventory",s);
                        //Toast.makeText(Inventory.this,""+s, Toast.LENGTH_LONG).show();
                        String[] tokens = s.split("\"");
                        mylist = tokens[3].split(", ");



                    }
                })
        );

    }


    public void fetchPokemon() {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokeRequest> pokeResquestCall = service.PokeList();

        pokeResquestCall.enqueue(new Callback<PokeRequest>() {
            @Override
            public void onResponse(Call<PokeRequest> call, Response<PokeRequest> response) {
                if (response.isSuccessful()) {
                    PokeRequest pokeRequest = response.body();
                    ArrayList<Pokemon> pokeList = pokeRequest.getResults();

                    for (int i =0; i < pokeList.size(); i++){
                        Pokemon p = pokeList.get(i);
                        ArrayList<Pokemon> selectedPokeList = new ArrayList<Pokemon>();
                        for (int j = 0; j<mylist.length;j++)
                            if (String.valueOf(p.getNumber()).equals(mylist[j]))
                                selectedPokeList.add(p);
                        listPokemonAdapter.addListPokemon(selectedPokeList);

                        }
                    }
                else {
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