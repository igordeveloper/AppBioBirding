package com.biobirding.biobirding.webservice;

import com.biobirding.biobirding.entity.Species;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public class SpeciesCall extends RequestCall {


    public Boolean insert(Species species) throws InterruptedException, IOException, JSONException{

        super.setRoute("/species/insert");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("scientificName="+species.getScientificName()+
                "&notes="+species.getNotes()+
                "&conservationState="+species.getConservationState());
        JSONObject json = super.Response();
        return json.getString("status").equals("true");
    }


    public ArrayList<Species> search(String scientificName) throws InterruptedException, IOException, JSONException{

        ArrayList<Species> listSpecies;

        super.setRoute("/species/search");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("scientificName="+scientificName);
        JSONObject json = super.Response();

        listSpecies = new ArrayList<>();
        JSONArray speciesCall = json.getJSONArray("species");
        for(int i = 0; i < speciesCall.length(); i++){
            JSONObject finalObject = speciesCall.getJSONObject(i);
            Species species = new Species();
            species.setId(Integer.parseInt(finalObject.getString("id")));
            species.setScientificName(finalObject.getString("scientificName"));
            listSpecies.add(species);
        }

        return listSpecies;
    }

    public Species select(Species selectSpecies) throws InterruptedException, IOException, JSONException{

        Species species = new Species();

        super.setRoute("/species/select");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("id=" + selectSpecies.getId());
        JSONObject json = super.Response();

        JSONObject fields = json.getJSONObject("species");
        species.setScientificName(fields.getString("scientificName"));
        species.setNotes(fields.getString("notes"));
        species.setConservationState(fields.getString("conservationState"));

        return species;
    }

    public Boolean update(Species species) throws InterruptedException, IOException, JSONException{

        JSONObject json;
        super.setRoute("/species/update");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("id="+species.getId()+
                "&scientificName="+species.getScientificName()+
                "&notes="+species.getNotes()+
                "&conservationState="+species.getConservationState());

        json = super.Response();
        return json.getString("status").equals("true");
    }
}
