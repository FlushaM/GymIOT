package com.example.gymiot.Model;

import java.util.ArrayList;

public class Gym {
    private String gymName; // gymName en camel case
    private String horario; // horario en camel case
    private String mensualidad; // mensualidad en camel case
    private String diario; // diario en camel case
    private String imageUrl; // imageUrl en camel case
    private ArrayList<String> maquinasDisponibles; // máquinas disponibles

    // Constructor vacío para Firebase
    public Gym() {
    }

    // Constructor con parámetros
    public Gym(String gymName, String horario, String mensualidad, String diario, String imageUrl, ArrayList<String> maquinasDisponibles) {
        this.gymName = gymName;
        this.horario = horario;
        this.mensualidad = mensualidad;
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

    public String getDiario() {
        return diario;
    }

    public void setDiario(String diario) {
        this.diario = diario;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<String> getMaquinasDisponibles() {
        return maquinasDisponibles;
    }

    public void setMaquinasDisponibles(ArrayList<String> maquinasDisponibles) {
        this.maquinasDisponibles = maquinasDisponibles;
    }
}
