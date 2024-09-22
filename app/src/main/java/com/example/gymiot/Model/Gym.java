package com.example.gymiot.Model;

import java.util.ArrayList;

public class Gym {
    private String id; // ID del documento
    private String gymName;
    private String horario;
    private String mensualidad;
    private String diario;
    private String imageUrl;
    private ArrayList<String> maquinasDisponibles;
    private String region;  // Nuevo campo
    private String pais;    // Nuevo campo
    private String ciudad;  // Nuevo campo
    private String calle;   // Nuevo campo

    // Constructor vacío para Firebase
    public Gym() {
    }

    // Constructor con parámetros (incluyendo los nuevos campos)
    public Gym(String id, String gymName, String horario, String mensualidad, String diario, String imageUrl,
               ArrayList<String> maquinasDisponibles, String region, String pais, String ciudad, String calle) {
        this.id = id;
        this.gymName = gymName;
        this.horario = horario;
        this.mensualidad = mensualidad;
        this.diario = diario;
        this.imageUrl = imageUrl;
        this.maquinasDisponibles = maquinasDisponibles;
        this.region = region;
        this.pais = pais;
        this.ciudad = ciudad;
        this.calle = calle;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }
}
