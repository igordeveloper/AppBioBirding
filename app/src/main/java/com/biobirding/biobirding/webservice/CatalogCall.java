package com.biobirding.biobirding.webservice;

import android.database.DatabaseUtils;
import android.text.TextUtils;
import android.util.Log;

import com.biobirding.biobirding.entity.LocalCatalog;
import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.helper.CatalogHelper;
import com.biobirding.biobirding.helper.SearchHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class CatalogCall extends RequestCall {


    public Boolean insert(LocalCatalog catalog) throws InterruptedException, IOException, JSONException{

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

    public Boolean selectCount(LocalCatalog catalog) throws InterruptedException, IOException, JSONException{

        super.setRoute("/catalog/selectCount");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("rg="+catalog.getRg()+
                "&latitude="+catalog.getLatitude()+
                "&longitude="+catalog.getLongitude()+
                "&timestamp="+catalog.getTimestamp());
        JSONObject json = super.Response();
        return !json.getString("count").equals("0");
    }

    public ArrayList<String>selectStateGroup()throws InterruptedException, IOException, JSONException{

        ArrayList<String> list = new ArrayList<>();

        super.setRoute("/catalog/selectStateGroup");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("");
        JSONObject json = super.Response();

        JSONArray listCall = json.getJSONArray("list");
        for(int i = 0; i < listCall.length(); i++){
            JSONObject finalObject = listCall.getJSONObject(i);
            list.add(finalObject.getString("state"));
        }

        return list;
    }

    public ArrayList<String>selectCityGroup(String state)throws InterruptedException, IOException, JSONException{

        ArrayList<String> list = new ArrayList<>();

        super.setRoute("/catalog/selectCityGroup");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("state="+state);
        JSONObject json = super.Response();

        JSONArray listCall = json.getJSONArray("list");
        for(int i = 0; i < listCall.length(); i++){
            JSONObject finalObject = listCall.getJSONObject(i);
            list.add(finalObject.getString("city"));
        }

        return list;
    }


    public ArrayList<CatalogHelper> selectFilter(SearchHelper searchHelper)throws InterruptedException, IOException, JSONException{

        ArrayList<CatalogHelper> list = new ArrayList<>();

        super.setRoute("/catalog/selectFilter");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("state="+searchHelper.getState()+
                            "&city="+searchHelper.getCity()+
                            "&species="+searchHelper.getSpeciesName()+
                            "&rg="+searchHelper.getUser().getRg()+
                            "&accessLevel="+searchHelper.getUser().getAccessLevel()+
                            "&startDate="+searchHelper.getStartDate()+
                            "&finishDate="+searchHelper.getFinishDate()+
                            "&identificationCode="+searchHelper.getIdentificationCode());
        JSONObject json = super.Response();

        JSONArray listCall = json.getJSONArray("list");
        for(int i = 0; i < listCall.length(); i++){
            JSONObject finalObject = listCall.getJSONObject(i);
            CatalogHelper catalogHelper = new CatalogHelper();
            catalogHelper.setCity(finalObject.getString("city"));
            catalogHelper.setState(finalObject.getString("state"));
            catalogHelper.setSpecies(finalObject.getString("species"));
            catalogHelper.setDate(finalObject.getString("date"));
            catalogHelper.setAge(finalObject.getString("age"));
            catalogHelper.setSex(finalObject.getString("sex"));
            catalogHelper.setId(Integer.valueOf(finalObject.getString("id")));
            list.add(catalogHelper);
        }

        return list;
    }


    public CatalogHelper select(Integer id) throws InterruptedException, IOException, JSONException{

        CatalogHelper catalogHelper = new CatalogHelper();

        super.setRoute("/catalog/select");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("id="+id);
        JSONObject json = super.Response();

        JSONObject fields = json.getJSONObject("catalog");
        catalogHelper.setId(Integer.parseInt(fields.getString("id")));
        catalogHelper.setIdSpecies(Integer.parseInt(fields.getString("species")));
        catalogHelper.setLatitude(Double.parseDouble(fields.getString("latitude")));
        catalogHelper.setLongitude(Double.parseDouble(fields.getString("longitude")));
        catalogHelper.setAge(fields.getString("age"));
        catalogHelper.setSex(fields.getString("sex"));
        catalogHelper.setTemperature(Double.parseDouble(fields.getString("temperature")));
        catalogHelper.setHumidity(Double.parseDouble(fields.getString("humidity")));
        catalogHelper.setWind(Double.parseDouble(fields.getString("wind")));
        catalogHelper.setWeather(fields.getString("weather"));
        catalogHelper.setNotes(fields.getString("notes"));
        catalogHelper.setIdentificationCode(fields.getString("identificationCode"));
        catalogHelper.setNeighborhood(fields.getString("neighborhood"));
        catalogHelper.setCity(fields.getString("city"));
        catalogHelper.setState(fields.getString("state"));

        return catalogHelper;
    }

    public Boolean update(CatalogHelper  catalogHelper) throws InterruptedException, IOException, JSONException {

        super.setRoute("/catalog/update");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("id="+catalogHelper.getId()+
                "&species="+catalogHelper.getIdSpecies()+
                "&age="+catalogHelper.getAge()+
                "&sex="+catalogHelper.getSex()+
                "&latitude="+catalogHelper.getLatitude()+
                "&longitude="+catalogHelper.getLongitude()+
                "&temperature="+catalogHelper.getTemperature()+
                "&humidity="+catalogHelper.getHumidity()+
                "&windSpeed="+catalogHelper.getWind()+
                "&weather="+catalogHelper.getWeather()+
                "&notes="+ URLEncoder.encode(catalogHelper.getNotes(), "UTF-8")+
                "&identificationCode="+catalogHelper.getIdentificationCode()+
                "&neighborhood="+catalogHelper.getNeighborhood()+
                "&city="+catalogHelper.getCity()+
                "&state="+catalogHelper.getState());
        JSONObject json = super.Response();

        return json.getString("status").equals("true");
    }


    public Boolean delete(CatalogHelper catalogHelper) throws InterruptedException, IOException, JSONException {

        super.setRoute("/catalog/delete");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("id="+catalogHelper.getId());
        JSONObject json = super.Response();

        if (json.has("exception")) {
            throw new InterruptedException(json.getString("exception"));
        }

        return json.getString("status").equals("true");

    }


}
