package com.dm.rentalvanou.iu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity{

    EditText etEmail;
    EditText edPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button) this.findViewById(R.id.buttonLoginLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainconLoginActivity.class));
            }
        });

        Button btnVolver = (Button) this.findViewById(R.id.buttonLoginVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });



        //Estos editText recogeran la información para logearse
        etEmail = (EditText) this.findViewById(R.id.editTextLoginEmail);
        edPassword = (EditText) this.findViewById(R.id.editTextLoginPassword);

    }

}

