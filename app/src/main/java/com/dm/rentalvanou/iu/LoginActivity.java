package com.dm.rentalvanou.iu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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
                    Toast.makeText(LoginActivity.this, "El user o password no existe", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.setResult(Activity.RESULT_CANCELED);
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
    }

    public void onStart(){
        super.onStart();
        // OPEN BD
        dbManager = DBManager.getManager(this.getApplicationContext());
        db = dbManager.getReadableDatabase();

        //  RECUPERACIÓN DE EMAIL SI VIENE DE REGISTRO
        Intent intent = getIntent();
        String email = intent.getStringExtra("clave");

        //Estos editText recogeran la información para logearse
        etEmail = (EditText) this.findViewById(R.id.editTextLoginEmail);
        etPassword = (EditText) this.findViewById(R.id.editTextLoginPassword);
        if(email != null){
            etEmail.setText(email);
        }
    }

    public void onPause(){
        super.onPause();
        dbManager.close();
        db.close();
    }

    private void pasaInfo(String user_name, String user_email){
        Intent intent = new Intent();
        intent.putExtra("uname", user_name);
        intent.putExtra("uemail", user_email);
        LoginActivity.this.setResult(Activity.RESULT_OK, intent);
        LoginActivity.this.finish();
    }

}

