package pt.isec.cubi.tp1_cubi_2016_17;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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

    public RegistosController() {
        reg = new Registo();
        config = Configuracao.getInstance();
        regIndex = 0;
        saving = false;
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
        /*
        saveRegisto.println(reg.toString());
        regIndex++;
        reg = new Registo();
        */
        if (reg.isComplete()) {
            saveRegisto.println(reg.toString());
            regIndex++;
            reg = new Registo();
        }
    }
    public String startSaving() {
       // String result ="Trying to Save...\n";
        String result ="";
        if (!saving) {

           // result+=("Saving...\n");
            //result+=(checkExternalMedia());
            checkExternalMedia();
            path = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            dir = new File(path.getAbsolutePath() + config.getFolder());
            dir.mkdirs();
            file = new File(dir, config.getFilename()+(config.getExtencao()));
            try {


                fw = new FileWriter(file, true);
              //  result+=("ficheiro = "+file.getAbsolutePath()+"\n");
                saving = true;

                bw = new BufferedWriter(fw);
               /* bw.write(reg.csvHeader());
                bw.newLine();
                bw.flush();
                bw.close();*/

                saveRegisto = new PrintWriter(bw);
                saveRegisto.println(reg.csvHeader());


            } catch (IOException e) {
                e.printStackTrace();
                result+=("ERRO na criacao de ficheiro\n");
                return result;
            }

        }else {
            result+=("not Saving...\n");
        }
        return result;
    }

    public String stopSaving() {
       // String result ="Trying to stop saving\n";
        String result ="";
        if (saving){
         //   result+=("Stop Saving...\n");
            try {
               // bw.flush();
                bw.close();
                saving = false;
            //    result+=("Saving Stoped\n");
            } catch (IOException e) {
                e.printStackTrace();
                result+=("ERRO: Saving not Stoped\n");
            }
            regIndex = 0;
        }
        return result;
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
       //return "\n\nExternal Media: readable="
       //         +mExternalStorageAvailable+" writable="+mExternalStorageWriteable+"\n";
    }

    public void enviarRegistos(){
       String localFile = file.getAbsolutePath();
        Format formatter = new SimpleDateFormat("ddMMyyhhmmss");
       String remoteFile = config.getFilename().concat("_").concat(formatter.format(new Date().getTime())).concat(config.getExtencao());
        new Comunicacao(file).execute(config.getHost(),config.getUser(),config.getPassw(),localFile,remoteFile);
    }

}
