package com.dm.rentalvanou.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.dm.rentalvanou.iu.R;

public class DBManager extends SQLiteOpenHelper {
    public static final String DB_NOMBRE = "rentalvanouDB";
    public static final int DB_VERSION = 1;

    //DATOS TABLA FURGONETAS
    public static final String TABLA_VAN = "furgonetas";
    public static final String VAN_ID = "id";
    public static final String VAN_IMG = "furgoimg";
    public static final String VAN_MARCA = "marca";
    public static final String VAN_MODELO = "modelo";
    public static final String VAN_ALTURA = "alto";
    public static final String VAN_ANCHO = "ancho";
    public static final String VAN_LARGO = "largo";
    public static final String VAN_CAPACIDAD = "capacidad";
    public static final String VAN_COMBUSTIBLE = "combustible";

    //DATOS TABLA USUARIOS
    public static final String TABLA_USER = "usuarios";
    public static final String USER_ID = "id";
    public static final String USER_NOMBRE = "nombre";
    public static final String USER_APELLIDOS = "apellidos";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWD = "password";

    //DATOS TABLA ALQUILERES
    public static final String TABLA_RENT = "alquileres";
    public static final String RENT_ID = "id";
    public static final String RENT_VAN_ID = "id_van";
    public static final String RENT_VAN_COMBUSTIBLE = "combustible";
    public static final String RENT_USER_ID = "id_user";
    public static final String RENT_F_INI = "fecha_inicio";
    public static final String RENT_F_FIN = "fecha_fin";
    public static final String RENT_COSTE = "coste";

    private static DBManager instancia = null;

    private DBManager(Context c){
        super(c, DB_NOMBRE, null, DB_VERSION);
    }

    public static DBManager getManager(Context c) {
        if(instancia == null) {
            instancia = new DBManager(c);
        }
        return instancia;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.i("DB_Manager", "Creando BBDD " + DB_NOMBRE + " v " + DB_VERSION);
        try{
            db.beginTransaction();
            //db.execSQL(aux.toString());
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLA_VAN + "("
                    + VAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + VAN_IMG + " INTEGER UNIQUE,"
                    + VAN_MARCA + " TEXT NOT NULL,"
                    + VAN_MODELO + " TEXT NOT NULL,"
                    + VAN_ALTURA + " TEXT NOT NULL,"
                    + VAN_ANCHO + " TEXT NOT NULL,"
                    + VAN_LARGO + " TEXT NOT NULL,"
                    + VAN_CAPACIDAD + " TEXT NOT NULL,"
                    + VAN_COMBUSTIBLE + " TEXT NOT NULL"
                    + ")"
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLA_USER + "("
                    + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + USER_NOMBRE + " TEXT NOT NULL,"
                    + USER_APELLIDOS + " TEXT NOT NULL,"
                    + USER_EMAIL + " TEXT UNIQUE,"
                    + USER_PASSWD + " TEXT NOT NULL"
                    + ")"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLA_RENT + "("
                    + RENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + RENT_VAN_ID + " INTEGER NOT NULL,"
                    + RENT_VAN_COMBUSTIBLE + " TEXT NOT NULL,"
                    + RENT_USER_ID + " INTEGER NOT NULL,"
                    + RENT_F_INI + " TEXT NOT NULL,"
                    + RENT_F_FIN + " TEXT NOT NULL,"
                    + RENT_COSTE + " TEXT NOT NULL"
                    + ")"
            );
            loadInicial(db);
            db.setTransactionSuccessful();
        }
        catch(SQLException exc){
            Log.e("DBManager.onCreate", "Creando " + TABLA_VAN + ", " + TABLA_USER + ", " + TABLA_RENT + ": " + exc.getMessage());
        }
        finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(  "DBManager",
                "DB: " + DB_NOMBRE + ": v" + oldVersion + " -> v" + newVersion );

        try {
            db.beginTransaction();
            db.execSQL( "DROP TABLE IF EXISTS " + TABLA_VAN );
            db.execSQL( "DROP TABLE IF EXISTS " + TABLA_USER );
            db.execSQL( "DROP TABLE IF EXISTS " + TABLA_RENT );
            db.setTransactionSuccessful();
        }  catch(SQLException exc) {
            Log.e( "DBManager.onUpgrade", exc.getMessage() );
        }
        finally {
            db.endTransaction();
        }
        this.onCreate( db );
    }

    public void loadInicial(SQLiteDatabase db) {

        int[] IMAGEN_FURGOS = {R.drawable.jumper, R.drawable.boxer, R.drawable.kangoo, R.drawable.courier, R.drawable.scudo, R.drawable.vivaro};
        String[] MARCA = {"Citroen", "Peugeot", "Renault", "Ford", "Fiat", "Opel"};
        String[] MODELO = {"Jumper", "Boxer", "Kangoo", "Courier", "Scudo", "Vivaro"};
        int[] ALTURA = {2254, 2522, 1864, 1764, 2779, 1971};
        int[] ANCHO = {2050, 2050, 1829, 1770, 2050, 1956};
        int[] LARGO = {4963, 5413, 3871, 4157, 6363, 4999};
        int[] CAPACIDAD = {12, 15, 3, 4, 17, 7};
        String[] combustibles = {"DIESEL", "GAS", "ELECTRICO"};
        String[] COMBUSTIBLE = {combustibles[0], combustibles[0], combustibles[0], combustibles[0], combustibles[0], combustibles[0]};

        ContentValues valores = new ContentValues();
        for (int i = 0; i < IMAGEN_FURGOS.length; i++) {
            valores.put(VAN_IMG, IMAGEN_FURGOS[i]);
            valores.put(VAN_MARCA, MARCA[i]);
            valores.put(VAN_MODELO, MODELO[i]);
            valores.put(VAN_ALTURA, ALTURA[i]);
            valores.put(VAN_ANCHO, ANCHO[i]);
            valores.put(VAN_LARGO, LARGO[i]);
            valores.put(VAN_CAPACIDAD, CAPACIDAD[i]);
            valores.put(VAN_COMBUSTIBLE, COMBUSTIBLE[i]);
            try {
                db.beginTransaction();
                db.insert(TABLA_VAN, null, valores);
                db.setTransactionSuccessful();
            } catch (SQLException exc) {
                Log.e("DBManager.dbInicial", exc.getMessage());
            } finally {
                db.endTransaction();
            }
        }
    }

    // Gets

    public Cursor getVans(SQLiteDatabase db) {
        return db.query( TABLA_VAN, null, null, null, null, null, null );
    }

    public Cursor getRents(SQLiteDatabase db, int id){
        Cursor toret = null;
        try{
            toret = db.rawQuery(" SELECT * FROM alquileres, furgonetas WHERE alquileres.id_van = furgonetas.id AND alquileres.id_user = " + id + " ORDER BY alquileres.fecha_inicio DESC", null);
        }
        catch (SQLException e){
            Log.e("USER NO EXISTE", e.getMessage());
        }

        return toret;
    }

    public Cursor getUser(SQLiteDatabase db, String email){
        Cursor toret = null;
        try{
            toret = db.rawQuery(" SELECT * FROM " + TABLA_USER + " WHERE " + USER_EMAIL + " = ? ", new String[]{email});
        }
        catch (SQLException e){
            Log.e("USER NO EXISTE", e.getMessage());
        }
        return toret;
    }

    public Cursor getLogin(SQLiteDatabase db, String email, String passwd){
        Cursor toret = null;
        try{
            toret = db.rawQuery(" SELECT * FROM " + TABLA_USER + " WHERE " + USER_EMAIL + " = ? AND " + USER_PASSWD + " = ? ", new String[]{email,passwd});
        }
        catch (SQLException e){
            Log.e("USER NO EXISTE", e.getMessage());
        }
        return toret;
    }

    // ADDS

    public void addVan(String[] valores){
        SQLiteDatabase db = instancia.getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL(" INSERT OR IGNORE INTO " + TABLA_VAN
                    + " ( " + VAN_IMG
                    + " , " + VAN_MARCA
                    + " , " + VAN_MODELO
                    + " , " + VAN_ALTURA
                    + " , " + VAN_ANCHO
                    + " , " + VAN_LARGO
                    + " , " + VAN_CAPACIDAD
                    + " , " + VAN_COMBUSTIBLE
                    + " )  VALUES( ?, ?, ?, ?, ?, ?, ?, ?) ", valores);
            db.setTransactionSuccessful();
        }
        catch (SQLException exc){
            Log.e("Error en la inserción", exc.getMessage());
        }
        finally {
            db.endTransaction();
        }
    }
    public void addUser(String[] values){
        SQLiteDatabase db = instancia.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(USER_NOMBRE, values[0]);
        valores.put(USER_APELLIDOS, values[1]);
        valores.put(USER_EMAIL, values[2]);
        valores.put(USER_PASSWD, values[3]);
        try {
            db.beginTransaction();
            db.insert(TABLA_USER, null, valores);
            db.setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e("Error en la inserción", exc.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public void addRents(String[] values){
        SQLiteDatabase db = instancia.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(RENT_VAN_ID, Integer.parseInt(values[0]));
        valores.put(RENT_VAN_COMBUSTIBLE, values[1]);
        valores.put(RENT_USER_ID, Integer.parseInt(values[2]));
        valores.put(RENT_F_INI, values[3]);
        valores.put(RENT_F_FIN, values[4]);
        valores.put(RENT_COSTE, values[5]);
        try {
            db.beginTransaction();
            db.insert(TABLA_RENT, null, valores);
            db.setTransactionSuccessful();

        } catch (SQLException exc) {
            Log.e("Error en la inserción", exc.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    // SEARCH

    public Cursor searchVan(SQLiteDatabase db, int value){
        Cursor toret = null;
        try {
            toret  = db.rawQuery( " SELECT " + VAN_ID + " FROM " + TABLA_VAN + " WHERE " + VAN_IMG + " = " + value, null);
        }
        catch(SQLException exc) {
            Log.e( "DBManager.searchFor", exc.getMessage() );
        }
        return toret;
    }

    public Cursor searchUser(SQLiteDatabase db, String value){
        Cursor toret = null;
        try {
            toret = db.rawQuery( " SELECT " + USER_ID + " FROM " + TABLA_USER + " WHERE " + USER_EMAIL + " = ?", new String[]{value});
        }
        catch(SQLException exc) {
            Log.e( "DBManager.searchFor", exc.getMessage() );
        }
        return toret;
    }

    // DELETE COUNT

    public boolean deleteCount( int userid) {
        SQLiteDatabase db = instancia.getWritableDatabase();
        boolean toret = false;
        try {
            db.beginTransaction();
            db.execSQL("DELETE FROM " + TABLA_RENT + " WHERE " + RENT_USER_ID + "=" + userid);
            db.execSQL("DELETE FROM " + TABLA_USER + " WHERE " + USER_ID + "=" + userid);
            db.setTransactionSuccessful();
            toret = true;
        } catch (SQLException exc) {
            Log.e("DBManager.elimina", exc.getMessage());
            toret = false;
        } finally {
            db.endTransaction();
        }
        return toret;
    }
}
