package pt.isec.cubi.tp1_cubi_2016_17;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;



public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 98;
    public TextView tv;
    public TextView tvlat;
    public TextView tvlon;
    public TextView tvalt;
    public TextView tvacc;
    public TextView tvgyr;
    public TextView tvlum;
    public static final int REQUEST_WRITE_STORAGE = 112;
    private String filename = "a21260825.dat";
    private  File path;
    private File dir;
    private String folder = "/cubi1617";
    public File file;
    private RadioButton actividade1,actividade2,actividade3;
    private String host = "kenobi.dei.uc.pt";
    private String user = "cubistudent";
    private String passw = "mis_cub_2017";

    //Sensores
    LocationListener locationListener;
    LocationManager locationManager;
    private SensorManager mSensorManager;
    private Sensor acel;
    private Sensor gyro;
    private Sensor lumi;
    private SensorEventListener listenerAcel;
    private SensorEventListener listenerGyro;
    private SensorEventListener listenerLumi;

    //Guarda valor atual do sensor para comparação com o seguinte
    double[] lat;
    double[] lon;
    double[] alt;
    float xAcc;
    float yAcc;
    float zAcc;
    float xGyro;
    float yGyro;
    float zGyro;
    float luminosidade;

    Date[] d;

    //Variáveis dos Radio Buttons
    RadioButton andar;
    RadioButton correr;
    RadioButton subir;
    RadioButton descer;
    RadioButton conduzir;

    String movimento;
    String Angulo;

    private long lastUpdate = 0;
    private long lastUpdateGyro = 0;
    private long lastUpdateLumi = 0;
    float[] gravity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        acel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        lumi = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        andar = (RadioButton) findViewById(R.id.Andar);
        correr = (RadioButton) findViewById(R.id.Correr);
        subir = (RadioButton) findViewById(R.id.SubirEscada);
        descer = (RadioButton) findViewById(R.id.DescerEscada);
        conduzir = (RadioButton) findViewById(R.id.Conduzir);

        tvlat = (TextView) findViewById(R.id.textViewLat);
        tvlon = (TextView) findViewById(R.id.textViewLong);
        tvalt = (TextView) findViewById(R.id.textViewAlt);
        tvacc = (TextView) findViewById(R.id.textViewAcc);
        tvgyr = (TextView) findViewById(R.id.textViewGyro);
        tvlum = (TextView) findViewById(R.id.textViewLum);

        tvlat.setText("");
        tvlon.setText("");
        tvalt.setText("");
        tvacc.setText("");
        tvgyr.setText("");
        tvlum.setText("");

        andar.setClickable(false);
        correr.setClickable(false);
        subir.setClickable(false);
        descer.setClickable(false);
        conduzir.setClickable(false);

        lat = new double[5];
        lon = new double[5];
        alt = new double[5];

        d = new Date[2];
        d[0] = new Date();

        gravity = new float[]{0, 0, 0};

        movimento="Andar";
        Angulo="Plano";

        analisar();

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                insere(location.getLatitude(),location.getLongitude(),location.getAltitude());
                tvlat.setText("Latitude: " + lat[0]);
                tvlon.setText("Longitude: " + lon[0]);
                tvlat.setText("Altitude: " + alt[0]);
                if(alt[0]>alt[1] && alt[1]>alt[2] && alt[2]>alt[3] && alt[3]>alt[4]){
                    Angulo="Subir";
                }
                else if(alt[0]<alt[1] && alt[1]<alt[2] && alt[2]<alt[3] && alt[3]<alt[4]){
                    Angulo="Descer";
                }
                else{
                    Angulo="Plano";
                }

                calculate();

                analisar();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        listenerAcel = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                final float alpha = 0.8f;
                long curTime = System.currentTimeMillis();

                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                if ((curTime - lastUpdate) > 1000) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    xAcc = event.values[0] - gravity[0];;
                    yAcc = event.values[1] - gravity[1];
                    zAcc = event.values[2] - gravity[2];

                    tvacc.setText("X: " + xAcc + " Y: " + yAcc + " Z: " + zAcc);
                }

                analisar();
            }
        };

        listenerGyro = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {

                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdateGyro) > 1000) {
                    long diffTime = (curTime - lastUpdateGyro);
                    lastUpdateGyro = curTime;

                    xGyro = event.values[0];
                    yGyro = event.values[1];
                    zGyro = event.values[2];
                    tvgyr.setText("Giroscopio X: " + xGyro + " Y: " + yGyro + " Z: " + zGyro);
                }

            }
        };

        listenerLumi = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {

                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdateLumi) > 1000) {
                    long diffTime = (curTime - lastUpdateLumi);
                    lastUpdateLumi = curTime;

                    luminosidade = event.values[0];
                    tvlum.setText("Luminosidade: " + luminosidade);
                }
            }
        };
    }

    public void comecar(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        mSensorManager.registerListener(listenerAcel, acel, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(listenerGyro, gyro, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(listenerLumi, lumi, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void parar(View view) {
        locationManager.removeUpdates(locationListener);
        mSensorManager.unregisterListener(listenerAcel);
        mSensorManager.unregisterListener(listenerGyro);
        mSensorManager.unregisterListener(listenerLumi);
    }

    public void calculate(){
        final int R = 6371;

        double latDistance = Math.toRadians(lat[1] - lat[0]);
        double lonDistance = Math.toRadians(lon[1] - lon[0]);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat[0])) * Math.cos(Math.toRadians(lat[1]))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;

        double height = alt[0] - alt[1];

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        distance = Math.sqrt(distance);

        double distanceBySeconds = distance;

        if(distance <= 6){
            movimento = "Andar";
        }
        else if(distance > 6 && distanceBySeconds <= 10) {
            movimento = "Correr";
        }
        else if(distance > 10) {
            movimento = "Conduzir";
        }
    }

    public void insere(double la,double lo,double al){
        lat[4]=lat[3];
        lat[3]=lat[2];
        lat[2]=lat[1];
        lat[1]=lat[0];
        lat[0]=la;

        lon[4]=lon[3];
        lon[3]=lon[2];
        lon[2]=lon[1];
        lon[1]=lon[0];
        lon[0]=lo;

        alt[4]=alt[3];
        alt[3]=alt[2];
        alt[2]=alt[1];
        alt[1]=alt[0];
        alt[0]=al;

        d[1]=d[0];
        d[0]=new Date();
    }

    public void analisar(){
        if(Angulo.equals("Subir") && movimento.equals("Andar")){
            andar.setChecked(false);
            correr.setChecked(false);
            subir.setChecked(true);
            descer.setChecked(false);
            conduzir.setChecked(false);
        }
        if(Angulo.equals("Descer") && movimento.equals("Andar")){
            andar.setChecked(false);
            correr.setChecked(false);
            subir.setChecked(false);
            descer.setChecked(true);
            conduzir.setChecked(false);
        }
        if(Angulo.equals("Plano") && movimento.equals("Andar")){
            andar.setChecked(true);
            correr.setChecked(false);
            subir.setChecked(false);
            descer.setChecked(false);
            conduzir.setChecked(false);
        }
        if(movimento.equals("Correr")){
            andar.setChecked(false);
            correr.setChecked(true);
            subir.setChecked(false);
            descer.setChecked(false);
            conduzir.setChecked(false);
        }
        if(Angulo.equals("Conduzir")){
            andar.setChecked(false);
            correr.setChecked(false);
            subir.setChecked(false);
            descer.setChecked(false);
            conduzir.setChecked(true);
        }
    }

    public void saveFile(View button){
        if (requestFilePermission(MainActivity.this)){
            checkExternalMedia();
            String txt="teste ficheiro escrita";
            if (toFile(txt))
                tv.append("\n\nFile written ");
            else
                tv.append("\n\nProblem: No File written ");
        }
    }
    private boolean toFile(String message){
        checkExternalMedia();
        path = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        tv.append("\nExternal file system root: "+path);
        dir = new File(path.getAbsolutePath() + folder);
        dir.mkdirs();
        file = new File(dir, filename);

        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(message);
            bw.newLine();
            bw.flush();
            bw.close();
            //editText.setText(" ");
            tv.append("\n\nFile written to "+file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       /* try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println("Hi , How are you");
            pw.println("Hello");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            tv.append("******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        tv.append("\n\nFile written to "+file);*/
        return true;
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
        }
        tv.append("\n\nExternal Media: readable="
                +mExternalStorageAvailable+" writable="+mExternalStorageWriteable);
    }
    private boolean requestFilePermission(Activity context) {
        boolean hasPermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        } else {
            // You are allowed to write external storage:
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/new_folder";
            File storageDir = new File(path);
            if (!storageDir.exists() && !storageDir.mkdirs()) {
                // This should never happen - log handled exception!
            }
        }
        return hasPermission;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] ==  PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "The app was allowed to write to your storage!", Toast.LENGTH_LONG).show();
                    // Reload the activity with permission granted or use the features what required the permission
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Permission was granted, do your thing!
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void uploadFile(View button) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                }
                permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED)
                    return;
            }
        }
        new LongOperation().execute();
    }

    private class LongOperation extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                JSch ssh = new JSch();
                Session session = ssh.getSession("cubistudent", "kenobi.dei.uc.pt", 22);
                // Remember that this is just for testing and we need a quick access, you can add an identity and known_hosts file to prevent
                // Man In the Middle attacks
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.setPassword("mis_cub_2017");

                session.connect();
                Channel channel = session.openChannel("sftp");
                channel.connect();

                ChannelSftp sftp = (ChannelSftp) channel;

                sftp.cd("data");
                // If you need to display the progress of the upload, read how to do it in the end of the article

                // use the put method , if you are using android remember to remove "file://" and use only the relative path
                sftp.put(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/cubi1617/a21260825.dat", "a21260825.dat");
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
}
