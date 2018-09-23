package com.biobirding.biobirding.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class TempGpsActivity extends BaseActivity {

    private FusedLocationProviderClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_gps);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPref = getSharedPreferences("bio", Context.MODE_PRIVATE);
        String nickname_bio = sharedPref.getString("nickname_bio", "123");
        Log.d("log", String.valueOf(nickname_bio).toString());
        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);
        client.flushLocations();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.getLastLocation().addOnSuccessListener(TempGpsActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    Log.d("latitude", String.valueOf(location.getLatitude()));
                    Log.d("longitude", String.valueOf(location.getLongitude()));

                    //TextView latitude = findViewById(R.id.latitude);
                    //TextView longitude = findViewById(R.id.longitude);

                    //latitude.setText(String.valueOf(location.getLatitude()));
                    //longitude.setText(String.valueOf(location.getLongitude()));

                    //requestWeather(location.getLatitude(), location.getLongitude());
                }
            }
        });
        /*Button button = findViewById(R.id.get);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(TempGpsActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                    return;
                }
                

            }
        });*/

    }

    /*private void requestWeather(Double latitude, Double longitude) {

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

    }*/

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1 );
    }
}
