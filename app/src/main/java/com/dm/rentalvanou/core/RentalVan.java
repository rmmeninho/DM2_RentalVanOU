package com.dm.rentalvanou.core;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.dm.rentalvanou.iu.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RentalVan {
    public static int[] IMAGEN_FURGOS = {R.drawable.jumper, R.drawable.boxer, R.drawable.kangoo,R.drawable.courier,R.drawable.scudo,R.drawable.vivaro};
    public static String[] FURGONETAS = {"Furgo 1","Furgo 2","Furgo 3","Furgo 4","Furgo 5","Furgo 6"};
    public static String[] MARCA = {"Citroen","Peugeot","Renault","Ford","Fiat","Opel"};
    public static String[] MODELO = {"Jumper","Boxer","Kangoo","Courier","Scudo","Vivaro"};
    public static int[] ALTURA = {2254,2522,1864,1764,2779,1971};
    public static int[] ANCHO = {2050,2050,1829,1770,2050,1956};
    public static int[] LARGO = {4963,5413,3871,4157,6363,4999};
    public static int[] CAPACIDAD = {12,15,3,4,17,7};
    public static String[] FILTRO_CARAC = {"todas","Disponibles","mayor altura","mayor ancho","mayor largo","mayor capacidad"};
    //public static String[] FILTRO_CARAC = {"todas","mayor altura","menor altura","menor ancho","mayor ancho", "mayor capacidad", "menor capacidad"};
    //public static String[] FILTRO_MARCA = {"todas","Citroen","Peugeot","Renault","Ford","Fiat","Opel"};
    //public static String[] FILTRO_MODELO = {"todas", "Jumper","Boxer","Master","Transit","Ducato","Vivaro"};
    public static final double PRECIO_FIJO = 35.00;

    public RentalVan(){

    }

    public String getFurgoneta(int pos){
        return FURGONETAS[pos];
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

    public double getPrecio(){
        return PRECIO_FIJO;
    }


    public String calculaAlquiler(int pos,String fecha_ini, String fecha_fin){
        double toret = PRECIO_FIJO;
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

        return String.valueOf(toret+" $");
    }

    public int calcularDiasAlquiler(String fecha_ini, String fecha_fin) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
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
