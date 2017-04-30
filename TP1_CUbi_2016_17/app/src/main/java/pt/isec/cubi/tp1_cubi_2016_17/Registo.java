package pt.isec.cubi.tp1_cubi_2016_17;

import java.security.Timestamp;
import java.util.Date;


public class Registo {
    double lat;
    double lon;
    double alt;
    long timestamp;
    float xAcc;
    float yAcc;
    float zAcc;
    float xGyro;
    float yGyro;
    float zGyro;
    float luminosidade;

    public Registo() {
        this.timestamp = new Date().getTime();
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public void setxAcc(float xAcc) {
        this.xAcc = xAcc;
    }

    public void setyAcc(float yAcc) {
        this.yAcc = yAcc;
    }

    public void setzAcc(float zAcc) {
        this.zAcc = zAcc;
    }

    public void setxGyro(float xGyro) {
        this.xGyro = xGyro;
    }

    public void setyGyro(float yGyro) {
        this.yGyro = yGyro;
    }

    public void setzGyro(float zGyro) {
        this.zGyro = zGyro;
    }

    public void setLuminosidade(float luminosidade) {
        this.luminosidade = luminosidade;
    }
}
