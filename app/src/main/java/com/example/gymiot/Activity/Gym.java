package com.example.gymiot.Model;

public class Gym {
    private String gymName;
    private String horario;
    private String mensualidad;
    private String ubicacion;
    private String diario;
    private String imagenUrl; // Para almacenar la URL de la imagen del gimnasio

    // Constructor vacío para Firebase
    public Gym() {
    }

    // Constructor con parámetros
    public Gym(String gymName, String horario, String mensualidad, String ubicacion, String diario, String imagenUrl) {
        this.gymName = gymName;
        this.horario = horario;
        this.mensualidad = mensualidad;
        this.ubicacion = ubicacion;
        this.diario = diario;
        this.imagenUrl = imagenUrl;
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

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
