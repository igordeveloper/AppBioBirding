package com.biobirding.biobirding.webservice;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SpeciesCall extends Call {

    public SpeciesCall(Context context) {
        super(context);
    }

    public JSONObject insert(String scientificName, String notes, String conservationState) throws IOException, JSONException {
        super.setRoute("/species/insert");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("scientificName="+scientificName+"&notes="+notes+"&conservationState="+conservationState);
        return super.Response();
    }

    public JSONObject search(String scientificName) throws IOException, JSONException {
        super.setRoute("/species/search");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("scientificName="+scientificName);
        return super.Response();
    }

    public JSONObject select(String scientificName) throws IOException, JSONException {
        super.setRoute("/species/select");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("scientificName="+scientificName);
        return super.Response();
    }

    public JSONObject update(String scientificName, String newScientificName, String notes, String conservationState) throws IOException, JSONException {
        super.setRoute("/species/update");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("scientificName="+scientificName+
                                "&newScientificName="+newScientificName+
                                "&notes="+notes+"&conservationState="+conservationState);
        return super.Response();
    }
}
