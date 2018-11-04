package com.biobirding.biobirding.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ReportCall extends RequestCall{

    public Boolean send(String rg, String startDate, String finishDate) throws InterruptedException, IOException, JSONException{

        super.setRoute("/report/send");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("rg="+rg+
                "&startDate="+startDate+
                "&finishDate="+finishDate);
        JSONObject json = super.Response();
        return json.getString("status").equals("true");

    }
}
