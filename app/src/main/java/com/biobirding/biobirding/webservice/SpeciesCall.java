package com.biobirding.biobirding.webservice;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SpeciesCall extends Call {

    public SpeciesCall(Context context) {
        super(context);
    }

    public JSONObject insert(String scientificName, String characteristics) throws IOException, JSONException {
        super.setRoute("/species/insert");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("scientificName="+scientificName+"&characteristics="+characteristics);
        return super.Response();
    }
}
