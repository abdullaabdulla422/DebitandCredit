package com.example.dm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LoginActivity extends AppCompatActivity {
EditText username,password;
Button btnLogin;
ToggleButton remember_me;
    Boolean saveLogin;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    String user,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.edt_username);
        password=findViewById(R.id.edt_pwd);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        btnLogin=findViewById(R.id.btn_login);
        remember_me = (ToggleButton) findViewById(R.id.remember_me);




                btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 user=  username.getText().toString();
                 pwd= password.getText().toString();

                if (remember_me.isChecked()){
                    loginPrefsEditor.putBoolean("saveLogin",true);
                    loginPrefsEditor.putString("username",user);
                    loginPrefsEditor.putString("password",pwd);
                    loginPrefsEditor.commit();
                }else {
                    loginPrefsEditor.putBoolean("saveLogin",false);
                    loginPrefsEditor.putString("username","");
                    loginPrefsEditor.putString("password","");
                    loginPrefsEditor.commit();
                }
                if((user.equals("dmi123"))&(pwd.equals("dmi123"))){
                    Toast.makeText(LoginActivity.this, "successfully login", Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(LoginActivity.this,Data.class);
                    startActivity(i);
                }else if(username.getText().toString().length()==0||password.getText().toString().length()==0){
                    Toast.makeText(LoginActivity.this, "please enter the empty fills", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "incorrect login", Toast.LENGTH_SHORT).show();
                }


            }
        });
        if (saveLogin) {
            username.setText(loginPreferences.getString("username",""));
            password.setText(loginPreferences.getString("password",""));
            remember_me.setChecked(true);
        }



    }
}
