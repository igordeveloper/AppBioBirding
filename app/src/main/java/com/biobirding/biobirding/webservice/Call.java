package com.biobirding.biobirding.webservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

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
import java.security.NoSuchAlgorithmException;

import utils.HashPassword;

public class Call {

    private final String DOMAIN = "http://206.81.1.173";
    private final String METHOD = "POST";
    private URL url;
    private String paramaters;
    private HttpURLConnection con;
    private Context context;

    public Call(Context context){
        this.context = context;
    }

    public void setConRequestProperty(String name, String value){
        this.con.setRequestProperty(name, value);
    }

    public void setConRequestProperty(){
        SharedPreferences sharedPref = this.context.getSharedPreferences("bio", Context.MODE_PRIVATE);
        String nickname = sharedPref.getString("nickname_bio", "");
        String password = sharedPref.getString("password_bio", "");
        HashPassword hash = new HashPassword();
        try {
            String str = nickname + "||" + hash.encode256(password);
            byte[] authorization = str.getBytes("UTF-8");
            this.con.setRequestProperty("authorizationCode", Base64.encodeToString(authorization, Base64.NO_WRAP).trim());
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }



    public void setParameters(String paramaters){
        this.paramaters = paramaters;
    }

    public String getParameters(){
        return this.paramaters;
    }

    public void setRoute(String route)  throws IOException{
        this.url = new URL(DOMAIN + route);
    }

    public void setHttpURLConnection() throws IOException {
        this.con = (HttpURLConnection) this.url.openConnection();
        this.con.setRequestMethod(METHOD);
    }

    public JSONObject Response() throws IOException, JSONException {
        this.con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(this.con.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
        writer.write(this.getParameters());
        writer.flush();
        writer.close();

        BufferedReader iny = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = iny.readLine()) != null) {
            response.append(output);
        }
        iny.close();

        JSONObject json = new JSONObject(response.toString());
        return json;
    }
}
