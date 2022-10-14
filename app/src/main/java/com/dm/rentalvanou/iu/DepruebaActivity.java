package com.dm.rentalvanou.iu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class DepruebaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deprueba);

        Button btn_Volve = (Button) this.findViewById(R.id.button);
        btn_Volve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DepruebaActivity.this, RegistroActivity.class));
            }
        });
    }
}
