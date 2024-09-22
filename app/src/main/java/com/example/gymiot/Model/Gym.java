package com.example.gymiot.Model;

import java.util.ArrayList;

public class Gym {
    private String gymName;
    private String horario;
    private String mensualidad;
    private String ubicacion;
    private String diario;
    private String imageUrl; // Cambiado de imagenUrl a imageUrl
    private ArrayList<String> maquinasDisponibles;

    // Constructor vacío para Firebase
    public Gym() {
    }

    // Constructor con parámetros
    public Gym(String gymName, String horario, String mensualidad, String ubicacion, String diario, String imageUrl, ArrayList<String> maquinasDisponibles) {
        this.gymName = gymName;
        this.horario = horario;
        this.mensualidad = mensualidad;
        this.ubicacion = ubicacion;
        this.diario = diario;
        this.imageUrl = imageUrl;
        this.maquinasDisponibles = maquinasDisponibles;
    }

    // Getters y Setters
    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMensualidad() {
        return mensualidad;
    }

    public void setMensualidad(String mensualidad) {
        this.mensualidad = mensualidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDiario() {
        return diario;
    }

    public void setDiario(String diario) {
        this.diario = diario;
    }

    public String getImageUrl() { // Cambiado a imageUrl
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) { // Cambiado a imageUrl
        this.imageUrl = imageUrl;
    }

    public ArrayList<String> getMaquinasDisponibles() {
        return maquinasDisponibles;
    }

    public void setMaquinasDisponibles(ArrayList<String> maquinasDisponibles) {
        this.maquinasDisponibles = maquinasDisponibles;
    }
}
