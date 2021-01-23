package com.example.gachamon.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PokeRequest {
    private ArrayList<Pokemon> results;
    public ArrayList<Pokemon> getResults() {
        return results;
    }
    public void setResults(ArrayList<Pokemon> results) {
        this.results = results;
    }

    @SerializedName("stats")
    private ArrayList<PokeStat> stats = new ArrayList<PokeStat>();

    public ArrayList<PokeStat> getStats() {
        return stats;
    }
    public void setStats(ArrayList<PokeStat> stats) {
        this.stats = stats;
    }



}
