package com.dm.rentalvanou.iu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalInfoActivity extends AppCompatActivity {

    TextView tvUser;
    Button btnCancelarCuenta;
    Button btnVolver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);


        tvUser = this.findViewById(R.id.textViewPersonalInfoUser);
        tvUser.setText("Rober");

        btnCancelarCuenta = (Button) this.findViewById(R.id.buttonPersonalInfoBaja);
        btnCancelarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalInfoActivity.this, "Trabajando en esta acción. \nDisculpen las molestias", Toast.LENGTH_SHORT).show();
            }
        });
        //Acciones para eliminar datos

        btnVolver = (Button) this.findViewById(R.id.buttonPersonalInfoVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalInfoActivity.this,MainActivity.class));
            }
        });


    }
}
