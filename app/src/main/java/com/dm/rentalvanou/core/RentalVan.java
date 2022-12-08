package com.dm.rentalvanou.core;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.dm.rentalvanou.iu.R;
import com.dm.rentalvanou.model.DBManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RentalVan extends AppCompatActivity {

    public static String[] FILTRO_CARAC = {"todas","mayor altura","mayor ancho","mayor largo","mayor capacidad"};
    public static final double PRECIO_FIJO = 35.00;
    private String[] combustibles = {"DIESEL", "GAS", "ELECTRICO"};
    private int[] IMAGEN_FURGOS;
    public static String[] FURGONETAS;
    private String[] MARCA;
    private String[] MODELO;
    private int[] ALTURA;
    private int[] ANCHO;
    private int[] LARGO;
    private int[] CAPACIDAD;
    private String[] COMBUSTIBLE;
    SQLiteDatabase db;

    public RentalVan(SQLiteDatabase db){
        this.db = db;
        Cursor cursorVan = this.db.query(DBManager.TABLA_VAN, null, null, null, null, null, null );
        int size = cursorVan.getCount();

        FURGONETAS = new String[size];
        IMAGEN_FURGOS = new int[size];
        MARCA = new String[size];
        MODELO = new String[size];
        ALTURA = new int[size];
        ANCHO = new int[size];
        LARGO = new int[size];
        CAPACIDAD = new int[size];
        COMBUSTIBLE = new String[size];

        if(cursorVan.moveToFirst()){
            int i = 0;
            do {
                FURGONETAS[i] = String.valueOf(i+1);
                IMAGEN_FURGOS[i] =  cursorVan.getInt(1);
                MARCA[i] = cursorVan.getString(2);
                MODELO[i] = cursorVan.getString(3);
                ALTURA[i] = cursorVan.getInt(4);
                ANCHO[i] = cursorVan.getInt(5);
                LARGO[i] = cursorVan.getInt(6);
                CAPACIDAD[i] = cursorVan.getInt(7);
                COMBUSTIBLE[i] = cursorVan.getString(8);
                i++;
            } while ( cursorVan.moveToNext() );
        }
        cursorVan.close();
    }

    public int[] getImgVans(){
        return IMAGEN_FURGOS;
    }

    public String[] getMarcas(){
        return MARCA;
    }

    public int[] getAlturas(){
        return ALTURA;
    }

    public int[] getAnchos(){
        return ANCHO;
    }

    public int[] getLargos(){
        return LARGO;
    }

    public int[] getCapacidades(){
        return CAPACIDAD;
    }

    public int getImgVan(int pos){
        return IMAGEN_FURGOS[pos];
    }

    public String getMarca(int pos){
        return MARCA[pos];
    }

    public String getModelo(int pos){
        return MODELO[pos];
    }

    public int getAltura(int pos){
        return ALTURA[pos];
    }

    public int getAncho(int pos){
        return ANCHO[pos];
    }

    public int getLargo(int pos){
        return LARGO[pos];
    }

    public int getCapacidad(int pos){
        return CAPACIDAD[pos];
    }

    public String getCombustible(int pos){ return COMBUSTIBLE[pos];}

    public void setTipoCombustible(int pos, int tipo){ COMBUSTIBLE[pos] = combustibles[tipo];}


    public double calculaTipoCombustible(int pos){
        double toret = PRECIO_FIJO;

        if(COMBUSTIBLE[pos] == "GAS"){
            return toret+=5.00;
        }
        else if(COMBUSTIBLE[pos] == "ELECTRICO"){
            return toret+=10.00;
        }
        else{
            return toret;
        }
    }

    public Double calculaAlquiler(int pos,String fecha_ini, String fecha_fin){
        double toret = calculaTipoCombustible(pos);
        int total_dias = 1;
        try{
            total_dias = calcularDiasAlquiler(fecha_ini,fecha_fin);
            if(total_dias < 0){
                System.out.println("Hacer algo para gestionar el posible error");
                total_dias = 0;
            }
        }
        catch (Exception ex){
            System.out.println("Error en la fecha");
        }

        if(ANCHO[pos] >= 2050){
            toret += 20.00;
        }

        toret*=total_dias;

        return toret;
    }

    public int calcularDiasAlquiler(String fecha_ini, String fecha_fin) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date f_ini = sdf.parse(fecha_ini);
        Date f_fin = sdf.parse(fecha_fin);

        long toret = f_fin.getTime() - f_ini.getTime();

        TimeUnit time = TimeUnit.DAYS;
        long diferencia = time.convert(toret, TimeUnit.MILLISECONDS);
        return (int) diferencia;
    }

    // Método que encuentra la posición que ocupa el valor pasado por argumento en el array FURGONETAS
    //Devuelve la posición ocupada. Valor de tipo int. Si no encuentra la posición devuelve -1
    public int getPosicion(String valor){
        //String[] vans = loadVans();
        int toret = -1;
        for(int i = 0; i < FURGONETAS.length; i++){
            if(FURGONETAS[i].equals(valor)){
                toret = i;
            }
        }
        return toret;


    }

    // Método que ordena de mayor a menor los elementos de un array int[]
    //Devuelve una copia del array ordenado sin modificar el original
    public int[] ordenaMayor(int[] aOrdenar){

        //preparación
        int[] toret = new int[aOrdenar.length];
        for(int i = 0; i < toret.length; i++){
            toret[i] = i;
        }
        int[] aux = aOrdenar.clone();

        //ordenación
        for(int i = 0; i < aux.length; i++) {
            for(int j = i+1; j < aux.length; j++){
                if(aux[i] < aux[j]){
                    //Cambio el orden de toret
                    int pal = toret[i];
                    toret[i] = toret[j];
                    toret[j] = pal;
                    //Cambio el orden de aux
                    int aux_1 =  aux[i];
                    aux[i] = aux[j];
                    aux[j] = aux_1;
                }
            }
        }
        return toret;
    }

    //Método que ordena el array desordenado con el orden del array ordenado.
    //Devuelve una copia del array desordenado ya ordenado, sin modificar los originales.
    public String[] copiaOrden(String[] desordenado, int[] ordenado){
        String[] toret = new String[desordenado.length];
        for(int i = 0; i < desordenado.length; i++){
            toret[i] = desordenado[ordenado[i]];
        }
        return toret;
    }
    //sobre carga del metodo copia orden
    public List<Item> copiaOrden(List<Item> desordenado, int[] ordenado){
        List<Item> toret = new ArrayList<>();
        for(int i = 0; i < desordenado.size(); i++){
            toret.add(desordenado.get(ordenado[i]));
        }
        return toret;
    }

    public String toString(){
        StringBuilder toret = new StringBuilder();
        toret.append("Disponemos de las furgonetas: \n");

        for(int i = 0; i < FURGONETAS.length; i++){
            String furgo = FURGONETAS[i];
            String marca = MARCA[i];
            String modelo = MODELO[i];
            toret.append(furgo).append(", ").append(marca).append(": ").append(modelo).append("\n");
        }
        return toret.toString();
    }
}
