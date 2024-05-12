package com.example.analizador_lexico;

public class etiqueta {

    String string;
    String etiqueta;
    int colmna;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public int getColmna() {
        return colmna;
    }

    public void setColmna(int colmna) {
        this.colmna = colmna;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    int fila;

    public etiqueta(String string, String etiqueta, int colmna, int fila) {
        this.string = string;
        this.etiqueta = etiqueta;
        this.colmna = colmna;
        this.fila = fila;
    }


}
