package com.dm.rentalvanou.iu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dm.rentalvanou.core.RentalVan;

public class MainconLoginActivity extends AppCompatActivity {

    ListView lvFurgos;
    ListView lvMarcasFurgos;
    TextView tvUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainconlogin);

        // USUARIO
        tvUser = (TextView) this.findViewById(R.id.textViewMcLUser);
        tvUser.setText("Rober");

        // HISTORIAL
        Button btnHistorial = (Button) this.findViewById(R.id.buttonMcLHistorial);
        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainconLoginActivity.this, DepruebaActivity.class));
            }
        });

        // TU CUENTA
        Button btntuCuenta = (Button) this.findViewById(R.id.buttonMcLtuCuenta);
        btntuCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainconLoginActivity.this, PersonalInfoActivity.class));
            }
        });


        // FILTRO CARACTERÍSTICAS
        Spinner spinnerFiltroCarac = (Spinner) this.findViewById(R.id.spinnerMcLFiltroCarac);
        ArrayAdapter<String> adapter_filtro_carac = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RentalVan.FILTRO_CARAC);
        spinnerFiltroCarac.setAdapter(adapter_filtro_carac);

        // VISUALIZACIÓN DE FURGONETAS
        lvFurgos = (ListView) this.findViewById(R.id.McLArrayFurgos);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RentalVan.FURGONETAS);
        lvFurgos.setAdapter(adapter);


        lvMarcasFurgos = (ListView) this.findViewById(R.id.McLArrayMarcas);
        ArrayAdapter<String> adapter_marcas = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RentalVan.MARCA);
        lvMarcasFurgos.setAdapter(adapter_marcas);


        // SELECCION DE FURGONETA CARACTERÍSTICAS
        lvFurgos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < RentalVan.FURGONETAS.length) {
                    String valor = parent.getItemAtPosition(position).toString();
                    pasaInfo(valor);
                } else {
                    Toast.makeText(MainconLoginActivity.this, "La característica no existe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // VISUALIZACIÓN DE FURGONETAS FON FILTRO
        showFurgos(spinnerFiltroCarac);

    }


    //Método donde intento pasar información a otra actividad
    private void pasaInfo(String valor) {
        Intent intent = new Intent(this, FurgoviewActivity.class);
        intent.putExtra("clave", valor);
        startActivity(intent);
    }

    // showFurgos: este método recoge la selección del spinner de filtros de características y le pasa esa selección al método showFurgos()
    private void showFurgos(Spinner sp) {

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < RentalVan.FILTRO_CARAC.length) {
                    showFurgonetas(RentalVan.FILTRO_CARAC[position]);
                } else {
                    Toast.makeText(MainconLoginActivity.this, "La característica no existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //showFurgos: es el método encargado de visualizar el listView con el filtro seleccionado.
    private void showFurgonetas(String aux){
        RentalVan rentalvan = new RentalVan();
        lvFurgos = (ListView) this.findViewById(R.id.McLArrayFurgos);
        lvMarcasFurgos = (ListView) this.findViewById(R.id.McLArrayMarcas);
        ArrayAdapter<String> adapter;
        ArrayAdapter<String> adapter_marcas;
        String[] nfurgos;
        String[] marca;

        switch (aux){
            case "mayor altura":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(RentalVan.ALTURA));
                marca = rentalvan.copiaOrden(RentalVan.MARCA,rentalvan.ordenaMayor(RentalVan.ALTURA));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter_marcas = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,marca);
                lvMarcasFurgos.setAdapter(adapter_marcas);
                break;
            case "mayor ancho":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(RentalVan.ANCHO));
                marca = rentalvan.copiaOrden(RentalVan.MARCA,rentalvan.ordenaMayor(RentalVan.ANCHO));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter_marcas = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,marca);
                lvMarcasFurgos.setAdapter(adapter_marcas);
                break;
            case "mayor largo":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(RentalVan.LARGO));
                marca = rentalvan.copiaOrden(RentalVan.MARCA,rentalvan.ordenaMayor(RentalVan.LARGO));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter_marcas = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,marca);
                lvMarcasFurgos.setAdapter(adapter_marcas);
                break;
            case "mayor capacidad":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(RentalVan.CAPACIDAD));
                marca = rentalvan.copiaOrden(RentalVan.MARCA,rentalvan.ordenaMayor(RentalVan.CAPACIDAD));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter_marcas = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,marca);
                lvMarcasFurgos.setAdapter(adapter_marcas);
                break;
            default:
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RentalVan.FURGONETAS);
                lvFurgos.setAdapter(adapter);
                adapter_marcas = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,RentalVan.MARCA);
                lvMarcasFurgos.setAdapter(adapter_marcas);
        }

    }

}
