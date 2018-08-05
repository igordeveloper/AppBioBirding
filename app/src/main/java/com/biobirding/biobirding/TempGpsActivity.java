package com.biobirding.biobirding;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class TempGpsActivity extends BaseActivity {

    private FusedLocationProviderClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_gps);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPref = getSharedPreferences("bio", Context.MODE_PRIVATE);
        String nickname_bio = sharedPref.getString("nickname_bio", "123");
        Log.d("log", String.valueOf(nickname_bio).toString());
        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);
        client.flushLocations();


        /*Button button = findViewById(R.id.getLocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(TempGpsActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                    return;
                }
                
                client.getLastLocation().addOnSuccessListener(TempGpsActivity.this, new OnSuccessListener<Location>() {
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
