package com.dm.rentalvanou.iu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.rentalvanou.core.RentalVan;


public class FurgoviewActivity extends AppCompatActivity {

    int furgo_select;
    TextView textView_1;
    TextView textView_2;
    TextView textView_3;
    TextView textView_4;
    TextView textView_5;
    TextView textView_6;
    TextView textView_7;
    ImageView imageView;
    RentalVan rentalVan;
    Button btn_Volver;
    Button btnRent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furgoview);


        Intent intent = getIntent();
        String furgo = intent.getStringExtra("clave");
        rentalVan = new RentalVan();
        furgo_select = rentalVan.getPosicion(furgo);

        textView_1 = (TextView) this.findViewById(R.id.textViewFurgoviewMarca);
        textView_2 = (TextView) this.findViewById(R.id.textViewFurgoviewModelo);
        textView_3 = (TextView) this.findViewById(R.id.textViewFurgoviewAltura);
        textView_4 = (TextView) this.findViewById(R.id.textViewFurgoviewAncho);
        textView_5 = (TextView) this.findViewById(R.id.textViewFurgoviewLargo);
        textView_6 = (TextView) this.findViewById(R.id.textViewFurgoviewCarga);
        textView_7 = (TextView) this.findViewById(R.id.textViewFurgoviewPrecio);

        imageView = (ImageView)this.findViewById(R.id.imageView2);

        //BOTÓN VOLVER
        btn_Volver = (Button) this.findViewById(R.id.buttonFurgoviewRent);
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
                Toast.makeText(FurgoviewActivity.this, "Trabajando en esta acción. \nDisculpen las molestias", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FurgoviewActivity.this, MainActivity.class));
            }
        });


        String marca = RentalVan.MARCA[furgo_select];
        String modelo = RentalVan.MODELO[furgo_select];
        String altura = String.valueOf(RentalVan.ALTURA[furgo_select]);
        String ancho = String.valueOf(RentalVan.ANCHO[furgo_select]);
        String largo = String.valueOf(RentalVan.LARGO[furgo_select]);
        String capacidad = String.valueOf(RentalVan.CAPACIDAD[furgo_select]);
        String precio = String.valueOf(rentalVan.calculaAlquiler(furgo_select));


        textView_1.setText(marca);
        textView_2.setText(modelo);
        textView_3.setText(altura);
        textView_4.setText(ancho);
        textView_5.setText(largo);
        textView_6.setText(capacidad);
        textView_7.setText(precio);

        imageView.setImageResource(RentalVan.IMAGEN_FURGOS[furgo_select]);
    }
}
