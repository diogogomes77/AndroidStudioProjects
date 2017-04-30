package pt.isec.cubi.tp1_cubi_2016_17;

import java.security.Timestamp;
import java.util.Date;


public class Registo {
    private Double lat;
    private Double lon;
    private Double alt;
    private long timestamp;
    private Float xAcc;
    private Float yAcc;
    private Float zAcc;
    private Float xGyro;
    private Float yGyro;
    private Float zGyro;
    private  Float luminosidade;
    boolean complete;

    private Registo instance = null;

    public Registo () {
        this.timestamp = new Date().getTime();
        complete = false;
    }

    public Registo getInstance(){
        if (instance == null) {
            instance = new Registo();
        }
        return instance;
    }

    public boolean isComplete(){

        if (lat == null) {
            return false;
        }
        if (lon == null) {
            return false;
        }
        if (alt == null) {
            return false;
        }

        if (xAcc == null) {
            return false;
        }
        if (yAcc == null) {
            return false;
        }
        if (zAcc == null) {
            return false;
        }
        if (xGyro == null) {
            return false;
        }
        if (yGyro == null) {
            return false;
        }
        if (zGyro == null) {
            return false;
        }
        if (luminosidade == null) {
            return false;
        }

        // Save csv
        return true;
    }
    public void setLat(Double lat) {
        this.lat = lat;

    }

    public void setLon(Double lon) {
        this.lon = lon;

    }

    public void setAlt(Double alt) {
        this.alt = alt;

    }

    public void setxAcc(Float xAcc) {
        this.xAcc = xAcc;

    }

    public void setyAcc(Float yAcc) {
        this.yAcc = yAcc;

    }

    public void setzAcc(Float zAcc) {
        this.zAcc = zAcc;

    }

    public void setxGyro(Float xGyro) {
        this.xGyro = xGyro;

    }

    public void setyGyro(Float yGyro) {
        this.yGyro = yGyro;

    }

    public void setzGyro(Float zGyro) {
        this.zGyro = zGyro;
 ;
    }

    public void setLuminosidade(Float luminosidade) {
        this.luminosidade = luminosidade;

    }
}
