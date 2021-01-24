package com.example.gachamon.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.gachamon.R;
import com.example.gachamon.main.MainActivity;
import com.example.gachamon.pokeapi.RetrofitInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    RetrofitInterface myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SharedPreferences pref;
    EditText edt_email,edt_password;
    Button btn_register,btn_login;
    Intent intent;
    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Retrofit retrofit = RetrofitClient.getInstance();
        intent = new Intent(LoginActivity.this, MainActivity.class);
        myAPI = retrofit.create(RetrofitInterface.class);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);

        btn_login = (Button) findViewById(R.id.loginBtn);
        btn_register = (Button) findViewById(R.id.registerBtn);

        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password =(EditText) findViewById(R.id.edt_password);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(edt_email.getText().toString(),edt_password.getText().toString());

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(edt_email.getText().toString(),edt_password.getText().toString());
            }
        });
    }



    private void registerUser(final String email, final String password) {
        compositeDisposable.add(myAPI.registerUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(LoginActivity.this,""+s, Toast.LENGTH_LONG).show();
                    }
                })
        );

    }


    private void loginUser(final String email, String password) {
        compositeDisposable.add(myAPI.loginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>(){
                    @Override
                    public void accept (String s) throws Exception {
                        Log.e("s=,", s);
                        if (s.contains("206")){
                            Toast.makeText(LoginActivity.this,"Wrong email", Toast.LENGTH_LONG).show();

                        }

                        if (s.contains("200")) {

                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email",email);
                            editor.commit();
                            startActivity(intent);
                        }

                        if (s.contains("204")) {
                            Toast.makeText(LoginActivity.this, "Email and password does not match", Toast.LENGTH_LONG).show();
                        }

                    }


                })




        );
    }
}