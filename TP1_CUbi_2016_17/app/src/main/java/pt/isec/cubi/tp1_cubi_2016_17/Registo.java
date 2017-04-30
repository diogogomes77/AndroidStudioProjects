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
    private String activity;

    public Registo () {
        this.timestamp = new Date().getTime();
    }

    public String csvHeader(){
        return ("lat,lng,alt,timestamp,x_acc,y_acc,z_acc,x_gyro,y_gyro,z_gyro,optSensor,activity");
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(lat));
        sb.append(",");
        sb.append(String.valueOf(lon));
        sb.append(",");
        sb.append(String.valueOf(alt));
        sb.append(",");
        sb.append(String.valueOf(timestamp));
        sb.append(",");
        sb.append(String.valueOf(xAcc));
        sb.append(",");
        sb.append(String.valueOf(yAcc));
        sb.append(",");
        sb.append(String.valueOf(zAcc));
        sb.append(",");
        sb.append(String.valueOf(xGyro));
        sb.append(",");
        sb.append(String.valueOf(yGyro));
        sb.append(",");
        sb.append(String.valueOf(zGyro));
        sb.append(",");
        sb.append(String.valueOf(luminosidade));
        sb.append(",");
        sb.append(activity);

        return sb.toString();
    }

    public boolean isComplete(){

        /*
        if (lat == null) {
            return false;
        }
        if (lon == null) {
            return false;
        }
        if (alt == null) {
            return false;
        }*/
        if (xAcc == null) {
            return false;
        }
        if (yAcc == null) {
            return false;
        }
        if (zAcc == null) {
            return false;
        }
        /*
        if (xGyro == null) {
            return false;
        }
        if (yGyro == null) {
            return false;
        }
        if (zGyro == null) {
            return false;
        }*/
        if (luminosidade == null) {
            return false;
        }
        if (activity == null) {
            return false;
        }
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
    }

    public void setLuminosidade(Float luminosidade) {
        this.luminosidade = luminosidade;
    }

    public String getActivity() {
        return activity;
    }
    public void setActivity(String activity) {
        this.activity = activity;
    }
}
