package com.dm.rentalvanou.iu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.dm.rentalvanou.core.HistoricArrayAdapter;
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
    CursorAdapter historico;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

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
                if (dbManager.deleteCount(userid)) {
                    startActivity(new Intent(HistoricActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(HistoricActivity.this, "ERROR DE BORRADO", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onStart() {

        super.onStart();
        dbManager = DBManager.getManager(this.getApplicationContext());
        db = dbManager.getReadableDatabase();
        rentalVan = new RentalVan(db);

        // DATOS DE OTRA ACTIVITY
        Intent intent = getIntent();
        try {
            user_name = intent.getStringExtra("uname");
            user_email = intent.getStringExtra("uemail");
        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
            Toast.makeText(HistoricActivity.this, "UNAME: NULL", Toast.LENGTH_SHORT).show();
            Toast.makeText(HistoricActivity.this, "UEMAIL: NULL", Toast.LENGTH_SHORT).show();
        }

        // RECUPERA DATOS DESDE LA BD
        if (user_name != null) {
            tvUser = findViewById(R.id.textViewHUser);
            tvUser.setText(user_name);
            Cursor user_id = dbManager.getUser(db, user_email);
            if (user_id.moveToFirst()) {
                do {
                    userid = user_id.getInt(0);
                } while (user_id.moveToNext());
            }
            user_id.close();
        }

        // traer de la BD los datos correspondientes.
        lvHistorico = this.findViewById(R.id.ListHistoric);
        historico = new SimpleCursorAdapter(HistoricActivity.this, R.layout.list_item_layout, null, new String[]{DBManager.VAN_MARCA, DBManager.VAN_MODELO, DBManager.RENT_VAN_COMBUSTIBLE, DBManager.RENT_F_INI, DBManager.RENT_F_FIN, DBManager.RENT_COSTE},
                new int[]{R.id.textViewLMarca, R.id.textViewLModelo, R.id.textViewLCombustible, R.id.textViewLFi, R.id.textViewLFf, R.id.textViewLCoste}, 0);
        lvHistorico.setAdapter(historico);
        actualizaHistorial(db, userid);
    }

    public void onPause(){
        super.onPause();
        this.dbManager.close();
        this.db.close();
    }

    private void pasaInfo() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("uname", user_name);
        intent.putExtra("uemail", user_email);
        //HistoricActivity.this.setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    // ON RESUME
    private void actualizaHistorial(SQLiteDatabase db, int userid) {
        this.historico.changeCursor(dbManager.getRents(db,userid));
    }
}
