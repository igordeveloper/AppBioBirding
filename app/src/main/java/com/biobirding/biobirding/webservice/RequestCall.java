package com.biobirding.biobirding.webservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.biobirding.biobirding.AppApplication;
import com.biobirding.biobirding.activity.LogoffActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestCall {

    private URL url;
    private String parameters;
    private HttpURLConnection con;
    private Context context;
    private SharedPreferences sharedPreferences;

    public RequestCall(){
        context = AppApplication.getAppContext();
        sharedPreferences = context.getSharedPreferences("bio", Context.MODE_PRIVATE);
    }

    public void setConRequestProperty(String name, String value){
        this.con.setRequestProperty(name, value);
    }

    public void setConRequestProperty(){

        String rg = sharedPreferences.getString("rg_bio", "");
        String password = sharedPreferences.getString("password_bio", "");
        try {
            String str = rg + "||" + password;
            byte[] authorization = str.getBytes("UTF-8");
            this.con.setRequestProperty("authorizationCode", Base64.encodeToString(authorization, Base64.NO_WRAP).trim());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void setParameters(String parameters){
        this.parameters = parameters;
    }

    public String getParameters(){
        return this.parameters;
    }

    public void setRoute(String route)  throws IOException{
        String DOMAIN = "http://206.81.1.173";
        this.url = new URL(DOMAIN + route);
    }

    public void setHttpURLConnection() throws IOException {
        this.con = (HttpURLConnection) this.url.openConnection();
        String METHOD = "POST";
        this.con.setRequestMethod(METHOD);
    }

    public JSONObject Response() throws IOException, JSONException, InterruptedException{
        this.con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(this.con.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
        writer.write(this.getParameters());
        writer.flush();
        writer.close();

        BufferedReader iny = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String output;
        StringBuilder response = new StringBuilder();

        while ((output = iny.readLine()) != null) {
            response.append(output);
        }
        iny.close();

        JSONObject json = new JSONObject(response.toString());

        if(json.has("authorized") && json.getString("authorized").equals("false")){
            Intent intent = new Intent();
            intent.setClass(context, LogoffActivity.class);
            context.startActivity(intent);
        }

        if (json.has("exception")) {
            throw new InterruptedException(json.getString("exception"));
        }

        return json;
    }
}