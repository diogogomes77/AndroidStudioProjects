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


public class Comunicacao extends AsyncTask<String, Integer, String> {

   /* private static Comunicacao instance = null;

    protected Comunicacao() {
    }

    public static Comunicacao getInstance() {
        if(instance == null) {
            instance = new Comunicacao();
        }
        return instance;
    }*/

    @Override
    protected String doInBackground(String... params) {
        try {
            String host = params[0];
            String user = params[1];
            String passwd = params[2];
            String localFile = params[3];
            String remoteFile = params[4];
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
        Log.d("PostExecuted",result);
    }
}
