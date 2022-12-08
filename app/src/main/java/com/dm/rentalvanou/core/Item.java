package com.dm.rentalvanou.core;

public class Item {
    private int img;
    private String marca;
    private String modelo;
    private String combustible;
    private String f_ini;
    private String f_fin;
    private String coste;

    public Item(int img, String marca){
        this.img = img;
        this.marca = marca;
    }

    public Item(String marca, String modelo, String combustible, String f_ini, String f_fin, String coste){
        this.marca = marca;
        this.modelo = modelo;
        this.combustible = combustible;
        this.f_ini = f_ini;
        this.f_fin = f_fin;
        this.coste = coste;
    }

    public int getImg(){ return this.img; }

    public String getMarca() {
        return this.marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getCombustible() {
        return combustible;
    }

    public String getF_ini() {
        return f_ini;
    }

    public String getF_fin() {
        return f_fin;
    }

    public String getCoste() {
        return coste;
    }
}
