package com.dm.rentalvanou.iu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dm.rentalvanou.core.RentalVan;
import com.dm.rentalvanou.model.DBManager;


public class LoginActivity extends AppCompatActivity{

    EditText etEmail;
    EditText etPassword;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // OPEN BD
        DBManager dbManager = DBManager.getManager(this.getApplicationContext());
        db = dbManager.getReadableDatabase();

        //  RECUPERACIÓN DE EMAIL SI VIENE DE REGISTRO
        Intent intent = getIntent();
        String email = intent.getStringExtra("clave");


        Button btnLogin = (Button) this.findViewById(R.id.buttonLoginLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String passwd = etPassword.getText().toString();
                String user_name = "";
                String user_email = "";
                Cursor user = dbManager.getLogin(db, email, passwd);
                if(user.moveToFirst()){
                    do{
                        user_name = user.getString(1);
                        user_email = user.getString(3);
                    }while(user.moveToNext());
                    pasaInfo(user_name,user_email);
                }
                else{
                    Toast.makeText(LoginActivity.this, "El user no existe", Toast.LENGTH_SHORT).show();
                }
                //startActivity(new Intent(LoginActivity.this, MainconLoginActivity.class));
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
        etPassword = (EditText) this.findViewById(R.id.editTextLoginPassword);
        if(email != null){
            etEmail.setText(email);
        }
    }
    private void pasaInfo(String name, String email){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("uname", name);
        intent.putExtra("uemail", email);
        startActivity(intent);
    }

}

