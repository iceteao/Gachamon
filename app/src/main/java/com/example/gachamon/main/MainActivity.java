package com.example.gachamon.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.gachamon.R;
import com.example.gachamon.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prf;
    Intent intent;
    TextView Userlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(MainActivity.this, LoginActivity.class);
        Userlog = findViewById(R.id.userlog);
        Userlog.setText(prf.getString("email",null));
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

    public void logout(View view) {
        SharedPreferences.Editor editor = prf.edit();
        editor.clear();
        editor.commit();
        startActivity(intent);
    }
}