package com.biobirding.biobirding.webservice;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class Login extends Call {

    public Login(Context context) {
        super(context);
    }

    public JSONObject check(String nickname, String password) throws IOException, JSONException {
        super.setRoute("/user/validate");
        super.setHttpURLConnection();
        super.setConRequestProperty("nickname", nickname);
        super.setConRequestProperty("password", password);
        super.setParameters("");
        return super.Response();
    }
}
