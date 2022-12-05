package com.dm.rentalvanou.iu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.dm.rentalvanou.core.ImgArrayAdapter;
import com.dm.rentalvanou.core.Item;
import com.dm.rentalvanou.core.RentalVan;
import com.dm.rentalvanou.model.DBManager;

import java.util.ArrayList;
import java.util.List;

public class HistoricActivity extends AppCompatActivity {

    String user_name = null;
    String user_email = null;
    int userid = 0;
    TextView tvUser;
    RentalVan rentalVan;
    ListView lvHistorico;
    List<Item> items;
    Button btn_volver;
    Button btn_delete;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        DBManager dbManager = DBManager.getManager(this.getApplicationContext());
        db = dbManager.getReadableDatabase();
        rentalVan = new RentalVan(db);

        // DATOS DE OTRA ACTIVITY
        Intent intent = getIntent();
        try{
            user_name = intent.getStringExtra("uname");
            user_email = intent.getStringExtra("uemail");
        }
        catch (Exception ex){
            Log.e("ERROR", ex.getMessage());
            Toast.makeText(HistoricActivity.this, "UNAME: NULL", Toast.LENGTH_SHORT).show();
            Toast.makeText(HistoricActivity.this, "UEMAIL: NULL", Toast.LENGTH_SHORT).show();
        }

        // RECUPERA DATOS DESDE LA BD
        if(user_name != null){
            tvUser = findViewById(R.id.textViewHUser);
            tvUser.setText(user_name);
            Cursor user_id = dbManager.getUser(db, user_email);
            if(user_id.moveToFirst()){
                do{
                   userid = user_id.getInt(0);
                }while(user_id.moveToNext());
            }
            user_id.close();
        }

        ArrayList<Integer> id_van = new ArrayList<>();
        ArrayList<String> marca = new ArrayList<>();
        ArrayList<String> modelo = new ArrayList<>();
        ArrayList<String> fecha_ini = new ArrayList<>();
        ArrayList<String> fecha_fin = new ArrayList<>();
        ArrayList<String> combustible = new ArrayList<>();
        ArrayList<String> coste = new ArrayList<>();

        // traer de la BD los datos correspondientes.
        Cursor historic = dbManager.getRents(db, userid);
        if(historic != null){
            if(historic.moveToFirst()) {
                do {
                    id_van.add(historic.getInt(1));
                    combustible.add(historic.getString(2));
                    fecha_ini.add(historic.getString(4));
                    fecha_fin.add(historic.getString(5));
                    coste.add(historic.getString(6));
                    marca.add(historic.getString(9));
                    modelo.add(historic.getString(10));
                } while (historic.moveToNext());
            }

        items = new ArrayList<>();

        for(int i = 0; i < id_van.size()-1; i++){
            Item aux = new Item(marca.get(i), modelo.get(i), combustible.get(i), fecha_ini.get(i), fecha_fin.get(i), coste.get(i));
            items.add(aux);
        }
        lvHistorico = this.findViewById(R.id.ListHistoric);
        ImgArrayAdapter adapter = new ImgArrayAdapter(this, R.layout.list_item_layout, items);
        lvHistorico.setAdapter(adapter);
        }

        else{
            Toast.makeText(this, "ERROR EL CURSOR ES NULO", Toast.LENGTH_SHORT).show();
        }

        // BOTONES

        btn_volver = this.findViewById(R.id.buttonHVolver);
        btn_delete = this.findViewById(R.id.buttonHDELETE);

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasaInfo();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbManager.deleteCount(userid)){
                   startActivity(new Intent(HistoricActivity.this, MainActivity.class));
                }
                else{
                    Toast.makeText(HistoricActivity.this, "ERROR DE BORRADO", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void pasaInfo() {
        Intent intent = new Intent(this, MainActivity.class);
        Toast.makeText(HistoricActivity.this, "USERMAIL"+ user_name, Toast.LENGTH_SHORT).show();
        if(user_email != null){
            intent.putExtra("uname", user_name);
            intent.putExtra("uemail", user_email);
        }
        startActivity(intent);
    }
}
