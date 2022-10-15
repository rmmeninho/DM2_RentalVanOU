package com.dm.rentalvanou.iu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dm.rentalvanou.core.RentalVan;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FurgoRentActivity extends AppCompatActivity {

    RentalVan rentalVan;
    // VARIABLES CALENDARIO
    String fecha_ini;
    String fecha_fin;
    String fecha;
    // DATOS VEHÍCULO
    ImageView imgFurgoSelec;
    TextView tvMarca;
    TextView tvModelo;
    // DATOS USUARIO
    TextView tvNombre;
    TextView tvApellidos;
    TextView tvEmail;
    //DATOS TIEMPO
    TextView tvFecha_ini;
    TextView tvFecha_fin;
    // DATOS COSTE TOTAL
    TextView tvCoste;
    // BOTONES
    Button btn_volver;
    Button btn_rent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furgorent);

        rentalVan = new RentalVan();
        // DATOS DE OTRA ACTIVITY
        Intent intent = getIntent();
        // INICIO FECHA ACTUAL
        Calendar hoy = Calendar.getInstance();
        fecha_ini = String.valueOf(hoy.get(Calendar.YEAR))+"/"+String.valueOf(hoy.get(Calendar.MONTH))+"/"+String.valueOf(hoy.get(Calendar.DAY_OF_MONTH));
        hoy.add(Calendar.DAY_OF_MONTH,1);
        fecha_fin = String.valueOf(hoy.get(Calendar.YEAR))+"/"+String.valueOf(hoy.get(Calendar.MONTH))+"/"+String.valueOf(hoy.get(Calendar.DAY_OF_MONTH));

        // INICIALIZACIÓN DE VARIABLES
        imgFurgoSelec = (ImageView) this.findViewById(R.id.imageViewRentFurgo);
        tvMarca = (TextView) this.findViewById(R.id.textViewFRMarca);
        tvModelo = (TextView) this.findViewById(R.id.textViewFRModelo);
        tvNombre = (TextView) this.findViewById(R.id.textViewFRTextNombre);
        tvApellidos = (TextView) this.findViewById(R.id.textViewFRTextApellidos);
        tvEmail = (TextView) this.findViewById(R.id.textViewFRTextEmail);
        tvFecha_ini = (TextView) this.findViewById(R.id.textViewFRTextFecha_ini);
        tvFecha_fin = (TextView) this.findViewById(R.id.textViewFRTextFecha_fin);
        tvCoste = (TextView) this.findViewById(R.id.textViewFRTextCoste);
        btn_volver = (Button) this.findViewById(R.id.buttonFRVolver);
        btn_rent = (Button) this.findViewById(R.id.buttonFRRent);

        int pos_furgo = intent.getIntExtra("clave",0);
        String marca = RentalVan.MARCA[pos_furgo];
        String modelo = RentalVan.MODELO[pos_furgo];


        double coste = rentalVan.calculaAlquiler(pos_furgo);

        imgFurgoSelec.setImageResource(RentalVan.IMAGEN_FURGOS[pos_furgo]);
        tvMarca.setText(marca);
        tvModelo.setText(modelo);

        tvFecha_ini.setText(fecha_ini);
        tvFecha_fin.setText(fecha_fin);

        tvCoste.setText(String.valueOf(coste));

       tvFecha_ini.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               abrirCalendario(tvFecha_ini);
           }
       });

        tvFecha_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario(tvFecha_fin);
            }
        });

    }

    private void abrirCalendario(TextView v) {
        Calendar calendario = Calendar.getInstance();
        int anho = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog aux = new DatePickerDialog(FurgoRentActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                v.setText( year + "/" + month + "/" + dayOfMonth);
            }
        }, anho, mes, dia);
        aux.show();
    }

}
