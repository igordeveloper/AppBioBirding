package com.biobirding.biobirding.webservice;

import android.util.Log;

import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

public class PopularNameCall extends RequestCall{

    private Species species;

    public Boolean insert(PopularName  popularName) throws InterruptedException, IOException, JSONException {

        super.setRoute("/popularName/insert");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("name="+popularName.getName()+
                "&species="+popularName.getId());
        JSONObject json = super.Response();

        if (json.has("exception")) {
            throw new InterruptedException(json.getString("exception"));
        }

        return json.getString("status") == "true";

    }



    public ArrayList<PopularName> selectAllFromSpecies(Species species) throws InterruptedException, IOException, JSONException {

        ArrayList<PopularName> popularNames = new ArrayList<>();

        super.setRoute("/popularName/selectAllFromSpecies");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("species="+species.getId());
        JSONObject json = super.Response();

        if (json.has("exception")) {
            throw new InterruptedException(json.getString("message"));
        }

        JSONArray popularNameCall = json.getJSONArray("popularNames");
        for(int i = 0; i < popularNameCall.length(); i++){
            JSONObject finalObject = popularNameCall.getJSONObject(i);
            PopularName popularName = new PopularName();
            popularName.setId(Integer.parseInt(finalObject.getString("species")));
            popularName.setName(finalObject.getString("name"));
            popularNames.add(popularName);
        }

        return popularNames;
    }

    public ArrayList<PopularName> selectAll() throws IOException, JSONException, InterruptedException {

        super.setRoute("/popularName/selectAll");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("");

        JSONObject json = super.Response();

        ArrayList<PopularName> listPopularNames = new ArrayList<>();
        JSONArray popularNamesCall = json.getJSONArray("popularNames");
        for(int i = 0; i < popularNamesCall.length(); i++){
            JSONObject finalObject = popularNamesCall.getJSONObject(i);
            PopularName popularName = new PopularName();
            popularName.setName(finalObject.getString("name"));
            popularName.setId(Integer.parseInt(finalObject.getString("species")));
            listPopularNames.add(popularName);
        }

        return listPopularNames;
    }

    public Boolean update(PopularName  popularName, String newName) throws InterruptedException, IOException, JSONException {

        super.setRoute("/popularName/update");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("name="+popularName.getName()+
                            "&species="+popularName.getId()+
                            "&newName="+newName);
        JSONObject json = super.Response();

        return json.getString("status") == "true";
    }

    public Boolean delete(PopularName  popularName) throws InterruptedException, IOException, JSONException {

        super.setRoute("/popularName/delete");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("name="+popularName.getName()+
                "&species="+popularName.getId());
        JSONObject json = super.Response();

        if (json.has("exception")) {
            throw new InterruptedException(json.getString("exception"));
        }

        return json.getString("status") == "true";

    }
}
