package com.biobirding.biobirding.webservice;

import com.biobirding.biobirding.entity.Catalog;
import com.biobirding.biobirding.entity.Species;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CatalogCall extends RequestCall {


    public Boolean insert(Catalog catalog) throws InterruptedException, IOException, JSONException{

        super.setRoute("/catalog/insert");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("rg="+catalog.getRg()+
                "&latitude="+catalog.getLatitude()+
                "&longitude="+catalog.getLongitude()+
                "&timestamp="+catalog.getTimestamp()+
                "&age="+catalog.getAge()+
                "&sex="+catalog.getSex()+
                "&species="+catalog.getSpecies()+
                "&notes="+catalog.getNotes()+
                "&identificationCode="+catalog.getIdentificationCode());
        JSONObject json = super.Response();
        return json.getString("status").equals("true");
    }

    public Boolean selectCount(Catalog catalog) throws InterruptedException, IOException, JSONException{

        super.setRoute("/catalog/selectCount");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("rg="+catalog.getRg()+
                "&latitude="+catalog.getLatitude()+
                "&longitude="+catalog.getLongitude()+
                "&timestamp="+catalog.getTimestamp());
        JSONObject json = super.Response();

        if(json.getString("count").equals("0")){
            return false;
        }else{
            return true;
        }
    }
}
