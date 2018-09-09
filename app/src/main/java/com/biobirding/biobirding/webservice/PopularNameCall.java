package com.biobirding.biobirding.webservice;

import com.biobirding.biobirding.entity.Species;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;

public class PopularNameCall extends RequestCall {

    private Species species;

    public JSONObject insert(Species species) throws IOException, JSONException, InvalidKeyException {
        super.setRoute("/species/insert");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("scientificName="+species.getScientificName()+
                "&notes="+species.getNotes()+
                "&conservationState="+species.getConservationState());
        return super.Response();
    }

    public JSONObject search(String scientificName) throws IOException, JSONException, InvalidKeyException {
        super.setRoute("/species/search");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("scientificName="+scientificName);
        return super.Response();
    }

    public JSONObject selectAll(Species species) throws IOException, JSONException, InvalidKeyException {
        super.setRoute("/popularName/selectAll");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("species="+species.getId());
        return super.Response();
    }

    public JSONObject update(Species species) throws IOException, JSONException, InvalidKeyException {
        super.setRoute("/species/update");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("id="+species.getId()+
                "&scientificName="+species.getScientificName()+
                "&notes="+species.getNotes()+
                "&conservationState="+species.getConservationState());
        return super.Response();
    }
}
