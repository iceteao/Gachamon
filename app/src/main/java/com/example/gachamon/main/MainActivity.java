package com.example.gachamon.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gachamon.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Archive(View view) {
        Intent intent = new Intent(this, archive.class);
        this.startActivity(intent);
    }

    public void Inventory(View view) {
        Intent intent = new Intent(this, Inventory.class);
        this.startActivity(intent);
    }

    public void Summon(View view) {
        Intent intent = new Intent(this, SummonPage.class);
        this.startActivity(intent);
    }
}