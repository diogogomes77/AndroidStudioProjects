package pt.isec.cubi.tp1_cubi_2016_17;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class Comunicacao extends AsyncTask<Void, Integer, String> {


    private File file;
    private Configuracao configz;

    public Comunicacao(File file,Configuracao config) {
        this.file = file;
        this.configz = config;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            String host = configz.getHost();
            String user = configz.getUser();
            String passwd = configz.getPassw();
            String localFile = file.getAbsolutePath();
            String remoteFile = configz.getRemoteFile();
            JSch ssh = new JSch();
            Session session = ssh.getSession(user, host, 22);
            // Remember that this is just for testing and we need a quick access, you can add an identity and known_hosts file to prevent
            // Man In the Middle attacks
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(passwd);

            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;

           // sftp.cd("data");
            sftp.put(localFile,remoteFile);
            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } catch (SftpException e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        }
        return "Terminado";
    }
    @Override
    protected void onPostExecute(String result) {
       PrintWriter pw = null;
        try {
            pw = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            // TODO
        }
        pw.close();

        Log.d("PostExecuted",result);
    }
}
