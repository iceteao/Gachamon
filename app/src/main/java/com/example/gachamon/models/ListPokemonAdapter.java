package com.example.gachamon.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gachamon.R;

import java.util.ArrayList;

public class ListPokemonAdapter extends RecyclerView.Adapter<ListPokemonAdapter.ViewHoler> {

    private ArrayList<Pokemon> dataset;
    private ArrayList<Pokemon> Ldataset;
    private Context context;

    public ListPokemonAdapter(Context context) {
        this.context = context;
        dataset = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_archive_pokemon,parent,false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        Pokemon p = dataset.get(position);
        holder.PokeTextview.setText(p.getName());

        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+ p.getNumber() +".png")
                .centerCrop()
                .into(holder.PokeImage);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void addListPokemon(ArrayList<Pokemon> pokeList) {
        dataset.addAll(pokeList);
        notifyDataSetChanged();
    }

    public void addListLegendaryPokemon(ArrayList<Pokemon> pokeList) {
        Ldataset.addAll(pokeList);
        notifyDataSetChanged();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        private TextView PokeTextview;
        private ImageView PokeImage;
        public ViewHoler(View itemView) {
            super(itemView);

            PokeTextview = (TextView) itemView.findViewById(R.id.pokeTextView1);
            PokeImage = (ImageView) itemView.findViewById(R.id.pokeImage1);
        }
    }
}
