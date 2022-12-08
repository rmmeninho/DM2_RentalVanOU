package com.dm.rentalvanou.iu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dm.rentalvanou.core.RentalVan;
import com.dm.rentalvanou.model.DBManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FurgoRentActivity extends AppCompatActivity {

    String user_name = "";
    String user_email = "";
    String marca = "";
    String modelo = "";
    String tipo_combustible = "";
    int pos_furgo;
    double coste;
    // VARIABLES CALENDARIO
    public static String fecha_ini;
    public static String fecha_fin;
    // DATOS VEHÍCULO
    TextView tvTipo_combustible;
    ImageView imgFurgoSelec;
    TextView tvMarca;
    TextView tvModelo;
    // DATOS USUARIO
    TextView tvUser;
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
    DBManager dbManager;
    RentalVan rentalVan;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furgorent);

        // OPEN BD
        dbManager = DBManager.getManager(this.getApplicationContext());
        db = dbManager.getReadableDatabase();
        rentalVan = new RentalVan(db);

        // DATOS DE OTRA ACTIVITY
        Intent intent = getIntent();
        pos_furgo = intent.getIntExtra("clave",0);
        try{
            user_name = intent.getStringExtra("uname");
            user_email = intent.getStringExtra("uemail");
        }
        catch (Exception ex){
            Log.e("ERROR", ex.getMessage());
            Toast.makeText(FurgoRentActivity.this, "UNAME: NULL", Toast.LENGTH_SHORT).show();
            Toast.makeText(FurgoRentActivity.this, "UEMAIL: NULL", Toast.LENGTH_SHORT).show();
        }

        if(user_name != null){
            tvUser = findViewById(R.id.textViewFRUser);
            tvUser.setText(user_name);
        }

        // INICIO FECHA ACTUAL
        Calendar hoy = Calendar.getInstance();
        fecha_ini = String.valueOf(hoy.get(Calendar.YEAR))+"-"+String.valueOf(hoy.get(Calendar.MONTH)+1)+"-"+String.valueOf(hoy.get(Calendar.DAY_OF_MONTH));
        hoy.add(Calendar.DAY_OF_MONTH,1);
        fecha_fin = String.valueOf(hoy.get(Calendar.YEAR))+"-"+String.valueOf(hoy.get(Calendar.MONTH)+1)+"-"+String.valueOf(hoy.get(Calendar.DAY_OF_MONTH));

        //REGISTRO DE MENU CONTEXTUAL
        tvTipo_combustible = (TextView) this.findViewById(R.id.textViewFROpcionesConsumo);
        this.registerForContextMenu(tvTipo_combustible);

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


        marca = rentalVan.getMarca(pos_furgo);
        modelo = rentalVan.getModelo(pos_furgo);
        tipo_combustible = rentalVan.getCombustible(pos_furgo);

        // RECUPERA DATOS DESDE LA BD
        if(user_name != null){
            Cursor user_cursor = dbManager.getUser(db, user_email);
            if(user_cursor.moveToFirst()){
                do{
                    tvNombre.setText(user_cursor.getString(1));
                    tvApellidos.setText(user_cursor.getString(2));
                    tvEmail.setText(user_cursor.getString(3));
                }while(user_cursor.moveToNext());
            }
        }

        coste = rentalVan.calculaAlquiler(pos_furgo,fecha_ini,fecha_fin);

        imgFurgoSelec.setImageResource(rentalVan.getImgVan(pos_furgo));
        tvMarca.setText(marca);
        tvModelo.setText(modelo);
        rentalVan.setTipoCombustible(pos_furgo, 0);
        tvTipo_combustible.setText(tipo_combustible);

        tvFecha_ini.setText(fecha_ini);
        tvFecha_fin.setText(fecha_fin);

        tvCoste.setText(String.valueOf(coste)+" €");

        Toast.makeText(FurgoRentActivity.this, "USER_EMAIL " + tvEmail.getText().toString(), Toast.LENGTH_SHORT).show();

       tvFecha_ini.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               abrirCalendarioIni();
           }
       });

        tvFecha_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendarioFin();
            }
        });

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasaInfoVoler();
            }
        });

        btn_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_name != null) {
                    int img = rentalVan.getImgVan(pos_furgo);
                    String combustible = tipo_combustible;
                    //
                    Cursor cursor_van = dbManager.searchVan(db, img);
                    String id_van = "";
                    if (cursor_van.moveToFirst()) {
                        do {
                            id_van = String.valueOf(cursor_van.getInt(0));
                        } while (cursor_van.moveToNext());
                    }
                    cursor_van.close();
                    //
                    Cursor cursor_user = dbManager.searchUser(db, user_email);
                    String id_user = "";
                    if (cursor_user.moveToFirst()) {
                        do {
                            id_user = String.valueOf(cursor_user.getInt(0));
                        } while (cursor_user.moveToNext());
                    }
                    cursor_user.close();

                    // FORMATEO DE FECHAS
                    Date f_ini = null;
                    Date f_fin = null;
                    String rent_ini = "";
                    String rent_fin = "";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try{
                        f_ini = sdf.parse(fecha_ini);
                        f_fin = sdf.parse(fecha_fin);
                        SimpleDateFormat isoDateFormat = new SimpleDateFormat( "yyyy-MM-dd", Locale.ROOT );
                        isoDateFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
                        rent_ini = isoDateFormat.format(f_ini);
                        rent_fin = isoDateFormat.format(f_fin);
                    }
                    catch (ParseException ex){
                        Log.e("Error en el parseo",ex.getMessage());
                    }
                    // FORMATEO DE FECHAS

                    // RENT
                    String[] toret = {id_van, tipo_combustible, id_user, rent_ini, rent_fin, String.valueOf(coste)};
                    if(dbManager.addRents(toret)){
                        pasaInfo();
                    }
                    else{
                        Cursor disponible = dbManager.fechaDisponible(id_van,tipo_combustible);
                        String fecha_disponible = "";
                        if (disponible.moveToFirst()) {
                            do {
                                fecha_disponible = disponible.getString(0);
                            } while (cursor_van.moveToNext());
                        }
                        disponible.close();
                        Toast.makeText(FurgoRentActivity.this, "Furgoneta NO disponible rerservada hasta : " + fecha_disponible , Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(FurgoRentActivity.this, "Debes Logearte", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FurgoRentActivity.this, LoginActivity.class));
                }
            }
        });

    }


    public void onPause(){
        super.onPause();
    }

    //A continuación se imnplementa el menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if ( v.getId() == R.id.textViewFROpcionesConsumo){
            this.getMenuInflater().inflate( R.menu.furgorent_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        boolean toret = false;
        RentalVan rentalVan = new RentalVan(db);
        switch(item.getItemId()){
            case R.id.opGas:
                rentalVan.setTipoCombustible(pos_furgo, 1);
                toret = true;
                break;
            case R.id.opElectrico:
                rentalVan.setTipoCombustible(pos_furgo, 2);
                toret = true;
                break;
            default:
                rentalVan.setTipoCombustible(pos_furgo, 0);
                toret = true;
                break;
        }
        tipo_combustible = rentalVan.getCombustible(pos_furgo);
        this.tvTipo_combustible.setText(tipo_combustible);
        coste = rentalVan.calculaAlquiler(pos_furgo,fecha_ini,fecha_fin);
        tvCoste.setText(String.valueOf(coste)+" €");

        return toret;
    }

    private void abrirCalendarioIni() {
        RentalVan rentalVan = new RentalVan(db);
        Calendar calendario = Calendar.getInstance();
        int anho = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog aux = new DatePickerDialog(FurgoRentActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Toast.makeText(FurgoRentActivity.this, "FECHA INT: " +year+"-"+month+"-"+dayOfMonth, Toast.LENGTH_LONG).show();

                fecha_ini = year + "-" + (month+1) + "-" + dayOfMonth;
                tvFecha_ini.setText(fecha_ini);
                try{
                    if(rentalVan.calcularDiasAlquiler(fecha_ini,fecha_fin) < 0){
                        Toast.makeText(FurgoRentActivity.this, "Alguna de las fechas no es adecuada. \nRevise las fechas. Gracias", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    System.out.println("Error");
                }

                coste = rentalVan.calculaAlquiler(pos_furgo,fecha_ini,fecha_fin);

                tvCoste.setText(String.valueOf(coste)+" €");
            }
        }, anho, mes, dia);

        aux.show();
    }

    private void abrirCalendarioFin() {
        RentalVan rentalVan = new RentalVan(db);
        Calendar calendario = Calendar.getInstance();
        int anho = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog aux = new DatePickerDialog(FurgoRentActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha_fin = year + "-" + (month+1) + "-" + dayOfMonth;
                tvFecha_fin.setText(fecha_fin);
                try{
                    if(rentalVan.calcularDiasAlquiler(fecha_ini,fecha_fin) < 0){
                        Toast.makeText(FurgoRentActivity.this, "Alguna de las fechas no es adecuada. \nRevise las fechas. Gracias", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    System.out.println("Error");
                }
                coste = rentalVan.calculaAlquiler(pos_furgo,fecha_ini,fecha_fin);
                tvCoste.setText(String.valueOf(coste)+" €");
            }
        }, anho, mes, dia);

        aux.show();
    }


    private void pasaInfo() {
        Intent intent = new Intent(this, HistoricActivity.class);
        if(user_name != null){
            intent.putExtra("uname", user_name);
            intent.putExtra("uemail", user_email);
        }
        startActivity(intent);
    }

    private void pasaInfoVoler() {
        Intent intent = new Intent(this, MainActivity.class);
        if(user_name != null){
            intent.putExtra("uname", user_name);
            intent.putExtra("uemail", user_email);
        }
        startActivity(intent);
    }

}
