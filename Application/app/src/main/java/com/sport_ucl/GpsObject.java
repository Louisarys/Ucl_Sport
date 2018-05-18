package com.sport_ucl;

/**
 * Created by Nour-Eddine on 17/03/18.
 */


/**
 * Création d'un objet Gps pour facilter l'impléméntation
 * d'une ligne dans la table des lieux GPS
 */

public class GpsObject {

    /*
        Création de l'objet Gps afin de pouvoir plus facilement implémenter une ligne dans la table
     */

    private long idGpsLieu ;
    private String nom    ;
    private String lieu  ;
    private double longitude ;
    private double latitude  ;


    public GpsObject(long idGpsLieu, String nom, String lieu, double longitude, double latitude) {
        this.idGpsLieu = idGpsLieu;
        this.nom = nom;
        this.lieu = lieu;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getIdGpsLieu() {
        return idGpsLieu;
    }

    public void setIdGpsLieu(long idGpsLieu) {
        this.idGpsLieu = idGpsLieu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
