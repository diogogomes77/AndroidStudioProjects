package pt.isec.cubi.tp1_cubi_2016_17;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistosController {

    private Registo reg;
    private Configuracao config;
    private File path;
    private File dir;
    private File file;
    private static RegistosController instance = null;
    private long regIndex;
    private FileWriter fw;
    private BufferedWriter bw;
    private PrintWriter saveRegisto;
    private boolean saving;

    protected RegistosController() {
        reg = new Registo();
        config = Configuracao.getInstance();
        regIndex = 0;
    }

    public static RegistosController getInstance() {
        if(instance == null) {
            instance = new RegistosController();
        }
        return instance;
    }

    public Registo getRegisto(){
        return reg;
    }

    public void setRegisto(Registo registo){
        reg = registo;
        if (reg.isComplete()) {
            saveRegisto.println(reg.toString());
            regIndex++;
            reg = new Registo();
        }
    }
    public void startSaving() throws IOException {
        if (!saving) {
            checkExternalMedia();
            path = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            dir = new File(path.getAbsolutePath() + config.getFolder());
            dir.mkdirs();
            file = new File(dir, config.getFilename().concat(config.getExtencao()));
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            saveRegisto = new PrintWriter(bw);

            saveRegisto.println(reg.csvHeader());
        }
    }
    public void stopSaving() {
        if (saving){
            try {
                bw.flush();
                bw.close();
                saving = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            regIndex = 0;
        }
    }

    private void checkExternalMedia(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
            // TODO
        }
       // tv.append("\n\nExternal Media: readable="
       //         +mExternalStorageAvailable+" writable="+mExternalStorageWriteable);
    }

    public void enviarCSV(){
        String localFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/cubi1617/a21260825.dat";
       // String localFile = file.getAbsolutePath();
        Format formatter = new SimpleDateFormat("ddMMYYYYhhmmss");

        String remoteFile = config.getFilename().concat("_").concat(formatter.format(new Date().getTime())).concat(config.getExtencao());
       // String remoteFile ="ok";
        new Comunicacao().execute(config.getHost(),config.getUser(),config.getPassw(),localFile,remoteFile);
    }
}
