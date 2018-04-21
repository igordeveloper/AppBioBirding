package com.biobirding.biobirding;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class GpsActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);
        client.flushLocations();


        Button button = findViewById(R.id.getLocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(GpsActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                    return;
                }
                
                client.getLastLocation().addOnSuccessListener(GpsActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            TextView latitude = findViewById(R.id.latitude);
                            TextView longitude = findViewById(R.id.longitude);

                            latitude.setText(String.valueOf(location.getLatitude()));
                            longitude.setText(String.valueOf(location.getLongitude()));

                            requestWeather(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
            }
        });

    }

    private void requestWeather(Double latitude, Double longitude) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Long now = System.currentTimeMillis()/1000;

        try {
            URL url = new URL("https://api.darksky.net/forecast/8987f4fc7f91d7ecd89cf9df78444281/"
                    +latitude+"," +
                    ""+longitude+", " +
                    ""+now.toString()+"" +
                    "?exclude=hourly,flags,alerts&lang=pt&units=si");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());
            JSONObject currently = (JSONObject) json.get("currently");

            TextView temperture = findViewById(R.id.temperature);
            temperture.setText(String.valueOf(currently.getString("temperature") + " °C"));

            TextView humidity = findViewById(R.id.humidity);
            Double h = Double.parseDouble(currently.getString("humidity"));
            h = h * 100;
            humidity.setText(String.valueOf(h.toString() + " %"));

            TextView windSpeed = findViewById(R.id.wind);
            windSpeed.setText(String.valueOf(currently.getString("windSpeed") + " m/s"));

            TextView resume = findViewById(R.id.resume);
            resume.setText(String.valueOf(currently.getString("summary")));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1 );
    }
}
