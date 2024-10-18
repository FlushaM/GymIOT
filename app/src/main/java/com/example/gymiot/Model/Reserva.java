package com.example.gymiot.Model;

public class Reserva {
    private String id;
    private String userId;
    private String gymId;
    private String diaReserva;
    private String horaReserva;

    public Reserva() {
    }

    public Reserva(String userId, String gymId, String diaReserva, String horaReserva) {
        this.userId = userId;
        this.gymId = gymId;
        this.diaReserva = diaReserva;
        this.horaReserva = horaReserva;
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public String getDiaReserva() {
        return diaReserva;
    }

    public void setDiaReserva(String diaReserva) {
        this.diaReserva = diaReserva;
    }

    public String getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(String horaReserva) {
        this.horaReserva = horaReserva;
    }
}
