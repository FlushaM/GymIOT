package com.example.gymiot.Model;

import java.util.ArrayList;
import java.util.List;

public class Gym {
    private String id;
    private String gymName;
    private String horarioApertura;
    private String horarioCierre;
    private ArrayList<String> diasDisponibles;
    private ArrayList<String> maquinasDisponibles;
    private String region;
    private String pais;
    private String ciudad;
    private String calle;
    private String mensualidad;
    private String diario;
    private List<String> imageUrls; // Cambiado para almacenar múltiples URLs de imágenes
    private String locationUrl; // Agregado para la URL de Google Maps

    // Constructor vacío para Firebase
    public Gym() {
    }

    public Gym(String id, String gymName, String horarioApertura, String horarioCierre, ArrayList<String> diasDisponibles,
               ArrayList<String> maquinasDisponibles, String region, String pais, String ciudad, String calle,
               String mensualidad, String diario, List<String> imageUrls, String locationUrl) {
        this.id = id;
        this.gymName = gymName;
        this.horarioApertura = horarioApertura;
        this.horarioCierre = horarioCierre;
        this.diasDisponibles = diasDisponibles;
        this.maquinasDisponibles = maquinasDisponibles;
        this.region = region;
        this.pais = pais;
        this.ciudad = ciudad;
        this.calle = calle;
        this.mensualidad = mensualidad;
        this.diario = diario;
        this.imageUrls = imageUrls;
        this.locationUrl = locationUrl;
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

    public String getHorarioApertura() {
        return horarioApertura;
    }

    public void setHorarioApertura(String horarioApertura) {
        this.horarioApertura = horarioApertura;
    }

    public String getHorarioCierre() {
        return horarioCierre;
    }

    public void setHorarioCierre(String horarioCierre) {
        this.horarioCierre = horarioCierre;
    }

    public ArrayList<String> getDiasDisponibles() {
        return diasDisponibles;
    }

    public void setDiasDisponibles(ArrayList<String> diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
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

    public List<String> getImageUrls() { // Getter para las URLs de las imágenes
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) { // Setter para las URLs de las imágenes
        this.imageUrls = imageUrls;
    }

    public String getLocationUrl() { // Getter para la URL de la ubicación
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) { // Setter para la URL de la ubicación
        this.locationUrl = locationUrl;
    }
}
