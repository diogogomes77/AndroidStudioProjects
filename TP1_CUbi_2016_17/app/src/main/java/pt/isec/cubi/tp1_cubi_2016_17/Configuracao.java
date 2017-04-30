package pt.isec.cubi.tp1_cubi_2016_17;


import android.widget.RadioButton;

import java.io.File;

public class Configuracao {
    private String filename = "DGJB_TP1";
    private String extencao = ".csv";
    private String folder = "/cubi1617_TP1";
    private String host = "kenobi.dei.uc.pt";
    private String user = "cubistudent";
    private String passw = "mis_cub_2017";

    private static Configuracao instance = null;

    protected Configuracao() {

    }

    public String getExtencao() {
        return extencao;
    }

    public void setExtencao(String extencao) {
        this.extencao = extencao;
    }

    public static Configuracao getInstance() {
        if(instance == null) {
            instance = new Configuracao();
        }
        return instance;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }
}
