package com.biobirding.biobirding.webservice;

import com.biobirding.biobirding.entity.Species;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class LogCall extends RequestCall {


    public Long lastSpeciesUpdate() throws InterruptedException, IOException, JSONException{

        super.setRoute("/log/lastSpeciesUpdate");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("action=species");
        JSONObject json = super.Response();
        Long timestamp = Long.valueOf(json.getString("timestamp"));
        return timestamp;
    }

}
