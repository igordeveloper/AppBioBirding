package webservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Call {

    private final String DOMAIN = "http://206.81.1.173";
    private final String METHOD = "POST";
    private URL url;
    private String paramaters;
    private HttpURLConnection con;

    public void setConRequestProperty(String name, String value){
        this.con.setRequestProperty(name, value);
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
        wr.writeBytes(this.getParameters());
        wr.flush();
        wr.close();

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
