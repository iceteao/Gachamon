package com.example.gachamon.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gachamon.R;
import com.example.gachamon.main.MainActivity;
import com.example.gachamon.main.SummonPage;
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
    String unlogged = "unlogged";
    Intent intent;
    @Override
    protected void onStart() {

        super.onStart();
        checkSession();

    }
    private void checkSession() {
        //check if user is logged in
        //if user is logged in --> move to mainActivity

        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        String userEmail = sessionManagement.getSession();
        Log.e("name",userEmail);
        if(userEmail != unlogged){

        }
        else {
            moveToMainActivity();
        }

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
        //View view = getLayoutInflater().inflate(R.layout.signupform,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setView(view).show();
        //EditText edt_email = (EditText) view.findViewById(R.id.edt_email);
        //EditText reg_password = (EditText) view.findViewById(R.id.edt_password);
        // compositeDisposable.add(myAPI.registerUser(email,edt_email.getText().toString(),reg_password.getText().toString())
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

       // checkSession();
      //  Log.e("name","userEmail");




    }
    private void moveToLogin() {
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}