package com.dm.rentalvanou.iu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TintInfo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.rentalvanou.core.ImgArrayAdapter;
import com.dm.rentalvanou.core.Item;
import com.dm.rentalvanou.core.RentalVan;
import com.dm.rentalvanou.model.DBManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

        RentalVan rentalVan;
        ListView lvFurgos;
        ListView lvMarcasFurgos;
        TextView tvUserName;
        String user_name = null;
        String user_email = null;
        List<Item> items;
        SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBManager dbManager = DBManager.getManager(this.getApplicationContext());
        db = dbManager.getReadableDatabase();
        rentalVan = new RentalVan(db);

        Intent intent = getIntent();
        user_name = intent.getStringExtra("uname");
        user_email = intent.getStringExtra("uemail");

        if(user_name != null){
            tvUserName = (TextView) findViewById(R.id.TextViewUser);
            tvUserName.setText(user_name);

            Button btnLogin = this.findViewById(R.id.buttonMainLogin);
            btnLogin.setText("LOGOUT");
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user_name = null;
                    finish();
                    startActivity(getIntent());
                }
            });

            View btnRegistro = findViewById(R.id.buttonMainRegistro);
            btnRegistro.setVisibility(View.GONE);
        }

        // LOGIN
        Button btnLogin = this.findViewById(R.id.buttonMainLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


        // REGISTRO
        Button btnRegistro = this.findViewById(R.id.buttonMainRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
            }
        });


        // FILTRO CARACTERÍSTICAS
        Spinner spinnerFiltroCarac = this.findViewById(R.id.spinnerMainFiltroCarac);
        ArrayAdapter<String> adapter_filtro_carac = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,RentalVan.FILTRO_CARAC);
        spinnerFiltroCarac.setAdapter(adapter_filtro_carac);

        // VISUALIZACIÓN DE FURGONETAS
        lvFurgos = this.findViewById(R.id.arrayFurgos);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RentalVan.FURGONETAS);
        lvFurgos.setAdapter(adapter);

        lvMarcasFurgos = this.findViewById(R.id.MainArrayMarcas);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rentalVan.getMarcas());
        lvMarcasFurgos.setAdapter(adapter1);
/*
        // VISUALIZACIÓN DE FURGONETAS
        items = new ArrayList<>();
        int[] vans = rentalVan.getImgVans();
        String[] marcas = rentalVan.getMarcas();
        for(int i = 0; i < vans.length; i++){
            Item aux = new Item(vans[i], marcas[i]);
            items.add(aux);
        }
        lvFurgos = this.findViewById(R.id.arrayFurgos);
        ImgArrayAdapter adapter = new ImgArrayAdapter(this, R.layout.list_item_layout, items);
        lvFurgos.setAdapter(adapter);
*/
        // SELECCION DE FURGONETA CARACTERÍSTICAS
        lvFurgos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < RentalVan.FURGONETAS.length) {
                    String valor = parent.getItemAtPosition(position).toString();
                    pasaInfo(valor);
                } else {
                    Toast.makeText(MainActivity.this, "La característica no existe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // VISUALIZACIÓN DE FURGONETAS FON FILTRO
        showFurgos(spinnerFiltroCarac);
    }
    /*
    private void creaLista(){
        final ListView lvItems = this.findViewById( R.id.arrayFurgos );
        this.adapterList = new ItemArrayAdapter( this, this.items );
        lvItems.setAdapter( this.adapterList );
    }
    */
    //Método donde intento pasar información a otra actividad
    private void pasaInfo(String valor) {

        Intent intent = new Intent(this, FurgoviewActivity.class);
        intent.putExtra("clave", valor);
        if(user_name != null){
            intent.putExtra("uname", user_name);
            intent.putExtra("uemail", user_email);
        }
        else{
            Toast.makeText(MainActivity.this, "UNAME: Null", Toast.LENGTH_SHORT).show();
        }

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
                    Toast.makeText(MainActivity.this, "La característica no existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //showFurgos: es el método encargado de visualizar el listView con el filtro seleccionado.
    private void showFurgonetas(String aux){
        RentalVan rentalvan = new RentalVan(db);
        lvFurgos = this.findViewById(R.id.arrayFurgos);
        lvMarcasFurgos = this.findViewById(R.id.MainArrayMarcas);
        ArrayAdapter<String> adapter;
        ArrayAdapter<String> adapter1;
        String[] nfurgos;
        String[] marca;
        switch (aux){
            case "mayor altura":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(rentalvan.getAlturas()));
                marca = rentalvan.copiaOrden(rentalvan.getMarcas(), rentalvan.ordenaMayor(rentalvan.getAlturas()));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,marca);
                lvMarcasFurgos.setAdapter(adapter1);
                break;
            case "mayor ancho":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(rentalvan.getAnchos()));
                marca = rentalvan.copiaOrden(rentalvan.getMarcas(),rentalvan.ordenaMayor(rentalvan.getAnchos()));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,marca);
                lvMarcasFurgos.setAdapter(adapter1);
                break;
            case "mayor largo":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(rentalvan.getLargos()));
                marca = rentalvan.copiaOrden(rentalvan.getMarcas(),rentalvan.ordenaMayor(rentalvan.getLargos()));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter1= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,marca);
                lvMarcasFurgos.setAdapter(adapter1);
                break;
            case "mayor capacidad":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(rentalvan.getCapacidades()));
                marca = rentalvan.copiaOrden(rentalvan.getMarcas(),rentalvan.ordenaMayor(rentalvan.getCapacidades()));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,marca);
                lvMarcasFurgos.setAdapter(adapter1);
                break;
            default:
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RentalVan.FURGONETAS);
                lvFurgos.setAdapter(adapter);
                adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,rentalvan.getMarcas());
                lvMarcasFurgos.setAdapter(adapter1);
        }
    }
}