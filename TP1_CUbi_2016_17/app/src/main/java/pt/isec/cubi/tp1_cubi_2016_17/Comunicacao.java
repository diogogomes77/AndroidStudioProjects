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
            // If you need to display the progress of the upload, read how to do it in the end of the article

            // use the put method , if you are using android remember to remove "file://" and use only the relative path
            //sftp.put(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/cubi1617/a21260825.dat", "a21260825.dat");
            sftp.put(remoteFile);
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
