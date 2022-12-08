package com.dm.rentalvanou.iu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.dm.rentalvanou.model.DBManager;

public class  RegistroActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> activityResultLauncherEdit;
    EditText etNombre;
    EditText etApellidos;
    EditText etEmail;
    EditText etPassword;
    DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Button btnVolver = (Button) this.findViewById(R.id.buttonRegistroVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
            }
        });

       Button btn_getRegistro = this.findViewById(R.id.buttonRegistroRegistro);
       btn_getRegistro.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                String nombre = etNombre.getText().toString();
                String apellidos = etApellidos.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String[] toret = {nombre,apellidos,email,password};

                if(dbManager.addUser(toret)){
                    pasaInfo(email);

                }
                else{
                    Toast.makeText(RegistroActivity.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                    RegistroActivity.this.setResult(RESULT_CANCELED);
                }
           }
       });

    }

    public void onStart(){
        super.onStart();
        dbManager = DBManager.getManager(this.getApplicationContext());

        this.etNombre = (EditText)  this.findViewById(R.id.editTextRegistroNombre);
        this.etApellidos = (EditText) this.findViewById(R.id.editTextRegistroApellidos);
        this.etEmail = (EditText) this.findViewById(R.id.editTextRegistroEmail);
        this.etPassword = (EditText) this.findViewById(R.id.editTextRegistroPassword);

    }

    public void onPause(){
        super.onPause();
        dbManager.close();
    }

    private void pasaInfo(String valor) {
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        intent.putExtra("clave", valor);
        startActivity(intent);
    }
}
