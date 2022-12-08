package com.dm.rentalvanou.iu;

import androidx.appcompat.app.AppCompatActivity;

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
import android.app.Activity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import com.dm.rentalvanou.core.ImageArrayAdapter;
import com.dm.rentalvanou.core.Item;
import com.dm.rentalvanou.core.RentalVan;
import com.dm.rentalvanou.model.DBManager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

        RentalVan rentalVan;
        ListView lvFurgos;
        ListView lvMarcasFurgos;
        Button btnLogin;
        TextView tvUserName;
        String user_name = "";
        String user_email = "";
        List<Item> itemsView;
        private ActivityResultLauncher<Intent> activityResultLauncherEdit;
        SQLiteDatabase db;
        DBManager dbManager;
        ArrayAdapter<String> adapter;
        ImageArrayAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // LOGIN
        btnLogin = this.findViewById(R.id.buttonMainLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                activityResultLauncherEdit.launch(intent);
            }
        });
        ActivityResultContract<Intent, ActivityResult> contract =
                new ActivityResultContracts.StartActivityForResult();
        ActivityResultCallback<ActivityResult> callback =
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) { //se comprueba el resultado
                            Intent intent = result.getData(); //se obtienen los datos de resultado
                            user_name = intent.getExtras().getString("uname");
                            user_email = intent.getExtras().getString("uemail");
                            Toast.makeText(MainActivity.this, "UNAME" + user_name, Toast.LENGTH_SHORT).show();
                            if(user_name != ""){
                                tvUserName = (TextView) findViewById(R.id.TextViewUser);
                                tvUserName.setText(user_name);

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
                        }
                    }
                };
        this.activityResultLauncherEdit = this.registerForActivityResult(contract, callback);

        // REGISTRO
        Button btnRegistro = this.findViewById(R.id.buttonMainRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
            }
        });

    }

    public void onStart() {

        super.onStart();

        dbManager = DBManager.getManager(this.getApplicationContext());
        db = dbManager.getReadableDatabase();
        rentalVan = new RentalVan(db);

        // FILTRO CARACTERÍSTICAS
        Spinner spinnerFiltroCarac = this.findViewById(R.id.spinnerMainFiltroCarac);
        ArrayAdapter<String> adapter_filtro_carac = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,RentalVan.FILTRO_CARAC);
        spinnerFiltroCarac.setAdapter(adapter_filtro_carac);

        lvFurgos = this.findViewById(R.id.arrayFurgos);
        lvMarcasFurgos = this.findViewById(R.id.MainArrayMarcas);

        // VISUALIZACIÓN DE FURGONETAS
        lvFurgos = this.findViewById(R.id.arrayFurgos);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RentalVan.FURGONETAS);
        lvFurgos.setAdapter(adapter);

        // VISUALIZACIÓN DE FURGONETAS
        itemsView = new ArrayList<>();
        int[] vans = rentalVan.getImgVans();
        String[] marcas = rentalVan.getMarcas();
        for(int i = 0; i < vans.length; i++){
            Item aux = new Item(vans[i], marcas[i]);
            itemsView.add(aux);
        }

        lvMarcasFurgos = this.findViewById(R.id.MainArrayMarcas);
        adapter1 = new ImageArrayAdapter(this, R.layout.list_vans_layout, itemsView);
        lvMarcasFurgos.setAdapter(adapter1);

        // SELECCION DE FURGONETA
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

        // VISUALIZACIÓN DE FURGONETAS CON FILTRO
        showFurgos(spinnerFiltroCarac);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.dbManager.close();
        this.db.close();
    }

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
        ImageArrayAdapter adapter1;
        String[] nfurgos;

        //carga de items
        int[] vans = rentalVan.getImgVans();
        String[] marcas = rentalVan.getMarcas();
        List<Item> itemV = new ArrayList<>();
        for(int i = 0; i < vans.length; i++){
            Item sol = new Item(vans[i], marcas[i]);
            itemV.add(sol);
        }
        switch (aux){
            case "mayor altura":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(rentalvan.getAlturas()));
                itemV = rentalvan.copiaOrden(itemV, rentalvan.ordenaMayor(rentalvan.getAlturas()));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter1 = new ImageArrayAdapter(this, R.layout.list_vans_layout, itemV);
                lvMarcasFurgos.setAdapter(adapter1);
                break;
            case "mayor ancho":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(rentalvan.getAnchos()));
                itemV = rentalvan.copiaOrden(itemV,rentalvan.ordenaMayor(rentalvan.getAnchos()));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter1 = new ImageArrayAdapter(this, R.layout.list_vans_layout, itemV);
                lvMarcasFurgos.setAdapter(adapter1);
                break;
            case "mayor largo":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(rentalvan.getLargos()));
                itemV = rentalvan.copiaOrden(itemV,rentalvan.ordenaMayor(rentalvan.getLargos()));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter1 = new ImageArrayAdapter(this, R.layout.list_vans_layout, itemV);
                lvMarcasFurgos.setAdapter(adapter1);
                break;
            case "mayor capacidad":
                nfurgos = rentalvan.copiaOrden(RentalVan.FURGONETAS,rentalvan.ordenaMayor(rentalvan.getCapacidades()));
                itemV = rentalvan.copiaOrden(itemV,rentalvan.ordenaMayor(rentalvan.getCapacidades()));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nfurgos);
                lvFurgos.setAdapter(adapter);
                adapter1 = new ImageArrayAdapter(this, R.layout.list_vans_layout, itemV);
                lvMarcasFurgos.setAdapter(adapter1);
                break;
            default:
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RentalVan.FURGONETAS);
                lvFurgos.setAdapter(adapter);
                adapter1 = new ImageArrayAdapter(this, R.layout.list_vans_layout, itemV);
                lvMarcasFurgos.setAdapter(adapter1);
        }
    }
}