package com.example.s334886_mappe3_deryja;

public class Sted {


    private double latitude;
    private double longitude;
    private String adresse;
    private String beskrivelse;

    public Sted(double latitude, double longitude, String adresse, String beskrivelse) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresse = adresse;
        this.beskrivelse = beskrivelse;
    }


    public Sted(){}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }
}



