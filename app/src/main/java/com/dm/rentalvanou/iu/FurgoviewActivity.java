package com.dm.rentalvanou.iu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.rentalvanou.core.RentalVan;
import com.dm.rentalvanou.model.DBManager;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;


public class FurgoviewActivity extends AppCompatActivity {

    int furgo_select;
    TextView textView_1;
    TextView textView_2;
    TextView textView_3;
    TextView textView_4;
    TextView textView_5;
    TextView textView_6;
    TextView textView_7;
    TextView tvUser;

    ImageView imageView;
    RentalVan rentalVan;
    Button btn_Volver;
    Button btnRent;
    String user_name = null;
    String user_email = null;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furgoview);

        DBManager dbManager = DBManager.getManager(this.getApplicationContext());
        db = dbManager.getReadableDatabase();
        rentalVan = new RentalVan(db);

        Intent intent = getIntent();
        String furgo = intent.getStringExtra("clave");
        try{
            user_name = intent.getStringExtra("uname");
            user_email = intent.getStringExtra("uemail");
        }
        catch (Exception ex){
            Log.e("ERROR", ex.getMessage());
            Toast.makeText(FurgoviewActivity.this, "UNAME: NULL", Toast.LENGTH_SHORT).show();
            Toast.makeText(FurgoviewActivity.this, "UEMAIL: NULL", Toast.LENGTH_SHORT).show();
        }

        furgo_select = rentalVan.getPosicion(furgo);

        if(user_name != null){
            tvUser = findViewById(R.id.textViewMainUser);
            tvUser.setText(user_name);
        }

        textView_1 = (TextView) this.findViewById(R.id.textViewFurgoviewMarca);
        textView_2 = (TextView) this.findViewById(R.id.textViewFurgoviewModelo);
        textView_3 = (TextView) this.findViewById(R.id.textViewFurgoviewAltura);
        textView_4 = (TextView) this.findViewById(R.id.textViewFurgoviewAncho);
        textView_5 = (TextView) this.findViewById(R.id.textViewFurgoviewLargo);
        textView_6 = (TextView) this.findViewById(R.id.textViewFurgoviewCarga);
        textView_7 = (TextView) this.findViewById(R.id.textViewFurgoviewCombustible);

        imageView = (ImageView) this.findViewById(R.id.imageView2);

        //BOTÓN VOLVER
        btn_Volver = (Button) this.findViewById(R.id.buttonFvVolver);
        btn_Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FurgoviewActivity.this, MainActivity.class));
            }
        });

        //BOTÓN RENT
        btnRent = (Button) this.findViewById(R.id.buttonFurgoviewRent);
        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasaInfo();
            }
        });

        String marca = rentalVan.getMarca(furgo_select);
        String modelo = rentalVan.getModelo(furgo_select);
        String altura = String.valueOf(rentalVan.getAltura(furgo_select));
        String ancho = String.valueOf(rentalVan.getAncho(furgo_select));
        String largo = String.valueOf(rentalVan.getLargo(furgo_select));
        String capacidad = String.valueOf(rentalVan.getCapacidad(furgo_select));
        String combustible = String.valueOf(rentalVan.getCombustible(furgo_select));

        textView_1.setText(marca);
        textView_2.setText(modelo);
        textView_3.setText(altura);
        textView_4.setText(ancho);
        textView_5.setText(largo);
        textView_6.setText(capacidad);
        textView_7.setText(combustible);

        imageView.setImageResource(rentalVan.getImgVan(furgo_select));


    }

    private void pasaInfo() {
        Intent intent = new Intent(this, FurgoRentActivity.class);
        intent.putExtra("clave", furgo_select);
        if(user_name != null){
            intent.putExtra("uname", user_name);
            intent.putExtra("uemail", user_email);
        }
        startActivity(intent);
    }
}
